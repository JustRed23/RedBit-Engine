package dev.JustRed23.redbit.mesh;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final int vertices;

    private final int vaoId;
    private final List<Integer> vboIdList;

    public Mesh(float[] pos, int vertices) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            this.vertices = vertices;
            this.vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer posBuffer = stack.mallocFloat(pos.length);
            posBuffer.put(0, pos);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    public void cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }

    public int getVertices() {
        return vertices;
    }

    public int getVaoId() {
        return vaoId;
    }
}
