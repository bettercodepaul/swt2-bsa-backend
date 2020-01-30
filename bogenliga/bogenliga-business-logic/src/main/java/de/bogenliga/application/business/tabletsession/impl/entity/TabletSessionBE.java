package de.bogenliga.application.business.tabletsession.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * @author Kay Scheerer
 */
public class TabletSessionBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 5856944226092695392L;

    private Long wettkampfId;
    private Long scheibennummer;
    private Long matchId;
    private Long satznummer;
    private Boolean active = false;
    private Long accessToken;


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


    public Boolean isActive() {
        return active;
    }


    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getAccessToken() {return this.accessToken;}

    public void setAccessToken(Long accessToken) {this.accessToken = accessToken;}
}
