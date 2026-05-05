package com.retro3d.render;

import com.retro3d.game.Camera;
import com.retro3d.game.WorldScene;
import com.retro3d.math.Vec3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public final class Renderer {
    private static final double NEAR = 0.08;
    private static final double FOV = 190.0;
    private static final Vec3 LIGHT = new Vec3(-0.35, -0.8, -0.45).normalized();

    private final int width;
    private final int height;
    private final BufferedImage image;
    private final int[] pixels;
    private final double[] depth;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.pixels = new int[width * height];
        this.depth = new double[width * height];
    }

    public BufferedImage render(WorldScene world, Camera camera, double timeSeconds) {
        clear();
        drawHorizon();

        List<SceneObject> objects = world.getObjects();
        for (int i = 0; i < objects.size(); i++) {
            SceneObject object = objects.get(i);
            List<Triangle> triangles = object.getMesh().getTriangles();
            for (int t = 0; t < triangles.size(); t++) {
                drawTriangle(object, triangles.get(t), camera, timeSeconds);
            }
        }

        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }

    private void clear() {
        Arrays.fill(depth, Double.POSITIVE_INFINITY);
        Arrays.fill(pixels, new Color(22, 28, 38).getRGB());
    }

    private void drawHorizon() {
        for (int y = 0; y < height; y++) {
            double k = (double) y / (double) (height - 1);
            Color top = new Color(42, 61, 87);
            Color bottom = new Color(98, 82, 71);
            int rgb = mix(top, bottom, k).getRGB();
            for (int x = 0; x < width; x++) {
                pixels[y * width + x] = rgb;
            }
        }
    }

    private void drawTriangle(SceneObject object, Triangle triangle, Camera camera, double timeSeconds) {
        Vec3 wa = toWorld(object, triangle.a, timeSeconds);
        Vec3 wb = toWorld(object, triangle.b, timeSeconds);
        Vec3 wc = toWorld(object, triangle.c, timeSeconds);

        Vec3 normal = wb.subtract(wa).cross(wc.subtract(wa)).normalized();
        if (normal.lengthSquared() < 0.00001) {
            return;
        }

        Vec3 ca = toCamera(wa, camera);
        Vec3 cb = toCamera(wb, camera);
        Vec3 cc = toCamera(wc, camera);

        if (ca.z <= NEAR || cb.z <= NEAR || cc.z <= NEAR) {
            return;
        }

        ScreenVertex a = project(ca);
        ScreenVertex b = project(cb);
        ScreenVertex c = project(cc);

        double winding = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
        if (winding >= 0.0) {
            return;
        }

        Color lit = applyLighting(triangle.color, normal);
        rasterize(a, b, c, lit.getRGB());
    }

    private Vec3 toWorld(SceneObject object, Vec3 point, double timeSeconds) {
        double angle = object.getRotationY();
        double wobble = Math.sin(timeSeconds * 4.0 + point.x * 2.3 + point.z) * 0.012;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = point.x * cos - point.z * sin;
        double z = point.x * sin + point.z * cos;

        Vec3 world = new Vec3(x + wobble, point.y, z - wobble).add(object.getPosition());

        // Quantization creates subtle vertex shimmer similar to early 3D hardware.
        double snap = 1.0 / 80.0;
        return new Vec3(
                Math.round(world.x / snap) * snap,
                Math.round(world.y / snap) * snap,
                Math.round(world.z / snap) * snap
        );
    }

    private Vec3 toCamera(Vec3 point, Camera camera) {
        Vec3 translated = point.subtract(camera.getPosition());

        double yaw = -camera.getYaw();
        double cosY = Math.cos(yaw);
        double sinY = Math.sin(yaw);
        double x = translated.x * cosY - translated.z * sinY;
        double z = translated.x * sinY + translated.z * cosY;

        double pitch = -camera.getPitch();
        double cosP = Math.cos(pitch);
        double sinP = Math.sin(pitch);
        double y = translated.y * cosP - z * sinP;
        double zp = translated.y * sinP + z * cosP;
        return new Vec3(x, y, zp);
    }

    private ScreenVertex project(Vec3 point) {
        double sx = width * 0.5 + (point.x / point.z) * FOV;
        double sy = height * 0.5 - (point.y / point.z) * FOV;

        // Snap screen coordinates to whole pixels for a crunchy fixed-point feel.
        return new ScreenVertex(Math.round(sx), Math.round(sy), point.z);
    }

    private Color applyLighting(Color base, Vec3 normal) {
        double shade = 0.45 + Math.max(0.0, -normal.dot(LIGHT)) * 0.7;
        shade = Math.max(0.25, Math.min(1.15, shade));
        return new Color(
                clampColor((int) (base.getRed() * shade)),
                clampColor((int) (base.getGreen() * shade)),
                clampColor((int) (base.getBlue() * shade))
        );
    }

    private void rasterize(ScreenVertex a, ScreenVertex b, ScreenVertex c, int rgb) {
        int minX = Math.max(0, (int) Math.floor(Math.min(a.x, Math.min(b.x, c.x))));
        int maxX = Math.min(width - 1, (int) Math.ceil(Math.max(a.x, Math.max(b.x, c.x))));
        int minY = Math.max(0, (int) Math.floor(Math.min(a.y, Math.min(b.y, c.y))));
        int maxY = Math.min(height - 1, (int) Math.ceil(Math.max(a.y, Math.max(b.y, c.y))));

        double area = edge(a.x, a.y, b.x, b.y, c.x, c.y);
        if (Math.abs(area) < 0.00001) {
            return;
        }

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                double px = x + 0.5;
                double py = y + 0.5;
                double w0 = edge(b.x, b.y, c.x, c.y, px, py);
                double w1 = edge(c.x, c.y, a.x, a.y, px, py);
                double w2 = edge(a.x, a.y, b.x, b.y, px, py);

                if (sameSign(w0, area) && sameSign(w1, area) && sameSign(w2, area)) {
                    w0 /= area;
                    w1 /= area;
                    w2 /= area;
                    double z = a.z * w0 + b.z * w1 + c.z * w2;
                    int index = y * width + x;
                    if (z < depth[index]) {
                        depth[index] = z;
                        pixels[index] = rgb;
                    }
                }
            }
        }
    }

    private static double edge(double ax, double ay, double bx, double by, double px, double py) {
        return (px - ax) * (by - ay) - (py - ay) * (bx - ax);
    }

    private static boolean sameSign(double value, double reference) {
        return reference < 0.0 ? value <= 0.0 : value >= 0.0;
    }

    private static Color mix(Color a, Color b, double t) {
        return new Color(
                clampColor((int) (a.getRed() + (b.getRed() - a.getRed()) * t)),
                clampColor((int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t)),
                clampColor((int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t))
        );
    }

    private static int clampColor(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private static final class ScreenVertex {
        final double x;
        final double y;
        final double z;

        ScreenVertex(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
