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
    private Long ligaDisziplinId;
    private Long ligaRegionId;
    private Long ligaUebergeordnetId;
    private Long ligaVerantwortlichId;
    private String ligaDetail;
    private String ligaFileBase64;
    private String ligaFileName;
    private String ligaFileType;

    public LigaBE(){
        // empty constructor
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


    public Long getLigaId() {
        return ligaId;
    }


    public void setLigaId(Long ligaId) {
        this.ligaId = ligaId;
    }

    public Long getLigaDisziplinId() { return ligaDisziplinId; }
    public void setLigaDisziplinId(Long ligaDisziplinId) { this.ligaDisziplinId = ligaDisziplinId; }


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
                ", ligaDisziplin'" + ligaDisziplinId + '\'' +
                ", ligaName='" + ligaName + '\'' +
                ", ligaRegionId='" + ligaRegionId + '\'' +
                ", ligaUebergeordnetId='" + ligaUebergeordnetId + '\'' +
                ", ligaVerantwortlichId=" + ligaVerantwortlichId + '\'' +
                ", ligaUserId=" + ligaId+
                '}';
    }



}
