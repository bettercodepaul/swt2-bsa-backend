package de.bogenliga.application.business.role.impl.business;

import de.bogenliga.application.business.role.api.RoleComponent;
import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.business.role.impl.dao.RoleDAO;
import de.bogenliga.application.business.role.impl.entity.RoleBE;
import de.bogenliga.application.business.role.impl.mapper.RoleMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link RoleComponent}
 */
@Component
public class RoleComponentImpl implements RoleComponent {

    private static final String PRECONDITION_MSG_ROLE = "RoleDO must not be null";
    private static final String PRECONDITION_MSG_ROLE_ID = "RoleDO ID must not be negative";
    private static final String PRECONDITION_MSG_ROLE_NAME = "RoleDO name must not be null or empty";
    private static final String USER_ROLE_DEFAULT = "USER";
    private final RoleDAO roleDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param roleDAO to access the database and return user representations
     */
    @Autowired
    public RoleComponentImpl(final RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }


    @Override
    public List<RoleDO> findAll() {
        final List<RoleBE> roleBEList = roleDAO.findAll();
        return roleBEList.stream().map(RoleMapper.toRoleDO).collect(Collectors.toList());
    }

    @Override
    public RoleDO findByName(final String roleName) {
        Preconditions.checkNotNullOrEmpty(roleName, PRECONDITION_MSG_ROLE_NAME);

        final RoleBE result = roleDAO.findByName(roleName);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for RoleName '%s'", roleName));
        }

        return RoleMapper.toRoleDO.apply(result);
    }

}
