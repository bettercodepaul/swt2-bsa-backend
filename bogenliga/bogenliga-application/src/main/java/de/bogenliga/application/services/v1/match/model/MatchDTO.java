package de.bogenliga.application.services.v1.match.model;

import java.util.List;
import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchDTO implements DataTransferObject {
    private static final long serialVersionUID = 2743639156011821590L;

    private Long nr;
    private Long id;
    private Long version;
    private Long wettkampfId;
    private Long mannschaftId;

    // actually verein name...
    private String mannschaftName;
    private Long begegnung;
    private Long scheibenNummer;
    private Long matchpunkte;
    private Long satzpunkte;

    // used to transport related passe objects to the frontend or to save them from the
    // table form (schusszettel) to the database
    private List<PasseDTO> passen;


    /**
     * Default constructor, hidden, so only use the one with params...
     */
    private MatchDTO() {
    }


    public MatchDTO(Long id, Long nr, Long version, Long wettkampfId, Long mannschaftId, Long begegnung,
                    Long scheibenNummer, Long matchpunkte, Long satzpunkte, List<PasseDTO> passen) {
        this.setId(id);
        this.setNr(nr);
        this.setVersion(version);
        this.setWettkampfId(wettkampfId);
        this.setMannschaftId(mannschaftId);
        this.setBegegnung(begegnung);
        this.setScheibenNummer(scheibenNummer);
        this.setMatchpunkte(matchpunkte);
        this.setSatzpunkte(satzpunkte);
        this.setPassen(passen);
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getNr() {
        return nr;
    }


    public void setNr(Long nr) {
        this.nr = nr;
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


    public Long getScheibenNummer() {
        return scheibenNummer;
    }


    public void setScheibenNummer(Long scheibenNummer) {
        this.scheibenNummer = scheibenNummer;
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
}
