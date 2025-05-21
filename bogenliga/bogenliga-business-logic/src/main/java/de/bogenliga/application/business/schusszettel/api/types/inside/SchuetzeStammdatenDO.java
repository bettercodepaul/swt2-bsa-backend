package de.bogenliga.application.business.schusszettel.api.types.inside;

/**
 * Repräsentiert einen Schützen mit Name und Rückennummer.
 * Wird vom Business-Layer verwendet und an das REST-DTO übergeben.
 *
 * @author Marty Lauterbach
 */
public class SchuetzeStammdatenDO {

    private Long schuetzenId;
    private Integer rueckennummer;
    private String vorname;
    private String nachname;

    public SchuetzeStammdatenDO() {
        // Standard-Konstruktor
    }

    public SchuetzeStammdatenDO(Long schuetzenId, Integer rueckennummer, String vorname, String nachname) {
        this.schuetzenId = schuetzenId;
        this.rueckennummer = rueckennummer;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Long getSchuetzenId() {
        return schuetzenId;
    }

    public void setSchuetzenId(Long schuetzenId) {
        this.schuetzenId = schuetzenId;
    }

    public Integer getRueckennummer() {
        return rueckennummer;
    }

    public void setRueckennummer(Integer rueckennummer) {
        this.rueckennummer = rueckennummer;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
}
