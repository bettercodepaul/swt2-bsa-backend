package de.bogenliga.application.services.v1.schuetzenstatistikmatch.service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.SchuetzenstatistikMatchComponent;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.types.SchuetzenstatistikMatchDO;
import de.bogenliga.application.services.v1.schuetzenstatistikmatch.model.SchuetzenstatistikMatchDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Tests the SchuetzenstatistikMatchService
 *
 * @author Lennart Raach
 */
public class SchuetzenstatistikMatchServiceTest {

    private static final long USER = 4L;
    private static final Long veranstaltungId = 1L;
    private static final Long wettkampfId = 2L;
    private static final Long vereinId = 7L;
    private static final String dsbMitgliedName = "Mitglied_Name";
    private static final float pfeilpunkteSchnitt = (float) 3.7;
    private static final float match1 = 1.02f;
    private static final float match2 = 12.3f;
    private static final float match3 = 4.4f;
    private static final float match4 = 5.21f;
    private static final float match5 = 5.1f;
    private static final float match6 = 2.13f;
    private static final float match7 = 6.91f;
    private static final int rueckennummer = 5;
    
    public static SchuetzenstatistikMatchDTO getSchuetzenstatistikMatchDTO() {
        return new SchuetzenstatistikMatchDTO(
                rueckennummer,
                dsbMitgliedName,
                match1,
                match2,
                match3,
                match4,
                match5,
                match6,
                match7,
                pfeilpunkteSchnitt
        );
    }


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SchuetzenstatistikMatchComponent schuetzenstatistikMatchComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private SchuetzenstatistikMatchService underTest;

    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }

    @Test
    public void getSchuetzenstatistikVeranstaltung_ok() {
        // prepare test data
        final SchuetzenstatistikMatchDO schuetzenstatistikMatchDO = new SchuetzenstatistikMatchDO();

        final List<SchuetzenstatistikMatchDO> schuetzenstatistikMatchDOList = Collections.singletonList(schuetzenstatistikMatchDO);

        // configure mocks
        when(schuetzenstatistikMatchComponent.getSchuetzenstatistikMatchVeranstaltung(anyLong(),anyLong())).thenReturn(schuetzenstatistikMatchDOList);

        // call test method
        final List<SchuetzenstatistikMatchDTO> actual = underTest.getSchuetzenstatistikMatchVeranstaltung(veranstaltungId,vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final SchuetzenstatistikMatchDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getDsbMitgliedName()).isEqualTo(schuetzenstatistikMatchDO.getDsbMitgliedName());
        assertThat(actualDTO.getRueckennummer()).isEqualTo(schuetzenstatistikMatchDO.getRueckennummer());
        assertThat(actualDTO.getPfeilpunkteSchnitt()).isEqualTo(schuetzenstatistikMatchDO.getPfeilpunkteSchnitt());
        assertThat(actualDTO.getMatch1()).isEqualTo(schuetzenstatistikMatchDO.getMatch1());
        assertThat(actualDTO.getMatch2()).isEqualTo(schuetzenstatistikMatchDO.getMatch2());
        assertThat(actualDTO.getMatch3()).isEqualTo(schuetzenstatistikMatchDO.getMatch3());
        assertThat(actualDTO.getMatch4()).isEqualTo(schuetzenstatistikMatchDO.getMatch4());
        assertThat(actualDTO.getMatch5()).isEqualTo(schuetzenstatistikMatchDO.getMatch5());
        assertThat(actualDTO.getMatch6()).isEqualTo(schuetzenstatistikMatchDO.getMatch6());
        assertThat(actualDTO.getMatch7()).isEqualTo(schuetzenstatistikMatchDO.getMatch7());


        // verify invocations
        verify(schuetzenstatistikMatchComponent).getSchuetzenstatistikMatchVeranstaltung(veranstaltungId, vereinId);
    }

    @Test
    public void getSchuetzenstatistikMatchWettkampf() {
        // prepare test data
        final SchuetzenstatistikMatchDO schuetzenstatistikMatchDO = new SchuetzenstatistikMatchDO();

        final List<SchuetzenstatistikMatchDO> schuetzenstatistikMatchDOList = Collections.singletonList(schuetzenstatistikMatchDO);

        // configure mocks
        when(schuetzenstatistikMatchComponent.getSchuetzenstatistikMatchWettkampf(anyLong(),anyLong())).thenReturn(schuetzenstatistikMatchDOList);

        // call test method
        final List<SchuetzenstatistikMatchDTO> actual = underTest.getSchuetzenstatistikMatchWettkampf(wettkampfId,vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final SchuetzenstatistikMatchDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getDsbMitgliedName()).isEqualTo(schuetzenstatistikMatchDO.getDsbMitgliedName());
        assertThat(actualDTO.getRueckennummer()).isEqualTo(schuetzenstatistikMatchDO.getRueckennummer());
        assertThat(actualDTO.getPfeilpunkteSchnitt()).isEqualTo(schuetzenstatistikMatchDO.getPfeilpunkteSchnitt());
        assertThat(actualDTO.getMatch1()).isEqualTo(schuetzenstatistikMatchDO.getMatch1());
        assertThat(actualDTO.getMatch2()).isEqualTo(schuetzenstatistikMatchDO.getMatch2());
        assertThat(actualDTO.getMatch3()).isEqualTo(schuetzenstatistikMatchDO.getMatch3());
        assertThat(actualDTO.getMatch4()).isEqualTo(schuetzenstatistikMatchDO.getMatch4());
        assertThat(actualDTO.getMatch5()).isEqualTo(schuetzenstatistikMatchDO.getMatch5());
        assertThat(actualDTO.getMatch6()).isEqualTo(schuetzenstatistikMatchDO.getMatch6());
        assertThat(actualDTO.getMatch7()).isEqualTo(schuetzenstatistikMatchDO.getMatch7());

        // verify invocations
        verify(schuetzenstatistikMatchComponent).getSchuetzenstatistikMatchWettkampf(wettkampfId,vereinId);

    }
    @Test
    public void equalMethodSchuetzenstatistikDTOTest(){

        SchuetzenstatistikMatchDTO schuetzenstatistikMatchDTOToCompareWith = SchuetzenstatistikMatchServiceTest.getSchuetzenstatistikMatchDTO();
        SchuetzenstatistikMatchDTO schuetzenstatistikMatchDTOComparator = SchuetzenstatistikMatchServiceTest.getSchuetzenstatistikMatchDTO();

        assertThat(schuetzenstatistikMatchDTOToCompareWith.equals(schuetzenstatistikMatchDTOComparator)).isTrue();

    }
}
