package de.bogenliga.application.business.mannschaftsmitglied.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;


public class MannschaftsmitgliedBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 2616130134662239870L;

    protected Long id;
    protected Long mannschaftId;
    protected Long dsbMitgliedId;
    protected Integer dsbMitgliedEingesetzt;
    protected Long rueckennummer;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRueckennummer() { return rueckennummer; }

    public void setRueckennummer(Long rueckennummer) { this.rueckennummer = rueckennummer; }

    @Override
    public String toString() {
        return "MannschaftsmitgliedBE{" +
                "id=" + id +
                "mannschaftId=" + mannschaftId +
                ", dsbMitgliedId=" + dsbMitgliedId +
                ", dsbMitgliedEingesetzt=" + dsbMitgliedEingesetzt +
                ", rueckennummer=" + rueckennummer +
                '}';
    }
}
