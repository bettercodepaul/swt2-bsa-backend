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
import de.bogenliga.application.business.user.impl.entity.UserFailedLoginAttemptsBE;
import de.bogenliga.application.business.user.impl.entity.UserPermissionBE;
import de.bogenliga.application.business.user.impl.mapper.UserMapper;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class SignInBA {

    private static final int MAX_ALLOWED_LOGIN_ATTEMPTS = 5;
    private static final int LOGIN_ATTEMPTS_TIME_RANGE = 60 * 60;

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


    public UserWithPermissionsDO signInUser(final String email, final String password) {
        // get user from database
        final UserBE existingUser = userDAO.findByEmail(email);

        if (existingUser == null) {
            // no user found
            return null;
        }

        // check login attempts
        final UserFailedLoginAttemptsBE failedLoginsBE = userLoginHistoryDAO.findSignInUserInformationByEmail(
                existingUser.getUserId(), LOGIN_ATTEMPTS_TIME_RANGE);

        if (failedLoginsBE.getFailedLoginAttempts() > MAX_ALLOWED_LOGIN_ATTEMPTS) {
            // do not allow a login
            // the sign in is blocked for the defined time range
            return null;
        }

        // check password
        final String hashedPassword = passwordHashingBA.calculateHash(password, existingUser.getUserSalt());

        if (hashedPassword.equals(existingUser.getUserPassword())) { // authorize user
            // get permissions
            final List<UserPermissionBE> userPermissionBEList = userPermissionDAO.findByUserId(
                    existingUser.getUserId());

            // map permissions to string list
            final List<String> userPermissions = userPermissionBEList.stream()
                    .map(UserPermissionBE::getPermissionName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return UserMapper.toUserWithPermissionsDO.apply(existingUser, userPermissions);
        } else {
            // wrong password
            return null;
        }
    }
}
