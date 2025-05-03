package de.bogenliga.application.business.schusszettel.api.types;

import java.util.List;

/**
 * Wrapper für die POST-Anfrage vom Typ SATZEINGABE
 * @author Marty Lauterbach, mklemmingen
 */
public class SatzEingabeDTO {
    private String typ = "SATZEINGABE";
    private List<SchuetzenSatzDTO> satzeingabe;

    public String getTyp() { return typ; }
    public void setTyp(String typ) { this.typ = typ; }

    public List<SchuetzenSatzDTO> getSatzeingabe() { return satzeingabe; }
    public void setSatzeingabe(List<SchuetzenSatzDTO> satzeingabe) { this.satzeingabe = satzeingabe; }
}