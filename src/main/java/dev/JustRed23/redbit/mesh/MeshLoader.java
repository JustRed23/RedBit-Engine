package dev.JustRed23.redbit.mesh;

import dev.JustRed23.redbit.utils.BufUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL30;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class MeshLoader {

    @Contract("_, _ -> new")
    public static @NotNull Mesh createMesh(float[] vertices, int[] indices) {
        int vaoID = createVAO();
        List<Integer> vbos = loadToVAO(vertices, null, indices);
        unbindVAO();
        return new Mesh(vaoID, vbos, indices.length, vbos.size() - 1);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull TexturedMesh createTexturedMesh(float[] vertices, float[] textureCoords, int[] indices, String texturePath) throws FileNotFoundException {
        int vaoID = createVAO();
        List<Integer> vbos = loadToVAO(vertices, textureCoords, indices);
        unbindVAO();
        return new TexturedMesh(vaoID, vbos, indices.length, vbos.size() - 1, texturePath);
    }

    public static void cleanup(@NotNull Mesh mesh) {
        mesh.vbos().forEach(GL30::glDeleteBuffers);
        GL30.glDeleteVertexArrays(mesh.vaoID());
    }

    private static @NotNull List<Integer> loadToVAO(float[] positions, float[] textureCoords, int[] indices) {
        List<Integer> vbos = new ArrayList<>();
        storeDataInAttributeList(0, 3, positions, vbos);
        if (textureCoords != null)
            storeDataInAttributeList(1, 2, textureCoords, vbos);
        bindIndicesBuffer(indices, vbos);
        return vbos;
    }

    private static int createVAO() {
        int vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        return vaoID;
    }

    private static void unbindVAO() {
        glBindVertexArray(0);
    }

    private static void storeDataInAttributeList(int attributeNumber, int dimensions, float[] data, @NotNull List<Integer> vbos) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, BufUtils.create(data), GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, dimensions, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static void updateDataInAttributeList(int attributeNumber, int dimensions, float[] data, @NotNull Mesh mesh) {
        glBindVertexArray(mesh.vaoID());
        glBindBuffer(GL_ARRAY_BUFFER, mesh.vbos().get(attributeNumber));
        glBufferData(GL_ARRAY_BUFFER, BufUtils.create(data), GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, dimensions, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        unbindVAO();
    }

    private static void bindIndicesBuffer(int[] indices, @NotNull List<Integer> vbos) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufUtils.create(indices), GL_STATIC_DRAW);
    }
}
