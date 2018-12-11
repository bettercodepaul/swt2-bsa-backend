package de.bogenliga.application.business.user.api;

import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the user database requests.
 */
public interface UserProfileComponent extends ComponentFacade {

    /**
     * Return a user entry with the given id.
     *
     * @param id of the user
     * @return single user entry with the given id;
     * null, if no user is found
     */
    UserProfileDO findById(Long id);
}
