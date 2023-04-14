package dev.JustRed23.redbit.engine.window;

import dev.JustRed23.redbit.engine.obj.GameObject;
import dev.JustRed23.redbit.engine.render.Camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class View {

    protected Camera camera;
    private boolean initialized = false;

    private final List<GameObject> gameObjects = new ArrayList<>();

    protected abstract void setup(Window parent) throws Exception;
    protected abstract void update() throws Exception;
    protected abstract void render() throws Exception;
    protected abstract void cleanup();

    void _setup(Window parent) throws Exception {
        if (initialized)
            return;

        setup(parent);

        for (GameObject gameObject : gameObjects)
            gameObject.init();

        initialized = true;
    }

    void _update() throws Exception {
        if (!initialized)
            return;
        update();

        for (GameObject gameObject : gameObjects)
            gameObject.update();
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

    public final void addGameObject(GameObject gameObject) {
        if (initialized)
            gameObject.init();
        gameObjects.add(gameObject);
    }

    public final List<GameObject> getGameObjects() {
        return Collections.unmodifiableList(gameObjects);
    }

    public final boolean isVisible() {
        return initialized;
    }
}
