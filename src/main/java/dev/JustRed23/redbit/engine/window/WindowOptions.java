package dev.JustRed23.redbit.engine.window;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record WindowOptions(int width, int height, String title, boolean resizable, boolean fullscreen, boolean exitOnClose, String iconPath) {

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

    @Contract(" -> new")
    public static @NotNull WindowOptions getDefault() {
        return new WindowOptions(800, 600, "RedBit", false, false, true, null);
    }

    public int aspectRatio() {
        return aspectRatio;
    }
}
