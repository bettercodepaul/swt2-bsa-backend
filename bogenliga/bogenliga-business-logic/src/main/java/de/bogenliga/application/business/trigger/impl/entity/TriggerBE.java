package de.bogenliga.application.business.trigger.impl.entity;

import java.time.OffsetDateTime;
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
    private Long operation;
    private Long status;
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


    public Long getOperation() {
        return operation;
    }


    public void setOperation(Long operation) {
        this.operation = operation;
    }


    public Long getStatus() {
        return status;
    }


    public void setStatus(Long status) {
        this.status = status;
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
                ", operation = '" + operation + '\'' +
                ", status = '" + status + '\'' +
                ", nachricht = '" + nachricht + '\'' +
                ", createdAtUtc = '" + createdAtUtc + '\'' +
                '}';
    }
}
