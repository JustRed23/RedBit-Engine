import dev.JustRed23.redbit.mesh.Mesh;
import dev.JustRed23.redbit.scene.Scene;
import dev.JustRed23.redbit.shader.ShaderProgram;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.io.IOException;

import static java.lang.Math.sin;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TestScene extends Scene {

    private Mesh triangle;
    private Mesh square;

    private ShaderProgram shaderProgram;

    protected void init(@Nullable Scene parent) throws IOException {
        shaderProgram = new ShaderProgram();
        shaderProgram.setVertexShaderLoc("shaders/vertex.glsl");
        shaderProgram.setFragmentShaderLoc("shaders/fragment.glsl");
        shaderProgram.createShader();

        triangle = new Mesh(new float[] {
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f,  0.5f, 0.0f
        }, 3);

        square = new Mesh(new float[] {
                //first triangle
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,

                //second triangle
                -0.5f,  0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f
        }, 6);
    }

    protected void update() {
        shaderProgram.bind();
        float time = (float) glfwGetTime() / 10.0f;
        float hue = (time % 1.0f) * 360;
        float value = (float) Math.abs(Math.sin(time * Math.PI));

        float[] rgb = hsvToRgb(hue, 1.0f, value);
        shaderProgram.set("color", new Vector3f(rgb[0], rgb[1], rgb[2]));
        shaderProgram.unbind();
    }

    private static float[] hsvToRgb(float h, float s, float v) {
        float c = s * v;
        float x = c * (1 - Math.abs((h / 60) % 2 - 1));
        float m = v - c;
        float[] rgb;

        if (h < 60) {
            rgb = new float[]{c, x, 0};
        } else if (h < 120) {
            rgb = new float[]{x, c, 0};
        } else if (h < 180) {
            rgb = new float[]{0, c, x};
        } else if (h < 240) {
            rgb = new float[]{0, x, c};
        } else if (h < 300) {
            rgb = new float[]{x, 0, c};
        } else {
            rgb = new float[]{c, 0, x};
        }

        rgb[0] += m;
        rgb[1] += m;
        rgb[2] += m;

        return rgb;
    }

    protected void render() {
        glClearColor(0.3f, 0f, 0f, 1f);
        shaderProgram.bind();
        glBindVertexArray(square.getVaoId());
        glDrawArrays(GL_TRIANGLES, 0, square.getVertices());
        shaderProgram.unbind();
    }

    protected void cleanup() {
        shaderProgram.cleanup();
        triangle.cleanup();
        square.cleanup();
    }
}
