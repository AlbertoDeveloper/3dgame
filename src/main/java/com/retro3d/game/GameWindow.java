package com.retro3d.game;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class GameWindow extends JFrame {
    private final GamePanel panel;

    public GameWindow() {
        super("Retro 3D Java Prototype");
        this.panel = new GamePanel();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                panel.stop();
            }
        });
    }

    public void start() {
        panel.start();
    }
}
