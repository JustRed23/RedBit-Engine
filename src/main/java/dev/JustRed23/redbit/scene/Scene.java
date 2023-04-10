package dev.JustRed23.redbit.scene;

import dev.JustRed23.redbit.func.Renderable;
import dev.JustRed23.redbit.func.Updateable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    public abstract static class SubScene extends Scene {}

    private final List<SubScene> subScenes = new ArrayList<>();

    private final List<Updateable> updateables = new ArrayList<>();
    private final List<Renderable> renderables = new ArrayList<>();

    protected abstract void init(@Nullable Scene parent) throws Exception;
    protected abstract void update();
    protected abstract void render();
    protected abstract void cleanup();

    public final void onInit() throws Exception {
        init(null);
        for (SubScene subScene : subScenes)
            subScene.onInit();
    }

    public final void onUpdate() {
        update();
        subScenes.forEach(SubScene::update);
        updateables.forEach(Updateable::update);
    }

    public final void onRender() {
        render();
        subScenes.forEach(SubScene::render);
        renderables.forEach(Renderable::render);
    }

    public final void onCleanup() {
        subScenes.forEach(SubScene::cleanup);
        cleanup();
    }

    public void addSubScene(SubScene subScene) {
        subScenes.add(subScene);
    }

    public void removeSubScene(SubScene subScene) {
        subScenes.remove(subScene);
    }

    public void addUpdateable(Updateable updateable) {
        updateables.add(updateable);
    }

    public void removeUpdateable(Updateable updateable) {
        updateables.remove(updateable);
    }

    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
    }

    public void removeRenderable(Renderable renderable) {
        renderables.remove(renderable);
    }
}
