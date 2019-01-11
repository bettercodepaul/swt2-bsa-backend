package de.bogenliga.application.business.mannschaftsmitglied.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class MannschaftsmitgliedBE extends CommonBusinessEntity implements BusinessEntity {

    private long mannschaftId;
    private long dsbMitgliedId;
    private boolean dsbMitgliedEingesetzt;

    public MannschaftsmitgliedBE(){

    }

    public long getMannschaftId() {
        return mannschaftId;
    }

    public void setMannschaftId(long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }

    public long getDsbMitgliedId() {
        return dsbMitgliedId;
    }

    public void setDsbMitgliedId(long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }

    public boolean isDsbMitgliedEingesetzt() {
        return dsbMitgliedEingesetzt;
    }

    public void setDsbMitgliedEingesetzt(boolean dsbMitgliedEingesetzt) {
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
    }

    @Override
    public String toString() {
        return "MannschaftsmitgliedBE{" +
                "mannschaftId=" + mannschaftId +
                ", dsbMitgliedId=" + dsbMitgliedId +
                ", dsbMitgliedEingesetzt=" + dsbMitgliedEingesetzt +
                '}';
    }
}
