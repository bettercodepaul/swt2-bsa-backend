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

    private static final Long veranstaltungId = 1L;
    private static final String veranstaltungName = "Name_der_Veranstaltung";
    private static final Long wettkampfId = 2L;
    private static final int wettkampfTag = 3;
    private static final Long mannschaftId = 4L;
    private static final int mannschaftNummer = 9;
    private static final Long vereinId = 7L;
    private static final String vereinName = "Name_Verein";
    private static final Long matchId = 6L;
    private static final int matchNr = 2;
    private static final Long dsbMitgliedId = 2L;
    private static final String dsbMitgliedName = "Mitglied_Name";
    private static final int rueckenNummer = 5;
    private static final float pfeilpunkteSchnitt = (float) 3.7;
    private static final String schuetzeSatz1 = "10,5,6,null,9,9";
    private static final String schuetzeSatz2 = "4,10,null,null,7,8";
    private static final String schuetzeSatz3 = "10,null,8,null,9,null";
    private static final String schuetzeSatz4 = "null,3,8,null,10,3";
    private static final String schuetzeSatz5 = "10,8,3,7,null,8";

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
        expectedSchuetzenstatistikBE.setVeranstaltungId(veranstaltungId);
        expectedSchuetzenstatistikBE.setVeranstaltungName(veranstaltungName);
        expectedSchuetzenstatistikBE.setWettkampfId(wettkampfId);
        expectedSchuetzenstatistikBE.setWettkampfTag(wettkampfTag);
        expectedSchuetzenstatistikBE.setMannschaftId(mannschaftId);
        expectedSchuetzenstatistikBE.setMannschaftNummer(mannschaftNummer);
        expectedSchuetzenstatistikBE.setVereinId(vereinId);
        expectedSchuetzenstatistikBE.setVereinName(vereinName);
        expectedSchuetzenstatistikBE.setMatchId(matchId);
        expectedSchuetzenstatistikBE.setMatchNr(matchNr);
        expectedSchuetzenstatistikBE.setDsbMitgliedId(dsbMitgliedId);
        expectedSchuetzenstatistikBE.setDsbMitgliedName(dsbMitgliedName);
        expectedSchuetzenstatistikBE.setRueckenNummer(rueckenNummer);
        expectedSchuetzenstatistikBE.setPfeilpunkteSchnitt(pfeilpunkteSchnitt);
        expectedSchuetzenstatistikBE.setschuetzeSatz1(schuetzeSatz1);
        expectedSchuetzenstatistikBE.setschuetzeSatz2(schuetzeSatz2);
        expectedSchuetzenstatistikBE.setschuetzeSatz3(schuetzeSatz3);
        expectedSchuetzenstatistikBE.setschuetzeSatz4(schuetzeSatz4);
        expectedSchuetzenstatistikBE.setschuetzeSatz5(schuetzeSatz5);
        return expectedSchuetzenstatistikBE;
    }


    public static SchuetzenstatistikDO getLigatabelleDO() {
        final SchuetzenstatistikDO expectedSchuetzenstatistikDO = new SchuetzenstatistikDO();
        expectedSchuetzenstatistikDO.setveranstaltungId(veranstaltungId);
        expectedSchuetzenstatistikDO.setveranstaltungName(veranstaltungName);
        expectedSchuetzenstatistikDO.setwettkampfId(wettkampfId);
        expectedSchuetzenstatistikDO.setwettkampfTag(wettkampfTag);
        expectedSchuetzenstatistikDO.setmannschaftId(mannschaftId);
        expectedSchuetzenstatistikDO.setmannschaftNummer(mannschaftNummer);
        expectedSchuetzenstatistikDO.setvereinId(vereinId);
        expectedSchuetzenstatistikDO.setvereinName(vereinName);
        expectedSchuetzenstatistikDO.setMatchId(matchId);
        expectedSchuetzenstatistikDO.setMatchNr(matchNr);
        expectedSchuetzenstatistikDO.setDsbMitgliedId(dsbMitgliedId);
        expectedSchuetzenstatistikDO.setDsbMitgliedName(dsbMitgliedName);
        expectedSchuetzenstatistikDO.setRueckenNummer(rueckenNummer);
        expectedSchuetzenstatistikDO.setPfeilpunkteSchnitt(pfeilpunkteSchnitt);

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

