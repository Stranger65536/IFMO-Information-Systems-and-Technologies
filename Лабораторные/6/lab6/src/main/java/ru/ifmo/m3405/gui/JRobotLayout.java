package ru.ifmo.m3405.gui;

import ru.ifmo.m3405.Robot;

import javax.swing.*;
import java.awt.*;


public final class JRobotLayout extends JPanel {
    private static final int BORDER_GAP = 70;
    private static final int GRAPH_POINT_WIDTH = 12;
    private static final int ROBOT_SPACING = 20;
    private static final String CAN_T_RETURN_A_SPEED_OF_UNRECOGNIZED_ROBOT = "Can't return a speed of unrecognized robot";

    private Robot firstRobot;
    private Robot secondRobot;
    private double firstRobotPosition;
    private double secondRobotPosition;

    public JRobotLayout(final Robot first, final Robot second) {
        this.reset(first, second);
    }

    public void reset(final Robot first, final Robot second) {
        this.firstRobot = first;
        this.firstRobotPosition = first.getInitialDistanceToCross();
        this.secondRobot = second;
        this.secondRobotPosition = second.getInitialDistanceToCross();
        this.repaint();
    }

    public void setRobotSpeed(final String uuid, final int angleSpeed) {
        if (firstRobot.getUuid().equals(uuid)) {
            firstRobot = new Robot(firstRobot.getUuid(), firstRobot.getInitialDistanceToCross(),
                    angleSpeed, firstRobot.getHeight(), firstRobot.getWidth());
        } else if (secondRobot.getUuid().equals(uuid)) {
            secondRobot = new Robot(secondRobot.getUuid(), secondRobot.getInitialDistanceToCross(),
                    angleSpeed, secondRobot.getHeight(), secondRobot.getWidth());
        } else {
            throw new IllegalArgumentException("Can't change speed of unrecognized robot");
        }
    }

    public double getRobotPosition(final String uuid) {
        if (firstRobot.getUuid().equals(uuid)) {
            return firstRobotPosition;
        } else if (secondRobot.getUuid().equals(uuid)) {
            return secondRobotPosition;
        } else {
            throw new IllegalArgumentException(CAN_T_RETURN_A_SPEED_OF_UNRECOGNIZED_ROBOT);
        }
    }

    public void moveRobot(final double time, final String uuid) {
        if (firstRobot.getUuid().equals(uuid)) {
            firstRobotPosition -= time / 1000 * Robot.angleSpeedToMetric(firstRobot.getSpeed());
        } else if (secondRobot.getUuid().equals(uuid)) {
            secondRobotPosition -= time / 1000 * Robot.angleSpeedToMetric(secondRobot.getSpeed());
        } else {
            throw new IllegalArgumentException(CAN_T_RETURN_A_SPEED_OF_UNRECOGNIZED_ROBOT);
        }
        this.repaint();
    }

    @Override
    @SuppressWarnings("NumericCastThatLosesPrecision")
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final double firstHeightRatio = (double) this.firstRobot.getHeight() / this.firstRobot.getInitialDistanceToCross();
        final double secondHeightRatio = (double) this.secondRobot.getHeight() / this.secondRobot.getInitialDistanceToCross();

