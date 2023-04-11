package dev.JustRed23.redbit.engine.window;

public record WindowOptions(int width, int height, String title, boolean resizable, boolean fullscreen) {

    private static int aspectRatio;

    public WindowOptions {
        if (width < 0)
            throw new IllegalArgumentException("Width cannot be negative");
        if (height < 0)
            throw new IllegalArgumentException("Height cannot be negative");
        if (title == null)
            throw new IllegalArgumentException("Title cannot be null");
        aspectRatio = width / height;
    }

    public static WindowOptions getDefault() {
        return new WindowOptions(800, 600, "RedBit", false, false);
    }

    public int aspectRatio() {
        return aspectRatio;
    }
}
