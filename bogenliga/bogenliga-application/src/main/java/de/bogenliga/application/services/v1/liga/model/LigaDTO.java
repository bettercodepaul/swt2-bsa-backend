package de.bogenliga.application.services.v1.liga.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I'm the data transfer object of the liga.
 * <p>
 * I define the payload for the external REST interface of the liga business entity.
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class LigaDTO implements DataTransferObject {


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
    private String ligaDetailFileBase64;
    private String ligaDetailFileName;
    private String ligaDetailFileType;


    /**
     * @param id
     * @param name
     * @param regionId
     * @param regionName
     * @param ligaUebergeordnetId
     * @param ligaUebergeordnetName
     * @param ligaVerantwortlichId
     * @param ligaVerantwortlichMail
     */
    public LigaDTO(Long id, String name, Long regionId, String regionName, Long ligaUebergeordnetId,
                   String ligaUebergeordnetName, Long ligaVerantwortlichId, String ligaVerantwortlichMail, Long disziplinId,
                   String ligaDetail, String ligaDetailFileBase64, String ligaDetailFileName, String ligaDetailFileType) {
        this.id = id;
        this.name = name;
        this.regionId = regionId;
        this.regionName = regionName;
        this.ligaUebergeordnetId = ligaUebergeordnetId;
        this.ligaUebergeordnetName = ligaUebergeordnetName;
        this.ligaVerantwortlichId = ligaVerantwortlichId;
        this.ligaVerantwortlichMail = ligaVerantwortlichMail;
        this.disziplinId = disziplinId;
        this.ligaDetail = ligaDetail;
        this.ligaDetailFileBase64 = ligaDetailFileBase64;
        this.ligaDetailFileName = ligaDetailFileName;
        this.ligaDetailFileType = ligaDetailFileType;
    }


    public LigaDTO() {
        // empty constructor
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getDisziplinId() { return this.disziplinId;}

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


    public void setLigaVerantwortlichMail(String ligaVerantwortlichMail) {
        this.ligaVerantwortlichMail = ligaVerantwortlichMail;
    }


    public String getLigaDetail() {
        return ligaDetail;
    }


    public void setLigaDetail(String ligaDetail) {
        this.ligaDetail = ligaDetail;
    }

    public String getLigaDetailFileBase64() {
        return ligaDetailFileBase64;
    }


    public void setLigaDetailFileBase64(String ligaDetailFileBase64) {
        this.ligaDetailFileBase64 = ligaDetailFileBase64;
    }

    public String getLigaDetailFileName() {
        return ligaDetailFileName;
    }


    public void setLigaDetailFileName(String ligaDetailFileName) {
        this.ligaDetailFileName = ligaDetailFileName;
    }

    public String getLigaDetailFileType() {
        return ligaDetailFileType;
    }


    public void setLigaDetailFileType(String ligaDetailFileType) {
        this.ligaDetailFileType = ligaDetailFileType;
    }

}
