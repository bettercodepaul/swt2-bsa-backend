package de.bogenliga.application.services.v1.trigger.model;

import de.bogenliga.application.business.schuetzenstatistik.impl.entity.SchuetzenstatistikBE;

/**
 * I represent a "Schütze" object, as represented in the old database (bogenliga.de).
 */
public class OldSchuetzeModel implements MappableOldModel<SchuetzenstatistikBE> {
    @Override
    public SchuetzenstatistikBE toNewModelBE() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
