package de.bogenliga.application.business.altsystem.uebersetzung;

/**
 * Konstanten für die Kategorien von Datensätzen in der Übersetzungstabelle
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public enum AltsystemUebersetzungKategorie {

    Liga_Liga("Liga_Liga"),
    Saison_Sportjahr("Saison_Sportjahr"),
    Mannschaft_Verein("Mannschaft_Verein"),
    Mannschaft_Mannschaft("Mannschaft_Mannschaft"),

    Schuetze_DSBMitglied("Schütze_DSBMitglied"),
    Schuetze_Mannschaft("Schütze_Mannschaft"),
    Mannschaft_Veranstaltung("Mannschaft_Veranstaltung"),
    Wettkampfergebnis_Match("Wettkampfergebnis_Match"),
    Match_Saetze("Match_Saetze"),
    Ergebnis_Passen("Ergebnis_Passen");


    String label;
    AltsystemUebersetzungKategorie(String label) {
        this.label = label;
    }
}
