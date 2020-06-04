package de.bogenliga.application.business.schusszettel.api;

import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the setzliste database requests.
 */
public interface SchusszettelComponent extends ComponentFacade {

    /**
     * Generates a pdf as binary document
     * @param wettkampfid ID for the competition
     * @return document
     */
    byte[] getAllSchusszettelPDFasByteArray(long wettkampfid);

}
