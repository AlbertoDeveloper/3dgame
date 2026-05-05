package com.retro3d.render;

import com.retro3d.math.Vec3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Mesh {
    private final List<Triangle> triangles;

    public Mesh(List<Triangle> triangles) {
        this.triangles = Collections.unmodifiableList(new ArrayList<Triangle>(triangles));
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public static Mesh makeGround(double size, Color color) {
        double s = size * 0.5;
        List<Triangle> tris = new ArrayList<Triangle>();
        tris.add(new Triangle(new Vec3(-s, 0.0, -s), new Vec3(s, 0.0, -s), new Vec3(s, 0.0, s), color));
        tris.add(new Triangle(new Vec3(-s, 0.0, -s), new Vec3(s, 0.0, s), new Vec3(-s, 0.0, s), color));
        return new Mesh(tris);
    }

    public static Mesh makeGrid(double size, int steps, Color color) {
        List<Triangle> tris = new ArrayList<Triangle>();
        double half = size * 0.5;
        double thickness = 0.025;
        double step = size / steps;

        for (int i = 0; i <= steps; i++) {
            double p = -half + step * i;
            addFlatBox(tris, new Vec3(p, 0.015, 0.0), thickness, thickness, size, color);
            addFlatBox(tris, new Vec3(0.0, 0.018, p), size, thickness, thickness, color);
        }
        return new Mesh(tris);
    }

    public static Mesh makeTempleBlock(Color color) {
        List<Triangle> tris = new ArrayList<Triangle>();
        addBox(tris, new Vec3(0.0, 0.55, 0.0), 2.5, 1.1, 2.5, color);
        addBox(tris, new Vec3(0.0, 1.35, 0.0), 1.6, 0.55, 1.6, color.brighter());
        addBox(tris, new Vec3(0.0, 1.82, 0.0), 0.9, 0.35, 0.9, color.darker());
        return new Mesh(tris);
    }

    public static Mesh makePyramid(Color color) {
        List<Triangle> tris = new ArrayList<Triangle>();
        Vec3 a = new Vec3(-1.8, 0.0, -1.8);
        Vec3 b = new Vec3(1.8, 0.0, -1.8);
        Vec3 c = new Vec3(1.8, 0.0, 1.8);
        Vec3 d = new Vec3(-1.8, 0.0, 1.8);
        Vec3 top = new Vec3(0.0, 2.4, 0.0);
        tris.add(new Triangle(a, b, top, color));
        tris.add(new Triangle(b, c, top, color.brighter()));
        tris.add(new Triangle(c, d, top, color));
        tris.add(new Triangle(d, a, top, color.darker()));
        tris.add(new Triangle(a, c, b, color.darker().darker()));
        tris.add(new Triangle(a, d, c, color.darker().darker()));
        return new Mesh(tris);
    }

    public static Mesh makeCrystal(Color color) {
        List<Triangle> tris = new ArrayList<Triangle>();
        Vec3 top = new Vec3(0.0, 1.8, 0.0);
        Vec3 bottom = new Vec3(0.0, -0.1, 0.0);
        Vec3 a = new Vec3(-0.45, 0.65, 0.0);
        Vec3 b = new Vec3(0.0, 0.65, -0.45);
        Vec3 c = new Vec3(0.45, 0.65, 0.0);
        Vec3 d = new Vec3(0.0, 0.65, 0.45);
        tris.add(new Triangle(a, b, top, color.brighter()));
        tris.add(new Triangle(b, c, top, color));
        tris.add(new Triangle(c, d, top, color.brighter()));
        tris.add(new Triangle(d, a, top, color));
        tris.add(new Triangle(b, a, bottom, color.darker()));
        tris.add(new Triangle(c, b, bottom, color.darker()));
        tris.add(new Triangle(d, c, bottom, color.darker()));
        tris.add(new Triangle(a, d, bottom, color.darker()));
        return new Mesh(tris);
    }

    public static Mesh makeShip(Color color) {
        List<Triangle> tris = new ArrayList<Triangle>();
        Vec3 nose = new Vec3(0.0, 0.0, 1.0);
        Vec3 left = new Vec3(-0.75, -0.15, -0.55);
        Vec3 right = new Vec3(0.75, -0.15, -0.55);
        Vec3 top = new Vec3(0.0, 0.35, -0.25);
        Vec3 bottom = new Vec3(0.0, -0.35, -0.25);
        tris.add(new Triangle(left, nose, top, color.brighter()));
        tris.add(new Triangle(nose, right, top, color));
        tris.add(new Triangle(nose, left, bottom, color.darker()));
        tris.add(new Triangle(right, nose, bottom, color.darker()));
        tris.add(new Triangle(left, top, bottom, color));
        tris.add(new Triangle(top, right, bottom, color));
        return new Mesh(tris);
    }

    public static Mesh makeTree(double scale) {
        List<Triangle> tris = new ArrayList<Triangle>();
        Color bark = new Color(91, 78, 47);
        Color leaves = new Color(74, 119, 76);
        addBox(tris, new Vec3(0.0, 1.25 * scale, 0.0), 0.48 * scale, 2.5 * scale, 0.48 * scale, bark);
        addPyramid(tris, new Vec3(0.0, 2.6 * scale, 0.0), 2.4 * scale, 1.65 * scale, leaves.darker());
        addPyramid(tris, new Vec3(0.0, 3.35 * scale, 0.0), 1.75 * scale, 1.45 * scale, leaves);
        addPyramid(tris, new Vec3(0.0, 3.95 * scale, 0.0), 1.15 * scale, 1.0 * scale, leaves.brighter());
        return new Mesh(tris);
    }

    public static Mesh makeForestMist(Color color) {
        List<Triangle> tris = new ArrayList<Triangle>();
        for (int i = 0; i < 8; i++) {
            double z = -12.0 + i * 4.2;
            double y = 0.15 + (i % 3) * 0.05;
            Color band = new Color(
                    Math.max(0, Math.min(255, color.getRed() - i * 4)),
                    Math.max(0, Math.min(255, color.getGreen() - i * 3)),
                    Math.max(0, Math.min(255, color.getBlue()))
            );
            addFlatBox(tris, new Vec3(0.0, y, z), 28.0, 0.02, 0.38, band);
        }
        return new Mesh(tris);
    }

    public static Mesh makeHero() {
        List<Triangle> tris = new ArrayList<Triangle>();
        Color tunic = new Color(50, 146, 53);
        Color skin = new Color(220, 165, 98);
        Color boots = new Color(103, 64, 43);
        Color shield = new Color(38, 71, 164);
        Color hair = new Color(222, 178, 50);

        addBox(tris, new Vec3(0.0, 0.75, 0.0), 0.58, 0.85, 0.35, tunic);
        addBox(tris, new Vec3(0.0, 1.35, 0.0), 0.38, 0.34, 0.34, skin);
        addPyramid(tris, new Vec3(0.0, 1.66, -0.07), 0.65, 0.55, tunic.brighter());
        addBox(tris, new Vec3(-0.23, 0.18, 0.0), 0.18, 0.42, 0.2, boots);
        addBox(tris, new Vec3(0.23, 0.18, 0.0), 0.18, 0.42, 0.2, boots);
        addBox(tris, new Vec3(-0.42, 0.78, 0.0), 0.16, 0.56, 0.16, skin.darker());
        addBox(tris, new Vec3(0.42, 0.78, 0.0), 0.16, 0.56, 0.16, skin.darker());
        addBox(tris, new Vec3(0.0, 1.47, -0.13), 0.48, 0.18, 0.12, hair);
        addBox(tris, new Vec3(-0.02, 0.82, -0.28), 0.58, 0.62, 0.08, shield);
        addBox(tris, new Vec3(-0.02, 0.82, -0.335), 0.36, 0.44, 0.04, new Color(210, 214, 224));
        return new Mesh(tris);
    }

    public static Mesh makeSword(Color color) {
        List<Triangle> tris = new ArrayList<Triangle>();
        addBox(tris, new Vec3(0.0, 0.0, 0.55), 0.08, 0.08, 1.15, color);
        addBox(tris, new Vec3(0.0, 0.0, -0.08), 0.52, 0.1, 0.1, new Color(108, 78, 41));
        addBox(tris, new Vec3(0.0, -0.02, -0.28), 0.14, 0.16, 0.28, new Color(86, 54, 32));
        return new Mesh(tris);
    }

    private static void addFlatBox(List<Triangle> tris, Vec3 center, double width, double height, double depth, Color color) {
        addBox(tris, center, width, height, depth, color);
    }

    private static void addBox(List<Triangle> tris, Vec3 center, double width, double height, double depth, Color color) {
        double x = width * 0.5;
        double y = height * 0.5;
        double z = depth * 0.5;

        Vec3 p000 = center.add(new Vec3(-x, -y, -z));
        Vec3 p001 = center.add(new Vec3(-x, -y, z));
        Vec3 p010 = center.add(new Vec3(-x, y, -z));
        Vec3 p011 = center.add(new Vec3(-x, y, z));
        Vec3 p100 = center.add(new Vec3(x, -y, -z));
        Vec3 p101 = center.add(new Vec3(x, -y, z));
        Vec3 p110 = center.add(new Vec3(x, y, -z));
        Vec3 p111 = center.add(new Vec3(x, y, z));

        addQuad(tris, p000, p100, p110, p010, color.darker());
        addQuad(tris, p101, p001, p011, p111, color);
        addQuad(tris, p001, p000, p010, p011, color.darker());
        addQuad(tris, p100, p101, p111, p110, color.brighter());
        addQuad(tris, p010, p110, p111, p011, color.brighter());
        addQuad(tris, p001, p101, p100, p000, color.darker().darker());
    }

    private static void addQuad(List<Triangle> tris, Vec3 a, Vec3 b, Vec3 c, Vec3 d, Color color) {
        tris.add(new Triangle(a, b, c, color));
        tris.add(new Triangle(a, c, d, color));
    }

    private static void addPyramid(List<Triangle> tris, Vec3 center, double size, double height, Color color) {
        double s = size * 0.5;
        double baseY = center.y - height * 0.5;
        Vec3 a = new Vec3(center.x - s, baseY, center.z - s);
        Vec3 b = new Vec3(center.x + s, baseY, center.z - s);
        Vec3 c = new Vec3(center.x + s, baseY, center.z + s);
        Vec3 d = new Vec3(center.x - s, baseY, center.z + s);
        Vec3 top = new Vec3(center.x, center.y + height * 0.5, center.z);

        tris.add(new Triangle(a, b, top, color.darker()));
        tris.add(new Triangle(b, c, top, color));
        tris.add(new Triangle(c, d, top, color.brighter()));
        tris.add(new Triangle(d, a, top, color));
        tris.add(new Triangle(a, c, b, color.darker().darker()));
        tris.add(new Triangle(a, d, c, color.darker().darker()));
    }
}
