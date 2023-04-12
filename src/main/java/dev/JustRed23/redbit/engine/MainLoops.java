package dev.JustRed23.redbit.engine;

import dev.JustRed23.redbit.engine.err.WindowInitException;
import dev.JustRed23.redbit.engine.window.WindowController;

import static org.lwjgl.glfw.GLFW.*;

public class MainLoops {

    private final int fps, ups;

    private int updates, renders;

    MainLoops(int fps, int ups) throws WindowInitException {
        if (Engine.initialized)
            throw new IllegalStateException("Engine is already initialized");

        this.fps = fps;
        this.ups = ups;
        Engine.initialized = true;
        run();
    }

    private void run() throws WindowInitException {
        long initialTime = System.nanoTime();
        final double timeU = 1_000_000_000d / ups;
        final double timeF = 1_000_000_000d / fps;

        double fpscounter = 0;
        double deltaU = 0, deltaF = 0;
        long updateTime = initialTime;

        WindowController.ready();

        while (!Engine.stopRequested && !Thread.currentThread().isInterrupted()) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - updateTime) / timeU;
            deltaF += (currentTime - updateTime) / timeF;
            fpscounter += (currentTime - updateTime) / 1_000_000_000d;

            if (fpscounter >= 1) {
                Engine.updateCounters(renders, updates);
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
                render();
                glfwPollEvents();
                deltaF--;
            }
        }

        cleanup();
    }

    void update() {
        WindowController.update();
        updates++;
    }

    void render() {
        WindowController.render();
        WindowController.swapBuffers();
        renders++;
    }

    void cleanup() {
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        Engine.exit();
    }
}
