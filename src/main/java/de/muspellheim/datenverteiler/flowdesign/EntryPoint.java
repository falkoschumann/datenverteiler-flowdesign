/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.flowdesign;

import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Diese Schnittstelle markiert eine Functional-Unit als Entry-Point und startet den Datenfluss.
 *
 * <p>Diese Schnittstelle ersetzt die gleichnamige aus der Flow-Bibliothek, damit statt eines String-Feldes die Klasse
 * {@code ArgumentList} der Datenverteiler-Applikationsfunktionen für die Aufrufparameter verwendet werden kann.</p>
 *
 * @author Falko Schumann
 * @since 1.0
 */
public interface EntryPoint {

    /**
     * Startet den Fluss am Entry-Point.
     *
     * @param aufrufparameter die Aufrufparameter der Applikation.
     */
    void run(ArgumentList aufrufparameter);

}
