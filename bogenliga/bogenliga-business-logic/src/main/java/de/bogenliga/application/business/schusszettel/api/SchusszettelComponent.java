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

    /**
     * Generates a pdf as binary document
     * @param matchId1 ID for the first match
     * @param matchId2 ID for the second atch
     * @return document
     */
    byte[] getFilledSchusszettelPDFasByteArray(long matchId1, long matchId2);

}
