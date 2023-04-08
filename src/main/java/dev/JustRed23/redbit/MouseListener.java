package dev.JustRed23.redbit;

import dev.JustRed23.redbit.input.MouseCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

public class MouseListener {

    private final List<MouseCallback> callbacks = Collections.synchronizedList(new ArrayList<>());

    MouseListener(List<MouseCallback> callbacks) {
        this.callbacks.addAll(callbacks);
    }

    void setup(long windowHandle) {
        glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
            synchronized (callbacks) {
                for (MouseCallback callback : callbacks)
                    callback.click(window, button, action, mods);
            }
        });

        glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> {
            synchronized (callbacks) {
                for (MouseCallback callback : callbacks)
                    callback.move(window, xpos, ypos);
            }
        });
    }

    public void addCallback(MouseCallback callback) {
        synchronized (callbacks) {
            callbacks.add(callback);
        }
    }

    public void removeCallback(MouseCallback callback) {
        synchronized (callbacks) {
            callbacks.remove(callback);
        }
    }
}
