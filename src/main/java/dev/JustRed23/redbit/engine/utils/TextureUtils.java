package dev.JustRed23.redbit.engine.utils;

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
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public final class TextureUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextureUtils.class);

    private static final Map<String, Integer> idMap = new HashMap<>();

    public static int load(String path) throws FileNotFoundException {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            int id = idMap.getOrDefault(path, -1);

            if (id == -1) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);

                URL imagePath = FileUtils.getResource(path);
                if (imagePath == null)
                    throw new FileNotFoundException("Image not found: " + path);

                File file = new File(imagePath.getPath());
                LOGGER.debug("Loading texture: " + file.getName());

                ByteBuffer image = STBImage.stbi_load(file.getAbsolutePath(), width, height, channels, 4);
                if (image == null)
                    throw new RuntimeException("Failed to load image: " + path);

                id = glGenTextures();
                idMap.put(path, id);
                glBindTexture(GL_TEXTURE_2D, id);
                glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
                glGenerateMipmap(GL_TEXTURE_2D);
                STBImage.stbi_image_free(image);
            }

            return id;
        }
    }

    public static void cleanup() {
        idMap.forEach((path, id) -> glDeleteTextures(id));
    }
}
