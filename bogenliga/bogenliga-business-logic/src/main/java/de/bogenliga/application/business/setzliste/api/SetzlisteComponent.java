package de.bogenliga.application.business.setzliste.api;

import de.bogenliga.application.business.match.api.types.MatchDO;
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
     *
     * @return List of matches for the competition
     */
    List<MatchDO> generateMatchesBySetzliste(Long wettkampfid, Long userId);


}
