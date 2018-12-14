package de.bogenliga.application.business.Setzliste.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Contains the values of the dsbmitglied business entity.
 *
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
 */
public class SetzlisteDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 298357103627898987L;

    /**
     * business parameter
     */
    private Integer ligatabelleTabellenplatz;
    private String vereinName;
    private Integer mannschaftNummer;
    private String veranstaltungName;
    private Integer wettkampfTag;
    private Date wettkampfDatum;
    private String wettkampfBeginn;
    private String wettkampfOrt;


    /**
     * Constructor with default parameters
     * @param
     */
    public SetzlisteDO(
            final Integer ligatabelleTabellenplatz,
            final String vereinName,
            final Integer mannschaftNummer,
            final String veranstaltungName,
            final Integer wettkampfTag,
            final Date wettkampfDatum,
            final String wettkampfBeginn,
            final String wettkampfOrt) {
        this.ligatabelleTabellenplatz = ligatabelleTabellenplatz;
        this.vereinName = vereinName;
        this.mannschaftNummer = mannschaftNummer;
        this.veranstaltungName = veranstaltungName;
        this.wettkampfTag = wettkampfTag;
        this.wettkampfDatum = wettkampfDatum;
        this.wettkampfBeginn = wettkampfBeginn;
        this.wettkampfOrt = wettkampfOrt;
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


//    @Override
//    public int hashCode() {
//        return Objects.hash(id, vorname, nachname, geburtsdatum, nationalitaet, mitgliedsnummer, vereinsId, userId,
//                createdByUserId, lastModifiedAtUtc,
//                lastModifiedByUserId, version);
//    }
}
