package dev.JustRed23.redbit.engine.window;

import dev.JustRed23.redbit.engine.render.Camera;

public abstract class View {

    protected Camera camera;
    private boolean initialized = false;

    protected abstract void setup(Window parent) throws Exception;
    protected abstract void update() throws Exception;
    protected abstract void render() throws Exception;
    protected abstract void cleanup();

    void _setup(Window parent) throws Exception {
        if (initialized)
            return;
        setup(parent);
        initialized = true;
    }

    void _update() throws Exception {
        if (!initialized)
            return;
        update();
    }

    void _render() throws Exception {
        if (!initialized)
            return;
        render();
    }

    void _cleanup() {
        if (!initialized)
            return;
        cleanup();
        initialized = false;
    }

    public boolean isVisible() {
        return initialized;
    }
}
