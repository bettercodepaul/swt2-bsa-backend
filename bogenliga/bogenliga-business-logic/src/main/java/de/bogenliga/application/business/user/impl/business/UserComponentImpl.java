package de.bogenliga.application.business.user.impl.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.business.user.impl.businessactivity.SignInBA;
import de.bogenliga.application.business.user.impl.businessactivity.TechnicalUserBA;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.user.impl.mapper.UserMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link UserComponent}
 */
@Component
public class UserComponentImpl implements UserComponent {

    private static final String PRECONDITION_MSG_USER = "UserDO must not be null";
    private static final String PRECONDITION_MSG_USER_ID = "UserDO ID must not be negative";
    private static final String PRECONDITION_MSG_USER_EMAIL = "UserDO email must not be null or empty";
    private static final String PRECONDITON_MSG_USER_PASSWORD = "UserDO password must not be null or empty";
    private final UserDAO userDAO;
    private final SignInBA signInBA;
    private final TechnicalUserBA technicalUserBA;


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
                             final SignInBA signInBA,
                             final TechnicalUserBA technicalUserBA) {
        this.userDAO = userDAO;
        this.signInBA = signInBA;
        this.technicalUserBA = technicalUserBA;
    }


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


    @Override
    public UserWithPermissionsDO signIn(final String email, final String password) {
        Preconditions.checkNotNullOrEmpty(email, PRECONDITION_MSG_USER_EMAIL);
        Preconditions.checkNotNullOrEmpty(password, PRECONDITON_MSG_USER_PASSWORD);

        return signInBA.signInUser(email, password);
    }


    @Override
    public boolean isTechnicalUser(final UserDO userDO) {
        Preconditions.checkNotNull(userDO, PRECONDITION_MSG_USER);
        Preconditions.checkArgument(userDO.getId() >= 0, PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(userDO.getEmail(), PRECONDITION_MSG_USER_EMAIL);

        return technicalUserBA.isTechnicalUser(userDO);
    }

}
