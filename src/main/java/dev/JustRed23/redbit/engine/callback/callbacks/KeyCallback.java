package dev.JustRed23.redbit.engine.callback.callbacks;

import dev.JustRed23.redbit.engine.callback.Callback;

public interface KeyCallback extends Callback {
    void invoke(int key, int action, int mods);
}
