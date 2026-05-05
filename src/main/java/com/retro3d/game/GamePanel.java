package com.retro3d.game;

import com.retro3d.render.Renderer;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public final class GamePanel extends JPanel implements Runnable, KeyListener {
    private static final int VIEW_WIDTH = 320;
    private static final int VIEW_HEIGHT = 240;
    private static final int SCALE = 3;
    private static final double TARGET_SECONDS = 1.0 / 60.0;

    private final Input input = new Input();
    private final Camera camera = new Camera();
    private final WorldScene world = new WorldScene();
    private final Renderer renderer = new Renderer(VIEW_WIDTH, VIEW_HEIGHT);

    private Thread loopThread;
    private volatile boolean running;
    private BufferedImage frame;
    private double elapsedSeconds;

    public GamePanel() {
        setPreferredSize(new Dimension(VIEW_WIDTH * SCALE, VIEW_HEIGHT * SCALE));
        setFocusable(true);
        setBackground(Color.BLACK);
        addKeyListener(this);
    }

    public synchronized void start() {
        if (running) {
            return;
        }

        running = true;
        loopThread = new Thread(this, "retro3d-main-loop");
        loopThread.start();
        requestFocusInWindow();
    }

    public synchronized void stop() {
        running = false;
    }

    @Override
    public void run() {
        long previous = System.nanoTime();
        double accumulator = 0.0;

        while (running) {
            long now = System.nanoTime();
            double delta = (now - previous) / 1_000_000_000.0;
            previous = now;
            accumulator += Math.min(delta, 0.25);

            while (accumulator >= TARGET_SECONDS) {
                update(TARGET_SECONDS);
                accumulator -= TARGET_SECONDS;
            }

            renderFrame();
            repaint();
            sleepBriefly();
        }
    }

    private void update(double dt) {
        elapsedSeconds += dt;
        world.update(input, camera, dt, elapsedSeconds);
        camera.update(input, world.getPlayer(), dt);

        if (input.isDown(KeyEvent.VK_ESCAPE)) {
            stop();
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }

        input.endFrame();
    }

    private void renderFrame() {
        frame = renderer.render(world, camera, elapsedSeconds);
    }

    private void sleepBriefly() {
        try {
            Thread.sleep(2L);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            if (frame != null) {
                g2.drawImage(frame, 0, 0, getWidth(), getHeight(), null);
            }
            drawHud(g2);
        } finally {
            g2.dispose();
        }
    }

    private void drawHud(Graphics2D g2) {
        int scale = SCALE;
        int hearts = world.getPlayer().getMaxHealth();
        int health = world.getPlayer().getHealth();
        for (int i = 0; i < hearts; i++) {
            int x = 10 + i * 17;
            int y = 10;
            Color color = i < health ? new Color(218, 42, 54) : new Color(76, 39, 52);
            g2.setColor(new Color(38, 18, 24));
            g2.fillOval(x * scale - 1, y * scale - 1, 8 * scale + 2, 8 * scale + 2);
            g2.fillOval((x + 6) * scale - 1, y * scale - 1, 8 * scale + 2, 8 * scale + 2);
            g2.fillPolygon(
                    new int[]{(x - 1) * scale, (x + 15) * scale, (x + 7) * scale},
                    new int[]{(y + 5) * scale, (y + 5) * scale, (y + 16) * scale},
                    3
            );
            g2.setColor(color);
            g2.fillOval(x * scale, y * scale, 8 * scale, 8 * scale);
            g2.fillOval((x + 6) * scale, y * scale, 8 * scale, 8 * scale);
            g2.fillPolygon(
                    new int[]{x * scale, (x + 14) * scale, (x + 7) * scale},
                    new int[]{(y + 5) * scale, (y + 5) * scale, (y + 15) * scale},
                    3
            );
        }

        g2.setColor(new Color(24, 31, 46));
        g2.fillOval((VIEW_WIDTH - 54) * scale, 10 * scale, 36 * scale, 36 * scale);
        g2.setColor(new Color(86, 109, 148));
        g2.drawOval((VIEW_WIDTH - 54) * scale, 10 * scale, 36 * scale, 36 * scale);
        g2.setColor(new Color(210, 218, 228));
        g2.fillRect((VIEW_WIDTH - 38) * scale, 18 * scale, 3 * scale, 20 * scale);
        g2.fillRect((VIEW_WIDTH - 45) * scale, 25 * scale, 17 * scale, 3 * scale);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        input.setDown(event.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        input.setDown(event.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }
}
