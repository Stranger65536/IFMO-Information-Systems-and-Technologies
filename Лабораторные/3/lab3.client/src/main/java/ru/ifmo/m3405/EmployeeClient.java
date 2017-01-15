package ru.ifmo.m3405;

import sofia_kp.SSAP_sparql_response;
import sofia_kp.iKPIC_subscribeHandler2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * @author trofiv
 */
public class EmployeeClient {
    private static final Logger LOG = Logger.getLogger(EmployeeClient.class.getSimpleName());
    private static final String EMPLOYEE_ID = "vladislav.trofimov@emc.com";

    public static void main(final String[] args) {
        final SSAgent service = new SSAgent(Constants.KPI_HOST, Constants.KPI_PORT,
                Constants.KPI_SPACE, new EmployeeEventHandler());

        service.start();
        //noinspection DuplicateStringLiteralInspection
        LOG.info("Service started");

        clearPredicates(service);

        service.subscribe(EMPLOYEE_ID, Predicates.VACATION_SALARY, null, Constants.KPI_LITERAL);
        service.subscribe(EMPLOYEE_ID, Predicates.SICK_SALARY, null, Constants.KPI_LITERAL);
        service.subscribe(EMPLOYEE_ID, Predicates.WORK_SALARY, null, Constants.KPI_LITERAL);

        processCommands(service);
    }

    private static void clearPredicates(final SSAgent service) {
        service.remove(EMPLOYEE_ID, Predicates.SICK_LEAVE, null, Constants.KPI_LITERAL);
        service.remove(EMPLOYEE_ID, Predicates.SICK_RETURN, null, Constants.KPI_LITERAL);
        service.remove(EMPLOYEE_ID, Predicates.SICK_SALARY, null, Constants.KPI_LITERAL);
        service.remove(EMPLOYEE_ID, Predicates.VACATION_LEAVE, null, Constants.KPI_LITERAL);
        service.remove(EMPLOYEE_ID, Predicates.VACATION_RETURN, null, Constants.KPI_LITERAL);
        service.remove(EMPLOYEE_ID, Predicates.VACATION_SALARY, null, Constants.KPI_LITERAL);
    }

    @SuppressWarnings("OverlyComplexMethod")
    private static void processCommands(final SSAgent service) {
        //noinspection DuplicateStringLiteralInspection
        System.out.print("Command list: " + '\n' +
                "exit quit stop" + '\n' +
                "vacation leave - going to vacation" + '\n' +
                "vacation return - return from vacation" + '\n' +
                "sick leave - leave to sick" + '\n' +
                "sick return - return from sick" + "\n\n" +
                "> ");

        //noinspection ImplicitDefaultCharsetUsage
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            String cmd = bufferedReader.readLine();
            String date;

            loop:
            while (!("exit".equals(cmd) || "quit".equals(cmd) || "stop".equals(cmd))) {
                switch (cmd) {
                    case "exit":
                        break loop;
                    case "quit":
                        break loop;
                    case "stop":
                        break loop;
                    case "vacation leave":
                        System.out.print("Enter date of operation: ");
                        date = bufferedReader.readLine();
                        service.insert(EMPLOYEE_ID, Predicates.VACATION_LEAVE,
                                date, Constants.KPI_LITERAL);
                        break;
                    case "vacation return":
                        date = bufferedReader.readLine();
                        service.insert(EMPLOYEE_ID, Predicates.VACATION_RETURN,
                                date, Constants.KPI_LITERAL);
                        break;
                    case "sick leave":
                        date = bufferedReader.readLine();
                        service.insert(EMPLOYEE_ID, Predicates.SICK_LEAVE,
                                date, Constants.KPI_LITERAL);
                        break;
                    case "sick return":
                        date = bufferedReader.readLine();
                        service.insert(EMPLOYEE_ID, Predicates.SICK_RETURN,
                                date, Constants.KPI_LITERAL);
                        break;
                    default:
                        //noinspection DuplicateStringLiteralInspection
                        System.out.println("Unknown command: " + cmd);
                }

                System.out.print("> ");
                cmd = bufferedReader.readLine();
            }
        } catch (IOException ignored) {
            //noinspection DuplicateStringLiteralInspection
            System.out.println("Can't read next command. Stopping...");
        } finally {
            service.stop();
        }
    }

    private static class EmployeeEventHandler implements iKPIC_subscribeHandler2 {
        @Override
        public void kpic_RDFEventHandler(
                final Vector<Vector<String>> newTriples,
                final Vector<Vector<String>> oldTriples,
                final String indSequence,
                final String subID) {
            //noinspection DuplicateStringLiteralInspection
            LOG.info(MessageFormat.format("inserted id : {0}, val : {1}", subID, newTriples.toString()));
            final List<String> triple = newTriples.get(0);

            if (triple.get(1).equals(Predicates.VACATION_SALARY)) {
                //noinspection DuplicateStringLiteralInspection
                LOG.info("Salary for vacation : " + Double.parseDouble(triple.get(2)));
            }

            if (triple.get(1).equals(Predicates.SICK_SALARY)) {
                //noinspection DuplicateStringLiteralInspection
                LOG.info("Salary for sick : " + Double.parseDouble(triple.get(2)));
            }

            if (triple.get(1).equals(Predicates.WORK_SALARY)) {
                //noinspection DuplicateStringLiteralInspection
                LOG.info("Salary for work : " + Double.parseDouble(triple.get(2)));
            }
        }

        @Override
        public void kpic_SPARQLEventHandler(
                final SSAP_sparql_response ssap_sparql_response,
                final SSAP_sparql_response ssap_sparql_response1,
                final String s, final String s1) {
        }

        @Override
        public void kpic_UnsubscribeEventHandler(final String s) {
        }

        @Override
        public void kpic_ExceptionEventHandler(final Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}