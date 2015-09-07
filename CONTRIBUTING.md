Wie Sie mithelfen können
========================

Mängel, Fehler und fehlende Funktionen können Sie im [Issue Tracker][issues]
berichten. Gerne auch mit [Pull Request][pulls] für einen Lösungsvorschlag.


Distributionspaket erzeugen
---------------------------

**Vorraussetzung:** Java 8 und Maven 3 sind installiert.

Das Projekt von https://github.com/falkoschumann/datenverteiler-flowdesign.git
klonen und die folgenden beiden Befehle auf der Konsole ausführen:

    mvn install
    mvn assembly:single


Hinweise
--------

  - Das Git-Repository wird gemäß [A successful Git branching model][branching]
    verwaltet.

  - Die Versionierung der Releases erfolgt gemäß [Semantic Versioning][semver].

  - Textbausteine die in Templates verwendet werden, müssen vorformatiert
    werden. Das gilt unter anderen für die Projektbeschreibung im `pom.xml` und
    Actions in `changes.xml`. Zur Vorformatierung gehört vor allem die
    Begrenzung der Zeilenlänge auf 80 Zeichen.


Release veröffentlichen
-----------------------

Im `develop`-Branch oder einem Release-Branch die Version für das neue Release
festlegen (mindestens das `SNAPSHOT` entfernen). Das Änderungsprotokoll
`changes.xml` prüfen und falls noch nicht geschehen ergänzen. Dann die Tests
ausführen sowie das Distributionspaket und die Projektseite prüfen:

    mvn install
    mvn site
    mvn assembly:single

Wenn alles in Ordnung ist, den Branch in den `master`-Branch mergen und mit
GitHub synchronisieren. Aus dem `master`-Branch heraus das Distributionspaket
und die Projektseite erzeugen:

    mvn install
    mvn site
    mvn assembly:single

In GitHub ein neues Release anlegen und den `master`-Branch zu Tagen auswählen.
Als Tag wird die Version mit dem Präfix "v" genommen, also zum Beispiel v1.2.3.
Als Title und Description im GitHub-Release werden die Description und die
Actions des aktuellen Releases der `changes.xml` angegeben. Das
Distributionspaket wird als Binary an das GitHub-Release angehangen.

Zum Schluss die Projektseite im `gh-pages`-Branch veröffentlichen. Dazu im
Branch die alte Projektseite löschen und dann die neue rein kopieren.


[issues]: https://github.com/falkoschumann/datenverteiler-flowdesign/issues
[pulls]: https://github.com/falkoschumann/datenverteiler-flowdesign/pulls
[semver]: http://semver.org
[branching]: http://nvie.com/posts/a-successful-git-branching-model/
