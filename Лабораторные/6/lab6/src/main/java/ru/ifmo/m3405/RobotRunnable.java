package ru.ifmo.m3405;

import ru.ifmo.m3405.gui.JRobotLayout;
import ru.ifmo.m3405.gui.MainFrame;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;

public class RobotRunnable implements Runnable {
    private static final CyclicBarrier INSERT_BARRIER = new CyclicBarrier(2);
    private static final CyclicBarrier FINISH_BARRIER = new CyclicBarrier(2);

    private static final String ROBOTS_SEEMS_TO_BE_ALREADY_CRASHED = "Robots seems to be already crashed!";
    private static final double EPSILON = 1.0E-15;
    private static final int FREQUENCY = 10;
    private static final int DELAY = 2000;
    private final SSAgent service;
    private final JRobotLayout layout;
    private final MainFrame mainFrame;
    private final Robot currentRobot;
    private final Exchanger<Robot> robotInfoExchanger = new Exchanger<>();
    private final Exchanger<String> startInfoExchanger = new Exchanger<>();
    private final CyclicBarrier readyStateBarrier = new CyclicBarrier(2);
    private final CyclicBarrier eventBarrier = new CyclicBarrier(2);
    private Robot remoteRobot;

    public RobotRunnable(final JRobotLayout layout, final Robot robot, final MainFrame mainFrame) {
        this.layout = layout;
        this.currentRobot = robot;
        this.mainFrame = mainFrame;
        this.service = new SSAgent(Constants.KPI_HOST, Constants.KPI_PORT, Constants.KPI_SPACE,
                new RobotSubscriptionHandler(currentRobot.getUuid(), robotInfoExchanger, startInfoExchanger, readyStateBarrier));

        service.start();
        clearSpace();

        service.subscribe(null, Predicates.INFO, null, Constants.KPI_LITERAL);
        service.subscribe(null, Predicates.CRASH, null, Constants.KPI_LITERAL);
        service.subscribe(null, Predicates.CROSS, null, Constants.KPI_LITERAL);
        service.subscribe(null, Predicates.RECEIVED, null, Constants.KPI_LITERAL);

        INSERT_BARRIER.reset();
        FINISH_BARRIER.reset();
    }

    private void clearSpace() {
        service.remove(null, Predicates.INFO, null, Constants.KPI_LITERAL);
        service.remove(null, Predicates.CRASH, null, Constants.KPI_LITERAL);
        service.remove(null, Predicates.CROSS, null, Constants.KPI_LITERAL);
        service.remove(null, Predicates.RECEIVED, null, Constants.KPI_LITERAL);
    }

