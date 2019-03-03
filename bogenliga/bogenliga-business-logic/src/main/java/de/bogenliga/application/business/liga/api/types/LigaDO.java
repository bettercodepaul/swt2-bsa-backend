package de.bogenliga.application.business.liga.api.types;

import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the liga business entity.
 *
 * @author Bruno Michael Cunha Teixeira, Bruno_Michael.Cunha_teixeira@Student.Reutlingen-University.de
 */
public class LigaDO extends CommonDataObject implements DataObject {

    /**
     * business parameter
     */
    private Long id;
    private String name;
    private Long regionId;
    private String regionName;
    private Long ligaUebergeordnetId;
    private String ligaUebergeordnetName;
    private Long ligaVerantwortlichId;
    private String ligaVerantwortlichMail;


    public LigaDO() {
        // empty constructor
    }


    /**
     *
     * @param id
     */
    public LigaDO(final long id) {
        this.id = id;
    }


    /**
     * Constructor with optional parameters
     *
     * @param id
     * @param name
     * @param regionId
     * @param regionName
     * @param ligaUebergeordnetId
     * @param ligaUebergeordnetName
     * @param ligaVerantwortlichId
     * @param ligaVerantwortlichMail
     * @param createdAtUtc
     * @param createdByUserId
     * @param lastModifiedAtUtc
     * @param lastModifiedByUserId
     * @param version
     */
    public LigaDO(final Long id, final String name, final Long regionId, final String regionName,
                  final Long ligaUebergeordnetId, final String ligaUebergeordnetName,
                  final Long ligaVerantwortlichId, final String ligaVerantwortlichMail,
                  final OffsetDateTime createdAtUtc, final Long createdByUserId,
                  final OffsetDateTime lastModifiedAtUtc, final Long lastModifiedByUserId,
                  final Long version) {
        this.id=id;
        this.name = name;
        this.regionId = regionId;
        this.regionName = regionName;
        this.ligaUebergeordnetId = ligaUebergeordnetId;
        this.ligaUebergeordnetName = ligaUebergeordnetName;
        this.ligaVerantwortlichId = ligaVerantwortlichId;
        this.ligaVerantwortlichMail = ligaVerantwortlichMail;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    /**
     * Constructor with mandatory parameters
     *
     * @param id
     * @param name
     * @param regionId
     * @param regionName
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */
    public LigaDO(final Long id, final String name, final Long regionId, final String regionName,
                  final OffsetDateTime createdAtUtc, Long createdByUserId, final Long version) {
        this.id = id;
        this.name = name;
        this.regionId = regionId;
        this.regionName = regionName;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }


    /**
     * Constructor without technical parameters
     *
     * @param id
     * @param name
     * @param regionId
     * @param regionName
     * @param ligaUebergeordnetId
     * @param ligaUebergeordnetName
     * @param ligaVerantwortlichId
     * @param ligaVerantwortlichMail
     */
    public LigaDO(final Long id, final String name, final Long regionId, final String regionName,
                  final Long ligaUebergeordnetId, final String ligaUebergeordnetName,
                  final Long ligaVerantwortlichId, final String ligaVerantwortlichMail) {
        this.id = id;
        this.name = name;
        this.regionId = regionId;
        this.regionName = regionName;
        this.ligaUebergeordnetId = ligaUebergeordnetId;
        this.ligaUebergeordnetName = ligaUebergeordnetName;
        this.ligaVerantwortlichId = ligaVerantwortlichId;
        this.ligaVerantwortlichMail = ligaVerantwortlichMail;
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


    public Long getLigaUebergeordnetId() {
        return ligaUebergeordnetId;
    }


    public void setLigaUebergeordnetId(Long ligaUebergeordnetId) {
        this.ligaUebergeordnetId = ligaUebergeordnetId;
    }


    public String getLigaUebergeordnetName() {
        return ligaUebergeordnetName;
    }


    public void setLigaUebergeordnetName(String ligaUebergeordnetName) {
        this.ligaUebergeordnetName = ligaUebergeordnetName;
    }


    public Long getLigaVerantwortlichId() {
        return ligaVerantwortlichId;
    }


    public void setLigaVerantwortlichId(Long ligaVerantwortlichId) {
        this.ligaVerantwortlichId = ligaVerantwortlichId;
    }


    public String getLigaVerantwortlichMail() {
        return ligaVerantwortlichMail;
    }


    public void setLigaVerantwortlichMail(String ligaVerantwortlichMail) {
        this.ligaVerantwortlichMail = ligaVerantwortlichMail;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, regionId, regionName, ligaUebergeordnetId,
                ligaUebergeordnetName, ligaVerantwortlichId, ligaVerantwortlichMail,
                createdByUserId, lastModifiedAtUtc, getLastModifiedByUserId(), version);
    }

    @Override
    public boolean equals(final Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LigaDO that = (LigaDO) o;
        return id == that.id &&
                regionId == that.regionId &&
                ligaUebergeordnetId ==that.ligaUebergeordnetId &&
                ligaVerantwortlichId == that.ligaVerantwortlichId &&
                Objects.equals(name, that.name)&&
                Objects.equals(regionName,that.regionName)&&
                Objects.equals(ligaUebergeordnetName,that.ligaUebergeordnetName)&&
                Objects.equals(ligaVerantwortlichMail,that.ligaVerantwortlichMail)&&
                Objects.equals(lastModifiedAtUtc,that.lastModifiedAtUtc);

    }

}