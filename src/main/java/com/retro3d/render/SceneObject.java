package com.retro3d.render;

import com.retro3d.math.Vec3;

public final class SceneObject {
    private final Mesh mesh;
    private Vec3 position;
    private double rotationY;
    private boolean visible = true;

    public SceneObject(Mesh mesh, Vec3 position) {
        this.mesh = mesh;
        this.position = position;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public double getRotationY() {
        return rotationY;
    }

    public void setRotationY(double rotationY) {
        this.rotationY = rotationY;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
