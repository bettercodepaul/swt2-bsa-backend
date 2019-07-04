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
    private Long id;
    private Long userid;
    private boolean leitend;


    /**
     * Constructors
     */
    public KampfrichterDTO() {
        // empty constructor
    }


    public KampfrichterDTO(final long wettkampfId,
                           final Long userId, final boolean leitend) {
        this.id = wettkampfId;


        this.userid = userId;
        this.leitend = leitend;
    }


    public Long getWettkampfId() {
        return id;
    }


    public void setWettkampfId(final Long wettkampfId) {
        this.id = wettkampfId;
    }



    public Long getUserId() {
        return userid;
    }


    public void setUserId(final Long userId) {
        this.userid = userId;
    }

    public boolean getLeitend(){return leitend;}

    public void setLeitend(final boolean leitend) {this.leitend = leitend;}
}
