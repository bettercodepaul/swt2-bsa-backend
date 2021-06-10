package de.bogenliga.application.business.user.impl.mapper;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.common.time.DateProvider;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserMapperTest {


    private static final long USER_ID = 123;
    private static final long OTHER_USER_ID = 321;
    private static final long VERSION = 1;
    private static final String EMAIL = "email";
    private static final String SALT = "salt";
    private static final String PASSWORD = "password";
    private static final Timestamp TIMESTAMP = DateProvider.currentTimestampUtc();
    private static final String PERMISSION_1 = "permission1";
    private static final String PERMISSION_2 = "permission2";

    private static final Long ID = 1337L;
    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final Date GEBURTSDATUM = Date.valueOf("1991-09-01");
    private static final String NATIONALITAET = "DE";
    private static final String MITGLIEDSNUMMER = "223344uu";
    private static final Long VEREINSID = 2L;


    @Test
    public void toDO() {
        final UserBE userBE = new UserBE();
        userBE.setUserId(USER_ID);
        userBE.setUserEmail(EMAIL);
        userBE.setUserSalt(SALT);
        userBE.setUserPassword(PASSWORD);
        userBE.setCreatedAtUtc(TIMESTAMP);
        userBE.setCreatedByUserId(OTHER_USER_ID);
        userBE.setVersion(VERSION);
        userBE.setLastModifiedAtUtc(TIMESTAMP);
        userBE.setLastModifiedByUserId(OTHER_USER_ID);

        final UserDO actual = UserMapper.toUserDO.apply(userBE);

        assertThat(actual.getId()).isEqualTo(USER_ID);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
        assertThat(actual.getCreatedAtUtc()).isEqualTo(DateProvider.convertTimestamp(TIMESTAMP));
        assertThat(actual.getCreatedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getLastModifiedAtUtc()).isEqualTo(DateProvider.convertTimestamp(TIMESTAMP));
        assertThat(actual.getLastModifiedByUserId()).isEqualTo(OTHER_USER_ID);
    }


    @Test
    public void toUserWithPermissionsDO() {
        final UserBE userBE = new UserBE();
        userBE.setUserId(USER_ID);
        userBE.setUserEmail(EMAIL);
        userBE.setUserSalt(SALT);
        userBE.setUserPassword(PASSWORD);
        userBE.setCreatedAtUtc(TIMESTAMP);
        userBE.setCreatedByUserId(OTHER_USER_ID);
        userBE.setVersion(VERSION);
        userBE.setLastModifiedAtUtc(TIMESTAMP);
        userBE.setLastModifiedByUserId(OTHER_USER_ID);

        final List<String> permissions = Arrays.asList(PERMISSION_1, PERMISSION_2);

        final UserWithPermissionsDO actual = UserMapper.toUserWithPermissionsDO.apply(userBE, permissions);

        assertThat(actual.getId()).isEqualTo(USER_ID);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
        assertThat(actual.getCreatedAtUtc()).isEqualTo(DateProvider.convertTimestamp(TIMESTAMP));
        assertThat(actual.getCreatedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getLastModifiedAtUtc()).isEqualTo(DateProvider.convertTimestamp(TIMESTAMP));
        assertThat(actual.getLastModifiedByUserId()).isEqualTo(OTHER_USER_ID);

        assertThat(actual.getPermissions()).containsAll(permissions);

    }


    @Test
    public void toBE() {
        final UserDO userDO = new UserDO();
        userDO.setId(USER_ID);
        userDO.setEmail(EMAIL);
        userDO.setCreatedAtUtc(DateProvider.convertTimestamp(TIMESTAMP));
        userDO.setCreatedByUserId(OTHER_USER_ID);
        userDO.setVersion(VERSION);
        userDO.setLastModifiedAtUtc(DateProvider.convertTimestamp(TIMESTAMP));
        userDO.setLastModifiedByUserId(OTHER_USER_ID);

        final UserBE actual = UserMapper.toUserBE.apply(userDO);

        assertThat(actual.getUserId()).isEqualTo(USER_ID);
        assertThat(actual.getUserEmail()).isEqualTo(EMAIL);
        assertThat(actual.getCreatedAtUtc()).isEqualTo(TIMESTAMP);
        assertThat(actual.getCreatedByUserId()).isEqualTo(OTHER_USER_ID);
        assertThat(actual.getLastModifiedAtUtc()).isEqualTo(TIMESTAMP);
        assertThat(actual.getLastModifiedByUserId()).isEqualTo(OTHER_USER_ID);

        assertThat(actual.getUserSalt()).isNull();
        assertThat(actual.getUserPassword()).isNull();

    }

    @Test
    public void toUserProfileDO(){

        //preparing testobjects
        final UserBE userBE = new UserBE();
        userBE.setUserId(USER_ID);
        userBE.setUserEmail(EMAIL);
        userBE.setUserSalt(SALT);
        userBE.setUserPassword(PASSWORD);
        userBE.setCreatedAtUtc(TIMESTAMP);
        userBE.setCreatedByUserId(OTHER_USER_ID);
        userBE.setVersion(VERSION);
        userBE.setLastModifiedAtUtc(TIMESTAMP);
        userBE.setLastModifiedByUserId(OTHER_USER_ID);

        final DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO();
        dsbMitgliedDO.setId(ID);
        dsbMitgliedDO.setNationalitaet(NATIONALITAET);
        dsbMitgliedDO.setMitgliedsnummer(MITGLIEDSNUMMER);
        dsbMitgliedDO.setNachname(NACHNAME);
        dsbMitgliedDO.setVorname(VORNAME);
        dsbMitgliedDO.setGeburtsdatum(GEBURTSDATUM);
        dsbMitgliedDO.setUserId(USER_ID);
        dsbMitgliedDO.setVereinsId(VEREINSID);

        //Calling test methods
        final UserProfileDO actual = UserMapper.toUserProfileDO.apply(userBE,dsbMitgliedDO);

        //assert
        assertThat(actual.getId()).isEqualTo(USER_ID);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
        assertThat(actual.getVorname()).isEqualTo(VORNAME);
        assertThat(actual.getNachname()).isEqualTo(NACHNAME);
        assertThat(actual.getGeburtsdatum()).isEqualTo(GEBURTSDATUM);
        assertThat(actual.getNationalitaet()).isEqualTo(NATIONALITAET);
        assertThat(actual.getMitgliedsnummer()).isEqualTo(MITGLIEDSNUMMER);
        assertThat(actual.getVereinsId()).isEqualTo(VEREINSID);
    }

}