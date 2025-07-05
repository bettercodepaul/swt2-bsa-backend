package de.bogenliga.application.services.v1.schusszettel.model.inside;

/**
 * Stellt das Ergebnis eines Satzes für beide Teams dar.
 * Enriched mit Team-Informationen für Frontend-Anzeige.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public class SatzErgebnisDTO {
    private Integer satzNr;
    private Integer team1Punkte;
    private Integer team2Punkte;
    // Enrichment: Team-Informationen
    private Long team1Id;
    private String team1Name;
    private Long team2Id;
    private String team2Name;

    public SatzErgebnisDTO() {
        // Für Jackson
    }

    public SatzErgebnisDTO(Integer satzNr, Integer team1Punkte, Integer team2Punkte) {
        this.satzNr = satzNr;
        this.team1Punkte = team1Punkte;
        this.team2Punkte = team2Punkte;
    }

    // Enriched constructor with team information
    public SatzErgebnisDTO(Integer satzNr, Integer team1Punkte, Integer team2Punkte, 
                          Long team1Id, String team1Name, Long team2Id, String team2Name) {
        this.satzNr = satzNr;
        this.team1Punkte = team1Punkte;
        this.team2Punkte = team2Punkte;
        this.team1Id = team1Id;
        this.team1Name = team1Name;
        this.team2Id = team2Id;
        this.team2Name = team2Name;
    }

    public Integer getSatzNr() { return satzNr; }
    public void setSatzNr(Integer satzNr) { this.satzNr = satzNr; }

    public Integer getTeam1Punkte() { return team1Punkte; }
    public void setTeam1Punkte(Integer team1Punkte) { this.team1Punkte = team1Punkte; }

    public Integer getTeam2Punkte() { return team2Punkte; }
    public void setTeam2Punkte(Integer team2Punkte) { this.team2Punkte = team2Punkte; }

    // Team information getters and setters
    public Long getTeam1Id() { return team1Id; }
    public void setTeam1Id(Long team1Id) { this.team1Id = team1Id; }

    public String getTeam1Name() { return team1Name; }
    public void setTeam1Name(String team1Name) { this.team1Name = team1Name; }

    public Long getTeam2Id() { return team2Id; }
    public void setTeam2Id(Long team2Id) { this.team2Id = team2Id; }

    public String getTeam2Name() { return team2Name; }
    public void setTeam2Name(String team2Name) { this.team2Name = team2Name; }
}
