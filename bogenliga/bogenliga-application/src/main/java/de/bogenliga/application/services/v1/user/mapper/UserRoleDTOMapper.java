package de.bogenliga.application.services.v1.user.mapper;

import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;
import de.bogenliga.application.services.v1.user.model.UserRoleDTO;
import de.bogenliga.application.services.v1.user.model.UserSignInDTO;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * I map the {@link UserRoleDO} object to the {@link UserRoleDTO} object
     */
    public static final Function<UserRoleDO, UserRoleDTO> toUserDTO = userRoleDO -> new UserRoleDTO(userRoleDO.getId(),
            userRoleDO.getEmail(), userRoleDO.getRoleId(), userRoleDO.getRoleName(), userRoleDO.getVersion() );


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

        return userRoleDTO;
    };



    /**
     * Constructor
     */
    private UserRoleDTOMapper() {
        // empty private constructor
    }
}
