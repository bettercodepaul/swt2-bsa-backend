package de.bogenliga.application.business.user.impl.businessactivity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.dao.UserLoginHistoryDAO;
import de.bogenliga.application.business.user.impl.dao.UserPermissionDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.user.impl.entity.UserFailedSignInAttemptsBE;
import de.bogenliga.application.business.user.impl.entity.UserPermissionBE;
import de.bogenliga.application.business.user.impl.entity.UserSignInHistoryBE;
import de.bogenliga.application.business.user.impl.mapper.UserMapper;
import de.bogenliga.application.business.user.impl.types.SignInResult;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I handle the user sign in process.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class SignInBA {

    private static final String ERROR_MESSAGE_INVALID_CREDENTIALS = "Invalid sign in credentials.";
    private static final String ERROR_MESSAGE_BLOCKED_USER = "User is blocked for %s seconds.";

    // 5 allowed attempts, multiplied by 3 (= 15) because 3 POST-requests are sent per login attempt.
    private static final int MAX_ALLOWED_LOGIN_ATTEMPTS = 15;
    private static final int LOGIN_ATTEMPTS_TIME_RANGE = 5 * 60; // 5 minutes in seconds

    private final UserDAO userDAO;
    private final UserPermissionDAO userPermissionDAO;
    private final UserLoginHistoryDAO userLoginHistoryDAO;
    private final PasswordHashingBA passwordHashingBA;


    @Autowired
    public SignInBA(final UserDAO userDAO,
                    final UserPermissionDAO userPermissionDAO,
                    final UserLoginHistoryDAO userLoginHistoryDAO,
                    final PasswordHashingBA passwordHashingBA) {
        this.userDAO = userDAO;
        this.userPermissionDAO = userPermissionDAO;
        this.userLoginHistoryDAO = userLoginHistoryDAO;
        this.passwordHashingBA = passwordHashingBA;
    }


    /**
     * Authorize or decline an user to use the system.
     * <ul>
     * <li>Check, if the user exists. </li>
     * <li>Check, if the user has too many failed sign in attempts (is blocked)</li>
     * <li>Check, if the password is correct</li>
     * <li>Logs the sign in history of the user</li>
     * </ul>
     *
     * @param email    of the user
     * @param password of the user
     *
     * @return user information with permissions to access the system functionalities
     *
     * @throws BusinessException, if the user is not found, the password is incorrect or the user is blocked.
     */
    public UserWithPermissionsDO signInUser(final String email, final String password) {
        // get user from database
        final UserBE existingUser = ensureUserExistence(email);

        // check sign in attempts
        checkFailedSignInAttempts(existingUser);

        // check password
        final String hashedPassword = passwordHashingBA.calculateHash(password, existingUser.getUserSalt());

        if (hashedPassword.equals(existingUser.getUserPassword())) {
            // existing user with correct password
            return authorizeUser(existingUser);

        } else {
            // wrong password
            persistSignInAttempt(existingUser, SignInResult.LOGIN_FAILED);
            throw new BusinessException(ErrorCode.INVALID_SIGN_IN_CREDENTIALS, ERROR_MESSAGE_INVALID_CREDENTIALS);
        }
    }


    private UserWithPermissionsDO authorizeUser(final UserBE existingUser) {
        final List<UserPermissionBE> userPermissionBEList = userPermissionDAO.findByUserId(
                existingUser.getUserId());

        // map permissions to string list
        final List<String> userPermissions = userPermissionBEList.stream()
                .map(UserPermissionBE::getPermissionName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        persistSignInAttempt(existingUser, SignInResult.LOGIN_SUCCESS);

        return UserMapper.toUserWithPermissionsDO.apply(existingUser, userPermissions);
    }


    private UserBE ensureUserExistence(final String email) {
        final UserBE existingUser = userDAO.findByEmail(email);

        if (existingUser == null) {
            // no user found
            throw new BusinessException(ErrorCode.INVALID_SIGN_IN_CREDENTIALS, ERROR_MESSAGE_INVALID_CREDENTIALS);
        }
        return existingUser;
    }


    private void checkFailedSignInAttempts(final UserBE existingUser) {
        final UserFailedSignInAttemptsBE failedLoginsBE = userLoginHistoryDAO.findSignInUserInformationByEmail(
                existingUser.getUserId(), LOGIN_ATTEMPTS_TIME_RANGE);

        if (failedLoginsBE.getFailedLoginAttempts() > MAX_ALLOWED_LOGIN_ATTEMPTS) {
            // do not allow a login
            // the sign in is blocked for a defined time range (LOGIN_ATTEMPTS_TIME_RANGE)
            persistSignInAttempt(existingUser, SignInResult.LOGIN_FAILED);
            throw new BusinessException(ErrorCode.TOO_MANY_INCORRECT_LOGIN_ATTEMPTS,
                    String.format(ERROR_MESSAGE_BLOCKED_USER, LOGIN_ATTEMPTS_TIME_RANGE),
                    LOGIN_ATTEMPTS_TIME_RANGE);
        }
    }


    private void persistSignInAttempt(final UserBE existingUser, final SignInResult signInResult) {
        final UserSignInHistoryBE userSignInHistoryBE = new UserSignInHistoryBE();
        userSignInHistoryBE.setSignInUserId(existingUser.getUserId());
        userSignInHistoryBE.setSignInResult(signInResult);
        userSignInHistoryBE.setSignInAtUtc(DateProvider.currentTimestampUtc());
        userLoginHistoryDAO.create(userSignInHistoryBE);
    }
}