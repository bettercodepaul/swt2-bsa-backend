package de.bogenliga.application.services.v1.trigger.model;



import java.time.OffsetDateTime;
import de.bogenliga.application.business.trigger.api.types.MigrationChangeState;
import de.bogenliga.application.business.trigger.api.types.MigrationChangeType;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerDTO implements DataTransferObject {

    private Long id;
    private Long version;
    private String kategorie;
    private Long altsystemId;
    private MigrationChangeType operation;
    private MigrationChangeState status;
    private String nachricht;
    private OffsetDateTime createdAtUtc;


    /**
     *
     * @param id
     * @param version
     * @param kategorie
     * @param altsystemId
     * @param operation
     * @param status
     * @param nachricht
     * @param createdAtUtc
     */

    public TriggerDTO(Long id, Long version, String kategorie, Long altsystemId, MigrationChangeType operation, MigrationChangeState status, String nachricht, OffsetDateTime createdAtUtc){
        this.id = id;
        this.version = version;
        this.kategorie = kategorie;
        this.altsystemId = altsystemId;
        this.operation = operation;
        this.status = status;
        this.nachricht = nachricht;
        this.createdAtUtc = createdAtUtc;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getVersion() {
        return version;
    }


    public void setVersion(Long version) {
        this.version = version;
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


    public MigrationChangeType getOperation() {
        return operation;
    }


    public void setOperation(MigrationChangeType operation) {
        this.operation = operation;
    }


    public MigrationChangeState getStatus() {
        return status;
    }


    public void setStatus(MigrationChangeState status) {
        this.status = status;
    }


    public String getNachricht() {
        return nachricht;
    }


    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }


    public OffsetDateTime getCreatedAtUtc() {
        return createdAtUtc;
    }


    public void setCreatedAtUtc(OffsetDateTime created_at_utc) {
        this.createdAtUtc = createdAtUtc;
    }

}
