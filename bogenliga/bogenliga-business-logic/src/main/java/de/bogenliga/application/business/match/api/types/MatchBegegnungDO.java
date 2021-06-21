package de.bogenliga.application.business.match.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;

import java.time.OffsetDateTime;

/**
 * @author
 */
public class MatchBegegnungDO extends CommonDataObject {
    private static final long serialVersionUID = -6105018809032979203L;

    private Long wettkampfId;
    private Long matchnummer;
    private Long matchid;
    private Long matchIdGegner;
    private String mannschaften;
    private Long begegnung;
    private Long scheibenNummer;
    private Long scheibenNummerGegner;
    private Long matchpunkte;
    private Long satzpunkte;
    private Long strafPunkteSatz1;
    private Long strafPunkteSatz2;
    private Long strafPunkteSatz3;
    private Long strafPunkteSatz4;
    private Long strafPunkteSatz5;


    public MatchBegegnungDO(Long wettkampfId,
                            Long matchnummer,
                            Long matchid,
                            Long matchIdGegner,
                            String mannschaften,
                            Long begegnung,
                            Long scheibenNummer,
                            Long scheibenNummerGegner,
                            Long matchpunkte,
                            Long satzpunkte,
                            Long strafPunkteSatz1, Long strafPunkteSatz2,
                            Long strafPunkteSatz3,
                            Long strafPunkteSatz4, Long strafPunkteSatz5,
                            final OffsetDateTime createdAtUtc,
                            final Long createdByUserId, final OffsetDateTime lastModifiedUtc,
                            final Long lastModifiedByUserId, final Long version) {
        this.setWettkampfId(wettkampfId);
        this.setMatchnummer(matchnummer);
        this.setMatchid(matchid);
        this.setMatchIdGegner(matchIdGegner);
        this.setMannschaften(mannschaften);
        this.setBegegnung(begegnung);
        this.setScheibenNummer(scheibenNummer);
        this.setScheibenNummerGegner(scheibenNummerGegner);
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


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getWettkampfId() {
        return wettkampfId;
    }

    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public Long getMatchnummer() {
        return matchnummer;
    }

    public void setMatchnummer(Long matchnummer) {
        this.matchnummer = matchnummer;
    }

    public Long getMatchid() {
        return matchid;
    }

    public void setMatchid(Long matchid) {
        this.matchid = matchid;
    }

    public Long getMatchIdGegner() {
        return matchIdGegner;
    }

    public void setMatchIdGegner(Long matchIdGegner) {
        this.matchIdGegner = matchIdGegner;
    }

    public String getMannschaften() {
        return mannschaften;
    }

    public void setMannschaften(String mannschaften) {
        this.mannschaften = mannschaften;
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

    public Long getScheibenNummerGegner() {
        return scheibenNummerGegner;
    }

    public void setScheibenNummerGegner(Long scheibenNummerGegner) {
        this.scheibenNummerGegner = scheibenNummerGegner;
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
