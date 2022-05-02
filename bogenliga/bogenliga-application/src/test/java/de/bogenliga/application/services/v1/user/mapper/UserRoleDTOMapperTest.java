package de.bogenliga.application.services.v1.user.mapper;

import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.services.v1.user.model.UserRoleDTO;
import org.junit.Test;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRoleDTOMapperTest {
    private static final long ID = 1337;
    private static final String EMAIL = "Sorscha.Kratikoff@test.de";
    private static final long ROLE_ID = 7;
    private static final String ROLE_NAME = "testuser";
    private static final String DSBMITGLIED_TABLE_SURNAME = "Schmidt";
    private static final String DSBMITGLIED_TABLE_FORENAME = "Hans";

    private UserRoleDO getDO() {
        UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setId(ID);
        userRoleDO.setEmail(EMAIL);
        userRoleDO.setRoleId(ROLE_ID);
        userRoleDO.setRoleName(ROLE_NAME);
        userRoleDO.setDsbMitgliedNachname(DSBMITGLIED_TABLE_SURNAME);
        userRoleDO.setDsbMitgliedVorname(DSBMITGLIED_TABLE_FORENAME);
        return userRoleDO;
    }

    private UserRoleDTO getDTO() {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setId(ID);
        userRoleDTO.setEmail(EMAIL);
        userRoleDTO.setRoleId(ROLE_ID);
        userRoleDTO.setRoleName(ROLE_NAME);
        userRoleDTO.setDsbMitgliedNachname(DSBMITGLIED_TABLE_SURNAME);
        userRoleDTO.setDsbMitgliedVorname(DSBMITGLIED_TABLE_FORENAME);
        return userRoleDTO;
    }

    @Test
    public void toDO() {
        UserRoleDTO userRoleDTO = getDTO();
        final UserRoleDO actual = UserRoleDTOMapper.toDO.apply(userRoleDTO);
        assertThat(actual.getRoleName()).isEqualTo(ROLE_NAME);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
        assertThat(actual.equals(getDO()));
        assertThat(actual.getDsbMitgliedNachname()).isEqualTo(DSBMITGLIED_TABLE_SURNAME);
        assertThat(actual.getDsbMitgliedVorname()).isEqualTo(DSBMITGLIED_TABLE_FORENAME);
    }

    @Test
    public void toDTO() {
        UserRoleDO userRoleDO = getDO();
        final UserRoleDTO actual = UserRoleDTOMapper.toDTO.apply(userRoleDO);
        assertThat(actual.getRoleName()).isEqualTo(ROLE_NAME);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
        assertThat(actual.getDsbMitgliedNachname()).isEqualTo(DSBMITGLIED_TABLE_SURNAME);
        assertThat(actual.getDsbMitgliedVorname()).isEqualTo(DSBMITGLIED_TABLE_FORENAME);
        assertThat(actual.equals(getDTO()));
    }
}
