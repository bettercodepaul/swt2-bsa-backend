package de.bogenliga.application.services.v1.trigger.model;

import de.bogenliga.application.business.liga.impl.entity.LigaBE;

/**
 * I represent a "Liga" object, as represented in the old database (bogenliga.de).
 */
public class OldLigaModel implements MappableOldModel<LigaBE> {
    @Override
    public LigaBE toNewModelBE() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
