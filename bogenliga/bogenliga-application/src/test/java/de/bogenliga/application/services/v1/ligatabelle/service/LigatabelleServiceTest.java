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

    private static final Long veranstaltungId = 1L;
    private static final String veranstaltungName = "Name_der_Veranstaltung";
    private static final Long wettkampfId = 2L;
    private static final int wettkampfTag = 3;
    private static final Long mannschaftId = 4L;
    private static final int mannschaftNummer = 9;
    private static final Long vereinId = 7L;
    private static final String vereinName = "Name_Verein";
    private static final int matchpkt = 6;
    private static final int matchpktGegen = 2;
    private static final int satzpkt = 18;
    private static final int satzpktGegen = 3;
    private static final int satzpktDifferenz = 15;
    private static final int sortierung = 0;
    private static final int tabellenplatz = 8;
    private static final int matchCount = 0;

    public static LigatabelleDO getLigatabelleDO() {
        final LigatabelleDO expectedLigatabelleDO = new LigatabelleDO();
        expectedLigatabelleDO.setveranstaltungId(veranstaltungId);
        expectedLigatabelleDO.setveranstaltungName(veranstaltungName);
        expectedLigatabelleDO.setwettkampfId(wettkampfId);
        expectedLigatabelleDO.setwettkampfTag(wettkampfTag);
        expectedLigatabelleDO.setmannschaftId(mannschaftId);
        expectedLigatabelleDO.setmannschaftNummer(mannschaftNummer);
        expectedLigatabelleDO.setvereinId(vereinId);
        expectedLigatabelleDO.setvereinName(vereinName);
        expectedLigatabelleDO.setmatchpkt(matchpkt);
        expectedLigatabelleDO.setMatchpktGegen(matchpktGegen);
        expectedLigatabelleDO.setsatzpkt(satzpkt);
        expectedLigatabelleDO.setSatzpktGegen(satzpktGegen);
        expectedLigatabelleDO.setSatzpktDifferenz(satzpktDifferenz);
        expectedLigatabelleDO.setsortierung(sortierung);
        expectedLigatabelleDO.settabellenplatz(tabellenplatz);
        expectedLigatabelleDO.setMatchCount(matchCount);

        return expectedLigatabelleDO;
    }
    public static LigatabelleDTO getLigatabelleDTO() {
        return new LigatabelleDTO(
        veranstaltungId,
        veranstaltungName,
        wettkampfId,
        wettkampfTag,
        mannschaftId,
        mannschaftNummer,
        vereinId,
        vereinName,
        matchpkt,
                matchpktGegen,
        satzpkt,
        satzpktGegen,
        satzpktDifferenz,
        sortierung,
        tabellenplatz,
                matchCount
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
        final List<LigatabelleDTO> actual = underTest.getLigatabelleVeranstaltung(veranstaltungId);

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
        verify(ligatabelleComponent).getLigatabelleVeranstaltung(veranstaltungId);
    }

    @Test
    public void getLigatabelleWettkampf() {
        // prepare test data
        final LigatabelleDO ligatabelleDO = getLigatabelleDO();

        final List<LigatabelleDO> ligatabelleDOList = Collections.singletonList(ligatabelleDO);

        // configure mocks
        when(ligatabelleComponent.getLigatabelleWettkampf(anyLong())).thenReturn(ligatabelleDOList);

        // call test method
        final List<LigatabelleDTO> actual = underTest.getLigatabelleWettkampf(wettkampfId);

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
        verify(ligatabelleComponent).getLigatabelleWettkampf(wettkampfId);

    }
}