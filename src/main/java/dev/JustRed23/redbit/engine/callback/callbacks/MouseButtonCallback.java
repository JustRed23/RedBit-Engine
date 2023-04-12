package dev.JustRed23.redbit.engine.callback.callbacks;

import dev.JustRed23.redbit.engine.callback.Callback;

public interface MouseButtonCallback extends Callback {
    void invoke(int button, int action, int mods);
}
