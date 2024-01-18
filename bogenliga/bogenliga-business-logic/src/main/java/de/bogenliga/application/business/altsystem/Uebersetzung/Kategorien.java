package de.bogenliga.application.business.altsystem.Uebersetzung;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public enum Kategorien {
    Mannschaft_Verein("Mannschaft_Verein"),
    Mannschaft_Mannschaft("Mannschaft_Mannschaft"),
    Schütze_Verein("Schütze_Verein"),
    Mannschaft_Veranstaltung("Mannschaft_Veranstaltung");


    String label;
    Kategorien(String label) {
        this.label = label;
    }
}
