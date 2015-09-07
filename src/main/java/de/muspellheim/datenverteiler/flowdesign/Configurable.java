/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.flowdesign;

import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Diese Schnittstelle konfiguriert eine Functional-Unit.
 *
 * <p>Diese Schnittstelle ersetzt die gleichnamige aus der Flow-Bibliothek, damit statt eines String-Feldes die Klasse
 * {@code ArgumentList} der Datenverteiler-Applikationsfunktionen für die Aufrufparameter verwendet werden kann.</p>
 *
 * @author Falko Schumann
 * @since 1.0
 */
public interface Configurable {

    /**
     * Als Konfiguration werden die Kommandozeilenoptionen der Applikation übergeben.
     *
     * @param aufrufparameter die Kommandozeilenoptionen der Applikattion.
     */
    void configure(ArgumentList aufrufparameter);

}

