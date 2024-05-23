package de.bogenliga.application.services.v1.schuetzenstatistikwettkampf.service;

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
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.SchuetzenstatistikWettkampfComponent;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types.SchuetzenstatistikWettkampftageDO;
import de.bogenliga.application.services.v1.schuetzenstatistikwettkampf.model.SchuetzenstatistikWettkampfDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Anna Baur
 */
public class SchuetzenstatistikWettkampfServiceTest {

    private static final long USER = 4L;
    private static final Long VERSION = 0L;
    private static final Long veranstaltungId = 1L;
    private static final Long wettkampfId = 2L;
    private static final int wettkampfTag = 3;
    private static final Long vereinId = 7L;
    private static final Long dsbMitgliedId = 2L;
    private static final String dsbMitgliedName = "Mitglied_Name";
    private static final int rueckenNummer = 5;
    private static final float wettkampftag1 = (float) 8.2;
    private static final float wettkampftag2 = (float) 8.6;
    private static final float wettkampftag3 = (float) 6.8;
    private static final float wettkampftag4 = (float) 7.4;
    private static final float wettkampftageSchnitt = (float) 8.2;



    public static SchuetzenstatistikWettkampftageDO getSchuetzenstatistikWettkampfDO() {
        final SchuetzenstatistikWettkampftageDO expectedSchuetzenstatistikWettkampfDO = new SchuetzenstatistikWettkampftageDO();
        expectedSchuetzenstatistikWettkampfDO.setveranstaltungId(veranstaltungId);
        expectedSchuetzenstatistikWettkampfDO.setwettkampfId(wettkampfId);
        expectedSchuetzenstatistikWettkampfDO.setwettkampfTag(wettkampfTag);
        expectedSchuetzenstatistikWettkampfDO.setvereinId(vereinId);
        expectedSchuetzenstatistikWettkampfDO.setDsbMitgliedId(dsbMitgliedId);
        expectedSchuetzenstatistikWettkampfDO.setDsbMitgliedName(dsbMitgliedName);
        expectedSchuetzenstatistikWettkampfDO.setRueckenNummer(rueckenNummer);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag1(wettkampftag1);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag2(wettkampftag2);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag3(wettkampftag3);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag4(wettkampftag4);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftageSchnitt(wettkampftageSchnitt);

        return expectedSchuetzenstatistikWettkampfDO;
    }

