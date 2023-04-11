import dev.JustRed23.redbit.engine.Engine;
import dev.JustRed23.redbit.engine.window.WindowController;
import dev.JustRed23.redbit.engine.window.WindowOptions;
import dev.JustRed23.stonebrick.app.Application;

public class Starter {

    private static boolean debug = false;

    public static void main(String[] args) {
        WindowController.createWindow(new WindowOptions(1024, 600, "RedBit", false, false));
        Application.launch(Engine.class, args);
    }
}
