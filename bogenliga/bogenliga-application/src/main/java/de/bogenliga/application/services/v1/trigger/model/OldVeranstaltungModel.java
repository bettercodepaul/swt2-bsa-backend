package de.bogenliga.application.services.v1.trigger.model;

import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;

/**
 * I represent a "Veranstaltung" object, as represented in the old database (bogenliga.de).
 */
public class OldVeranstaltungModel implements MappableOldModel<VeranstaltungBE> {
    @Override
    public VeranstaltungBE toNewModelBE() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
