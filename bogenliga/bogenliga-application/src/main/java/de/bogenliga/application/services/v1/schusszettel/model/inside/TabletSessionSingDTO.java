package de.bogenliga.application.services.v1.schusszettel.model.inside;

/**
 * Enthält eine einzige Session eines Teams an einem WettkampfTag.
 *
 * Attributes:
 * * - WettkampfId
 *      * Per Team:
 *      * - TeamId
 *      * - TeamName
 *      * - Status
 *      * - Token
 *      * - CurrentPasse
 *      * - Nächster Gegner (can be null)
 *
 * @author Marty Lauterbach
 */
public class TabletSessionSingDTO {

    private Long teamId;
    private String teamName;
    private String status;
    private String token;
    private Integer currentPasse;
    private String naechsterGegnerName;

    // Default constructor
    public TabletSessionSingDTO() {
    }

    // Constructor with all fields
    public TabletSessionSingDTO(Long teamId, String teamName, String status, String token,
                               Integer currentPasse, String naechsterGegnerName) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.status = status;
        this.token = token;
        this.currentPasse = currentPasse;
        this.naechsterGegnerName = naechsterGegnerName;
    }

    // Getters and Setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCurrentPasse() {
        return currentPasse;
    }

    public void setCurrentPasse(Integer currentPasse) {
        this.currentPasse = currentPasse;
    }

    public String getNaechsterGegner() {
        return naechsterGegnerName;
    }

    public void setNaechsterGegner(String naechsterGegnerName) {
        this.naechsterGegnerName = naechsterGegnerName;
    }

    // Renamed getter/setter to match the field name
    public String getNaechsterGegnerName() {
        return naechsterGegnerName;
    }

    public void setNaechsterGegnerName(String naechsterGegnerName) {
        this.naechsterGegnerName = naechsterGegnerName;
    }
}
