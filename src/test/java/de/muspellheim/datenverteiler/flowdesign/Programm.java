/*
 * Copyright (c) 2015 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.datenverteiler.flowdesign;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.muspellheim.flowdesign.BaseFunctionaUnit;
import de.muspellheim.flowdesign.DependsOn;
import de.muspellheim.flowdesign.InputPin;
import de.muspellheim.flowdesign.OutputPin;

/**
 * Beipiel für eine Applikaton mit Flow-Design.
 *
 * <p><img src="doc-files/programm.png" alt="Übersicht Flow-Design für Programm"></p>
 * <p>X ist eine zusammengesetzte Functional-Unit (Board). A, B, und Gui sind Parts. Gui ist ein Entry-Point. A ist
 * konfigurierbar mit den Aufrufparametern der Applikation. B hat als Abhängigkeit R.</p>
 *
 * <p>A, B und X nutzen das Standardschema für die Benennung von Input-Pin (process) und Output-Pin (result). Aus diesen
 * Grund kann die Basisklasse {@link BaseFunctionaUnit} verwendet werden. Gui verwenden eigene Namen für Input-Pin
 * (display) und Output-Pin (query).</p>
 *
 * <p>Das Programm implementiert {@link FlowApplikation}. Die {@link FlowApplikation} wird mit dem
 * {@link FlowApplikationStarter} gestartet.</p>
 *
 * <p>Die Generics T, S und U im Beispiel werden verwendet, als Platzhalter für konrekte Datenobjekte. Eine reale
 * Implementierung kann auf Generics verzichten.</p>
 *
 * @author Falko Schumann
 */
public class Programm<T, S, U> implements FlowApplikation {

    private ClientDavInterface datenverteilerverbindung;

    private Gui<U, T> gui;
    private R r;
    private A<T, S> a;
    private B<S, U> b;
    private X<T, S, U> x;

    public static void main(String[] args) {
        FlowApplikationStarter.start(new Programm(), args);
    }

    @Override
    public void setDatenverteilerverbindung(ClientDavInterface datenverteilerverbindung) {
        this.datenverteilerverbindung = datenverteilerverbindung;
    }

    @Override
    public void build() {
        gui = new Gui<>();
        r = new R();

        a = new A<>();
        b = new B<>();
        x = new X<>(a, b);
    }

    @Override
    public void bind() {
        gui.query().connect(x::process);
        x.result().connect(gui::display);
    }

    @Override
    public void inject() {
        b.inject(r);
    }

    @Override
    public void configure(ArgumentList aufrufparameter) {
        a.configure(aufrufparameter);
    }

    @Override
    public void run(ArgumentList aufrufparameter) {
        gui.run(aufrufparameter);
    }

    private static class Gui<U, T> implements EntryPoint {

        private final OutputPin<T> query = new OutputPin<>();

        private void display(U input) {
            // ...
        }

        public OutputPin<T> query() {
            return query;
        }

        @Override
        public void run(ArgumentList aufrufparameter) {
            // ...
        }

    }

    private static class A<T, S> extends BaseFunctionaUnit<T, S> implements Configurable {

        @Override
        public void process(T input) {
            // ...
        }

        @Override
        public void configure(ArgumentList aufrufparameter) {
            // ...
        }

    }

    private static class B<S, U> extends BaseFunctionaUnit<S, U> implements DependsOn<R> {

        @Override
        public void process(S input) {
            // ...
        }

        @Override
        public void inject(R object) {
            // ...
        }

    }

    private static class X<T, S, U> extends BaseFunctionaUnit<T, U> {

        private final InputPin<T> process;

        public X(A<T, S> a, B<S, U> b) {
            process = a::process;
            a.result().connect(b::process);
            b.result().connect(this.result()::publish);
        }

        @Override
        public void process(T input) {
            process.accept(input);
        }

    }

    private static class R {

        // ...

    }

}
