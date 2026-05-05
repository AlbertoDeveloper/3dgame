package com.retro3d.game;

import com.retro3d.math.Vec3;

import java.awt.event.KeyEvent;

public final class Camera {
    private Vec3 position = new Vec3(0.0, 1.25, -7.0);
    private double yaw;
    private double pitch;

    public void update(Input input, double dt) {
        double speed = input.isDown(KeyEvent.VK_SHIFT) ? 6.0 : 3.0;
        double move = speed * dt;
        double turn = 1.9 * dt;
        double look = 1.2 * dt;

        if (input.isDown(KeyEvent.VK_LEFT)) {
            yaw -= turn;
        }
        if (input.isDown(KeyEvent.VK_RIGHT)) {
            yaw += turn;
        }
        if (input.isDown(KeyEvent.VK_UP)) {
            pitch = Math.max(-0.7, pitch - look);
        }
        if (input.isDown(KeyEvent.VK_DOWN)) {
            pitch = Math.min(0.55, pitch + look);
        }

        Vec3 forward = new Vec3(Math.sin(yaw), 0.0, Math.cos(yaw));
        Vec3 right = new Vec3(Math.cos(yaw), 0.0, -Math.sin(yaw));
        Vec3 velocity = Vec3.ZERO;

        if (input.isDown(KeyEvent.VK_W)) {
            velocity = velocity.add(forward);
        }
        if (input.isDown(KeyEvent.VK_S)) {
            velocity = velocity.subtract(forward);
        }
        if (input.isDown(KeyEvent.VK_D) || input.isDown(KeyEvent.VK_E)) {
            velocity = velocity.add(right);
        }
        if (input.isDown(KeyEvent.VK_A) || input.isDown(KeyEvent.VK_Q)) {
            velocity = velocity.subtract(right);
        }

        if (velocity.lengthSquared() > 0.001) {
            position = position.add(velocity.normalized().scale(move));
        }
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
