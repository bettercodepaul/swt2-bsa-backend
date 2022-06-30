package de.bogenliga.application.services.v1.Sync.service;

import java.security.Principal;
import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.naming.NoPermissionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.ligatabelle.api.LigatabelleComponent;
import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.LigamatchDO;
import de.bogenliga.application.business.match.api.types.MatchBegegnungDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncLigatabelleDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMannschaftsmitgliedDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncPasseDTO;
import de.bogenliga.application.services.v1.sync.model.WettkampfExtDTO;
import de.bogenliga.application.services.v1.sync.service.SyncService;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Adrian Kempf, HSRT MKI SS22 - SWT2
 */
public class SyncServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LigatabelleComponent ligatabelleComponent;

    @Mock
    private PasseComponent passeComponent;

    @Mock
    private WettkampfComponent wettkampfComponent;

    @Mock
    private RequiresOnePermissionAspect requiresOnePermissionAspect;

    @Mock
    private Principal principal;

    @Mock
    private MatchComponent matchComponent;

    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;


    @InjectMocks
    private SyncService underTest;

    @Captor
    private ArgumentCaptor<WettkampfDO> wettkampfDOArgumentCaptor;

    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf Id must not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "Veranstaltung Id must not be negative";
    //private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf Id must not be negative";

    protected static final Long MATCH_ID = 1L;
    protected static final Long MATCH_NR = 1L;
    protected static final Long MATCH_BEGEGNUNG = 1L;
    protected static final Long MATCH_WETTKAMPF_ID = 1L;
    protected static final Long MATCH_MANNSCHAFT_ID = 1L;
    protected static final Long MATCH_SCHEIBENNUMMER = 3L;
    protected static final Long MATCH_MATCHPUNKTE = 6L;
    protected static final Long MATCH_SATZPUNKTE = 3L;
    protected static final Long CURRENT_USER_ID = 1L;

    protected static final Long MATCH_STRAFPUNKTE_SATZ1 = 10L;
    protected static final Long MATCH_STRAFPUNKTE_SATZ2 = 0L;
    protected static final Long MATCH_STRAFPUNKTE_SATZ3 = 20L;
    protected static final Long MATCH_STRAFPUNKTE_SATZ4 = 0L;
    protected static final Long MATCH_STRAFPUNKTE_SATZ5 = 0L;

    protected static final Long MATCH_VERSION = 1L;
    protected static final String MATCH_MANNSCHAFT_NAME = "TSV_Grafenberg";
    protected static final Long MATCH_NAECHSTE_MATCH_ID = 1L;
    protected static final Long MATCH_NAECHSTE_NAECHSTE_MATCH_ID = 1L;
    protected static final String MATCH_WETTKAMP_TYP_ID = "0";
    protected static final Long MATCH_WETTKAMPF_TAG = 1L;
    protected static final Integer MATCH_RUECKENNUMMER = 2;

    protected static final String MATCH_NAME_GEGNER = "TSV Grafenberg Gegner";
    protected static final Long MATCH_SCHEIBENNUMMER_GEGNER = 4L;
    protected static final Long MATCH_ID_GEGNER = 2L;

