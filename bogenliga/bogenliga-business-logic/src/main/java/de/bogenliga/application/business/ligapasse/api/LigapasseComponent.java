package de.bogenliga.application.business.ligapasse.api;

import java.util.List;

import de.bogenliga.application.business.ligapasse.api.types.LigapasseDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * TODO [AL] class documentation
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
public interface LigapasseComponent extends ComponentFacade {

    /**
     * Returns a Ligapasse with the given id
     *
     * @param  wettkampfId Wettkampf ID of the MatchPasseView to be queried from the database.
     *
     *
     * @return returns a MatchPasseView
     */
    LigapasseDO findById(long wettkampfId);
}
