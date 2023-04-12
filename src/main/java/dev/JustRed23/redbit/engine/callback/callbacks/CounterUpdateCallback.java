package dev.JustRed23.redbit.engine.callback.callbacks;

import dev.JustRed23.redbit.engine.callback.Callback;

public interface CounterUpdateCallback extends Callback {
    void invoke(int framesPerSecond, int updatesPerSecond);
}
