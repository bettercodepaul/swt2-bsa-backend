package de.bogenliga.application.business.dsbmitglied.impl.dao;

import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest.getDsbMitgliedBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
public class DsbMitgliedBasicDAOTest {

    private static final long USER = 0;

    private static final long ID = 1337;
    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final String GEBURTSDATUM = "1.9.1991";
    private static final String NATIONALITAET = "DE";
    private static final String MITGLIEDSNUMMER = "223344uu";
    private static final long VEREINSID = 2;
    private static final long USERID = 4242;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private DsbMitgliedDAO underTest;


    @Test
    public void findAll() {
        // prepare test data
        final DsbMitgliedBE expectedBE = getDsbMitgliedBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<DsbMitgliedBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getDsbMitgliedId())
                .isEqualTo(expectedBE.getDsbMitgliedId());
        assertThat(actual.get(0).getDsbMitgliedVorname())
                .isEqualTo(expectedBE.getDsbMitgliedVorname());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }


    @Test
    public void findById() {
        // prepare test data
        final DsbMitgliedBE expectedBE = new DsbMitgliedBE();
        expectedBE.setDsbMitgliedId(ID);
        expectedBE.setDsbMitgliedVorname(VORNAME);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final DsbMitgliedBE actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getDsbMitgliedId())
                .isEqualTo(expectedBE.getDsbMitgliedId());
        assertThat(actual.getDsbMitgliedVorname())
                .isEqualTo(expectedBE.getDsbMitgliedVorname());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }


    @Test
    public void create() {
        // prepare test data
        final DsbMitgliedBE input = new DsbMitgliedBE();
        input.setDsbMitgliedId(ID);
        input.setDsbMitgliedVorname(VORNAME);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final DsbMitgliedBE actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getDsbMitgliedId())
                .isEqualTo(input.getDsbMitgliedId());
        assertThat(actual.getDsbMitgliedVorname())
                .isEqualTo(input.getDsbMitgliedVorname());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }


    @Test
    public void update() {
        // prepare test data
        final DsbMitgliedBE input = new DsbMitgliedBE();
        input.setDsbMitgliedId(ID);
        input.setDsbMitgliedVorname(VORNAME);
        input.setDsbMitgliedUserId(USERID);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final DsbMitgliedBE actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getDsbMitgliedId())
                .isEqualTo(input.getDsbMitgliedId());
        assertThat(actual.getDsbMitgliedVorname())
                .isEqualTo(input.getDsbMitgliedVorname());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any());
    }


    @Test
    public void delete() {
        // prepare test data
        final DsbMitgliedBE input = new DsbMitgliedBE();
        input.setDsbMitgliedId(ID);
        input.setDsbMitgliedVorname(VORNAME);

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }
}