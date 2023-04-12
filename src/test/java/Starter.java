import dev.JustRed23.redbit.engine.Engine;
import dev.JustRed23.redbit.engine.callback.CallbackController;
import dev.JustRed23.redbit.engine.callback.callbacks.CounterUpdateCallback;
import dev.JustRed23.redbit.engine.callback.callbacks.KeyCallback;
import dev.JustRed23.redbit.engine.window.View;
import dev.JustRed23.redbit.engine.window.Window;
import dev.JustRed23.redbit.engine.window.WindowController;
import dev.JustRed23.redbit.engine.window.WindowOptions;
import dev.JustRed23.stonebrick.app.Application;
import dev.JustRed23.stonebrick.data.FileStructure;

import static org.lwjgl.glfw.GLFW.*;

public class Starter {

    private static boolean debug = false;

    public static void main(String[] args) {
        Window mainWindow = WindowController.createWindow(new WindowOptions(1024, 600, "RedBit", false, false, true, "logo.png"));
        //WindowController.createWindow(new WindowOptions(800, 400, "RedBit TEST", true, false, false, "logo.png"));

        View view = new MyView();

        CallbackController.addCallback(mainWindow, (KeyCallback) (key, action, mods) -> {
            if (debug)
                System.out.println("Key: " + key + " Action: " + action + " Mods: " + mods);

            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(mainWindow.getWindowHandle(), true);

            if (key == GLFW_KEY_ENTER && action == GLFW_RELEASE)
                mainWindow.setView(mainWindow.getCurrentView() == view ? null : view);

            if (key == GLFW_KEY_F11 && action == GLFW_RELEASE)
                mainWindow.toggleFullscreen();
        });

        CallbackController.addCallback(null, (CounterUpdateCallback) (fps, ups) -> mainWindow.setTitle("RedBit - FPS: " + fps + " UPS: " + ups));

        FileStructure.discover(NoFileStructure.class);
        Application.launch(Engine.class, args);
    }
}
