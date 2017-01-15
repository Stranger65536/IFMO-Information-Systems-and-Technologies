package ru.ifmo.m3405;

import ru.ifmo.m3405.gui.MainFrame;

import javax.swing.*;

/**
 * @author trofiv
 */
public class Main {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            final MainFrame frame = new MainFrame("RoboCrasher");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}