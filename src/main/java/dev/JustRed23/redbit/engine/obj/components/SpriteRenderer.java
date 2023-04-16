package dev.JustRed23.redbit.engine.obj.components;

import dev.JustRed23.redbit.engine.obj.Component;
import dev.JustRed23.redbit.engine.obj.Transform;
import dev.JustRed23.redbit.engine.render.ShaderProgram;
import dev.JustRed23.redbit.engine.render.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private final Vector4f color;

    private Sprite sprite;
    private ShaderProgram shader;

    private Transform lastTransform;
    private boolean dirty = true;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite) {
        this.color = new Vector4f(1, 1, 1, 1);
        this.sprite = sprite;
    }

    protected void init() {
        lastTransform = parent.transform.copy();
    }

    protected void update() {
        if (!lastTransform.equals(parent.transform)) {
            parent.transform.copy(lastTransform);
            dirty = true;
        }
    }

    public void setColor(Vector4f color) {
        if (this.color.equals(color))
            return;

        this.color.set(color);
        dirty = true;
    }

    public final Vector4f getColor() {
        return color;
    }

    public final Texture getTexture() {
        return sprite.getTexture();
    }

    public final Vector2f[] getTextureCoords() {
        return sprite.getTextureCoords();
    }

    public final void setShader(ShaderProgram shader) {
        if (this.shader == shader)
            return;

        this.shader = shader;
        dirty = true;
    }

    public final ShaderProgram getShader() {
        return shader == null ? ShaderProgram.getDefault() : shader;
    }

    public void setSprite(Sprite sprite) {
        if (this.sprite == sprite)
            return;

        this.sprite = sprite;
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clean() {
        this.dirty = false;
    }
}
