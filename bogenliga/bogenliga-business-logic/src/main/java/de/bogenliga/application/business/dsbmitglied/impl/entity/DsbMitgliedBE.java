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
public class DsbMitgliedBE extends DsbMitgliedWithoutVereinsnameBE implements BusinessEntity {
    private String dsbMitgliedVereinName;

    public String getDsbMitgliedVereinName() {
        return dsbMitgliedVereinName;
    }


    public void setDsbMitgliedVereinName(final String dsbMitgliedVereinName) {
        this.dsbMitgliedVereinName = dsbMitgliedVereinName;
    }

    @Override
    public String toString() {
        return "DsbMitgliedBE{" +
                "dsbMitgliedId=" + getDsbMitgliedId() +
                ", dsbMitgliedVorname='" + getDsbMitgliedVorname() + '\'' +
                ", dsbMitgliedNachname='" + getDsbMitgliedNachname() + '\'' +
                ", dsbMitgliedGeburtsdatum='" + getDsbMitgliedGeburtsdatum() + '\'' +
                ", dsbMitgliedNationalitaet='" + getDsbMitgliedNationalitaet() + '\'' +
                ", dsbMitgliedMitgliedsnummer='" + getDsbMitgliedMitgliedsnummer() + '\'' +
                ", dsbMitgliedVereinsId=" + getDsbMitgliedVereinsId() +
                ", dsbMitgliedVereinName=" + getDsbMitgliedVereinName() +
                ", dsbMitgliedUserId=" + getDsbMitgliedUserId() +
                ", dsbMitgliedBeitrittsdatum=" + getDsbMitgliedBeitrittsdatum() +
                '}';
    }
}
