package de.bogenliga.application.services.v1.schuetzenstatistik.service;

import de.bogenliga.application.business.schuetzenstatistik.api.SchuetzenstatistikComponent;
import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.services.v1.schuetzenstatistik.model.SchuetzenstatistikDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SchuetzenstatistikServiceTest {

    private static final long USER = 4L;
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
    private static final int PFEILPUNKTE_SCHNITT = 3;
    private static final String[] SCHUETZE_SAETZE = {"{5,8}","{9,3}", "{4,8}", "{5,2}", "{3,7}"};
    private static final String SCHUETZE_SATZ1 = "{5,8}";
    private static final String SCHUETZE_SATZ2 = "{9,3}";
    private static final String SCHUETZE_SATZ3 = "{4,8}";
    private static final String SCHUETZE_SATZ4 = "{5,2}";
    private static final String SCHUETZE_SATZ5 = "{3,7}";

    public static SchuetzenstatistikDO getSchuetzenstatistikDO() {
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
        expectedSchuetzenstatistikDO.setSchuetzeSaetze(SCHUETZE_SAETZE);
        return expectedSchuetzenstatistikDO;
    }

    public static SchuetzenstatistikDTO getSchuetzenstatistikDTO() {
        return new SchuetzenstatistikDTO(
            VERANSTALTUNG_ID,
            VERANSTALTUNG_NAME,
            WETTKAMPF_ID,
            WETTKAMPF_TAG,
            MANNSCHAFT_ID,
            MANNSCHAFT_NUMMER,
            VEREIN_ID,
            VEREIN_NAME,
            MATCH_ID,
            MATCH_NR,
            DSB_MITGLIED_ID,
            DSB_MITGLIED_NAME,
            RUECKEN_NUMMER,
            PFEILPUNKTE_SCHNITT,
            SCHUETZE_SATZ1,
            SCHUETZE_SATZ2,
            SCHUETZE_SATZ3,
            SCHUETZE_SATZ4,
            SCHUETZE_SATZ5
        );
    }

    @Rule
    public MockitoRule MockitoRule = MockitoJUnit.rule();

    @Mock
    private SchuetzenstatistikComponent SchuetzenstatistikComponent;

    @Mock
    private Principal Principal;

    @InjectMocks
    private SchuetzenstatistikService UnderTest;

    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }

    @Test
    public void getSchuetzenstatistikVeranstaltung_ok() {
        // prepare test data
        final SchuetzenstatistikDO SchuetzenstatistikDO = new SchuetzenstatistikDO();

        final List<SchuetzenstatistikDO> SchuetzenstatistikDOList = Collections.singletonList(schuetzenstatistikDO);

        // configure mocks
        when(schuetzenstatistikComponent.getSchuetzenstatistikVeranstaltung(anyLong(),anyLong())).thenReturn(schuetzenstatistikDOList);

        // call test method
        final List<SchuetzenstatistikDTO> actual = underTest.getSchuetzenstatistikVeranstaltung(VERANSTALTUNG_ID,VEREIN_ID);

        // assert result
        assertThat(actual)
            .isNotNull()
            .hasSize(1);

        final SchuetzenstatistikDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getVeranstaltungId()).isEqualTo(schuetzenstatistikDO.getveranstaltungId());
        assertThat(actualDTO.getVeranstaltungName()).isEqualTo(schuetzenstatistikDO.getveranstaltungName());
        assertThat(actualDTO.getWettkampfId()).isEqualTo(schuetzenstatistikDO.getwettkampfId());
        assertThat(actualDTO.getWettkampfTag()).isEqualTo(schuetzenstatistikDO.getwettkampfTag());
        assertThat(actualDTO.getMannschaftId()).isEqualTo(schuetzenstatistikDO.getmannschaftId());
        assertThat(actualDTO.getMannschaftNummer()).isEqualTo(schuetzenstatistikDO.getmannschaftNummer());
        assertThat(actualDTO.getVereinId()).isEqualTo(schuetzenstatistikDO.getvereinId());
        assertThat(actualDTO.getVereinName()).isEqualTo(schuetzenstatistikDO.getvereinName());
        assertThat(actualDTO.getMatchId()).isEqualTo(schuetzenstatistikDO.getMatchId());
        assertThat(actualDTO.getMatchNr()).isEqualTo(schuetzenstatistikDO.getMatchNr());
        assertThat(actualDTO.getDsbMitgliedId()).isEqualTo(schuetzenstatistikDO.getDsbMitgliedId());
        assertThat(actualDTO.getDsbMitgliedName()).isEqualTo(schuetzenstatistikDO.getDsbMitgliedName());
        assertThat(actualDTO.getRueckenNummer()).isEqualTo(schuetzenstatistikDO.getRueckenNummer());
        assertThat(actualDTO.getPfeilpunkteSchnitt()).isEqualTo(schuetzenstatistikDO.getPfeilpunkteSchnitt());
        assertThat(actualDTO.getSchuetzeSatz1()).isEqualTo(removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz1()));
        assertThat(actualDTO.getSchuetzeSatz2()).isEqualTo(removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz2()));
        assertThat(actualDTO.getSchuetzeSatz3()).isEqualTo(removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz3()));
        assertThat(actualDTO.getSchuetzeSatz4()).isEqualTo(removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz4()));
        assertThat(actualDTO.getSchuetzeSatz5()).isEqualTo(removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz5()));

        // verify invocations
        verify(schuetzenstatistikComponent).getSchuetzenstatistikVeranstaltung(VERANSTALTUNG_ID,VEREIN_ID);
    }

    @Test
    public void getSchuetzenstatistikWettkampf() {
        // prepare test data
        final SchuetzenstatistikDO SchuetzenstatistikDO = new SchuetzenstatistikDO();

        final List<SchuetzenstatistikDO> SchuetzenstatistikDOList = Collections.singletonList(schuetzenstatistikDO);

        // configure mocks
        when(schuetzenstatistikComponent.getSchuetzenstatistikWettkampf(anyLong(),anyLong())).thenReturn(schuetzenstatistikDOList);

        // call test method
        final List<SchuetzenstatistikDTO> actual = underTest.getSchuetzenstatistikWettkampf(WETTKAMPF_ID,VEREIN_ID);

        // assert result
        assertThat(actual)
            .isNotNull()
            .hasSize(1);

        final SchuetzenstatistikDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getVeranstaltungId()).isEqualTo(schuetzenstatistikDO.getveranstaltungId());
        assertThat(actualDTO.getVeranstaltungName()).isEqualTo(schuetzenstatistikDO.getveranstaltungName());
        assertThat(actualDTO.getWettkampfId()).isEqualTo(schuetzenstatistikDO.getwettkampfId());
        assertThat(actualDTO.getWettkampfTag()).isEqualTo(schuetzenstatistikDO.getwettkampfTag());
        assertThat(actualDTO.getMannschaftId()).isEqualTo(schuetzenstatistikDO.getmannschaftId());
        assertThat(actualDTO.getMannschaftNummer()).isEqualTo(schuetzenstatistikDO.getmannschaftNummer());
        assertThat(actualDTO.getVereinId()).isEqualTo(schuetzenstatistikDO.getvereinId());
        assertThat(actualDTO.getVereinName()).isEqualTo(schuetzenstatistikDO.getvereinName());
        assertThat(actualDTO.getMatchId()).isEqualTo(schuetzenstatistikDO.getMatchId());
        assertThat(actualDTO.getMatchNr()).isEqualTo(schuetzenstatistikDO.getMatchNr());
        assertThat(actualDTO.getDsbMitgliedId()).isEqualTo(schuetzenstatistikDO.getDsbMitgliedId());
        assertThat(actualDTO.getDsbMitgliedName()).isEqualTo(schuetzenstatistikDO.getDsbMitgliedName());
        assertThat(actualDTO.getRueckenNummer()).isEqualTo(schuetzenstatistikDO.getRueckenNummer());
        assertThat(actualDTO.getPfeilpunkteSchnitt()).isEqualTo(schuetzenstatistikDO.getPfeilpunkteSchnitt());
        assertThat(actualDTO.getSchuetzeSatz1()).isEqualTo(removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz1()));
        assertThat(actualDTO.getSchuetzeSatz2()).isEqualTo(removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz2()));
        assertThat(actualDTO.getSchuetzeSatz3()).isEqualTo(removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz3()));
        assertThat(actualDTO.getSchuetzeSatz4()).isEqualTo(removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz4()));
        assertThat(actualDTO.getSchuetzeSatz5()).isEqualTo(removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz5()));
        // verify invocations
        verify(schuetzenstatistikComponent).getSchuetzenstatistikWettkampf(WETTKAMPF_ID,VEREIN_ID);

    }

    // Method for removing the braces from Sätze
    private static String removeCurlyBracketsFromSchuetzeSatz(String schuetzeSatz) {
    if (schuetzeSatz != null && schuetzeSatz.length() > 1) {
        return schuetzeSatz.substring(1, schuetzeSatz.length() - 1);
    } else {
        return "";
    }
    }
    @Test
    public void equalMethodSchuetzenstatistikDTOTest(){

        SchuetzenstatistikDTO schuetzenstatistikDTOToCompareWith = SchuetzenstatistikServiceTest.getSchuetzenstatistikDTO();
        SchuetzenstatistikDTO schuetzenstatistikDTOComparator = SchuetzenstatistikServiceTest.getSchuetzenstatistikDTO();

        assertThat(schuetzenstatistikDTOToCompareWith.equals(schuetzenstatistikDTOComparator)).isTrue();

    }
}
