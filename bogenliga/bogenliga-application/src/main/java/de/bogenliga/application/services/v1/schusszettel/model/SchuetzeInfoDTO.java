package de.bogenliga.application.services.v1.schusszettel.model;

import java.util.List;

/**
 * Repräsentiert einen Schützen und seine bisherige Punktzahl im laufenden Match.
 * @author Marty Lauterbach, mklemmingen
 */
class SchuetzeInfoDTO {
    private Long schuetzenId;
    private Integer punkteBisher;

    public Long getSchuetzenId() { return schuetzenId; }
    public void setSchuetzenId(Long schuetzenId) { this.schuetzenId = schuetzenId; }

    public Integer getPunkteBisher() { return punkteBisher; }
    public void setPunkteBisher(Integer punkteBisher) { this.punkteBisher = punkteBisher; }
}
