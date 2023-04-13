package dev.JustRed23.redbit.engine.render;

import dev.JustRed23.redbit.engine.utils.GLUtils;
import dev.JustRed23.redbit.engine.utils.TextureUtils;

import java.io.FileNotFoundException;

public class TexturedMesh extends Mesh {

    private int textureID;

    public TexturedMesh(float[] vertices, int[] indices, float[] textureCoords, String texturePath) throws FileNotFoundException {
        super(vertices, indices, vbo -> {
            vbo.add(GLUtils.createVBO(1, 2, textureCoords));
            return null;
        });
        this.textureID = TextureUtils.load(texturePath);
    }

    public int getTextureID() {
        return textureID;
    }
}
