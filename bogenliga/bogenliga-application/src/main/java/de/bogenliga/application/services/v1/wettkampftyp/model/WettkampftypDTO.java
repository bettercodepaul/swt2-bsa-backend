package de.bogenliga.application.services.v1.wettkampftyp.model;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 *  *I´m the data transfer object of the wettkampftyp.
 *
 *  *I define the payload for the external REST interface of the wettkampftyp business entity.
 *
 * @author Marvin Holm, Daniel Schott
 */
public class WettkampftypDTO implements DataTransferObject {



    private Long id;
    private String name;

    private OffsetDateTime createdAtUtc;
    private Long createdByUserId;
    private Long version;

    public WettkampftypDTO(Long wettkampftypId, String name, Long createdByUserId, OffsetDateTime createdAtUtc, Long version){
        this.setId(wettkampftypId);
        this.setName(name);

        this.setCreatedAtUtc(createdAtUtc);
        this.setCreatedByUserId(createdByUserId);
        this.setVersion(version);

    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
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
