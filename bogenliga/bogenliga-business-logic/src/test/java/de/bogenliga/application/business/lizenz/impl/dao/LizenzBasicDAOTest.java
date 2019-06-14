package de.bogenliga.application.business.lizenz.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.lizenz.impl.business.LizenzComponentImplTest.getLizenzBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Rahul Pöse
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class LizenzBasicDAOTest {

    private long LIZENZID = 0;
    private String LIZENZNUMMER = "WT1234567";
    private long LIZENZREGIONID = 1;
    private long LIZENZDSBMITGLIEDID = 71;
    private String LIZENZTYP = "Liga";
    private long LIZENZDISZIPLINID = 0;
    private DsbMitgliedComponentImplTest dsbMitgliedComponentImplTest = new DsbMitgliedComponentImplTest();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private LizenzDAO underTest;


     @Test
     public void findAll() {
     // prepare test data
     final LizenzBE expectedBE = dsbMitgliedComponentImplTest.getLizenzBE();
         expectedBE.setLizenzId((long)0);
         expectedBE.setLizenznummer("WT1234567");
         expectedBE.setLizenzRegionId((long)1);
         expectedBE.setLizenzDsbMitgliedId(LIZENZDSBMITGLIEDID);
     // configure mocks
     when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

     // call test method
     final List<LizenzBE> actual = underTest.findAll();

     // assert result
     assertThat(actual)
     .isNotNull()
     .isNotEmpty()
     .hasSize(1);

     assertThat(actual.get(0)).isNotNull();

     assertThat(actual.get(0).getLizenzDisziplinId())
     .isEqualTo(expectedBE.getLizenzDisziplinId());
     assertThat(actual.get(0).getLizenzDsbMitgliedId())
     .isEqualTo(expectedBE.getLizenzDsbMitgliedId());
     assertThat(actual.get(0).getLizenzId())
     .isEqualTo(expectedBE.getLizenzId());
     assertThat(actual.get(0).getLizenznummer())
     .isEqualTo(expectedBE.getLizenznummer());
     assertThat(actual.get(0).getLizenzRegionId())
     .isEqualTo(expectedBE.getLizenzRegionId());
     assertThat(actual.get(0).getLizenztyp())
     .isEqualTo(expectedBE.getLizenztyp());

     // verify invocations
     verify(basicDao).selectEntityList(any(), any(), any());
     }



     @Test
     public void findKampfrichterLizenzByDsbMitgliedId() {
     // prepare test data
     final LizenzBE expectedBE = new LizenzBE();
     expectedBE.setLizenzId(LIZENZID);
     expectedBE.setLizenzDsbMitgliedId(LIZENZDSBMITGLIEDID);

     // configure mocks
     when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

     // call test method
     final LizenzBE actual = underTest.findKampfrichterLizenzByDsbMitgliedId((long)LIZENZDSBMITGLIEDID);

     // assert result
     assertThat(actual).isNotNull();

     assertThat(actual.getLizenzDsbMitgliedId())
     .isEqualTo(expectedBE.getLizenzDsbMitgliedId());
     assertThat(actual.getLizenzDsbMitgliedId())
     .isEqualTo(expectedBE.getLizenzDsbMitgliedId());

     // verify invocations
     verify(basicDao).selectSingleEntity(any(), any(), any());
     }

    @Test
    public void findByDsbMitgliedId() {
        // prepare test data
        final LizenzBE expectedBE = getLizenzBE();
        expectedBE.setLizenzId((long)0);
        expectedBE.setLizenznummer("WT1234567");
        expectedBE.setLizenzRegionId((long)1);
        expectedBE.setLizenzDsbMitgliedId(LIZENZDSBMITGLIEDID);

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));


        // call test method
        final List<LizenzBE> actual = underTest.findByDsbMitgliedId((long)LIZENZDSBMITGLIEDID);


        // assert result
        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getLizenzDisziplinId())
                .isEqualTo(expectedBE.getLizenzDisziplinId());
        assertThat(actual.get(0).getLizenzDsbMitgliedId())
                .isEqualTo(expectedBE.getLizenzDsbMitgliedId());
        assertThat(actual.get(0).getLizenzId())
                .isEqualTo(expectedBE.getLizenzId());
        assertThat(actual.get(0).getLizenznummer())
                .isEqualTo(expectedBE.getLizenznummer());
        assertThat(actual.get(0).getLizenzRegionId())
                .isEqualTo(expectedBE.getLizenzRegionId());
        assertThat(actual.get(0).getLizenztyp())
                .isEqualTo(expectedBE.getLizenztyp());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void create() {
        // prepare test data
        final LizenzBE input = new LizenzBE();
        input.setLizenzId(LIZENZID);
        input.setLizenzDsbMitgliedId(LIZENZDSBMITGLIEDID);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final LizenzBE actual = underTest.create(input, LIZENZID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());
        assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }


    @Test
    public void update() {
        // prepare test data
        final LizenzBE input = new LizenzBE();
        input.setLizenzId(LIZENZID);
        input.setLizenzDsbMitgliedId(LIZENZDSBMITGLIEDID);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final LizenzBE actual = underTest.update(input, LIZENZID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());
        assertThat(actual.getLizenzDsbMitgliedId())
                .isEqualTo(input.getLizenzDsbMitgliedId());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any());
    }


    @Test
    public void delete() {
        // prepare test data
        final LizenzBE input = new LizenzBE();
        input.setLizenzId(LIZENZID);
        input.setLizenzDsbMitgliedId(LIZENZDSBMITGLIEDID);

        // configure mocks

        // call test method
        underTest.delete(input, LIZENZDSBMITGLIEDID);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }
}
