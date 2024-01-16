package de.bogenliga.application.business.trigger.impl.entity;

import java.sql.Timestamp;
import de.bogenliga.application.business.trigger.api.types.MigrationChangeState;
import de.bogenliga.application.business.trigger.api.types.MigrationChangeType;
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
    private MigrationChangeType changeOperation;
    private MigrationChangeState changeStatus;
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


    public MigrationChangeType getChangeOperation() {
        return changeOperation;
    }


    public void setChangeOperation(MigrationChangeType changeOperation) {
        this.changeOperation = changeOperation;
    }


    public MigrationChangeState getChangeStatus() {
        return changeStatus;
    }


    public void setChangeStatus(MigrationChangeState changeStatus) {
        this.changeStatus = changeStatus;
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
    public String toString (){
        return "TriggerBE {" +
                "id = '" + id + '\'' +
                ", kategorie = '" + kategorie + '\'' +
                ", altsystemId = '" + altsystemId + '\'' +
                ", operation = '" + changeOperation + '\'' +
                ", status = '" + changeStatus + '\'' +
                ", nachricht = '" + nachricht + '\'' +
                ", runAtUtc = '" + runAtUtc + '\'' +
                '}';
    }
}
