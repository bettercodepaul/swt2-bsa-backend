package de.bogenliga.application.business.dsbmitglied.impl.business;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class DsbMitgliedComponentImplTest {

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

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DsbMitgliedDAO dsbMitgliedDAO;
    @InjectMocks
    private DsbMitgliedComponentImpl underTest;
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


    public static DsbMitgliedDO getDsbMitgliedDO() {
        return new DsbMitgliedDO(
                ID,
                VORNAME,
                NACHNAME,
                GEBURTSDATUM,
                NATIONALITAET,
                MITGLIEDSNUMMER,
                VEREINSID,
                USERID);
    }


    @Test
    public void findAll() {
        // prepare test data
        final DsbMitgliedBE expectedBE = getDsbMitgliedBE();
        final List<DsbMitgliedBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMitgliedDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<DsbMitgliedDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getDsbMitgliedId());
        assertThat(actual.get(0).getVorname())
                .isEqualTo(expectedBE.getDsbMitgliedVorname());
        assertThat(actual.get(0).getNachname())
                .isEqualTo(expectedBE.getDsbMitgliedNachname());
        assertThat(actual.get(0).getGeburtsdatum())
                .isEqualTo(expectedBE.getDsbMitgliedGeburtsdatum());
        assertThat(actual.get(0).getNationalitaet())
                .isEqualTo(expectedBE.getDsbMitgliedNationalitaet());
        assertThat(actual.get(0).getMitgliedsnummer())
                .isEqualTo(expectedBE.getDsbMitgliedMitgliedsnummer());
        assertThat(actual.get(0).getVereinsId())
                .isEqualTo(expectedBE.getDsbMitgliedVereinsId());
        assertThat(actual.get(0).getUserId())
                .isEqualTo(expectedBE.getDsbMitgliedUserId());

        // verify invocations
        verify(dsbMitgliedDAO).findAll();
    }


    @Test
    public void findById() {
        // prepare test data
        final DsbMitgliedBE expectedBE = getDsbMitgliedBE();

        // configure mocks
        when(dsbMitgliedDAO.findById(ID)).thenReturn(expectedBE);

        // call test method
        final DsbMitgliedDO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getDsbMitgliedId());

        // verify invocations
        verify(dsbMitgliedDAO).findById(ID);
    }


    @Test
    public void create() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();

        final DsbMitgliedBE expectedBE = getDsbMitgliedBE();

        // configure mocks
        when(dsbMitgliedDAO.create(any(DsbMitgliedBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMitgliedDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(dsbMitgliedDAO).create(dsbMitgliedBEArgumentCaptor.capture(), anyLong());

        final DsbMitgliedBE persistedBE = dsbMitgliedBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getDsbMitgliedId())
                .isEqualTo(input.getId());
    }


    @Test
    public void create_with_mandatory_parameters() {
        // prepare test data
        final OffsetDateTime dateTime = OffsetDateTime.now();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final DsbMitgliedDO input = new DsbMitgliedDO(
                ID,
                VORNAME,
                NACHNAME,
                GEBURTSDATUM,
                NATIONALITAET,
                MITGLIEDSNUMMER,
                VEREINSID,
                dateTime,
                USER,
                VERSION);

        final DsbMitgliedBE expectedBE = new DsbMitgliedBE();
        expectedBE.setDsbMitgliedId(ID);
        expectedBE.setDsbMitgliedVorname(VORNAME);
        expectedBE.setDsbMitgliedNachname(NACHNAME);
        expectedBE.setDsbMitgliedGeburtsdatum(GEBURTSDATUM);
        expectedBE.setDsbMitgliedNationalitaet(NATIONALITAET);
        expectedBE.setDsbMitgliedMitgliedsnummer(MITGLIEDSNUMMER);
        expectedBE.setDsbMitgliedVereinsId(VEREINSID);
        expectedBE.setDsbMitgliedUserId(USERID);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setVersion(VERSION);


        // configure mocks
        when(dsbMitgliedDAO.create(any(DsbMitgliedBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMitgliedDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(dsbMitgliedDAO).create(dsbMitgliedBEArgumentCaptor.capture(), anyLong());

        final DsbMitgliedBE persistedBE = dsbMitgliedBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getDsbMitgliedId())
                .isEqualTo(input.getId());
    }


    @Test
    public void create_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMitgliedDAO);
    }


    @Test
    public void create_withoutVorname_shouldThrowException() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();
        input.setId(ID);
        input.setVorname(null);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(input, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMitgliedDAO);
    }


    @Test
    public void update() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();

        final DsbMitgliedBE expectedBE = getDsbMitgliedBE();

        // configure mocks
        when(dsbMitgliedDAO.update(any(DsbMitgliedBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMitgliedDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getNachname())
                .isEqualTo(input.getNachname());

        // verify invocations
        verify(dsbMitgliedDAO).update(dsbMitgliedBEArgumentCaptor.capture(), anyLong());

        final DsbMitgliedBE persistedBE = dsbMitgliedBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getDsbMitgliedId())
                .isEqualTo(input.getId());
        assertThat(persistedBE.getDsbMitgliedVorname())
                .isEqualTo(input.getVorname());
    }


    @Test
    public void update_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMitgliedDAO);
    }


    @Test
    public void update_withoutVorname_shouldThrowException() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();
        input.setVorname(null);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(input, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMitgliedDAO);
    }


    @Test
    public void delete() {
        // prepare test data
        final DsbMitgliedDO input = getDsbMitgliedDO();

        final DsbMitgliedBE expectedBE = getDsbMitgliedBE();

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(dsbMitgliedDAO).delete(dsbMitgliedBEArgumentCaptor.capture(), anyLong());

        final DsbMitgliedBE persistedBE = dsbMitgliedBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getDsbMitgliedId())
                .isEqualTo(input.getId());
    }


    @Test
    public void delete_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMitgliedDAO);
    }
}