package de.bogenliga.application.services.v1.trigger.model;

import de.bogenliga.application.common.altsystem.AltsystemEntity;

/**
 * A migration change contains information about an object which has
 * changed in the old database ("Altsystem") and should be
 * transferred to the new database.
 */
public class MigrationChange<T extends AltsystemEntity> {

    /**
     * The relevant entity retrieved from the old database ("Altsystem").
     */
    private final T altsystemEntity;
    /**
     * The operation this migration change should perform (create or update).
     */
    private final MigrationChangeType type;
    /**
     * The current state of this migration change.
     */
    private MigrationChangeState state;


    public MigrationChange(T altsystemEntity, MigrationChangeType type, MigrationChangeState state) {
        this.altsystemEntity = altsystemEntity;
        this.type = type;
        this.state = state;
    }

    public T getAltsystemEntity() {
        return altsystemEntity;
    }

    public MigrationChangeType getType() {
        return type;
    }

    public MigrationChangeState getState() {
        return state;
    }

    public void setState(MigrationChangeState state) {
        this.state = state;
    }
}
