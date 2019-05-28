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
    private Long strafPunktSatz1;
    private Long strafPunktSatz2;
    private Long strafPunktSatz3;
    private Long strafPunktSatz4;
    private Long strafPunktSatz5;


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
                "strafPunktSatz1=" + strafPunktSatz1 +
                "strafPunktSatz2=" + strafPunktSatz2 +
                "strafPunktSatz3=" + strafPunktSatz3 +
                "strafPunktSatz4=" + strafPunktSatz4 +
                "strafPunktSatz5=" + strafPunktSatz5 +
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
