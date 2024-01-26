package de.bogenliga.application.business.altsystem.uebersetzung;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public enum AltsystemUebersetzungKategorie {

    Liga_Liga("Liga_Liga"),
    Saison_Sportjahr("Saison_Sportjahr"),
    Mannschaft_Verein("Mannschaft_Verein"),
    Mannschaft_Mannschaft("Mannschaft_Mannschaft"),
    Schütze_Verein("Schütze_Verein"),
    Schütze_DSBMitglied("Schütze_DSBMitglied"),
    Schütze_Mannschaft("Schütze_Mannschaft"),
    Mannschaft_Veranstaltung("Mannschaft_Veranstaltung"),
    Match_Saetze("Match_Saetze"),
    Ergebnis_Passen("Ergebnis_Passen");


    String label;
    AltsystemUebersetzungKategorie(String label) {
        this.label = label;
    }
}
