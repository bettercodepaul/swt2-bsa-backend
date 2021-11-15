package de.bogenliga.application.business.schusszettel.api;

import de.bogenliga.application.business.schusszettel.api.types.MatchPasseViewDO;
import de.bogenliga.application.common.component.ComponentFacade;
import java.util.List;

/**
 *
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
public interface MatchPasseViewComponent extends ComponentFacade {

    /**
     * Returns all MatchPasseView objects.
     *
     * @return List of all MatchPasseView objects. MatchPasseView is created by merging the views ligamatch and ligapasse
     */
    List<MatchPasseViewDO> findAll();

    /**
     * Returns a "MatchPasseView" with the given ids
     *
     * @param  wettkampfId Wettkampf ID of the MatchPasseView to be queried from the database.
     * @param  matchId Match ID of the MatchPasseView to be queried from the database.
     * @param  matchNr Match number of the MatchPasseView to be queried from the database.
     *
     * @return returns a MatchPasseView
     */
    MatchPasseViewDO findByIdsMatchNr(long wettkampfId,long matchId,int matchNr);

    /**
     * Returns a "MatchPasseView" with the given ids
     *
     * @param  wettkampfId Wettkampf ID of the MatchPasseView to be queried from the database.
     * @param  matchId Match ID of the MatchPasseView to be queried from the database.
     * @param  passeId Passe ID of the MatchPasseView to be queried from the database.
     *
     * @return returns a MatchPasseView
     */
    MatchPasseViewDO findByIdsPasseId(long wettkampfId,long matchId,long passeId);
}
