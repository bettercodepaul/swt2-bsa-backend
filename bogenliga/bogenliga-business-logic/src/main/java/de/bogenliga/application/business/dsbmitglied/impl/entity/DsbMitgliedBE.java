package de.bogenliga.application.business.dsbmitglied.impl.entity;

import java.sql.Date;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the dsbmitglied business entity.
 * <p>
 * A dsbmitglied is a registered member of the DSB. The dsbmitglied is not necessarily a technical user of the system.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class DsbMitgliedBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private Long dsbMitgliedId;
    private String dsbMitgliedVorname;
    private String dsbMitgliedNachname;
    private Date dsbMitgliedGeburtsdatum;
    private String dsbMitgliedNationalitaet;
    private String dsbMitgliedMitgliedsnummer;
    private Long dsbMitgliedVereinsId;
    private String dsbMitgliedVereinName;
    private Long dsbMitgliedUserId;
    private Date dsbMitgliedBeitrittsdatum;


    public DsbMitgliedBE(){
        // empty constructor
    }


    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }


    public void setDsbMitgliedId(final Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
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


    public Date getDsbMitgliedGeburtsdatum() {
        return dsbMitgliedGeburtsdatum;
    }


    public void setDsbMitgliedGeburtsdatum(final Date dsbMitgliedGeburtsdatum) {
        this.dsbMitgliedGeburtsdatum = dsbMitgliedGeburtsdatum;
    }

    public String getDsbMitgliedNationalitaet() {
        return dsbMitgliedNationalitaet;
    }


    public void setDsbMitgliedNationalitaet(final String dsbMitgliedNationalitaet) {
        this.dsbMitgliedNationalitaet = dsbMitgliedNationalitaet;
    }

    public String getDsbMitgliedMitgliedsnummer() {
        return dsbMitgliedMitgliedsnummer;
    }


    public void setDsbMitgliedMitgliedsnummer(final String dsbMitgliedMitgliedsnummer) {
        this.dsbMitgliedMitgliedsnummer = dsbMitgliedMitgliedsnummer;
    }


    public Long getDsbMitgliedVereinsId() {
        return dsbMitgliedVereinsId;
    }


    public void setDsbMitgliedVereinsId(final Long dsbMitgliedVereinsId) {
        this.dsbMitgliedVereinsId = dsbMitgliedVereinsId;
    }

    public String getDsbMitgliedVereinName() {
        return dsbMitgliedVereinName;
    }


    public void setDsbMitgliedVereinName(final String dsbMitgliedVereinName) {
        this.dsbMitgliedVereinName = dsbMitgliedVereinName;
    }

    public Long getDsbMitgliedUserId() {
        return dsbMitgliedUserId;
    }


    public void setDsbMitgliedUserId(final Long dsbMitgliedUserId) {
        this.dsbMitgliedUserId = dsbMitgliedUserId;
    }

    public Date getDsbMitgliedBeitrittsdatum() {
        return dsbMitgliedBeitrittsdatum;
    }


    public void setDsbMitgliedBeitrittsdatum(final Date dsbMitgliedBeitrittsdatum) {
        this.dsbMitgliedBeitrittsdatum = dsbMitgliedBeitrittsdatum;
    }

    @Override
    public String toString() {
        return "DsbMitgliedBE{" +
                "dsbMitgliedId=" + dsbMitgliedId +
                ", dsbMitgliedVorname='" + dsbMitgliedVorname + '\'' +
                ", dsbMitgliedNachname='" + dsbMitgliedNachname + '\'' +
                ", dsbMitgliedGeburtsdatum='" + dsbMitgliedGeburtsdatum + '\'' +
                ", dsbMitgliedNationalitaet='" + dsbMitgliedNationalitaet + '\'' +
                ", dsbMitgliedMitgliedsnummer='" + dsbMitgliedMitgliedsnummer + '\'' +
                ", dsbMitgliedVereinsId=" + dsbMitgliedVereinsId +
                ", dsbMitgliedVereinName=" + dsbMitgliedVereinName +
                ", dsbMitgliedUserId=" + dsbMitgliedUserId +
                ", dsbMitgliedBeitrittsdatum=" + dsbMitgliedBeitrittsdatum +
                '}';
    }
}
