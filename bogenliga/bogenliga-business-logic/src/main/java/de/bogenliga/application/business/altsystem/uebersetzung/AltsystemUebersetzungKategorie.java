package de.bogenliga.application.business.altsystem.uebersetzung;

/**
 * Konstanten für die Kategorien von Datensätzen in der Übersetzungstabelle
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public enum AltsystemUebersetzungKategorie {

    LIGA_LIGA("Liga_Liga"),
    SAISON_SPORTJAHR("Saison_Sportjahr"),
    MANNSCHAFT_VEREIN("Mannschaft_Verein"),
    MANNSCHAFT_MANNSCHAFT("Mannschaft_Mannschaft"),

    SCHUETZE_DSB_MITGLIED("Schütze_DSBMitglied"),
    SCHUETZE_MANNSCHAFT("Schütze_Mannschaft"),
    MANNSCHAFT_VERANSTALTUNG("Mannschaft_Veranstaltung"),
    WETTKAMPFERGEBNIS_MATCH("Wettkampfergebnis_Match"),
    MATCH_SAETZE("Match_Saetze"),
    ERGEBNIS_PASSEN("Ergebnis_Passen");


    String label;
    AltsystemUebersetzungKategorie(String label) {
        this.label = label;
    }
}
