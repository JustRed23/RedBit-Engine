package dev.JustRed23.redbit.engine.render;

import dev.JustRed23.redbit.engine.err.UniformException;
import dev.JustRed23.redbit.engine.obj.components.MeshRenderer;
import dev.JustRed23.redbit.engine.utils.ResourcePool;
import dev.JustRed23.redbit.engine.window.View;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4f;

import java.io.FileNotFoundException;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class BatchRenderer {

    /* COMMON */
    private final int POSITION_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXTURE_SIZE = 0; //TODO: 2

    private final int POSITION_OFFSET = 0;
    private final int COLOR_OFFSET = (POSITION_OFFSET + POSITION_SIZE) * Float.BYTES;
    //TODO: private final int TEXTURE_OFFSET = (COLOR_OFFSET + COLOR_SIZE) * Float.BYTES;

    private final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE + TEXTURE_SIZE;
    private final int VERTEX_BYTES = VERTEX_SIZE * Float.BYTES;
    /* COMMON */

    private final ShaderProgram shader;
    private final View view;
    private final int maxMeshes;

    private MeshRenderer[] renderers;
    private int totalMeshes;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoId, vboId;

    public BatchRenderer(View view, int maxSize, ShaderProgram shader) {
        this.maxMeshes = maxSize;
        this.shader = shader;
        this.view = view;
        this.renderers = new MeshRenderer[maxSize];
        this.vertices = new float[maxSize * 4 * VERTEX_SIZE];
        this.totalMeshes = 0;
        this.hasRoom = true;
    }

    public void start() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboid = glGenBuffers();
        int[] indices = genIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboid);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, VERTEX_BYTES, POSITION_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 4, GL_FLOAT, false, VERTEX_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        //glVertexAttribPointer(2, 2, GL_FLOAT, false, VERTEX_BYTES, TEXTURE_OFFSET);
        //glEnableVertexAttribArray(2);
    }

    public void addMesh(MeshRenderer renderer) {
        int index = this.totalMeshes;
        renderers[index] = renderer;
        this.totalMeshes++;

        loadVertexData(index);

        if (this.totalMeshes >= this.maxMeshes)
            this.hasRoom = false;
    }

    public void render() throws UniformException {
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.bind();
        shader.set("uProjectionMatrix", view.getCamera().getProjectionMatrix());
        shader.set("uViewMatrix", view.getCamera().getViewMatrix());

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        //glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, totalMeshes * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        //glDisableVertexAttribArray(2);

        glBindVertexArray(0);

        shader.unbind();
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        //glDisableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);

        shader.cleanup();
    }

    private void loadVertexData(int index) {
        MeshRenderer renderer = this.renderers[index];

        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = renderer.getColor();

        float xAdd = 1, yAdd = 1;
        for (int i = 0; i < 4; i++) {
            if (i == 1)
                yAdd = 0;
            else if (i == 2)
                xAdd = 0;
            else if (i == 3)
                yAdd = 1;

            vertices[offset] = renderer.parent.transform.position.x + (xAdd * renderer.parent.transform.scale.x);
            vertices[offset + 1] = renderer.parent.transform.position.y + (yAdd * renderer.parent.transform.scale.y);

            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }
    }

    private int @NotNull [] genIndices() {
        int[] indices = new int[maxMeshes * 6];
        for (int i = 0; i < maxMeshes; i++)
            loadElementIndices(indices, i);
        return indices;
    }

    private void loadElementIndices(int @NotNull [] elements, int index) {
        int indexOffset = index * 6;
        int offset = index * 4;

        //triangle 1
        elements[indexOffset] = offset + 3;
        elements[indexOffset + 1] = offset + 2;
        elements[indexOffset + 2] = offset;

        //triangle 2
        elements[indexOffset + 3] = offset;
        elements[indexOffset + 4] = offset + 2;
        elements[indexOffset + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return hasRoom;
    }
}
