package de.bogenliga.application.business.dsbmannschaft.api;

import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.common.component.ComponentFacade;

public interface DsbMannschaftSortierungComponent extends ComponentFacade {

    /**
     * Updates the sorting attribute of a Mannschaft.
     * @param mannschaftDO A DsbMannschaftDO which contains at least the Mannschaft-ID
     *                     and the Sortierung. The rest can be empty / null.
     * @return the persisited DO.
     */
    DsbMannschaftDO updateSortierung(DsbMannschaftDO mannschaftDO, long currentDsbMitgliedID);
}
