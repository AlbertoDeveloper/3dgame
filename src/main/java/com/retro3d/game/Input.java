package com.retro3d.game;

public final class Input {
    private final boolean[] keys = new boolean[512];
    private final boolean[] previous = new boolean[512];

    public void setDown(int keyCode, boolean down) {
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = down;
        }
    }

    public boolean isDown(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode];
    }

    public boolean wasPressed(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode] && !previous[keyCode];
    }

    public void endFrame() {
        System.arraycopy(keys, 0, previous, 0, keys.length);
    }
}
