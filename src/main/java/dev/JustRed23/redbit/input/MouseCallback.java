package dev.JustRed23.redbit.input;

public interface MouseCallback {

    void move(long window, double xpos, double ypos);

    void click(long window, int button, int action, int mods);
}
