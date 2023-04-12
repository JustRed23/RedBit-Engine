package dev.JustRed23.redbit.engine.render;

import dev.JustRed23.redbit.engine.utils.GLUtils;

public class ColoredMesh extends Mesh {

    public ColoredMesh(float[] vertices, int[] indices, float[] colors) {
        super(vertices, indices, vbo -> {
            vbo.add(GLUtils.createVBO(1, 4, colors));
            return null;
        });
    }
}
