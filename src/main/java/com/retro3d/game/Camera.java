package com.retro3d.game;

import com.retro3d.math.Vec3;

import java.awt.event.KeyEvent;

public final class Camera {
    private Vec3 position = new Vec3(0.0, 2.2, -5.2);
    private double yaw;
    private double pitch = 0.03;

    public void update(Input input, Player player, double dt) {
        double look = 1.2 * dt;

        if (input.isDown(KeyEvent.VK_UP)) {
            pitch = Math.max(-0.35, pitch - look);
        }
        if (input.isDown(KeyEvent.VK_DOWN)) {
            pitch = Math.min(0.28, pitch + look);
        }

        Vec3 target = player.getPosition().add(new Vec3(0.0, 1.05, 0.0));
        Vec3 offset = new Vec3(-Math.sin(yaw) * 5.2, 1.75, -Math.cos(yaw) * 5.2);
        position = target.add(offset);
    }

    public Vec3 getPosition() {
        return position;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }
}
