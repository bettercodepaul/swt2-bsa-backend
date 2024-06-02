package de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.business;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types.SchuetzenstatistikWettkampftageDO;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.entity.SchuetzenstatistikWettkampfBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 *
 * @author Anna Baur
 */
public class SchuetzenstatistikWettkampfComponentImplTest {
    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long wettkampfId = 2L;
    private static final Long vereinId = 7L;
    private static final Long veranstaltungId = 1L;
    private static final String dsbMitgliedName = "Mitglied_Name";
    private static final int rueckenNummer = 5;
    private static final float wettkampftag1 = (float) 8.5;
    private static final float wettkampftag2 = (float) 9.46;
    private static final float wettkampftag3 = (float) 6.3;
    private static final float wettkampftag4 = (float) 8.2;
    private static final float wettkampftageSchnitt = (float) 8.12;




    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.dao.SchuetzenstatistikWettkampfDAO SchuetzenstatistikWettkampfDAO;

    @InjectMocks
    private SchuetzenstatistikWettkampfComponentImpl underTest;

    @Captor
    private ArgumentCaptor<SchuetzenstatistikWettkampfBE> SchuetzenstatistikWettkampfBEArgumentCaptor;

    public static SchuetzenstatistikWettkampfBE getSchuetzenstatistikWettkampfBE() {
        final SchuetzenstatistikWettkampfBE expectedSchuetzenstatistikWettkampfBE = new SchuetzenstatistikWettkampfBE();
        expectedSchuetzenstatistikWettkampfBE.setDsbMitgliedName(dsbMitgliedName);
        expectedSchuetzenstatistikWettkampfBE.setRueckenNummer(rueckenNummer);
        expectedSchuetzenstatistikWettkampfBE.setWettkampftag1(wettkampftag1);
        expectedSchuetzenstatistikWettkampfBE.setWettkampftag2(wettkampftag2);
        expectedSchuetzenstatistikWettkampfBE.setWettkampftag3(wettkampftag3);
        expectedSchuetzenstatistikWettkampfBE.setWettkampftag4(wettkampftag4);
        expectedSchuetzenstatistikWettkampfBE.setWettkampftageSchnitt(wettkampftageSchnitt);
        return expectedSchuetzenstatistikWettkampfBE;
    }


    public static SchuetzenstatistikWettkampftageDO getLigatabelleDO() {
        final SchuetzenstatistikWettkampftageDO expectedSchuetzenstatistikWettkampfDO = new SchuetzenstatistikWettkampftageDO();
        expectedSchuetzenstatistikWettkampfDO.setDsbMitgliedName(dsbMitgliedName);
        expectedSchuetzenstatistikWettkampfDO.setRueckenNummer(rueckenNummer);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag1(wettkampftag1);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag2(wettkampftag2);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag3(wettkampftag3);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag4(wettkampftag4);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftageSchnitt(wettkampftageSchnitt);


        return expectedSchuetzenstatistikWettkampfDO;
    }


    //alle Parameter ok, Test ok
    @Test
    public void getSchuetzenstatistikWettkampfVeranstaltung_allesok() {
        // prepare test data
        final SchuetzenstatistikWettkampfBE expectedSchuetzenstatistikWettkampfBE = getSchuetzenstatistikWettkampfBE();
        final List<SchuetzenstatistikWettkampfBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikWettkampfBE);

        // configure mocks
        when(SchuetzenstatistikWettkampfDAO.getSchuetzenstatistikWettkampfVeranstaltung(anyLong(), anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<SchuetzenstatistikWettkampftageDO> actual = underTest.getSchuetzenstatistikWettkampfVeranstaltung(veranstaltungId, vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();


        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getDsbMitgliedName());
        assertThat(actual.get(0).getRueckenNummer()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getRueckenNummer());
        assertThat(actual.get(0).getWettkampftag1()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getWettkampftag1());
        assertThat(actual.get(0).getWettkampftag2()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getWettkampftag2());
        assertThat(actual.get(0).getWettkampftag3()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getWettkampftag3());
        assertThat(actual.get(0).getWettkampftag4()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getWettkampftag4());
        assertThat(actual.get(0).getWettkampftageSchnitt()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getWettkampftageSchnitt());

        // verify invocations
        verify(SchuetzenstatistikWettkampfDAO)
                .getSchuetzenstatistikWettkampfVeranstaltung(veranstaltungId,vereinId);
    }

    //Input ID null -> Exception
    @Test
    public void getSchuetzenstatistikWettkampfVeranstaltung_IDnull() {
        // prepare test data
        final SchuetzenstatistikWettkampfBE expectedSchuetzenstatistikWettkampfBE = getSchuetzenstatistikWettkampfBE();
        final List<SchuetzenstatistikWettkampfBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikWettkampfBE);
        // configure mocks
        when(SchuetzenstatistikWettkampfDAO.getSchuetzenstatistikWettkampfVeranstaltung(anyLong(), anyLong())).thenReturn(null);
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getSchuetzenstatistikWettkampfVeranstaltung(anyLong(), anyLong()))
                .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Veranstaltungs-ID 0 and Verein-ID 0")
                .withNoCause();
        // assert result

        // verify invocations
        verify(SchuetzenstatistikWettkampfDAO).getSchuetzenstatistikWettkampfVeranstaltung(0L, 0L);
    }


    @Test
    public void getSchuetzenstatistikWettkampf_allesok() {
        // prepare test data
        final SchuetzenstatistikWettkampfBE expectedSchuetzenstatistikWettkampfBE = getSchuetzenstatistikWettkampfBE();
        final List<SchuetzenstatistikWettkampfBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikWettkampfBE);

        // configure mocks
        when(SchuetzenstatistikWettkampfDAO.getSchuetzenstatistikWettkampf(anyLong(), anyLong())).thenReturn(expectedBEList);


        // call test method
        final List<SchuetzenstatistikWettkampftageDO> actual = underTest.getSchuetzenstatistikWettkampf(wettkampfId,vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();


        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getDsbMitgliedName());
        assertThat(actual.get(0).getRueckenNummer()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getRueckenNummer());
        assertThat(actual.get(0).getWettkampftag1()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getWettkampftag1());
        assertThat(actual.get(0).getWettkampftag2()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getWettkampftag2());
        assertThat(actual.get(0).getWettkampftag3()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getWettkampftag3());
        assertThat(actual.get(0).getWettkampftag4()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getWettkampftag4());
        assertThat(actual.get(0).getWettkampftageSchnitt()).isEqualTo(expectedSchuetzenstatistikWettkampfBE.getWettkampftageSchnitt());

        // verify invocations
        verify(SchuetzenstatistikWettkampfDAO).getSchuetzenstatistikWettkampf(wettkampfId, vereinId);
    }

    //Input ID null -> Exception
    @Test
    public void getSchuetzenstatistikWettkampf_IDnull() {
        // prepare test data
        final SchuetzenstatistikWettkampfBE expectedSchuetzenstatistikWettkampfBE = getSchuetzenstatistikWettkampfBE();
        final List<SchuetzenstatistikWettkampfBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikWettkampfBE);

        // configure mocks
        when(SchuetzenstatistikWettkampfDAO.getSchuetzenstatistikWettkampf(anyLong(), anyLong())).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getSchuetzenstatistikWettkampf(anyLong(), anyLong()))
                .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Wettkampf-ID 0 and Verein-ID 0")
                .withNoCause();

        // assert result

        // verify invocations
        verify(SchuetzenstatistikWettkampfDAO).getSchuetzenstatistikWettkampf(0L, 0L);
    }
}
