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
        camera.update(input, dt);
        world.update(elapsedSeconds);

        if (input.isDown(KeyEvent.VK_ESCAPE)) {
            stop();
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }
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
        } finally {
            g2.dispose();
        }
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
