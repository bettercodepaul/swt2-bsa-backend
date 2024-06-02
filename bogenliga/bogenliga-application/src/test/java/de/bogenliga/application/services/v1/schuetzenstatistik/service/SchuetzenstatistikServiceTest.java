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
        private static final int pfeilpunkteSchnitt = 3;
        private static final String[] schuetzeSaetze = {"{5,8}","{9,3}", "{4,8}", "{5,2}", "{3,7}"};
        private static final String schuetzeSatz1 = "{5,8}";
        private static final String schuetzeSatz2 = "{9,3}";
        private static final String schuetzeSatz3 = "{4,8}";
        private static final String schuetzeSatz4 = "{5,2}";
        private static final String schuetzeSatz5 = "{3,7}";




    public static SchuetzenstatistikDO getSchuetzenstatistikDO() {
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
            expectedSchuetzenstatistikDO.setSchuetzeSaetze(schuetzeSaetze);
            return expectedSchuetzenstatistikDO;
        }

        public static SchuetzenstatistikDTO getSchuetzenstatistikDTO() {
            return new SchuetzenstatistikDTO(
                    veranstaltungId,
                    veranstaltungName,
                    wettkampfId,
                    wettkampfTag,
                    mannschaftId,
                    mannschaftNummer,
                    vereinId,
                    vereinName,
                    matchId,
                    matchNr,
                    dsbMitgliedId,
                    dsbMitgliedName,
                    rueckenNummer,
                    pfeilpunkteSchnitt,
                    schuetzeSatz1,
                    schuetzeSatz2,
                    schuetzeSatz3,
                    schuetzeSatz4,
                    schuetzeSatz5
            );
        }


        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock
        private SchuetzenstatistikComponent schuetzenstatistikComponent;

        @Mock
        private Principal principal;

        @InjectMocks
        private SchuetzenstatistikService underTest;

        @Before
        public void initMocks() {
            when(principal.getName()).thenReturn(String.valueOf(USER));
        }

        @Test
        public void getSchuetzenstatistikVeranstaltung_ok() {
            // prepare test data
            final SchuetzenstatistikDO schuetzenstatistikDO = new SchuetzenstatistikDO();

            final List<SchuetzenstatistikDO> schuetzenstatistikDOList = Collections.singletonList(schuetzenstatistikDO);

            // configure mocks
            when(schuetzenstatistikComponent.getSchuetzenstatistikVeranstaltung(anyLong(),anyLong())).thenReturn(schuetzenstatistikDOList);

            // call test method
            final List<SchuetzenstatistikDTO> actual = underTest.getSchuetzenstatistikVeranstaltung(veranstaltungId,vereinId);

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
            verify(schuetzenstatistikComponent).getSchuetzenstatistikVeranstaltung(veranstaltungId,vereinId);
        }

        @Test
        public void getSchuetzenstatistikWettkampf() {
            // prepare test data
            final SchuetzenstatistikDO schuetzenstatistikDO = new SchuetzenstatistikDO();

            final List<SchuetzenstatistikDO> schuetzenstatistikDOList = Collections.singletonList(schuetzenstatistikDO);

            // configure mocks
            when(schuetzenstatistikComponent.getSchuetzenstatistikWettkampf(anyLong(),anyLong())).thenReturn(schuetzenstatistikDOList);

            // call test method
            final List<SchuetzenstatistikDTO> actual = underTest.getSchuetzenstatistikWettkampf(wettkampfId,vereinId);

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
            verify(schuetzenstatistikComponent).getSchuetzenstatistikWettkampf(wettkampfId,vereinId);

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

