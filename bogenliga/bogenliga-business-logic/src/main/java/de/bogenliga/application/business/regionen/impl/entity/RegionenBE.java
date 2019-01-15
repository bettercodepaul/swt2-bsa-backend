package de.bogenliga.application.business.regionen.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the Regionen Bussines Entity
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
public class RegionenBE extends CommonBusinessEntity implements BusinessEntity {
    private Long regionId;
    private String regionName;
    private String regionKuerzel;
    private String regionTyp;
    private Long regionUebergeordnet;


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


    public void setRegionUebergeordnet(Long regionUebergeordnet) {
        this.regionUebergeordnet = regionUebergeordnet;
    }

    @Override
    public String toString (){
        return "RegionBE {" +
                "regionId = '" + regionId + '\'' +
                ", regionName = '" + regionName + '\'' +
                ", regionKuerzel = '" + regionKuerzel + '\'' +
                ", regionTyp = '" + regionTyp + '\'' +
                ", regionUebergeordnet = '" + regionUebergeordnet + '\'' +
                '}';
    }
}
