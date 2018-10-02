package de.bogenliga.application.business.user.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.impl.businessactivity.PasswordHashingBA;
import de.bogenliga.application.business.user.impl.businessactivity.TechnicalUserBA;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.user.impl.mapper.UserMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

@Component
public class UserComponentImpl implements UserComponent {

    private static final String PRECONDITION_MSG_USER = "UserDO must not be null";
    private static final String PRECONDITION_MSG_USER_ID = "UserDO ID must not be negative";
    private static final String PRECONDITION_MSG_USER_EMAIL = "UserDO email must not be null or empty";
    private static final String PRECONDITON_MSG_USER_PASSWORD = "UserDO password must not be null or empty";

    private final UserDAO userDAO;
    private final TechnicalUserBA technicalUserBA;
    private final PasswordHashingBA passwordHashingBA;

    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     *  @param userDAO to access the database and return user representations
     * @param technicalUserBA to handle all technical user operations
     * @param passwordHashingBA to handle the password and salt generation
     */
    @Autowired
    public UserComponentImpl(final UserDAO userDAO,
                             final TechnicalUserBA technicalUserBA,
                             final PasswordHashingBA passwordHashingBA) {
        this.userDAO = userDAO;
        this.technicalUserBA = technicalUserBA;
        this.passwordHashingBA = passwordHashingBA;
    }

    @Override
    public List<UserDO> findAll() {
        final List<UserBE> userBEList = userDAO.findAll();
        return userBEList.stream().map(UserMapper.toVO).collect(Collectors.toList());
    }

    @Override
    public UserDO findById(final int id) {
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

        return UserMapper.toVO.apply(result);
    }


    @Override
    public UserDO findByEmail(final String email) {
        Preconditions.checkNotNullOrEmpty(email, PRECONDITION_MSG_USER_EMAIL);

        final UserBE result = userDAO.findByEmail(email);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for email '%s'", email));
        }

        return UserMapper.toVO.apply(result);
    }


    @Override
    public UserDO create(final UserDO userDO) {
        checkUserDO(userDO);

        final UserBE userBE = UserMapper.toBE.apply(userDO);
        return UserMapper.toVO.apply(userDAO.create(userBE));
    }


    @Override
    public UserDO update(final UserDO userDO) {
        checkUserDO(userDO);

        final UserBE userBE = UserMapper.toBE.apply(userDO);
        return UserMapper.toVO.apply(userDAO.update(userBE));
    }


    @Override
    public void delete(final UserDO userDO) {
        Preconditions.checkNotNull(userDO, PRECONDITION_MSG_USER);

        final UserBE userBE = UserMapper.toBE.apply(userDO);
        userDAO.delete(userBE);

    }


    @Override
    public UserDO signin(final String email, final String password) {
        Preconditions.checkNotNullOrEmpty(email, PRECONDITION_MSG_USER_EMAIL);
        Preconditions.checkNotNullOrEmpty(password, PRECONDITON_MSG_USER_PASSWORD);

        // TODO check lock

        // get user from database
        final UserBE existingUser = userDAO.findByEmail(email);
        if (existingUser == null) {
            throw new BusinessException(ErrorCode.INVALID_LOGIN_CREDENTIALS, "Invalid login");
        }

        // check password
        final String hashedPassword = passwordHashingBA.calculateHash(password, existingUser.getUserSalt());

        if (hashedPassword.equals(existingUser.getUserPassword())) {
            // authenticated
            return UserMapper.toVO.apply(existingUser);
        } else {
            throw new BusinessException(ErrorCode.INVALID_LOGIN_CREDENTIALS, "Invalid login");
        }
    }


    @Override
    public boolean isTechnicalUser(final UserDO userDO) {
        checkUserDO(userDO);

        return technicalUserBA.isTechnicalUser(userDO);
    }


    private void checkUserDO(final UserDO userDO) {
        Preconditions.checkNotNull(userDO, PRECONDITION_MSG_USER);
        Preconditions.checkArgument(userDO.getId() >= 0, PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(userDO.getEmail(), PRECONDITION_MSG_USER_EMAIL);
    }
}
