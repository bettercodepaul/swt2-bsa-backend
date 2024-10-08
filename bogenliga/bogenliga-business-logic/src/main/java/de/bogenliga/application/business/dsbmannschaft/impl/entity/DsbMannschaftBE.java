package de.bogenliga.application.business.dsbmannschaft.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

public class DsbMannschaftBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = -6431886856322437597L;
    private Long id;
    private Long vereinId;
    private Long nummer;
    private Long veranstaltungId;
    private Long benutzerId;
    private Long sortierung;
    private Long sportjahr;

    public DsbMannschaftBE() {/*empty constructor*/}

    public DsbMannschaftBE(Long id, Long vereinId, Long nummer, Long veranstaltungId, Long benutzerId, Long sortierung, Long sportjahr) {
        this.id = id;
        this.vereinId = vereinId;
        this.nummer = nummer;
        this.veranstaltungId = veranstaltungId;
        this.benutzerId = benutzerId;
        this.sortierung = sortierung;
        this.sportjahr = sportjahr;
    }

    public Long getId() {
        return id;
    }


    public void setId(final Long id) {
        this.id = id;
    }


    public Long getVereinId() {
        return vereinId;
    }


    public void setVereinId(final Long vereinId) {
        this.vereinId = vereinId;
    }


    public Long getNummer() {
        return nummer;
    }


    public void setNummer(final Long nummer) {
        this.nummer = nummer;
    }


    public Long getVeranstaltungId() {
        return veranstaltungId;
    }


    public void setVeranstaltungId(final Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }


    public Long getBenutzerId() {
        return benutzerId;
    }


    public void setBenutzerId(final Long benutzerId) {
        this.benutzerId = benutzerId;
    }


    public Long getSortierung() {
        return sortierung;
    }


    public void setSortierung(final Long sortierung) {
        this.sortierung = sortierung;
    }

    public Long getSportjahr() {
        return sportjahr;
    }


    public void setSportjahr(final Long sportjahr) {
        this.sportjahr = sportjahr;
    }




    @Override
    public String toString() {
        return "DsbMannschaftBE{" +
                "mannschaftId=" + id +
                ", mannschaftVereinId='" + vereinId + '\'' +
                ", mannschaftNummer='" + nummer + '\'' +
                ", mannschaftVeranstaltungId='" + veranstaltungId + '\'' +
                ", mannschaftBenutzerId='" + benutzerId + '\'' +
                ", mannschaftSortierung='" + sortierung + '\'' +
                ", mannschaftSportjahr='" + sportjahr + '\'' +
                '}';
    }
}
