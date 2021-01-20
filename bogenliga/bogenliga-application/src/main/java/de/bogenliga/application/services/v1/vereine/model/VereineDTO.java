package de.bogenliga.application.services.v1.vereine.model;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I'm the data transfer Object of Vereine.
 * <p>
 * I define the payload for the external REST interface of the Vereine business entity
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
public class VereineDTO implements DataTransferObject {
    private Long id;
    private String name;
    private String identifier;
    private Long regionId;
    private String regionName;
    private String website;
    private String description;
    private String icon;
    private OffsetDateTime createdAtUtc;
    private Long createdByUserId;
    private Long version;


    /**
     * Constructors
     */

    public VereineDTO() {
        //empty
    }


    /**
     *
     * @param id
     * @param name
     * @param identifier
     * @param regionId
     * @param regionName
     * @param website
     * @param description
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */
    public VereineDTO(Long id, String name, String identifier, Long regionId, String regionName,
                      String website, String description, String icon, OffsetDateTime createdAtUtc,
                      Long createdByUserId, Long version) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.regionId = regionId;
        this.regionName = regionName;
        this.website = website;
        this.description = description;
        this.icon = icon;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }

    public VereineDTO(Long id, String name, String identifier, Long regionId,
                      String website, String description, String icon, OffsetDateTime createdAtUtc,
                      Long createdByUserId, Long version) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.regionId = regionId;
        this.website = website;
        this.description = description;
        this.icon = icon;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
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


    public String getRegionName() {
        return regionName;
    }


    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }


    public String getWebsite() { return website; }


    public void setWebsite(String website) { this.website = website; }


    public String getDescription() { return description; }


    public void setDescription(String description) { this.description = description; }


    public String getIcon() { return icon; }


    public void setIcon(String icon) { this.icon = icon; }


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
