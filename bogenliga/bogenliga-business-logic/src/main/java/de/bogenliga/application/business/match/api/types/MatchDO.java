package de.bogenliga.application.business.match.api.types;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.component.types.CommonDataObject;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchDO extends CommonDataObject {
    private static final long serialVersionUID = -6105018809032979203L;

    private Long nr;
    private Long id;
    private Long wettkampfId;
    private Long mannschaftId;
    private Long begegnung;
    private Long scheibenNummer;
    private Long matchpunkte;
    private Long satzpunkte;


    public MatchDO(Long id, Long nr, Long wettkampfId, Long mannschaftId, Long begegnung, Long scheibenNummer,
                   Long matchpunkte, Long satzpunkte,
                   final OffsetDateTime createdAtUtc,
                   final Long createdByUserId, final OffsetDateTime lastModifiedUtc,
                   final Long lastModifiedByUserId, final Long version) {
        this.setId(id);
        this.setNr(nr);
        this.setWettkampfId(wettkampfId);
        this.setMannschaftId(mannschaftId);
        this.setBegegnung(begegnung);
        this.setScheibenNummer(scheibenNummer);
        this.setMatchpunkte(matchpunkte);
        this.setSatzpunkte(satzpunkte);

        this.setVersion(version);
        this.setLastModifiedAtUtc(lastModifiedUtc);
        this.setCreatedAtUtc(createdAtUtc);
        this.setCreatedByUserId(createdByUserId);
        this.setLastModifiedByUserId(lastModifiedByUserId);
    }


    public MatchDO(Long id, Long nr, Long wettkampfId, Long mannschaftId, Long begegnung,
                   Long scheibenNummer, Long matchpunkte, Long satzpunkte) {
        this.setId(id);
        this.setNr(nr);
        this.setWettkampfId(wettkampfId);
        this.setMannschaftId(mannschaftId);
        this.setBegegnung(begegnung);
        this.setScheibenNummer(scheibenNummer);
        this.setMatchpunkte(matchpunkte);
        this.setSatzpunkte(satzpunkte);
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

}
