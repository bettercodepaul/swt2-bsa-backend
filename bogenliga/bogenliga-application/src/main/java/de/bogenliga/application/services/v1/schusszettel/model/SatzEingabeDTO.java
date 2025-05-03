package de.bogenliga.application.services.v1.schusszettel.model;

import java.util.List;

/**
 * Wrapper für die POST-Anfrage vom Typ SATZEINGABE
 * @author Marty Lauterbach, mklemmingen
 */
public class SatzEingabeDTO {
    private List<SchuetzenSatzDTO> satzeingabe;

    public List<SchuetzenSatzDTO> getSatzeingabe() {
        return satzeingabe;
    }

    public void setSatzeingabe(List<SchuetzenSatzDTO> satzeingabe) {
        this.satzeingabe = satzeingabe;
    }
}
