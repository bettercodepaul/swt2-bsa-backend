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
    private static final Long serialVersionUID = 8559563978424033907L;
    private Long userID;
    private Long wettkampfID;
    private boolean leitend;

    // The parameter names have to be the exact same as the attribute names in kampfrichter-do.class.ts
    public KampfrichterDTO(final Long id, final Long wettkampfID, final boolean leitend) {
        this.userID = id;
        this.wettkampfID = wettkampfID;
        this.leitend = leitend;
    }


    public Long getUserID() {
        return userID;
    }

    public void setUserId(final Long userId) {
        this.userID = userId;
    }


    public Long getWettkampfID() {
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


    @Override
    public String toString() {
        return "KampfrichterDTO{" +
                "id=" + userID +
                ", wettkampfID=" + wettkampfID +
                ", leitend=" + leitend +
                '}';
    }
}
