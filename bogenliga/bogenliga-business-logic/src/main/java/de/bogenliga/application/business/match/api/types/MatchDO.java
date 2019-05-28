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
    private Long strafPunkteSatz1;
    private Long strafPunkteSatz2;
    private Long strafPunkteSatz3;
    private Long strafPunkteSatz4;
    private Long strafPunkteSatz5;


    public MatchDO(Long id, Long nr, Long wettkampfId, Long mannschaftId, Long begegnung, Long scheibenNummer,
                   Long matchpunkte, Long satzpunkte, Long strafPunkteSatz1, Long strafPunkteSatz2,
                   Long strafPunkteSatz3,
                   Long strafPunkteSatz4, Long strafPunkteSatz5,
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
        this.setStrafPunkteSatz1(strafPunkteSatz1);
        this.setStrafPunkteSatz2(strafPunkteSatz2);
        this.setStrafPunkteSatz3(strafPunkteSatz3);
        this.setStrafPunkteSatz4(strafPunkteSatz4);
        this.setStrafPunkteSatz5(strafPunkteSatz5);

        this.setVersion(version);
        this.setLastModifiedAtUtc(lastModifiedUtc);
        this.setCreatedAtUtc(createdAtUtc);
        this.setCreatedByUserId(createdByUserId);
        this.setLastModifiedByUserId(lastModifiedByUserId);
    }


    public MatchDO(Long id, Long nr, Long wettkampfId, Long mannschaftId, Long begegnung,
                   Long scheibenNummer, Long matchpunkte, Long satzpunkte, Long strafPunkteSatz1, Long strafPunkteSatz2,
                   Long strafPunkteSatz3, Long strafPunkteSatz4, Long strafPunkteSatz5) {
        this.setId(id);
        this.setNr(nr);
        this.setWettkampfId(wettkampfId);
        this.setMannschaftId(mannschaftId);
        this.setBegegnung(begegnung);
        this.setScheibenNummer(scheibenNummer);
        this.setMatchpunkte(matchpunkte);
        this.setSatzpunkte(satzpunkte);

        this.setStrafPunkteSatz1(strafPunkteSatz1);
        this.setStrafPunkteSatz2(strafPunkteSatz2);
        this.setStrafPunkteSatz3(strafPunkteSatz3);
        this.setStrafPunkteSatz4(strafPunkteSatz4);
        this.setStrafPunkteSatz5(strafPunkteSatz5);
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
}
