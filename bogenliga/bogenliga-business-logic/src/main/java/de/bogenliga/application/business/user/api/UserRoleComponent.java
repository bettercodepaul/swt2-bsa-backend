package de.bogenliga.application.business.user.api;

import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

/**
 * Responsible for the user database requests.
 */
public interface UserRoleComponent extends ComponentFacade {

    /**
     * Return all user entries.
     *
     * @return list of all dsbmitglied dsbmitglied in the database;
     * empty list, if no dsbmitglied is found
     */
    List<UserRoleDO> findAll();


    /**
     * Return a user entry with the given id.
     *
     * @param id of the user
     * @return single user entry with the given id;
     * null, if no user is found
     */
    List<UserRoleDO> findById(Long id);


    /**
     * Return all users with the given role
     * @param roleId
     * @return
     */
    List<UserRoleDO> findByRoleId(Long roleId);


    /**
     * Return a user entry with the given id.
     *
     * @param email of the user
     *
     * @return single user entry with the given email; null, if no user is found
     */
    UserRoleDO findByEmail(String email);


    /**
     * Create an user role.
     * <p>
     * The password will be hashed and checked with the stored password from the database.
     *
     * @param userId    the ID is used as user identifier name
     * @param roleId  of the user to be set
     *
     * @return the user with permissions, if the user exists and the password is correct
     */

    UserRoleDO create(final Long userId, final Long roleId, final Long currentUserId);

    /**
     * Create a user role by default role "User" .
     * <p>
     * The password will be hashed and checked with the stored password from the database.
     *
     * @param userId    the email address is used as unique login name
     *
     * @return the user with permissions, if the user exists and the password is correct
     */

    UserRoleDO create(final Long userId, final Long currentUserId);
    /**
     * Udpate password of user
     *
     * <p>
     * A new salt will be created and the password will be hashed and checked with
     * password requirements prdefined.
     *
     *
     * @param userRolesDO users current ID and new Role ID
     * @param currentUserId current user
     *
     * @return the user, if the user exists and the password is sufficient
     */
    List<UserRoleDO> update(final List<UserRoleDO> userRolesDO, final Long currentUserId);


    /**
     * Send Mail to all Admin E-mails with Feedback given as parameter
     *
     *
     * @param text text given in Frontend, optional with Mail of the sender at the end
     *
     * @return void
     */
    void sendFeedback(final String text);

}
