package com.retro3d.render;

import com.retro3d.math.Vec3;

import java.awt.Color;

public final class Triangle {
    public final Vec3 a;
    public final Vec3 b;
    public final Vec3 c;
    public final Color color;

    public Triangle(Vec3 a, Vec3 b, Vec3 c, Color color) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.color = color;
    }
}
