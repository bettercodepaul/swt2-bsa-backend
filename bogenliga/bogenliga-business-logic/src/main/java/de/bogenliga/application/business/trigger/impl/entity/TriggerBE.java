package de.bogenliga.application.business.trigger.impl.entity;

import java.time.OffsetDateTime;
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
    private Long version;
    private String kategorie;
    private Long altsystemId;
    private MigrationChangeType changeOperation;
    private MigrationChangeState changeStatus;
    private String nachricht;
    private OffsetDateTime createdAtUtc;


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
    @Override
    public String toString (){
        return "TriggerBE {" +
                "id = '" + id + '\'' +
                ", version = '" + version + '\'' +
                ", kategorie = '" + kategorie + '\'' +
                ", altsystemId = '" + altsystemId + '\'' +
                ", operation = '" + changeOperation + '\'' +
                ", status = '" + changeStatus + '\'' +
                ", nachricht = '" + nachricht + '\'' +
                ", createdAtUtc = '" + createdAtUtc + '\'' +
                '}';
    }
}
