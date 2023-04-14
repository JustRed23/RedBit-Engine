package dev.JustRed23.redbit.engine.window;

import dev.JustRed23.redbit.engine.obj.GameObject;
import dev.JustRed23.redbit.engine.render.Camera;
import dev.JustRed23.redbit.engine.render.Renderer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class View {

    private final Renderer renderer = new Renderer();
    protected Camera camera;
    private Window parent;
    private boolean initialized = false;

    private final List<GameObject> gameObjects = new ArrayList<>();

    protected abstract void setup(Window parent) throws Exception;
    protected abstract void update() throws Exception;
    protected abstract void render() throws Exception;
    protected abstract void cleanup();

    void _setup(Window parent) throws Exception {
        if (initialized)
            return;

        this.parent = parent;

        setup(parent);

        for (GameObject gameObject : gameObjects) {
            gameObject.init();
            renderer.add(this, gameObject);
        }

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

        renderer.render();
    }

    void _cleanup() {
        if (!initialized)
            return;
        cleanup();
        renderer.cleanup();
        initialized = false;
    }

    public final void addGameObject(GameObject gameObject) throws FileNotFoundException {
        if (initialized) {
            gameObject.init();
            renderer.add(this, gameObject);
        }
        gameObjects.add(gameObject);
    }

    public final List<GameObject> getGameObjects() {
        return Collections.unmodifiableList(gameObjects);
    }

    public final boolean isVisible() {
        return initialized;
    }

    public final Camera getCamera() {
        return camera;
    }

    public final Window getParent() {
        return parent;
    }
}
