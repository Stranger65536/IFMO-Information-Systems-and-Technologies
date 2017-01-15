package ru.ifmo.m3405;

import sofia_kp.SSAP_sparql_response;
import sofia_kp.iKPIC_subscribeHandler2;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;

public class RobotSubscriptionHandler implements iKPIC_subscribeHandler2 {
    private static final String JSON_PARSING_ERROR = "Json parsing error";
    private final String currentUUID;
    private final Exchanger<Robot> robotInfoExchanger;
    private final Exchanger<String> startInfoExchanger;
    private final CyclicBarrier readyStateBarrier;

    @SuppressWarnings("LockAcquiredButNotSafelyReleased")
    public RobotSubscriptionHandler(
            final String currentUUID,
            final Exchanger<Robot> robotInfoExchanger,
            final Exchanger<String> startInfoExchanger,
            final CyclicBarrier readyStateBarrier) {
        this.currentUUID = currentUUID;
        this.robotInfoExchanger = robotInfoExchanger;
        this.startInfoExchanger = startInfoExchanger;
        this.readyStateBarrier = readyStateBarrier;
    }

    @Override
    @SuppressWarnings("OverlyComplexMethod")
    public void kpic_RDFEventHandler(
            final Vector<Vector<String>> newTriples,
            final Vector<Vector<String>> oldTriples,
            final String indSequence,
            final String subID) {
        for (List<String> triple : newTriples) {
            final String subject = triple.get(0);
            final String predicate = triple.get(1);
            //noinspection unused
            final String object = triple.get(2);

            switch (predicate) {
                case Predicates.INFO:
                    processInfoReceive(subject);
                    break;
                case Predicates.RECEIVED:
                    processAcknowledgement(subject);
                    break;
                case Predicates.CRASH:
                    processStartReceive(predicate);
                    break;
                case Predicates.CROSS:
                    processStartReceive(predicate);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized predicate");
            }
        }
    }

    private void processInfoReceive(final String subject) {
        try {
            final Robot robot = Robot.valueOf(subject);
            if (!currentUUID.equals(robot.getUuid())) {
                System.out.println(currentUUID + ": received opponent info");
                robotInfoExchanger.exchange(robot);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processAcknowledgement(final String subject) {
        try {
            final Robot robot = Robot.valueOf(subject);
            if (!currentUUID.equals(robot.getUuid())) {
                readyStateBarrier.await();
            }
        } catch (IOException ignored) {
            System.out.println(JSON_PARSING_ERROR);
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private void processStartReceive(final String predicate) {
        try {
            System.out.println("Start signal received");
            startInfoExchanger.exchange(predicate);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void kpic_SPARQLEventHandler(
            final SSAP_sparql_response ssap_sparql_response,
            final SSAP_sparql_response ssap_sparql_response1,
            final String s,
            final String s1) {
        System.err.println("Unrecognized event");
    }

    @Override
    public void kpic_UnsubscribeEventHandler(final String s) {
        System.out.println("Unsubscribed: " + s);
    }

    @Override
    public void kpic_ExceptionEventHandler(final Throwable throwable) {
        throwable.printStackTrace();
    }
}