    @Override
    public void run() {
        try {
            mainFrame.setLabelMessage("Waiting for subscription");
            Thread.sleep(DELAY);
            INSERT_BARRIER.await();

            mainFrame.setAllInputsEnabled(false);

            service.insert(currentRobot.toString(), Predicates.INFO, currentRobot.toString(), Constants.KPI_LITERAL);

            this.remoteRobot = robotInfoExchanger.exchange(null);
            System.out.println(currentRobot.getUuid() + ": exchanged with thread");

            service.insert(currentRobot.toString(), Predicates.RECEIVED, currentRobot.getUuid(), Constants.KPI_LITERAL);
            readyStateBarrier.await();

            mainFrame.setCrossAndCrashEnabled(true);
            mainFrame.setLabelMessage("Waiting for start");

            final String action = startInfoExchanger.exchange(null);
            startRobot(action);
            mainFrame.setLabelMessage("Run!");
            eventBarrier.await();
            FINISH_BARRIER.await();
            mainFrame.setLabelMessage("Complete:)");
            mainFrame.setAllButtonsEnabled(true);
            mainFrame.setCrossAndCrashEnabled(false);
            mainFrame.setAllInputsEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRobot(final String action) {
        switch (action) {
            case Predicates.CRASH:
                layout.setRobotSpeed(currentRobot.getUuid(), calculateAngleSpeedToCrash());
                break;
            case Predicates.CROSS:
                layout.setRobotSpeed(currentRobot.getUuid(), calculateAngleSpeedToCross());
                break;
            default:
                throw new IllegalArgumentException("Unknown action");
        }
        final Timer timer = new Timer();
        timer.schedule(new MoveTimerTask(FREQUENCY, timer), 0, FREQUENCY);
    }

    private int calculateAngleSpeedToCrash() {
        if (currentRobot.getInitialDistanceToCross() <= remoteRobot.getWidth() / 2 ||
                remoteRobot.getInitialDistanceToCross() <= currentRobot.getWidth() / 2) {
            throw new IllegalArgumentException(ROBOTS_SEEMS_TO_BE_ALREADY_CRASHED);
        }

        final int currentCrashDistance = currentRobot.getInitialDistanceToCross() - remoteRobot.getWidth() / 2;
        final int remoteCrashDistance = remoteRobot.getInitialDistanceToCross() - currentRobot.getWidth() / 2;

        final double currentSpeed = Robot.angleSpeedToMetric(currentRobot.getSpeed());
        final double remoteSpeed = Robot.angleSpeedToMetric(remoteRobot.getSpeed());

        final double currentTimeToCross = currentCrashDistance / currentSpeed;
        final double remoteTimeToCross = remoteCrashDistance / remoteSpeed;

        if (currentTimeToCross > remoteTimeToCross ||
                Math.abs(currentTimeToCross - remoteTimeToCross) < EPSILON &&
                        currentRobot.getUuid().compareTo(remoteRobot.getUuid()) < 0) {
            final int newSpeed = Robot.metricSpeedToAngle(currentCrashDistance / remoteTimeToCross);
            return newSpeed <= MainFrame.SPEED_LIMIT ? newSpeed : MainFrame.SPEED_LIMIT;
        } else {
            final int opponentSpeed = Robot.metricSpeedToAngle(remoteCrashDistance / currentTimeToCross);
            return opponentSpeed <= MainFrame.SPEED_LIMIT
                    ? Robot.metricSpeedToAngle(currentSpeed)
                    : Double.valueOf(currentCrashDistance * (double) MainFrame.SPEED_LIMIT / remoteCrashDistance).intValue();
        }
    }

    @SuppressWarnings({"MagicNumber", "Duplicates"})
    private int calculateAngleSpeedToCross() {
        if (currentRobot.getInitialDistanceToCross() <= remoteRobot.getWidth() / 2 ||
                remoteRobot.getInitialDistanceToCross() <= currentRobot.getWidth() / 2) {
            throw new IllegalArgumentException(ROBOTS_SEEMS_TO_BE_ALREADY_CRASHED);
        }

        final int currentCrashDistance = currentRobot.getInitialDistanceToCross() + remoteRobot.getWidth() + currentRobot.getHeight();
        final int remoteCrashDistance = remoteRobot.getInitialDistanceToCross() + currentRobot.getWidth() + remoteRobot.getHeight();

        final double currentSpeed = Robot.angleSpeedToMetric(currentRobot.getSpeed());
        final double remoteSpeed = Robot.angleSpeedToMetric(remoteRobot.getSpeed());

        final double currentTimeToCross = currentCrashDistance / currentSpeed;
        final double remoteTimeToCross = remoteCrashDistance / remoteSpeed;

        if (currentTimeToCross <= remoteTimeToCross &&
                (Math.abs(currentTimeToCross - remoteTimeToCross) >= EPSILON ||
                        currentRobot.getUuid().compareTo(remoteRobot.getUuid()) >= 0)) {
            return Robot.metricSpeedToAngle(currentSpeed);
        } else {
            final int newSpeed = Robot.metricSpeedToAngle((currentRobot.getInitialDistanceToCross()
                    - remoteRobot.getWidth() / 2.0) / remoteTimeToCross);
            return newSpeed <= MainFrame.SPEED_LIMIT ? newSpeed : MainFrame.SPEED_LIMIT;
        }
    }

    private class MoveTimerTask extends TimerTask {
        private final int period;
        private final Timer timer;

        public MoveTimerTask(final int period, final Timer timer) {
            this.timer = timer;
            this.period = period;
        }

        @Override
        public void run() {
            layout.moveRobot(period, currentRobot.getUuid());

            if (layout.getRobotPosition(currentRobot.getUuid()) < -currentRobot.getHeight()) {
                try {
                    eventBarrier.await();
                    timer.cancel();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
