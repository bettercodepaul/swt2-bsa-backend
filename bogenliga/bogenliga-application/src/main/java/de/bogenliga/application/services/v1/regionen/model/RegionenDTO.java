package de.bogenliga.application.services.v1.regionen.model;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;

/**
 * I map the {@link RegionenDO} and {@link RegionenDTO} objects
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class RegionenDTO implements DataTransferObject {
    private Long id;
    private String regionName;
    private String regionKuerzel;
    private String regionTyp;
    private Long regionUebergeordnet;
    private String regionUebergeordnetAsName;
    private OffsetDateTime createdAtUtc;
    private Long createdByUserId;
    private Long version;



    /**
     * Constructors
     */
    public RegionenDTO() {
        //empty
    }


    /**
     *
     * @param id
     * @param regionName
     * @param regionKuerzel
     * @param regionTyp
     * @param regionUebergeordnet
     * @param regionUebergeordnetAsName
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */
    public RegionenDTO(Long id, String regionName, String regionKuerzel, String regionTyp, Long regionUebergeordnet,
                       String regionUebergeordnetAsName, OffsetDateTime createdAtUtc, Long createdByUserId, Long version) {
        this.id = id;
        this.regionName = regionName;
        this.regionKuerzel = regionKuerzel;
        this.regionTyp = regionTyp;
        this.regionUebergeordnet = regionUebergeordnet;
        this.regionUebergeordnetAsName = regionUebergeordnetAsName;
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


    public String getRegionName() {
        return regionName;
    }


    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }


    public String getRegionKuerzel() {
        return regionKuerzel;
    }


    public void setRegionKuerzel(String regionKuerzel) {
        this.regionKuerzel = regionKuerzel;
    }


    public String getRegionTyp() {
        return regionTyp;
    }


    public void setRegionTyp(String regionTyp) {
        this.regionTyp = regionTyp;
    }


    public Long getRegionUebergeordnet() {
        return regionUebergeordnet;
    }


    public void setRegionUebergeordnet(Long regionUebergeordnet) { this.regionUebergeordnet = regionUebergeordnet;}


    public String getRegionUebergeordnetAsName() { return regionUebergeordnetAsName; }


    public void setRegionUebergeordnetAsName(String regionUebergeordnetAsName) {
        this.regionUebergeordnetAsName = regionUebergeordnetAsName;
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