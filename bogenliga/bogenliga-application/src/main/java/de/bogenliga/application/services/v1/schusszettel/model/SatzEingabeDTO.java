package de.bogenliga.application.services.v1.schusszettel.model;

import de.bogenliga.application.services.v1.schusszettel.model.inside.SchuetzenSatzDTO;
import java.util.List;

/**
 * Wrapper für die POST-Anfrage vom Typ SATZEINGABE
 * @author Marty Lauterbach, mklemmingen
 */
public class SatzEingabeDTO {
    private List<SchuetzenSatzDTO> satzeingabe;

    public SatzEingabeDTO() {
        // Für Jackson
    }

    public List<SchuetzenSatzDTO> getSatzeingabe() {
        return satzeingabe;
    }

    public void setSatzeingabe(List<SchuetzenSatzDTO> satzeingabe) {
        this.satzeingabe = satzeingabe;
    }
}
