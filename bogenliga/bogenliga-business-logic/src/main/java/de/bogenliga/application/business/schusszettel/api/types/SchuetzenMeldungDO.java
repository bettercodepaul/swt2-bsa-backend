package de.bogenliga.application.business.schusszettel.api.types;

import java.util.List;

/**
 * Businessobjekt für die Schützenmeldung (POST).
 * Entspricht SchuetzenMeldungDTO im API-Layer.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public class SchuetzenMeldungDO {
    private List<Long> gemeldeteSchuetzen;

    public List<Long> getGemeldeteSchuetzen() {
        return gemeldeteSchuetzen;
    }

    public void setGemeldeteSchuetzen(List<Long> gemeldeteSchuetzen) {
        this.gemeldeteSchuetzen = gemeldeteSchuetzen;
    }
}
