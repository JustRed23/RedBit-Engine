package dev.JustRed23.redbit;

import dev.JustRed23.stonebrick.app.Application;

public class Engine extends Application {

    protected void init() throws Exception {

    }

    protected void start() throws Exception {

    }

    protected void stop() throws Exception {

    }

    public static void launch(String[] args) {
        Application.launch(args);
    }

    public static void kill() {
        Engine.exit();
    }
}
