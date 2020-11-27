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


    /**
     * Constructor with mandatory parameters
     *
     * @param id              ID of the Verein
     * @param name            Name of the Verein
     * @param dsbIdentifier   Unique identifier for the Verein
     * @param regionId        Region the Verein belongs to
     * @param createdAtUtc    The time the Verein was created.
     * @param createdByUserId The id of the user that created the Verein
     * @param version         Version of the Verein
     */
    public VereinDO(final Long id, final String name, final String dsbIdentifier, final Long regionId,
                    final OffsetDateTime createdAtUtc, final Long createdByUserId,
                    final Long version) {
        this.id = id;
        this.name = name;
        this.dsbIdentifier = dsbIdentifier;
        this.regionId = regionId;
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
     * @param createdAtUtc
     * @param createdByUserId
     * @param lastModifiedUtc
     * @param lastModifiedBy
     * @param version
     */
    public VereinDO(final Long id, final String name, final String dsbIdentifier, final Long regionId,
                    final String regionName,
                    final OffsetDateTime createdAtUtc, final Long createdByUserId,
                    final OffsetDateTime lastModifiedUtc, final Long lastModifiedBy,
                    final Long version) {
        this.id = id;
        this.name = name;
        this.dsbIdentifier = dsbIdentifier;
        this.regionId = regionId;
        this.regionName = regionName;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedUtc;
        this.lastModifiedByUserId = lastModifiedBy;
        this.version = version;
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


    @Override
    public int hashCode() {
        return Objects.hash(id, name, dsbIdentifier, regionId,
                createdByUserId, lastModifiedAtUtc,
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
                Objects.equals(lastModifiedAtUtc, that.lastModifiedAtUtc);
    }
}
