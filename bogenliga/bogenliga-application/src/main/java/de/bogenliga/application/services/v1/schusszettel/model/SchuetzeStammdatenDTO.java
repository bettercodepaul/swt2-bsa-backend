package de.bogenliga.application.services.v1.schusszettel.model;

/**
 * DTO für Schützeninformationen, die an das Tablet geliefert werden.
 *
 * Wird für die Anzeige der Namen, Rückennummern und IDs verwendet.
 *
 * @author Marty Lauterbach
 */
public class SchuetzeStammdatenDTO {

    private Long schuetzenId;
    private Integer rueckennummer;
    private String vorname;
    private String nachname;

    public SchuetzeStammdatenDTO() {
        // Standard-Konstruktor für Jackson
    }

    public SchuetzeStammdatenDTO(Long schuetzenId, Integer rueckennummer, String vorname, String nachname) {
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
