package dev.JustRed23.redbit.engine.utils;

import dev.JustRed23.redbit.engine.render.Texture;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public final class TextureUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextureUtils.class);

    private static final Map<String, Texture> texMap = new HashMap<>();

    public static @NotNull Texture load(String path) throws FileNotFoundException {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            Texture texture = texMap.getOrDefault(path, null);

            if (texture == null) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);

                URL imagePath = FileUtils.getResource(path);
                if (imagePath == null)
                    throw new FileNotFoundException("Image not found: " + path);

                File file = new File(imagePath.getPath());
                LOGGER.debug("Loading texture: " + file.getName());

                STBImage.stbi_set_flip_vertically_on_load(true);
                ByteBuffer image = STBImage.stbi_load(file.getAbsolutePath(), width, height, channels, 0);
                if (image == null || channels.get(0) < 3)
                    throw new RuntimeException("Failed to load image: " + path);

                int id = glGenTextures();
                glBindTexture(GL_TEXTURE_2D, id);

                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

                int format = channels.get(0) == 3 ? GL_RGB : GL_RGBA;
                glTexImage2D(GL_TEXTURE_2D, 0, format, width.get(0), height.get(0), 0, format, GL_UNSIGNED_BYTE, image);
                STBImage.stbi_image_free(image);

                texture = new Texture(id, width.get(0), height.get(0), format);
                texMap.put(path, texture);
            }

            return texture;
        }
    }

    public static void bindTexture(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public static void unbindTexture() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static void cleanup() {
        texMap.forEach((s, texture) -> glDeleteTextures(texture.id()));
    }
}
