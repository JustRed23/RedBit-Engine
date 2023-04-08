package dev.JustRed23.redbit;

import dev.JustRed23.redbit.ex.EngineInitializationException;
import dev.JustRed23.redbit.input.KeyCallback;
import dev.JustRed23.redbit.input.MouseCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;

import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Screen {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Screen.class);

    private final int width;
    private final int height;

    private String title;

    private long windowHandle;

    private KeyListener keyListener;
    private MouseListener mouseListener;

    private int updates, renders;

    Screen(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    void setup(List<KeyCallback> keyCallbacks, List<MouseCallback> mouseCallbacks) throws EngineInitializationException {
        LOGGER.info("Initializing screen");
        glfwSetErrorCallback((error, description) -> LOGGER.error("GLFW Error: " + error + " - " + description));

        if (!glfwInit())
            throw new EngineInitializationException("GLFW failed to initialize");

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowHandle == NULL)
            throw new EngineInitializationException("GLFW failed to create a window");

        keyListener = new KeyListener(keyCallbacks);
        keyListener.setup(windowHandle);

        mouseListener = new MouseListener(mouseCallbacks);
        mouseListener.setup(windowHandle);

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            if (vidmode == null)
                throw new EngineInitializationException("GLFW failed to get the primary monitor");

            // Center the window
            glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowHandle);
        LOGGER.info("Screen initialized (took {}ms)", System.currentTimeMillis() - Engine.START_TIME);

        glfwMakeContextCurrent(NULL);
    }

    void startRendering(int fps, int ups) {
        long initialTime = System.nanoTime();
        final double timeU = 1_000_000_000d / ups;
        final double timeF = 1_000_000_000d / fps;

        double fpscounter = 0;
        double deltaU = 0, deltaF = 0;
        long updateTime = initialTime;

        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();

        while (!glfwWindowShouldClose(windowHandle)) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - updateTime) / timeU;
            deltaF += (currentTime - updateTime) / timeF;
            fpscounter += (currentTime - updateTime) / 1_000_000_000d;

            if (fpscounter >= 1) {
                glfwSetWindowTitle(windowHandle, title + " | FPS: " + renders + " | UPS: " + updates);
                fpscounter = 0;
                updates = 0;
                renders = 0;
            }

            updateTime = currentTime;

            if (deltaU >= 1) {
                update();
                deltaU--;
            }

            if (deltaF >= 1) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                render();

                glfwSwapBuffers(windowHandle); // swap the color buffers

                // Poll for window events. The key callback above will only be
                // invoked during this call.
                glfwPollEvents();
                deltaF--;
            }
        }

        Engine.exit();
    }

    void stopRendering() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void update() {
        updates++;
    }

    public void render() {
        renders++;
    }

    /**
     * @return the width of the screen
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the screen
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the title of the screen
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the screen
     * @param title the new title of the screen
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the window handle of the screen, used for GLFW
     */
    public long getWindowHandle() {
        return windowHandle;
    }
}
