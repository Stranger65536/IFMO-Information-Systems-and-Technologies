package ru.ifmo.m3405;

import sofia_kp.KPICore;
import sofia_kp.SIBResponse;
import sofia_kp.iKPIC_subscribeHandler2;

import java.util.LinkedList;
import java.util.List;

/**
 * @author trofiv
 */
public class SSAgent {
    private final KPICore kpi;
    private final List<String> subscriptionIdList;
    private final iKPIC_subscribeHandler2 handler;

    public SSAgent(
            final String host,
            final int port,
            final String space,
            final iKPIC_subscribeHandler2 handler) {
        kpi = new KPICore(host, port, space);
        this.handler = handler;
        kpi.setEventHandler2(handler);
        subscriptionIdList = new LinkedList<>();
    }

    public void start() {
        kpi.join();
    }


    @SuppressWarnings({"AccessToStaticFieldLockedOnInstance", "DuplicateStringLiteralInspection"})
    public void subscribe(
            final String subject,
            final String predicate,
            final String object,
            final String objectType) {
        synchronized (kpi) {
            final SIBResponse response = kpi.subscribeRDF(subject, predicate, object, objectType, handler);

            if (response.isConfirmed()) {
                final String subscriptionId = response.subscription_id;
                if (subscriptionId != null && !subscriptionId.isEmpty()) {
                    subscriptionIdList.add(subscriptionId);
                }
            }
        }
    }

    public void stop() {
        unSubscribeAll();
        try {
            kpi.leave();
        } catch (Exception ignored) {
        }
    }

    @SuppressWarnings({"AccessToStaticFieldLockedOnInstance", "DuplicateStringLiteralInspection"})
    public void unSubscribeAll() {
        synchronized (kpi) {
            subscriptionIdList.forEach(kpi::unsubscribe);
        }
        subscriptionIdList.clear();
    }

    @SuppressWarnings({"AccessToStaticFieldLockedOnInstance", "DuplicateStringLiteralInspection"})
    public String insert(
            final String subject,
            final String predicate,
            final String object,
            final String objType) {
        final SIBResponse response;

        synchronized (kpi) {
            response = kpi.insert(subject,    // subject
                    predicate,                // predicate
                    object,                   // object
                    "uri",                    // subject type
                    objType);                 // object type
        }

        return response.Message;
    }

    @SuppressWarnings({"AccessToStaticFieldLockedOnInstance", "unused"})
    public String remove(
            final String subject,
            final String predicate,
            final String object,
            final String objType) {
        final SIBResponse response;
        synchronized (kpi) {
            response = kpi.remove(subject,    // subject
                    predicate,                // predicate
                    object,                   // object
                    "uri",                    // subject type
                    objType);                 // object type
        }

        return response.Message;
    }

    public SIBResponse queryRDF(
            final String subject,
            final String predicate,
            final String object,
            final String objType) {
        final SIBResponse response;
        synchronized (kpi) {
            response = kpi.queryRDF(subject,    // subject
                    predicate,                  // predicate
                    object,                     // object
                    "uri",                      // subject type
                    objType);                   // object type
        }

        return response;
    }
}