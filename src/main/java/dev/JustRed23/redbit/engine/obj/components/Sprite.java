package dev.JustRed23.redbit.engine.obj.components;

import dev.JustRed23.redbit.engine.render.Texture;
import org.joml.Vector2f;

public final class Sprite {

    private final Texture texture;
    private final Vector2f[] textureCoords;

    public Sprite(Texture texture, Vector2f[] textureCoords) {
        this.texture = texture;
        this.textureCoords = textureCoords;
    }

    public Sprite(Texture texture) {
        this(texture, new Vector2f[] {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        });
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTextureCoords() {
        return textureCoords;
    }
}
