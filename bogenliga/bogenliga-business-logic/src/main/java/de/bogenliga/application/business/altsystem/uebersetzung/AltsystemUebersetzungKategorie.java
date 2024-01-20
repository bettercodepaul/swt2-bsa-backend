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
    Mannschaft_Veranstaltung("Mannschaft_Veranstaltung");


    String label;
    AltsystemUebersetzungKategorie(String label) {
        this.label = label;
    }
}
