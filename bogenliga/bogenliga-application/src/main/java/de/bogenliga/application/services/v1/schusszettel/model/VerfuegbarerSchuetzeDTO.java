package de.bogenliga.application.services.v1.schusszettel.model;

import java.util.List;

/**
 * Liste von Vereinsmitgliedern zur Auswahl bei Schützenmeldung.
 * @author Marty Lauterbach, mklemmingen
 */
class VerfuegbarerSchuetzeDTO {
    private Long schuetzenId;
    private String name;

    public Long getSchuetzenId() { return schuetzenId; }
    public void setSchuetzenId(Long schuetzenId) { this.schuetzenId = schuetzenId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}