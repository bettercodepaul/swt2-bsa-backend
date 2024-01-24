package de.bogenliga.application.business.trigger.impl.entity;

import java.sql.Timestamp;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * Represents Trigger Business Entity
 *
 * @author Maximilian Fronmueller
 */
public class TriggerBE extends CommonBusinessEntity implements BusinessEntity {
    private Long id;
    private String kategorie;
    private Long altsystemId;
    private TriggerChangeOperation changeOperation;
    private TriggerChangeStatus changeStatus;
    private String nachricht;
    private Timestamp createdAtUtc;
    private Timestamp runAtUtc;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
    public String getKategorie() {
        return kategorie;
    }


    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }


    public Long getAltsystemId() {
        return altsystemId;
    }


    public void setAltsystemId(Long altsystemId) {
        this.altsystemId = altsystemId;
    }


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


    public String getNachricht() {
        return nachricht;
    }


    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }
    public Timestamp getCreatedAtUtc(){
        return createdAtUtc;
    }
    public void setCreatedAtUtc(Timestamp createdAtUtc) {
        this.createdAtUtc = createdAtUtc;
    }
    public Timestamp getRunAtUtc() {
        return runAtUtc;
    }


    public void setRunAtUtc(Timestamp runAtUtc) {
        this.runAtUtc = runAtUtc;
    }


    @Override
    public String toString() {
        return "TriggerBE{" +
                "id=" + id +
                ", kategorie='" + kategorie + '\'' +
                ", altsystemId=" + altsystemId +
                ", changeOperation=" + changeOperation +
                ", changeStatus=" + changeStatus +
                ", nachricht='" + nachricht + '\'' +
                ", createdAtUtc=" + createdAtUtc +
                ", runAtUtc=" + runAtUtc +
                '}';
    }
}
