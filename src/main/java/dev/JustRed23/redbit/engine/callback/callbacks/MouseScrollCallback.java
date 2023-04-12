package dev.JustRed23.redbit.engine.callback.callbacks;

import dev.JustRed23.redbit.engine.callback.Callback;

public interface MouseScrollCallback extends Callback {
    void invoke(double yoffset);
}
