import dev.JustRed23.redbit.engine.err.UniformException;
import dev.JustRed23.redbit.engine.obj.GameObject;
import dev.JustRed23.redbit.engine.obj.Transform;
import dev.JustRed23.redbit.engine.obj.components.SpriteRenderer;
import dev.JustRed23.redbit.engine.render.*;
import dev.JustRed23.redbit.engine.utils.ResourcePool;
import dev.JustRed23.redbit.engine.window.View;
import dev.JustRed23.redbit.engine.window.Window;
import org.joml.Vector2f;

import java.io.FileNotFoundException;

import static org.lwjgl.opengl.GL11.*;

public class MyView extends View {

    private int screenWidth, screenHeight;

    protected void setup(Window parent) throws Exception {
        loadResources();

        screenWidth = parent.getWidth();
        screenHeight = parent.getHeight();
        camera = new Camera(new Vector2f(-250, 0), screenWidth, screenHeight);

        GameObject fox = new GameObject("Fox", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        fox.addComponent(new SpriteRenderer(ResourcePool.getTexture("textures/fox.jpg"), ResourcePool.getShader("shaders/default")) {
            protected void update() {}
        });
        addGameObject(fox);
    }

    protected void loadResources() throws FileNotFoundException {
        ResourcePool.getShader("shaders/default");
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
