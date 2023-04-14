package dev.JustRed23.redbit.engine.utils;

import dev.JustRed23.redbit.engine.render.ShaderProgram;
import dev.JustRed23.redbit.engine.render.Texture;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public final class ResourcePool {

    private static final Map<String, ShaderProgram> shaders = new HashMap<>();

    public static ShaderProgram getShader(String shaderPath) throws FileNotFoundException {
        if (shaders.containsKey(shaderPath))
            return shaders.get(shaderPath);

        ShaderProgram shader = new ShaderProgram(shaderPath + "/vertex.glsl", shaderPath + "/fragment.glsl");
        shaders.put(shaderPath, shader);
        return shader;
    }

    public static Texture getTexture(String texturePath) throws FileNotFoundException {
        return TextureUtils.load(texturePath);
    }
}
