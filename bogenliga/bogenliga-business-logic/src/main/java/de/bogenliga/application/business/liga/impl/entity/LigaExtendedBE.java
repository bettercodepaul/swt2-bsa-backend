package de.bogenliga.application.business.liga.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;

/**
 * Extends LigaBE with additional fields for the extended business logic.
 */
public class LigaExtendedBE extends LigaBE implements BusinessEntity {

    private static final long serialVersionUID = 1L;

    private String disziplinName;
    private String regionName;
    private String uebergeordneteLigaName;
    private String verantwortlicherName;

    public LigaExtendedBE() {
        // empty constructor
    }

    /**
     * Constructor with mandatory parameters
     *
     * @param ligaId
     * @param ligaName
     * @param ligaDisziplinId
     * @param ligaRegionId
     * @param ligaUebergeordnetId
     * @param ligaVerantwortlichId
     * @param ligaDetail
     * @param ligaFileBase64
     * @param ligaFileName
     * @param ligaFileType
     * @param disziplinName
     * @param regionName
     * @param uebergeordneteLigaName
     * @param verantwortlicherName
     */
    public LigaExtendedBE(final Long ligaId, final String ligaName, final Long ligaDisziplinId, final Long ligaRegionId,
                          final Long ligaUebergeordnetId, final Long ligaVerantwortlichId, final String ligaDetail,
                          final String ligaFileBase64, final String ligaFileName, final String ligaFileType,
                          final String disziplinName, final String regionName, final String uebergeordneteLigaName,
                          final String verantwortlicherName) {
        super.setLigaId(ligaId);
        super.setLigaName(ligaName);
        super.setLigaDisziplinId(ligaDisziplinId);
        super.setLigaRegionId(ligaRegionId);
        super.setLigaUebergeordnetId(ligaUebergeordnetId);
        super.setLigaVerantwortlichId(ligaVerantwortlichId);
        super.setLigaDetail(ligaDetail);
        super.setLigaFileBase64(ligaFileBase64);
        super.setLigaFileName(ligaFileName);
        super.setLigaFileType(ligaFileType);

        this.disziplinName = disziplinName;
        this.regionName = regionName;
        this.uebergeordneteLigaName = uebergeordneteLigaName;
        this.verantwortlicherName = verantwortlicherName;
    }

    public String getDisziplinName() {
        return disziplinName;
    }

    public void setDisziplinName(final String disziplinName) {
        this.disziplinName = disziplinName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(final String regionName) {
        this.regionName = regionName;
    }

    public String getUebergeordneteLigaName() {
        return uebergeordneteLigaName;
    }

    public void setUebergeordneteLigaName(final String uebergeordneteLigaName) {
        this.uebergeordneteLigaName = uebergeordneteLigaName;
    }

    public String getVerantwortlicherName() {
        return verantwortlicherName;
    }

    public void setVerantwortlicherName(final String verantwortlicherName) {
        this.verantwortlicherName = verantwortlicherName;
    }

    @Override
    public String toString() {
        return "LigaExtendedBE{" +
                "ligaId=" + super.getLigaId() +
                ", ligaName='" + super.getLigaName() + '\'' +
                ", disziplinName='" + disziplinName + '\'' +
                ", regionName='" + regionName + '\'' +
                ", uebergeordneteLigaName='" + uebergeordneteLigaName + '\'' +
                ", verantwortlicherName='" + verantwortlicherName + '\'' +
                '}';
    }
}



