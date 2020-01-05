package de.bogenliga.application.services.v1.user.mapper;

import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.user.model.UserRoleDTO;

import java.util.function.Function;

/**
 * I map the {@link UserRoleDO} and {@link UserRoleDTO} objects
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public final class UserRoleDTOMapper implements DataTransferObjectMapper {



    /**
     * I map the {@link UserRoleDTO} object to the {@link UserRoleDO} object
     */
    public static final Function<UserRoleDTO, UserRoleDO> toDO = dto -> {

        UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setId(dto.getId());
        userRoleDO.setEmail(dto.getEmail());
        userRoleDO.setRoleId(dto.getRoleId());
        userRoleDO.setRoleName(dto.getRoleName());
        userRoleDO.setVersion(dto.getVersion());
        userRoleDO.setActive(dto.isActive());
        return userRoleDO;
    };

    /**
     * I map the {@link UserRoleDO} object to the {@link UserRoleDTO} object
     */
    public static final Function<UserRoleDO, UserRoleDTO> toDTO = dob -> {

        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setId(dob.getId());
        userRoleDTO.setEmail(dob.getEmail());
        userRoleDTO.setRoleId(dob.getRoleId());
        userRoleDTO.setRoleName(dob.getRoleName());
        userRoleDTO.setVersion(dob.getVersion());
        userRoleDTO.setActive(dob.isActive());
        return userRoleDTO;
    };



    /**
     * Constructor
     */
    private UserRoleDTOMapper() {
        // empty private constructor
    }
}
