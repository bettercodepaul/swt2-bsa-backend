package de.bogenliga.application.business.schusszettel.api.types;

import java.util.List;

/**
 * Business Object (DO) für eine SATZEINGABE-Nachricht.
 * Enthält die Schussdaten pro Schütze.
 *
 * @author Marty Lauterbach
 */
public class SatzEingabeDO {
    private List<SchuetzenSatzDO> satzeingabe;

    public List<SchuetzenSatzDO> getSatzeingabe() {
        return satzeingabe;
    }

    public void setSatzeingabe(List<SchuetzenSatzDO> satzeingabe) {
        this.satzeingabe = satzeingabe;
    }
}
