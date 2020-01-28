package de.bogenliga.application.business.user.impl.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.business.user.impl.businessactivity.SignInBA;
import de.bogenliga.application.business.user.impl.businessactivity.TechnicalUserBA;
import de.bogenliga.application.business.user.impl.businessactivity.PasswordHashingBA;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.user.impl.mapper.UserMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserComponent}
 */
@Component
public class UserComponentImpl implements UserComponent {

    private static final String PRECONDITION_MSG_USER = "UserDO must not be null";
    private static final String PRECONDITION_MSG_USER_ID = "UserDO ID must not be negative";
    private static final String PRECONDITION_MSG_USER_NULL = "UserID must not be null";
    private static final String PRECONDITION_MSG_USER_EMAIL = "UserDO email must not be null or empty";
    private static final String PRECONDITON_MSG_USER_PWD = "UserDO password must not be null or empty";
    private static final String PRECONDITON_MSG_USER_WRONG_PWD = "Current password incorrect";
    private static final String USER_ROLE_DEFAULT = "USER";
    private final UserDAO userDAO;
    private final SignInBA signInBA;
    private final TechnicalUserBA technicalUserBA;
    private final PasswordHashingBA passwordHashingBA;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param userDAO to access the database and return user representations
     * @param signInBA to sign in users
     * @param technicalUserBA to handle all technical user operations
     */
    @Autowired
    public UserComponentImpl(final UserDAO userDAO,
                             final PasswordHashingBA passwordHashingBA,
                             final SignInBA signInBA,
                             final TechnicalUserBA technicalUserBA) {
        this.userDAO = userDAO;
        this.passwordHashingBA = passwordHashingBA;
        this.signInBA = signInBA;
        this.technicalUserBA = technicalUserBA;
    }

    /**
     * findAll
     *
     * liefert die Liste alle User - jeweils ID und User-Name (Email)
     */

    @Override
    public List<UserDO> findAll() {
        final List<UserBE> userBEList = userDAO.findAll();
        return userBEList.stream().map(UserMapper.toUserDO).collect(Collectors.toList());
    }

    /**
     * findByID
     *
     * @param id User-Id des gesuchten Users
     * Liest den UserName zu einer gegebenen User-ID
     */
    @Override
    public UserDO findById(final Long id) {
        Preconditions.checkNotNull(id, PRECONDITION_MSG_USER_ID);
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_USER_ID);

        if (id == 0) {
            // 0 identifies the SYSTEM user
            return technicalUserBA.getSystemUser();
        }

        final UserBE result = userDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return UserMapper.toUserDO.apply(result);
    }

    /**
     * findBy Email
     *
     * liefert die Liste alle User - jeweils ID und User-Name (Email)
     * @param  email User-Name des gesuchten Users
     */

    @Override
    public UserDO findByEmail(final String email) {
        Preconditions.checkNotNullOrEmpty(email, PRECONDITION_MSG_USER_EMAIL);

        final UserBE result = userDAO.findByEmail(email);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for email '%s'", email));
        }

        return UserMapper.toUserDO.apply(result);
    }

    /**
     * signIn
     *
     * User anmelden
     * @param  email User-Name
     * @param  password Kennwort
     */
    @Override
    public UserWithPermissionsDO signIn(final String email, final String password) {
        Preconditions.checkNotNullOrEmpty(email, PRECONDITION_MSG_USER_EMAIL);
        Preconditions.checkNotNullOrEmpty(password, PRECONDITON_MSG_USER_PWD);

        return signInBA.signInUser(email, password);
    }

    /**
     * create
     *
     * Neuen User anlegen
     * @param  email User-Name
     * @param  password Kennwort
     * @param  currentUserId aktueller User mit den Rechten zur Neuanlage
     */
    @Override
    public UserDO create(final String email, final String password, final Long currentUserId, final boolean isUsing2FA) {
        Preconditions.checkNotNullOrEmpty(email, PRECONDITION_MSG_USER_EMAIL);
        Preconditions.checkNotNullOrEmpty(password, PRECONDITON_MSG_USER_PWD);
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_USER_NULL);

        final UserBE result = new UserBE();
        final String salt = passwordHashingBA.generateSalt();
        final String pwdhash = passwordHashingBA.calculateHash(password, salt);
        result.setUserEmail(email);
        result.setUserSalt(salt);
        result.setUserPassword(pwdhash);
        result.setUsing2FA(isUsing2FA);

        final UserBE persistedUserBE = userDAO.create(result, currentUserId);


        return UserMapper.toUserDO.apply(persistedUserBE);
    }



    /**
     * update
     *
     * Passwort aktualiseren
     * @param  userDO User-ID des Accounts
     * @param  password Kennwort bisher
     * @param  newPassword Kennwort neu
     * @param  currentUserId aktueller User mit den Rechten zum Ändern
     *                       - hieraus wird das Passwort zur Vergleichsprüfung bestimmt
     *
     */
    @Override
    public UserDO update(final UserDO userDO, final String password, final String newPassword, final Long currentUserId) {
        Preconditions.checkNotNull(userDO, PRECONDITION_MSG_USER);
        Preconditions.checkArgument(userDO.getId() > 0, PRECONDITION_MSG_USER_NULL);
        Preconditions.checkNotNullOrEmpty(password, PRECONDITON_MSG_USER_PWD);
        Preconditions.checkNotNullOrEmpty(newPassword, PRECONDITON_MSG_USER_PWD);
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_USER_ID);
        Preconditions.checkArgument(currentUserId > 0, PRECONDITION_MSG_USER_NULL);


        final UserBE currentUser = userDAO.findById(userDO.getId());
        // string vergleich der hash-Werte im aktuelllen BE Object und aus dem aktuellen Password
        // nur bei Identität (Passwort richtig) geht es weiter
        // check password
        final String hashedPassword = passwordHashingBA.calculateHash(password, currentUser.getUserSalt());
        final String newpwdhash = passwordHashingBA.calculateHash(newPassword, currentUser.getUserSalt());

        if (hashedPassword.equals(currentUser.getUserPassword())) {
            // existing user with correct password
            currentUser.setUserPassword(newpwdhash);
            final UserBE persistedUserBE = userDAO.update(currentUser, currentUserId);
            return UserMapper.toUserDO.apply(persistedUserBE);

        } else {
            // wrong password
            throw new BusinessException(ErrorCode.INSUFFICIENT_CREDENTIALS, PRECONDITON_MSG_USER_WRONG_PWD);
        }
    }





    @Override
    public boolean isTechnicalUser(final UserDO userDO) {
        Preconditions.checkNotNull(userDO, PRECONDITION_MSG_USER);
        Preconditions.checkArgument(userDO.getId() >= 0, PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(userDO.getEmail(), PRECONDITION_MSG_USER_EMAIL);

        return technicalUserBA.isTechnicalUser(userDO);
    }

    @Override
    public boolean deactivate(long id) {
        final UserBE user = userDAO.findById(id);
        user.setActive(false);
        final UserBE updatedUser = userDAO.update(user, id);
        return !UserMapper.toUserDO.apply(updatedUser).isActive();
    }

}
