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
    private final List<Collider> colliders = new ArrayList<Collider>();
    private final Player player = new Player();
    private double elapsedSeconds;

    public WorldScene() {
        objects.add(new SceneObject(Mesh.makeGround(36.0, new Color(56, 101, 58)), new Vec3(0.0, -0.05, 2.0)));
        objects.add(new SceneObject(Mesh.makeForestMist(new Color(154, 166, 198)), new Vec3(0.0, 0.02, 7.0)));

        addTree(-5.5, -4.0, 1.0);
        addTree(-2.2, -6.8, 0.9);
        addTree(3.4, -5.7, 1.1);
        addTree(6.8, -2.5, 0.95);
        addTree(-7.5, 1.8, 1.15);
        addTree(-3.8, 4.8, 0.85);
        addTree(2.3, 5.5, 1.0);
        addTree(7.0, 4.0, 1.2);
        addTree(-8.5, 10.0, 0.9);
        addTree(-1.0, 11.8, 1.1);
        addTree(5.8, 10.6, 0.95);

        objects.add(new SceneObject(Mesh.makeTempleBlock(new Color(120, 104, 88)), new Vec3(-9.5, 0.0, 14.0)));
        objects.add(new SceneObject(Mesh.makeCrystal(new Color(122, 186, 214)), new Vec3(9.3, 0.0, 13.0)));

        objects.add(player.getBody());
        objects.add(player.getSword());
    }

    public void update(Input input, Camera camera, double dt, double timeSeconds) {
        elapsedSeconds = timeSeconds;
        player.update(input, camera, this, dt, timeSeconds);
    }

    private void addTree(double x, double z, double scale) {
        objects.add(new SceneObject(Mesh.makeTree(scale), new Vec3(x, 0.0, z)));
        colliders.add(new Collider(new Vec3(x, 0.0, z), 0.42 * scale));
    }

    public List<SceneObject> getObjects() {
        return Collections.unmodifiableList(objects);
    }

    public List<Collider> getColliders() {
        return Collections.unmodifiableList(colliders);
    }

    public Player getPlayer() {
        return player;
    }

    public double getElapsedSeconds() {
        return elapsedSeconds;
    }
}
