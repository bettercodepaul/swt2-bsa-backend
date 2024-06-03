package de.bogenliga.application.business.user.impl.business;

import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Date;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

public class UserProfileComponentImplTest {
    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long ID = 1337L;
    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final Date GEBURTSDATUM = Date.valueOf("1991-09-01");
    private static final String NATIONALITAET = "DE";
    private static final String MITGLIEDSNUMMER = "223344uu";
    private static final Long VEREINSID = 2L;
    private static final String VEREINNAME = "TEST VEREIN";
    private static final Long USERID = 4242L;
    private static final String EMAIL = "Sorscha.Kratikoff@test.de";

    private static final Boolean KAMPFRICHTER = true;
    private static final Date BEITRITTSDATUM = Date.valueOf("2001-01-01");

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private UserDAO userDAO;
    @Mock
    private DsbMitgliedComponent dsbMitgliedComponent;
    @InjectMocks
    private UserProfileComponentImpl underTest;
    @Captor
    private ArgumentCaptor<DsbMitgliedBE> dsbMitgliedBEArgumentCaptor;

    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static DsbMitgliedBE getDsbMitgliedBE() {
        final DsbMitgliedBE expectedBE = new DsbMitgliedBE();
        expectedBE.setDsbMitgliedId(ID);
        expectedBE.setDsbMitgliedVorname(VORNAME);
        expectedBE.setDsbMitgliedNachname(NACHNAME);
        expectedBE.setDsbMitgliedGeburtsdatum(GEBURTSDATUM);
        expectedBE.setDsbMitgliedNationalitaet(NATIONALITAET);
        expectedBE.setDsbMitgliedMitgliedsnummer(MITGLIEDSNUMMER);
        expectedBE.setDsbMitgliedVereinsId(VEREINSID);
        expectedBE.setDsbMitgliedVereinName(VEREINNAME);
        expectedBE.setDsbMitgliedUserId(USERID);
        expectedBE.setDsbMitgliedBeitrittsdatum(BEITRITTSDATUM);

        return expectedBE;
    }

    public static DsbMitgliedDO getDsbMitgliedDO() {
        final DsbMitgliedDO expectedDO = new DsbMitgliedDO(ID, VORNAME, NACHNAME, GEBURTSDATUM, NATIONALITAET, MITGLIEDSNUMMER, VEREINSID, VEREINNAME, USERID, KAMPFRICHTER, BEITRITTSDATUM);

        return expectedDO;
    }

    public static UserBE getUserBE() {
        UserBE userBE = new UserBE();
        userBE.setUserId(USERID);
        userBE.setUserEmail(EMAIL);
        userBE.setDsbMitgliedId(ID);
        return userBE;
    }

    @Test
    public void findById() {

        // prepare test data
        final DsbMitgliedDO expectedDsbMitgliedDO = getDsbMitgliedDO();
        final UserBE expectedUserBE = getUserBE();

        // configure mocks
        when(dsbMitgliedComponent.findById(ID)).thenReturn(expectedDsbMitgliedDO);
        when(userDAO.findById(USERID)).thenReturn(expectedUserBE);

        // call test method
        final UserProfileDO actual = underTest.findById(USERID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedDsbMitgliedDO.getUserId());
        assertThat(actual.getVorname())
                .isEqualTo(expectedDsbMitgliedDO.getVorname());
        assertThat(actual.getNachname())
                .isEqualTo(expectedDsbMitgliedDO.getNachname());
        assertThat(actual.getGeburtsdatum())
                .isEqualTo(expectedDsbMitgliedDO.getGeburtsdatum());
        assertThat(actual.getNationalitaet())
                .isEqualTo(expectedDsbMitgliedDO.getNationalitaet());
        assertThat(actual.getMitgliedsnummer())
                .isEqualTo(expectedDsbMitgliedDO.getMitgliedsnummer());
        assertThat(actual.getVereinsId())
                .isEqualTo(expectedDsbMitgliedDO.getVereinsId());
    }
}
