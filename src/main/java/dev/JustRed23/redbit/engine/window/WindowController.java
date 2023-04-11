package dev.JustRed23.redbit.engine.window;

import dev.JustRed23.redbit.engine.Engine;
import dev.JustRed23.redbit.engine.err.WindowInitException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WindowController {

    private static final List<Window> windows = Collections.synchronizedList(new ArrayList<>());
    private static int windowCount = 0;

    public static @NotNull Window createWindow(@NotNull WindowOptions options) {
        Window window = new Window(options);
        windows.add(window);
        windowCount++;
        return window;
    }

    public static @NotNull Window createWindow() {
        return createWindow(WindowOptions.getDefault());
    }

    /* EVENTS */

    public static void update() {
        windows.forEach(Window::update);
    }

    public static void render() {
        windows.forEach(Window::render);
    }

    public static void swapBuffers() {
        windows.forEach(Window::swapBuffers);
    }

    /* EVENTS */

    /* INTERNAL */

    private static boolean ready = false;

    static void checkIfLastWindow() {
        if (!ready)
            return;

        windowCount--;
        if (windowCount == 0) {
            System.out.println("Last window closed");
            Engine.stopRequested = true;
        }
    }

    public static List<Window> getWindows() {
        return windows;
    }

    public static void ready() throws WindowInitException {
        if (ready)
            return;
        ready = true;

        for (Window window : windows) {
            window.setup();
        }
    }

    /* INTERNAL */
}
