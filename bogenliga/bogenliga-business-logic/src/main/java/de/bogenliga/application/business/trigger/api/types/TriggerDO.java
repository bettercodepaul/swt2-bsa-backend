package de.bogenliga.application.business.trigger.api.types;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerDO extends CommonDataObject implements DataObject {
    private Long id;
    private String kategorie;
    private Long altsystemId;
    private TriggerChangeOperation operation;
    private TriggerChangeStatus status;
    private String nachricht;
    private OffsetDateTime createdAt;
    private OffsetDateTime runAtUtc;
    private OffsetDateTime updatedAtUtc;




    public TriggerDO(final Long id, final String kategorie, final Long altsystemId, final TriggerChangeOperation operation, final TriggerChangeStatus status, final String nachricht, final OffsetDateTime createdAt, final OffsetDateTime runAtUtc, OffsetDateTime updatedAtUtc){
        this.id = id;
        this.kategorie = kategorie;
        this.altsystemId = altsystemId;
        this.operation = operation;
        this.status = status;
        this.nachricht = nachricht;
        this.createdAt = createdAt;
        this.runAtUtc = runAtUtc;
        this.updatedAtUtc = updatedAtUtc;
    }


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


    public TriggerChangeOperation getOperation() {
        return operation;
    }


    public void setOperation(TriggerChangeOperation operation) {
        this.operation = operation;
    }


    public TriggerChangeStatus getStatus() {
        return status;
    }


    public void setStatus(TriggerChangeStatus status) {
        this.status = status;
    }


    public String getNachricht() {
        return nachricht;
    }


    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }
    public OffsetDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public OffsetDateTime getRunAtUtc() {
        return runAtUtc;
    }


    public void setRunAtUtc(OffsetDateTime runAtUtc) {
        this.runAtUtc = runAtUtc;
    }


    public void setUpdatedAtUtc(OffsetDateTime updatedAtUtc) {
        this.updatedAtUtc = updatedAtUtc;
    }


    public OffsetDateTime getUpdatedAtUtc() {
        return updatedAtUtc;
    }
}
