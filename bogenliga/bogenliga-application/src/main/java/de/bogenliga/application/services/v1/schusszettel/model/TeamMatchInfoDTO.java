package de.bogenliga.application.services.v1.schusszettel.model;

import java.util.List;

/**
 * Aggregiertes Match-Ergebnis eines Teams, bestehend aus ID, Name und Matchpunkten.
 * @author Marty Lauterbach, mklemmingen
 */
class TeamMatchInfoDTO {
    private Long teamId;
    private String teamName;
    private Integer matchpunkte;

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public Integer getMatchpunkte() { return matchpunkte; }
    public void setMatchpunkte(Integer matchpunkte) { this.matchpunkte = matchpunkte; }
}