package dev.JustRed23.redbit.engine.utils;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public final class GLUtils {

    public static int createVBO(int attribute, int attributeSize, float[] data) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufUtils.create(data), GL_STATIC_DRAW);
        glVertexAttribPointer(attribute, attributeSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vbo;
    }

    public static int createIBO(int[] indices) {
        int ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufUtils.create(indices), GL_STATIC_DRAW);
        return ibo;
    }
}
