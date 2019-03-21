package de.bogenliga.application.business.role.api;

import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

/**
 * Responsible for the user database requests.
 */
public interface RoleComponent extends ComponentFacade {

    /**
     * Return all role entries.
     *
     * @return list of all roles in the database;
     * empty list, if no roles are found
     */
    List<RoleDO> findAll();


    /**
     * Return a role entry with the given name.
     *
     * @param roleName of the role
     * @return single role entry with the given name;
     * null, if no role is found
     */
    RoleDO findByName(String roleName);

}
