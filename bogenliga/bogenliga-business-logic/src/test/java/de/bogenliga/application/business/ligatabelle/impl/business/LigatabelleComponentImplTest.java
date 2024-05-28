package de.bogenliga.application.business.ligatabelle.impl.business;

import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.business.ligatabelle.impl.dao.LigatabelleDAO;
import de.bogenliga.application.business.ligatabelle.impl.entity.LigatabelleBE;
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

public class LigatabelleComponentImplTest {


    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static Long veranstaltungId = 1L;
    private static String veranstaltungName = "Name_der_Veranstaltung";
    private static Long wettkampfId = 2L;
    private static int wettkampfTag = 3;
    private static Long mannschaftId = 4L;
    private static int mannschaftNummer = 9;
    private static Long vereinId = 7L;
    private static String vereinName = "Name_Verein";
    private static int matchpkt = 6;
    private static int matchpktGegen = 2;
    private static int satzpkt = 18;
    private static int satzpktGegen = 3;
    private static int satzpktDifferenz = 15;
    private static int sortierung = 0;
    private static int tabellenplatz = 8;

    private static int matchCount = 0;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LigatabelleDAO ligatabelleDAO;

     @InjectMocks
    private LigatabelleComponentImpl underTest;

    @Captor
    private ArgumentCaptor<LigatabelleBE> ligatabelleBEArgumentCaptor;

    public static LigatabelleBE getLigatabelleBE() {
        final LigatabelleBE expectedLigatabelleBE = new LigatabelleBE();
        expectedLigatabelleBE.setVeranstaltungId(veranstaltungId);
        expectedLigatabelleBE.setVeranstaltungName(veranstaltungName);
        expectedLigatabelleBE.setWettkampfId(wettkampfId);
        expectedLigatabelleBE.setWettkampfTag(wettkampfTag);
        expectedLigatabelleBE.setMannschaftId(mannschaftId);
        expectedLigatabelleBE.setMannschaftNummer(mannschaftNummer);
        expectedLigatabelleBE.setVereinId(vereinId);
        expectedLigatabelleBE.setVereinName(vereinName);
        expectedLigatabelleBE.setMatchpkt(matchpkt);
        expectedLigatabelleBE.setMatchpktGegen(matchpktGegen);
        expectedLigatabelleBE.setSatzpkt(satzpkt);
        expectedLigatabelleBE.setSatzpktGegen(satzpktGegen);
        expectedLigatabelleBE.setSatzpktDifferenz(satzpktDifferenz);
        expectedLigatabelleBE.setSortierung(sortierung);
        expectedLigatabelleBE.setTabellenplatz(tabellenplatz);
        expectedLigatabelleBE.setMatchCount(matchCount);

        return expectedLigatabelleBE;
    }


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


    //alle Parameter ok, Test ok

    @Test
    public void getLigatabelleVeranstaltung_allesok() {
        // prepare test data
        final LigatabelleBE expectedLigatabelleBE = getLigatabelleBE();
        final List<LigatabelleBE> expectedBEList = Collections.singletonList(expectedLigatabelleBE);

        // configure mocks
        when(ligatabelleDAO.getLigatabelleVeranstaltung(anyLong())).thenReturn(expectedBEList);


        // call test method
        final List<LigatabelleDO> actual = underTest.getLigatabelleVeranstaltung(expectedLigatabelleBE.getVeranstaltungId());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getveranstaltungId()).isEqualTo(expectedLigatabelleBE.getVeranstaltungId());
        assertThat(actual.get(0).getveranstaltungName()).isEqualTo(expectedLigatabelleBE.getVeranstaltungName());
        assertThat(actual.get(0).getwettkampfId()).isEqualTo(expectedLigatabelleBE.getWettkampfId());
        assertThat(actual.get(0).getwettkampfTag()).isEqualTo(expectedLigatabelleBE.getWettkampfTag());
        assertThat(actual.get(0).getmannschaftId()).isEqualTo(expectedLigatabelleBE.getMannschaftId());
        assertThat(actual.get(0).getmannschaftNummer()).isEqualTo(expectedLigatabelleBE.getMannschaftNummer());
        assertThat(actual.get(0).getvereinId()).isEqualTo(expectedLigatabelleBE.getVereinId());
        assertThat(actual.get(0).getvereinName()).isEqualTo(expectedLigatabelleBE.getVereinName());
        assertThat(actual.get(0).getmatchpkt()).isEqualTo(expectedLigatabelleBE.getMatchpkt());
        assertThat(actual.get(0).getMatchpktGegen()).isEqualTo(expectedLigatabelleBE.getMatchpktGegen());
        assertThat(actual.get(0).getsatzpkt()).isEqualTo(expectedLigatabelleBE.getSatzpkt());
        assertThat(actual.get(0).getSatzpktGegen()).isEqualTo(expectedLigatabelleBE.getSatzpktGegen());
        assertThat(actual.get(0).getSatzpktDifferenz()).isEqualTo(expectedLigatabelleBE.getSatzpktDifferenz());
        assertThat(actual.get(0).getsortierung()).isEqualTo(expectedLigatabelleBE.getSortierung());
        assertThat(actual.get(0).gettabellenplatz()).isEqualTo(expectedLigatabelleBE.getTabellenplatz());
        assertThat(actual.get(0).getMatchCount()).isEqualTo(expectedLigatabelleBE.getMatchCount());

