package de.bogenliga.application.services.v1.schusszettel.model;

/**
 * Repräsentiert einen Schützen und seine bisherige Punktzahl im laufenden Match.
 * @author Marty Lauterbach, mklemmingen
 */
public class SchuetzeMatchPunkteDTO {
    private Long schuetzenId;
    private Integer punkteBisher;

    public SchuetzeMatchPunkteDTO() {
        // Leerer Konstruktor für Jackson
    }

    public SchuetzeMatchPunkteDTO(Long schuetzenId, Integer punkteBisher) {
        this.schuetzenId = schuetzenId;
        this.punkteBisher = punkteBisher;
    }

    public Long getSchuetzenId() { return schuetzenId; }
    public void setSchuetzenId(Long schuetzenId) { this.schuetzenId = schuetzenId; }

    public Integer getPunkteBisher() { return punkteBisher; }
    public void setPunkteBisher(Integer punkteBisher) { this.punkteBisher = punkteBisher; }
}
