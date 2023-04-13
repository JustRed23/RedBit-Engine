package dev.JustRed23.redbit.engine.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public Camera(Vector2f position, int projectionWidth, int projectionHeight) {
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        this.position = position;
        updateProjection(projectionWidth, projectionHeight);
    }

    public void updateProjection(int width, int height) {
        projectionMatrix.identity();
        projectionMatrix.ortho(0, width, 0, height, 0, 100);
    }

    public Matrix4f getViewMatrix() {
        Vector3f front = new Vector3f(0, 0, -1);
        Vector3f up = new Vector3f(0, 1, 0);
        viewMatrix.identity();
        return viewMatrix.lookAt(new Vector3f(position.x, position.y, 20), front.add(position.x, position.y, 0), up);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
