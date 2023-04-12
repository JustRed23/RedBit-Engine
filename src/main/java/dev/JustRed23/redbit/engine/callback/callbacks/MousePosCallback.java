package dev.JustRed23.redbit.engine.callback.callbacks;

import dev.JustRed23.redbit.engine.callback.Callback;

public interface MousePosCallback extends Callback {
    void invoke(double x, double y);
}
