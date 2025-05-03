package de.bogenliga.application.business.schusszettel.api.types;

/**
 * Repräsentiert einen Schützen mit Punktestand für das laufende Match.
 * Wird im Business-Layer verwendet.
 *
 * @author Marty Lauterbach
 */
public class SchuetzeInfoDO {
    private Long schuetzenId;
    private Integer punkteBisher;

    public SchuetzeInfoDO() {
        // Standard-Konstruktor
    }

    public SchuetzeInfoDO(Long schuetzenId, Integer punkteBisher) {
        this.schuetzenId = schuetzenId;
        this.punkteBisher = punkteBisher;
    }

    public Long getSchuetzenId() {
        return schuetzenId;
    }

    public void setSchuetzenId(Long schuetzenId) {
        this.schuetzenId = schuetzenId;
    }

    public Integer getPunkteBisher() {
        return punkteBisher;
    }

    public void setPunkteBisher(Integer punkteBisher) {
        this.punkteBisher = punkteBisher;
    }
}
