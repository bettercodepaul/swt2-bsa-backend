package de.bogenliga.application.business.match.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.BaseMatchTest;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.match.impl.mapper.MatchMapper;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchDAOTest extends BaseMatchTest {

    @Mock
    private BasicDAO basicDao;

    @InjectMocks
    private MatchDAO underTest;


    private void validateObjectList (List<MatchBE> actual) {
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
    }


    @Test
    public void findById() {
        final MatchBE expectedMatchBE = getMatchBE();

        // configure mocks to return the expectedMatchBE when findById is called
        when(basicDao.selectSingleEntity(any(), any(), anyLong())).thenReturn(expectedMatchBE);

        final MatchBE actual = underTest.findById(MATCH_ID);

        // assert result
        assertValid(expectedMatchBE, actual);
    }


    @Test
    public void findByPk() {
        final MatchBE expectedMatchBE = getMatchBE();

        // configure mocks to return the expectedMatchBE when findById is called
        when(basicDao.selectSingleEntity(
                any(), any(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong())
        ).thenReturn(expectedMatchBE);

        final MatchBE actual = underTest.findByPk(
                MATCH_NR, MATCH_WETTKAMPF_ID,
                MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                MATCH_SCHEIBENNUMMER
        );

        // assert result
        assertValid(expectedMatchBE, actual);
    }


    @Test
    public void findAll() {
        final MatchBE expectedMatchBE = getMatchBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedMatchBE));

        // call test method
        final List<MatchBE> actual = underTest.findAll();

        // assert result
        validateObjectList(actual);
    }


    @Test
    public void findByWettkampfId() {
        final MatchBE expectedMatchBE = getMatchBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedMatchBE));

        // call test method
        final List<MatchBE> actual = underTest.findByWettkampfId(MATCH_WETTKAMPF_ID);

        // assert result
        validateObjectList(actual);
    }


    @Test
    public void findByMannschaftId() {
        final MatchBE expectedMatchBE = getMatchBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedMatchBE));

        // call test method
        final List<MatchBE> actual = underTest.findByMannschaftId(MATCH_MANNSCHAFT_ID);

        // assert result
        validateObjectList(actual);
    }


    @Test
    public void create() {
        final MatchBE expectedMatchBE = getMatchBE();
        when(basicDao.insertEntity(any(), any())).thenReturn(expectedMatchBE);
        MatchBE actual = underTest.create(expectedMatchBE, CURRENT_USER_ID);

        // assert result
        assertValid(expectedMatchBE, actual);
    }


    @Test
    public void update() {
        final MatchBE expectedMatchBE = getMatchBE();
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(expectedMatchBE);
        MatchBE actual = underTest.update(expectedMatchBE, CURRENT_USER_ID);

        // assert result
        assertValid(expectedMatchBE, actual);
    }


    @Test
    public void delete() {
        final MatchBE expectedMatchBE = getMatchBE();
        underTest.delete(expectedMatchBE, CURRENT_USER_ID);
    }
}