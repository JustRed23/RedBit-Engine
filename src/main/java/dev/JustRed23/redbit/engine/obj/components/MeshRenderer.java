package dev.JustRed23.redbit.engine.obj.components;

import dev.JustRed23.redbit.engine.obj.Component;
import dev.JustRed23.redbit.engine.render.ShaderProgram;
import org.joml.Vector4f;

public abstract class MeshRenderer extends Component {

    private final Vector4f color;
    private final ShaderProgram shader;

    public MeshRenderer(Vector4f color, ShaderProgram shader) {
        this.color = color;
        this.shader = shader;
    }

    public final Vector4f getColor() {
        return color;
    }

    public ShaderProgram getShader() {
        return shader;
    }
}
