package ru.ifmo.m3405.gui;

import ru.ifmo.m3405.*;
import ru.ifmo.m3405.Robot;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFrame extends JFrame {
    private static final Locale LOCALE = Locale.ENGLISH;
    private static final Dimension MINIMUM_FRAME_DIMENSION = new Dimension(800, 600);
    private static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension INITIAL_FRAME_DIMENSION = new Dimension(
            SCREEN_DIMENSION.width / 4 * 3,
            SCREEN_DIMENSION.height / 8 * 7);
    private static final Point INITIAL_FRAME_LOCATION = new Point(
            SCREEN_DIMENSION.width / 2 - INITIAL_FRAME_DIMENSION.width / 2,
            SCREEN_DIMENSION.height / 2 - INITIAL_FRAME_DIMENSION.height / 2);
    private static final Dimension CONTROL_PANEL_DIMENSION = new Dimension(
            300, INITIAL_FRAME_DIMENSION.height);

    private static final String DISTANCE_LABEL = "Distance:";
    private static final String INITIAL_SPEED_LABEL = "Initial speed:";
    private static final String HEIGHT_LABEL = "Height:";
    private static final String WIDTH_LABEL = "Width:";
    private static final String FIRST_ROBOT_LABEL = "First Robot";
    private static final String SECOND_ROBOT_LABEL = "Second Robot";
    private static final String USER = "User";
    private static final String ROBOTS = "Robots";

    private static final int DEFAULT_WIDTH = 140;
    private static final int DEFAULT_HEIGHT = 170;
    private static final int DEFAULT_SPEED = 360;
    private static final int DEFAULT_DISTANCE = 1500;

    private static final int MAX_WIDTH = 500;
    private static final int MAX_HEIGHT = 500;
    private static final int MAX_SPEED = 720;
    public static final int SPEED_LIMIT = MAX_SPEED;
    private static final int MAX_DISTANCE = 5000;
    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    private static final int FONT_SIZE = 20;

    private final JPanel controlPanel = new JPanel();
    private final JLabel firstRobotHeaderLabel = new JLabel(FIRST_ROBOT_LABEL, SwingConstants.CENTER);
    private final JLabel firstRobotWidthLabel = new JLabel(WIDTH_LABEL, SwingConstants.RIGHT);
    private final JSpinner firstRobotWidthField = new JSpinner(new SpinnerNumberModel(DEFAULT_WIDTH, 1, MAX_WIDTH, 1));
    private final JLabel firstRobotHeightLabel = new JLabel(HEIGHT_LABEL, SwingConstants.RIGHT);
    private final JSpinner firstRobotHeightField = new JSpinner(new SpinnerNumberModel(DEFAULT_HEIGHT, 1, MAX_HEIGHT, 1));
    private final JLabel firstRobotInitialSpeedLabel = new JLabel(INITIAL_SPEED_LABEL, SwingConstants.RIGHT);
    private final JSpinner firstRobotInitialSpeedField = new JSpinner(new SpinnerNumberModel(DEFAULT_SPEED, 1, MAX_SPEED, 1));
    private final JLabel firstRobotDistanceLabel = new JLabel(DISTANCE_LABEL, SwingConstants.RIGHT);
    private final JSpinner firstRobotDistanceField = new JSpinner(new SpinnerNumberModel(DEFAULT_DISTANCE, 1, MAX_DISTANCE, 1));
    private final JLabel secondRobotHeaderLabel = new JLabel(SECOND_ROBOT_LABEL, SwingConstants.CENTER);
    private final JLabel secondRobotWidthLabel = new JLabel(WIDTH_LABEL, SwingConstants.RIGHT);
    private final JSpinner secondRobotWidthField = new JSpinner(new SpinnerNumberModel(DEFAULT_WIDTH, 1, MAX_WIDTH, 1));
    private final JLabel secondRobotHeightLabel = new JLabel(HEIGHT_LABEL, SwingConstants.RIGHT);
    private final JSpinner secondRobotHeightField = new JSpinner(new SpinnerNumberModel(DEFAULT_HEIGHT, 1, MAX_HEIGHT, 1));
    private final JLabel secondRobotInitialSpeedLabel = new JLabel(INITIAL_SPEED_LABEL, SwingConstants.RIGHT);
    private final JSpinner secondRobotInitialSpeedField = new JSpinner(new SpinnerNumberModel(DEFAULT_SPEED, 1, MAX_SPEED, 1));
    private final JLabel secondRobotDistanceLabel = new JLabel(DISTANCE_LABEL, SwingConstants.RIGHT);
    private final JSpinner secondRobotDistanceField = new JSpinner(new SpinnerNumberModel(DEFAULT_DISTANCE, 1, MAX_DISTANCE, 1));
    private final JButton applyButton = new JButton("Apply");
    private final JButton crossButton = new JButton(Predicates.CROSS);
    private final JButton crashButton = new JButton(Predicates.CRASH);
    private final JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);
    private final SSAgent service;
    private final JRobotLayout layoutPanel;

    public MainFrame(final String title) {
        super(title);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(INITIAL_FRAME_DIMENSION);
        this.setMinimumSize(MINIMUM_FRAME_DIMENSION);
        this.setLocation(INITIAL_FRAME_LOCATION);
        this.setLocale(LOCALE);

        service = new SSAgent(Constants.KPI_HOST, Constants.KPI_PORT, Constants.KPI_SPACE, null);
        service.start();

        final Robot firstRobot = new Robot(UUID.randomUUID().toString(), DEFAULT_DISTANCE, DEFAULT_SPEED, DEFAULT_HEIGHT, DEFAULT_WIDTH);
        final Robot secondRobot = new Robot(UUID.randomUUID().toString(), DEFAULT_DISTANCE, DEFAULT_SPEED, DEFAULT_HEIGHT, DEFAULT_WIDTH);
        layoutPanel = new JRobotLayout(firstRobot, secondRobot);

        preparePanels();
        this.pack();
    }

    private static GridBagConstraints getInitialConstraints() {
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = -1;
        return c;
    }

    private static void setConstraintsForHeader(final GridBagConstraints c) {
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        c.weightx = 2;
    }

    private static void setConstraintsForLabel(final GridBagConstraints c) {
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 1;
        c.weightx = 1;
    }

    private static void setConstraintsForInput(final GridBagConstraints c) {
        c.gridx = 1;
        c.gridwidth = 1;
        c.weightx = 1;
    }

    private static void setConstraintsForBigButton(final GridBagConstraints c) {
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        c.weightx = 2;
    }

    private void preparePanels() {
        layoutPanel.setLayout(new BorderLayout());
        layoutPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 10));
        layoutPanel.setBackground(Color.LIGHT_GRAY);
        layoutPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setPreferredSize(CONTROL_PANEL_DIMENSION);
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        final GridBagConstraints c = getInitialConstraints();

        addRobotControls(c,
                firstRobotHeaderLabel,
                firstRobotWidthLabel,
                firstRobotWidthField,
                firstRobotHeightLabel,
                firstRobotHeightField,
                firstRobotInitialSpeedLabel,
                firstRobotInitialSpeedField,
                firstRobotDistanceLabel,
                firstRobotDistanceField);

        addRobotControls(c,
                secondRobotHeaderLabel,
                secondRobotWidthLabel,
                secondRobotWidthField,
                secondRobotHeightLabel,
                secondRobotHeightField,
                secondRobotInitialSpeedLabel,
                secondRobotInitialSpeedField,
                secondRobotDistanceLabel,
                secondRobotDistanceField);

        addRobotControlHandlers();
        addRobotButtons(c);
        addRobotButtonsHandlers();

        this.add(layoutPanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.LINE_END);
    }

    private void addRobotControls(
            final GridBagConstraints c,
            final JLabel robotHeaderLabel,
            final JLabel robotWidthLabel,
            final JSpinner robotWidthField,
            final JLabel robotHeightLabel,
            final JSpinner robotHeightField,
            final JLabel robotInitialSpeedLabel,
            final JSpinner robotInitialSpeedField,
            final JLabel robotDistanceLabel,
            final JSpinner robotDistanceField) {
        setConstraintsForHeader(c);
        controlPanel.add(robotHeaderLabel, c);
        setConstraintsForLabel(c);
        controlPanel.add(robotWidthLabel, c);
        setConstraintsForInput(c);
        controlPanel.add(robotWidthField, c);
        setConstraintsForLabel(c);
        controlPanel.add(robotHeightLabel, c);
        setConstraintsForInput(c);
        controlPanel.add(robotHeightField, c);
        setConstraintsForLabel(c);
        controlPanel.add(robotInitialSpeedLabel, c);
        setConstraintsForInput(c);
        controlPanel.add(robotInitialSpeedField, c);
        setConstraintsForLabel(c);
        controlPanel.add(robotDistanceLabel, c);
        setConstraintsForInput(c);
        controlPanel.add(robotDistanceField, c);
    }

    private void addRobotControlHandlers() {
        final ChangeListener handler = e -> updateRobotsInfo();
        firstRobotWidthField.addChangeListener(handler);
        firstRobotHeightField.addChangeListener(handler);
        firstRobotInitialSpeedField.addChangeListener(handler);
        firstRobotDistanceField.addChangeListener(handler);
        secondRobotWidthField.addChangeListener(handler);
        secondRobotHeightField.addChangeListener(handler);
        secondRobotInitialSpeedField.addChangeListener(handler);
        secondRobotDistanceField.addChangeListener(handler);
    }

    private void addRobotButtons(final GridBagConstraints c) {
        setConstraintsForBigButton(c);
        statusLabel.setFont(new Font(statusLabel.getFont().getName(), Font.BOLD, FONT_SIZE));
        controlPanel.add(statusLabel, c);
        setConstraintsForBigButton(c);
        controlPanel.add(applyButton, c);
        setConstraintsForBigButton(c);
        controlPanel.add(crossButton, c);
        setConstraintsForBigButton(c);
        controlPanel.add(crashButton, c);
    }

    private void addRobotButtonsHandlers() {
        applyButton.setEnabled(true);
        applyButton.addActionListener(e -> {
            updateRobotsInfo();
        });
        applyButton.addActionListener(listener -> {
            THREAD_POOL.submit(new RobotRunnable(this.layoutPanel, this.layoutPanel.getFirstRobot(), this));
            THREAD_POOL.submit(new RobotRunnable(this.layoutPanel, this.layoutPanel.getSecondRobot(), this));
        });

        crossButton.setEnabled(false);
        crossButton.addActionListener(listener -> {
            setAllButtonsEnabled(false);
            THREAD_POOL.submit(() -> service.insert(USER, Predicates.CROSS, ROBOTS, Constants.KPI_LITERAL));
        });

        crashButton.setEnabled(false);
        crashButton.addActionListener(listener -> {
            setAllButtonsEnabled(false);
            THREAD_POOL.submit(() -> service.insert(USER, Predicates.CRASH, ROBOTS, Constants.KPI_LITERAL));
        });
    }

    public void updateRobotsInfo() {
        setCrossAndCrashEnabled(false);
        final Robot firstRobot = new Robot(
                layoutPanel.getFirstRobot().getUuid(),
                ((Number) firstRobotDistanceField.getValue()).intValue(),
                ((Number) firstRobotInitialSpeedField.getValue()).intValue(),
                ((Number) firstRobotHeightField.getValue()).intValue(),
                ((Number) firstRobotWidthField.getValue()).intValue());
        final Robot secondRobot = new Robot(
                layoutPanel.getSecondRobot().getUuid(),
                ((Number) secondRobotDistanceField.getValue()).intValue(),
                ((Number) secondRobotInitialSpeedField.getValue()).intValue(),
                ((Number) secondRobotHeightField.getValue()).intValue(),
                ((Number) secondRobotWidthField.getValue()).intValue());
        layoutPanel.reset(firstRobot, secondRobot);
    }

    public void setAllButtonsEnabled(final boolean state) {
        applyButton.setEnabled(state);
        crossButton.setEnabled(state);
        crashButton.setEnabled(state);
    }

    public void setCrossAndCrashEnabled(final boolean state) {
        crossButton.setEnabled(state);
        crashButton.setEnabled(state);
    }

    public void setLabelMessage(final String text) {
        statusLabel.setText(text);
    }

    public void setAllInputsEnabled(final boolean state) {
        firstRobotWidthField.setEnabled(state);
        firstRobotHeightField.setEnabled(state);
        firstRobotInitialSpeedField.setEnabled(state);
        firstRobotDistanceField.setEnabled(state);
        secondRobotWidthField.setEnabled(state);
        secondRobotHeightField.setEnabled(state);
        secondRobotInitialSpeedField.setEnabled(state);
        secondRobotDistanceField.setEnabled(state);
    }
}