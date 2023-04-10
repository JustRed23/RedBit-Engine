package dev.JustRed23.redbit;

import dev.JustRed23.redbit.ex.EngineInitializationException;
import dev.JustRed23.redbit.ex.SceneInitializationException;
import dev.JustRed23.redbit.func.Renderable;
import dev.JustRed23.redbit.func.Updateable;
import dev.JustRed23.redbit.input.KeyCallback;
import dev.JustRed23.redbit.input.MouseCallback;
import dev.JustRed23.redbit.scene.Scene;
import dev.JustRed23.redbit.stats.TimingManager;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Screen {

    private static final Logger LOGGER = LoggerFactory.getLogger(Screen.class);

    private final int width;
    private final int height;

    private String title;

    private long windowHandle;

    private KeyListener keyListener;
    private MouseListener mouseListener;

    private int updates, renders;

    private Scene currentScene, lastScene;

    private final List<Updateable> globalUpdateables = new ArrayList<>();
    private final List<Renderable> globalRenderables = new ArrayList<>();

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
        glfwSwapInterval(0);

        // Make the window visible
        glfwShowWindow(windowHandle);
        LOGGER.info("Screen initialized (took {}ms)", System.currentTimeMillis() - Engine.START_TIME);
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
                TimingManager.startTiming("update");
                update();
                TimingManager.stopTiming("update");
                deltaU--;
            }

            if (deltaF >= 1) {
                glEnable(GL_DEPTH_TEST);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                TimingManager.startTiming("render");
                render();
                TimingManager.stopTiming("render");

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
        if (currentScene != null)
            currentScene.onUpdate();
        for (Updateable updateable : globalUpdateables)
            updateable.update();
        updates++;
    }

    public void render() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        if (currentScene != null)
            currentScene.onRender();
        for (Renderable renderable : globalRenderables)
            renderable.render();
        renders++;
    }

    /**
     * Sets the current scene, will cleanup the last scene and initialize the new scene
     * @param scene the new scene
     */
    public void setScene(Scene scene) throws SceneInitializationException {
        if (currentScene != null)
            currentScene.onCleanup();
        lastScene = currentScene;

        currentScene = scene;
        try {
            if (currentScene != null)
                currentScene.onInit();
        } catch (Exception e) {
            throw new SceneInitializationException("Could not initialize new scene", e);
        }
    }

    /**
     * @return the current active scene
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * @return the last active scene
     */
    public Scene getLastScene() {
        return lastScene;
    }

    /**
     * Add a global updateable, this updateable will be updated every frame no matter what scene is active
     * @param updateable the updateable to add
     */
    public void addGlobalUpdateable(Updateable updateable) {
        globalUpdateables.add(updateable);
    }

    /**
     * Add a global renderable, this renderable will be rendered every frame no matter what scene is active
     * @param renderable the renderable to add
     */
    public void addGlobalRenderable(Renderable renderable) {
        globalRenderables.add(renderable);
    }

    /**
     * Remove a global updateable
     * @param updateable the updateable to remove
     */
    public void removeGlobalUpdateable(Updateable updateable) {
        globalUpdateables.remove(updateable);
    }

    /**
     * Remove a global renderable
     * @param renderable the renderable to remove
     */
    public void removeGlobalRenderable(Renderable renderable) {
        globalRenderables.remove(renderable);
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
     * @return the time in seconds since the engine started
     */
    public double getTime() {
        return glfwGetTime();
    }

    /**
     * @return the window handle of the screen, used for GLFW
     */
    public long getWindowHandle() {
        return windowHandle;
    }
}
