package de.bogenliga.application.business.user.impl.business;

import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
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
    private static final Long USERID = 4242L;
    private static final String EMAIL = "Sorscha.Kratikoff@test.de";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DsbMitgliedDAO dsbMitgliedDAO;
    @Mock
    private UserDAO userDAO;
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
        expectedBE.setDsbMitgliedUserId(USERID);

        return expectedBE;
    }

    public static UserBE getUserBE() {
        UserBE userBE = new UserBE();
        userBE.setUserId(ID);
        userBE.setUserEmail(EMAIL);
        return userBE;
    }

    @Test
    public void findById() {
        // prepare test data
        final DsbMitgliedBE expectedDsbMitgliedBE = getDsbMitgliedBE();
        final UserBE expectedUserBE = getUserBE();

        // configure mocks
        when(dsbMitgliedDAO.findByUserId(ID)).thenReturn(expectedDsbMitgliedBE);
        when(userDAO.findById(ID)).thenReturn(expectedUserBE);

        // call test method
        final UserProfileDO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedUserBE.getUserId());
    }
}
