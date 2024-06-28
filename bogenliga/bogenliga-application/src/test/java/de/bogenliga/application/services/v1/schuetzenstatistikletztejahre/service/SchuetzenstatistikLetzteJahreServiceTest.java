package de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.service;

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
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.SchuetzenstatistikLetzteJahreComponent;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO;
import de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.model.SchuetzenstatistikLetzteJahreDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreServiceTest {

    private static final long USER = 4L;

    private static final long vereinId = (long) 7;
    private static final long veranstaltungId = (long) 1;
    private static final long sportjahr = (long) 2002;

    private static final String schuetzenname = "Name Schütze";
    private static final float sportjahr1 = (long) 8;
    private static final float sportjahr2 = (long) 7;
    private static final float sportjahr3 = (long) 6.5;
    private static final float sportjahr4 = (long) 7.8;
    private static final float sportjahr5 = (long) 8.2;
    private static final float allejahre_schnitt = (long) 7.5;

    public static SchuetzenstatistikLetzteJahreDO getSchuetzenstatistikLetzteJahreDO() {
        final SchuetzenstatistikLetzteJahreDO expectedSchuetzenstatistikLetzteJahreDO = new SchuetzenstatistikLetzteJahreDO();
        expectedSchuetzenstatistikLetzteJahreDO.setSchuetzenname(schuetzenname);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr1(sportjahr1);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr2(sportjahr2);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr3(sportjahr3);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr4(sportjahr4);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr5(sportjahr5);
        expectedSchuetzenstatistikLetzteJahreDO.setAllejahre_schnitt(allejahre_schnitt);

        return expectedSchuetzenstatistikLetzteJahreDO;
    }

    public static SchuetzenstatistikLetzteJahreDTO getSchuetzenstatistikLetzteJahreDTO() {
        return new SchuetzenstatistikLetzteJahreDTO(
                schuetzenname,
                sportjahr1,
                sportjahr2,
                sportjahr3,
                sportjahr4,
                sportjahr5,
                allejahre_schnitt
        );
    }

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SchuetzenstatistikLetzteJahreComponent schuetzenstatistikLetzteJahreComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private SchuetzenstatistikLetzteJahreService underTest;

    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }

    @Test
    public void getSchuetzenstatistikLetzteJahre_ok() {
        // prepare test data
        final SchuetzenstatistikLetzteJahreDO schuetzenstatistikLetzteJahreDO = new SchuetzenstatistikLetzteJahreDO();

        final List<SchuetzenstatistikLetzteJahreDO> schuetzenstatistikLetzteJahreDOList = Collections.singletonList(schuetzenstatistikLetzteJahreDO);

        // configure mocks
        when(schuetzenstatistikLetzteJahreComponent.getSchuetzenstatistikLetzteJahre(anyLong(), anyLong(),anyLong())).thenReturn(schuetzenstatistikLetzteJahreDOList);

        // call test method
        final List<SchuetzenstatistikLetzteJahreDTO> actual = underTest.getSchuetzenstatistikLetzteJahre(sportjahr, veranstaltungId,vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final SchuetzenstatistikLetzteJahreDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getSchuetzenname()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSchuetzenname());
        assertThat(actualDTO.getSportjahr1()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSportjahr1());
        assertThat(actualDTO.getSportjahr2()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSportjahr2());
        assertThat(actualDTO.getSportjahr3()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSportjahr3());
        assertThat(actualDTO.getSportjahr4()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSportjahr4());
        assertThat(actualDTO.getSportjahr5()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSportjahr5());
        assertThat(actualDTO.getAllejahre_schnitt()).isEqualTo(schuetzenstatistikLetzteJahreDO.getAllejahre_schnitt());

        // verify invocations
        verify(schuetzenstatistikLetzteJahreComponent).getSchuetzenstatistikLetzteJahre(sportjahr, veranstaltungId,vereinId);
    }

    @Test
    public void equalMethodSchuetzenstatistikWettkampfDTOTest(){

        SchuetzenstatistikLetzteJahreDTO schuetzenstatistikLetzteJahreDTOToCompareWith = SchuetzenstatistikLetzteJahreServiceTest.getSchuetzenstatistikLetzteJahreDTO();
        SchuetzenstatistikLetzteJahreDTO schuetzenstatistikLetzteJahreDTOComparator = SchuetzenstatistikLetzteJahreServiceTest.getSchuetzenstatistikLetzteJahreDTO();

        assertThat(schuetzenstatistikLetzteJahreDTOToCompareWith.equals(schuetzenstatistikLetzteJahreDTOComparator)).isTrue();

    }
}
