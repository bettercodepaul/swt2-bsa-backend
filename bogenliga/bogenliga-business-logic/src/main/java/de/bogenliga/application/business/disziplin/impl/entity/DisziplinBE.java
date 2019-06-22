package de.bogenliga.application.business.disziplin.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Marcel Neumann
 * @author Robin Mueller
 */
public class DisziplinBE extends CommonBusinessEntity implements BusinessEntity {
    private Long disziplinID;
    private String disziplinName;

    @Override
    public String toString() {
        return "MatchBE{" +
                "disziplinID=" + disziplinID +
                "disziplinName=" + disziplinName +
                '}';
    }

    public Long getDisziplinID() {
        return disziplinID;
    }


    public void setDisziplinID(Long disziplinID) {
        this.disziplinID = disziplinID;
    }


    public String getDisziplinName() {
        return disziplinName;
    }


    public void setDisziplinName(String disziplinName) {
        this.disziplinName = disziplinName;
    }
}