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

    private static final float pfeilpunkteSchnitt = (float) 3.7;
    private static final float match1 = 1.02f;
    private static final float match2 = 12.3f;
    private static final float match3 = 4.4f;
    private static final float match4 = 5.21f;
    private static final float match5 = 5.1f;
    private static final float match6 = 2.13f;
    private static final float match7 = 6.91f;
    private static final String dsbMitgliedName = "Mitglied_Name";
    private static final int rueckennummer = 5;
    private static final Long wettkampfId = 2L;
    private static final Long vereinId = 7L;
    private static final Long veranstaltungId = 1L;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private de.bogenliga.application.business.schuetzenstatistikmatch.impl.dao.SchuetzenstatistikMatchDAO SchuetzenstatistikMatchDAO;

    @InjectMocks
    private SchuetzenstatistikMatchComponentImpl underTest;

    public static SchuetzenstatistikMatchBE getSchuetzenstatistikMatchBE() {
        final SchuetzenstatistikMatchBE expectedSchuetzenstatistikMatchBE = new SchuetzenstatistikMatchBE();
        expectedSchuetzenstatistikMatchBE.setDsbMitgliedName(dsbMitgliedName);
        expectedSchuetzenstatistikMatchBE.setRueckennummer(rueckennummer);
        expectedSchuetzenstatistikMatchBE.setPfeilpunkteSchnitt(pfeilpunkteSchnitt);
        expectedSchuetzenstatistikMatchBE.setMatch1(match1);
        expectedSchuetzenstatistikMatchBE.setMatch1(match2);
        expectedSchuetzenstatistikMatchBE.setMatch1(match3);
        expectedSchuetzenstatistikMatchBE.setMatch1(match4);
        expectedSchuetzenstatistikMatchBE.setMatch1(match5);
        expectedSchuetzenstatistikMatchBE.setMatch1(match6);
        expectedSchuetzenstatistikMatchBE.setMatch1(match7);
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
        final List<SchuetzenstatistikMatchDO> actual = underTest.getSchuetzenstatistikMatchVeranstaltung(veranstaltungId, vereinId);

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
        verify(SchuetzenstatistikMatchDAO).getSchuetzenstatistikMatchVeranstaltung(veranstaltungId, vereinId);
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
        when(SchuetzenstatistikMatchDAO.getSchuetzenstatistikMatchWettkampf(anyLong(), anyLong())).thenReturn(expectedBEList);


        // call test method
        final List<SchuetzenstatistikMatchDO> actual = underTest.getSchuetzenstatistikMatchWettkampf(wettkampfId, vereinId);

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
        verify(SchuetzenstatistikMatchDAO).getSchuetzenstatistikMatchWettkampf(wettkampfId, vereinId);
    }

    //Input ID null -> Exception
    @Test
    public void getSchuetzenstatistikWettkampf_IDnull() {

        // configure mocks
        when(SchuetzenstatistikMatchDAO.getSchuetzenstatistikMatchWettkampf(anyLong(), anyLong())).thenReturn(null);

            // call test method
            assertThatExceptionOfType(BusinessException.class)
                    .isThrownBy(() -> underTest.getSchuetzenstatistikMatchWettkampf(anyLong(), anyLong()))
                    .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Wettkampf-ID 0 and Verein-ID 0")
                    .withNoCause();

        // assert result

        // verify invocations
        verify(SchuetzenstatistikMatchDAO).getSchuetzenstatistikMatchWettkampf(0L, 0L);
    }

}
