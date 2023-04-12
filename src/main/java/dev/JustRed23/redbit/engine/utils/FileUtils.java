package dev.JustRed23.redbit.engine.utils;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public final class FileUtils {

    private static final List<ByteBuffer> resourcesToFree = new ArrayList<>();
    private static final Map<String, GLFWImage> images = new HashMap<>();

    public static GLFWImage loadImage(String path) {
        if (images.containsKey(path))
            return images.get(path);

        ByteBuffer imageBuf;
        IntBuffer w = null;
        IntBuffer h = null;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            w = stack.mallocInt(1);
            h = stack.mallocInt(1);

            final URL resource = getResource(path);
            if (resource == null)
                throw new RuntimeException("Failed to load image: " + path);

            File file = new File(resource.getPath());

            imageBuf = stbi_load(file.getAbsolutePath(), w, h, comp, 4);
            if (imageBuf == null)
                throw new RuntimeException("Failed to load image: " + path);

            resourcesToFree.add(imageBuf);
        }

        GLFWImage image = GLFWImage.malloc();
        image.set(w.get(), h.get(), imageBuf);
        images.put(path, image);
        return image;
    }

    public static void cleanup() {
        resourcesToFree.forEach(STBImage::stbi_image_free);
        images.forEach((s, glfwImage) -> glfwImage.free());
        images.clear();
    }

    public static @Nullable String readFile(String path) {
        try (InputStream resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (resourceAsStream == null)
                return null;
            return new String(resourceAsStream.readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static @Nullable URL getResource(String path) {
        return FileUtils.class.getClassLoader().getResource(path);
    }
}
