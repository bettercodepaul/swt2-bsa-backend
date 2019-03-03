package de.bogenliga.application.services.v1.kampfrichter.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the kampfrichter.
 *
 * I define the payload for the external REST interface of the kampfrichter business entity.
 *
 *
 * @see DataTransferObject
 */
public class KampfrichterDTO implements DataTransferObject {
    private static final long serialVersionUID = 8559563978424033907L;
    private Long wettkampfId;
    private Long userId;
    private boolean leitend;


    /**
     * Constructors
     */
    public KampfrichterDTO() {
        // empty constructor
    }


    public KampfrichterDTO(final long wettkampfId,
                           final Long userId, final boolean leitend) {
        this.wettkampfId = wettkampfId;


        this.userId = userId;
        this.leitend = leitend;
    }


    public Long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(final Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }



    public Long getUserId() {
        return userId;
    }


    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public boolean getLeitend(){return leitend;}

    public void setLeitend(final boolean leitend) {this.leitend = leitend;}
}
