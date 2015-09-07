/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.flowdesign;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Schnittstelle der Fabrik zum Erzeugen des Flows einer auf Flow-Design basierten Applikation. Die Methoden werden
 * implementiert, um die entsprechende Phase zu realisiereren. Die Methoden werden in der folgenden Reihenfolge
 * aufgerufen:
 *
 * <ol>
 * <li>{@link #setDatenverteilerverbindung(ClientDavInterface)}</li>
 * <li>{@link #build()}</li>
 * <li>{@link #bind()}</li>
 * <li>{@link #inject()}</li>
 * <li>{@link #configure(ArgumentList)} </li>
 * <li>{@link #run(ArgumentList)}</li>
 * </ol>
 *
 * <p>Diese Schnittstelle ersetzt die Klasse {@code Flow} aus der Flow-Bibliothek, damit statt eines String-Feldes die
 * Klasse {@code ArgumentList} der Datenverteiler-Applikationsfunktionen für die Aufrufparameter verwendet werden kann.</p>
 *
 * @author Falko Schumann
 * @since 1.0
 */
public interface FlowApplikation {

    /**
     * Setzt die Datenverteilerverbindung der Applikation. Diese Methode wird vor allen anderen aufgerufen, damit die
     * Abhängigkeit zum Datenverteiler sowohl per Konstruktor als auch per Setter in Functional-Units injiziert werden
     * kann.
     *
     * @param datenverteilerverbindung der Datenverteiler wird als Abhängigkeit vom System bereitgestellt.
     */
    void setDatenverteilerverbindung(ClientDavInterface datenverteilerverbindung);

    /**
     * Diese Methode legt die Functional-Units an.
     */
    void build();

    /**
     * Diese Methode verbindet die Output-Pins und Input-Pins der Functional-Units miteinander.
     */
    void bind();

    /**
     * Diese Methode erzeugt und injiziert die Abhängigkeiten der Functional-Units. Wenn Abhängigkeiten per Konstruktor
     * injiziert werden, geschieht dies bereits in {@link #build()}.
     */
    void inject();

    /**
     * Diese Methode konfiguriert die Functional-Units. Die Aufrufparameter der Applikation werden an die
     * Functional-Units weitergereicht, die sie benötigen, damit diese ihre Konfiguration daraus lesen können.
     *
     * @param aufrufparameter die Aufrufparameter der Applikation.
     */
    void configure(ArgumentList aufrufparameter);

    /**
     * Diese Methode startet den Flow durch den Start der Functional-Units die den Entry-Point des Flows darstellt. Der
     * Entry-Point bekommt die restlichen Aufrufparameter der Applikation übergeben, die noch nicht im Schritt
     * {@link #configure(ArgumentList)} konsumiert wurden.
     *
     * @param aufrufparameter die Aufrufparameter der Applikation, die noch nicht von {@link #configure(ArgumentList)}
     *                        konsumiert wurden.
     */
    void run(ArgumentList aufrufparameter);

}
