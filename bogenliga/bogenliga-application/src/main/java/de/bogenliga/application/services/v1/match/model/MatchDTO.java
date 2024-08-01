package de.bogenliga.application.services.v1.match.model;

import java.util.List;
import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchDTO implements DataTransferObject {
    private static final long serialVersionUID = 2743639156011821590L;

    private Long matchNr;
    private Long id;
    private Long version;
    private Long wettkampfId;
    private Long mannschaftId;

    // actually verein name...
    private String mannschaftName;
    private Long begegnung;
    private String wettkampfTyp;
    private Long wettkampfTag;
    private Long matchScheibennummer;
    private Long matchpunkte;
    private Long satzpunkte;

    private Long strafPunkteSatz1;
    private Long strafPunkteSatz2;
    private Long strafPunkteSatz3;
    private Long strafPunkteSatz4;
    private Long strafPunkteSatz5;
    private String mannschaftNameGegner;

    // used to transport related passe objects to the frontend or to save them from the
    // table form (schusszettel) to the database
    private List<PasseDTO> passen;


    //empty constructor for json objekt form frontend to backend
    public MatchDTO(){


    }

    public MatchDTO(Long id, Long matchNr, Long version, Long wettkampfId, Long mannschaftId, Long begegnung,
                    Long scheibenNummer, Long matchpunkte, Long satzpunkte, List<PasseDTO> passen,
                    Long strafPunkteSatz1,
                    Long strafPunkteSatz2, Long strafPunkteSatz3, Long strafPunkteSatz4, Long strafPunkteSatz5) {
        this.setId(id);
        this.setMatchNr(matchNr);
        this.setVersion(version);
        this.setWettkampfId(wettkampfId);
        this.setMannschaftId(mannschaftId);
        this.setBegegnung(begegnung);
        this.setMatchScheibennummer(scheibenNummer);
        this.setMatchpunkte(matchpunkte);
        this.setSatzpunkte(satzpunkte);
        this.setPassen(passen);
        this.setStrafPunkteSatz1(strafPunkteSatz1);
        this.setStrafPunkteSatz2(strafPunkteSatz2);
        this.setStrafPunkteSatz3(strafPunkteSatz3);
        this.setStrafPunkteSatz4(strafPunkteSatz4);
        this.setStrafPunkteSatz5(strafPunkteSatz5);
        this.setMannschaftNameGegner(mannschaftNameGegner);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getMatchNr() {
        return matchNr;
    }


    public void setMatchNr(Long matchNr) {
        this.matchNr = matchNr;
    }


    public Long getVersion() {
        return version;
    }


    public void setVersion(Long version) {
        this.version = version;
    }


    public String getMannschaftName() {
        return mannschaftName;
    }


    public void setMannschaftName(String mannschaftName) {
        this.mannschaftName = mannschaftName;
    }


    public Long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }


    public Long getMannschaftId() {
        return mannschaftId;
    }


    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }


    public Long getBegegnung() {
        return begegnung;
    }


    public void setBegegnung(Long begegnung) {
        this.begegnung = begegnung;
    }


    public Long getMatchScheibennummer() {
        return this.matchScheibennummer;
    }


    public void setMatchScheibennummer(Long matchScheibennummer) {
        this.matchScheibennummer = matchScheibennummer;
    }


    public Long getMatchpunkte() {
        return matchpunkte;
    }


    public void setMatchpunkte(Long matchpunkte) {
        this.matchpunkte = matchpunkte;
    }


    public Long getSatzpunkte() {
        return satzpunkte;
    }


    public void setSatzpunkte(Long satzpunkte) {
        this.satzpunkte = satzpunkte;
    }


    public List<PasseDTO> getPassen() {
        return passen;
    }


    public void setPassen(List<PasseDTO> passen) {
        this.passen = passen;
    }


    public String getWettkampfTyp() {
        return wettkampfTyp;
    }


    public void setWettkampfTyp(String wettkampfTyp) {
        this.wettkampfTyp = wettkampfTyp;
    }


    public Long getStrafPunkteSatz1() {
        return strafPunkteSatz1;
    }


    public void setStrafPunkteSatz1(Long strafPunkteSatz1) {
        this.strafPunkteSatz1 = strafPunkteSatz1;
    }


    public Long getStrafPunkteSatz2() {
        return strafPunkteSatz2;
    }


    public void setStrafPunkteSatz2(Long strafPunkteSatz2) {
        this.strafPunkteSatz2 = strafPunkteSatz2;
    }


    public Long getStrafPunkteSatz3() {
        return strafPunkteSatz3;
    }


    public void setStrafPunkteSatz3(Long strafPunkteSatz3) {
        this.strafPunkteSatz3 = strafPunkteSatz3;
    }


    public Long getStrafPunkteSatz4() {
        return strafPunkteSatz4;
    }


    public void setStrafPunkteSatz4(Long strafPunkteSatz4) {
        this.strafPunkteSatz4 = strafPunkteSatz4;
    }


    public Long getStrafPunkteSatz5() {
        return strafPunkteSatz5;
    }


    public void setStrafPunkteSatz5(Long strafPunkteSatz5) {
        this.strafPunkteSatz5 = strafPunkteSatz5;
    }


    public Long getWettkampfTag() {
        return wettkampfTag;
    }


    public void setWettkampfTag(Long wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }


    public String getMannschaftNameGegner() {
        return mannschaftNameGegner;
    }


    public void setMannschaftNameGegner(String mannschaftNameGegner) {
        this.mannschaftNameGegner = mannschaftNameGegner;
    }
}
