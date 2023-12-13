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
    private Long disziplinId;
    private Long regionId;
    private String regionName;
    private Long ligaUebergeordnetId;
    private String ligaUebergeordnetName;
    private Long ligaVerantwortlichId;
    private String ligaVerantwortlichMail;
    private String ligaDetail;
    private String ligaFileBase64;
    private String ligaFileName;
    private String ligaFileType;


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
     * @param regionName
     * @param ligaUebergeordnetId
     * @param ligaUebergeordnetName
     * @param ligaVerantwortlichId
     * @param ligaVerantwortlichMail
     * @param createdByUserId
     */
    public LigaDO(final Long id, final String name, final Long disziplinId, final String regionName,
                  final Long ligaUebergeordnetId, final String ligaUebergeordnetName,
                  final Long ligaVerantwortlichId, final String ligaVerantwortlichMail,
                  final Long createdByUserId,
                  final String ligaDetail) {
        this.id=id;
        this.name = name;
        this.disziplinId = disziplinId;

        this.regionName = regionName;
        this.ligaUebergeordnetId = ligaUebergeordnetId;
        this.ligaUebergeordnetName = ligaUebergeordnetName;
        this.ligaVerantwortlichId = ligaVerantwortlichId;
        this.ligaVerantwortlichMail = ligaVerantwortlichMail;

        this.createdByUserId = createdByUserId;



        this.ligaDetail = ligaDetail;



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
                  final Long ligaVerantwortlichId, final String ligaVerantwortlichMail, final Long disziplinId, final String ligaDetail,
                  final String ligaFileBase64, final String ligaFileName, final String ligaFileType) {
        this.id = id;
        this.name = name;
        this.regionId = regionId;
        this.disziplinId = disziplinId;
        this.regionName = regionName;
        this.ligaUebergeordnetId = ligaUebergeordnetId;
        this.ligaUebergeordnetName = ligaUebergeordnetName;
        this.ligaVerantwortlichId = ligaVerantwortlichId;
        this.ligaVerantwortlichMail = ligaVerantwortlichMail;
        this.ligaDetail = ligaDetail;
        this.ligaFileBase64 = ligaFileBase64;
        this.ligaFileName = ligaFileName;
        this.ligaFileType = ligaFileType;



    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getDisziplinId() { return this.disziplinId; }
    public void setDisziplinId(Long disziplinId) { this.disziplinId = disziplinId; }


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



    public String getLigaDetail() {
        return ligaDetail;
    }


    public void setLigaDetail(String ligaDetail) {
        this.ligaDetail = ligaDetail;
    }

    public String getLigaFileBase64() {
        return ligaFileBase64;
    }


    public void setLigaFileBase64(String ligaFileBase64) {
        this.ligaFileBase64 = ligaFileBase64;
    }

    public String getLigaFileName() {
        return ligaFileName;
    }


    public void setLigaFileName(String ligaFileName) {
        this.ligaFileName = ligaFileName;
    }

    public String getLigaFileType() {
        return ligaFileType;
    }


    public void setLigaFileType(String ligaFileType) {
        this.ligaFileType = ligaFileType;
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