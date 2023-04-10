import dev.JustRed23.redbit.Engine;
import dev.JustRed23.redbit.ex.SceneInitializationException;
import dev.JustRed23.redbit.input.MouseCallback;
import dev.JustRed23.redbit.shader.ShaderProgram;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Starter {

    public static void main(String[] args) {
        Engine.builder()
                .title("RedBit")
                .width(1024)
                .height(600)
                .fps(144)
                .addKeyListener((window, key, scancode, action, mods) -> {
                    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                        Engine.halt(false);
                    if (key == GLFW_KEY_ENTER && action == GLFW_RELEASE) {
                        try {
                            if (Engine.getScreen().getCurrentScene() == null)
                                Engine.getScreen().setScene(new TestScene());
                            else Engine.getScreen().setScene(null);
                        } catch (SceneInitializationException e) {
                            throw new RuntimeException(e);
                        }
                    }
//                    if (key == GLFW_KEY_F11 && action == GLFW_RELEASE) {
//                        ShaderProgram program = new ShaderProgram();
//                        program.setFragmentShaderLoc("shaders/fragment.glsl");
//                        program.setVertexShaderLoc("shaders/vertex.glsl");
//                        try {
//                            program.createShader();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        Starter.program = program;
//
//                        Engine.getScreen().addRenderable(() -> {
//                            program.bind();
//                            glBegin(GL_TRIANGLES);
//
//                            glVertex2f(-0.5f, -0.5f);
//                            glVertex2f(0.0f, 0.5f);
//                            glVertex2f(0.5f, -0.5f);
//
//                            glEnd();
//                            program.unbind();
//                        });
//                    }
//
//                    if (key == GLFW_KEY_UP && action == GLFW_RELEASE) {
//                        program.bind();
//                        program.set("color", new Vector3f(1.0f, 0.0f, 0.0f));
//                        program.unbind();
//                    }
                })
                .addMouseListener(new MouseCallback() {
                    public void move(long window, double xpos, double ypos) {
                        //System.out.println("X: " + xpos + ", Y: " + ypos);
                    }

                    public void click(long window, int button, int action, int mods) {
                        //System.out.println("Button: " + button + " Action: " + action + " Mods: " + mods);
                    }
                })
                .launch(args);
    }
}
