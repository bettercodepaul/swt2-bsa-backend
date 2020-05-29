package de.bogenliga.application.business.meldezettel.api;

import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the meldezettel database requests.
 */
public interface MeldezettelComponent extends ComponentFacade {

    /**
     * Generates a pdf as binary document
     * @param wettkampfid ID for the competition
     * @return document
     */
    byte[] getMeldezettelPDFasByteArray(long wettkampfid);

}
