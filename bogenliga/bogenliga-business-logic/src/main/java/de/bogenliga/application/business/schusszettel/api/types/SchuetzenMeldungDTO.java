package de.bogenliga.application.business.schusszettel.api.types;

import java.util.List;

/**
 * Wrapper für die POST-Anfrage vom Typ SCHUETZENMELDUNG
 * @author Marty Lauterbach, mklemmingen
 */
public class SchuetzenMeldungDTO {
    private String typ = "SCHUETZENMELDUNG";
    private List<Long> gemeldeteSchuetzen;

    public String getTyp() { return typ; }
    public void setTyp(String typ) { this.typ = typ; }

    public List<Long> getGemeldeteSchuetzen() { return gemeldeteSchuetzen; }
    public void setGemeldeteSchuetzen(List<Long> gemeldeteSchuetzen) { this.gemeldeteSchuetzen = gemeldeteSchuetzen; }
}