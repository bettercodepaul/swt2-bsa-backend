package de.bogenliga.application.business.Setzliste.impl.entity;

import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

import java.sql.Date;

/**
 * I represent the setzliste business entity.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class SetzlisteBE extends CommonBusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private Long wettkampfid;
    private Long mannschaftid;
    private Integer ligatabelleTabellenplatz;

    private String vereinName;
    private Integer mannschaftNummer;
    private String veranstaltungName;
    private Integer wettkampfTag;
    private Date wettkampfDatum;
    private String wettkampfBeginn;
    private String wettkampfOrt;


    public SetzlisteBE(){
        // empty constructor
    }


    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getLigatabelleTabellenplatz() {
        return ligatabelleTabellenplatz;
    }

    public void setLigatabelleTabellenplatz(Integer ligatabelleTabellenplatz) {
        this.ligatabelleTabellenplatz = ligatabelleTabellenplatz;
    }

    public String getVereinName() {
        return vereinName;
    }

    public void setVereinName(String vereinName) {
        this.vereinName = vereinName;
    }

    public Integer getMannschaftNummer() {
        return mannschaftNummer;
    }

    public void setMannschaftNummer(Integer mannschaftNummer) {
        this.mannschaftNummer = mannschaftNummer;
    }

    public String getVeranstaltungName() {
        return veranstaltungName;
    }

    public void setVeranstaltungName(String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
    }

    public Integer getWettkampfTag() {
        return wettkampfTag;
    }

    public void setWettkampfTag(Integer wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }

    public Date getWettkampfDatum() {
        return wettkampfDatum;
    }

    public void setWettkampfDatum(Date wettkampfDatum) {
        this.wettkampfDatum = wettkampfDatum;
    }

    public String getWettkampfBeginn() {
        return wettkampfBeginn;
    }

    public void setWettkampfBeginn(String wettkampfBeginn) {
        this.wettkampfBeginn = wettkampfBeginn;
    }

    public String getWettkampfOrt() {
        return wettkampfOrt;
    }

    public void setWettkampfOrt(String wettkampfOrt) {
        this.wettkampfOrt = wettkampfOrt;
    }

    public Long getWettkampfid() { return wettkampfid; }

    public void setWettkampfid(Long wettkampfid) { this.wettkampfid = wettkampfid; }

    public Long getMannschaftid() {
        return mannschaftid;
    }

    public void setMannschaftid(Long mannschaftid) {
        this.mannschaftid = mannschaftid;
    }

    @Override
    public String toString() {
        return "setzliste{ligatabelleTabellenplatz='" + ligatabelleTabellenplatz +
                "', WettkampfID='" + wettkampfid +
                "', MannschaftID='" + mannschaftid +
                "', vereinName='" + vereinName +
                "', mannschaftNummer='" + mannschaftNummer +
                "', veranstaltungName='" + veranstaltungName +
                "', wettkampfTag='" + wettkampfTag +
                "', wettkampfDatum='" + wettkampfDatum +
                "', wettkampfBeginn='" + wettkampfBeginn +
                "', wettkampfOrt='" + wettkampfOrt +
                "'}";
    }
}
