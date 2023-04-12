package dev.JustRed23.redbit.engine.callback;

import dev.JustRed23.redbit.engine.callback.callbacks.*;
import dev.JustRed23.redbit.engine.window.Window;

import java.util.*;

public class CallbackController {

    private static final Map<Window, List<KeyCallback>> keyCallbacks = new HashMap<>();
    private static final Map<Window, List<MouseButtonCallback>> mouseCallbacks = new HashMap<>();
    private static final Map<Window, List<MousePosCallback>> mousePosCallbacks = new HashMap<>();
    private static final Map<Window, List<MouseScrollCallback>> mouseScrollCallbacks = new HashMap<>();

    public static void addCallback(Window window, Callback callback) {
        if (callback instanceof KeyCallback keyCallback)
            keyCallbacks.computeIfAbsent(window, k -> new ArrayList<>())
                    .add(keyCallback);
        else if (callback instanceof MouseButtonCallback mouseButtonCallback)
            mouseCallbacks.computeIfAbsent(window, k -> new ArrayList<>())
                    .add(mouseButtonCallback);
        else if (callback instanceof MousePosCallback mousePosCallback)
            mousePosCallbacks.computeIfAbsent(window, k -> new ArrayList<>())
                    .add(mousePosCallback);
        else if (callback instanceof MouseScrollCallback mouseScrollCallback)
            mouseScrollCallbacks.computeIfAbsent(window, k -> new ArrayList<>())
                    .add(mouseScrollCallback);
    }

    /* INTERNAL */
    public static void keyCallback(Window window, int key, int action, int mods) {
        final List<KeyCallback> keyCallbacks1 = keyCallbacks.get(window);
        if (keyCallbacks1 != null)
            keyCallbacks1.forEach(cb -> cb.invoke(key, action, mods));
    }

    public static void mouseButtonCallback(Window window, int button, int action, int mods) {
        final List<MouseButtonCallback> mouseButtonCallbacks = mouseCallbacks.get(window);
        if (mouseButtonCallbacks != null)
            mouseButtonCallbacks.forEach(cb -> cb.invoke(button, action, mods));
    }

    public static void mousePosCallback(Window window, double x, double y) {
        final List<MousePosCallback> mousePosCallbacks1 = mousePosCallbacks.get(window);
        if (mousePosCallbacks1 != null)
            mousePosCallbacks1.forEach(cb -> cb.invoke(x, y));
    }

    public static void mouseScrollCallback(Window window, double yOffset) {
        final List<MouseScrollCallback> mouseScrollCallbacks1 = mouseScrollCallbacks.get(window);
        if (mouseScrollCallbacks1 != null)
            mouseScrollCallbacks1.forEach(cb -> cb.invoke(yOffset));
    }

    public static void clearCallbacks(Window window) {
        keyCallbacks.remove(window);
        mouseCallbacks.remove(window);
        mousePosCallbacks.remove(window);
        mouseScrollCallbacks.remove(window);
    }

    public static void cleanup() {
        keyCallbacks.clear();
        mouseCallbacks.clear();
        mousePosCallbacks.clear();
        mouseScrollCallbacks.clear();
    }
    /* INTERNAL */
}
