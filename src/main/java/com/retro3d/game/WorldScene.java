package com.retro3d.game;

import com.retro3d.math.Vec3;
import com.retro3d.render.Mesh;
import com.retro3d.render.SceneObject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class WorldScene {
    private final List<SceneObject> objects = new ArrayList<SceneObject>();
    private final SceneObject playerMarker;

    public WorldScene() {
        objects.add(new SceneObject(Mesh.makeGround(42.0, new Color(73, 108, 64)), new Vec3(0.0, -0.05, 8.0)));
        objects.add(new SceneObject(Mesh.makeGrid(42.0, 6, new Color(32, 46, 45)), new Vec3(0.0, 0.0, 8.0)));

        objects.add(new SceneObject(Mesh.makeTempleBlock(new Color(151, 110, 78)), new Vec3(-3.2, 0.0, 8.2)));
        objects.add(new SceneObject(Mesh.makeTempleBlock(new Color(115, 126, 152)), new Vec3(3.0, 0.0, 11.0)));
        objects.add(new SceneObject(Mesh.makePyramid(new Color(172, 91, 72)), new Vec3(0.0, 0.0, 15.0)));

        for (int i = -3; i <= 3; i++) {
            objects.add(new SceneObject(Mesh.makeCrystal(new Color(70, 150, 175)), new Vec3(i * 2.2, 0.0, 20.0 + Math.abs(i))));
        }

        playerMarker = new SceneObject(Mesh.makeShip(new Color(224, 188, 82)), new Vec3(0.0, 1.0, 9.0));
        objects.add(playerMarker);
    }

    public void update(double timeSeconds) {
        playerMarker.setRotationY(timeSeconds * 1.4);
        playerMarker.setPosition(new Vec3(Math.sin(timeSeconds) * 1.4, 1.15 + Math.sin(timeSeconds * 2.0) * 0.15, 9.0));
    }

    public List<SceneObject> getObjects() {
        return Collections.unmodifiableList(objects);
    }
}
