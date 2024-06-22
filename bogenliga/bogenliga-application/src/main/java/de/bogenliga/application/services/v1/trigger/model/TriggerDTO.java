package de.bogenliga.application.services.v1.trigger.model;



import java.time.OffsetDateTime;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 *
 *
 * @author Maximilian Fronmüller
 */
public class TriggerDTO implements DataTransferObject {

    private Long id;
    private String kategorie;
    private Long altsystemId;
    private TriggerChangeOperation operation;
    private TriggerChangeStatus status;
    private String nachricht;
    private OffsetDateTime createdAtUtc;
    private OffsetDateTime runAtUtc;
    private OffsetDateTime lastModifiedAtUtc;


    /**
     *
     * @param id
     * @param kategorie
     * @param altsystemId
     * @param operation
     * @param status
     * @param nachricht
     * @param createdAtUtc
     * @param runAtUtc
     * @param lastModifiedAtUtc
     */

    public TriggerDTO(Long id, String kategorie, Long altsystemId, TriggerChangeOperation operation, TriggerChangeStatus status, String nachricht, OffsetDateTime createdAtUtc, OffsetDateTime runAtUtc, OffsetDateTime lastModifiedAtUtc){
        this.id = id;
        this.kategorie = kategorie;
        this.altsystemId = altsystemId;
        this.operation = operation;
        this.status = status;
        this.nachricht = nachricht;
        this.createdAtUtc = createdAtUtc;
        this.runAtUtc = runAtUtc;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
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
    public OffsetDateTime getCreatedAtUtc(){
        return createdAtUtc
                ;}


    public void setCreatedAtUtc(OffsetDateTime createdAtUtc) {
        this.createdAtUtc = createdAtUtc;
    }


    public OffsetDateTime getRunAtUtc() {
        return runAtUtc;
    }


    public void setRunAtUtc(OffsetDateTime runAtUtc) {
        this.runAtUtc = runAtUtc;
    }

    public void setLastModifiedAtUtc(OffsetDateTime lastModifiedAtUtc){
        this.lastModifiedAtUtc = lastModifiedAtUtc;
    }
    public OffsetDateTime getlastModifiedAtUtc(){
        return lastModifiedAtUtc;
    }
}
