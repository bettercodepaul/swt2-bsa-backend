package de.bogenliga.application.business.Setzliste.api;

import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

/**
 * Responsible for the setzliste database requests.
 */
public interface SetzlisteComponent extends ComponentFacade {

    /**
     * Generates a pdf as binary document
     * @param wettkampfid ID for the competition
     * @return document
     */
    byte[] getPDFasByteArray(long wettkampfid);

    /**
     * <p>Creates matches in database based on the structure of Setzliste if matches don't exist
     *
     * </p>
     * @param wettkampfid ID for the competition
     */
    void generateMatchesBySetzliste(long wettkampfid);


}
