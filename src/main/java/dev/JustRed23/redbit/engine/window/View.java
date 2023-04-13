package dev.JustRed23.redbit.engine.window;

import dev.JustRed23.redbit.engine.render.Camera;

public abstract class View {

    protected Camera camera;

    protected abstract void setup(Window parent) throws Exception;
    protected abstract void update() throws Exception;
    protected abstract void render() throws Exception;
    protected abstract void cleanup();
}
