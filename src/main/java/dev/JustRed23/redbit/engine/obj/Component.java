package dev.JustRed23.redbit.engine.obj;

public abstract class Component {

    public GameObject parent;

    public final Component copy(GameObject newParent) {
        Component copy = onCopy();
        copy.parent = newParent;
        return copy;
    }

    public final Component copy() {
        return copy(parent);
    }

    protected void init() {}
    protected Component onCopy() {
        return new Component() {

            protected void init() {
                Component.this.init();
            }

            protected void update() {
                Component.this.update();
            }
        };
    }
    protected abstract void update();
}
