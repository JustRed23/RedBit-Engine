import components.TestComponent;
import dev.JustRed23.redbit.engine.err.UniformException;
import dev.JustRed23.redbit.engine.obj.GameObject;
import dev.JustRed23.redbit.engine.render.*;
import dev.JustRed23.redbit.engine.window.View;
import dev.JustRed23.redbit.engine.window.Window;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class MyView extends View {

    private ShaderProgram basicShader, texturedShader;

    private Mesh square;

    private Mesh texturedSquare;

    private int screenWidth, screenHeight;

    private GameObject myGameObject;

    protected void setup(Window parent) throws Exception {
        screenWidth = parent.getWidth();
        screenHeight = parent.getHeight();
        camera = new Camera(new Vector2f(-(screenWidth / 2f), -(screenHeight / 2f)), screenWidth, screenHeight);

        myGameObject = new GameObject("MyGameObject");
        myGameObject.addComponent(new TestComponent());
        addGameObject(myGameObject);

        basicShader = new ShaderProgram("shaders/default/vertex.glsl", "shaders/default/fragment.glsl");
        texturedShader = new ShaderProgram("shaders/textured/vertex.glsl", "shaders/textured/fragment.glsl");

        float mySquareSize = 100f;
        square = new ColoredMesh(new float[] {
                mySquareSize, -mySquareSize, 0.0f, //bottom right
                -mySquareSize, mySquareSize, 0.0f, //top left
                mySquareSize, mySquareSize, 0.0f,  //top right
                -mySquareSize, -mySquareSize, 0.0f //bottom left
        }, new int[] {
                0, 1, 2, //top right triangle
                0, 1, 3 //bottom left triangle
        }, new float[] {
                1.0f, 0.0f, 0.0f, 1.0f, //bottom right
                0.0f, 1.0f, 0.0f, 1.0f, //top left
                0.0f, 0.0f, 1.0f, 1.0f, //top right
                1.0f, 1.0f, 0.0f, 1.0f //bottom left
        });

        mySquareSize = 0.5f;
        texturedSquare = new TexturedMesh(new float[] {
                mySquareSize, -mySquareSize, 0.0f, //bottom right
                -mySquareSize, mySquareSize, 0.0f, //top left
                mySquareSize, mySquareSize, 0.0f,  //top right
                -mySquareSize, -mySquareSize, 0.0f //bottom left
        }, new int[] {
                0, 1, 2, //top right triangle
                0, 1, 3 //bottom left triangle
        }, new float[] {
                1.0f, 1.0f, //bottom right
                0.0f, 0.0f, //top left
                1.0f, 0.0f, //top right
                0.0f, 1.0f //bottom left
        }, "textures/fox.jpg");
    }

    protected void update() {

    }

    protected void render() throws UniformException {
        glClearColor(0.3f, 0, 0, 1);

        //texturedShader.set("uTextureSampler", 0);
        //texturedSquare.render(texturedShader);

        basicShader.set("uProjectionMatrix", camera.getProjectionMatrix());
        basicShader.set("uViewMatrix", camera.getViewMatrix());

        square.render(basicShader);
    }

    public void wireframe() {
        if (!isVisible())
            return;

        square.setShowWireframe(!square.isShowingWireframe());
    }

    protected void cleanup() {
        basicShader.cleanup();
        square.cleanup();

        texturedShader.cleanup();
        texturedSquare.cleanup();
    }
}
