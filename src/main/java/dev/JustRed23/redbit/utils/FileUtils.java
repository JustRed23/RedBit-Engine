package dev.JustRed23.redbit.utils;

import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class FileUtils {

    public static @Nullable String readFile(String path) {
        try (InputStream resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (resourceAsStream == null)
                return null;
            return new String(resourceAsStream.readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
