package de.bogenliga.application.business.kampfrichter.impl.dao;

import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static de.bogenliga.application.business.kampfrichter.impl.business.KampfrichterComponentImplTest.getKampfrichterBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Besmira Sopa
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class KampfrichterBasicDAOTest {

    private static final long USER = 0;

    private static final long USERID = 1337;
    private static final long WETTKAMPFID = 9999;
    private static final boolean LEITEND = false;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private KampfrichterDAO underTest;


     @Test
     public void findAll() {
     // prepare test data
     final KampfrichterBE expectedBE = getKampfrichterBE();

     // configure mocks
     when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

     // call test method
     final List<KampfrichterBE> actual = underTest.findAll();

     // assert result
     assertThat(actual)
     .isNotNull()
     .isNotEmpty()
     .hasSize(1);

     assertThat(actual.get(0)).isNotNull();

     assertThat(actual.get(0).getKampfrichterUserId())
     .isEqualTo(expectedBE.getKampfrichterUserId());
     assertThat(actual.get(0).getKampfrichterWettkampfId())
     .isEqualTo(expectedBE.getKampfrichterWettkampfId());

     // verify invocations
     verify(basicDao).selectEntityList(any(), any(), any());


     }



     @Test
     public void findById() {
     // prepare test data
     final KampfrichterBE expectedBE = new KampfrichterBE();
     expectedBE.setKampfrichterUserId(USERID);
     expectedBE.setKampfrichterWettkampfId(WETTKAMPFID);

     // configure mocks
     when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

     // call test method
     final KampfrichterBE actual = underTest.findById(USERID);

     // assert result
     assertThat(actual).isNotNull();

     assertThat(actual.getKampfrichterUserId())
     .isEqualTo(expectedBE.getKampfrichterUserId());
     assertThat(actual.getKampfrichterWettkampfId())
     .isEqualTo(expectedBE.getKampfrichterWettkampfId());


     // verify invocations
     verify(basicDao).selectSingleEntity(any(), any(), any());
     }


    @Test
    public void create() {
        // prepare test data
        final KampfrichterBE input = new KampfrichterBE();
        input.setKampfrichterUserId(USERID);
        input.setKampfrichterWettkampfId(WETTKAMPFID);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final KampfrichterBE actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getKampfrichterUserId())
                .isEqualTo(input.getKampfrichterUserId());
        assertThat(actual.getKampfrichterWettkampfId())
                .isEqualTo(input.getKampfrichterWettkampfId());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }


    @Test
    public void update() {
        // prepare test data
        final KampfrichterBE input = new KampfrichterBE();
        input.setKampfrichterUserId(USERID);
        input.setKampfrichterWettkampfId(WETTKAMPFID);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final KampfrichterBE actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getKampfrichterUserId())
                .isEqualTo(input.getKampfrichterUserId());
        assertThat(actual.getKampfrichterWettkampfId())
                .isEqualTo(input.getKampfrichterWettkampfId());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any());
    }


    @Test
    public void delete() {
        // prepare test data
        final KampfrichterBE input = new KampfrichterBE();
        input.setKampfrichterUserId(USERID);
        input.setKampfrichterWettkampfId(WETTKAMPFID);

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }
}
