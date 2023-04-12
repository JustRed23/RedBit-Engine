package dev.JustRed23.redbit.engine.listener;

import dev.JustRed23.redbit.engine.callback.CallbackController;
import dev.JustRed23.redbit.engine.callback.callbacks.MouseButtonCallback;
import dev.JustRed23.redbit.engine.callback.callbacks.MousePosCallback;
import dev.JustRed23.redbit.engine.callback.callbacks.MouseScrollCallback;
import dev.JustRed23.redbit.engine.window.Window;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener instance;

    private double x, y, lastX, lastY, scrollY;
    private boolean[] buttons = new boolean[8];
    private boolean dragging;

    public MouseListener(Window window) {
        x = 0;
        y = 0;
        lastX = 0;
        lastY = 0;
        scrollY = 0;

        enableCallbacks(window);
    }

    private void enableCallbacks(Window window) {
        CallbackController.addCallback(window, (MousePosCallback) (x, y) -> {
            this.x = x;
            this.y = y;

            if (dragging) return;
            for (boolean button : buttons)
                if (button) {
                    dragging = true;
                    break;
                }
        });

        CallbackController.addCallback(window, (MouseButtonCallback) (button, action, mods) -> {
            if (action == GLFW_PRESS)
                buttons[button] = true;
            else if (action == GLFW_RELEASE) {
                buttons[button] = false;
                dragging = false;
            }
        });

        CallbackController.addCallback(window, (MouseScrollCallback) y -> {
            this.scrollY = y;
        });
    }

    public static MouseListener getInstance(Window window) {
        if (instance == null)
            instance = new MouseListener(window);
        return instance;
    }

    public void update() {
        scrollY = 0;
        lastX = x;
        lastY = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDX() {
        return lastX - x;
    }

    public double getDY() {
        return lastY - y;
    }

    public double getScrollY() {
        return scrollY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public boolean isButtonDown(int button) {
        if (button < buttons.length && button >= 0)
            return buttons[button];
        return false;
    }
}
