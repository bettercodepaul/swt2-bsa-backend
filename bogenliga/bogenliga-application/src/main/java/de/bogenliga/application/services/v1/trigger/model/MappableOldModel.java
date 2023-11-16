package de.bogenliga.application.services.v1.trigger.model;

import de.bogenliga.application.common.component.entity.BusinessEntity;

/**
 * I represent any relevant object imported from the old database (bogenliga.de).
 * This interface can be implemented to add support for old-to-new database conversion.
 */
public interface MappableOldModel<T extends BusinessEntity> {

    /**
     * Converts this bogenliga.de model into a business entity suitable for the new database.
     *
     * @return An equivalent, Bogenliga APP business entity representation of this old database model.
     */
    T toNewModelBE();
}
