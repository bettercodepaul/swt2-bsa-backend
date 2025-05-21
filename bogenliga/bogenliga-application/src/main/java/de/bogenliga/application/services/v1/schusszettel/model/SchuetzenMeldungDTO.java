package de.bogenliga.application.services.v1.schusszettel.model;

import java.util.List;

/**
 * Wrapper für die POST-Anfrage vom Typ SCHUETZENMELDUNG
 * @author Marty Lauterbach, mklemmingen
 */
public class SchuetzenMeldungDTO {
    private List<Long> gemeldeteSchuetzen;

    public List<Long> getGemeldeteSchuetzen() { return gemeldeteSchuetzen; }
    public void setGemeldeteSchuetzen(List<Long> gemeldeteSchuetzen) { this.gemeldeteSchuetzen = gemeldeteSchuetzen; }
}