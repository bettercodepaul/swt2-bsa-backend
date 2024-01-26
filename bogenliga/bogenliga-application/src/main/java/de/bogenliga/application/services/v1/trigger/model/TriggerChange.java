package de.bogenliga.application.services.v1.trigger.model;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import de.bogenliga.application.business.trigger.api.TriggerComponent;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.common.altsystem.AltsystemDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;

/**
 * A migration change contains information about an object which has
 * changed in the old database ("Altsystem") and should be
 * transferred to the new database.
 */
public class TriggerChange<T extends AltsystemDO> {
    private final TriggerDO data;
    /**
     * The relevant data object retrieved from the old database ("Altsystem").
     */
    private final T altsystemDataObject;
    /**
     * The relevant entity which handles conversion to a new model.
     */
    private final AltsystemEntity<T> altsystemEntity;

    private final long triggeringUserId;

    private final TriggerComponent triggerComponent;

    public TriggerChange(TriggerComponent triggerComponent, TriggerDO data, T altsystemDataObject, AltsystemEntity<T> altsystemEntity, long triggeringUserId) {
        this.triggerComponent = triggerComponent;
        this.data = data;
        this.altsystemDataObject = altsystemDataObject;
        this.altsystemEntity = altsystemEntity;
        this.triggeringUserId = triggeringUserId;
    }


    public TriggerDO getData() {
        return data;
    }


    public T getAltsystemDataObject() {
        return altsystemDataObject;
    }

    public TriggerChangeOperation getType() {
        return data.getOperation();
    }

    public TriggerChangeStatus getState() {
        return data.getStatus();
    }

    public void setState(TriggerChangeStatus state) {
        data.setStatus(state);
    }

    public boolean tryMigration() {
        data.setRunAtUtc(OffsetDateTime.now());
        setState(TriggerChangeStatus.IN_PROGRESS);
        triggerComponent.update(data, triggeringUserId);

        try {
            executeMigrationOnEntity();
            setState(TriggerChangeStatus.SUCCESS);
            return true;
        } catch (Exception exception) {
            setState(TriggerChangeStatus.ERROR);
            data.setNachricht(exception.getMessage());
            return false;
        } finally {
            triggerComponent.update(data, triggeringUserId);
        }
    }

    void executeMigrationOnEntity() throws SQLException {
        switch (getType()) {
            case CREATE:
                altsystemEntity.create(altsystemDataObject, triggeringUserId);
                break;
            case UPDATE:
                altsystemEntity.update(altsystemDataObject, triggeringUserId);
                break;
            default:
                throw new IllegalStateException("Invalid operation: " + getType());
        }
    }
}
