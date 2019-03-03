package de.bogenliga.application.business.mannschaftsmitglied.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class MannschaftsmitgliedBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = 2616130134662239870L;
    private Long mannschaftId;
    private Long dsbMitgliedId;
    private boolean dsbMitgliedEingesetzt;
    private String dsbMitgliedVorname;
    private String dsbMitgliedNachname;


    /**
     * Constructor
     */
    public MannschaftsmitgliedBE() {
        // empty
    }


    public Long getMannschaftId() {
        return mannschaftId;
    }


    public void setMannschaftId(final long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }


    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }


    public void setDsbMitgliedId(final long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }


    public boolean isDsbMitgliedEingesetzt() {
        return dsbMitgliedEingesetzt;
    }


    public void setDsbMitgliedEingesetzt(final boolean dsbMitgliedEingesetzt) {
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


    public void setDsbMitgliedNachname(final String dsbMitgliedNachname) {
        this.dsbMitgliedNachname = dsbMitgliedNachname;
    }


    @Override
    public String toString() {
        return "MannschaftsmitgliedBE{" +
                "mannschaftId=" + mannschaftId +
                ", dsbMitgliedId=" + dsbMitgliedId +
                ", dsbMitgliedEingesetzt=" + dsbMitgliedEingesetzt +
                ", dsbMitgliedVorname=" + dsbMitgliedVorname +
                ", dsbMitgliedNachname=" + dsbMitgliedNachname +
                '}';
    }
}
