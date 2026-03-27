package de.bogenliga.application.business.schuetzenstatistik.impl.business;

import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.business.schuetzenstatistik.impl.entity.SchuetzenstatistikBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SchuetzenstatistikComponentImplTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long VERANSTALTUNG_ID = 1L;
    private static final String VERANSTALTUNG_NAME = "Name_der_Veranstaltung";
    private static final Long WETTKAMPF_ID = 2L;
    private static final int WETTKAMPF_TAG = 3;
    private static final Long MANNSCHAFT_ID = 4L;
    private static final int MANNSCHAFT_NUMMER = 9;
    private static final Long VEREIN_ID = 7L;
    private static final String VEREIN_NAME = "Name_Verein";
    private static final Long MATCH_ID = 6L;
    private static final int MATCH_NR = 2;
    private static final Long DSB_MITGLIED_ID = 2L;
    private static final String DSB_MITGLIED_NAME = "Mitglied_Name";
    private static final int RUECKEN_NUMMER = 5;
    private static final float PFEILPUNKTE_SCHNITT = (float) 3.7;
    private static final String SCHUETZE_SATZ_1 = "10,5,6,null,9,9";
    private static final String SCHUETZE_SATZ_2 = "4,10,null,null,7,8";
    private static final String SCHUETZE_SATZ_3 = "10,null,8,null,9,null";
    private static final String SCHUETZE_SATZ_4 = "null,3,8,null,10,3";
    private static final String SCHUETZE_SATZ_5 = "10,8,3,7,null,8";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private de.bogenliga.application.business.schuetzenstatistik.impl.dao.SchuetzenstatistikDAO SchuetzenstatistikDAO;

    @InjectMocks
    private SchuetzenstatistikComponentImpl underTest;

    @Captor
    private ArgumentCaptor<SchuetzenstatistikBE> SchuetzenstatistikBEArgumentCaptor;

    public static SchuetzenstatistikBE getSchuetzenstatistikBE() {
        final SchuetzenstatistikBE expectedSchuetzenstatistikBE = new SchuetzenstatistikBE();
        expectedSchuetzenstatistikBE.setVeranstaltungId(VERANSTALTUNG_ID);
        expectedSchuetzenstatistikBE.setVeranstaltungName(VERANSTALTUNG_NAME);
        expectedSchuetzenstatistikBE.setWettkampfId(WETTKAMPF_ID);
        expectedSchuetzenstatistikBE.setWettkampfTag(WETTKAMPF_TAG);
        expectedSchuetzenstatistikBE.setMannschaftId(MANNSCHAFT_ID);
        expectedSchuetzenstatistikBE.setMannschaftNummer(MANNSCHAFT_NUMMER);
        expectedSchuetzenstatistikBE.setVereinId(VEREIN_ID);
        expectedSchuetzenstatistikBE.setVereinName(VEREIN_NAME);
        expectedSchuetzenstatistikBE.setMatchId(MATCH_ID);
        expectedSchuetzenstatistikBE.setMatchNr(MATCH_NR);
        expectedSchuetzenstatistikBE.setDsbMitgliedId(DSB_MITGLIED_ID);
        expectedSchuetzenstatistikBE.setDsbMitgliedName(DSB_MITGLIED_NAME);
        expectedSchuetzenstatistikBE.setRueckenNummer(RUECKEN_NUMMER);
        expectedSchuetzenstatistikBE.setPfeilpunkteSchnitt(PFEILPUNKTE_SCHNITT);
        expectedSchuetzenstatistikBE.setschuetzeSatz1(SCHUETZE_SATZ_1);
        expectedSchuetzenstatistikBE.setschuetzeSatz2(SCHUETZE_SATZ_2);
        expectedSchuetzenstatistikBE.setschuetzeSatz3(SCHUETZE_SATZ_3);
        expectedSchuetzenstatistikBE.setschuetzeSatz4(SCHUETZE_SATZ_4);
        expectedSchuetzenstatistikBE.setschuetzeSatz5(SCHUETZE_SATZ_5);
        return expectedSchuetzenstatistikBE;
    }


    public static SchuetzenstatistikDO getLigatabelleDO() {
        final SchuetzenstatistikDO expectedSchuetzenstatistikDO = new SchuetzenstatistikDO();
        expectedSchuetzenstatistikDO.setveranstaltungId(VERANSTALTUNG_ID);
        expectedSchuetzenstatistikDO.setveranstaltungName(VERANSTALTUNG_NAME);
        expectedSchuetzenstatistikDO.setwettkampfId(WETTKAMPF_ID);
        expectedSchuetzenstatistikDO.setwettkampfTag(WETTKAMPF_TAG);
        expectedSchuetzenstatistikDO.setmannschaftId(MANNSCHAFT_ID);
        expectedSchuetzenstatistikDO.setmannschaftNummer(MANNSCHAFT_NUMMER);
        expectedSchuetzenstatistikDO.setvereinId(VEREIN_ID);
        expectedSchuetzenstatistikDO.setvereinName(VEREIN_NAME);
        expectedSchuetzenstatistikDO.setMatchId(MATCH_ID);
        expectedSchuetzenstatistikDO.setMatchNr(MATCH_NR);
        expectedSchuetzenstatistikDO.setDsbMitgliedId(DSB_MITGLIED_ID);
        expectedSchuetzenstatistikDO.setDsbMitgliedName(DSB_MITGLIED_NAME);
        expectedSchuetzenstatistikDO.setRueckenNummer(RUECKEN_NUMMER);
        expectedSchuetzenstatistikDO.setPfeilpunkteSchnitt(PFEILPUNKTE_SCHNITT);

        return expectedSchuetzenstatistikDO;
    }


    //alle Parameter ok, Test ok
    @Test
    public void getSchuetzenstatistikVeranstaltung_allesok() {
        // prepare test data
        final SchuetzenstatistikBE expectedSchuetzenstatistikBE = getSchuetzenstatistikBE();
        final List<SchuetzenstatistikBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikBE);

        // configure mocks
        when(SchuetzenstatistikDAO.getSchuetzenstatistikVeranstaltung(anyLong(), anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<SchuetzenstatistikDO> actual = underTest.getSchuetzenstatistikVeranstaltung(expectedSchuetzenstatistikBE.getVeranstaltungId(),
                expectedSchuetzenstatistikBE.getVereinId());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getveranstaltungId()).isEqualTo(expectedSchuetzenstatistikBE.getVeranstaltungId());
        assertThat(actual.get(0).getveranstaltungName()).isEqualTo(expectedSchuetzenstatistikBE.getVeranstaltungName());
        assertThat(actual.get(0).getwettkampfId()).isEqualTo(expectedSchuetzenstatistikBE.getWettkampfId());
        assertThat(actual.get(0).getwettkampfTag()).isEqualTo(expectedSchuetzenstatistikBE.getWettkampfTag());
        assertThat(actual.get(0).getmannschaftId()).isEqualTo(expectedSchuetzenstatistikBE.getMannschaftId());
        assertThat(actual.get(0).getmannschaftNummer()).isEqualTo(expectedSchuetzenstatistikBE.getMannschaftNummer());
        assertThat(actual.get(0).getvereinId()).isEqualTo(expectedSchuetzenstatistikBE.getVereinId());
        assertThat(actual.get(0).getvereinName()).isEqualTo(expectedSchuetzenstatistikBE.getVereinName());
        assertThat(actual.get(0).getMatchId()).isEqualTo(expectedSchuetzenstatistikBE.getMatchId());
        assertThat(actual.get(0).getMatchNr()).isEqualTo(expectedSchuetzenstatistikBE.getMatchNr());
        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(expectedSchuetzenstatistikBE.getDsbMitgliedId());
        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedSchuetzenstatistikBE.getDsbMitgliedName());
        assertThat(actual.get(0).getRueckenNummer()).isEqualTo(expectedSchuetzenstatistikBE.getRueckenNummer());
        assertThat(actual.get(0).getPfeilpunkteSchnitt()).isEqualTo(expectedSchuetzenstatistikBE.getPfeilpunkteSchnitt());

        // verify invocations
        verify(SchuetzenstatistikDAO)
                .getSchuetzenstatistikVeranstaltung(expectedSchuetzenstatistikBE.getVeranstaltungId(),
                        expectedSchuetzenstatistikBE.getVereinId()
                );
    }

    //Input ID null -> Exception
    @Test
    public void getSchuetzenstatistikVeranstaltung_IDnull() {
        // prepare test data
        final SchuetzenstatistikBE expectedSchuetzenstatistikBE = getSchuetzenstatistikBE();
        final List<SchuetzenstatistikBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikBE);
        // configure mocks
        when(SchuetzenstatistikDAO.getSchuetzenstatistikVeranstaltung(anyLong(), anyLong())).thenReturn(null);
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getSchuetzenstatistikVeranstaltung(anyLong(), anyLong()))
                .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Veranstaltungs-ID 0 and Verein-ID 0")
                .withNoCause();
        // assert result

        // verify invocations
        verify(SchuetzenstatistikDAO).getSchuetzenstatistikVeranstaltung(0L, 0L);
    }


    @Test
    public void getSchuetzenstatistikWettkampf_allesok() {
        // prepare test data
        final SchuetzenstatistikBE expectedSchuetzenstatistikBE = getSchuetzenstatistikBE();
        final List<SchuetzenstatistikBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikBE);

        // configure mocks
        when(SchuetzenstatistikDAO.getSchuetzenstatistikWettkampf(anyLong(), anyLong())).thenReturn(expectedBEList);


        // call test method
        final List<SchuetzenstatistikDO> actual = underTest.getSchuetzenstatistikWettkampf(expectedSchuetzenstatistikBE.getWettkampfId(),
                expectedSchuetzenstatistikBE.getVereinId());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getveranstaltungId()).isEqualTo(expectedSchuetzenstatistikBE.getVeranstaltungId());
        assertThat(actual.get(0).getveranstaltungName()).isEqualTo(expectedSchuetzenstatistikBE.getVeranstaltungName());
        assertThat(actual.get(0).getwettkampfId()).isEqualTo(expectedSchuetzenstatistikBE.getWettkampfId());
        assertThat(actual.get(0).getwettkampfTag()).isEqualTo(expectedSchuetzenstatistikBE.getWettkampfTag());
        assertThat(actual.get(0).getmannschaftId()).isEqualTo(expectedSchuetzenstatistikBE.getMannschaftId());
        assertThat(actual.get(0).getmannschaftNummer()).isEqualTo(expectedSchuetzenstatistikBE.getMannschaftNummer());
        assertThat(actual.get(0).getvereinId()).isEqualTo(expectedSchuetzenstatistikBE.getVereinId());
        assertThat(actual.get(0).getvereinName()).isEqualTo(expectedSchuetzenstatistikBE.getVereinName());
        assertThat(actual.get(0).getMatchId()).isEqualTo(expectedSchuetzenstatistikBE.getMatchId());
        assertThat(actual.get(0).getMatchNr()).isEqualTo(expectedSchuetzenstatistikBE.getMatchNr());
        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(expectedSchuetzenstatistikBE.getDsbMitgliedId());
        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedSchuetzenstatistikBE.getDsbMitgliedName());
        assertThat(actual.get(0).getRueckenNummer()).isEqualTo(expectedSchuetzenstatistikBE.getRueckenNummer());
        assertThat(actual.get(0).getPfeilpunkteSchnitt()).isEqualTo(expectedSchuetzenstatistikBE.getPfeilpunkteSchnitt());

        // verify invocations
        verify(SchuetzenstatistikDAO).getSchuetzenstatistikWettkampf(expectedSchuetzenstatistikBE.getWettkampfId(), expectedSchuetzenstatistikBE.getVereinId());
    }

    //Input ID null -> Exception
    @Test
    public void getSchuetzenstatistikWettkampf_IDnull() {
        // prepare test data
        final SchuetzenstatistikBE expectedSchuetzenstatistikBE = getSchuetzenstatistikBE();
        final List<SchuetzenstatistikBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikBE);

        // configure mocks
        when(SchuetzenstatistikDAO.getSchuetzenstatistikWettkampf(anyLong(), anyLong())).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getSchuetzenstatistikWettkampf(anyLong(), anyLong()))
                .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Wettkampf-ID 0 and Verein-ID 0")
                .withNoCause();

        // assert result

        // verify invocations
        verify(SchuetzenstatistikDAO).getSchuetzenstatistikWettkampf(0L, 0L);
    }

}

