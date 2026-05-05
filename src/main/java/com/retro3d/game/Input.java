package com.retro3d.game;

public final class Input {
    private final boolean[] keys = new boolean[512];

    public void setDown(int keyCode, boolean down) {
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = down;
        }
    }

    public boolean isDown(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode];
    }
}
