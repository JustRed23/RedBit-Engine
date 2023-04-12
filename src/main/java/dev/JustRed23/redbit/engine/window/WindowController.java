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
        windows.stream().filter(windows -> windows.getWindowHandle() != 0).forEach(Window::update);
    }

    public static void render() {
        windows.stream().filter(windows -> windows.getWindowHandle() != 0).forEach(Window::render);
    }

    public static void swapBuffers() {
        windows.stream().filter(windows -> windows.getWindowHandle() != 0).forEach(Window::swapBuffers);
    }

    /* EVENTS */

    /* INTERNAL */

    private static boolean ready = false;

    static void checkIfLastWindow(boolean exitOnClose) {
        if (!ready)
            return;

        windowCount--;
        if (windowCount == 0 || exitOnClose)
            Engine.stopRequested = true;
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

    public static void cleanup() {
        if (!ready)
            return;

        for (Window window : windows) {
            if (window.getWindowHandle() != 0) //already gone
                window.destroy();
        }

        windows.clear();
        windowCount = 0;
    }

    /* INTERNAL */
}
