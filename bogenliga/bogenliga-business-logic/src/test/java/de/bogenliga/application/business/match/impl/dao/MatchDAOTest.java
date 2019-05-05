package de.bogenliga.application.business.match.impl.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.baseClass.impl.BasicBETest;
import de.bogenliga.application.business.match.impl.BaseMatchTest;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.Mockito.*;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchDAOTest extends BaseMatchTest {

    @Mock
    private BasicDAO basicDao;

    @InjectMocks
    private MatchDAO underTest;

    private MatchBE expectedBE;

    // Implements generic way to test business entities methods
    private BasicBETest<MatchBE> basicDAOTest;


    @Before
    public void testSetup() {
        basicDAOTest = new BasicBETest<>();

        expectedBE = getMatchBE();
        basicDAOTest.setBE(expectedBE);

        // configure mocks

        // find by id
        when(basicDao.selectSingleEntity(any(), any(), anyLong())).thenReturn(expectedBE);
        // find by pk
        when(basicDao.selectSingleEntity(any(), any(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(expectedBE);
        // find by match/wettkampf id
        when(basicDao.selectEntityList(any(), any(), anyLong())).thenReturn(Collections.singletonList(expectedBE));
        // find all
        when(basicDao.selectEntityList(any(), any())).thenReturn(Collections.singletonList(expectedBE));
    }

/*
    private void validateObjectList(List<MatchBE> actual) {
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();
    }


    private void assertValid(MatchBE expectedMatchBE, MatchBE actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expectedMatchBE.getId()).isEqualTo(MATCH_ID);
        assertThat(actual.getBegegnung()).isEqualTo(expectedMatchBE.getBegegnung()).isEqualTo(MATCH_BEGEGNUNG);
        assertThat(actual.getMannschaftId()).isEqualTo(expectedMatchBE.getMannschaftId()).isEqualTo(
                MATCH_MANNSCHAFT_ID);
        assertThat(actual.getWettkampfId()).isEqualTo(expectedMatchBE.getWettkampfId()).isEqualTo(MATCH_WETTKAMPF_ID);
        assertThat(actual.getMatchpunkte()).isEqualTo(expectedMatchBE.getMatchpunkte()).isEqualTo(MATCH_MATCHPUNKTE);
        assertThat(actual.getSatzpunkte()).isEqualTo(expectedMatchBE.getSatzpunkte()).isEqualTo(MATCH_SATZPUNKTE);
        assertThat(actual.getNr()).isEqualTo(expectedMatchBE.getNr()).isEqualTo(MATCH_NR);
    }*/


    @Test
    public void findById() {
        basicDAOTest.testMethod(underTest.findById(MATCH_ID));
    }


    @Test
    public void findByPk() {
        basicDAOTest.testMethod(underTest.findByPk(
                MATCH_NR, MATCH_WETTKAMPF_ID,
                MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                MATCH_SCHEIBENNUMMER
        ));
    }


    @Test
    public void findAll() {
        basicDAOTest.testMethod(underTest.findAll());
    }


    @Test
    public void findByWettkampfId() {
        basicDAOTest.testMethod(underTest.findByWettkampfId(MATCH_WETTKAMPF_ID));
    }


    @Test
    public void findByMannschaftId() {
        basicDAOTest.testMethod(underTest.findByMannschaftId(MATCH_MANNSCHAFT_ID));
    }


}