package de.bogenliga.application.services.v1.trigger.model;

import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;

/**
 * I represent a "Mannschaft" object, as represented in the old database (bogenliga.de).
 */
public class OldMannschaftModel implements MappableOldModel<DsbMannschaftBE> {
    @Override
    public DsbMannschaftBE toNewModelBE() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
