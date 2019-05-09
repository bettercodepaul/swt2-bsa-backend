package de.bogenliga.application.business.mannschaftsmitglied.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;


public class MannschaftsmitgliedBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 2616130134662239870L;

    private Long id;
    private Long mannschaftId;
    private Long dsbMitgliedId;
    private Integer dsbMitgliedEingesetzt;
    private String dsbMitgliedVorname;
    private String dsbMitgliedNachname;


    /**
     * Constructor
     */
    public MannschaftsmitgliedBE() {
    }


    public Long getMannschaftId() {
        return mannschaftId;
    }


    public void setMannschaftId(final Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }


    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }


    public void setDsbMitgliedId(final Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }


    public Integer getDsbMitgliedEingesetzt() {
        return dsbMitgliedEingesetzt;
    }


    public void setDsbMitgliedEingesetzt(final Integer dsbMitgliedEingesetzt) {
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
    }


    public String getDsbMitgliedVorname() {
        return dsbMitgliedVorname;
    }


    public void setDsbMitgliedVorname(final String dsbMitgliedVorname) {
        this.dsbMitgliedVorname = dsbMitgliedVorname;
    }


    public String getDsbMitgliedNachname() {
        return dsbMitgliedNachname;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public void setDsbMitgliedNachname(final String dsbMitgliedNachname) {
        this.dsbMitgliedNachname = dsbMitgliedNachname;
    }


    @Override
    public String toString() {
        return "MannschaftsmitgliedBE{" +
                "id=" + id +
                "mannschaftId=" + mannschaftId +
                ", dsbMitgliedId=" + dsbMitgliedId +
                ", dsbMitgliedEingesetzt=" + dsbMitgliedEingesetzt +
                ", dsbMitgliedVorname=" + dsbMitgliedVorname +
                ", dsbMitgliedNachname=" + dsbMitgliedNachname +
                '}';
    }
}
