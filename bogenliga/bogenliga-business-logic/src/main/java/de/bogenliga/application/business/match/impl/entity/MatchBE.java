package de.bogenliga.application.business.match.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 3591981606558698229L;

    private Long nr;
    private Long id;
    private Long wettkampfId;
    private Long mannschaftId;
    private Long scheibenNummer;
    private Long begegnung;
    private Long matchpunkte;
    private Long satzpunkte;
    private Long strafPunkteSatz1;
    private Long strafPunkteSatz2;
    private Long strafPunkteSatz3;
    private Long strafPunkteSatz4;
    private Long strafPunkteSatz5;


    @Override
    public String toString() {
        return "MatchBE{" +
                "id=" + id +
                "nr=" + nr +
                "wettkampfId=" + wettkampfId +
                "mannschaftId=" + mannschaftId +
                "scheibenNummer=" + scheibenNummer +
                "begegnung=" + begegnung +
                "matchpunkte=" + matchpunkte +
                "satzpunkte=" + satzpunkte +
                "strafPunkteSatz1=" + strafPunkteSatz1 +
                "strafPunkteSatz2=" + strafPunkteSatz2 +
                "strafPunkteSatz3=" + strafPunkteSatz3 +
                "strafPunkteSatz4=" + strafPunkteSatz4 +
                "strafPunkteSatz5=" + strafPunkteSatz5 +
                '}';
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


    public Long getScheibenNummer() {
        return scheibenNummer;
    }


    public void setScheibenNummer(Long scheibenNummer) {
        this.scheibenNummer = scheibenNummer;
    }


    public Long getBegegnung() {
        return begegnung;
    }


    public void setBegegnung(Long begegnung) {
        this.begegnung = begegnung;
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
