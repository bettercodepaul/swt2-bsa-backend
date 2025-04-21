package de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.SchuetzenstatistikLetzteJahreComponent;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO;
import de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.model.SchuetzenstatistikLetzteJahreDTO;

/**
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreServiceTest {

    private static final long USER = 4L;

    private static final long VEREINID = (long) 7;
    private static final long VERANSTALTUNGID = (long) 1;
    private static final long SPORTJAHR = (long) 2002;

    private static final String SCHUETZENNAME = "Name Schütze";
    private static final float SPORTJAHR1 = (long) 8;
    private static final float SPORTJAHR2 = (long) 7;
    private static final float SPORTJAHR3 = (long) 6.5;
    private static final float SPORTJAHR4 = (long) 7.8;
    private static final float SPORTJAHR5 = (long) 8.2;
    private static final float ALLEJAHRE_SCHNITT = (long) 7.5;

    public static SchuetzenstatistikLetzteJahreDO getSchuetzenstatistikLetzteJahreDO() {
        final SchuetzenstatistikLetzteJahreDO expectedSchuetzenstatistikLetzteJahreDO = new SchuetzenstatistikLetzteJahreDO();
        expectedSchuetzenstatistikLetzteJahreDO.setSchuetzenname(SCHUETZENNAME);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr1(SPORTJAHR1);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr2(SPORTJAHR2);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr3(SPORTJAHR3);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr4(SPORTJAHR4);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr5(SPORTJAHR5);
        expectedSchuetzenstatistikLetzteJahreDO.setAllejahre_schnitt(ALLEJAHRE_SCHNITT);

        return expectedSchuetzenstatistikLetzteJahreDO;
    }

    public static SchuetzenstatistikLetzteJahreDTO getSchuetzenstatistikLetzteJahreDTO() {
        return new SchuetzenstatistikLetzteJahreDTO(
                SCHUETZENNAME,
                SPORTJAHR1,
                SPORTJAHR2,
                SPORTJAHR3,
                SPORTJAHR4,
                SPORTJAHR5,
                ALLEJAHRE_SCHNITT
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
        final List<SchuetzenstatistikLetzteJahreDTO> actual = underTest.getSchuetzenstatistikLetzteJahre(SPORTJAHR, VERANSTALTUNGID, VEREINID);

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
        verify(schuetzenstatistikLetzteJahreComponent).getSchuetzenstatistikLetzteJahre(SPORTJAHR, VERANSTALTUNGID, VEREINID);
    }

    @Test
    public void equalMethodSchuetzenstatistikWettkampfDTOTest(){

        SchuetzenstatistikLetzteJahreDTO schuetzenstatistikLetzteJahreDTOToCompareWith = SchuetzenstatistikLetzteJahreServiceTest.getSchuetzenstatistikLetzteJahreDTO();
        SchuetzenstatistikLetzteJahreDTO schuetzenstatistikLetzteJahreDTOComparator = SchuetzenstatistikLetzteJahreServiceTest.getSchuetzenstatistikLetzteJahreDTO();

        assertThat(schuetzenstatistikLetzteJahreDTOToCompareWith.equals(schuetzenstatistikLetzteJahreDTOComparator)).isTrue();

    }
}
