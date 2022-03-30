package de.bogenliga.application.business.vereine.api.types;

import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the verein Business Entity
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class VereinDO extends CommonDataObject implements DataObject {

    /**
     * business parameter
     */
    private Long id;
    private String name;
    private String dsbIdentifier;
    private Long regionId;
    private String regionName;
    private String website;
    private String description;
    private String icon;


    /**
     * Constructor with mandatory parameters
     *
     * @param id              ID of the Verein
     * @param name            Name of the Verein
     * @param dsbIdentifier   Unique identifier for the Verein
     * @param regionId        Region the Verein belongs to
     * @param website         Website of the Verein
     * @param description     Description of the Verein
     * @param createdAtUtc    The time the Verein was created.
     * @param createdByUserId The id of the user that created the Verein
     * @param version         Version of the Verein
     */
    public VereinDO(final Long id, final String name, final String dsbIdentifier, final Long regionId,
                    final String website, final String description, final String icon, final OffsetDateTime createdAtUtc,
                    final Long createdByUserId, final Long version) {
        this.id = id;
        this.name = name;
        this.dsbIdentifier = dsbIdentifier;
        this.regionId = regionId;
        this.website = website;
        this.description = description;
        this.icon = icon;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }


    /**
     * Constructor with optional parameters
     *
     * @param id
     * @param name
     * @param dsbIdentifier
     * @param regionId
     * @param regionName
     * @param website
     * @param description
     * @param createdAtUtc
     * @param createdByUserId
     * @param lastModifiedUtc
     * @param lastModifiedBy
     * @param version
     */
    public VereinDO(final Long id, final String name, final String dsbIdentifier, final Long regionId,
                    final String regionName, final String website, final String description, final String icon,
                    final OffsetDateTime createdAtUtc, final Long createdByUserId,
                    final OffsetDateTime lastModifiedUtc, final Long lastModifiedBy,
                    final Long version) {
        this.id = id;
        this.name = name;
        this.dsbIdentifier = dsbIdentifier;
        this.regionId = regionId;
        this.regionName = regionName;
        this.website = website;
        this.description = description;
        this.icon = icon;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedUtc;
        this.lastModifiedByUserId = lastModifiedBy;
        this.version = version;
    }

    /**
     * NoArgs constructor
     */
    public VereinDO(){

    }


    /**
     * Constuctor with id to delete existing verein entries
     *
     * @param id Id of existing Verein entry
     */
    public VereinDO(final Long id) {
        this.id = id;
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


    public String getDsbIdentifier() {
        return dsbIdentifier;
    }


    public void setDsbIdentifier(String dsbIdentifier) {
        this.dsbIdentifier = dsbIdentifier;
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


    @Override
    public int hashCode() {
        return Objects.hash(id, name, dsbIdentifier, regionId,
                website, description, icon, createdByUserId, lastModifiedAtUtc,
                lastModifiedByUserId, version);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VereinDO that = (VereinDO) o;
        return id == that.id &&
                regionId == that.regionId &&
                createdByUserId == that.createdByUserId &&
                lastModifiedByUserId == that.lastModifiedByUserId &&
                version == that.version &&
                Objects.equals(name, that.name) &&
                Objects.equals(dsbIdentifier, that.dsbIdentifier) &&
                Objects.equals(website, that.website) &&
                Objects.equals(description, that.description) &&
                Objects.equals(icon, that.icon) &&
                Objects.equals(lastModifiedAtUtc, that.lastModifiedAtUtc);
    }
}