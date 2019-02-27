package de.bogenliga.application.business.wettkampf.api;

import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfOverviewDO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfOverviewBE;
import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

/**
 * Responsible for the user database requests.
 */
public interface WettkampfOverviewComponent extends ComponentFacade {

    /**
     * Return a overview of all competitions
     *
     * @return all competitions with day and league name
     */
    List<WettkampfOverviewDO> getOverview();
}
