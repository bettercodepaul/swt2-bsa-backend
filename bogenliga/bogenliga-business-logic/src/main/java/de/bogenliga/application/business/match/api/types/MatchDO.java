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
    private Long fehlerSatz1;
    private Long FehlerSatz2;
    private Long fehlerSatz3;
    private Long fehlerSatz4;
    private Long fehlerSatz5;


    public MatchDO(Long id, Long nr, Long wettkampfId, Long mannschaftId, Long begegnung, Long scheibenNummer,
                   Long matchpunkte, Long satzpunkte,Long fehlerSatz1, Long fehlerSatz2, Long fehlerSatz3,
                   Long fehlerSatz4, Long fehlerSatz5,
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
        this.setFehlerSatz1(fehlerSatz1);
        this.setFehlerSatz2(fehlerSatz2);
        this.setFehlerSatz3(fehlerSatz3);
        this.setFehlerSatz4(fehlerSatz4);
        this.setFehlerSatz5(fehlerSatz5);

        this.setVersion(version);
        this.setLastModifiedAtUtc(lastModifiedUtc);
        this.setCreatedAtUtc(createdAtUtc);
        this.setCreatedByUserId(createdByUserId);
        this.setLastModifiedByUserId(lastModifiedByUserId);
    }


    public MatchDO(Long id, Long nr, Long wettkampfId, Long mannschaftId, Long begegnung,
                   Long scheibenNummer, Long matchpunkte, Long satzpunkte,Long fehlerSatz1, Long fehlerSatz2,
                   Long fehlerSatz3, Long fehlerSatz4, Long fehlerSatz5) {
        this.setId(id);
        this.setNr(nr);
        this.setWettkampfId(wettkampfId);
        this.setMannschaftId(mannschaftId);
        this.setBegegnung(begegnung);
        this.setScheibenNummer(scheibenNummer);
        this.setMatchpunkte(matchpunkte);
        this.setSatzpunkte(satzpunkte);
        this.setFehlerSatz1(fehlerSatz1);
        this.setFehlerSatz2(fehlerSatz2);
        this.setFehlerSatz3(fehlerSatz3);
        this.setFehlerSatz4(fehlerSatz4);
        this.setFehlerSatz5(fehlerSatz5);
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


    public Long getFehlerSatz1() {
        return fehlerSatz1;
    }


    public void setFehlerSatz1(Long fehlerSatz1) {
        this.fehlerSatz1 = fehlerSatz1;
    }


    public Long getFehlerSatz2() {
        return FehlerSatz2;
    }


    public void setFehlerSatz2(Long fehlerSatz2) {
        FehlerSatz2 = fehlerSatz2;
    }


    public Long getFehlerSatz3() {
        return fehlerSatz3;
    }


    public void setFehlerSatz3(Long fehlerSatz3) {
        this.fehlerSatz3 = fehlerSatz3;
    }


    public Long getFehlerSatz4() {
        return fehlerSatz4;
    }


    public void setFehlerSatz4(Long fehlerSatz4) {
        this.fehlerSatz4 = fehlerSatz4;
    }


    public Long getFehlerSatz5() {
        return fehlerSatz5;
    }


    public void setFehlerSatz5(Long fehlerSatz5) {
        this.fehlerSatz5 = fehlerSatz5;
    }
}
