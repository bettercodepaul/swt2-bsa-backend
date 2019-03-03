package de.bogenliga.application.business.user.impl.mapper;

import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.business.user.impl.entity.UserRoleBE;
import de.bogenliga.application.business.user.impl.entity.UserRoleExtBE;
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
public class UserRoleMapperTest {


    private static final long USER_ID = 123;
    private static final long OTHER_USER_ID = 789;
    private static final String EMAIL = "email";
    private static final long ROLE_ID = 3;
    private static final String ROLE_NAME = "TESTUSER";
    private static final Timestamp TIMESTAMP = DateProvider.currentTimestampUtc();
    private static final long VERSION = 7;


    @Test
    public void extToURDO() {
        final UserRoleExtBE userRoleExtBE = new UserRoleExtBE();
        userRoleExtBE.setUserId(USER_ID);
        userRoleExtBE.setUserEmail(EMAIL);
        userRoleExtBE.setRoleId(ROLE_ID);
        userRoleExtBE.setRoleName(ROLE_NAME);
        userRoleExtBE.setCreatedAtUtc(TIMESTAMP);
        userRoleExtBE.setCreatedByUserId(OTHER_USER_ID);
        userRoleExtBE.setVersion(VERSION);
        userRoleExtBE.setLastModifiedAtUtc(TIMESTAMP);
        userRoleExtBE.setLastModifiedByUserId(OTHER_USER_ID);

        final UserRoleDO actual = UserRoleMapper.extToUserRoleDO.apply(userRoleExtBE);

        assertThat(actual.getId()).isEqualTo(USER_ID);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
        assertThat(actual.getRoleId()).isEqualTo(ROLE_ID);
        assertThat(actual.getRoleName()).isEqualTo(ROLE_NAME);
        assertThat(actual.getCreatedAtUtc()).isEqualTo(DateProvider.convertTimestamp(TIMESTAMP));
        assertThat(actual.getCreatedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getLastModifiedAtUtc()).isEqualTo(DateProvider.convertTimestamp(TIMESTAMP));
        assertThat(actual.getLastModifiedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getVersion()).isEqualTo(VERSION);
    }

    @Test
    public void toURDO() {
        final UserRoleBE userRoleBE = new UserRoleBE();
        userRoleBE.setUserId(USER_ID);
        userRoleBE.setRoleId(ROLE_ID);
        userRoleBE.setCreatedAtUtc(TIMESTAMP);
        userRoleBE.setCreatedByUserId(OTHER_USER_ID);
        userRoleBE.setVersion(VERSION);
        userRoleBE.setLastModifiedAtUtc(TIMESTAMP);
        userRoleBE.setLastModifiedByUserId(OTHER_USER_ID);

        final UserRoleDO actual = UserRoleMapper.toUserRoleDO.apply(userRoleBE);

        assertThat(actual.getId()).isEqualTo(USER_ID);
        assertThat(actual.getRoleId()).isEqualTo(ROLE_ID);
        assertThat(actual.getCreatedAtUtc()).isEqualTo(DateProvider.convertTimestamp(TIMESTAMP));
        assertThat(actual.getCreatedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getLastModifiedAtUtc()).isEqualTo(DateProvider.convertTimestamp(TIMESTAMP));
        assertThat(actual.getLastModifiedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getVersion()).isEqualTo(VERSION);
        assertThat(actual.getEmail()).isNull();
        assertThat(actual.getRoleName()).isNull();
    }



    @Test
    public void toURBE() {
        final UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setId(USER_ID);
        userRoleDO.setRoleId(ROLE_ID);
        userRoleDO.setCreatedAtUtc(DateProvider.convertTimestamp(TIMESTAMP));
        userRoleDO.setCreatedByUserId(OTHER_USER_ID);
        userRoleDO.setVersion(VERSION);
        userRoleDO.setLastModifiedAtUtc(DateProvider.convertTimestamp(TIMESTAMP));
        userRoleDO.setLastModifiedByUserId(OTHER_USER_ID);

        final UserRoleBE actual = UserRoleMapper.toUserRoleBE.apply(userRoleDO);

        assertThat(actual.getUserId()).isEqualTo(USER_ID);
        assertThat(actual.getRoleId()).isEqualTo(ROLE_ID);
        assertThat(actual.getCreatedAtUtc()).isEqualTo(TIMESTAMP);
        assertThat(actual.getCreatedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getLastModifiedAtUtc()).isEqualTo(TIMESTAMP);
        assertThat(actual.getLastModifiedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getVersion()).isEqualTo(VERSION);
    }

    @Test
    public void toURExtBE() {
        final UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setId(USER_ID);
        userRoleDO.setEmail(EMAIL);
        userRoleDO.setRoleId(ROLE_ID);
        userRoleDO.setRoleName(ROLE_NAME);
        userRoleDO.setCreatedAtUtc(DateProvider.convertTimestamp(TIMESTAMP));
        userRoleDO.setCreatedByUserId(OTHER_USER_ID);
        userRoleDO.setVersion(VERSION);
        userRoleDO.setLastModifiedAtUtc(DateProvider.convertTimestamp(TIMESTAMP));
        userRoleDO.setLastModifiedByUserId(OTHER_USER_ID);

        final UserRoleExtBE actual = UserRoleMapper.toUserRoleExtBE.apply(userRoleDO);

        assertThat(actual.getUserId()).isEqualTo(USER_ID);
        assertThat(actual.getUserEmail()).isEqualTo(EMAIL);
        assertThat(actual.getRoleId()).isEqualTo(ROLE_ID);
        assertThat(actual.getRoleName()).isEqualTo(ROLE_NAME);
        assertThat(actual.getCreatedAtUtc()).isEqualTo(TIMESTAMP);
        assertThat(actual.getCreatedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getLastModifiedAtUtc()).isEqualTo(TIMESTAMP);
        assertThat(actual.getLastModifiedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getVersion()).isEqualTo(VERSION);
    }

}