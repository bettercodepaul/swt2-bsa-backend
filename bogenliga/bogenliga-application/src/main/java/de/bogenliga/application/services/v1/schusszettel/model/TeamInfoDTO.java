package de.bogenliga.application.services.v1.schusszettel.model;

/**
 * Enthält ID und Name eines Teams für Anzeigezwecke.
 * Wird im Tablet-Schusszettel sowohl für eigenes als auch gegnerisches Team verwendet.
 * @author Marty Lauterbach, mklemmingen
 */
public class TeamInfoDTO {
    private Long teamId;
    private String teamName;

    public TeamInfoDTO() {}

    public TeamInfoDTO(Long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
}