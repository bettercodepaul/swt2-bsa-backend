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
     * Returns all Ligamatch objects.
     *
     * @return List of all MatchPasseView objects. MatchPasseView is created by merging the views ligamatch and ligapasse
     */
    List<LigamatchDO> findAll();

    /**
     * Returns a Ligamatch with the given id
     *
     * @param  wettkampfId Wettkampf ID of the MatchPasseView to be queried from the database.
     *
     *
     * @return returns a MatchPasseView
     */
    LigamatchDO findById(long wettkampfId);

}
