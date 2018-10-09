package de.bogenliga.application.business.dsbmitglied.impl.entity;

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
    private long dsbMitgliedId;
    private String dsbMitgliedVorname;
    private String dsbMitgliedNachname;
    private String dsbMitgliedGeburtsdatum;
    private String dsbMitgliedNationalitaet;
    private String dsbMitgliedMitgliedsnummer;
    private long dsbMitgliedVereinsId;
    private long dsbMitgliedUserId;


    public DsbMitgliedBE(){
        // empty constructor
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getDsbMitgliedId() {
        return dsbMitgliedId;
    }

    public void setDsbMitgliedId(long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }

    public String getDsbMitgliedVorname() {
        return dsbMitgliedVorname;
    }

    public void setDsbMitgliedVorname(String dsbMitgliedVorname) {
        this.dsbMitgliedVorname = dsbMitgliedVorname;
    }

    public String getDsbMitgliedNachname() {
        return dsbMitgliedNachname;
    }

    public void setDsbMitgliedNachname(String dsbMitgliedNachname) {
        this.dsbMitgliedNachname = dsbMitgliedNachname;
    }

    public String getDsbMitgliedGeburtsdatum() {
        return dsbMitgliedGeburtsdatum;
    }

    public void setDsbMitgliedGeburtsdatum(String dsbMitgliedGeburtsdatum) {
        this.dsbMitgliedGeburtsdatum = dsbMitgliedGeburtsdatum;
    }

    public String getDsbMitgliedNationalitaet() {
        return dsbMitgliedNationalitaet;
    }

    public void setDsbMitgliedNationalitaet(String dsbMitgliedNationalitaet) {
        this.dsbMitgliedNationalitaet = dsbMitgliedNationalitaet;
    }

    public String getDsbMitgliedMitgliedsnummer() {
        return dsbMitgliedMitgliedsnummer;
    }

    public void setDsbMitgliedMitgliedsnummer(String dsbMitgliedMitgliedsnummer) {
        this.dsbMitgliedMitgliedsnummer = dsbMitgliedMitgliedsnummer;
    }

    public long getDsbMitgliedVereinsId() {
        return dsbMitgliedVereinsId;
    }

    public void setDsbMitgliedVereinsId(long dsbMitgliedVereinsId) {
        this.dsbMitgliedVereinsId = dsbMitgliedVereinsId;
    }

    public long getDsbMitgliedUserId() {
        return dsbMitgliedUserId;
    }

    public void setDsbMitgliedUserId(long dsbMitgliedUserId) {
        this.dsbMitgliedUserId = dsbMitgliedUserId;
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
                ", dsbMitgliedUserId=" + dsbMitgliedUserId +
                '}';
    }
}
