package de.bogenliga.application.services.v1.role.mapper;

import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.services.v1.role.model.RoleDTO;
import org.junit.Test;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleDTOMapperTest {
    private static final long ID = 3;
    private static final String ROLENAME = "USER";
    private static final long VERSION = 7;

    private RoleDO getDO() {
        RoleDO roleDO = new RoleDO();
        roleDO.setId(ID);
        roleDO.setRoleName(ROLENAME);
        roleDO.setVersion(VERSION);
        return roleDO;
    }

    private RoleDTO getDTO() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(ID);
        roleDTO.setRoleName(ROLENAME);
        roleDTO.setVersion(VERSION);
        return roleDTO;
    }

    @Test
    public void toDO() {
        RoleDTO roleDTO = getDTO();
        final RoleDO actual = RoleDTOMapper.toDO.apply(roleDTO);
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getRoleName()).isEqualTo(ROLENAME);
        assertThat(actual.equals(getDO()));
    }

    @Test
    public void toDTO() {
        RoleDO roleDO = getDO();
        final RoleDTO actual = RoleDTOMapper.toDTO.apply(roleDO);
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getRoleName()).isEqualTo(ROLENAME);
        assertThat(actual.getVersion()).isEqualTo(VERSION);
    }
}
