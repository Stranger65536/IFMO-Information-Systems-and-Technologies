package ru.ifmo.m3405;

import sofia_kp.KPICore;
import sofia_kp.SIBResponse;
import sofia_kp.iKPIC_subscribeHandler2;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author trofiv
 */
public class SSAgent {
    protected static final String SUCCESSFULLY_STOP = "Successfully left smart space.";
    protected static final String STOP_ERROR = "Could not leave smart space. It is not fatal but a little but annoying.";
    private static final Logger LOG = Logger.getLogger(SSAgent.class.getName());
    private static final String SUCCESSFULLY_JOINED = "Successfully joined to smart space.";
    private static final String JOIN_ERROR = "Could not join to smart space. Will not function properly!";
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
        configureKpi();
        subscriptionIdList = new LinkedList<>();
    }

    private void configureKpi() {
        if (LOG.isLoggable(Level.FINEST)) {
            kpi.enable_debug_message();
        }
        if (LOG.isLoggable(Level.FINER)) {
            kpi.enable_error_message();
        }
    }

    public void start() {
        final SIBResponse response = kpi.join();
        if (response != null && response.isConfirmed()) {
            LOG.log(Level.FINE, SUCCESSFULLY_JOINED);
        } else {
            LOG.log(Level.SEVERE, JOIN_ERROR);
        }
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
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine(String.format("%s : Subscription of <%s> confirmed ", new Date(), predicate));
                }
            } else {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine(String.format("%s : Subscription of <%s> was aborted ", new Date(), predicate));
                }
            }
        }
    }

    @SuppressWarnings({"AccessToStaticFieldLockedOnInstance", "DuplicateStringLiteralInspection"})
    public void unSubscribeAll() {
        synchronized (kpi) {
            for (String id : subscriptionIdList) {
                final SIBResponse response = kpi.unsubscribe(id);

                if (response.isConfirmed()) {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine(String.format("%s : Unsubscription is confirmed.", new Date()));
                    }
                } else {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine(String.format("%s : Unsubscription isn't confirmed", new Date()));
                    }
                }
            }
        }

        subscriptionIdList.clear();
    }

    public void stop() {
        unSubscribeAll();

        final SIBResponse response = kpi.leave();
        if (response.isConfirmed()) {
            LOG.log(Level.FINE, SUCCESSFULLY_STOP);
        } else {
            LOG.log(Level.WARNING, STOP_ERROR);
        }
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
            if (response != null && response.isConfirmed()) {
                LOG.fine(String.format("Insert of <%s, %s, %s> succeeded", subject, predicate, object));
            } else {
                LOG.warning(String.format("Insert of <%s, %s, %s> failed", subject, predicate, object));
                throw new IllegalArgumentException("Insert triple failed");
            }
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
            if (response.isConfirmed()) {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine(String.format("Remove of <%s, %s, %s> succeeded", subject, predicate, object));
                }
            } else {
                LOG.warning(String.format("Remove of <%s, %s, %s> failed", subject, predicate, object));
                throw new IllegalArgumentException("Remove triple failed");
            }
        }

        return response.Message;
    }

    @SuppressWarnings("AccessToStaticFieldLockedOnInstance")
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
            if (response.isConfirmed()) {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine(String.format("Fetch of <%s, %s, %s> succeeded", subject, predicate, object));
                }
            } else {
                LOG.warning(String.format("Fetch of <%s, %s, %s> failed", subject, predicate, object));
                throw new IllegalArgumentException("Fetch triple failed");
            }
        }

        return response;
    }
}