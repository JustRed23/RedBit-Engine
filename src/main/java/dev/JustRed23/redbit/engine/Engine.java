package dev.JustRed23.redbit.engine;

import dev.JustRed23.redbit.engine.err.WindowInitException;
import dev.JustRed23.redbit.engine.utils.ResourcePool;
import dev.JustRed23.stonebrick.app.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFWErrorCallback.getDescription;

public class Engine extends Application {

    public static final Logger LOGGER = LoggerFactory.getLogger(Engine.class);
    public static final long START_TIME = System.nanoTime();

    public static boolean stopRequested = false;
    static boolean initialized = false;

    protected void init() throws Exception {
        glfwSetErrorCallback((error, description) -> LOGGER.error("GLFW Error " + error + " - " + getDescription(description)));
        LOGGER.info("Running LWJGL version " + org.lwjgl.Version.getVersion());
    }

    protected void start() throws Exception {
        Application.runLater(() -> {
            try {
                new MainLoops(144, 60);
                loadInternalResources();
            } catch (WindowInitException e) {
                LOGGER.error("Failed to initialize window", e);
            } catch (Exception e) {
                LOGGER.error("Failed to load internal resources", e);
                throw new RuntimeException(e);
            }
        });
    }

    private void loadInternalResources() throws Exception {
        ResourcePool.getShader("shaders/default");
    }

    protected void stop() throws Exception {

    }

    public static void halt(boolean force) {
        if (force)
            System.exit(0);
        else
            stopRequested = true;
    }

    public static void halt() {
        halt(false);
    }
}
