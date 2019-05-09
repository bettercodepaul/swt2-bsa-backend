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

}
