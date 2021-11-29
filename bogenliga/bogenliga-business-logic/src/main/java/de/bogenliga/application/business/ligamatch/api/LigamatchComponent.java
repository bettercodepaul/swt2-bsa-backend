package de.bogenliga.application.business.ligamatch.api;

import java.util.List;
import de.bogenliga.application.business.ligamatch.api.types.LigamatchDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * TODO [AL] class documentation
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
public interface LigamatchComponent extends ComponentFacade {

    /**
     * Returns a Ligamatch with the given id
     *
     * @param  wettkampfId Wettkampf ID of the MatchPasseView to be queried from the database.
     *
     *
     * @return returns a MatchPasseView
     */
    List<LigamatchDO> findById(long wettkampfId);

}
