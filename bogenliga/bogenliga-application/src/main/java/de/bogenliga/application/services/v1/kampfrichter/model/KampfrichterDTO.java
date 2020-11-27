package de.bogenliga.application.services.v1.kampfrichter.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the kampfrichter.
 * <p>
 * I define the payload for the external REST interface of the kampfrichter business entity.
 *
 * @see DataTransferObject
 */
public class KampfrichterDTO implements DataTransferObject {
    private static final long serialVersionUID = 8559563978424033907L;
    private Long userID;
    private Long wettkampfID;
    private boolean leitend;


    /**
     * Constructors
     */
    public KampfrichterDTO() {
        // empty constructor
    }


    public KampfrichterDTO(final Long userId, final long wettkampfId, final boolean leitend) {
        this.userID = userId;
        this.wettkampfID = wettkampfId;
        this.leitend = leitend;
    }


    public Long getUserId() {
        return userID;
    }


    public void setUserId(final Long userId) {
        this.userID = userId;
    }


    public Long getWettkampfId() {
        return wettkampfID;
    }


    public void setWettkampfId(final Long wettkampfId) {
        this.wettkampfID = wettkampfId;
    }


    public boolean getLeitend() {
        return leitend;
    }


    public void setLeitend(final boolean leitend) {
        this.leitend = leitend;
    }
}
