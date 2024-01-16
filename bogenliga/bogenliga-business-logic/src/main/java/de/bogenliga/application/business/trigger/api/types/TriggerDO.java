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
    private Long version;
    private String kategorie;
    private Long altsystemId;
    private MigrationChangeType operation;
    private MigrationChangeState status;
    private String nachricht;
    private OffsetDateTime createdAtUtc;




    public TriggerDO(final Long id, final Long version, final String kategorie, final Long altsystemId, final MigrationChangeType operation, final MigrationChangeState status, final String nachricht, final OffsetDateTime createdAtUtc){
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
}
