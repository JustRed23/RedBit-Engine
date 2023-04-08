package dev.JustRed23.redbit.input;

public interface KeyCallback {

    void invoke(long window, int key, int scancode, int action, int mods);
}
