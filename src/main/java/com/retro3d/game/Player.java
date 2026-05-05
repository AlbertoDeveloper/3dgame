package com.retro3d.game;

import com.retro3d.math.Vec3;
import com.retro3d.render.Mesh;
import com.retro3d.render.SceneObject;

import java.awt.Color;
import java.awt.event.KeyEvent;

public final class Player {
    private static final double BODY_RADIUS = 0.38;
    private static final double ATTACK_SECONDS = 0.32;

    private final SceneObject body;
    private final SceneObject sword;
    private Vec3 position = new Vec3(0.0, 0.0, 0.0);
    private double yaw;
    private double attackTimer;
    private int health = 5;
    private int maxHealth = 6;

    public Player() {
        body = new SceneObject(Mesh.makeHero(), position);
        sword = new SceneObject(Mesh.makeSword(new Color(208, 218, 226)), position);
        sword.setVisible(false);
    }

    public void update(Input input, Camera camera, WorldScene world, double dt, double elapsedSeconds) {
        double turnSpeed = 2.35 * dt;
        if (input.isDown(KeyEvent.VK_LEFT)) {
            yaw -= turnSpeed;
        }
        if (input.isDown(KeyEvent.VK_RIGHT)) {
            yaw += turnSpeed;
        }

        Vec3 move = movementFromInput(input, camera.getYaw());
        double speed = input.isDown(KeyEvent.VK_SHIFT) ? 4.4 : 2.7;

        if (move.lengthSquared() > 0.001) {
            yaw = Math.atan2(move.x, move.z);
            moveWithCollisions(move.normalized().scale(speed * dt), world);
        }

        if (input.wasPressed(KeyEvent.VK_SPACE) || input.wasPressed(KeyEvent.VK_CONTROL)) {
            attackTimer = ATTACK_SECONDS;
        }

        if (input.wasPressed(KeyEvent.VK_H)) {
            health = Math.max(0, health - 1);
        }
        if (input.wasPressed(KeyEvent.VK_J)) {
            health = Math.min(maxHealth, health + 1);
        }

        if (attackTimer > 0.0) {
            attackTimer = Math.max(0.0, attackTimer - dt);
        }

        syncObjects(elapsedSeconds);
    }

    private Vec3 movementFromInput(Input input, double cameraYaw) {
        Vec3 forward = new Vec3(Math.sin(cameraYaw), 0.0, Math.cos(cameraYaw));
        Vec3 right = new Vec3(Math.cos(cameraYaw), 0.0, -Math.sin(cameraYaw));
        Vec3 move = Vec3.ZERO;

        if (input.isDown(KeyEvent.VK_W)) {
            move = move.add(forward);
        }
        if (input.isDown(KeyEvent.VK_S)) {
            move = move.subtract(forward);
        }
        if (input.isDown(KeyEvent.VK_D)) {
            move = move.add(right);
        }
        if (input.isDown(KeyEvent.VK_A)) {
            move = move.subtract(right);
        }
        return move;
    }

    private void moveWithCollisions(Vec3 delta, WorldScene world) {
        Vec3 next = new Vec3(
                clamp(position.x + delta.x, -13.0, 13.0),
                position.y,
                clamp(position.z + delta.z, -13.0, 16.0)
        );

        for (int i = 0; i < world.getColliders().size(); i++) {
            Collider collider = world.getColliders().get(i);
            Vec3 offset = next.subtract(collider.getCenter());
            double minDistance = BODY_RADIUS + collider.getRadius();
            double flatDistance = Math.sqrt(offset.x * offset.x + offset.z * offset.z);

            if (flatDistance < minDistance && flatDistance > 0.0001) {
                double push = minDistance - flatDistance;
                next = new Vec3(
                        next.x + (offset.x / flatDistance) * push,
                        next.y,
                        next.z + (offset.z / flatDistance) * push
                );
            }
        }

        position = new Vec3(clamp(next.x, -13.0, 13.0), next.y, clamp(next.z, -13.0, 16.0));
    }

    private void syncObjects(double elapsedSeconds) {
        double bob = Math.sin(elapsedSeconds * 9.0) * 0.035;
        body.setPosition(new Vec3(position.x, bob, position.z));
        body.setRotationY(yaw);

        if (attackTimer > 0.0) {
            double t = 1.0 - attackTimer / ATTACK_SECONDS;
            double arc = -1.1 + t * 2.2;
            Vec3 forward = new Vec3(Math.sin(yaw + arc), 0.0, Math.cos(yaw + arc));
            Vec3 hand = position.add(forward.scale(0.72));
            sword.setPosition(new Vec3(hand.x, 0.88 + bob, hand.z));
            sword.setRotationY(yaw + arc);
            sword.setVisible(true);
        } else {
            Vec3 forward = new Vec3(Math.sin(yaw), 0.0, Math.cos(yaw));
            Vec3 right = new Vec3(Math.cos(yaw), 0.0, -Math.sin(yaw));
            Vec3 hand = position.add(forward.scale(0.42)).add(right.scale(0.44));
            sword.setPosition(new Vec3(hand.x, 0.86 + bob, hand.z));
            sword.setRotationY(yaw);
            sword.setVisible(true);
        }
    }

    public SceneObject getBody() {
        return body;
    }

    public SceneObject getSword() {
        return sword;
    }

    public Vec3 getPosition() {
        return position;
    }

    public double getYaw() {
        return yaw;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
