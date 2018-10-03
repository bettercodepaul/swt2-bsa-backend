package de.bogenliga.application.business.user.api;

import java.util.List;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * TODO [YP] AL: Add documentation
 */
public interface UserComponent extends ComponentFacade {

    /**
     * Return all user entries.
     *
     * @return list of all user user in the database;
     * empty list, if no user is found
     */
    List<UserDO> findAll();


    /**
     * Return a user entry with the given id.
     *
     * @param id of the user
     * @return single user entry with the given id;
     * null, if no user is found
     */
    UserDO findById(int id);

    /**
     * Return a user entry with the given id.
     *
     * @param email of the user
     *
     * @return single user entry with the given email; null, if no user is found
     */
    UserDO findByEmail(String email);

    /**
     * Create a new user in the database.
     *
     * @param userDO new user
     * @return persisted version of the user
     */
    UserDO create(UserDO userDO, long currentUserId);


    /**
     * Update an existing user. The user is identified by the id.
     *
     * @param userDO existing userDO to update
     * @return persisted version of the user
     */
    UserDO update(UserDO userDO, long currentUserId);


    /**
     * Delete an existing user. The user is identified by the id.
     *
     * @param userDO user to delete
     */
    void delete(UserDO userDO, long currentUserId);

    /**
     * Sign in an user with an email address and a password.
     * <p>
     * The password will be hashed and checked with the stored password from the database.
     *
     * @param email    the email address is used as unique login name
     * @param password of the user
     *
     * @return the user with permissions, if the user exists and the password is correct
     */
    UserWithPermissionsDO signIn(String email, String password);

    /**
     * Identifies technical user, e.g. the SYSTEM user
     *
     * @param userDO to be checked
     *
     * @return true, if the user is a technical user
     */
    boolean isTechnicalUser(UserDO userDO);
}
