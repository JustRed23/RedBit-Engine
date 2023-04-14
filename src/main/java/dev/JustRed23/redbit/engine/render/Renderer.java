package dev.JustRed23.redbit.engine.render;

import dev.JustRed23.redbit.engine.err.UniformException;
import dev.JustRed23.redbit.engine.obj.GameObject;
import dev.JustRed23.redbit.engine.obj.components.MeshRenderer;
import dev.JustRed23.redbit.engine.window.View;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private final List<BatchRenderer> batches = new ArrayList<>();
    private final int MAX_BATCH_SIZE = 1000;

    public void add(View view, GameObject object) {
        MeshRenderer renderer = object.getComponent(MeshRenderer.class);
        if (renderer == null)
            return;

        add(view, renderer);
    }

    private void add(View view, MeshRenderer renderer) {
        boolean added = false;
        for (BatchRenderer batch : batches) {
            if (batch.hasRoom()) {
                batch.addMesh(renderer);
                added = true;
                break;
            }
        }

        if (!added) {
            BatchRenderer batch = new BatchRenderer(view, MAX_BATCH_SIZE, renderer.getShader());
            batch.start();
            batches.add(batch);
            batch.addMesh(renderer);
        }
    }

    public void render() throws UniformException {
        for (BatchRenderer batch : batches)
            batch.render();
    }

    public void cleanup() {
        for (BatchRenderer batch : batches)
            batch.cleanup();
        batches.clear();
    }
}
