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
    private Long strafPunktSatz1;
    private Long strafPunktSatz2;
    private Long strafPunktSatz3;
    private Long strafPunktSatz4;
    private Long strafPunktSatz5;


    public MatchDO(Long id, Long nr, Long wettkampfId, Long mannschaftId, Long begegnung, Long scheibenNummer,
                   Long matchpunkte, Long satzpunkte, Long strafPunktSatz1, Long strafPunktSatz2, Long strafPunktSatz3,
                   Long strafPunktSatz4, Long strafPunktSatz5,
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
        this.setStrafPunktSatz1(strafPunktSatz1);
        this.setStrafPunktSatz2(strafPunktSatz2);
        this.setStrafPunktSatz3(strafPunktSatz3);
        this.setStrafPunktSatz4(strafPunktSatz4);
        this.setStrafPunktSatz5(strafPunktSatz5);

        this.setVersion(version);
        this.setLastModifiedAtUtc(lastModifiedUtc);
        this.setCreatedAtUtc(createdAtUtc);
        this.setCreatedByUserId(createdByUserId);
        this.setLastModifiedByUserId(lastModifiedByUserId);
    }


    public MatchDO(Long id, Long nr, Long wettkampfId, Long mannschaftId, Long begegnung,
                   Long scheibenNummer, Long matchpunkte, Long satzpunkte, Long strafPunktSatz1, Long strafPunktSatz2,
                   Long strafPunktSatz3, Long strafPunktSatz4, Long strafPunktSatz5) {
        this.setId(id);
        this.setNr(nr);
        this.setWettkampfId(wettkampfId);
        this.setMannschaftId(mannschaftId);
        this.setBegegnung(begegnung);
        this.setScheibenNummer(scheibenNummer);
        this.setMatchpunkte(matchpunkte);
        this.setSatzpunkte(satzpunkte);
        this.setStrafPunktSatz1(strafPunktSatz1);
        this.setStrafPunktSatz2(strafPunktSatz2);
        this.setStrafPunktSatz3(strafPunktSatz3);
        this.setStrafPunktSatz4(strafPunktSatz4);
        this.setStrafPunktSatz5(strafPunktSatz5);
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


    public Long getStrafPunktSatz1() {
        return strafPunktSatz1;
    }


    public void setStrafPunktSatz1(Long strafPunktSatz1) {
        this.strafPunktSatz1 = strafPunktSatz1;
    }


    public Long getStrafPunktSatz2() {
        return strafPunktSatz2;
    }


    public void setStrafPunktSatz2(Long strafPunktSatz2) {
        this.strafPunktSatz2 = strafPunktSatz2;
    }


    public Long getStrafPunktSatz3() {
        return strafPunktSatz3;
    }


    public void setStrafPunktSatz3(Long strafPunktSatz3) {
        this.strafPunktSatz3 = strafPunktSatz3;
    }


    public Long getStrafPunktSatz4() {
        return strafPunktSatz4;
    }


    public void setStrafPunktSatz4(Long strafPunktSatz4) {
        this.strafPunktSatz4 = strafPunktSatz4;
    }


    public Long getStrafPunktSatz5() {
        return strafPunktSatz5;
    }


    public void setStrafPunktSatz5(Long strafPunktSatz5) {
        this.strafPunktSatz5 = strafPunktSatz5;
    }
}
