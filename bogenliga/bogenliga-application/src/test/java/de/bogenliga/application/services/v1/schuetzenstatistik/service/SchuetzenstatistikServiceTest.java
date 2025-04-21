package de.bogenliga.application.services.v1.schuetzenstatistik.service;

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

import de.bogenliga.application.business.schuetzenstatistik.api.SchuetzenstatistikComponent;
import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.services.v1.schuetzenstatistik.model.SchuetzenstatistikDTO;

public class SchuetzenstatistikServiceTest {

        private static final long USER = 4L;
        private static final Long VERSION = 0L;

        private static final Long VERANSTALTUNGID = 1L;
        private static final String VERANSTALTUNGNAME = "Name_der_Veranstaltung";
        private static final Long WETTKAMPFID = 2L;
        private static final int WETTKAMPFTAG = 3;
        private static final Long MANNSCHAFTID = 4L;
        private static final int MANNSCHAFTNUMMER = 9;
        private static final Long VEREINID = 7L;
        private static final String VEREINNAME = "Name_Verein";
        private static final Long MATCHID = 6L;
        private static final int MATCHNR = 2;
        private static final Long DSBMITGLIEDID = 2L;
        private static final String DSBMITGLIEDNAME = "Mitglied_Name";
        private static final int RUECKENNUMMER = 5;
        private static final int PFEILPUNKTESCHNITT = 3;
        private static final String[] SCHUETZE_SAETZE = {"{5,8}","{9,3}", "{4,8}", "{5,2}", "{3,7}"};
        private static final String SCHUETZESATZ1 = "{5,8}";
        private static final String SCHUETZESATZ2 = "{9,3}";
        private static final String SCHUETZESATZ3 = "{4,8}";
        private static final String SCHUETZESATZ4 = "{5,2}";
        private static final String SCHUETZESATZ5 = "{3,7}";

        public static SchuetzenstatistikDO getSchuetzenstatistikDO() {
            final SchuetzenstatistikDO expectedSchuetzenstatistikDO = new SchuetzenstatistikDO();
            expectedSchuetzenstatistikDO.setveranstaltungId(VERANSTALTUNGID);
            expectedSchuetzenstatistikDO.setveranstaltungName(VERANSTALTUNGNAME);
            expectedSchuetzenstatistikDO.setwettkampfId(WETTKAMPFID);
            expectedSchuetzenstatistikDO.setwettkampfTag(WETTKAMPFTAG);
            expectedSchuetzenstatistikDO.setmannschaftId(MANNSCHAFTID);
            expectedSchuetzenstatistikDO.setmannschaftNummer(MANNSCHAFTNUMMER);
            expectedSchuetzenstatistikDO.setvereinId(VEREINID);
            expectedSchuetzenstatistikDO.setvereinName(VEREINNAME);
            expectedSchuetzenstatistikDO.setMatchId(MATCHID);
            expectedSchuetzenstatistikDO.setMatchNr(MATCHNR);
            expectedSchuetzenstatistikDO.setDsbMitgliedId(DSBMITGLIEDID);
            expectedSchuetzenstatistikDO.setDsbMitgliedName(DSBMITGLIEDNAME);
            expectedSchuetzenstatistikDO.setRueckenNummer(RUECKENNUMMER);
            expectedSchuetzenstatistikDO.setPfeilpunkteSchnitt(PFEILPUNKTESCHNITT);
            expectedSchuetzenstatistikDO.setSchuetzeSaetze(SCHUETZE_SAETZE);
            return expectedSchuetzenstatistikDO;
        }

        public static SchuetzenstatistikDTO getSchuetzenstatistikDTO() {
            return new SchuetzenstatistikDTO(
                VERANSTALTUNGID,
                VERANSTALTUNGNAME,
                WETTKAMPFID,
                WETTKAMPFTAG,
                MANNSCHAFTID,
                MANNSCHAFTNUMMER,
                VEREINID,
                VEREINNAME,
                MATCHID,
                MATCHNR,
                DSBMITGLIEDID,
                DSBMITGLIEDNAME,
                RUECKENNUMMER,
                PFEILPUNKTESCHNITT,
                SCHUETZESATZ1,
                SCHUETZESATZ2,
                SCHUETZESATZ3,
                SCHUETZESATZ4,
                SCHUETZESATZ5
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
            final List<SchuetzenstatistikDTO> actual = underTest.getSchuetzenstatistikVeranstaltung(VERANSTALTUNGID,VEREINID);

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
            verify(schuetzenstatistikComponent).getSchuetzenstatistikVeranstaltung(VERANSTALTUNGID,VEREINID);
        }

        @Test
        public void getSchuetzenstatistikWettkampf() {
            // prepare test data
            final SchuetzenstatistikDO schuetzenstatistikDO = new SchuetzenstatistikDO();

            final List<SchuetzenstatistikDO> schuetzenstatistikDOList = Collections.singletonList(schuetzenstatistikDO);

            // configure mocks
            when(schuetzenstatistikComponent.getSchuetzenstatistikWettkampf(anyLong(),anyLong())).thenReturn(schuetzenstatistikDOList);

            // call test method
            final List<SchuetzenstatistikDTO> actual = underTest.getSchuetzenstatistikWettkampf(WETTKAMPFID,VEREINID);

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
            verify(schuetzenstatistikComponent).getSchuetzenstatistikWettkampf(WETTKAMPFID,VEREINID);

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

