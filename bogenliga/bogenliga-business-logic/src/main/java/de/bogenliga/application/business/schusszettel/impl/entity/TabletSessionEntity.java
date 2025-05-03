package de.bogenliga.application.business.schusszettel.impl.entity;

import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * Entity für eine Tablet-Schusszettel-Session.
 * Repräsentiert eine persistente Autorisierungs- und Statusinstanz pro Team am Spieltag.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public class TabletSessionEntity extends CommonBusinessEntity {

    private Long id;
    private String token;
    private Long teamId;
    private Long wettkampfId;
    private Long currentMatchId;
    private Integer currentPasseNumber;
    private String status;
    private Long gegnerTeamId;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public Long getWettkampfId() { return wettkampfId; }
    public void setWettkampfId(Long wettkampfId) { this.wettkampfId = wettkampfId; }

    public Long getCurrentMatchId() { return currentMatchId; }
    public void setCurrentMatchId(Long currentMatchId) { this.currentMatchId = currentMatchId; }

    public Integer getCurrentPasseNumber() { return currentPasseNumber; }
    public void setCurrentPasseNumber(Integer currentPasseNumber) { this.currentPasseNumber = currentPasseNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getGegnerTeamId() { return gegnerTeamId; }
    public void setGegnerTeamId(Long gegnerTeamId) { this.gegnerTeamId = gegnerTeamId; }
}