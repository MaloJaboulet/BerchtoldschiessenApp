![Logo](https://www.ssgn.ch/templates/yootheme/cache/0e/logo_sggn150-0e1fe872.png)

# Berchtoldschiessen Anwendung

Diese Anwendung dient der Erstellung von Standblättern für das Berchtoldschiessen.

## Installation

1. Laden Sie `BerchtoldSchiessenApp.jar` herunter.
2. Öffnen Sie `BerchtoldSchiessenApp.jar`.

## Verwendung

1. Stellen Sie sicher, dass Ihre Teilnehmerdaten in einer CSV-Datei (`competitors.csv`) vorliegen.
    - Die CSV-Datei muss die folgenden Spalten enthalten. Die Spaltennamen dürfen nicht in der Datei enthalten sein.:
        - `lizenzNummer` - Die Lizenznummer des Teilnehmers.
        - `vorname` - Der Vorname des Teilnehmers.
        - `nachname` - Der Nachname des Teilnehmers.
        - `geburtsDatum` - Das Geburtsdatum des Teilnehmers.
        - `istGast` - Ein boolescher Wert, der angibt, ob der Teilnehmer ein Gast ist. (true/false)
2. Führen Sie die Anwendung aus, um Standblätter zu erstellen.
3. Verwenden Sie den Druckdienst, um die generierten Dokumente zu drucken.

## Entwicklung

### Voraussetzungen

- Java
- Maven
- IntelliJ IDEA (empfohlen)

### Erstellung des Projekts

```sh
mvn clean install
```

### Tests ausführen

```sh
mvn test
```

## Autoren

- [@Malo Jaboulet](https://www.github.com/MaloJaboulet)

## Lizenz

Dieses Projekt ist unter der MIT-Lizenz lizenziert - siehe die LICENSE-Datei für Details.
