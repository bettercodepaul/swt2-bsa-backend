package de.bogenliga.application.services.v1.wettkampf. model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * DTO für kombinierte Veranstaltung-Wettkampf-Daten für die Startseite
 */
public class VeranstaltungWettkampfDTO implements DataTransferObject {

    // Wettkampf-Felder
    private Long wettkampfId;
    private String wettkampfDatum;
    private Long wettkampfTag;
    private String wettkampfStrasse;
    private String wettkampfPlz;
    private String wettkampfOrtsname;
    private String wettkampfOrtsinfo;
    private String wettkampfBeginn;
    private Long wettkampfDisziplinId;
    private Long wettkampfTypId;
    private Long wettkampfAusrichter;

    // Veranstaltung-Felder
    private Long veranstaltungId;
    private String veranstaltungName;
    private Long veranstaltungSportjahr;
    private Long veranstaltungLigaId;
    private String veranstaltungLigaName;

    // Konstruktoren
    public VeranstaltungWettkampfDTO() {
        // Empty constructor
    }

    public VeranstaltungWettkampfDTO(
            Long wettkampfId,
            String wettkampfDatum,
            Long wettkampfTag,
            String wettkampfStrasse,
            String wettkampfPlz,
            String wettkampfOrtsname,
            String wettkampfOrtsinfo,
            String wettkampfBeginn,
            Long wettkampfDisziplinId,
            Long wettkampfTypId,
            Long wettkampfAusrichter,
            Long veranstaltungId,
            String veranstaltungName,
            Long veranstaltungSportjahr,
            Long veranstaltungLigaId) {
        this.wettkampfId = wettkampfId;
        this.wettkampfDatum = wettkampfDatum;
        this.wettkampfTag = wettkampfTag;
        this.wettkampfStrasse = wettkampfStrasse;
        this. wettkampfPlz = wettkampfPlz;
        this.wettkampfOrtsname = wettkampfOrtsname;
        this.wettkampfOrtsinfo = wettkampfOrtsinfo;
        this.wettkampfBeginn = wettkampfBeginn;
        this.wettkampfDisziplinId = wettkampfDisziplinId;
        this.wettkampfTypId = wettkampfTypId;
        this. wettkampfAusrichter = wettkampfAusrichter;
        this.veranstaltungId = veranstaltungId;
        this. veranstaltungName = veranstaltungName;
        this.veranstaltungSportjahr = veranstaltungSportjahr;
        this.veranstaltungLigaId = veranstaltungLigaId;
        this.veranstaltungLigaName = veranstaltungLigaName;
    }

    // Getter und Setter
    public Long getWettkampfId() { return wettkampfId; }
    public void setWettkampfId(Long wettkampfId) { this.wettkampfId = wettkampfId; }

    public String getWettkampfDatum() { return wettkampfDatum; }
    public void setWettkampfDatum(String wettkampfDatum) { this.wettkampfDatum = wettkampfDatum; }

    public Long getWettkampfTag() { return wettkampfTag; }
    public void setWettkampfTag(Long wettkampfTag) { this.wettkampfTag = wettkampfTag; }

    public String getWettkampfStrasse() { return wettkampfStrasse; }
    public void setWettkampfStrasse(String wettkampfStrasse) { this.wettkampfStrasse = wettkampfStrasse; }

    public String getWettkampfPlz() { return wettkampfPlz; }
    public void setWettkampfPlz(String wettkampfPlz) { this.wettkampfPlz = wettkampfPlz; }

    public String getWettkampfOrtsname() { return wettkampfOrtsname; }
    public void setWettkampfOrtsname(String wettkampfOrtsname) { this.wettkampfOrtsname = wettkampfOrtsname; }

    public String getWettkampfOrtsinfo() { return wettkampfOrtsinfo; }
    public void setWettkampfOrtsinfo(String wettkampfOrtsinfo) { this.wettkampfOrtsinfo = wettkampfOrtsinfo; }

    public String getWettkampfBeginn() { return wettkampfBeginn; }
    public void setWettkampfBeginn(String wettkampfBeginn) { this.wettkampfBeginn = wettkampfBeginn; }

    public Long getWettkampfDisziplinId() { return wettkampfDisziplinId; }
    public void setWettkampfDisziplinId(Long wettkampfDisziplinId) { this.wettkampfDisziplinId = wettkampfDisziplinId; }

    public Long getWettkampfTypId() { return wettkampfTypId; }
    public void setWettkampfTypId(Long wettkampfTypId) { this.wettkampfTypId = wettkampfTypId; }

    public Long getWettkampfAusrichter() { return wettkampfAusrichter; }
    public void setWettkampfAusrichter(Long wettkampfAusrichter) { this.wettkampfAusrichter = wettkampfAusrichter; }

    public Long getVeranstaltungId() { return veranstaltungId; }
    public void setVeranstaltungId(Long veranstaltungId) { this.veranstaltungId = veranstaltungId; }

    public String getVeranstaltungName() { return veranstaltungName; }
    public void setVeranstaltungName(String veranstaltungName) { this.veranstaltungName = veranstaltungName; }

    public Long getVeranstaltungSportjahr() { return veranstaltungSportjahr; }
    public void setVeranstaltungSportjahr(Long veranstaltungSportjahr) { this.veranstaltungSportjahr = veranstaltungSportjahr; }

    public Long getVeranstaltungLigaId() { return veranstaltungLigaId; }
    public void setVeranstaltungLigaId(Long veranstaltungLigaId) { this.veranstaltungLigaId = veranstaltungLigaId; }

    public String getVeranstaltungLigaName() { return veranstaltungLigaName; }
    public void setVeranstaltungLigaName(String veranstaltungLigaName) { this.veranstaltungLigaName = veranstaltungLigaName; }
}