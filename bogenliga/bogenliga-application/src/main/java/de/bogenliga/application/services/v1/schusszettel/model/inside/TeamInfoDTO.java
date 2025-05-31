package de.bogenliga.application.services.v1.schusszettel.model.inside;

/**
 * Enthält ID und Name eines Teams für Anzeigezwecke.
 * Wird im Tablet-Schusszettel sowohl für eigenes als auch gegnerisches Team verwendet.
 * @author Marty Lauterbach, mklemmingen
 */
public class TeamInfoDTO {
    private Long teamId;
    private String teamName;
    private Long matchID;


    public TeamInfoDTO() {
    }


    public TeamInfoDTO(Long teamId, String teamName, Long matchID) {
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
