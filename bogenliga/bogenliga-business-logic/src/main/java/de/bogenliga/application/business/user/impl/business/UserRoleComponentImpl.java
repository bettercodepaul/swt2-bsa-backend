package de.bogenliga.application.business.user.impl.business;

import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.UserRoleComponent;
import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.business.user.impl.dao.UserRoleExtDAO;
import de.bogenliga.application.business.role.impl.dao.RoleDAO;
import de.bogenliga.application.business.user.impl.entity.UserRoleBE;
import de.bogenliga.application.business.user.impl.entity.UserRoleExtBE;
import de.bogenliga.application.business.role.impl.entity.RoleBE;
import de.bogenliga.application.business.user.impl.mapper.UserRoleMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserComponent}
 */
@Component
public class UserRoleComponentImpl implements UserRoleComponent {

    private static final String PRECONDITION_MSG_USERROLE = "UserRoleDO must not be null";
    private static final String PRECONDITION_MSG_USER_ID = "UserID must not be null or negative";
    private static final String PRECONDITION_MSG_USER_EMAIL = "UserEmail must not be null or empty";
    private static final String PRECONDITION_MSG_ROLE_ID = "RoleID must not be null or negative";
    private static final String USER_ROLE_DEFAULT = "USER";
    private final UserRoleExtDAO userRoleExtDAO;

    private final RoleDAO roleDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param userRoleExtDAO  to access the database and return user role (including name, email - not IDs only)
     * @param roleDAO  to access the database and return default role
     */
    @Autowired
    public UserRoleComponentImpl(final UserRoleExtDAO userRoleExtDAO, RoleDAO roleDAO) {

        this.userRoleExtDAO = userRoleExtDAO;
        this.roleDAO = roleDAO;
    }


    @Override
    public List<UserRoleDO> findAll() {
        final List<UserRoleExtBE> userRoleExtBEList = userRoleExtDAO.findAll();
        return userRoleExtBEList.stream().map(UserRoleMapper.extToUserRoleDO).collect(Collectors.toList());
    }

    @Override
    public List<UserRoleDO> findById(final Long id) {
        Preconditions.checkNotNull(id, PRECONDITION_MSG_USERROLE);
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_USER_ID);

        final List<UserRoleExtBE> result = userRoleExtDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }
        List<UserRoleDO> userRoleDOList = new ArrayList<>();
        for(UserRoleExtBE userRoleExtBE : result){
            userRoleDOList.add(UserRoleMapper.extToUserRoleDO.apply(userRoleExtBE));
        }

        return userRoleDOList;
    }


    @Override
    public UserRoleDO findByEmail(final String email) {
        Preconditions.checkNotNullOrEmpty(email, PRECONDITION_MSG_USER_EMAIL);

        final UserRoleExtBE result = userRoleExtDAO.findByEmail(email);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for email '%s'", email));
        }

        return UserRoleMapper.extToUserRoleDO.apply(result);
    }


    // create with default role
    public UserRoleDO create(final Long userId, final Long currentUserId) {
        Preconditions.checkNotNull(userId, PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_USER_ID);

        // find the default role
        final RoleBE defaultRoleBE =  roleDAO.findByName(USER_ROLE_DEFAULT);


        final UserRoleBE result = new UserRoleBE();
        result.setUserId(userId);
        result.setRoleId(defaultRoleBE.getRoleId());

        final UserRoleBE persistedUserRoleBE = userRoleExtDAO.create(result, currentUserId);

        return UserRoleMapper.toUserRoleDO.apply(persistedUserRoleBE);
    }

    // create with role and userid
    public UserRoleDO create(final Long userId, final Long roleId, final Long currentUserId) {
        Preconditions.checkNotNull(userId, PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(roleId, PRECONDITION_MSG_ROLE_ID);
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_USER_ID);

        final UserRoleBE result = new UserRoleBE();
        result.setUserId(userId);
        result.setRoleId(roleId);

        final UserRoleBE persistedUserBE = userRoleExtDAO.create(result, currentUserId);

        return UserRoleMapper.toUserRoleDO.apply(persistedUserBE);
    }


    /**
     * Implementation for userRole update method
     * @param userRoleDOS list of userroles
     * @param currentUserId current user
     *
     * @return
     */
    public List<UserRoleDO> update(final List<UserRoleDO> userRoleDOS, final Long currentUserId) {
        Preconditions.checkNotNull(userRoleDOS, PRECONDITION_MSG_USERROLE);
        Preconditions.checkNotNull(userRoleDOS.get(0).getId(), PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(userRoleDOS.get(0).getRoleId(), PRECONDITION_MSG_ROLE_ID);
        Preconditions.checkArgument(userRoleDOS.get(0).getId() >= 0, PRECONDITION_MSG_USER_ID);
        Preconditions.checkArgument(userRoleDOS.get(0).getRoleId() >= 0, PRECONDITION_MSG_ROLE_ID);
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_USER_ID);

        List<UserRoleBE> userRoleBES = new ArrayList<>();
        for(UserRoleDO userRoleDO : userRoleDOS) {
            UserRoleBE result = new UserRoleBE();
            result.setUserId(userRoleDO.getId());
            result.setRoleId(userRoleDO.getRoleId());
            userRoleBES.add(result);
        }

        final List<UserRoleBE> persistedUserRoleBE = userRoleExtDAO.createOrUpdate(userRoleBES, currentUserId);


        List<UserRoleDO> persistedUserRoleDO = new ArrayList<>();
        for(UserRoleBE userRoleBE: persistedUserRoleBE){
            persistedUserRoleDO.add(UserRoleMapper.toUserRoleDO.apply(userRoleBE));
        }

        return persistedUserRoleDO;
    }



}
