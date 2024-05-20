package de.bogenliga.application.business.schuetzenstatistikmatch.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.schuetzenstatistikmatch.impl.entity.SchuetzenstatistikMatchBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.schuetzenstatistikmatch.impl.business.SchuetzenstatistikMatchComponentImplTest.getSchuetzenstatistikMatchBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class to test the functionality on SchuetzenstatistikMatchDAO
 *
 * @author Lennart Raach
 */
public class SchuetzenstatistikMatchDAOTest {

    // test data
    private static final Long veranstaltungId = 1L;
    private static final Long vereinId = 7L;
    private static final Long wettkampfId = 2L;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private SchuetzenstatistikMatchDAO underTest;


    @Test
    public void getSchuetzenstatistikMatchVeranstaltung_oktest() {
        final SchuetzenstatistikMatchBE expectedBE = getSchuetzenstatistikMatchBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<SchuetzenstatistikMatchBE> actual = underTest.getSchuetzenstatistikMatchVeranstaltung(veranstaltungId, vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedBE.getDsbMitgliedName());
        assertThat(actual.get(0).getPfeilpunkteSchnitt()).isEqualTo(expectedBE.getPfeilpunkteSchnitt());
        assertThat(actual.get(0).getRueckennummer()).isEqualTo(expectedBE.getRueckennummer());
        assertThat(actual.get(0).getMatch1()).isEqualTo(expectedBE.getMatch1());
        assertThat(actual.get(0).getMatch2()).isEqualTo(expectedBE.getMatch2());
        assertThat(actual.get(0).getMatch3()).isEqualTo(expectedBE.getMatch3());
        assertThat(actual.get(0).getMatch4()).isEqualTo(expectedBE.getMatch4());
        assertThat(actual.get(0).getMatch5()).isEqualTo(expectedBE.getMatch5());
        assertThat(actual.get(0).getMatch6()).isEqualTo(expectedBE.getMatch6());
        assertThat(actual.get(0).getMatch7()).isEqualTo(expectedBE.getMatch7());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void getSchuetzenstatistikMatchWettkampf_oktest() {
        final SchuetzenstatistikMatchBE expectedBE = getSchuetzenstatistikMatchBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<SchuetzenstatistikMatchBE> actual = underTest.getSchuetzenstatistikMatchWettkampf(wettkampfId, vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedBE.getDsbMitgliedName());
        assertThat(actual.get(0).getPfeilpunkteSchnitt()).isEqualTo(expectedBE.getPfeilpunkteSchnitt());
        assertThat(actual.get(0).getRueckennummer()).isEqualTo(expectedBE.getRueckennummer());
        assertThat(actual.get(0).getMatch1()).isEqualTo(expectedBE.getMatch1());
        assertThat(actual.get(0).getMatch2()).isEqualTo(expectedBE.getMatch2());
        assertThat(actual.get(0).getMatch3()).isEqualTo(expectedBE.getMatch3());
        assertThat(actual.get(0).getMatch4()).isEqualTo(expectedBE.getMatch4());
        assertThat(actual.get(0).getMatch5()).isEqualTo(expectedBE.getMatch5());
        assertThat(actual.get(0).getMatch6()).isEqualTo(expectedBE.getMatch6());
        assertThat(actual.get(0).getMatch7()).isEqualTo(expectedBE.getMatch7());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
}
