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
 * & Alessa Hackh
 */
public class SchuetzenstatistikWettkampfComponentImplTest {
    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long WETTKAMPF_ID  = 2L;
    private static final Long VEREIN_ID  = 7L;
    private static final Long VERANSTALTUNG_ID = 1L;
    private static final String DSB_MITGLIED_NAME  = "Mitglied_Name";
    private static final int RUECKEN_NUMMER  = 5;
    private static final float WETTKAMPFTAG_1  = 8.5f;
    private static final float WETTKAMPFTAG_2 = 9.46f;
    private static final float WETTKAMPFTAG_3 = 6.3f;
    private static final float WETTKAMPFTAG_4= 8.2f;
    private static final float WETTKAMPFTAGE_SCHNITT  = 8.12f;
    private static final Long SPORTJAHR  = 4L;




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
        expectedSchuetzenstatistikWettkampfBE.setDsbMitgliedName(DSB_MITGLIED_NAME);
        expectedSchuetzenstatistikWettkampfBE.setRueckenNummer(RUECKEN_NUMMER);
        expectedSchuetzenstatistikWettkampfBE.setWettkampftag1(WETTKAMPFTAG_1);
        expectedSchuetzenstatistikWettkampfBE.setWettkampftag2(WETTKAMPFTAG_2);
        expectedSchuetzenstatistikWettkampfBE.setWettkampftag3(WETTKAMPFTAG_3);
        expectedSchuetzenstatistikWettkampfBE.setWettkampftag4(WETTKAMPFTAG_4);
        expectedSchuetzenstatistikWettkampfBE.setWettkampftageSchnitt(WETTKAMPFTAGE_SCHNITT);
        return expectedSchuetzenstatistikWettkampfBE;
    }


    public static SchuetzenstatistikWettkampftageDO getLigatabelleDO() {
        final SchuetzenstatistikWettkampftageDO expectedSchuetzenstatistikWettkampfDO = new SchuetzenstatistikWettkampftageDO();
        expectedSchuetzenstatistikWettkampfDO.setDsbMitgliedName(DSB_MITGLIED_NAME);
        expectedSchuetzenstatistikWettkampfDO.setRueckenNummer(RUECKEN_NUMMER);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag1(WETTKAMPFTAG_1);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag2(WETTKAMPFTAG_2);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag3(WETTKAMPFTAG_3);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftag4(WETTKAMPFTAG_4);
        expectedSchuetzenstatistikWettkampfDO.setWettkampftageSchnitt(WETTKAMPFTAGE_SCHNITT);


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
        final List<SchuetzenstatistikWettkampftageDO> actual = underTest.getSchuetzenstatistikWettkampfVeranstaltung(VERANSTALTUNG_ID, VEREIN_ID);

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
                .getSchuetzenstatistikWettkampfVeranstaltung(VERANSTALTUNG_ID,VEREIN_ID);
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
        final List<SchuetzenstatistikWettkampftageDO> actual = underTest.getSchuetzenstatistikWettkampf(WETTKAMPF_ID,VEREIN_ID);

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
        verify(SchuetzenstatistikWettkampfDAO).getSchuetzenstatistikWettkampf(WETTKAMPF_ID, VEREIN_ID);
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

    @Test
    public void getSchuetzenstatistikAlleLigen_allesok() {
        // prepare test data
        final SchuetzenstatistikWettkampfBE expectedSchuetzenstatistikWettkampfBE = getSchuetzenstatistikWettkampfBE();
        final List<SchuetzenstatistikWettkampfBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikWettkampfBE);

        // configure mocks
        when(SchuetzenstatistikWettkampfDAO.getSchuetzenstatistikAlleLigen(anyLong(), anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<SchuetzenstatistikWettkampftageDO> actual = underTest.getSchuetzenstatistikAlleLigen(SPORTJAHR, VEREIN_ID);

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
                .getSchuetzenstatistikAlleLigen(SPORTJAHR,VEREIN_ID);
    }

    @Test
    public void getSchuetzenstatistikAlleLigen_IDnull() {
        // prepare test data
        final SchuetzenstatistikWettkampfBE expectedSchuetzenstatistikWettkampfBE = getSchuetzenstatistikWettkampfBE();
        final List<SchuetzenstatistikWettkampfBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikWettkampfBE);

        // configure mocks
        when(SchuetzenstatistikWettkampfDAO.getSchuetzenstatistikAlleLigen(anyLong(), anyLong())).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getSchuetzenstatistikAlleLigen(anyLong(), anyLong()))
                .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Sportjahr 0")
                .withNoCause();

        // assert result

        // verify invocations
        verify(SchuetzenstatistikWettkampfDAO).getSchuetzenstatistikAlleLigen(0L, 0L);
    }

}
