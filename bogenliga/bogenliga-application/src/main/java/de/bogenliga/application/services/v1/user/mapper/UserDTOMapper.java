package de.bogenliga.application.services.v1.user.mapper;

import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;
import de.bogenliga.application.services.v1.user.model.UserDTO;
import de.bogenliga.application.services.v1.user.model.UserSignInDTO;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * I map the {@link UserDO} and {@link UserDTO} objects
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public final class UserDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link ConfigurationDO} object to the {@link ConfigurationDTO} object
     */
    public static final Function<UserDO, UserDTO> toUserDTO = userDO -> new UserDTO(userDO.getId(), userDO.getVersion(),
            userDO.getEmail());

    public static final Function<UserWithPermissionsDO, UserSignInDTO> toUserSignInDTO = userWithPermissionsDO -> {

        Set<UserPermission> userPermissions = Collections.emptySet();
        if (userWithPermissionsDO.getPermissions() != null) {
            userPermissions = userWithPermissionsDO.getPermissions().stream()
                    .map(UserPermission::fromValue)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }

        UserSignInDTO userSignInDTO = new UserSignInDTO(toUserDTO.apply(userWithPermissionsDO));
        userSignInDTO.setPermissions(userPermissions);

        return userSignInDTO;
    };

    /**
     * I map the {@link ConfigurationDTO} object to the {@link ConfigurationDO} object
     */
    public static final Function<UserDTO, UserDO> toDO = dto -> {

        UserDO userDO = new UserDO();
        userDO.setId(dto.getId());
        userDO.setVersion(dto.getVersion());
        userDO.setEmail(dto.getEmail());

        return userDO;
    };

    /**
     * I map the {@link ConfigurationDO} object to the {@link ConfigurationDTO} object
     */
    public static final Function<UserDO, UserDTO> toDTO = dob -> {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(dob.getId());
        userDTO.setVersion(dob.getVersion());
        userDTO.setEmail(dob.getEmail());

        return userDTO;
    };



    /**
     * Constructor
     */
    private UserDTOMapper() {
        // empty private constructor
    }
}
