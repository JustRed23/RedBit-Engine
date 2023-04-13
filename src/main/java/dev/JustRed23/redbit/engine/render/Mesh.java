package dev.JustRed23.redbit.engine.render;

import dev.JustRed23.redbit.engine.utils.GLUtils;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL30.*;

public abstract class Mesh {

    private final int vaoId;
    private final List<Integer> vbos = new ArrayList<>();

    private final int verticesCount;

    protected Mesh(float[] vertices, int[] indices, Function<List<Integer>, Void> vboCreateFunction) {
        this.vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vbos.add(GLUtils.createVBO(0, 3, vertices));

        vboCreateFunction.apply(vbos);

        vbos.add(GLUtils.createIBO(indices));
        verticesCount = indices.length;

        glBindVertexArray(0);
    }

    public final void render(ShaderProgram shaderProgram) {
        shaderProgram.bind();
        glBindVertexArray(vaoId);

        for (int i = 0; i < getAttributeCount(); i++)
            glEnableVertexAttribArray(i);

        glDrawElements(GL_TRIANGLES, verticesCount, GL_UNSIGNED_INT, 0);

        for (int i = 0; i < getAttributeCount(); i++)
            glDisableVertexAttribArray(i);

        glBindVertexArray(0);
        shaderProgram.unbind();
    }

    public void cleanup() {
        vbos.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }

    public final int getAttributeCount() {
        return vbos.size() - 1;
    }

    public int getVerticesCount() {
        return verticesCount;
    }

    public final int getVaoId() {
        return vaoId;
    }
}
