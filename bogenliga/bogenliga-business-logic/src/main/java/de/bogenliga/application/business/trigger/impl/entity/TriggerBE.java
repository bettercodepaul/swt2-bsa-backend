package de.bogenliga.application.business.trigger.impl.entity;

import java.sql.Timestamp;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * Represents Trigger Business Entity
 *
 * @author Maximilian Fronmueller
 */
public class TriggerBE extends RawTriggerBE implements BusinessEntity{
    private TriggerChangeOperation changeOperation;
    private TriggerChangeStatus changeStatus;


    public TriggerChangeOperation getChangeOperation() {
        return changeOperation;
    }


    public void setChangeOperation(TriggerChangeOperation changeOperation) {
        this.changeOperation = changeOperation;
    }


    public TriggerChangeStatus getChangeStatus() {
        return changeStatus;
    }


    public void setChangeStatus(TriggerChangeStatus changeStatus) {
        this.changeStatus = changeStatus;
    }


    @Override
    public String toString() {
        return "TriggerBE{" +
                "id=" + getId() +
                ", kategorie='" + getKategorie() + '\'' +
                ", altsystemId=" + getAltsystemId() +
                ", changeOperation=" + changeOperation +
                ", changeStatus=" + changeStatus +
                ", nachricht='" + getNachricht() + '\'' +
                ", runAtUtc=" + getRunAtUtc() +
                '}';
    }
}
