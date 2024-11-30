# Erstellen und Verwenden einer neuen Vorlage für die Anwendung Berchtoldschiessen

## Erstellen einer neuen Vorlage

1. **Navigieren Sie zum Vorlagenverzeichnis**:
    - Öffnen Sie das `BASE_DIRECTORY`, das in der Datei `config.properties` angegeben ist.
    - Der Standardpfad ist `C:/BerchtoldschiessenApp/`.

2. **Erstellen Sie eine neue Vorlagendatei**:
    - Erstellen Sie eine neue `.docx`-Datei im `BASE_DIRECTORY`.
    - Benennen Sie die Datei entsprechend, zum Beispiel `New_Template.docx`.

3. **Bearbeiten Sie die Vorlage**:
    - Öffnen Sie die neu erstellte „docx“-Datei mit einem Textverarbeitungsprogramm (z. B. Microsoft Word).
    - Gestalten Sie die Vorlage entsprechend Ihren Anforderungen.
    - Es sollten keine Platzhalter geändert werden. [Platzhalter hinzufügen](#Platzhalter-hinzufügen).

4. **Speichern Sie die Vorlage**:
    - Speichern Sie die Änderungen in der Datei `.docx`.
    - Stellen Sie sicher, dass die Datei im `BASE_DIRECTORY` gespeichert ist.

## Verwendung der neuen Vorlage

1. **Aktualisieren Sie die Konfiguration**:
    - Öffnen Sie die Datei `config.properties`.
    - Aktualisieren Sie die Eigenschaft „INPUT_DOCX“, um auf die neue Vorlagendatei zu verweisen. Zum Beispiel:
      ```ini
      INPUT_DOCX=Neue_Vorlage.docx
      ```

2. **Starten Sie die Anwendung**:
    - Vergewissern Sie sich, dass Ihre Teilnehmerdaten in der Datei `competitors.csv` vorhanden sind.
    - Führen Sie die Anwendung aus, indem Sie die Datei `BerchtoldSchiessenApp.jar` starten.

3. **Dokumente generieren**:
    - Die Anwendung wird die neue Vorlage verwenden, um die Dokumente zu generieren.
    - Überprüfen Sie den `OUTPUT_FOLDER` für die generierten Dokumente.

4. **Drucken Sie die Dokumente**:
    - Verwenden Sie den Druckdienst innerhalb der Anwendung, um die erstellten Dokumente zu drucken.

## Platzhalter hinzufügen in Vorlage

Neue Platzhalter sollten nur hinzugefügt werden, wenn sie in der nötig Anwendung verwendet werden. <br>
Um neue Platzhalter hinzuzufügen, folgen Sie den Schritten unten:

1. Neue Spalte in CSV-Datei hinzufügen:
    - Fügen Sie eine neue Spalte in der Datei `competitors.csv` hinzu.

2. Felder in [`CompetitorDTO`](../main/java/com/jaboumal/dto/CompetitorDTO.java) und [
   `BerchtoldschiessenDTO`](../main/java/com/jaboumal/dto/xml/BerchtoldschiessenDTO.java) hinzufügen:
    - Fügen Sie ein neues Feld in die Klasse `CompetitorDTO` hinzu.
    - Fügen Sie ein neues Feld in die Klasse `BerchtoldschiessenDTO` hinzu.
3. XML-Datei von BerchtoldschiessenDTO erstellen.
    - Es muss eine XML-Datei mit allen Feldern erstellt werden.
    - Am einfachsten wir die output-data.xml Datei benutzt. Dafür muss die Zeile `inputXml.delete();` in der Klasse
      `XMLService` auskommentiert werden.
4. Platzhalter in der Vorlage hinzufügen:
    - Die Vorlage in Word öffnen.
    - Den Reiter `Entwicklertools` auswählen. Falls dieser nicht sichtbar ist, kann er in den Word-Optionen aktiviert
      werden.
    - Klicken Sie auf `XML-Zuordnungsbereich` und wählen Sie die XML-Datei aus.
    - Wählen Sie `No Namespace` aus.
    - Ordnen Sie die Felder aus der XML-Datei den Platzhaltern in der Vorlage zu. Es müssen alle Felder zugeordnet
      werden, auch wenn diese schon vorher dort waren.

## Hinweise

- Vergewissern Sie sich, dass die Platzhalter in der Vorlage mit den Datenfeldern in der Datei `competitors.csv`
  übereinstimmen.