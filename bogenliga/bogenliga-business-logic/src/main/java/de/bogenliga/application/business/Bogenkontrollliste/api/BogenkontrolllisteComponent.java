package de.bogenliga.application.business.Bogenkontrollliste.api;

import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the setzliste database requests.
 */
public interface BogenkontrolllisteComponent extends ComponentFacade {

    /**
     * Generates a pdf as binary document
     * @param wettkampfid ID for the competition
     * @return document
     */
    byte[] getBogenkontrolllistePDFasByteArray(long wettkampfid);

}
