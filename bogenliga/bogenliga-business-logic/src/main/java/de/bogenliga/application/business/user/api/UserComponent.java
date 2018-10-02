package de.bogenliga.application.business.user.api;

import de.bogenliga.application.business.user.api.types.UserVO;
import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

public interface UserComponent extends ComponentFacade {

    /**
     * Return all user entries.
     *
     * @return list of all user user in the database;
     * empty list, if no user is found
     */
    List<UserVO> findAll();


    /**
     * Return a user entry with the given id.
     *
     * @param id of the user
     * @return single user entry with the given id;
     * null, if no user is found
     */
    UserVO findById(int id);


    /**
     * Create a new user in the database.
     *
     * @param userVO new user
     * @return persisted version of the user
     */
    UserVO create(UserVO userVO);


    /**
     * Update an existing user. The user is identified by the id.
     *
     * @param userVO existing userVO to update
     * @return persisted version of the user
     */
    UserVO update(UserVO userVO);


    /**
     * Delete an existing user. The user is identified by the id.
     *
     * @param userVO user to delete
     */
    void delete(UserVO userVO);
}
