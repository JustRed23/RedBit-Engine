package dev.JustRed23.redbit.utils;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class BufUtils {

    public static @NotNull FloatBuffer create(@NotNull Matrix4f mat) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        mat.get(buffer);
        buffer.flip();
        return buffer;
    }

    public static @NotNull FloatBuffer create(float @NotNull [] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static @NotNull IntBuffer create(int @NotNull [] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
