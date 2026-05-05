package com.retro3d.tools;

import com.retro3d.game.Camera;
import com.retro3d.game.WorldScene;
import com.retro3d.render.Renderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public final class RenderSmokeTest {
    private RenderSmokeTest() {
    }

    public static void main(String[] args) throws Exception {
        Renderer renderer = new Renderer(320, 240);
        WorldScene world = new WorldScene();
        Camera camera = new Camera();
        world.update(new com.retro3d.game.Input(), camera, 1.0 / 60.0, 1.0);
        camera.update(new com.retro3d.game.Input(), world.getPlayer(), 1.0 / 60.0);
        BufferedImage image = renderer.render(world, camera, 1.0);
        Set<Integer> colors = new HashSet<Integer>();

        for (int y = 0; y < image.getHeight(); y += 4) {
            for (int x = 0; x < image.getWidth(); x += 4) {
                colors.add(image.getRGB(x, y));
            }
        }

        if (colors.size() < 8) {
            throw new IllegalStateException("Renderer smoke test failed: frame has too few distinct colors.");
        }

        File outputDir = new File("build/screenshots");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Could not create " + outputDir.getAbsolutePath());
        }

        File output = new File(outputDir, "smoke.png");
        ImageIO.write(image, "png", output);
        System.out.println("Renderer smoke test passed: " + output.getAbsolutePath());
    }
}
