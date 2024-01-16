package de.bogenliga.application.services.v1.trigger.model;



import java.time.OffsetDateTime;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerDTO implements DataTransferObject {

    private Long id;
    private String kategorie;
    private Long altsystemId;
    private TriggerChangeOperation operation;
    private TriggerChangeStatus status;
    private String nachricht;
    private OffsetDateTime runAtUtc;


    /**
     *
     * @param id
     * @param kategorie
     * @param altsystemId
     * @param operation
     * @param status
     * @param nachricht
     * @param runAtUtc
     */

    public TriggerDTO(Long id, String kategorie, Long altsystemId, TriggerChangeOperation operation, TriggerChangeStatus status, String nachricht, OffsetDateTime runAtUtc){
        this.id = id;
        this.kategorie = kategorie;
        this.altsystemId = altsystemId;
        this.operation = operation;
        this.status = status;
        this.nachricht = nachricht;
        this.runAtUtc = runAtUtc;
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


    public OffsetDateTime getRunAtUtc() {
        return runAtUtc;
    }


    public void setRunAtUtc(OffsetDateTime runAtUtc) {
        this.runAtUtc = runAtUtc;
    }

}
