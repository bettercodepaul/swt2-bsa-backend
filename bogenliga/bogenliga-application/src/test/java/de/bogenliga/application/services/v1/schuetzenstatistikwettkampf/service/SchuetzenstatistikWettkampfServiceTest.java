package de.bogenliga.application.services.v1.schuetzenstatistikwettkampf.service;

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

import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.SchuetzenstatistikWettkampfComponent;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types.SchuetzenstatistikWettkampftageDO;
import de.bogenliga.application.services.v1.schuetzenstatistikwettkampf.model.SchuetzenstatistikWettkampfDTO;

/**
 * @author Anna Baur
 */
public class SchuetzenstatistikWettkampfServiceTest {

    private static final long USER = 4L;
    private static final Long VERANSTALTUNGID = 1L;
    private static final Long WETTKAMPFID = 2L;
    private static final Long VEREINID = 7L;
    private static final String DSBMITGLIEDNAME = "Mitglied_Name";
    private static final int RUECKENNUMMER = 5;
    private static final float WETTKAMPFTAG1 = (float) 8.2;
    private static final float WETTKAMPFTAG2 = (float) 8.6;
    private static final float WETTKAMPFTAG3 = (float) 6.8;
    private static final float WETTKAMPFTAG4 = (float) 7.4;
    private static final float WETTKAMPFTAGESCHNITT = (float) 8.2;

    public static SchuetzenstatistikWettkampftageDO getSchuetzenstatistikWettkampfDO() {
        final SchuetzenstatistikWettkampftageDO expectedSchuetzenstatistikWettkampfDO = new SchuetzenstatistikWettkampftageDO();
        expectedSchuetzenstatistikWettkampfDO.setDsbMitgliedName(DSBMITGLIEDNAME);
        expectedSchuetzenstatistikWettkampfDO.setRueckenNummer(RUECKENNUMMER);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag1(WETTKAMPFTAG1);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag2(WETTKAMPFTAG2);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag3(WETTKAMPFTAG3);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag4(WETTKAMPFTAG4);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftageSchnitt(WETTKAMPFTAGESCHNITT);

        return expectedSchuetzenstatistikWettkampfDO;
    }

    public static SchuetzenstatistikWettkampfDTO getSchuetzenstatistikWettkampfDTO() {
        return new SchuetzenstatistikWettkampfDTO(
                DSBMITGLIEDNAME,
                RUECKENNUMMER,
                WETTKAMPFTAG1,
                WETTKAMPFTAG2,
                WETTKAMPFTAG3,
                WETTKAMPFTAG4,
                WETTKAMPFTAGESCHNITT
        );
    }

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SchuetzenstatistikWettkampfComponent schuetzenstatistikWettkampfComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private SchuetzenstatistikWettkampfService underTest;

    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }

    @Test
    public void getSchuetzenstatistikWettkampfVeranstaltung_ok() {
        // prepare test data
        final SchuetzenstatistikWettkampftageDO schuetzenstatistikWettkampftageDO = new SchuetzenstatistikWettkampftageDO();

        final List<SchuetzenstatistikWettkampftageDO> schuetzenstatistikWettkampftageDOList = Collections.singletonList(schuetzenstatistikWettkampftageDO);

        // configure mocks
        when(schuetzenstatistikWettkampfComponent.getSchuetzenstatistikWettkampfVeranstaltung(anyLong(), anyLong())).thenReturn(schuetzenstatistikWettkampftageDOList);

        // call test method
        final List<SchuetzenstatistikWettkampfDTO> actual = underTest.getSchuetzenstatistikWettkampfVeranstaltung(VERANSTALTUNGID, VEREINID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final SchuetzenstatistikWettkampfDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getDsbMitgliedName()).isEqualTo(schuetzenstatistikWettkampftageDO.getDsbMitgliedName());
        assertThat(actualDTO.getRueckenNummer()).isEqualTo(schuetzenstatistikWettkampftageDO.getRueckenNummer());
        assertThat(actualDTO.getWettkampftag1()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag1());
        assertThat(actualDTO.getWettkampftag2()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag2());
        assertThat(actualDTO.getWettkampftag3()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag3());
        assertThat(actualDTO.getWettkampftag4()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag4());
        assertThat(actualDTO.getWettkampftageSchnitt()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftageSchnitt());

        // verify invocations
        verify(schuetzenstatistikWettkampfComponent).getSchuetzenstatistikWettkampfVeranstaltung(VERANSTALTUNGID, VEREINID);
    }

    @Test
    public void getSchuetzenstatistikWettkampf() {
        // prepare test data
        final SchuetzenstatistikWettkampftageDO schuetzenstatistikWettkampftageDO = new SchuetzenstatistikWettkampftageDO();

        final List<SchuetzenstatistikWettkampftageDO> schuetzenstatistikWettkampftageDOList = Collections.singletonList(schuetzenstatistikWettkampftageDO);

        // configure mocks
        when(schuetzenstatistikWettkampfComponent.getSchuetzenstatistikWettkampf(anyLong(), anyLong())).thenReturn(schuetzenstatistikWettkampftageDOList);

        // call test method
        final List<SchuetzenstatistikWettkampfDTO> actual = underTest.getSchuetzenstatistikWettkampf(WETTKAMPFID, VEREINID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final SchuetzenstatistikWettkampfDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getDsbMitgliedName()).isEqualTo(schuetzenstatistikWettkampftageDO.getDsbMitgliedName());
        assertThat(actualDTO.getRueckenNummer()).isEqualTo(schuetzenstatistikWettkampftageDO.getRueckenNummer());
        assertThat(actualDTO.getWettkampftag1()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag1());
        assertThat(actualDTO.getWettkampftag2()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag2());
        assertThat(actualDTO.getWettkampftag3()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag3());
        assertThat(actualDTO.getWettkampftag4()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag4());
        assertThat(actualDTO.getWettkampftageSchnitt()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftageSchnitt());

        // verify invocations
        verify(schuetzenstatistikWettkampfComponent).getSchuetzenstatistikWettkampf(WETTKAMPFID, VEREINID);

    }

    @Test
    public void equalMethodSchuetzenstatistikWettkampfDTOTest() {

        SchuetzenstatistikWettkampfDTO schuetzenstatistikWettkampfDTOToCompareWith = SchuetzenstatistikWettkampfServiceTest.getSchuetzenstatistikWettkampfDTO();
        SchuetzenstatistikWettkampfDTO schuetzenstatistikWettkampfDTOComparator = SchuetzenstatistikWettkampfServiceTest.getSchuetzenstatistikWettkampfDTO();

        assertThat(schuetzenstatistikWettkampfDTOToCompareWith.equals(schuetzenstatistikWettkampfDTOComparator)).isTrue();

    }
}