/*
    private static final Long PASSE_ID = 1L;
    private static final Long PASSE_ID_1 = 1L;
    private static final Long PASSE_ID_2 = 2L;
    private static final Long PASSE_LFDR_NR = 2L;
    private static final Integer PASSE_SCHUETZE_NR_1 = 1;
    private static final Integer PASSE_SCHUETZE_NR_2 = 2;
    private static final Long PASSE_DSB_MITGLIED_ID = 1L;
    private static final Integer PASSE_PFEIL_1 = 10;
    private static final Integer PASSE_PFEIL_2 = 5;
*/
    // Passe aus PassServiceTest
    private static final Long PASSE_ID = 1L;
    private static final Long PASSE_LFDR_NR = 2L;
    private static final Integer PASSE_PFEIL_1 = 10;
    private static final Integer PASSE_PFEIL_2 = 8;
    private static final Long PASSE_DSB_MITGLIED_ID = 123L;

    private static final Long MM_ID_1 = 1L;
    private static final Long MM_ID_2 = 2L;
    private static final Long MM_ID_3 = 3L;
    private static final Long MM_mannschaftsId = 1L;
    private static final Long MM_dsbMitgliedId = 100L;
    private static final Integer MM_dsbMitgliedEingesetzt = 1;
    private static final String MM_dsbMitgliedVorname = "Foo";
    private static final String MM_dsbMitgliedNachname = "Bar";
    private static final Long MM_rueckennummer_1 = 5L;
    private static final Long MM_rueckennummer_2 = 6L;
    private static final Long MM_rueckennummer_3 = 7L;


    private static final Long W_id = 5L;
    private static final String W_name = "Liga_kummulativ";

    private static final Long W_vid = 243L;
    private static final Long W_typId = 0L;
    private static final Long W_tag = 5L;
    private static final Date W_datum = new Date(20190521L);
    private static final String W_strasse = "Reutlingerstr. 6";
    private static final String W_plz = "72764";
    private static final String W_ortsname = "Reutlingen";
    private static final String W_ortsinfo= "Hinter dem Haus";
    private static final String W_begin = "gestern";
    private static final Long W_disId = 12345L;

    private static final Long M_id = 2222L;
    private static final String M_name = null; //empty
    private static final Long M_vereinId = 101010L;
    private static final Long M_nummer = 111L;
    private static final Long M_benutzerId = 12L;
    private static final Long M_veranstaltungId = 1L;
    private static final Long M_sortierung = 1L;

    private static final Long VEREIN_USER = 1L;
    private static final Long VERSION = 0L;
    private static final String VEREIN_NAME = "Test Verein";
    private static final Long VEREIN_ID = 1L;
    private static final String VEREIN_DSB_IDENTIFIER = "id";
    private static final Long REGION_ID = 0L;
    private static final String REGION_NAME = "";
    private static final String VEREIN_WEBSITE = "";
    private static final String VEREIN_DESCRIPTION = "";
    private static final String VEREIN_ICON = null;
    private static final OffsetDateTime VEREIN_OFFSETDATETIME = null;

    // LigatabelleDO
    private static final long USER = 4L;
    //private static final Long VERSION = 0L;

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

    //MannschaftsmitgliedDO
    //private static final Long USER = 0L;
    private static final Long id = 1L;
    private static final Long mannschaftsId = 1L;
    private static final Long dsbMitgliedId = 100L;
    private static final Integer dsbMitgliedEingesetzt = 1;
    private static final String dsbMitgliedVorname = "Mario";
    private static final String dsbMitgliedNachname = "Gomez";
    private static final Long rueckennummer = 5L;
    private static final Long wettkampId = 30L;

    //WettkampfExtDO
    private static final long user_Id = 13;

    private static final long wettkampf_Id = 322;
    private static final long wettkampf_Veranstaltung_Id = 0;
    private static final Date wettkampf_Datum = new Date(20190521L);
    private static final String wettkampf_Datum_S = "2019-05-21";
    private static final String wettkampf_Strasse = "Reutlingerstr. 6";
    private static final String wettkampf_Plz = "72764";
    private static final String wettkampf_Ortsname = "Reutlingen";
    private static final String wettkampf_Ortsinfo = "Im Keller";
    private static final String wettkampf_Beginn = "8:00";
    private static final long wettkampf_Tag = 8;
    private static final long wettkampf_Disziplin_Id = 0;
    private static final long wettkampf_Wettkampftyp_Id = 1;
    private static final long mannschafts_id = 1;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private static final long version = 1234;
    private static final long wettkampfAusrichter = 8;
    private static final String offlineToken = "OfflineTokenTest";

    protected MatchBegegnungDO getMatchBegegnungDO(){
        return null;
    }

    protected LigatabelleDO getLigaTabelleDO(){
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

        return expectedLigatabelleDO;
    }

    protected MatchDO getMatchDO() {
        return new MatchDO(
                MATCH_ID,
                MATCH_NR,
                MATCH_BEGEGNUNG,
                MATCH_MANNSCHAFT_ID,
                MATCH_WETTKAMPF_ID,
                MATCH_MATCHPUNKTE,
                MATCH_SCHEIBENNUMMER,
                MATCH_SATZPUNKTE,
                MATCH_STRAFPUNKTE_SATZ1,
                MATCH_STRAFPUNKTE_SATZ2,
                MATCH_STRAFPUNKTE_SATZ3,
                MATCH_STRAFPUNKTE_SATZ4,
                MATCH_STRAFPUNKTE_SATZ5
        );
    }

    protected LigamatchDO getLigamatchDO() {
        return new LigamatchDO(
                MATCH_ID,
                MATCH_WETTKAMPF_ID,
                MATCH_NR,
                MATCH_SCHEIBENNUMMER,
                MATCH_MATCHPUNKTE,
                MATCH_SATZPUNKTE,
                MATCH_MANNSCHAFT_ID,
                MATCH_MANNSCHAFT_NAME,
                MATCH_NAME_GEGNER,
                MATCH_SCHEIBENNUMMER_GEGNER,
                MATCH_ID_GEGNER,
                MATCH_NAECHSTE_MATCH_ID,
                MATCH_NAECHSTE_NAECHSTE_MATCH_ID,
                MATCH_STRAFPUNKTE_SATZ1,
                MATCH_STRAFPUNKTE_SATZ2,
                MATCH_STRAFPUNKTE_SATZ3,
                MATCH_STRAFPUNKTE_SATZ4,
                MATCH_STRAFPUNKTE_SATZ5
        );
    }

    protected PasseDO getPasseDO() {
        return new PasseDO(
                PASSE_ID,
                MATCH_MANNSCHAFT_ID,
                MATCH_WETTKAMPF_ID,
                MATCH_NR,
                MATCH_ID,
                PASSE_LFDR_NR,
                PASSE_DSB_MITGLIED_ID,
                PASSE_PFEIL_1,
                PASSE_PFEIL_2,
                null, null, null, null
        );
    }


    protected MannschaftsmitgliedDO getMMDO(Long id, Long rueckennummer) {
        return new MannschaftsmitgliedDO(
                id,
                MM_mannschaftsId,
                MM_dsbMitgliedId,
                MM_dsbMitgliedEingesetzt,
                MM_dsbMitgliedVorname,
                MM_dsbMitgliedNachname,
                rueckennummer
        );
    }

    protected DsbMannschaftDO getMannschaftDO(Long id) {
        return new DsbMannschaftDO(
                id,
                M_name,
                M_vereinId,
                M_nummer,
                M_benutzerId,
                M_veranstaltungId,
                M_sortierung
        );
    }


    public static MannschaftsmitgliedDO getMannschaftsmitgliedDO() {
        return new MannschaftsmitgliedDO(
                id, mannschaftsId, dsbMitgliedId, dsbMitgliedEingesetzt, dsbMitgliedVorname, dsbMitgliedNachname, rueckennummer
        );
    }

    protected List<MannschaftsmitgliedDO> getMannschaftsMitglieder() {
        List<MannschaftsmitgliedDO> mmdos = new ArrayList<>();
        mmdos.add(getMMDO(MM_ID_1, 5L));
        mmdos.add(getMMDO(MM_ID_2, 6L));
        mmdos.add(getMMDO(MM_ID_3, 7L));
        return mmdos;
    }

    // id entfernt aus getPasseDO
    protected PasseDTO getPasseDTO(Long id, Integer nr) {
        PasseDTO passeDTO = PasseDTOMapper.toDTO.apply(getPasseDO());
        passeDTO.setRueckennummer(nr);
        return passeDTO;
    }



    private LigamatchBE getLigamatchBE() {
        LigamatchBE ligamatchBE = new LigamatchBE();
        ligamatchBE.setWettkampfId(MATCH_WETTKAMPF_ID);
        ligamatchBE.setMatchId(MATCH_ID);
        ligamatchBE.setMatchNr(MATCH_NR);
        ligamatchBE.setScheibennummer(MATCH_SCHEIBENNUMMER);
        ligamatchBE.setMannschaftId(MATCH_MANNSCHAFT_ID);
        ligamatchBE.setBegegnung(MATCH_BEGEGNUNG);
        ligamatchBE.setNaechsteMatchId(MATCH_NAECHSTE_MATCH_ID);
        ligamatchBE.setNaechsteNaechsteMatchId(MATCH_NAECHSTE_NAECHSTE_MATCH_ID);
        ligamatchBE.setStrafpunkteSatz1(MATCH_STRAFPUNKTE_SATZ1);
        ligamatchBE.setStrafpunkteSatz2(MATCH_STRAFPUNKTE_SATZ2);
        ligamatchBE.setStrafpunkteSatz3(MATCH_STRAFPUNKTE_SATZ3);
        ligamatchBE.setStrafpunkteSatz4(MATCH_STRAFPUNKTE_SATZ4);
        ligamatchBE.setStrafpunkteSatz5(MATCH_STRAFPUNKTE_SATZ5);
        ligamatchBE.setWettkampftypId(MATCH_WETTKAMP_TYP_ID);
        ligamatchBE.setWettkampfTag(MATCH_WETTKAMPF_TAG);
        ligamatchBE.setMannschaftName(MATCH_MANNSCHAFT_NAME);
        ligamatchBE.setRueckennummer(MATCH_RUECKENNUMMER);
        return ligamatchBE;
    }

    public static WettkampfDO getWettkampfDO() {
        return new WettkampfDO(
                wettkampf_Id,
                wettkampf_Veranstaltung_Id,
                wettkampf_Datum,
                wettkampf_Strasse,
                wettkampf_Plz,
                wettkampf_Ortsname,
                wettkampf_Ortsinfo,
                wettkampf_Beginn,
                wettkampf_Tag,
                wettkampf_Disziplin_Id,
                wettkampf_Wettkampftyp_Id,
                created_At_Utc,
                user_Id,
                version,
                wettkampfAusrichter
        );
    }

    private static WettkampfDTO getWettkampfDTO() {
        return new WettkampfDTO(
                wettkampf_Id,
                wettkampf_Veranstaltung_Id,
                wettkampf_Datum,
                wettkampf_Strasse,
                wettkampf_Plz,
                wettkampf_Ortsname,
                wettkampf_Ortsinfo,
                wettkampf_Beginn,
                wettkampf_Tag,
                wettkampf_Disziplin_Id,
                wettkampf_Wettkampftyp_Id,
                version,
                wettkampfAusrichter

        );
    }

    private static WettkampfExtDTO getWettkampfExtDTO() {
        return new WettkampfExtDTO(
                wettkampf_Id,
                wettkampf_Veranstaltung_Id,
                wettkampf_Datum,
                wettkampf_Strasse,
                wettkampf_Plz,
                wettkampf_Ortsname,
                wettkampf_Ortsinfo,
                wettkampf_Beginn,
                wettkampf_Tag,
                wettkampf_Disziplin_Id,
                wettkampf_Wettkampftyp_Id,
                version,
                wettkampfAusrichter,
                offlineToken
        );

    }

    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(CURRENT_USER_ID));
    }

    @Test
    public void testgetLigatabelleVeranstaltungOffline() {
        LigatabelleDO ligatabelleDO = getLigaTabelleDO();

        final List<LigatabelleDO> ligatabelleDOList = Collections.singletonList(ligatabelleDO);
        when(ligatabelleComponent.getLigatabelleVeranstaltung(anyLong())).thenReturn(ligatabelleDOList);
        final List<LigaSyncLigatabelleDTO> actual = underTest.getLigatabelleVeranstaltungOffline(wettkampfId);
        assertThat(actual).isNotNull().hasSize(1);

        final LigaSyncLigatabelleDTO actualLigaSyncTabelleDTO = actual.get(0);

        assertThat(actualLigaSyncTabelleDTO).isNotNull();
        assertThat(actualLigaSyncTabelleDTO.getVeranstaltungId()).isEqualTo(ligatabelleDO.getveranstaltungId());
        assertThat(actualLigaSyncTabelleDTO.getVeranstaltungName()).isEqualTo(ligatabelleDO.getveranstaltungName());
        assertThat(actualLigaSyncTabelleDTO.getWettkampfId()).isEqualTo(ligatabelleDO.getwettkampfId());
        assertThat(actualLigaSyncTabelleDTO.getWettkampfTag()).isEqualTo(ligatabelleDO.getwettkampfTag());
        assertThat(actualLigaSyncTabelleDTO.getMannschaftId()).isEqualTo(ligatabelleDO.getmannschaftId());
        assertThat(actualLigaSyncTabelleDTO.getMatchpktGegen()).isEqualTo(ligatabelleDO.getMatchpktGegen());
        assertThat(actualLigaSyncTabelleDTO.getSatzpkt()).isEqualTo(ligatabelleDO.getsatzpkt());
        assertThat(actualLigaSyncTabelleDTO.getSatzpktDifferenz()).isEqualTo(ligatabelleDO.getSatzpktDifferenz());
        assertThat(actualLigaSyncTabelleDTO.getSortierung()).isEqualTo(ligatabelleDO.getsortierung());
        assertThat(actualLigaSyncTabelleDTO.getTabellenplatz()).isEqualTo(ligatabelleDO.gettabellenplatz());

        //verify invocations
        verify(ligatabelleComponent).getLigatabelleVeranstaltung(wettkampfId);
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);
    }

    @Test
    public void testFindByWettkampfId() {
        LigamatchDO ligamatchDO = getLigamatchDO();

        final List<LigamatchDO> ligatabelleDOList = Collections.singletonList(ligamatchDO);
        when(matchComponent.getLigamatchDOsByWettkampfId(anyLong())).thenReturn(ligatabelleDOList);
        final List<LigaSyncMatchDTO> actual = underTest.findByWettkampfId(wettkampfId);
        assertThat(actual).isNotNull().hasSize(1);

        final LigaSyncMatchDTO actualLigaSyncMatchDTO = actual.get(0);

        assertThat(actualLigaSyncMatchDTO).isNotNull();
        assertThat(actualLigaSyncMatchDTO.getWettkampfId()).isEqualTo(ligamatchDO.getWettkampfId());
        // verify invocations
        verify(matchComponent).getLigamatchDOsByWettkampfId(anyLong());
        /*
        assertThat(actualLigaSyncMatchDTO.getMannschaftId()).isEqualTo(ligatabelleDO.getmannschaftId());
        assertThat(actualLigaSyncMatchDTO.get).isEqualTo(ligatabelleDO.getDsbMitgliedId());
        assertThat(actualLigaSyncMatchDTO.getRueckennummer()).isEqualTo(ligatabelleDO.getRueckennummer());
        */
        //verify invocations
        //verify(ligatabelleComponent).getLigatabelleVeranstaltung(wettkampfId);
        //Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);
        // Passe findByWettkampfID
        /*
            final PasseDO passeDo = getPasseDO();
            final List<PasseDO> passeDoList = Collections.singletonList(passeDo);
            //configure Mocks
            when(passeComponent.findByWettkampfId(anyLong())).thenReturn(passeDoList);
            // call test method
            final List<PasseDTO> actual = underTest.findByWettkampfId(1L);

            // assert result
            assertThat(actual)
                    .isNotNull()
                    .hasSize(1);

            final PasseDTO actualDTO = actual.get(0);

            assertThat(actualDTO).isNotNull();
            assertThat(actualDTO.getId()).isEqualTo(passeDo.getId());

            // verify invocations
            verify(passeComponent).findByWettkampfId(anyLong());
            */
    }

    @Test
    public void testGetLigapassenOffline() {
        final PasseDO passeDo = getPasseDO();
        final List<PasseDO> passeDoList = Collections.singletonList(passeDo);
        //configure Mocks
        when(passeComponent.findByWettkampfId(anyLong())).thenReturn(passeDoList);
        // call test method
        final List<LigaSyncPasseDTO> actual = underTest.getLigapassenOffline(1L);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final LigaSyncPasseDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(passeDo.getId());
        assertThat(actualDTO.getWettkampfId()).isEqualTo(passeDo.getPasseWettkampfId());

        // verify invocations
        verify(passeComponent).findByWettkampfId(anyLong());
    }
    /*
    @Test
    public void findById_Null() {
        when(matchComponent.findById(anyLong())).thenReturn(null);
        // expect a NPE as the null-state should be checked in MatchComponentImpl
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.findById(MATCH_ID));
    }

        @Test
    public void testFindByWettkampfId() {
        final PasseDO passeDo = getPasseDO();
        final List<PasseDO> passeDoList = Collections.singletonList(passeDo);
        //configure Mocks
        when(passeComponent.findByWettkampfId(anyLong())).thenReturn(passeDoList);
        // call test method
        final List<PasseDTO> actual = underTest.findByWettkampfId(1L);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final PasseDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(passeDo.getId());

        // verify invocations
        verify(passeComponent).findByWettkampfId(anyLong());
    }
    */

    /*
    @Test
    public void testGetMannschaftsmitgliedernOffline() {
        MatchDO matchDO = getMatchDO();
        MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();
        MannschaftsmitgliedComponent mannschaftsmitgliedComponentMock = mock(MannschaftsmitgliedComponent.class);
        MatchComponent matchComponentMock = mock(MatchComponent.class);

        long scheibenNummer = 1;
        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);
        when(matchComponentMock.findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong())).thenReturn(matchDO);
        when(mannschaftsmitgliedComponent.findSchuetzenInUebergelegenerLiga(anyLong(), anyLong())).thenReturn(mannschaftsmitgliedDOList);

        final List<LigaSyncMannschaftsmitgliedDTO> actualMannschaftsmitgliedDOList = underTest.getMannschaftsmitgliedernOffline(wettkampfId);
        LigaSyncMannschaftsmitgliedDTO mannschaftsmitgliedDTO = actualMannschaftsmitgliedDOList.get(0);
        assertThat(actualMannschaftsmitgliedDOList).isNotNull().hasSize(8);

        //UserService userService = mock(UserService.class);
        //User user = mock(User.class);
        //when(userService.getUserById(anyLong())).thenReturn(user);

        assertThat(mannschaftsmitgliedDTO.id.equals(mannschaftsmitgliedDO.getId()));
        assertThat(mannschaftsmitgliedDTO.mannschaftId.equals(mannschaftsmitgliedDO.getMannschaftId()));
        assertThat(mannschaftsmitgliedDTO.dsbMitgliedId.equals(mannschaftsmitgliedDO.getDsbMitgliedId()));

        //verify invocations
        verify(matchComponent).findByWettkampfIDMatchNrScheibenNr(wettkampfId, 1L, scheibenNummer);
        verify(mannschaftsmitgliedComponent).findSchuetzenInUebergelegenerLiga(mannschaftsId, wettkampfId);
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
    }
    */

    @Test
    public void update() {
        // prepare test data
        WettkampfDTO input = getWettkampfDTO();
        //final WettkampfExtDTO input = getWettkampfExtDTO();
        final WettkampfDO expected = getWettkampfDO();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        //when(wettkampfComponent.findById(anyLong())).thenReturn(expected);
        when(wettkampfComponent.update(any(), anyLong())).thenReturn(expected);

        try {
            // call test method
            final WettkampfExtDTO actual = underTest.update(wettkampfId, input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(input.getId());

            // verify invocations
            verify(wettkampfComponent).update(wettkampfDOArgumentCaptor.capture(), anyLong());

            final WettkampfDO updatedWettkampf = wettkampfDOArgumentCaptor.getValue();

            assertThat(updatedWettkampf).isNotNull();
            assertThat(updatedWettkampf.getId()).isEqualTo(input.getId());

        } catch (NoPermissionException e) {
        }
    }

    @Test
    public void update_Null() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.update(wettkampfId,null, principal));
    }

    /*
    @Test
    public void updateNoPermission() {
        // prepare test data
        final WettkampfDTO input = getWettkampfDTO();

        final WettkampfDO expected = getWettkampfDO();

        // configure mocks
        when(wettkampfComponent.update(expected, anyLong())).thenReturn(expected);
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionAusrichter(any(), anyLong())).thenReturn(false);

        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(()-> underTest.update(wettkampfId, input, principal));

    }
    */
}
