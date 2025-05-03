package de.bogenliga.application.services.v1.schusszettel.model;

/**
 * Liste von Vereinsmitgliedern zur Auswahl bei Schützenmeldung.
 * @author Marty Lauterbach, mklemmingen
 */
public class VerfuegbarerSchuetzeDTO {
    private Long schuetzenId;
    private String name;

    // Konstruktor für Mapper
    public VerfuegbarerSchuetzeDTO(Long schuetzenId, String name) {
        this.schuetzenId = schuetzenId;
        this.name = name;
    }

    // Leerer Konstruktor für Frameworks
    public VerfuegbarerSchuetzeDTO() {}

    public Long getSchuetzenId() {
        return schuetzenId;
    }

    public void setSchuetzenId(Long schuetzenId) {
        this.schuetzenId = schuetzenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
