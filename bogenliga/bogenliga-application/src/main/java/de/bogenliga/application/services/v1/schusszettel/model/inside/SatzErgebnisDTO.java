package de.bogenliga.application.services.v1.schusszettel.model.inside;

/**
 * Stellt das Ergebnis eines Satzes für beide Teams dar.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public class SatzErgebnisDTO {
    private Integer satzNr;
    private Integer team1Punkte;
    private Integer team2Punkte;

    public SatzErgebnisDTO() {
        // Für Jackson
    }

    public SatzErgebnisDTO(Integer satzNr, Integer team1Punkte, Integer team2Punkte) {
        this.satzNr = satzNr;
        this.team1Punkte = team1Punkte;
        this.team2Punkte = team2Punkte;
    }

    public Integer getSatzNr() { return satzNr; }
    public void setSatzNr(Integer satzNr) { this.satzNr = satzNr; }

    public Integer getTeam1Punkte() { return team1Punkte; }
    public void setTeam1Punkte(Integer team1Punkte) { this.team1Punkte = team1Punkte; }

    public Integer getTeam2Punkte() { return team2Punkte; }
    public void setTeam2Punkte(Integer team2Punkte) { this.team2Punkte = team2Punkte; }
}
