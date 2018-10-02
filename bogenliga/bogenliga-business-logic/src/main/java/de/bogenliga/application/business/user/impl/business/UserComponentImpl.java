package de.bogenliga.application.business.user.impl.business;

import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserVO;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.user.impl.mapper.UserMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserComponentImpl implements UserComponent {

    private static final String PRECONDITION_MSG_USER = "UserVO must not be null";
    private static final String PRECONDITION_MSG_USER_ID = "UserVO ID must not be null or empty";
    private static final String PRECONDITION_MSG_USER_EMAIL = "UserVO email must not be null or empty";
    private static final String PRECONDITON_MSG_USER_SALT = "UserVO salt must not be null or empty";
    private static final String PRECONDITON_MSG_USER_PASSWORD = "UserVO password must not be null or empty";

    private final UserDAO userDAO;

    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     *
     * @param userDAO to access the database and return user representations
     */
    @Autowired
    public UserComponentImpl(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<UserVO> findAll() {
        final List<UserBE> userBEList = userDAO.findAll();
        return userBEList.stream().map(UserMapper.toVO).collect(Collectors.toList());
    }

    @Override
    public UserVO findById(int id) {
        
        final UserBE result = userDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return UserMapper.toVO.apply(result);
    }

    @Override
    public UserVO create(UserVO userVO) {
        checkUserVO(userVO);

        final UserBE userBE = UserMapper.toBE.apply(userVO);
        return UserMapper.toVO.apply(userDAO.create(userBE));
    }

    @Override
    public UserVO update(UserVO userVO) {
        checkUserVO(userVO);

        final UserBE userBE = UserMapper.toBE.apply(userVO);
        return UserMapper.toVO.apply(userDAO.update(userBE));
    }

    @Override
    public void delete(UserVO userVO) {
        Preconditions.checkNotNull(userVO, PRECONDITION_MSG_USER);

        final UserBE userBE = UserMapper.toBE.apply(userVO);
        userDAO.delete(userBE);
        
    }

    private void checkUserVO(UserVO userVO){
        Preconditions.checkNotNull(userVO, PRECONDITION_MSG_USER);
        Preconditions.checkNotNull(userVO.getEmail(), PRECONDITION_MSG_USER_EMAIL);
        Preconditions.checkNotNull(userVO.getSalt(), PRECONDITON_MSG_USER_SALT);
        Preconditions.checkNotNull(userVO.getPassword(), PRECONDITON_MSG_USER_PASSWORD);
    }
}
