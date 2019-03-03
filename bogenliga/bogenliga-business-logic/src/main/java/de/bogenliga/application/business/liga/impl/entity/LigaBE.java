package de.bogenliga.application.business.liga.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the liga business entity.
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class LigaBE extends CommonBusinessEntity implements BusinessEntity {

    private Long ligaId;
    private String ligaName;
    private Long ligaRegionId;
    private Long ligaUebergeordnetId;
    private Long ligaVerantwortlichId;

    public LigaBE(){
        // empty constructor
    }


    public Long getLigaId() {
        return ligaId;
    }


    public void setLigaId(Long ligaId) {
        this.ligaId = ligaId;
    }


    public String getLigaName() {
        return ligaName;
    }


    public void setLigaName(String ligaName) {
        this.ligaName = ligaName;
    }


    public Long getLigaRegionId() {
        return ligaRegionId;
    }


    public void setLigaRegionId(Long ligaRegionId) {
        this.ligaRegionId = ligaRegionId;
    }


    public Long getLigaUebergeordnetId() {
        return ligaUebergeordnetId;
    }


    public void setLigaUebergeordnetId(Long ligaUebergeordnetId) {
        this.ligaUebergeordnetId = ligaUebergeordnetId;
    }


    public Long getLigaVerantwortlichId() {
        return ligaVerantwortlichId;
    }


    public void setLigaVerantwortlichId(Long ligaVerantwortlichId) {
        this.ligaVerantwortlichId = ligaVerantwortlichId;
    }


    @Override
    public String toString(){
        return "LigaBE{" +
                "ligaId=" + ligaId +
                ", ligaName='" + ligaName + '\'' +
                ", ligaRegionId='" + ligaRegionId + '\'' +
                ", ligaUebergeordnetId='" + ligaUebergeordnetId + '\'' +
                ", ligaVerantwortlichId=" + ligaVerantwortlichId + '\'' +
                ", ligaUserId=" + ligaId+
                '}';
    }



}
