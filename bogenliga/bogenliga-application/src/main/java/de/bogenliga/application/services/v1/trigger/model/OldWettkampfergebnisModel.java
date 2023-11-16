package de.bogenliga.application.services.v1.trigger.model;

import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;

/**
 * I represent a "Wettkampfergebnis" object, as represented in the old database (bogenliga.de).
 */
public class OldWettkampfergebnisModel implements MappableOldModel<WettkampfBE> {
    @Override
    public WettkampfBE toNewModelBE() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
