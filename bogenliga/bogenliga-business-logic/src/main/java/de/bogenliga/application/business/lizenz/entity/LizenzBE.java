package de.bogenliga.application.business.lizenz.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

import java.sql.Date;

/**
 * I represent the lizenz business entity.
 * <p>
 * A dsbmitglied is a registered member of the DSB. The dsbmitglied is not necessarily a technical user of the system.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class LizenzBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private Long lizenzId;
    private String lizenznummer;
    private Long lizenzRegionId;
    private Long lizenzDsbMitgliedId;
    private String lizenztyp;
    private Long lizenzDisziplinId;

    public Long getLizenzId() {
        return lizenzId;
    }

    public void setLizenzId(Long lizenzId) {
        this.lizenzId = lizenzId;
    }

    public String getLizenznummer() {
        return lizenznummer;
    }

    public void setLizenznummer(String lizenznummer) {
        this.lizenznummer = lizenznummer;
    }

    public Long getLizenzRegionId() {
        return lizenzRegionId;
    }

    public void setLizenzRegionId(Long lizenzRegionId) {
        this.lizenzRegionId = lizenzRegionId;
    }

    public Long getLizenzDsbMitgliedId() {
        return lizenzDsbMitgliedId;
    }

    public void setLizenzDsbMitgliedId(Long lizenzDsbMitgliedId) {
        this.lizenzDsbMitgliedId = lizenzDsbMitgliedId;
    }

    public String getLizenztyp() {
        return lizenztyp;
    }

    public void setLizenztyp(String lizenztyp) {
        this.lizenztyp = lizenztyp;
    }

    public Long getLizenzDisziplinId() {
        return lizenzDisziplinId;
    }

    public void setLizenzDisziplinId(Long lizenzDisziplinId) {
        this.lizenzDisziplinId = lizenzDisziplinId;
    }



    public LizenzBE(){
        // empty constructor
    }


    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }




    @Override
    public String toString() {
        return "Lizenz{" +
                "lizenzId=" + lizenzId +
                ", lizenznummer='" + lizenznummer + '\'' +
                ", lizenzRegionId='" + lizenzRegionId + '\'' +
                ", lizenzDsbMitgliedId='" + lizenzDsbMitgliedId + '\'' +
                ", lizenztyp='" + lizenztyp + '\'' +
                ", lizenzDisziplinId='" + lizenzDisziplinId +
                '}';
    }

}
