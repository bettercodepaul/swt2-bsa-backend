package de.bogenliga.application.business.user.impl.businessactivity;

import org.springframework.stereotype.Component;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.common.component.businessactivity.BusinessActivity;

/**
 * I encapsulate the logic for technical users and their management.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class TechnicalUserBA implements BusinessActivity {

    private static final UserDO SYSTEM_USER = new UserDO(0, "SYSTEM", null, 0, null, 0, 0);


    /**
     * I return the SYSTEM user
     *
     * @return SYSTEM user with the id = 0
     */
    public UserDO getSystemUser() {
        return SYSTEM_USER;
    }


    /**
     * I validate, if the given user is a technical user.
     *
     * @param userDO to be checked
     * @return true, if the user matches one of the statically defined technical users.
     */
    public boolean isTechnicalUser(final UserDO userDO) {
        return userDO.equals(SYSTEM_USER);
    }
}
