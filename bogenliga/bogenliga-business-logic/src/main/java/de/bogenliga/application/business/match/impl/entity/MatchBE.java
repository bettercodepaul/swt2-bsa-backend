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
    private Long fehlerSatz1;
    private Long fehlerSatz2;
    private Long fehlerSatz3;
    private Long fehlerSatz4;
    private Long fehlerSatz5;


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
                "fehlerSatz1=" + fehlerSatz1 +
                "fehlerSatz2=" + fehlerSatz2 +
                "fehlerSatz3=" + fehlerSatz3 +
                "fehlerSatz4=" + fehlerSatz4 +
                "fehlerSatz5=" + fehlerSatz5 +
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


    public Long getFehlerSatz1() {
        return fehlerSatz1;
    }


    public void setFehlerSatz1(Long fehlerSatz1) {
        this.fehlerSatz1 = fehlerSatz1;
    }


    public Long getFehlerSatz2() {
        return fehlerSatz2;
    }


    public void setFehlerSatz2(Long fehlerSatz2) {
        this.fehlerSatz2 = fehlerSatz2;
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
