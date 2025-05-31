package de.bogenliga.application.business.schusszettel.api.types.inside;

/**
 * Business-Objekt zur Darstellung eines Teams mit ID und Name.
 * Wird im Business-Layer für den Tablet-Schusszettel verwendet.
 *
 * @author Marty Lauterbach
 */
public class TeamInfoDO {
    private Long teamId;
    private String teamName;
    private Long matchID;

    public TeamInfoDO() {
        // Standard-Konstruktor
    }

    public TeamInfoDO(Long teamId, String teamName, Long matchID) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.matchID = matchID;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getMatchID() {
        return matchID;
    }
    public void setMatchID(Long matchID) {
        this.matchID = matchID;
    }
}
