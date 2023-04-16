package dev.JustRed23.redbit.engine.render;

import dev.JustRed23.redbit.engine.err.UniformException;
import dev.JustRed23.redbit.engine.obj.GameObject;
import dev.JustRed23.redbit.engine.obj.components.SpriteRenderer;
import dev.JustRed23.redbit.engine.window.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {

    private final List<BatchRenderer> batches = new ArrayList<>();
    private final int MAX_BATCH_SIZE = 1000;

    public void add(View view, GameObject object) {
        SpriteRenderer renderer = object.getComponent(SpriteRenderer.class);
        if (renderer == null)
            return;

        add(view, renderer);
    }

    private void add(View view, SpriteRenderer renderer) {
        boolean added = false;
        for (BatchRenderer batch : batches) {
            if (batch.hasRoom() && batch.zIndex() == renderer.parent.zIndex()) {
                Texture tex = renderer.getTexture();
                if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
                    batch.addSprite(renderer);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            BatchRenderer batch = new BatchRenderer(view, MAX_BATCH_SIZE, renderer.parent.zIndex(), renderer.getShader());
            batch.start();
            batches.add(batch);
            batch.addSprite(renderer);
            Collections.sort(batches);
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
