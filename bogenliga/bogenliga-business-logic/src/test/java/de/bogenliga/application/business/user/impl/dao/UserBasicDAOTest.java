package de.bogenliga.application.business.user.impl.dao;

import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

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
public class UserBasicDAOTest {

    private static final long ID = 9999;
    private static final String EMAIL = "funktioniert@irgendwie.net";
    private static final long USER = 0;
    private static final long DSBID = 33;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private UserDAO underTest;


    @Test
    public void findAll() {
        // prepare test data
        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserEmail(EMAIL);

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<UserBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getUserId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.get(0).getUserEmail())
                .isEqualTo(expectedBE.getUserEmail());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }


    @Test
    public void findById() {
        // prepare test data
        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserEmail(EMAIL);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final UserBE actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getUserEmail())
                .isEqualTo(expectedBE.getUserEmail());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }

    @Test
    public void findByEmail() {
        // prepare test data
        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserEmail(EMAIL);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final UserBE actual = underTest.findByEmail(EMAIL);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getUserEmail())
                .isEqualTo(expectedBE.getUserEmail());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }

    @Test
    public void findByDsbMitgliedId() {
        // prepare test data
        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setDsb_mitglied_id(DSBID);


        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final UserBE actual = underTest.findByDsbMitgliedId(DSBID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getUserEmail())
                .isEqualTo(expectedBE.getUserEmail());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }

    @Test
    public void create() {
        // prepare test data
        final UserBE input = new UserBE();
        input.setUserId(ID);
        input.setUserEmail(EMAIL);
        input.setDsb_mitglied_id(USER);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final UserBE actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(input.getUserId());
        assertThat(actual.getUserEmail())
                .isEqualTo(input.getUserEmail());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }


    @Test
    public void update() {
        // prepare test data
        final UserBE input = new UserBE();
        input.setUserId(ID);
        input.setUserEmail(EMAIL);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final UserBE actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(input.getUserId());
        assertThat(actual.getUserEmail())
                .isEqualTo(input.getUserEmail());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any());
    }


    @Test
    public void delete() {
        // prepare test data
        final UserBE input = new UserBE();
        input.setUserId(ID);
        input.setUserEmail(EMAIL);

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }
}