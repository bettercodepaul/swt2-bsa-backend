package de.bogenliga.application.business.lizenz.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;


public class LizenzDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 123567;


    /**
     * business parameter
     */
    private Long lizenzId;
    private String lizenznummer;
    private Long lizenzRegionId;
    private Long lizenzDsbMitgliedId;
    private String lizenztyp;
    private Long lizenzDisziplinId;


    public LizenzDO(Long lizenzId, String lizenznummer, Long lizenzRegionId, Long lizenzDsbMitgliedId,
                    String lizenztyp, Long lizenzDisziplinId) {
        this.lizenzId = lizenzId;
        this.lizenznummer = lizenznummer;
        this.lizenzRegionId = lizenzRegionId;
        this.lizenzDsbMitgliedId = lizenzDsbMitgliedId;
        this.lizenztyp = lizenztyp;
        this.lizenzDisziplinId = lizenzDisziplinId;
    }


    public LizenzDO() {
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


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


}
