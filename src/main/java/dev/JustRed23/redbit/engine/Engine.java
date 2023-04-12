package dev.JustRed23.redbit.engine;

import dev.JustRed23.redbit.engine.err.WindowInitException;
import dev.JustRed23.stonebrick.app.Application;
import dev.JustRed23.stonebrick.data.FileStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFWErrorCallback.getDescription;

public class Engine extends Application {

    public static final Logger LOGGER = LoggerFactory.getLogger(Engine.class);
    public static final long START_TIME = System.currentTimeMillis();

    public static boolean stopRequested = false;
    static boolean initialized = false;

    static int fps, ups;

    static void updateCounters(int fps, int ups) {
        Engine.fps = fps;
        Engine.ups = ups;
    }

    protected void init() throws Exception {
        FileStructure.discover(NoFileStructure.class);
        glfwSetErrorCallback((error, description) -> LOGGER.error("GLFW Error " + error + " - " + getDescription(description)));
    }

    protected void start() throws Exception {
        Application.runLater(() -> {
            try {
                new MainLoops(144, 60);
            } catch (WindowInitException e) {
                LOGGER.error("Failed to initialize window", e);
            }
        });
    }

    protected void stop() throws Exception {

    }

    public static int getFps() {
        return fps;
    }

    public static int getUps() {
        return ups;
    }
}
