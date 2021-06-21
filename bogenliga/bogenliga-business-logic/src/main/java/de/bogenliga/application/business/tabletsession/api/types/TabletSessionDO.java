package de.bogenliga.application.business.tabletsession.api.types;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * @author Kay Scheerer
 */
public class TabletSessionDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 108484078463895720L;

    private Long wettkampfId;
    private Long scheibennummer;
    private Long satznummer;
    private Long matchId;
    private boolean active;
    private Long accessToken; // new CM

    public TabletSessionDO () {}

    public TabletSessionDO(Long wettkampfId, Long scheibennummer, Long satznummer, Long matchId, Boolean active, Long accessToken) { //new CM
        this.wettkampfId = wettkampfId;
        this.scheibennummer = scheibennummer;
        this.satznummer = satznummer;
        this.matchId = matchId;
        this.active = active;
        this.accessToken = accessToken;
    }


    public TabletSessionDO(Long wettkampfId, Long scheibennummer, Long satznummer, Long matchId, Boolean active, Long accessToken, //new CM
                           final OffsetDateTime createdAtUtc,
                           final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                           final Long lastModifiedByUserId, final Long version) {
        this.wettkampfId = wettkampfId;
        this.scheibennummer = scheibennummer;
        this.satznummer = satznummer;
        this.matchId = matchId;
        this.active = active;
        //new CM
        this.accessToken = accessToken;

        // set parameter from CommonDataObject
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
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


    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getAccessToken() {return this.accessToken;}

    public void setAccessToken(Long accessToken) {this.accessToken = accessToken;}
}
