import dev.JustRed23.redbit.Engine;
import dev.JustRed23.redbit.input.MouseCallback;

import static org.lwjgl.glfw.GLFW.*;

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
                })
                .addMouseListener(new MouseCallback() {
                    public void move(long window, double xpos, double ypos) {
                        System.out.println("X: " + xpos + ", Y: " + ypos);
                    }

                    public void click(long window, int button, int action, int mods) {
                        System.out.println("Button: " + button + " Action: " + action + " Mods: " + mods);
                    }
                })
                .launch(args);
    }
}
