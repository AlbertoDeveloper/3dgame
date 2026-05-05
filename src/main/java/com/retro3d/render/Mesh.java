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
        Color tunic = new Color(42, 151, 48);
        Color tunicDark = new Color(28, 99, 39);
        Color skin = new Color(222, 172, 98);
        Color boots = new Color(108, 55, 35);
        Color glove = new Color(132, 72, 39);
        Color shieldBlue = new Color(31, 76, 174);
        Color shieldRim = new Color(178, 190, 202);
        Color crest = new Color(233, 210, 71);
        Color hair = new Color(220, 132, 35);

        addBox(tris, new Vec3(0.0, 0.82, 0.02), 0.48, 0.72, 0.32, tunic);
        addPyramid(tris, new Vec3(0.0, 0.48, 0.03), 0.72, 0.42, tunicDark);
        addBox(tris, new Vec3(0.0, 1.17, 0.0), 0.56, 0.16, 0.36, tunic.brighter());

        addBox(tris, new Vec3(-0.2, 0.25, 0.0), 0.17, 0.5, 0.18, boots);
        addBox(tris, new Vec3(0.2, 0.25, 0.0), 0.17, 0.5, 0.18, boots);
        addBox(tris, new Vec3(-0.2, 0.54, 0.02), 0.15, 0.28, 0.15, new Color(222, 104, 48));
        addBox(tris, new Vec3(0.2, 0.54, 0.02), 0.15, 0.28, 0.15, new Color(222, 104, 48));

        addBox(tris, new Vec3(-0.42, 0.85, 0.02), 0.14, 0.48, 0.14, glove);
        addBox(tris, new Vec3(0.43, 0.83, 0.12), 0.14, 0.46, 0.14, glove);
        addBox(tris, new Vec3(0.47, 0.82, 0.36), 0.13, 0.15, 0.18, skin);

        addBox(tris, new Vec3(0.0, 1.42, 0.04), 0.38, 0.38, 0.34, skin);
        addBox(tris, new Vec3(-0.31, 1.42, 0.02), 0.12, 0.14, 0.08, skin);
        addBox(tris, new Vec3(0.31, 1.42, 0.02), 0.12, 0.14, 0.08, skin);
        addBox(tris, new Vec3(0.0, 1.57, 0.12), 0.44, 0.12, 0.16, hair);
        addBox(tris, new Vec3(-0.21, 1.39, 0.16), 0.12, 0.34, 0.14, hair);
        addBox(tris, new Vec3(0.21, 1.39, 0.16), 0.12, 0.34, 0.14, hair);
        addBox(tris, new Vec3(0.0, 1.37, 0.245), 0.08, 0.05, 0.035, new Color(82, 45, 34));

        addPyramid(tris, new Vec3(0.0, 1.78, -0.12), 0.54, 0.52, tunic);
        addBox(tris, new Vec3(0.0, 1.62, -0.28), 0.32, 0.18, 0.34, tunicDark);

        addBox(tris, new Vec3(0.0, 0.92, -0.29), 0.72, 0.78, 0.08, shieldRim);
        addBox(tris, new Vec3(0.0, 0.92, -0.34), 0.56, 0.62, 0.05, shieldBlue);
        addBox(tris, new Vec3(0.0, 1.03, -0.375), 0.28, 0.1, 0.035, crest);
        addBox(tris, new Vec3(-0.1, 0.88, -0.375), 0.12, 0.22, 0.035, crest);
        addBox(tris, new Vec3(0.1, 0.88, -0.375), 0.12, 0.22, 0.035, crest);
        return new Mesh(tris);
    }

    public static Mesh makeSword(Color color) {
        List<Triangle> tris = new ArrayList<Triangle>();
        addBox(tris, new Vec3(0.0, 0.0, 0.58), 0.065, 0.07, 1.18, color);
        addPyramid(tris, new Vec3(0.0, 0.0, 1.24), 0.12, 0.24, color.brighter());
        addBox(tris, new Vec3(0.0, 0.0, -0.08), 0.5, 0.1, 0.1, new Color(118, 75, 37));
        addBox(tris, new Vec3(0.0, -0.02, -0.28), 0.13, 0.15, 0.3, new Color(80, 49, 31));
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
