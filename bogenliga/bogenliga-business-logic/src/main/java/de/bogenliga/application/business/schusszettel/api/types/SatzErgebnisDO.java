package de.bogenliga.application.business.schusszettel.api.types;

/**
 * Business-Objekt zur Darstellung eines Satz-Ergebnisses.
 * @author Marty Lauterbach, mklemmingen
 */
public class SatzErgebnisDO {
    private Integer satzNr;
    private Integer team1Punkte;
    private Integer team2Punkte;

    public Integer getSatzNr() { return satzNr; }
    public void setSatzNr(Integer satzNr) { this.satzNr = satzNr; }

    public Integer getTeam1Punkte() { return team1Punkte; }
    public void setTeam1Punkte(Integer team1Punkte) { this.team1Punkte = team1Punkte; }

    public Integer getTeam2Punkte() { return team2Punkte; }
    public void setTeam2Punkte(Integer team2Punkte) { this.team2Punkte = team2Punkte; }
}
