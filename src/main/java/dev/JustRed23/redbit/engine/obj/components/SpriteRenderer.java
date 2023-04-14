package dev.JustRed23.redbit.engine.obj.components;

import dev.JustRed23.redbit.engine.obj.Component;
import dev.JustRed23.redbit.engine.render.ShaderProgram;
import dev.JustRed23.redbit.engine.render.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class SpriteRenderer extends Component {

    private final Vector4f color;
    private final ShaderProgram shader;
    private final Vector2f[] textureCoords;
    private final Texture texture;

    public SpriteRenderer(Vector4f color, ShaderProgram shader) {
        this.color = color;
        this.shader = shader;
        this.textureCoords = new Vector2f[] { //temp
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        this.texture = null;
    }

    public SpriteRenderer(Texture texture, ShaderProgram shader) {
        this.texture = texture;
        this.color = new Vector4f(1, 1, 1, 1);
        this.shader = shader;
        this.textureCoords = new Vector2f[] { //temp
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
    }

    public final Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTextureCoords() {
        return textureCoords;
    }

    public ShaderProgram getShader() {
        return shader;
    }
}
