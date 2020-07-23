package de.bogenliga.application.business.user.api;

import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

/**
 * Responsible for the user database requests.
 */
public interface UserComponent extends ComponentFacade {

    /**
     * Return all user entries.
     *
     * @return list of all dsbmitglied dsbmitglied in the database;
     * empty list, if no dsbmitglied is found
     */
    List<UserDO> findAll();


    /**
     * Return a user entry with the given id.
     *
     * @param id of the user
     * @return single user entry with the given id;
     * null, if no user is found
     */
    UserDO findById(Long id);

    /**
     * Return a user entry with the given id.
     *
     * @param email of the user
     *
     * @return single user entry with the given email; null, if no user is found
     */
    UserDO findByEmail(String email);


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
     * Create new user
     *
     * <p>
     * new entry in benutzer entitiy will be crreated
     *
     *
     * @param email of the user
     * @param password of the user
     * @param dsb_mitglied_id current user
     * @param currentUserId current user
     *
     *
     * @return the user, if the user exists and the password is sufficient
     */
    UserDO create(final String email, final String password, final Long  dsb_mitglied_id, final Long currentUserId, final boolean isUsing2FA);

    /**
     * Udpate password of user
     *
     * <p>
     * A new salt will be created and the password will be hashed and checked with
     * password requirements prdefined.
     *
     *
     * @param userDO ID of the user account the pwd is to be changed
     * @param password of the user
     * @param newPassword of the user
     * @param currentUserId current user
     *
     * @return the user, if the user exists and the password is sufficient
     */
    UserDO updatePassword(final UserDO userDO, final String password, final String newPassword, final Long currentUserId);

    /**
     * Reset password of user
     *
     * <p>
     * A new salt will be created and the password will be hashed and checked with
     * password requirements prdefined.
     * @param userDO ID of the user account the pwd is to be changed
     * @param newPassword of the user
     * @param currentUserId current user
     *
     * @return the user, if the user exists and the password is sufficient
     */
    UserDO resetPassword(final UserDO userDO, final String newPassword, final Long currentUserId);

    /**
     * Identifies technical user, e.g. the SYSTEM user
     *
     * @param userDO to be checked
     *
     * @return true, if the user is a technical user
     */
    boolean isTechnicalUser(UserDO userDO);

    /**
     * Changes the active parameter to false to deactivate the user
     * @param id of the user to be deactivated
     * @return true, if the deactivation was successful
     */
    boolean deactivate(final long id);
}
