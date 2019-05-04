package de.bogenliga.application.services.v1.wettkampftyp.model;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 *  *I´m the data transfer object of the wettkampf.
 *
 *  *I define the payload for the external REST interface of the wettkampf business entity.
 *
 * @author Marvin Holm, Daniel Schott
 */
public class WettkampftypDTO implements DataTransferObject {



    private Long wettkampftypId;
    private String wettkamptypName;

    private OffsetDateTime createdAtUtc;
    private Long createdByUserId;
    private Long version;

    public WettkampftypDTO(Long wettkampftypId, String wettkamptypName,
                           Long createdByUserId, OffsetDateTime createdAtUtc, Long version){
        setWettkampftypId(wettkampftypId);
        setWettkamptypName(wettkamptypName);

    }


    public Long getWettkampftypId() {
        return wettkampftypId;
    }


    public void setWettkampftypId(Long wettkampftypId) {
        this.wettkampftypId = wettkampftypId;
    }


    public String getWettkamptypName() {
        return wettkamptypName;
    }


    public void setWettkamptypName(String wettkamptypName) {
        this.wettkamptypName = wettkamptypName;
    }


    public OffsetDateTime getCreatedAtUtc() {
        return createdAtUtc;
    }


    public void setCreatedAtUtc(OffsetDateTime createdAtUtc) {
        this.createdAtUtc = createdAtUtc;
    }


    public Long getCreatedByUserId() {
        return createdByUserId;
    }


    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }


    public Long getVersion() {
        return version;
    }


    public void setVersion(Long version) {
        this.version = version;
    }
}
