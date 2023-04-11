package dev.JustRed23.redbit.engine.window;

public interface View {
    void setup(Window parent) throws Exception;
    void update();
    void render();
    void cleanup();
}
