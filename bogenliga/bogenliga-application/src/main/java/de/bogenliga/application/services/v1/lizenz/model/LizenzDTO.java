package de.bogenliga.application.services.v1.lizenz.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I'm the data transfer object of the lizenz.
 * <p>
 * I define the payload for the external REST interface of the lizenz business entity.
 *
 * @author Manuel Baisch
 */
public class LizenzDTO implements DataTransferObject {


    private Long lizenzId;
    private String lizenznummer;
    private Long lizenzRegionId;
    private Long lizenzDsbMitgliedId;
    private String lizenztyp;
    private Long lizenzDisziplinId;


    /**
     * @param lizenzId
     * @param lizenznummer
     * @param lizenzRegionId
     * @param lizenzDsbMitgliedId
     * @param lizenztyp
     * @param lizenzDisziplinId
     */
    public LizenzDTO(Long lizenzId, String lizenznummer, Long lizenzRegionId, Long lizenzDsbMitgliedId,
                     String lizenztyp, Long lizenzDisziplinId) {
        this.lizenzId = lizenzId;
        this.lizenznummer = lizenznummer;
        this.lizenzRegionId = lizenzRegionId;
        this.lizenzDsbMitgliedId = lizenzDsbMitgliedId;
        this.lizenztyp = lizenztyp;
        this.lizenzDisziplinId = lizenzDisziplinId;
    }


    public LizenzDTO() {
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
