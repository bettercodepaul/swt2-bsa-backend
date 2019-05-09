package de.bogenliga.application.business.regionen.api.types;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 *Contains the values of the class Business entities
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
public class RegionenDO extends CommonDataObject implements DataObject {
    private Long id;
    private String regionName;
    private String regionKuerzel;
    private String regionType;
    private Long regionUebergeordnet;


    /**
     * Constructor with mandatory parameters
     */

    public RegionenDO(final Long id, final String regionName, final String regionKuerzel,
                      final String regionType, final Long regionUebergeordnet, final OffsetDateTime createdAtUtc,
                      final Long createdByUserId, final OffsetDateTime lastModifiedUtc, final Long lastModifiedByUserId,
                      final Long version){

        this.id = id;
        this.regionName = regionName;
        this.regionKuerzel = regionKuerzel;
        this.regionType = regionType;
        this.regionUebergeordnet = regionUebergeordnet;

        //set param from CommonDataObject

        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }

    /**
     * Constructor with mandatory parameters
     */

    public RegionenDO(final Long id, final String regionName, final String regionKuerzel,
                      final String regionType, final Long regionUebergeordnet, final OffsetDateTime createdAtUtc,
                      final Long createdByUserId, final Long version){

        this.id = id;
        this.regionName = regionName;
        this.regionKuerzel = regionKuerzel;
        this.regionType = regionType;
        this.regionUebergeordnet = regionUebergeordnet;

        //set param from CommonDataObject

        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }


    public RegionenDO(final Long id, final String regionName, final String regionKuerzel,
                      final String regionType, final Long regionUebergeordnet){


        this.id = id;
        this.regionName = regionName;
        this.regionKuerzel = regionKuerzel;
        this.regionType = regionType;
        this.regionUebergeordnet = regionUebergeordnet;

    }

    /**
     * Constuctor with id to delete existing region entries
     *
     * @param id Id of existing Verein entry
     */
    public RegionenDO(final Long id) {
        this.id = id;
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


    public String getRegionType() {
        return regionType;
    }


    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }


    public Long getRegionUebergeordnet() {
        return regionUebergeordnet;
    }


    public void setRegionUebergeordnet(Long regionUebergeordnet) {
        this.regionUebergeordnet = regionUebergeordnet;
    }
}
