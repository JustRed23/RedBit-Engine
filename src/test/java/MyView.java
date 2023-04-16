import dev.JustRed23.redbit.engine.err.UniformException;
import dev.JustRed23.redbit.engine.obj.GameObject;
import dev.JustRed23.redbit.engine.obj.Transform;
import dev.JustRed23.redbit.engine.obj.components.Sprite;
import dev.JustRed23.redbit.engine.obj.components.SpriteRenderer;
import dev.JustRed23.redbit.engine.obj.components.SpriteSheet;
import dev.JustRed23.redbit.engine.render.*;
import dev.JustRed23.redbit.engine.utils.ResourcePool;
import dev.JustRed23.redbit.engine.window.View;
import dev.JustRed23.redbit.engine.window.Window;
import org.joml.Vector2f;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class MyView extends View {

    private int screenWidth, screenHeight;

    private final List<GameObject> fire = new ArrayList<>();

    protected void setup(Window parent) throws Exception {
        loadResources();

        screenWidth = parent.getWidth();
        screenHeight = parent.getHeight();
        camera = new Camera(new Vector2f(0, 0), screenWidth, screenHeight);

        /*GameObject fox = new GameObject("Fox", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        fox.addComponent(new SpriteRenderer(new Sprite(ResourcePool.getTexture("textures/fox.jpg"))) {
            protected void update() {}
        });
        addGameObject(fox);*/

        for (int i = 0; i < 20; i++) {
            GameObject fire = new GameObject("Fire", new Transform(new Vector2f(i * 50, 0), new Vector2f(128, 128)), i);
            fire.addComponent(new SpriteRenderer(ResourcePool.getSpriteSheet("textures/firesheet.png").getSprite(0)));
            this.fire.add(fire);
            addGameObject(fire);
        }
    }

    protected void loadResources() throws FileNotFoundException {
        ResourcePool.getShader("shaders/default");
        ResourcePool.getTexture("textures/fox.jpg");
        ResourcePool.addSpriteSheet("textures/firesheet.png", tex -> new SpriteSheet(tex, 128, 128, 20, 0));
    }

    private int spriteIndex = 0;
    private float flipTime = 0.2f;
    private float timer = 0;
    protected void update() {
        timer += flipTime / 3;
        if (timer >= flipTime) {
            timer = 0;
            spriteIndex++;
            if (spriteIndex >= 20)
                spriteIndex = 0;

            for (GameObject fire : fire)
                fire.getComponent(SpriteRenderer.class).setSprite(ResourcePool.getSpriteSheet("textures/firesheet.png").getSprite(spriteIndex));
        }
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
