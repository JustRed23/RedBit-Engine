package dev.JustRed23.redbit.engine.obj;

import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

public abstract class Component {

    protected GameObject parent;

    public final @Nullable Component copy(GameObject newParent) {
        Component copy;
        try {
            copy = getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            LoggerFactory.getLogger(Component.class).error("Failed to copy component", e);
            return null;
        }
        copy.parent = newParent;
        return copy;
    }

    public final Component copy() {
        return copy(parent);
    }

    protected void init() {}
    protected abstract void update();
}
