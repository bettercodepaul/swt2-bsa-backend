package de.bogenliga.application.services.v1.tabletsession.model;

/**
 * @author Kay Scheerer
 */
public class TabletSessionDTO {

    private Long wettkampfId;
    private Long matchScheibennummer;
    private Long satznummer;
    private Long matchId;
    private Long otherMatchId;
    private boolean active = false;
    private Long accessToken;


    public TabletSessionDTO(Long wettkampfId, Long matchScheibennummer, Long satznummer, Long matchId, Boolean active, Long accessToken) {
        this.wettkampfId = wettkampfId;
        this.matchScheibennummer = matchScheibennummer;
        this.satznummer = satznummer;
        this.matchId = matchId;
        this.active = active;
        this.accessToken = accessToken;
    }

    public TabletSessionDTO(){
    }

    public Long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }


    public Long getMatchScheibennummer() {
        return this.matchScheibennummer;
    }


    public void setMatchScheibennummer(Long matchScheibennummer) {
        this.matchScheibennummer = matchScheibennummer;
    }


    public Long getSatznummer() {
        return satznummer;
    }


    public void setSatznummer(Long satznummer) {
        this.satznummer = satznummer;
    }


    public Long getMatchId() {
        return matchId;
    }


    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }


    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }


    public void setOtherMatchId(Long id) {
        this.otherMatchId = id;
    }

    public Long getOtherMatchId() {
        return this.otherMatchId;
    }

    public Long getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(Long accessToken) {
        this.accessToken = accessToken;
    }
}
