package de.bogenliga.application.services.v1.trigger.model;

import java.util.HashMap;
import java.util.Map;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.altsystem.liga.entity.AltsystemLiga;
import de.bogenliga.application.common.altsystem.AltsystemDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;

/**
 * A migration change contains information about an object which has
 * changed in the old database ("Altsystem") and should be
 * transferred to the new database.
 */
public class MigrationChange<T extends AltsystemDO> {
    private static final Map<Class<?>, AltsystemEntity<?>> dataObjectToEntity = getClassToMapperMap();

    private static Map<Class<?>, AltsystemEntity<?>> getClassToMapperMap() {
        Map<Class<?>, AltsystemEntity<?>> result = new HashMap<>();

        // TODO register other classes
        result.put(AltsystemLigaDO.class, new AltsystemLiga());

        return result;
    }

    /**
     * The relevant entity retrieved from the old database ("Altsystem").
     */
    private final T altsystemDataObject;
    /**
     * The operation this migration change should perform (create or update).
     */
    private final MigrationChangeType type;
    /**
     * The current state of this migration change.
     */
    private MigrationChangeState state;


    public MigrationChange(T altsystemDataObject, MigrationChangeType type, MigrationChangeState state) {
        this.altsystemDataObject = altsystemDataObject;
        this.type = type;
        this.state = state;
    }

    public T getAltsystemDataObject() {
        return altsystemDataObject;
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

    public boolean tryMigration() {
        setState(MigrationChangeState.IN_PROGRESS);

        try {
            executeMigrationOnEntity();
            setState(MigrationChangeState.SUCCESS);
            return true;
        } catch (Exception exception) {
            setState(MigrationChangeState.ERROR);
            return false;
        }
    }

    private void executeMigrationOnEntity() {
        AltsystemEntity<T> entity = (AltsystemEntity<T>) dataObjectToEntity.get(altsystemDataObject.getClass());

        switch (type) {
            case CREATE:
                entity.create(altsystemDataObject);
                break;
            case UPDATE:
                entity.update(altsystemDataObject);
                break;
            default:
                throw new IllegalStateException("Invalid operation: " + type);
        }
    }
}
