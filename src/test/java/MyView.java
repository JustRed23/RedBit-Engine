import dev.JustRed23.redbit.engine.err.UniformException;
import dev.JustRed23.redbit.engine.render.Camera;
import dev.JustRed23.redbit.engine.render.ColoredMesh;
import dev.JustRed23.redbit.engine.render.Mesh;
import dev.JustRed23.redbit.engine.render.ShaderProgram;
import dev.JustRed23.redbit.engine.window.View;
import dev.JustRed23.redbit.engine.window.Window;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class MyView extends View {

    private ShaderProgram basicShader;

    private Mesh square;

    public void setup(Window parent) throws Exception {
        camera = new Camera(new Vector2f(), parent.getWidth(), parent.getHeight());

        basicShader = new ShaderProgram("shaders/default/vertex.glsl", "shaders/default/fragment.glsl");

        float mySquareSize = 100;
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
    }

    public void update() {
        //camera.position.x -= 0.5f;
    }

    public void render() throws UniformException {
        glClearColor(0.3f, 0, 0, 1);

        basicShader.set("uProjectionMatrix", camera.getProjectionMatrix());
        basicShader.set("uViewMatrix", camera.getViewMatrix());

        square.render(basicShader);
    }

    public void cleanup() {
        basicShader.cleanup();
        square.cleanup();
    }
}
