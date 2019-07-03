package de.bogenliga.application.services.v1.tabletsession.model;

/**
 * @author Kay Scheerer
 */
public class TabletSessionDTO {

    public TabletSessionDTO(Long wettkampfId, Long scheibennummer, Long satznummer, Long matchId) {
        this.wettkampfId = wettkampfId;
        this.scheibennummer = scheibennummer;
        this.satznummer = satznummer;
        this.matchId = matchId;
    }

    public TabletSessionDTO(){

    }

    public Long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }


    public Long getScheibennummer() {
        return scheibennummer;
    }


    public void setScheibennummer(Long scheibennummer) {
        this.scheibennummer = scheibennummer;
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


    private Long wettkampfId;
    private Long scheibennummer;
    private Long satznummer;
    private Long matchId;
}
