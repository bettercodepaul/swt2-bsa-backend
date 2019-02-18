package de.bogenliga.application.business.dsbmannschaft.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

public class DsbMannschaftBE extends CommonBusinessEntity implements BusinessEntity {

    private long id;
    private long vereinId;
    private long nummer;
    private long veranstaltungId;
    private long benutzerId;

    public DsbMannschaftBE(){/*empty constructor*/}


    public long getId() {
        return id;
    }

    public long getVereinId() {
        return vereinId;
    }

    public long getNummer() {
        return nummer;
    }

    public long getVeranstaltungId() {
        return veranstaltungId;
    }

    public long getBenutzerId() {
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