    public static SchuetzenstatistikWettkampfDTO getSchuetzenstatistikWettkampfDTO() {
        return new SchuetzenstatistikWettkampfDTO(
                veranstaltungId,
                wettkampfId,
                wettkampfTag,
                vereinId,
                dsbMitgliedId,
                dsbMitgliedName,
                rueckenNummer,
                wettkampftag1,
                wettkampftag2,
                wettkampftag3,
                wettkampftag4,
                wettkampftageSchnitt
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
        when(schuetzenstatistikWettkampfComponent.getSchuetzenstatistikWettkampfVeranstaltung(anyLong(),anyLong())).thenReturn(schuetzenstatistikWettkampftageDOList);

        // call test method
        final List<SchuetzenstatistikWettkampfDTO> actual = underTest.getSchuetzenstatistikWettkampfVeranstaltung(veranstaltungId,vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final SchuetzenstatistikWettkampfDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getVeranstaltungId()).isEqualTo(schuetzenstatistikWettkampftageDO.getveranstaltungId());
        assertThat(actualDTO.getWettkampfId()).isEqualTo(schuetzenstatistikWettkampftageDO.getwettkampfId());
        assertThat(actualDTO.getWettkampfTag()).isEqualTo(schuetzenstatistikWettkampftageDO.getwettkampfTag());
        assertThat(actualDTO.getVereinId()).isEqualTo(schuetzenstatistikWettkampftageDO.getvereinId());
        assertThat(actualDTO.getDsbMitgliedId()).isEqualTo(schuetzenstatistikWettkampftageDO.getDsbMitgliedId());
        assertThat(actualDTO.getDsbMitgliedName()).isEqualTo(schuetzenstatistikWettkampftageDO.getDsbMitgliedName());
        assertThat(actualDTO.getRueckenNummer()).isEqualTo(schuetzenstatistikWettkampftageDO.getRueckenNummer());
        assertThat(actualDTO.getWettkampftag1()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag1());
        assertThat(actualDTO.getWettkampftag2()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag2());
        assertThat(actualDTO.getWettkampftag3()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag3());
        assertThat(actualDTO.getWettkampftag4()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag4());
        assertThat(actualDTO.getWettkampftageSchnitt()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftageSchnitt());

        // verify invocations
        verify(schuetzenstatistikWettkampfComponent).getSchuetzenstatistikWettkampfVeranstaltung(veranstaltungId,vereinId);
    }

    @Test
    public void getSchuetzenstatistikWettkampf() {
        // prepare test data
        final SchuetzenstatistikWettkampftageDO schuetzenstatistikWettkampftageDO = new SchuetzenstatistikWettkampftageDO();

        final List<SchuetzenstatistikWettkampftageDO> schuetzenstatistikWettkampftageDOList = Collections.singletonList(schuetzenstatistikWettkampftageDO);

        // configure mocks
        when(schuetzenstatistikWettkampfComponent.getSchuetzenstatistikWettkampf(anyLong(),anyLong())).thenReturn(schuetzenstatistikWettkampftageDOList);

        // call test method
        final List<SchuetzenstatistikWettkampfDTO> actual = underTest.getSchuetzenstatistikWettkampf(wettkampfId,vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final SchuetzenstatistikWettkampfDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getVeranstaltungId()).isEqualTo(schuetzenstatistikWettkampftageDO.getveranstaltungId());
        assertThat(actualDTO.getWettkampfId()).isEqualTo(schuetzenstatistikWettkampftageDO.getwettkampfId());
        assertThat(actualDTO.getWettkampfTag()).isEqualTo(schuetzenstatistikWettkampftageDO.getwettkampfTag());
        assertThat(actualDTO.getVereinId()).isEqualTo(schuetzenstatistikWettkampftageDO.getvereinId());
        assertThat(actualDTO.getDsbMitgliedId()).isEqualTo(schuetzenstatistikWettkampftageDO.getDsbMitgliedId());
        assertThat(actualDTO.getDsbMitgliedName()).isEqualTo(schuetzenstatistikWettkampftageDO.getDsbMitgliedName());
        assertThat(actualDTO.getRueckenNummer()).isEqualTo(schuetzenstatistikWettkampftageDO.getRueckenNummer());
        assertThat(actualDTO.getWettkampftag1()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag1());
        assertThat(actualDTO.getWettkampftag2()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag2());
        assertThat(actualDTO.getWettkampftag3()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag3());
        assertThat(actualDTO.getWettkampftag4()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftag4());
        assertThat(actualDTO.getWettkampftageSchnitt()).isEqualTo(schuetzenstatistikWettkampftageDO.getWettkampftageSchnitt());

        // verify invocations
        verify(schuetzenstatistikWettkampfComponent).getSchuetzenstatistikWettkampf(wettkampfId,vereinId);

    }

    @Test
    public void equalMethodSchuetzenstatistikWettkampfDTOTest(){

        SchuetzenstatistikWettkampfDTO schuetzenstatistikWettkampfDTOToCompareWith = SchuetzenstatistikWettkampfServiceTest.getSchuetzenstatistikWettkampfDTO();
        SchuetzenstatistikWettkampfDTO schuetzenstatistikWettkampfDTOComparator = SchuetzenstatistikWettkampfServiceTest.getSchuetzenstatistikWettkampfDTO();

        assertThat(schuetzenstatistikWettkampfDTOToCompareWith.equals(schuetzenstatistikWettkampfDTOComparator)).isTrue();

    }
}
