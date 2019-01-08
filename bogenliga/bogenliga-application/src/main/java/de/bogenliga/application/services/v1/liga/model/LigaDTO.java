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
    private Long regionId;
    private String regionName;
    private Long ligaUebergeordnetId;
    private String ligaUebergeordnetName;
    private Long ligaVerantwortlichId;
    private String ligaVerantwortlichMail;


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
                   String ligaUebergeordnetName, Long ligaVerantwortlichId, String ligaVerantwortlichMail) {
        this.id = id;
        this.name = name;
        this.regionId = regionId;
        this.regionName = regionName;
        this.ligaUebergeordnetId = ligaUebergeordnetId;
        this.ligaUebergeordnetName = ligaUebergeordnetName;
        this.ligaVerantwortlichId = ligaVerantwortlichId;
        this.ligaVerantwortlichMail = ligaVerantwortlichMail;
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
}
