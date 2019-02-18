package de.bogenliga.application.business.dsbmannschaft.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

public class DsbMannschaftBE extends CommonBusinessEntity implements BusinessEntity {

    private Long id;
    private Long vereinId;
    private Long nummer;
    private Long veranstaltungId;
    private Long benutzerId;

    public DsbMannschaftBE(){/*empty constructor*/}


    public Long getId() {
        return id;
    }

    public Long getVereinId() {
        return vereinId;
    }

    public Long getNummer() {
        return nummer;
    }

    public Long getVeranstaltungId() {
        return veranstaltungId;
    }

    public Long getBenutzerId() {
        return benutzerId;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setVereinId(final long vereinId) {
        this.vereinId = vereinId;
    }

    public void setNummer(final long nummer) {
        this.nummer = nummer;
    }

    public void setVeranstaltungId(final long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public void setBenutzerId(final long benutzerId) {
        this.benutzerId = benutzerId;
    }

    @Override
    public String toString() {
        return "DsbMannschaftBE{" +
                "mannschaftId=" + id +
                ", mannschaftVereinId='" + vereinId + '\'' +
                ", mannschaftNummer='" + nummer + '\'' +
                ", mannschaftVeranstaltungId='" + veranstaltungId + '\'' +
                ", mannschaftBenutzerId='" + benutzerId + '\'' +

                '}';
    }
}
