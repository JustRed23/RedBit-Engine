package dev.JustRed23.redbit.engine.obj;

import org.joml.Vector2f;

public class Transform {

    public Vector2f position, scale;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    public Transform copy() {
        return new Transform(new Vector2f(position), new Vector2f(scale));
    }

    public void copy(Transform copyTo) {
        if (copyTo == null)
            copyTo = new Transform();
        copyTo.position.set(position);
        copyTo.scale.set(scale);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Transform transform)
            return transform.position.equals(position) && transform.scale.equals(scale);
        return false;
    }
}
