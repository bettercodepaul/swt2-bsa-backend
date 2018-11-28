package de.bogenliga.application.business.Setzliste.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

import java.sql.Date;

/**
 * I represent the dsbmitglied business entity.
 * <p>
 * A dsbmitglied is a registered member of the DSB. The dsbmitglied is not necessarily a technical user of the system.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class SetzlisteBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private Integer matchNr;
    private Integer matchScheibennummer;
    private Integer ligatabelleTabellenplatz;
    private String vereinName;
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


    public Integer getMatchNr() {
        return matchNr;
    }

    public void setMatchNr(Integer matchNr) {
        this.matchNr = matchNr;
    }

    public Integer getMatchScheibennummer() {
        return matchScheibennummer;
    }

    public void setMatchScheibennummer(Integer matchScheibennummer) {
        this.matchScheibennummer = matchScheibennummer;
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

    @Override
    public String toString() {
        return "setzliste{" +
                "matchnr=" + matchNr +
                ", matchScheibennummer='" + matchScheibennummer + '\'' +
                ", ligatabelleTabellenplatz='" + ligatabelleTabellenplatz + '\'' +
                ", vereinName='" + vereinName + '\'' +
                ", wettkampfTag='" + wettkampfTag + '\'' +
                ", wettkampfDatum='" + wettkampfDatum + '\'' +
                ", wettkampfBeginn=" + wettkampfBeginn +
                ", wettkampfOrt=" + wettkampfOrt +
                '}';
    }
}
