package dev.JustRed23.redbit;

import dev.JustRed23.redbit.input.KeyCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

public class KeyListener {

    private final List<KeyCallback> callbacks = Collections.synchronizedList(new ArrayList<>());

    KeyListener(List<KeyCallback> callbacks) {
        this.callbacks.addAll(callbacks);
    }

    void setup(long windowHandle) {
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            synchronized (callbacks) {
                for (KeyCallback callback : callbacks)
                    callback.invoke(window, key, scancode, action, mods);
            }
        });
    }

    public void addCallback(KeyCallback callback) {
        synchronized (callbacks) {
            callbacks.add(callback);
        }
    }

    public void removeCallback(KeyCallback callback) {
        synchronized (callbacks) {
            callbacks.remove(callback);
        }
    }
}
