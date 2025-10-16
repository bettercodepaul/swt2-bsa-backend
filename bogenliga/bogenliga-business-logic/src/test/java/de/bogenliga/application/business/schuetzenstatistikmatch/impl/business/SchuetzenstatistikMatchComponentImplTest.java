package de.bogenliga.application.business.schuetzenstatistikmatch.impl.business;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.types.SchuetzenstatistikMatchDO;
import de.bogenliga.application.business.schuetzenstatistikmatch.impl.entity.SchuetzenstatistikMatchBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Test class to test the functionality on SchuetzenstatistikMatchComponentImpl
 *
 * @author Lennart Raach
 */
public class SchuetzenstatistikMatchComponentImplTest {

    private static final float PFEILPUNKTE_SCHNITT = 3.7f;
    private static final float MATCH_1 = 1.02f;
    private static final float MATCH_2 = 12.3f;
    private static final float MATCH_3 = 4.4f;
    private static final float MATCH_4  = 5.21f;
    private static final float MATCH_5 = 5.1f;
    private static final float MATCH_6 = 2.13f;
    private static final float MATCH_7 = 6.91f;
    private static final Long TAG = 1L;
    private static final String DSB_MITGLIED_NAME  = "Mitglied_Name";
    private static final int RUECKENNUMMER  = 5;
    private static final Long WETTKAMPF_ID  = 2L;
    private static final Long VEREIN_ID = 7L;
    private static final Long VERANSTALTUNG_ID = 1L;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private de.bogenliga.application.business.schuetzenstatistikmatch.impl.dao.SchuetzenstatistikMatchDAO SchuetzenstatistikMatchDAO;

    @InjectMocks
    private SchuetzenstatistikMatchComponentImpl underTest;

    public static SchuetzenstatistikMatchBE getSchuetzenstatistikMatchBE() {
        final SchuetzenstatistikMatchBE expectedSchuetzenstatistikMatchBE = new SchuetzenstatistikMatchBE();
        expectedSchuetzenstatistikMatchBE.setDsbMitgliedName(DSB_MITGLIED_NAME);
        expectedSchuetzenstatistikMatchBE.setRueckennummer(RUECKENNUMMER);
        expectedSchuetzenstatistikMatchBE.setPfeilpunkteSchnitt(PFEILPUNKTE_SCHNITT);
        expectedSchuetzenstatistikMatchBE.setMatch1(MATCH_1);
        expectedSchuetzenstatistikMatchBE.setMatch1(MATCH_2);
        expectedSchuetzenstatistikMatchBE.setMatch1(MATCH_3);
        expectedSchuetzenstatistikMatchBE.setMatch1(MATCH_4);
        expectedSchuetzenstatistikMatchBE.setMatch1(MATCH_5);
        expectedSchuetzenstatistikMatchBE.setMatch1(MATCH_6);
        expectedSchuetzenstatistikMatchBE.setMatch1(MATCH_7);
        return expectedSchuetzenstatistikMatchBE;
    }

    //alle Parameter ok, Test ok
    @Test
    public void getSchuetzenstatistikVeranstaltung_allesok() {
        // prepare test data
        final SchuetzenstatistikMatchBE expectedSchuetzenstatistikMatchBE = getSchuetzenstatistikMatchBE();
        final List<SchuetzenstatistikMatchBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikMatchBE);

        // configure mocks
        when(SchuetzenstatistikMatchDAO.getSchuetzenstatistikMatchVeranstaltung(anyLong(), anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<SchuetzenstatistikMatchDO> actual = underTest.getSchuetzenstatistikMatchVeranstaltung(VERANSTALTUNG_ID, VEREIN_ID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedSchuetzenstatistikMatchBE.getDsbMitgliedName());
        assertThat(actual.get(0).getPfeilpunkteSchnitt()).isEqualTo(expectedSchuetzenstatistikMatchBE.getPfeilpunkteSchnitt());
        assertThat(actual.get(0).getRueckennummer()).isEqualTo(expectedSchuetzenstatistikMatchBE.getRueckennummer());
        assertThat(actual.get(0).getMatch1()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch1());
        assertThat(actual.get(0).getMatch2()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch2());
        assertThat(actual.get(0).getMatch3()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch3());
        assertThat(actual.get(0).getMatch4()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch4());
        assertThat(actual.get(0).getMatch5()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch5());
        assertThat(actual.get(0).getMatch6()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch6());
        assertThat(actual.get(0).getMatch7()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch7());

        // verify invocations
        verify(SchuetzenstatistikMatchDAO).getSchuetzenstatistikMatchVeranstaltung(VERANSTALTUNG_ID, VEREIN_ID);
    }

    //Input ID null -> Exception
    @Test
    public void getSchuetzenstatistikVeranstaltung_IDnull() {
        // configure mocks
        when(SchuetzenstatistikMatchDAO.getSchuetzenstatistikMatchVeranstaltung(anyLong(), anyLong())).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getSchuetzenstatistikMatchVeranstaltung(anyLong(), anyLong()))
                .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Veranstaltungs-ID 0 and Verein-ID 0")
                .withNoCause();

        // assert result

        // verify invocations
        verify(SchuetzenstatistikMatchDAO).getSchuetzenstatistikMatchVeranstaltung(0L, 0L);
    }


    @Test
    public void getSchuetzenstatistikWettkampf_allesok() {
        // prepare test data
        final SchuetzenstatistikMatchBE expectedSchuetzenstatistikMatchBE = getSchuetzenstatistikMatchBE();
        final List<SchuetzenstatistikMatchBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikMatchBE);

        // configure mocks
        when(SchuetzenstatistikMatchDAO.getSchuetzenstatistikMatchWettkampf(anyLong(), anyLong(), anyLong())).thenReturn(expectedBEList);


        // call test method
        final List<SchuetzenstatistikMatchDO> actual = underTest.getSchuetzenstatistikMatchWettkampf(WETTKAMPF_ID, VEREIN_ID, TAG);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedSchuetzenstatistikMatchBE.getDsbMitgliedName());
        assertThat(actual.get(0).getPfeilpunkteSchnitt()).isEqualTo(expectedSchuetzenstatistikMatchBE.getPfeilpunkteSchnitt());
        assertThat(actual.get(0).getRueckennummer()).isEqualTo(expectedSchuetzenstatistikMatchBE.getRueckennummer());
        assertThat(actual.get(0).getMatch1()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch1());
        assertThat(actual.get(0).getMatch2()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch2());
        assertThat(actual.get(0).getMatch3()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch3());
        assertThat(actual.get(0).getMatch4()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch4());
        assertThat(actual.get(0).getMatch5()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch5());
        assertThat(actual.get(0).getMatch6()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch6());
        assertThat(actual.get(0).getMatch7()).isEqualTo(expectedSchuetzenstatistikMatchBE.getMatch7());

        // verify invocations
        verify(SchuetzenstatistikMatchDAO).getSchuetzenstatistikMatchWettkampf(WETTKAMPF_ID, VEREIN_ID, TAG);
    }

    //Input ID null -> Exception
    @Test
    public void getSchuetzenstatistikWettkampf_IDnull() {

        // configure mocks
        when(SchuetzenstatistikMatchDAO.getSchuetzenstatistikMatchWettkampf(anyLong(), anyLong(), anyLong())).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getSchuetzenstatistikMatchWettkampf(anyLong(), anyLong(), anyLong()))
                .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Wettkampf-ID 0 and Verein-ID 0")
                .withNoCause();

        // assert result

        // verify invocations
        verify(SchuetzenstatistikMatchDAO).getSchuetzenstatistikMatchWettkampf(0L, 0L, 0L);
    }

}

