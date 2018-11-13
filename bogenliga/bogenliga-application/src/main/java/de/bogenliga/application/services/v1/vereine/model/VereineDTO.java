package de.bogenliga.application.services.v1.vereine.model;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I'm the data transfer Object of Vereine.
 *
 * I define the payload for the external REST interface of the Vereine business entity
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
public class VereineDTO implements DataTransferObject {
    private Long id;
    private String name;
    private String identifier;
    private Long regionId;
    private OffsetDateTime createdAtUtc;
    private Long createdByUserId;
    private Long version;


    /**
     * Constructors
     *
     */

    public VereineDTO(){
        //empty
    }

    public VereineDTO (final Long id, final String name, final String identifier, final Long regionId,
                       final OffsetDateTime createdAtUtc, final Long createdByUserId, final Long version ){

        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.regionId = regionId;
        this.createdAtUtc = createdAtUtc;
        this.version = version;
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


    public String getIdentifier() {
        return identifier;
    }


    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    public Long getRegionId() {
        return regionId;
    }


    public void setRegionId(Long regionId) {
        this.regionId = regionId;
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
        version = version;
    }
}
