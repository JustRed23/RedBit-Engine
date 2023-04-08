package dev.JustRed23.redbit;

import dev.JustRed23.redbit.ex.EngineInitializationException;
import dev.JustRed23.redbit.input.KeyCallback;
import dev.JustRed23.redbit.input.MouseCallback;
import dev.JustRed23.redbit.stats.TimingManager;
import dev.JustRed23.stonebrick.app.Application;
import dev.JustRed23.stonebrick.data.FileStructure;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Engine extends Application {

    /* INIT */
    private static boolean initialized = false;
    private static Builder builder;

    @Contract(value = " -> new", pure = true)
    public static @NotNull Builder builder() {
        if (initialized)
            throw new IllegalStateException("Engine already initialized");
        initialized = true;
        return new Engine.Builder();
    }
    /* END INIT */

    public static final Long START_TIME = System.currentTimeMillis();

    private static Screen screen;

    protected void init() throws EngineInitializationException {
        FileStructure.discover(NoFileStructure.class);
        screen = new Screen(builder.width, builder.height, builder.title);
        screen.setup(builder.keyCallbacks, builder.mouseCallbacks);
    }

    protected void start() {
        screen.startRendering(builder.fps, builder.ups);
    }

    protected void stop() {
        screen.stopRendering();
        TimingManager.clearTimings();
    }

    public static void halt(boolean force) {
        if (force)
            System.exit(0);
        else
            glfwSetWindowShouldClose(screen.getWindowHandle(), true);
    }

    public static @Nullable Screen getScreen() {
        return screen;
    }

    public static class Builder {

            private String title = "RedBit";
            private int width = 800;
            private int height = 600;
            private int fps = 60;
            private int ups = 60;
            private final List<KeyCallback> keyCallbacks = new ArrayList<>();
            private final List<MouseCallback> mouseCallbacks = new ArrayList<>();

            private Builder() {}

            public Builder title(String title) {
                this.title = title;
                return this;
            }

            public Builder width(int width) {
                this.width = width;
                return this;
            }

            public Builder height(int height) {
                this.height = height;
                return this;
            }

            public Builder fps(int fps) {
                this.fps = fps;
                return this;
            }

            public Builder ups(int ups) {
                this.ups = ups;
                return this;
            }

            public Builder addKeyListener(KeyCallback callback) {
                keyCallbacks.add(callback);
                return this;
            }

            public Builder addKeyListeners(KeyCallback... callbacks) {
                keyCallbacks.addAll(Arrays.asList(callbacks));
                return this;
            }

            public Builder addMouseListener(MouseCallback callback) {
                mouseCallbacks.add(callback);
                return this;
            }

            public Builder addMouseListeners(MouseCallback... callbacks) {
                mouseCallbacks.addAll(Arrays.asList(callbacks));
                return this;
            }

            public void launch(String[] args) {
                Engine.builder = this;
                Application.launch(Engine.class, args);
            }
    }
}
