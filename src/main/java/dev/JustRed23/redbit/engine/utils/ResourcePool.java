package dev.JustRed23.redbit.engine.utils;

import dev.JustRed23.redbit.engine.obj.components.SpriteSheet;
import dev.JustRed23.redbit.engine.render.ShaderProgram;
import dev.JustRed23.redbit.engine.render.Texture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ResourcePool {

    private static final Map<String, ShaderProgram> shaders = new HashMap<>();
    private static final Map<String, SpriteSheet> spriteSheets = new HashMap<>();

    public static ShaderProgram getShader(String shaderPath) throws FileNotFoundException {
        if (shaders.containsKey(shaderPath))
            return shaders.get(shaderPath);

        ShaderProgram shader = new ShaderProgram(shaderPath + "/vertex.glsl", shaderPath + "/fragment.glsl");
        shaders.put(shaderPath, shader);
        return shader;
    }

    public static @NotNull Texture getTexture(String texturePath) throws FileNotFoundException {
        return TextureUtils.load(texturePath);
    }

    public static void addSpriteSheet(String spriteSheetPath, @NotNull Function<Texture, SpriteSheet> spriteSheet) {
        if (spriteSheets.containsKey(spriteSheetPath)) {
            LoggerFactory.getLogger(ResourcePool.class).error("Sprite sheet {} already exists", spriteSheetPath);
            return;
        }

        final Texture texture;
        try {
            texture = getTexture(spriteSheetPath);
        } catch (FileNotFoundException e) {
            LoggerFactory.getLogger(ResourcePool.class).error("Texture {} not found", spriteSheetPath);
            return;
        }

        spriteSheets.put(spriteSheetPath, spriteSheet.apply(texture));
    }

    public static @Nullable SpriteSheet getSpriteSheet(String spriteSheetPath) {
        if (!spriteSheets.containsKey(spriteSheetPath)) {
            LoggerFactory.getLogger(ResourcePool.class).error("Sprite sheet {} not found", spriteSheetPath);
            return null;
        }

        return spriteSheets.get(spriteSheetPath);
    }
}
