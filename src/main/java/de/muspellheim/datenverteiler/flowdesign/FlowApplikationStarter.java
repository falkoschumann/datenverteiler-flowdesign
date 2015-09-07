/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.flowdesign;

import de.bsvrz.dav.daf.main.ClientDavConnection;
import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientDavParameters;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Basisklasse für eine Appliaktion die auf Flow-Design aufbaut.
 *
 * @author Falko Schumann
 * @since 1.0
 */
public final class FlowApplikationStarter {

    private static Debug logger;
    private static StringBuilder applikationsbeschreibung;
    private static String applikationsname = "";

    private FlowApplikationStarter() {
        // utility class
    }

    /**
     * Startet die Applikation.
     *
     * @param flowApplikation der Datenfluss der Applikation.
     * @param args            die Aufrufparameter der Applikation.
     */
    public static void start(FlowApplikation flowApplikation, String[] args) {
        start(flowApplikation, "typ.applikation", args);
    }

    /**
     * Startet die Applikation.
     *
     * @param flowApplikation    der Datenfluss der Applikation.
     * @param applikationstypPid die PID des Typs der Applikation. Der Applikationstyp sollte {@code typ.applikation}
     *                           oder davon abgeleitet sein.
     * @param args               die Aufrufparameter der Applikation.
     */
    public static void start(FlowApplikation flowApplikation, String applikationstypPid, String[] args) {
        applikationsbeschreibung = erzeugeApplikationsbeschreibung(args);
        final ArgumentList aufrufparameter = new ArgumentList(args);
        initialisiereLogger(flowApplikation, aufrufparameter);
        Thread.setDefaultUncaughtExceptionHandler(new FlowApplikationUncaughtExceptionHandler());
        try {
            final ClientDavInterface verbindung = erzeugeVerbindung(applikationstypPid, aufrufparameter);
            stelleVerbindungHer(verbindung);
            applikationsbeschreibung.append(verbindung.getLocalConfigurationAuthority().getPid());
            MessageSender.getInstance().init(verbindung, applikationsname, applikationsbeschreibung.toString());

            flowApplikation.setDatenverteilerverbindung(verbindung);
            flowApplikation.build();
            flowApplikation.bind();
            flowApplikation.inject();
            flowApplikation.configure(aufrufparameter);
            flowApplikation.run(aufrufparameter);

            aufrufparameter.ensureAllArgumentsUsed();
            verbindung.sendApplicationReadyMessage();
        } catch (Exception ex) {
            logger.error("Fehler", ex);
            System.exit(1);
        }
    }


    private static StringBuilder erzeugeApplikationsbeschreibung(String[] args) {
        StringBuilder result = new StringBuilder();
        for (String arg : args) {
            result.append(arg);
        }
        return result;
    }

    private static void initialisiereLogger(Object applikation, ArgumentList aufrufparameter) {
        final String[] klassennamensteile = applikation.getClass().getName().split("[.]");
        final int indexLetzterTeil = klassennamensteile.length - 1;
        final String name;
        if (indexLetzterTeil < 0) {
            name = "FlowApplikation";
        } else {
            name = klassennamensteile[indexLetzterTeil];
        }
        applikationsname = name;
        applikationsbeschreibung.append(name);
        Debug.init(name, aufrufparameter);
        logger = Debug.getLogger();
    }

    private static ClientDavInterface erzeugeVerbindung(String applikationstypPid, ArgumentList aufrufparameter) throws Exception {
        final ClientDavParameters verbindungsparameter = new ClientDavParameters(aufrufparameter);
        verbindungsparameter.setApplicationTypePid(applikationstypPid);
        verbindungsparameter.setApplicationName(applikationsname);
        return new ClientDavConnection(verbindungsparameter);
    }

    private static void stelleVerbindungHer(final ClientDavInterface verbindung) throws Exception {
        verbindung.enableExplicitApplicationReadyMessage();
        verbindung.connect();
        verbindung.login();
    }

    private static class FlowApplikationUncaughtExceptionHandler implements UncaughtExceptionHandler {

        /**
         * Speicherreserve, die freigegeben wird, wenn ein Error auftritt, damit die Ausgaben nach einem
         * OutOfMemoryError funktionieren.
         */
        private volatile byte[] reserve = new byte[20000];

        public void uncaughtException(Thread thread, Throwable throwable) {
            if (throwable instanceof Error) {
                // Speicherreserve freigeben, damit die Ausgaben nach einem OutOfMemoryError funktionieren
                reserve = null;
                try {
                    System.err.println("Schwerwiegender Laufzeitfehler: Ein Thread hat sich wegen eines Errors beendet, Prozess wird terminiert");
                    System.err.println(thread);
                    throwable.printStackTrace(System.err);
                    logger.error("Schwerwiegender Laufzeitfehler: " + thread + " hat sich wegen eines Errors beendet, Prozess wird terminiert", throwable);
                } finally {
                    System.exit(1);
                }
            } else {
                System.err.println("Laufzeitfehler: Ein Thread hat sich wegen einer Exception beendet:");
                System.err.println(thread);
                throwable.printStackTrace(System.err);
                logger.warning("Laufzeitfehler: " + thread + " hat sich wegen einer Exception beendet", throwable);
            }
        }
    }

}
