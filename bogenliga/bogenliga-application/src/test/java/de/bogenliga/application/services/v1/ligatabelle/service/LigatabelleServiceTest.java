package de.bogenliga.application.services.v1.ligatabelle.service;

import de.bogenliga.application.business.ligatabelle.api.LigatabelleComponent;
import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.services.v1.ligatabelle.model.LigatabelleDTO;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;


public class LigatabelleServiceTest {

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
    private static final int MATCHPKT = 6;
    private static final int MATCHPKT_GEGEN = 2;
    private static final int SATZPKT = 18;
    private static final int SATZPKT_GEGEN = 3;
    private static final int SATZPKT_DIFFERENZ = 15;
    private static final int SORTIERUNG = 0;
    private static final int TABELLENPLATZ = 8;
    private static final int MATCH_COUNT = 0;

    public static LigatabelleDO getLigatabelleDO() {
        final LigatabelleDO expectedLigatabelleDO = new LigatabelleDO();
        expectedLigatabelleDO.setveranstaltungId(VERANSTALTUNG_ID);
        expectedLigatabelleDO.setveranstaltungName(VERANSTALTUNG_NAME);
        expectedLigatabelleDO.setwettkampfId(WETTKAMPF_ID);
        expectedLigatabelleDO.setwettkampfTag(WETTKAMPF_TAG);
        expectedLigatabelleDO.setmannschaftId(MANNSCHAFT_ID);
        expectedLigatabelleDO.setmannschaftNummer(MANNSCHAFT_NUMMER);
        expectedLigatabelleDO.setvereinId(VEREIN_ID);
        expectedLigatabelleDO.setvereinName(VEREIN_NAME);
        expectedLigatabelleDO.setmatchpkt(MATCHPKT);
        expectedLigatabelleDO.setMatchpktGegen(MATCHPKT_GEGEN);
        expectedLigatabelleDO.setsatzpkt(SATZPKT);
        expectedLigatabelleDO.setSatzpktGegen(SATZPKT_GEGEN);
        expectedLigatabelleDO.setSatzpktDifferenz(SATZPKT_DIFFERENZ);
        expectedLigatabelleDO.setsortierung(SORTIERUNG);
        expectedLigatabelleDO.settabellenplatz(TABELLENPLATZ);
        expectedLigatabelleDO.setMatchCount(MATCH_COUNT);

        return expectedLigatabelleDO;
    }
    public static LigatabelleDTO getLigatabelleDTO() {
        return new LigatabelleDTO(
                VERANSTALTUNG_ID,
                VERANSTALTUNG_NAME,
                WETTKAMPF_ID,
                WETTKAMPF_TAG,
                MANNSCHAFT_ID,
                MANNSCHAFT_NUMMER,
                VEREIN_ID,
                VEREIN_NAME,
                MATCHPKT,
                MATCHPKT_GEGEN,
                SATZPKT,
                SATZPKT_GEGEN,
                SATZPKT_DIFFERENZ,
                SORTIERUNG,
                TABELLENPLATZ,
                MATCH_COUNT
        );
    }


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LigatabelleComponent ligatabelleComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private LigatabelleService underTest;

    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }

    @Test
    public void getLigatabelleVeranstaltung_ok() {
        // prepare test data
        final LigatabelleDO ligatabelleDO = getLigatabelleDO();

        final List<LigatabelleDO> ligatabelleDOList = Collections.singletonList(ligatabelleDO);

        // configure mocks
        when(ligatabelleComponent.getLigatabelleVeranstaltung(anyLong())).thenReturn(ligatabelleDOList);

        // call test method
        final List<LigatabelleDTO> actual = underTest.getLigatabelleVeranstaltung(VERANSTALTUNG_ID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final LigatabelleDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getVeranstaltungId()).isEqualTo(ligatabelleDO.getveranstaltungId());
        assertThat(actualDTO.getVeranstaltungName()).isEqualTo(ligatabelleDO.getveranstaltungName());
        assertThat(actualDTO.getWettkampfId()).isEqualTo(ligatabelleDO.getwettkampfId());
        assertThat(actualDTO.getWettkampfTag()).isEqualTo(ligatabelleDO.getwettkampfTag());
        assertThat(actualDTO.getMannschaftId()).isEqualTo(ligatabelleDO.getmannschaftId());
        assertThat(actualDTO.getMannschaftNummer()).isEqualTo(ligatabelleDO.getmannschaftNummer());
        assertThat(actualDTO.getVereinId()).isEqualTo(ligatabelleDO.getvereinId());
        assertThat(actualDTO.getVereinName()).isEqualTo(ligatabelleDO.getvereinName());
        assertThat(actualDTO.getMatchpkt()).isEqualTo(ligatabelleDO.getmatchpkt());
        assertThat(actualDTO.getMatchpktGegen()).isEqualTo(ligatabelleDO.getMatchpktGegen());
        assertThat(actualDTO.getSatzpkt()).isEqualTo(ligatabelleDO.getsatzpkt());
        assertThat(actualDTO.getSatzpktGegen()).isEqualTo(ligatabelleDO.getSatzpktGegen());
        assertThat(actualDTO.getSatzpktDifferenz()).isEqualTo(ligatabelleDO.getSatzpktDifferenz());
        assertThat(actualDTO.getSortierung()).isEqualTo(ligatabelleDO.getsortierung());
        assertThat(actualDTO.getTabellenplatz()).isEqualTo(ligatabelleDO.gettabellenplatz());
        assertThat(actualDTO.getMatchCount()).isEqualTo(ligatabelleDO.getMatchCount());

        // verify invocations
        verify(ligatabelleComponent).getLigatabelleVeranstaltung(VERANSTALTUNG_ID);
    }

    @Test
    public void getLigatabelleWettkampf() {
        // prepare test data
        final LigatabelleDO ligatabelleDO = getLigatabelleDO();

        final List<LigatabelleDO> ligatabelleDOList = Collections.singletonList(ligatabelleDO);

        // configure mocks
        when(ligatabelleComponent.getLigatabelleWettkampf(anyLong())).thenReturn(ligatabelleDOList);

        // call test method
        final List<LigatabelleDTO> actual = underTest.getLigatabelleWettkampf(WETTKAMPF_ID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final LigatabelleDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getVeranstaltungId()).isEqualTo(ligatabelleDO.getveranstaltungId());
        assertThat(actualDTO.getVeranstaltungName()).isEqualTo(ligatabelleDO.getveranstaltungName());
        assertThat(actualDTO.getWettkampfId()).isEqualTo(ligatabelleDO.getwettkampfId());
        assertThat(actualDTO.getWettkampfTag()).isEqualTo(ligatabelleDO.getwettkampfTag());
        assertThat(actualDTO.getMannschaftId()).isEqualTo(ligatabelleDO.getmannschaftId());
        assertThat(actualDTO.getMannschaftNummer()).isEqualTo(ligatabelleDO.getmannschaftNummer());
        assertThat(actualDTO.getVereinId()).isEqualTo(ligatabelleDO.getvereinId());
        assertThat(actualDTO.getVereinName()).isEqualTo(ligatabelleDO.getvereinName());
        assertThat(actualDTO.getMatchpkt()).isEqualTo(ligatabelleDO.getmatchpkt());
        assertThat(actualDTO.getMatchpktGegen()).isEqualTo(ligatabelleDO.getMatchpktGegen());
        assertThat(actualDTO.getSatzpkt()).isEqualTo(ligatabelleDO.getsatzpkt());
        assertThat(actualDTO.getSatzpktGegen()).isEqualTo(ligatabelleDO.getSatzpktGegen());
        assertThat(actualDTO.getSatzpktDifferenz()).isEqualTo(ligatabelleDO.getSatzpktDifferenz());
        assertThat(actualDTO.getSortierung()).isEqualTo(ligatabelleDO.getsortierung());
        assertThat(actualDTO.getTabellenplatz()).isEqualTo(ligatabelleDO.gettabellenplatz());
        assertThat(actualDTO.getMatchCount()).isEqualTo(ligatabelleDO.getMatchCount());

        // verify invocations
        verify(ligatabelleComponent).getLigatabelleWettkampf(WETTKAMPF_ID);

    }
}