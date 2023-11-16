package de.bogenliga.application.services.v1.trigger.model;

import de.bogenliga.application.business.vereine.impl.entity.VereinBE;

/**
 * I represent a "Verein" object, as represented in the old database (bogenliga.de).
 */
public class OldVereinModel implements MappableOldModel<VereinBE> {
    @Override
    public VereinBE toNewModelBE() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
