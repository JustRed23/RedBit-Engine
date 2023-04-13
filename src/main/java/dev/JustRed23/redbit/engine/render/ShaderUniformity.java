package dev.JustRed23.redbit.engine.render;

import dev.JustRed23.redbit.engine.utils.BufUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

class ShaderUniformity {

    private int programId;

    void setProgramId(int programId) {
        this.programId = programId;
    }

    private int setupUniform(String name) {
        int location = glGetUniformLocation(programId, name);
        if (location < 0)
            throw new RuntimeException("Could not find uniform variable '" + name + "'");
        return location;
    }

    public void set(String name, int value) {
        glUniform1i(setupUniform(name), value);
    }

    public void set(String name, float value) {
        glUniform1f(setupUniform(name), value);
    }

    public void set(String name, boolean value) {
        glUniform1i(setupUniform(name), value ? 1 : 0);
    }

    public void set(String name, @NotNull Vector2f vec) {
        glUniform2f(setupUniform(name), vec.x, vec.y);
    }

    public void set(String name, @NotNull Vector2i vec) {
        glUniform2i(setupUniform(name), vec.x, vec.y);
    }

    public void set(String name, @NotNull Vector3f vec) {
        glUniform3f(setupUniform(name), vec.x, vec.y, vec.z);
    }

    public void set(String name, @NotNull Vector3i vec) {
        glUniform3i(setupUniform(name), vec.x, vec.y, vec.z);
    }

    public void set(String name, @NotNull Vector4f vec) {
        glUniform4f(setupUniform(name), vec.x, vec.y, vec.z, vec.w);
    }

    public void set(String name, @NotNull Vector4i vec) {
        glUniform4i(setupUniform(name), vec.x, vec.y, vec.z, vec.w);
    }

    public void set(String name, @NotNull FloatBuffer buffer) {
        glUniformMatrix4fv(setupUniform(name), false, buffer);
    }

    public void set(String name, @NotNull Matrix4f matrix) {
        glUniformMatrix4fv(setupUniform(name), false, BufUtils.create(matrix));
    }
}