        // verify invocations
        verify(ligatabelleDAO).getLigatabelleVeranstaltung(expectedLigatabelleBE.getVeranstaltungId());
    }


    //Input ID null -> Exception

    @Test
    public void getLigatabelleVeranstaltung_IDnull() {
        // prepare test data
        final LigatabelleBE expectedLigatabelleBE = getLigatabelleBE();
        final List<LigatabelleBE> expectedBEList = Collections.singletonList(expectedLigatabelleBE);
        // configure mocks
        when(ligatabelleDAO.getLigatabelleVeranstaltung(anyLong())).thenReturn(null);
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getLigatabelleVeranstaltung(anyLong()))
                .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Veranstaltungs-ID '0'")
                .withNoCause();

        // assert result

        // verify invocations
        verify(ligatabelleDAO).getLigatabelleVeranstaltung(0L);
    }


    @Test
    public void getLigatabelleWettkampf_allesok() {
        // prepare test data
        final LigatabelleBE expectedLigatabelleBE = getLigatabelleBE();
        final List<LigatabelleBE> expectedBEList = Collections.singletonList(expectedLigatabelleBE);

        // configure mocks
        when(ligatabelleDAO.getLigatabelleWettkampf(anyLong())).thenReturn(expectedBEList);


        // call test method
        final List<LigatabelleDO> actual = underTest.getLigatabelleWettkampf(expectedLigatabelleBE.getWettkampfId());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getveranstaltungId()).isEqualTo(expectedLigatabelleBE.getVeranstaltungId());
        assertThat(actual.get(0).getveranstaltungName()).isEqualTo(expectedLigatabelleBE.getVeranstaltungName());
        assertThat(actual.get(0).getwettkampfId()).isEqualTo(expectedLigatabelleBE.getWettkampfId());
        assertThat(actual.get(0).getwettkampfTag()).isEqualTo(expectedLigatabelleBE.getWettkampfTag());
        assertThat(actual.get(0).getmannschaftId()).isEqualTo(expectedLigatabelleBE.getMannschaftId());
        assertThat(actual.get(0).getmannschaftNummer()).isEqualTo(expectedLigatabelleBE.getMannschaftNummer());
        assertThat(actual.get(0).getvereinId()).isEqualTo(expectedLigatabelleBE.getVereinId());
        assertThat(actual.get(0).getvereinName()).isEqualTo(expectedLigatabelleBE.getVereinName());
        assertThat(actual.get(0).getmatchpkt()).isEqualTo(expectedLigatabelleBE.getMatchpkt());
        assertThat(actual.get(0).getMatchpktGegen()).isEqualTo(expectedLigatabelleBE.getMatchpktGegen());
        assertThat(actual.get(0).getsatzpkt()).isEqualTo(expectedLigatabelleBE.getSatzpkt());
        assertThat(actual.get(0).getSatzpktGegen()).isEqualTo(expectedLigatabelleBE.getSatzpktGegen());
        assertThat(actual.get(0).getSatzpktDifferenz()).isEqualTo(expectedLigatabelleBE.getSatzpktDifferenz());
        assertThat(actual.get(0).getsortierung()).isEqualTo(expectedLigatabelleBE.getSortierung());
        assertThat(actual.get(0).gettabellenplatz()).isEqualTo(expectedLigatabelleBE.getTabellenplatz());
        assertThat(actual.get(0).getMatchCount()).isEqualTo(expectedLigatabelleBE.getMatchCount());

        // verify invocations
        verify(ligatabelleDAO).getLigatabelleWettkampf(expectedLigatabelleBE.getWettkampfId());
    }

    //Input ID null -> Exception

    @Test
    public void getLigatabelleWettkampf_IDnull() {
        // prepare test data
        final LigatabelleBE expectedLigatabelleBE = getLigatabelleBE();
        final List<LigatabelleBE> expectedBEList = Collections.singletonList(expectedLigatabelleBE);

        // configure mocks
        when(ligatabelleDAO.getLigatabelleWettkampf(anyLong())).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getLigatabelleWettkampf(anyLong()))
                .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Wettkampf-ID '0'")
                .withNoCause();

        // assert result

        // verify invocations
        verify(ligatabelleDAO).getLigatabelleWettkampf(0L);
    }





    
}