package de.bogenliga.application.business.schusszettel.api.types;

/**
 * Aggregiertes Match-Ergebnis eines Teams im Business-Layer.
 * @author Marty Lauterbach, mklemmingen
 */
public class TeamMatchInfoDO {
    private Long teamId;
    private String teamName;
    private Integer matchpunkte;

    public TeamMatchInfoDO() {
        // für Mapper etc.
    }

    public TeamMatchInfoDO(Long teamId, String teamName, Integer matchpunkte) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.matchpunkte = matchpunkte;
    }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public Integer getMatchpunkte() { return matchpunkte; }
    public void setMatchpunkte(Integer matchpunkte) { this.matchpunkte = matchpunkte; }
}
