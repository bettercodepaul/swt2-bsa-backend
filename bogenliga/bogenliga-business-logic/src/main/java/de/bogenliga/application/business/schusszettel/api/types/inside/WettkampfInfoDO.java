package de.bogenliga.application.business.schusszettel.api.types.inside;

import java.sql.Date;

/**
 * Business-Objekt zur Darstellung von Wettkampf-Informationen.
 * Wird im Business-Layer für den Tablet-Schusszettel verwendet.
 *
 * @author Marty Lauterbach
 */
@SuppressWarnings("squid:S4144")
public class WettkampfInfoDO {

    private Long wettkampfId;
    private Long wettkampfTag;
    private Date wettkampfDatum;
    private String wettkampfBeginn;
    private String wettkampfOrtsname;
    private String wettkampfOrtsinfo;
    private String wettkampfStrasse;
    private String wettkampfPlz;

    // Veranstaltungsinfo
    private Long veranstaltungId;
    private String veranstaltungName;
    private Long veranstaltungSportjahr;
    private String ligaName;
    private String wettkampftypName;

    public WettkampfInfoDO() {
        // Standard-Konstruktor
    }

    public WettkampfInfoDO(Long wettkampfId, Long wettkampfTag, Date wettkampfDatum,
                           String wettkampfBeginn, String wettkampfOrtsname, String wettkampfOrtsinfo,
                           String wettkampfStrasse, String wettkampfPlz, Long veranstaltungId,
                           String veranstaltungName, Long veranstaltungSportjahr,
                           String ligaName, String wettkampftypName) {
        this.wettkampfId = wettkampfId;
        this.wettkampfTag = wettkampfTag;
        this.wettkampfDatum = wettkampfDatum;
        this.wettkampfBeginn = wettkampfBeginn;
        this.wettkampfOrtsname = wettkampfOrtsname;
        this.wettkampfOrtsinfo = wettkampfOrtsinfo;
        this.wettkampfStrasse = wettkampfStrasse;
        this.wettkampfPlz = wettkampfPlz;
        this.veranstaltungId = veranstaltungId;
        this.veranstaltungName = veranstaltungName;
        this.veranstaltungSportjahr = veranstaltungSportjahr;
        this.ligaName = ligaName;
        this.wettkampftypName = wettkampftypName;
    }

    // Getters and Setters
    public Long getWettkampfId() { return wettkampfId; }
    public void setWettkampfId(Long wettkampfId) { this.wettkampfId = wettkampfId; }

    public Long getWettkampfTag() { return wettkampfTag; }
    public void setWettkampfTag(Long wettkampfTag) { this.wettkampfTag = wettkampfTag; }

    public Date getWettkampfDatum() { return wettkampfDatum; }
    public void setWettkampfDatum(Date wettkampfDatum) { this.wettkampfDatum = wettkampfDatum; }

    public String getWettkampfBeginn() { return wettkampfBeginn; }
    public void setWettkampfBeginn(String wettkampfBeginn) { this.wettkampfBeginn = wettkampfBeginn; }

    public String getWettkampfOrtsname() { return wettkampfOrtsname; }
    public void setWettkampfOrtsname(String wettkampfOrtsname) { this.wettkampfOrtsname = wettkampfOrtsname; }

    public String getWettkampfOrtsinfo() { return wettkampfOrtsinfo; }
    public void setWettkampfOrtsinfo(String wettkampfOrtsinfo) { this.wettkampfOrtsinfo = wettkampfOrtsinfo; }

    public String getWettkampfStrasse() { return wettkampfStrasse; }
    public void setWettkampfStrasse(String wettkampfStrasse) { this.wettkampfStrasse = wettkampfStrasse; }

    public String getWettkampfPlz() { return wettkampfPlz; }
    public void setWettkampfPlz(String wettkampfPlz) { this.wettkampfPlz = wettkampfPlz; }

    public Long getVeranstaltungId() { return veranstaltungId; }
    public void setVeranstaltungId(Long veranstaltungId) { this.veranstaltungId = veranstaltungId; }

    public String getVeranstaltungName() { return veranstaltungName; }
    public void setVeranstaltungName(String veranstaltungName) { this.veranstaltungName = veranstaltungName; }

    public Long getVeranstaltungSportjahr() { return veranstaltungSportjahr; }
    public void setVeranstaltungSportjahr(Long veranstaltungSportjahr) { this.veranstaltungSportjahr = veranstaltungSportjahr; }

    public String getLigaName() { return ligaName; }
    public void setLigaName(String ligaName) { this.ligaName = ligaName; }

    public String getWettkampftypName() { return wettkampftypName; }
    public void setWettkampftypName(String wettkampftypName) { this.wettkampftypName = wettkampftypName; }
}