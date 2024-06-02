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
public class RawTriggerBE extends CommonBusinessEntity implements BusinessEntity {
    private Long id;
    private String kategorie;
    private Long altsystemId;
    private Long changeOperationId;
    private Long changeStatusId;
    private String nachricht;
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


    public Long getChangeOperationId() {
        return changeOperationId;
    }


    public void setChangeOperationId(Long changeOperationId) {
        this.changeOperationId = changeOperationId;
    }


    public Long getChangeStatusId() {
        return changeStatusId;
    }


    public void setChangeStatusId(Long changeStatusId) {
        this.changeStatusId = changeStatusId;
    }


    public String getNachricht() {
        return nachricht;
    }


    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
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
                ", changeOperationId=" + changeOperationId +
                ", changeStatusId=" + changeStatusId +
                ", nachricht='" + nachricht + '\'' +
                ", runAtUtc=" + runAtUtc +
                '}';
    }
}
