import dev.JustRed23.redbit.engine.err.UniformException;
import dev.JustRed23.redbit.engine.obj.GameObject;
import dev.JustRed23.redbit.engine.obj.Transform;
import dev.JustRed23.redbit.engine.obj.components.MeshRenderer;
import dev.JustRed23.redbit.engine.render.*;
import dev.JustRed23.redbit.engine.utils.ResourcePool;
import dev.JustRed23.redbit.engine.window.View;
import dev.JustRed23.redbit.engine.window.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.FileNotFoundException;

import static org.lwjgl.opengl.GL11.*;

public class MyView extends View {

    private int screenWidth, screenHeight;

    protected void setup(Window parent) throws Exception {
        loadResources();

        screenWidth = parent.getWidth();
        screenHeight = parent.getHeight();
        camera = new Camera(new Vector2f(-(screenWidth / 2f) + 310f, -(screenHeight / 2f) + 160f), screenWidth, screenHeight);

        int offset = 10;

        float totalWidth = 600 + offset * 2;
        float totalHeight = 300 + offset * 2;

        float sizeX = totalWidth / 100f;
        float sizeY = totalHeight / 100f;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = x * sizeX + offset;
                float yPos = y * sizeY + offset;

                Transform transform = new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY));
                GameObject gameObject = new GameObject("Test " + x + " " + y, transform);
                gameObject.addComponent(new MeshRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1), ResourcePool.getShader("shaders/default")) {
                    protected void update() {}
                });
                addGameObject(gameObject);
            }
        }
    }

    protected void loadResources() throws FileNotFoundException {
        ResourcePool.getShader("shaders/default");
        ResourcePool.getShader("shaders/textured");
        ResourcePool.getTexture("textures/fox.jpg");
    }

    protected void update() {

    }

    protected void render() throws UniformException {
        glClearColor(0.3f, 0, 0, 1);
    }

    public void wireframe() {
        if (!isVisible())
            return;


    }

    protected void cleanup() {

    }
}
