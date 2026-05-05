package com.retro3d.game;

import com.retro3d.math.Vec3;

public final class Collider {
    private final Vec3 center;
    private final double radius;

    public Collider(Vec3 center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Vec3 getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}
