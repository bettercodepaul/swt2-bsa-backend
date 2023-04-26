package de.bogenliga.application.business.setzliste.impl.entity;

import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the setzliste business entity.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class SetzlisteBE extends CommonBusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;

    private Integer ligatabelleTabellenplatz;
    private Long wettkampfID;
    private Long mannschaftID;

    public SetzlisteBE(){
        // empty constructor
    }

    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getLigatabelleTabellenplatz() {
        return ligatabelleTabellenplatz;
    }

    public void setLigatabelleTabellenplatz(Integer ligatabelleTabellenplatz) {
        this.ligatabelleTabellenplatz = ligatabelleTabellenplatz;
    }

    public Long getWettkampfID() { return wettkampfID; }

    public void setWettkampfID(Long wettkampfID) { this.wettkampfID = wettkampfID; }

    public Long getMannschaftID() {
        return mannschaftID;
    }

    public void setMannschaftID(Long mannschaftID) {
        this.mannschaftID = mannschaftID;
    }

    @Override
    public String toString() {
        return "setzliste{ligatabelleTabellenplatz='" + ligatabelleTabellenplatz +
                "', WettkampfID='" + wettkampfID +
                "', MannschaftID='" + mannschaftID +
                "'}";
    }
}
