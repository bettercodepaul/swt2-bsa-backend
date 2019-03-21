package de.bogenliga.application.services.v1.role.mapper;

import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;
import de.bogenliga.application.services.v1.role.model.RoleDTO;

import java.util.function.Function;


/**
 * I map the {@link RoleDO} and {@link RoleDTO} objects
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public final class RoleDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link ConfigurationDO} object to the {@link ConfigurationDTO} object
     */
    public static final Function<RoleDO, RoleDTO> toUserDTO = roleDO -> new RoleDTO(roleDO.getId(), roleDO.getVersion(),
            roleDO.getRoleName());


    /**
     * I map the {@link RoleDTO} object to the {@link RoleDO} object
     */
    public static final Function<RoleDTO, RoleDO> toDO = dto -> {

        RoleDO roleDO = new RoleDO();
        roleDO.setId(dto.getId());
        roleDO.setVersion(dto.getVersion());
        roleDO.setRoleName(dto.getRoleName());

        return roleDO;
    };

    /**
     * I map the {@link RoleDO} object to the {@link RoleDTO} object
     */
    public static final Function<RoleDO, RoleDTO> toDTO = dob -> {

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(dob.getId());
        roleDTO.setVersion(dob.getVersion());
        roleDTO.setRoleName(dob.getRoleName());

        return roleDTO;
    };



    /**
     * Constructor
     */
    private RoleDTOMapper() {
        // empty private constructor
    }
}
