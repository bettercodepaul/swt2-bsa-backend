package de.bogenliga.application.business.match.impl.business;

import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.BaseMatchTest;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.match.impl.mapper.MatchMapper;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchComponentImplTest extends BaseMatchTest {

    @Mock
    private MatchDAO matchDAO;

    @InjectMocks
    private MatchComponentImpl underTest;


    private void validateObjectList (List<MatchDO> actual) {
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();
    }


    private void assertValid(MatchBE expectedMatchBE, MatchDO actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expectedMatchBE.getId()).isEqualTo(MATCH_ID);
        assertThat(actual.getBegegnung()).isEqualTo(expectedMatchBE.getBegegnung()).isEqualTo(MATCH_BEGEGNUNG);
        assertThat(actual.getMannschaftId()).isEqualTo(expectedMatchBE.getMannschaftId()).isEqualTo(
                MATCH_MANNSCHAFT_ID);
        assertThat(actual.getWettkampfId()).isEqualTo(expectedMatchBE.getWettkampfId()).isEqualTo(MATCH_WETTKAMPF_ID);
        assertThat(actual.getMatchpunkte()).isEqualTo(expectedMatchBE.getMatchpunkte()).isEqualTo(MATCH_MATCHPUNKTE);
        assertThat(actual.getSatzpunkte()).isEqualTo(expectedMatchBE.getSatzpunkte()).isEqualTo(MATCH_SATZPUNKTE);
        assertThat(actual.getStrafPunkteSatz1()).isEqualTo(expectedMatchBE.getStrafPunkteSatz1()).isEqualTo(
                MATCH_STRAFPUNKT_SATZ_1);
        assertThat(actual.getStrafPunkteSatz2()).isEqualTo(expectedMatchBE.getStrafPunkteSatz2()).isEqualTo(
                MATCH_STRAFPUNKT_SATZ_2);
        assertThat(actual.getStrafPunkteSatz3()).isEqualTo(expectedMatchBE.getStrafPunkteSatz3()).isEqualTo(
                MATCH_STRAFPUNKT_SATZ_3);
        assertThat(actual.getStrafPunkteSatz4()).isEqualTo(expectedMatchBE.getStrafPunkteSatz4()).isEqualTo(
                MATCH_STRAFPUNKT_SATZ_4);
        assertThat(actual.getStrafPunkteSatz5()).isEqualTo(expectedMatchBE.getStrafPunkteSatz5()).isEqualTo(
                MATCH_STRAFPUNKT_SATZ_5);

        assertThat(actual.getNr()).isEqualTo(expectedMatchBE.getNr()).isEqualTo(MATCH_NR);
    }


    @Test
    public void findAll() {
        MatchBE expectedMatchBE = getMatchBE();

        final List<MatchBE> expectedBEList = Collections.singletonList(expectedMatchBE);

        // configure mocks
        when(matchDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<MatchDO> actual = underTest.findAll();

        // assert result
        validateObjectList(actual);
    }


    @Test
    public void findById() {
        MatchBE expectedMatchBE = getMatchBE();

        // configure mocks to return the expectedMatchBE when findById is called
        when(matchDAO.findById(anyLong())).thenReturn(expectedMatchBE);

        final MatchDO actual = underTest.findById(MATCH_ID);

        // assert result
        assertValid(expectedMatchBE, actual);

        verify(matchDAO).findById(MATCH_ID);
    }


    @Test
    public void findById_Null_Return() {
        when(matchDAO.findById(anyLong())).thenReturn(null);

        assertThatThrownBy(() -> {
            underTest.findById(MATCH_ID);
        }).isInstanceOf(BusinessException.class);
    }


    @Test
    public void findById_Null_Param() {
        MatchBE expectedMatchBE = getMatchBE();
        when(matchDAO.findById(any())).thenReturn(expectedMatchBE);

        assertThatThrownBy(() -> {
            underTest.findById(null);
        }).isInstanceOf(BusinessException.class);
    }


    @Test
    public void findByPk() {
        MatchBE expectedMatchBE = getMatchBE();

        // configure mocks to return the expectedMatchBE when findById is called
        when(matchDAO.findByPk(
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong()
        )).thenReturn(expectedMatchBE);

        final MatchDO actual = underTest.findByPk(
                MATCH_NR, MATCH_WETTKAMPF_ID,
                MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                MATCH_SCHEIBENNUMMER
        );

        // assert result
        assertValid(expectedMatchBE, actual);

        verify(matchDAO).findByPk(
                MATCH_NR, MATCH_WETTKAMPF_ID,
                MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                MATCH_SCHEIBENNUMMER
        );
    }


    @Test
    public void findByPk_Null_Return() {
        when(matchDAO.findByPk(
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong()
        )).thenReturn(null);

        assertThatThrownBy(() -> {
            underTest.findByPk(
                    MATCH_NR, MATCH_WETTKAMPF_ID,
                    MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                    MATCH_SCHEIBENNUMMER
            );
        }).isInstanceOf(BusinessException.class);
    }


    @Test
    public void findByWettkampfId() {
        MatchBE expectedMatchBE = getMatchBE();

        final List<MatchBE> expectedBEList = Collections.singletonList(expectedMatchBE);

        // configure mocks
        when(matchDAO.findByWettkampfId(anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<MatchDO> actual = underTest.findByWettkampfId(MATCH_WETTKAMPF_ID);

        // assert result
        validateObjectList(actual);
        verify(matchDAO).findByWettkampfId(MATCH_WETTKAMPF_ID);
    }


    @Test
    public void findByMannschaftId() {
        MatchBE expectedMatchBE = getMatchBE();

        final List<MatchBE> expectedBEList = Collections.singletonList(expectedMatchBE);

        // configure mocks
        when(matchDAO.findByMannschaftId(anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<MatchDO> actual = underTest.findByMannschaftId(MATCH_MANNSCHAFT_ID);

        // assert result
        validateObjectList(actual);
        verify(matchDAO).findByMannschaftId(MATCH_MANNSCHAFT_ID);
    }


    @Test
    public void create() {
        MatchBE expectedMatchBE = getMatchBE();
        when(matchDAO.create(any(MatchBE.class), anyLong())).thenReturn(expectedMatchBE);
        MatchDO matchDO = MatchMapper.toMatchDO.apply(expectedMatchBE);
        MatchDO actual = underTest.create(matchDO, CURRENT_USER_ID);

        // assert result
        assertValid(expectedMatchBE, actual);

        // cannot verify invocation as BE is different in component impl method (from mapper)
    }


    @Test
    public void update() {
        MatchBE expectedMatchBE = getMatchBE();
        // configure mocks to return the expectedMatchBE when findById is called
        when(matchDAO.findById(anyLong())).thenReturn(expectedMatchBE);
        when(matchDAO.update(any(MatchBE.class), anyLong())).thenReturn(expectedMatchBE);
        MatchDO matchDO = MatchMapper.toMatchDO.apply(expectedMatchBE);
        MatchDO actual = underTest.update(matchDO, CURRENT_USER_ID);

        // assert result
        assertValid(expectedMatchBE, actual);

        // cannot verify invocation as BE is different in component impl method (from mapper)
    }


    @Test
    public void delete() {
        MatchBE expectedMatchBE = getMatchBE();
        MatchDO matchDO = MatchMapper.toMatchDO.apply(expectedMatchBE);
        underTest.delete(matchDO, CURRENT_USER_ID);
    }
}