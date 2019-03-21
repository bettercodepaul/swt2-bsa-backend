package de.bogenliga.application.business.role.impl.mapper;

import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.business.role.impl.entity.RoleBE;
import de.bogenliga.application.business.role.impl.mapper.RoleMapper;
import de.bogenliga.application.common.time.DateProvider;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class RoleMapperTest {


    private static final long ID = 3;
    private static final String ROLENAME = "USER";
    private static final long VERSION = 1L;
    private static final Timestamp TIMESTAMP = DateProvider.currentTimestampUtc();
    private static final long MOD_USER_ID = 321;


    @Test
    public void toDO() {
        final RoleBE roleBE = new RoleBE();
        roleBE.setRoleId(ID);
        roleBE.setRoleName(ROLENAME);
        roleBE.setVersion(VERSION);
        roleBE.setCreatedAtUtc(TIMESTAMP);
        roleBE.setCreatedByUserId(MOD_USER_ID);
        roleBE.setLastModifiedAtUtc(TIMESTAMP);
        roleBE.setLastModifiedByUserId(MOD_USER_ID);

        final RoleDO actual = RoleMapper.toRoleDO.apply(roleBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getRoleName()).isEqualTo(ROLENAME);
        assertThat(actual.getCreatedAtUtc()).isEqualTo(DateProvider.convertTimestamp(TIMESTAMP));
        assertThat(actual.getCreatedByUserId()).isEqualTo(MOD_USER_ID);
        assertThat(actual.getLastModifiedAtUtc()).isEqualTo(DateProvider.convertTimestamp(TIMESTAMP));
        assertThat(actual.getLastModifiedByUserId()).isEqualTo(MOD_USER_ID);
    }




    @Test
    public void toBE() {
        final RoleDO roleDO = new RoleDO();
        roleDO.setId(ID);
        roleDO.setRoleName(ROLENAME);
        roleDO.setCreatedAtUtc(DateProvider.convertTimestamp(TIMESTAMP));
        roleDO.setCreatedByUserId(MOD_USER_ID);
        roleDO.setVersion(VERSION);
        roleDO.setLastModifiedAtUtc(DateProvider.convertTimestamp(TIMESTAMP));
        roleDO.setLastModifiedByUserId(MOD_USER_ID);

        final RoleBE actual = RoleMapper.toUserBE.apply(roleDO);

        assertThat(actual.getRoleId()).isEqualTo(ID);
        assertThat(actual.getRoleName()).isEqualTo(ROLENAME);
        assertThat(actual.getCreatedAtUtc()).isEqualTo(TIMESTAMP);
        assertThat(actual.getCreatedByUserId()).isEqualTo(MOD_USER_ID);
        assertThat(actual.getLastModifiedAtUtc()).isEqualTo(TIMESTAMP);
        assertThat(actual.getLastModifiedByUserId()).isEqualTo(MOD_USER_ID);

    }

}