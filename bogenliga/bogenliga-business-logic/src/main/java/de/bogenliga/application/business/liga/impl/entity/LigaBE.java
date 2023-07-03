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
    private String ligaDetailFileBase64;
    private String ligaDetailFileName;
    private String ligaDetailFileType;

    public LigaBE(){
        // empty constructor
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
