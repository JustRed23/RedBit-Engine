package dev.JustRed23.redbit.engine.err;

public class WindowInitException extends Exception {

    public WindowInitException(String message) {
        super(message);
    }

    public WindowInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
