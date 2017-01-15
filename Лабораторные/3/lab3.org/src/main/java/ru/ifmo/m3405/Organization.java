package ru.ifmo.m3405;

import sofia_kp.SSAP_sparql_response;
import sofia_kp.iKPIC_subscribeHandler2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * @author trofiv
 */
public class Organization {
    private static final Logger LOG = Logger.getLogger(Organization.class.getSimpleName());
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static SSAgent service;

    public static void main(final String[] args) {
        service = new SSAgent(Constants.KPI_HOST, Constants.KPI_PORT,
                Constants.KPI_SPACE, new OrganizationEventHandler());

        service.start();
        //noinspection DuplicateStringLiteralInspection
        LOG.info("Service started");

        service.subscribe(null, Predicates.VACATION_LEAVE, null, Constants.KPI_LITERAL);
        service.subscribe(null, Predicates.VACATION_RETURN, null, Constants.KPI_LITERAL);
        service.subscribe(null, Predicates.SICK_LEAVE, null, Constants.KPI_LITERAL);
        service.subscribe(null, Predicates.SICK_RETURN, null, Constants.KPI_LITERAL);

        processCommands();
    }

    private static void processCommands() {
        //noinspection DuplicateStringLiteralInspection
        System.out.print("Command list: " + '\n' +
                "exit quit stop" + "\n\n" +
                "> ");

        //noinspection ImplicitDefaultCharsetUsage
        try (final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(System.in))) {
            String cmd = bufferedReader.readLine();

            loop:
            while (!("exit".equals(cmd) || "quit".equals(cmd) || "stop".equals(cmd))) {
                switch (cmd) {
                    case "exit":
                        break loop;
                    case "quit":
                        break loop;
                    case "stop":
                        break loop;
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
            //noinspection StaticVariableUsedBeforeInitialization
            service.stop();
        }
    }

    private static class OrganizationEventHandler implements iKPIC_subscribeHandler2 {
        private static void processReturnFromSick(final List<String> triple) {
            final List<Vector<String>> leave = service.queryRDF(triple.get(0),
                    Predicates.SICK_LEAVE, null, Constants.KPI_LITERAL).query_results;

            final LocalDate leaveDateLocal = LocalDate.parse(leave.get(0).get(2), FORMAT);
            final LocalDate dateReturnLocal = LocalDate.parse(triple.get(2), FORMAT);

            final double salary = Period.between(leaveDateLocal, dateReturnLocal)
                    .getDays() * Constants.DAY_SALARY_FOR_SICK;

            LOG.info(MessageFormat.format("{0} : leave on sick on : {1}, return on : {2}, salary for it : {3}",
                    triple.get(0), leaveDateLocal, dateReturnLocal, salary));

            service.insert(triple.get(0), Predicates.SICK_SALARY, Double.toString(salary), Constants.KPI_LITERAL);
        }

        private static void processReturnFromVacation(final List<String> triple) {
            final List<Vector<String>> leave = service.queryRDF(triple.get(0),
                    Predicates.VACATION_LEAVE, null, Constants.KPI_LITERAL).query_results;

            final LocalDate leaveDateLocal = LocalDate.parse(leave.get(0).get(2), FORMAT);
            final LocalDate dateReturnLocal = LocalDate.parse(triple.get(2), FORMAT);

            final double salary = Period.between(leaveDateLocal, dateReturnLocal)
                    .getDays() * Constants.DAY_SALARY_FOR_VACATION;

            LOG.info(MessageFormat.format("{0} : leave on vacation on : {1}, return on : {2}, salary for it : {3}",
                    triple.get(0), leaveDateLocal, dateReturnLocal, salary));

            service.insert(triple.get(0), Predicates.VACATION_SALARY, Double.toString(salary), Constants.KPI_LITERAL);
        }

        private static void processGetOutFromWork(final List<String> triple) {
            final LocalDate leavingDateLocal = LocalDate.parse(triple.get(2), FORMAT);

            final List<Vector<String>> returnFromVacation = service.queryRDF(triple.get(0),
                    Predicates.VACATION_RETURN, null, Constants.KPI_LITERAL).query_results;
            final List<Vector<String>> returnFromSick = service.queryRDF(triple.get(0),
                    Predicates.SICK_RETURN, null, Constants.KPI_LITERAL).query_results;

            final LocalDate lastReturnDate;

            //noinspection IfStatementWithTooManyBranches
            if (returnFromVacation.isEmpty() && returnFromSick.isEmpty()) {
                lastReturnDate = leavingDateLocal;
            } else if (returnFromVacation.isEmpty()) {
                lastReturnDate = LocalDate.parse(returnFromSick.get(0).get(2), FORMAT);
            } else if (returnFromSick.isEmpty()) {
                lastReturnDate = LocalDate.parse(returnFromVacation.get(0).get(2), FORMAT);
            } else {
                final LocalDate returnFromSickDate = LocalDate.parse(returnFromSick.get(0).get(2), FORMAT);
                final LocalDate returnFromVacationDate = LocalDate.parse(returnFromVacation.get(0).get(2), FORMAT);
                lastReturnDate = returnFromSickDate.isAfter(returnFromVacationDate)
                        ? returnFromSickDate : returnFromVacationDate;
            }

            final double salary = Period.between(lastReturnDate, leavingDateLocal)
                    .getDays() * Constants.DAY_SALARY_ORIGINAL;

            LOG.info(MessageFormat.format("{0} : work from : {1}, to : {2}, salary for it : {3}",
                    triple.get(0), lastReturnDate, leavingDateLocal, salary));

            service.insert(triple.get(0), Predicates.WORK_SALARY, Double.toString(salary), Constants.KPI_LITERAL);
        }

        @Override
        public void kpic_RDFEventHandler(
                final Vector<Vector<String>> newTriples,
                final Vector<Vector<String>> oldTriples,
                final String indSequence,
                final String subID) {
            //noinspection DuplicateStringLiteralInspection
            LOG.info(MessageFormat.format("inserted id : {0}, val : {1}", subID, newTriples.toString()));
            final List<String> triple = newTriples.get(0);

            switch (triple.get(1)) {
                case Predicates.VACATION_LEAVE:
                    processGetOutFromWork(triple);
                    break;
                case Predicates.VACATION_RETURN:
                    processReturnFromVacation(triple);
                    break;
                case Predicates.SICK_LEAVE:
                    processGetOutFromWork(triple);
                    break;
                case Predicates.SICK_RETURN:
                    processReturnFromSick(triple);
                    break;
                default:
                    System.out.println("Unknown command");
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