        drawAxis(g, firstHeightRatio, secondHeightRatio);
        drawLabels(g);
        drawRobots(g, firstHeightRatio, secondHeightRatio);
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    private void drawAxis(final Graphics g, final double firstHeightRatio, final double secondHeightRatio) {
        g.setColor(Color.DARK_GRAY);

        g.drawLine(getWidth() - BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, BORDER_GAP);
        g.drawLine(BORDER_GAP, BORDER_GAP, getWidth() - BORDER_GAP, BORDER_GAP);

        // create hatch marks for x axis
        final int firstPartLength = (int) ((getWidth() - 2 * BORDER_GAP) * firstHeightRatio);

        for (int i = 0; i < this.firstRobot.getInitialDistanceToCross() / this.firstRobot.getHeight() + 1; i++) {
            final int y0 = BORDER_GAP;
            final int y1 = BORDER_GAP + GRAPH_POINT_WIDTH;
            final int x = BORDER_GAP + i * firstPartLength;
            g.drawLine(x, y0, x, y1);
        }

        // create hatch marks for y axis
        final int secondPartLength = (int) ((getHeight() - 2 * BORDER_GAP) * secondHeightRatio);

        for (int i = 0; i < this.secondRobot.getInitialDistanceToCross() / this.secondRobot.getHeight() + 1; i++) {
            final int x0 = getWidth() - BORDER_GAP;
            final int x1 = getWidth() - BORDER_GAP - GRAPH_POINT_WIDTH;
            final int y = getHeight() - BORDER_GAP - i * secondPartLength;
            g.drawLine(x0, y, x1, y);
        }
    }

    private void drawLabels(final Graphics g) {
        g.drawString("0", getWidth() - BORDER_GAP, BORDER_GAP / 3 * 2);
        g.drawString(Integer.valueOf(firstRobot.getInitialDistanceToCross()).toString(),
                BORDER_GAP / 2, BORDER_GAP / 3 * 2);
        g.drawString(Integer.valueOf(secondRobot.getInitialDistanceToCross()).toString(),
                getWidth() - BORDER_GAP, getHeight() - BORDER_GAP / 2);
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    private void drawRobots(final Graphics g, final double firstHeightRatio, final double secondHeightRatio) {
        final double widthPixels = getWidth() - 2 * BORDER_GAP;
        final double heightPixels = getHeight() - 2 * BORDER_GAP;

        g.setColor(Color.blue);
        final double firstWidthRatio = (double) this.firstRobot.getWidth() / this.secondRobot.getInitialDistanceToCross();
        final int firstPlotWidth = (int) (heightPixels * firstWidthRatio);
        final int firstPlotHeight = (int) (widthPixels * firstHeightRatio);
        final int firstDistance = (int) (widthPixels * (firstRobot.getInitialDistanceToCross()
                - this.firstRobotPosition) / firstRobot.getInitialDistanceToCross());
        //noinspection SuspiciousNameCombination
        g.fillRoundRect(BORDER_GAP - firstPlotHeight + firstDistance,
                BORDER_GAP + ROBOT_SPACING,
                firstPlotHeight,
                firstPlotWidth,
                firstPlotHeight / 2,
                firstPlotWidth / 2);
        g.setColor(Color.white);
        g.drawString(Integer.toString(firstRobot.getSpeed()),
                BORDER_GAP - firstPlotHeight + firstDistance + firstPlotHeight / 2,
                BORDER_GAP + ROBOT_SPACING + firstPlotWidth / 2);

        g.setColor(Color.red);
        final double secondWidthRatio = (double) this.secondRobot.getWidth() / this.firstRobot.getInitialDistanceToCross();
        final int secondPlotWidth = (int) (widthPixels * secondWidthRatio);
        final int secondPlotHeight = (int) (heightPixels * secondHeightRatio);
        final int secondDistance = (int) (heightPixels * (secondRobot.getInitialDistanceToCross()
                - this.secondRobotPosition) / secondRobot.getInitialDistanceToCross());
        g.fillRoundRect(getWidth() - BORDER_GAP - ROBOT_SPACING - secondPlotWidth,
                getHeight() - BORDER_GAP - secondDistance,
                secondPlotWidth,
                secondPlotHeight,
                secondPlotWidth / 2,
                secondPlotHeight / 2);
        g.setColor(Color.white);
        g.drawString(Integer.toString(secondRobot.getSpeed()),
                getWidth() - BORDER_GAP - ROBOT_SPACING - secondPlotWidth + secondPlotWidth / 2,
                getHeight() - BORDER_GAP - secondDistance + secondPlotHeight / 2);
    }

    public Robot getFirstRobot() {
        return firstRobot;
    }

    public Robot getSecondRobot() {
        return secondRobot;
    }
}