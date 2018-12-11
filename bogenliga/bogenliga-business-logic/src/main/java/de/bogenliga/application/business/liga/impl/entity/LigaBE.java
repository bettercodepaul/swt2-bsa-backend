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
    private String ligaRegionName;
    private Long ligaUebergeordnetId;
    private String ligaUebergeordnetName;
    private Long ligaVerantwortlichId;
    private String ligaVerantwortlichMail;
    private Long ligaUserId;

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


    public String getLigaRegionName() {
        return ligaRegionName;
    }


    public void setLigaRegionName(String ligaRegionName) {
        this.ligaRegionName = ligaRegionName;
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


    public Long getLigaUserId() {
        return ligaUserId;
    }


    public void setLigaUserId(Long ligaUserId) {
        this.ligaUserId = ligaUserId;
    }

    @Override
    public String toString(){
        return "LigaBE{" +
                "ligaId=" + ligaId +
                ", ligaName='" + ligaName + '\'' +
                ", ligaRegionId='" + ligaRegionId + '\'' +
                ", ligaRegionName='" + ligaRegionName + '\'' +
                ", ligaUebergeordnetId='" + ligaUebergeordnetId + '\'' +
                ", ligaUebergeordnetName='" + ligaUebergeordnetName + '\'' +
                ", ligaVerantwortlichId=" + ligaVerantwortlichId + '\'' +
                ", ligaVerantwortlichMail=" + ligaVerantwortlichMail + '\'' +
                ", ligaUserId=" + ligaId+
                '}';
    }



}
