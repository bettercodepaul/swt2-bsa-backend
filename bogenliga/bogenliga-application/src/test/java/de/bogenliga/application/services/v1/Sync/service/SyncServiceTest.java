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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import de.bogenliga.application.services.v1.mannschaftsmitglied.service.MannschaftsMitgliedService;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.services.v1.match.service.MatchService;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncLigatabelleDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMannschaftsmitgliedDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncPasseDTO;
import de.bogenliga.application.services.v1.sync.model.SyncWrapper;
import de.bogenliga.application.services.v1.sync.model.WettkampfExtDTO;
import de.bogenliga.application.services.v1.sync.service.SyncService;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.*;
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
    private MatchService matchService;

    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;

    @Mock
    private MannschaftsMitgliedService mannschaftsMitgliedService;


    @InjectMocks
    private SyncService underTest;

    @Captor
    private ArgumentCaptor<WettkampfDO> wettkampfDOArgumentCaptor;


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

    protected static final String MATCH_MANNSCHAFT_NAME = "TSV_Grafenberg";
    protected static final Long MATCH_NAECHSTE_MATCH_ID = 1L;
    protected static final Long MATCH_NAECHSTE_NAECHSTE_MATCH_ID = 1L;
    protected static final String MATCH_WETTKAMP_TYP_ID = "0";
    protected static final Long MATCH_WETTKAMPF_TAG = 1L;
    protected static final Integer MATCH_RUECKENNUMMER = 2;
    protected static final Integer STRAFPUNKTe_SATZ_1 = 1;
    protected static final Integer STRAFPUNKTe_SATZ_2 = 2;
    protected static final Integer STRAFPUNKTe_SATZ_3 = 3;
    protected static final Integer STRAFPUNKTe_SATZ_4 = 4;
    protected static final Integer STRAFPUNKTe_SATZ_5 = 5;


    protected static final String MATCH_NAME_GEGNER = "TSV Grafenberg Gegner";
    protected static final Long MATCH_SCHEIBENNUMMER_GEGNER = 4L;
    protected static final Long MATCH_ID_GEGNER = 2L;

    // Passe aus PassServiceTest
    private static final Long PASSE_ID = 1L;
    private static final Long PASSE_LFDR_NR = 2L;
    private static final Integer PASSE_PFEIL_1 = 10;
    private static final Integer PASSE_PFEIL_2 = 8;
    private static final Long PASSE_DSB_MITGLIED_ID = 123L;
    private static final Integer[] PASSE_RINGZAHL = new Integer[]{PASSE_PFEIL_1, PASSE_PFEIL_2};

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
    private static final String mannschaftName = "GegnerMannschaft";
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
        expectedLigatabelleDO.setMatchCount(matchCount);

        return expectedLigatabelleDO;
    }

    protected MatchDO getMatchDO() {
        return new MatchDO(
                MATCH_ID,
                MATCH_NR,
                MATCH_WETTKAMPF_ID,
                MATCH_MANNSCHAFT_ID,
                MATCH_BEGEGNUNG,
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

    public static MannschaftsMitgliedDTO getMannschaftsmitgliedDTO(){
        return new MannschaftsMitgliedDTO(
          id, mannschaftsId, dsbMitgliedId, dsbMitgliedEingesetzt, rueckennummer
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
        ligamatchBE.setMatchScheibennummer(MATCH_SCHEIBENNUMMER);
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

    public static WettkampfDO getWettkampfDOWithToken() {
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
                wettkampfAusrichter,
                offlineToken
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

    public static LigaSyncMatchDTO getLigaSyncMatchDTO() {
        return new LigaSyncMatchDTO (
                MATCH_ID,
                version,
                wettkampfId,
                MATCH_NR.intValue(),
                MATCH_SCHEIBENNUMMER.intValue(),
                MATCH_MATCHPUNKTE,
                MATCH_SATZPUNKTE,
                MATCH_MANNSCHAFT_ID,
                MATCH_MANNSCHAFT_NAME,
                MATCH_NAME_GEGNER,
                MATCH_SCHEIBENNUMMER_GEGNER.intValue(),
                MATCH_ID_GEGNER,
                MATCH_NAECHSTE_MATCH_ID,
                MATCH_NAECHSTE_NAECHSTE_MATCH_ID,
                STRAFPUNKTe_SATZ_1,
                STRAFPUNKTe_SATZ_2,
                STRAFPUNKTe_SATZ_3,
                STRAFPUNKTe_SATZ_4,
                STRAFPUNKTe_SATZ_5
        );
    }

    private static MatchDTO getMatchDTO () {
        List<PasseDTO> passen = new ArrayList<>();
        passen.add(getPasseDTO());
        passen.add(getPasseDTO());

        return new MatchDTO (
                MATCH_ID,
                MATCH_NR,
                version,
                wettkampfId,
                MATCH_MANNSCHAFT_ID,
                MATCH_BEGEGNUNG,
                MATCH_SCHEIBENNUMMER,
                MATCH_MATCHPUNKTE,
                MATCH_SATZPUNKTE,
                passen,
                MATCH_STRAFPUNKTE_SATZ1,
                MATCH_STRAFPUNKTE_SATZ2,
                MATCH_STRAFPUNKTE_SATZ3,
                MATCH_STRAFPUNKTE_SATZ4,
                MATCH_STRAFPUNKTE_SATZ5
        );
    }

    public static LigaSyncPasseDTO getLigaSyncPasseDTO() {
        return new LigaSyncPasseDTO (
                PASSE_ID,
                VERSION,
                MATCH_ID,
                MATCH_MANNSCHAFT_ID,
                MATCH_WETTKAMPF_ID,
                PASSE_LFDR_NR,
                dsbMitgliedId,
                PASSE_RINGZAHL
        );
    }

    private static PasseDTO getPasseDTO () {
        return new PasseDTO (
                PASSE_ID,
                MATCH_MANNSCHAFT_ID,
                MATCH_WETTKAMPF_ID,
                MATCH_NR,
                MATCH_ID,
                PASSE_LFDR_NR,
                PASSE_DSB_MITGLIED_ID,
                PASSE_RINGZAHL
        );
    }

    private static LigaSyncLigatabelleDTO getLigaSyncLigatabelleDTO() {
        return new LigaSyncLigatabelleDTO (
                veranstaltungId,
                veranstaltungName,
                wettkampfId,
                wettkampfTag,
                mannschaftId,
                mannschaftName,
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

    private static LigaSyncMannschaftsmitgliedDTO getLigaSyncMannschaftsMitgliedDTO (Long id) {
        return new LigaSyncMannschaftsmitgliedDTO(
                id,
                version,
                mannschaftsId,
                dsbMitgliedId,
                rueckennummer
        );
    }

    protected List<LigaSyncMannschaftsmitgliedDTO> getLigaSyncMannschaftsMitgliederDTO() {
        List<LigaSyncMannschaftsmitgliedDTO> mm_sync_dtos = new ArrayList<>();
        mm_sync_dtos.add(getLigaSyncMannschaftsMitgliedDTO(MM_ID_1));
        mm_sync_dtos.add(getLigaSyncMannschaftsMitgliedDTO(MM_ID_2));
        mm_sync_dtos.add(getLigaSyncMannschaftsMitgliedDTO(MM_ID_3));
        return mm_sync_dtos;
    }

    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(CURRENT_USER_ID));
    }


    @Test
    public void testLigaSyncMannschaftsmitgliedDTO(){
        final LigaSyncMannschaftsmitgliedDTO ligaSyncMannschaftsmitgliedDTO = new LigaSyncMannschaftsmitgliedDTO(id,version,mannschaftId,dsbMitgliedId, rueckennummer);

        ligaSyncMannschaftsmitgliedDTO.setId(id);
        assertEquals(id, ligaSyncMannschaftsmitgliedDTO.getId());

        ligaSyncMannschaftsmitgliedDTO.setMannschaftId(mannschaftId);
        assertEquals(mannschaftId, ligaSyncMannschaftsmitgliedDTO.getMannschaftId());

        ligaSyncMannschaftsmitgliedDTO.setDsbMitgliedId(dsbMitgliedId);
        assertEquals(dsbMitgliedId, ligaSyncMannschaftsmitgliedDTO.getDsbMitgliedId());

        final long version01 = version;

        // test setter by setting instance.version to version01
        ligaSyncMannschaftsmitgliedDTO.setVersion(version01);
        // now create variable and set it to whatever instance.version is ( expected is version01 )
        final long version02 = ligaSyncMannschaftsmitgliedDTO.getVersion();
        // check if version of instance equals version01 variable
        assertEquals(version01, version02);

        ligaSyncMannschaftsmitgliedDTO.setRueckennummer(rueckennummer);
        assertEquals(rueckennummer, ligaSyncMannschaftsmitgliedDTO.getRueckennummer());
    }

    @Test
    public void equals1(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getVeranstaltungName(),test2.getVeranstaltungName());
    }
    @Test
    public void equals2(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setVeranstaltungName("xxx");
        assertNotEquals(test1.getVeranstaltungName(),test2.getVeranstaltungName());
    }

    @Test
    public void equals0(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getVeranstaltungName(),test2.getVeranstaltungName());
    }

    @Test
    public void equals3(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        assertEquals(test1.getVeranstaltungId(),test2.getVeranstaltungId());
    }
    @Test
    public void equals4(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setVeranstaltungId(99L);
        assertNotEquals(test1.getVeranstaltungId(),test2.getVeranstaltungId());
    }

    @Test
    public void equals5(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getWettkampfId(),test2.getWettkampfId());
    }
    @Test
    public void equals6(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setWettkampfId(99L);
        assertNotEquals(test1.getWettkampfId(),test2.getWettkampfId());
    }

    @Test
    public void equals7(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getWettkampfTag(),test2.getWettkampfTag());
    }
    @Test
    public void equals8(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setWettkampfTag(17);
        assertNotEquals(test1.getWettkampfTag(),test2.getWettkampfTag());
    }

    @Test
    public void equals9(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getMannschaftId(),test2.getMannschaftId());
    }
    @Test
    public void equals10(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setMannschaftId(99L);
        assertNotEquals(test1.getMannschaftId(),test2.getMannschaftId());
    }

    @Test
    public void equals11(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getMannschaftName(),test2.getMannschaftName());
    }
    @Test
    public void equals12(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setMannschaftName("XXXX");
        assertNotEquals(test1.getMannschaftName(),test2.getMannschaftName());
    }

    @Test
    public void equals13(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getMatchpkt(),test2.getMatchpkt());
    }
    @Test
    public void equals14(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setMatchpkt(99);
        assertNotEquals(test1.getMatchpkt(),test2.getMatchpkt());
    }
    @Test
    public void equals15(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getMatchpktGegen(),test2.getMatchpktGegen());
    }
    @Test
    public void equals16(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setMatchpktGegen(99);
        assertNotEquals(test1.getMatchpktGegen(),test2.getMatchpktGegen());
    }

    @Test
    public void equals17(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getSatzpkt(),test2.getSatzpkt());
    }
    @Test
    public void equals18(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setSatzpkt(99);
        assertNotEquals(test1.getSatzpkt(),test2.getSatzpkt());
    }

    @Test
    public void equals19(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getSatzpktGegen(),test2.getSatzpktGegen());
    }
    @Test
    public void equals20(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setSatzpktGegen(99);
        assertNotEquals(test1.getSatzpktGegen(),test2.getSatzpktGegen());
    }

    @Test
    public void equals21(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getSatzpktDifferenz(),test2.getSatzpktDifferenz());
    }
    @Test
    public void equals22(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setSatzpktDifferenz(99);
        assertNotEquals(test1.getSatzpktDifferenz(),test2.getSatzpktDifferenz());
    }

    @Test
    public void equals23(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getSortierung(),test2.getSortierung());
    }
    @Test
    public void equals24(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setSortierung(99);
        assertNotEquals(test1.getSortierung(),test2.getSortierung());
    }

    @Test
    public void equals25(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getTabellenplatz(),test2.getTabellenplatz());
    }
    @Test
    public void equals26(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setTabellenplatz(99);
        assertNotEquals(test1.getTabellenplatz(),test2.getTabellenplatz());
    }

    @Test
    public void equals27(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();

        assertEquals(test1.getMatchCount(),test2.getMatchCount());
    }
    @Test
    public void equals28(){
        final LigaSyncLigatabelleDTO test1 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test2 = getLigaSyncLigatabelleDTO();
        test2.setMatchCount(99);
        assertNotEquals(test1.getMatchCount(),test2.getMatchCount());
    }


    // Start of testing
    // cover equals cases by creating another ligasynchtabelle object and cover all cases ...
    @Test
    public void testLigaSyncLigatabelleDTO(){
        final LigaSyncLigatabelleDTO ligaSyncLigatabelleDTO = getLigaSyncLigatabelleDTO();
        //to make it more readable, I ll introduce the parameters for creating the instance
        Long veranstaltungsID01 = 7L;
        String veranstaltungsName01 = "neuerName";
        Long wettkampfId01 = 3L;
        Integer wettkampfTag01 = 1;
        Long mannschaftId01 = 3L;
        String mannschaftName01 = "neuererName";
        Integer matchpkt01 = 5;
        Integer matchpktGegen01 = 1;
        Integer satzpkt01 = 17;
        Integer satzpktGegen01 = 5;
        Integer satzpktDifferenz01 = 14;
        Integer sortierung01 = 2;
        Integer tabellenplatz01 = 17;
        Integer matchCount01 = 1;
        // see, now this does look better!
        final LigaSyncLigatabelleDTO ligaSyncLigatabelleDTO02 = new LigaSyncLigatabelleDTO(
                veranstaltungsID01, veranstaltungsName01, wettkampfId01, wettkampfTag01,
                mannschaftId01, mannschaftName01, matchpkt01, matchpktGegen01, satzpkt01,
                satzpktGegen01, satzpktDifferenz01, sortierung01, tabellenplatz01, matchCount01
        );

        Long veranstaltungId = 17L;
        ligaSyncLigatabelleDTO.setVeranstaltungId(veranstaltungId);
        assertEquals(veranstaltungId, ligaSyncLigatabelleDTO.getVeranstaltungId());

        //final String veranstalungsname =  "Test";
        ligaSyncLigatabelleDTO.setVeranstaltungName(veranstaltungName);
        assertEquals(veranstaltungName, ligaSyncLigatabelleDTO.getVeranstaltungName());

        ligaSyncLigatabelleDTO.setWettkampfId(wettkampfId);
        assertEquals(wettkampfId, ligaSyncLigatabelleDTO.getWettkampfId());

        final Integer wettkampfTag = 3;
        ligaSyncLigatabelleDTO.setWettkampfTag(wettkampfTag);
        assertEquals(wettkampfTag, ligaSyncLigatabelleDTO.getWettkampfTag());

        ligaSyncLigatabelleDTO.setMannschaftId(mannschaftsId);
        assertEquals(mannschaftsId, ligaSyncLigatabelleDTO.getMannschaftId());

        final String mannschaftName = "default_mannschaft";
        ligaSyncLigatabelleDTO.setMannschaftName(mannschaftName);
        assertEquals(mannschaftName, ligaSyncLigatabelleDTO.getMannschaftName());

        final Integer matchPunkt = 17;
        ligaSyncLigatabelleDTO.setMatchpkt(matchPunkt);
        assertEquals(matchPunkt, ligaSyncLigatabelleDTO.getMatchpkt());

        ligaSyncLigatabelleDTO.setMatchpkt(matchPunkt);
        assertEquals(matchPunkt,ligaSyncLigatabelleDTO.getMatchpkt());

        ligaSyncLigatabelleDTO.setMatchpktGegen(matchPunkt);
        assertEquals(matchPunkt,ligaSyncLigatabelleDTO.getMatchpktGegen());

        final Integer satzPunkt = 42;
        ligaSyncLigatabelleDTO.setSatzpkt(satzPunkt);
        assertEquals(satzPunkt,ligaSyncLigatabelleDTO.getSatzpkt());

        ligaSyncLigatabelleDTO.setSatzpktGegen(satzPunkt);
        assertEquals(satzPunkt,ligaSyncLigatabelleDTO.getSatzpktGegen());

        ligaSyncLigatabelleDTO.setSatzpktDifferenz(satzPunkt);
        assertEquals(satzPunkt,ligaSyncLigatabelleDTO.getSatzpktDifferenz());

        final Integer sortierung = 0;
        ligaSyncLigatabelleDTO.setSortierung(sortierung);
        assertEquals(sortierung,ligaSyncLigatabelleDTO.getSortierung());

        final Integer tabellenPlatz = 8;
        ligaSyncLigatabelleDTO.setTabellenplatz(tabellenPlatz);
        assertEquals(tabellenPlatz,ligaSyncLigatabelleDTO.getTabellenplatz());

        final Integer matchCount = 0;
        ligaSyncLigatabelleDTO.setTabellenplatz(matchCount);
        assertEquals(matchCount,ligaSyncLigatabelleDTO.getMatchCount());


    }

    @Test
    public void testLigaSyncLigatabelleDTOToString(){
        final LigaSyncLigatabelleDTO ligaSyncLigatabelleDTO = getLigaSyncLigatabelleDTO();
        assertNotNull(ligaSyncLigatabelleDTO.toString());
    }
    @Test
    public void testLigaSyncLigatabelleDTOEquals(){
        final LigaSyncLigatabelleDTO ligaSyncLigatabelleDTO = getLigaSyncLigatabelleDTO();
        //to make it more readable, I ll introduce the parameters for creating the instance
        Long veranstaltungsID01 = 77L;
        String veranstaltungsName01 = "neuerName";
        Long wettkampfId01 = 33L;
        Integer wettkampfTag01 = 99;
        Long mannschaftId01 = 33L;
        String mannschaftName01 = "neuererName";
        Integer matchpkt01 = 99;
        Integer matchpktGegen01 = 99;
        Integer satzpkt01 = 99;
        Integer satzpktGegen01 = 99;
        Integer satzpktDifferenz01 = 99;
        Integer sortierung01 = 99;
        Integer tabellenplatz01 = 99;
        Integer matchCount01 = 0;
        // see, now this does look better!
        final LigaSyncLigatabelleDTO ligaSyncLigatabelleDTO02 = new LigaSyncLigatabelleDTO(
                veranstaltungsID01, veranstaltungsName01, wettkampfId01, wettkampfTag01,
                mannschaftId01, mannschaftName01, matchpkt01, matchpktGegen01, satzpkt01,
                satzpktGegen01, satzpktDifferenz01, sortierung01, tabellenplatz01, matchCount01
        );

        final LigaSyncLigatabelleDTO ligaSyncLigatabelleDTO03 = getLigaSyncLigatabelleDTO();
        //a != b, -> false
        assertNotEquals(ligaSyncLigatabelleDTO, ligaSyncLigatabelleDTO02);
        // a = a, -> true
        assertEquals(ligaSyncLigatabelleDTO02, ligaSyncLigatabelleDTO02);

        // a ia not instanceof b -> false
        assertNotEquals(ligaSyncLigatabelleDTO, new Object());

        // a = b -> true
        assertEquals(ligaSyncLigatabelleDTO03, ligaSyncLigatabelleDTO);
    }

    @Test
    public void ligaSyncPasseTestDTO(){

        Long match_id = 17L;
        Long match_id_2 = 18L;
        Long lfdnr = 31L;
        Integer rueckennummer = 5;
        Integer[] ringzahl = {10,9,8,7};
        LigaSyncPasseDTO test = new LigaSyncPasseDTO(id, version, match_id, mannschaftId, wettkampfId, lfdnr, dsbMitgliedId, rueckennummer, ringzahl);
        LigaSyncPasseDTO test_2 = new LigaSyncPasseDTO(id, version, match_id_2, mannschaftId, wettkampfId, lfdnr, dsbMitgliedId, rueckennummer, ringzahl);
        assertThat(test.getId()).isEqualTo(id);
        assertThat(test.getVersion()).isEqualTo(version);
        assertThat(test.getMatchId()).isEqualTo(match_id);
        assertThat(test.getMannschaftId()).isEqualTo(mannschaftId);
        assertThat(test.getWettkampfId()).isEqualTo(wettkampfId);
        assertThat(test.getLfdNr()).isEqualTo(lfdnr);
        assertThat(test.getDsbMitgliedId()).isEqualTo(dsbMitgliedId);
        assertThat(test.getRueckennummer()).isEqualTo(rueckennummer);
        assertThat(test.getRingzahl()).isEqualTo(ringzahl);

        assertEquals(test.equals(test),test.equals(test));
        assertNotEquals(test.equals(test),test.equals(test_2 ));



    }

    @Test
    public void testLigaSynchTabelleDTO(){
        final LigaSyncLigatabelleDTO test01 = getLigaSyncLigatabelleDTO();
        final LigaSyncLigatabelleDTO test02 = getLigaSyncLigatabelleDTO();

        assertTrue(test01.equals(test02) && test02.equals(test01) );
        assertNotEquals(test01, null);
        assertNotEquals(test02, null);
        assertNotEquals(test01, new Object());
        assertNotEquals(test02,(new Object()));
        assertEquals(test01.hashCode(), test02.hashCode());

        test01.setVeranstaltungName(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setVeranstaltungId(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setWettkampfId(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setWettkampfTag(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setMannschaftId(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setMannschaftName(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setMatchpkt(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setMatchpktGegen(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setSatzpkt(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setSatzpktGegen(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setSatzpktDifferenz(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setSortierung(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setTabellenplatz(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
        test01.setMatchCount(null);
        assertNotEquals(test01.hashCode(), test02.hashCode());
    }


    @Test
    public void testgetLigatabelleVeranstaltungToString() {
        final LigaSyncLigatabelleDTO ligaSyncLigatabelleDTO = getLigaSyncLigatabelleDTO();
        final String actual = ligaSyncLigatabelleDTO.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(veranstaltungId))
                .contains(Long.toString(wettkampfId))
                .contains(Long.toString(mannschaftId));
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
        assertThat(actualLigaSyncTabelleDTO.getMatchCount()).isEqualTo(ligatabelleDO.getMatchCount());

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
    }

    @Test
    public void ligaSyncMatchDTOToString(){
        final LigaSyncMatchDTO ligaSyncMatchDTO = getLigaSyncMatchDTO();
        final String actual = ligaSyncMatchDTO.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(MATCH_ID))
                .contains(Long.toString(wettkampfId));
    }

    @Test
    public void ligaSyncPasseDTOToString(){
        final LigaSyncPasseDTO ligaSyncPasseDTO = getLigaSyncPasseDTO();
        final String actual = ligaSyncPasseDTO.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(PASSE_ID))
                .contains(Long.toString(MATCH_ID));
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

    @Test
    public void ligaSyncMatchDTOGetterSetterTest(){
        final LigaSyncMatchDTO ligaSyncMatchDTO = getLigaSyncMatchDTO();
        LigaSyncMatchDTO newLigaSyncMatchDTO = new LigaSyncMatchDTO();
        newLigaSyncMatchDTO.setMatchIdGegner(ligaSyncMatchDTO.getMatchIdGegner());
        newLigaSyncMatchDTO.setMatchNr(ligaSyncMatchDTO.getMatchNr());
        newLigaSyncMatchDTO.setWettkampfId(ligaSyncMatchDTO.getWettkampfId());
        newLigaSyncMatchDTO.setVersion(ligaSyncMatchDTO.getVersion());
        newLigaSyncMatchDTO.setId(ligaSyncMatchDTO.getId());
        newLigaSyncMatchDTO.setMatchpunkte(ligaSyncMatchDTO.getMatchpunkte());
        newLigaSyncMatchDTO.setMannschaftName(ligaSyncMatchDTO.getMannschaftName());
        newLigaSyncMatchDTO.setMatchScheibennummer(ligaSyncMatchDTO.getMatchScheibennummer());
        newLigaSyncMatchDTO.setMannschaftId(ligaSyncMatchDTO.getMannschaftId());
        newLigaSyncMatchDTO.setNaechsteMatchId(ligaSyncMatchDTO.getNaechsteMatchId());
        newLigaSyncMatchDTO.setNaechsteNaechsteMatchNrMatchId(ligaSyncMatchDTO.getNaechsteNaechsteMatchNrMatchId());
        newLigaSyncMatchDTO.setNameGegner(ligaSyncMatchDTO.getNameGegner());
        newLigaSyncMatchDTO.setSatzpunkte(ligaSyncMatchDTO.getSatzpunkte());
        newLigaSyncMatchDTO.setScheibennummerGegner(ligaSyncMatchDTO.getScheibennummerGegner());
        newLigaSyncMatchDTO.setStrafpunkteSatz1(ligaSyncMatchDTO.getStrafpunkteSatz1());
        newLigaSyncMatchDTO.setStrafpunkteSatz2(ligaSyncMatchDTO.getStrafpunkteSatz2());
        newLigaSyncMatchDTO.setStrafpunkteSatz3(ligaSyncMatchDTO.getStrafpunkteSatz3());
        newLigaSyncMatchDTO.setStrafpunkteSatz4(ligaSyncMatchDTO.getStrafpunkteSatz4());
        newLigaSyncMatchDTO.setStrafpunkteSatz5(ligaSyncMatchDTO.getStrafpunkteSatz5());

        assertThat(newLigaSyncMatchDTO.getId()).isEqualTo(ligaSyncMatchDTO.getId());
        assertThat(newLigaSyncMatchDTO.getMatchIdGegner()).isEqualTo(ligaSyncMatchDTO.getMatchIdGegner());
        assertThat(newLigaSyncMatchDTO.getMatchNr()).isEqualTo(ligaSyncMatchDTO.getMatchNr());
        assertThat(newLigaSyncMatchDTO.getWettkampfId()).isEqualTo(ligaSyncMatchDTO.getWettkampfId());
        assertThat(newLigaSyncMatchDTO.getVersion()).isEqualTo(ligaSyncMatchDTO.getVersion());
        assertThat(newLigaSyncMatchDTO.getMatchIdGegner()).isEqualTo(ligaSyncMatchDTO.getMatchIdGegner());
        assertThat(newLigaSyncMatchDTO.getMatchpunkte()).isEqualTo(ligaSyncMatchDTO.getMatchpunkte());
        assertThat(newLigaSyncMatchDTO.getMannschaftName()).isEqualTo(ligaSyncMatchDTO.getMannschaftName());
        assertThat(newLigaSyncMatchDTO.getMatchScheibennummer()).isEqualTo(ligaSyncMatchDTO.getMatchScheibennummer());
        assertThat(newLigaSyncMatchDTO.getMannschaftId()).isEqualTo(ligaSyncMatchDTO.getMannschaftId());
        assertThat(newLigaSyncMatchDTO.getMannschaftId()).isEqualTo(ligaSyncMatchDTO.getMannschaftId());
        assertThat(newLigaSyncMatchDTO.getNaechsteMatchId()).isEqualTo(ligaSyncMatchDTO.getNaechsteMatchId());
        assertThat(newLigaSyncMatchDTO.getNaechsteNaechsteMatchNrMatchId()).isEqualTo(ligaSyncMatchDTO.getNaechsteNaechsteMatchNrMatchId());
        assertThat(newLigaSyncMatchDTO.getNameGegner()).isEqualTo(ligaSyncMatchDTO.getNameGegner());
        assertThat(newLigaSyncMatchDTO.getStrafpunkteSatz1()).isEqualTo(ligaSyncMatchDTO.getStrafpunkteSatz1());
        assertThat(newLigaSyncMatchDTO.getStrafpunkteSatz2()).isEqualTo(ligaSyncMatchDTO.getStrafpunkteSatz2());
        assertThat(newLigaSyncMatchDTO.getStrafpunkteSatz3()).isEqualTo(ligaSyncMatchDTO.getStrafpunkteSatz3());
        assertThat(newLigaSyncMatchDTO.getStrafpunkteSatz4()).isEqualTo(ligaSyncMatchDTO.getStrafpunkteSatz4());
        assertThat(newLigaSyncMatchDTO.getStrafpunkteSatz5()).isEqualTo(ligaSyncMatchDTO.getStrafpunkteSatz5());
        assertThat(newLigaSyncMatchDTO.getSatzpunkte()).isEqualTo(ligaSyncMatchDTO.getSatzpunkte());
    }

    @Test
    public void ligaSyncMatchDTOEqualsTest(){
        final LigaSyncMatchDTO ligaSyncMatchDTO = getLigaSyncMatchDTO();
        LigaSyncMatchDTO newLigaSyncMatchDTO = new LigaSyncMatchDTO();
        newLigaSyncMatchDTO.setMatchIdGegner(ligaSyncMatchDTO.getMatchIdGegner());
        newLigaSyncMatchDTO.setMatchNr(ligaSyncMatchDTO.getMatchNr());
        newLigaSyncMatchDTO.setId(ligaSyncMatchDTO.getId());
        newLigaSyncMatchDTO.setWettkampfId(ligaSyncMatchDTO.getWettkampfId());
        newLigaSyncMatchDTO.setVersion(ligaSyncMatchDTO.getVersion());
        newLigaSyncMatchDTO.setMatchpunkte(ligaSyncMatchDTO.getMatchpunkte());
        newLigaSyncMatchDTO.setMannschaftName(ligaSyncMatchDTO.getMannschaftName());
        newLigaSyncMatchDTO.setMatchScheibennummer(ligaSyncMatchDTO.getMatchScheibennummer());
        newLigaSyncMatchDTO.setMannschaftId(ligaSyncMatchDTO.getMannschaftId());
        newLigaSyncMatchDTO.setNaechsteMatchId(ligaSyncMatchDTO.getNaechsteMatchId());
        newLigaSyncMatchDTO.setNaechsteNaechsteMatchNrMatchId(ligaSyncMatchDTO.getNaechsteNaechsteMatchNrMatchId());
        newLigaSyncMatchDTO.setNameGegner(ligaSyncMatchDTO.getNameGegner());
        newLigaSyncMatchDTO.setSatzpunkte(ligaSyncMatchDTO.getSatzpunkte());
        newLigaSyncMatchDTO.setScheibennummerGegner(ligaSyncMatchDTO.getScheibennummerGegner());
        newLigaSyncMatchDTO.setStrafpunkteSatz1(ligaSyncMatchDTO.getStrafpunkteSatz1());
        newLigaSyncMatchDTO.setStrafpunkteSatz2(ligaSyncMatchDTO.getStrafpunkteSatz2());
        newLigaSyncMatchDTO.setStrafpunkteSatz3(ligaSyncMatchDTO.getStrafpunkteSatz3());
        newLigaSyncMatchDTO.setStrafpunkteSatz4(ligaSyncMatchDTO.getStrafpunkteSatz4());
        newLigaSyncMatchDTO.setStrafpunkteSatz5(ligaSyncMatchDTO.getStrafpunkteSatz5());

        assertEquals(newLigaSyncMatchDTO,ligaSyncMatchDTO);
    }

    @Test
    public void ligaSyncPasseDTOEqualsTest(){
        final LigaSyncPasseDTO ligaSyncPasseDTO = getLigaSyncPasseDTO();
        LigaSyncPasseDTO newLigaSyncPasseDTO = new LigaSyncPasseDTO();
        newLigaSyncPasseDTO.setRueckennummer(ligaSyncPasseDTO.getRueckennummer());
        newLigaSyncPasseDTO.setId(ligaSyncPasseDTO.getId());
        newLigaSyncPasseDTO.setVersion(ligaSyncPasseDTO.getVersion());
        newLigaSyncPasseDTO.setMatchId(ligaSyncPasseDTO.getMatchId());
        newLigaSyncPasseDTO.setMannschaftId(ligaSyncPasseDTO.getMannschaftId());
        newLigaSyncPasseDTO.setWettkampfId(ligaSyncPasseDTO.getWettkampfId());
        newLigaSyncPasseDTO.setLfdNr(ligaSyncPasseDTO.getLfdNr());
        newLigaSyncPasseDTO.setDsbMitgliedId(ligaSyncPasseDTO.getDsbMitgliedId());
        newLigaSyncPasseDTO.setRingzahl(ligaSyncPasseDTO.getRingzahl());

        assertEquals(newLigaSyncPasseDTO,ligaSyncPasseDTO);
    }

    @Test
    public void findByWettkampfIdNegative() {
        LigamatchDO ligamatchDO = getLigamatchDO();

        final List<LigamatchDO> ligatabelleDOList = Collections.singletonList(ligamatchDO);
        when(matchComponent.getLigamatchDOsByWettkampfId(anyLong())).thenReturn(ligatabelleDOList);
        // expect a NPE as the null-state should be checked in MatchComponentImpl
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findByWettkampfId(-1));
    }

    @Test
    public void testGetMannschaftsmitgliedernOffline() {
        MatchDO matchDO = getMatchDO();
        MannschaftsmitgliedDO mannschaftsmitgliedDO = getMannschaftsmitgliedDO();

        long scheibenNummer = 1;
        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = Collections.singletonList(mannschaftsmitgliedDO);
        when(matchComponent.findByWettkampfIDMatchNrScheibenNr(anyLong(), eq(1L), anyLong())).thenReturn(matchDO);
        when(mannschaftsmitgliedComponent.findSchuetzenInUebergelegenerLiga(anyLong(), anyLong())).thenReturn(mannschaftsmitgliedDOList);

        final List<LigaSyncMannschaftsmitgliedDTO> actualMannschaftsmitgliedDOList = underTest.getMannschaftsmitgliedernOffline(wettkampfId);
        LigaSyncMannschaftsmitgliedDTO mannschaftsmitgliedDTO = actualMannschaftsmitgliedDOList.get(0);
        assertThat(actualMannschaftsmitgliedDOList).isNotNull().hasSize(8);

        assertThat(mannschaftsmitgliedDTO.getId()).isEqualTo(mannschaftsmitgliedDO.getId());
        assertThat(mannschaftsmitgliedDTO.getMannschaftId()).isEqualTo(mannschaftsmitgliedDO.getMannschaftId());
        assertThat(mannschaftsmitgliedDTO.getDsbMitgliedId()).isEqualTo(mannschaftsmitgliedDO.getDsbMitgliedId());

        //verify invocations
        verify(matchComponent).findByWettkampfIDMatchNrScheibenNr(wettkampfId, 1L, scheibenNummer);
        verify(mannschaftsmitgliedComponent, times(8)).findSchuetzenInUebergelegenerLiga(any(), any());
    }

    @Test
    public void update() {
        // prepare test data
        WettkampfDTO input = getWettkampfDTO();
        long id = input.getId();
        final WettkampfDO getDO = getWettkampfDO();
        final WettkampfExtDTO result = getWettkampfExtDTO();
        final WettkampfDO expected = getWettkampfDOWithToken();

        // configure mocks
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(wettkampfComponent.findById(anyLong())).thenReturn(getDO);
        when(wettkampfComponent.update(any(), anyLong())).thenReturn(expected);

        try {
            // call test method
            final List<WettkampfExtDTO> actual = underTest.getToken(id, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.get(0).getId()).isEqualTo(input.getId());
            assertThat(actual.get(0).getOfflineToken()).isNotNull();
            assertThat(actual.get(0).getOfflineToken()).isEqualTo(result.getOfflineToken());

            // verify invocations
            verify(wettkampfComponent).update(wettkampfDOArgumentCaptor.capture(), anyLong());

            final WettkampfDO updatedWettkampf = wettkampfDOArgumentCaptor.getValue();

            assertThat(updatedWettkampf).isNotNull();
            assertThat(updatedWettkampf.getId()).isEqualTo(input.getId());

        } catch (NoPermissionException e) {
        }
    }


    @Test
    public void getTokenNegativeWettkampfIdTest() {
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getToken(-1, principal));
    }

    @Test
    public void testSynchronizeMatchesAndPassen() throws NoPermissionException {

        // Setting up incoming data for synchronizeMatchesAndPassen
        // 4 Matches in total
        ArrayList<LigaSyncMatchDTO> ligaSyncMatchDTOs = new ArrayList<>();
        ligaSyncMatchDTOs.add(getLigaSyncMatchDTO());
        ligaSyncMatchDTOs.add(getLigaSyncMatchDTO());
        ligaSyncMatchDTOs.add(getLigaSyncMatchDTO());
        ligaSyncMatchDTOs.add(getLigaSyncMatchDTO());

        // Provide 4 different MatchIDs
        Long[] matchIDs = new Long[] {MATCH_ID, 2L, 3L, 4L};

        // Have 4 Matches with 4 different IDs to check, whether the Passen will get set correctly
        ligaSyncMatchDTOs.get(1).setId(matchIDs[1]);
        ligaSyncMatchDTOs.get(2).setId(matchIDs[2]);
        ligaSyncMatchDTOs.get(3).setId(matchIDs[3]);

        // Change two MatchNr and begegnung to check if synchronizeMatchesAndPassen can seperate Matches
        ligaSyncMatchDTOs.get(2).setMatchNr(112);
        ligaSyncMatchDTOs.get(3).setMatchNr(112);

        ArrayList<LigaSyncPasseDTO> ligaSyncPasseDTOs = new ArrayList<>();
        ligaSyncPasseDTOs.add(getLigaSyncPasseDTO());
        ligaSyncPasseDTOs.add(getLigaSyncPasseDTO());
        ligaSyncPasseDTOs.add(getLigaSyncPasseDTO());
        ligaSyncPasseDTOs.add(getLigaSyncPasseDTO());

        // Change MatchID, so that every Match has one Passe
        ligaSyncPasseDTOs.get(1).setMatchId(matchIDs[1]);
        ligaSyncPasseDTOs.get(2).setMatchId(matchIDs[2]);
        ligaSyncPasseDTOs.get(3).setMatchId(matchIDs[3]);

        // Set up expected result
        List<MatchDTO> expectedMatchDTOs = new ArrayList<>();
        List<PasseDTO> expectedPasseDTOs = new ArrayList<>();

        // Set up expected Passen
        expectedPasseDTOs.add(getPasseDTO());
        expectedPasseDTOs.add(getPasseDTO());
        expectedPasseDTOs.add(getPasseDTO());
        expectedPasseDTOs.add(getPasseDTO());

        expectedPasseDTOs.get(1).setMatchId(matchIDs[1]);
        expectedPasseDTOs.get(2).setMatchId(matchIDs[2]);
        expectedPasseDTOs.get(3).setMatchId(matchIDs[3]);


        // Set up expected Matches
        expectedMatchDTOs.add(getMatchDTO());
        expectedMatchDTOs.add(getMatchDTO());
        expectedMatchDTOs.add(getMatchDTO());
        expectedMatchDTOs.add(getMatchDTO());

        expectedMatchDTOs.get(1).setId(matchIDs[1]);
        expectedMatchDTOs.get(2).setId(matchIDs[2]);
        expectedMatchDTOs.get(3).setId(matchIDs[3]);

        expectedMatchDTOs.get(2).setNr(ligaSyncMatchDTOs.get(2).getMatchNr().longValue());
        expectedMatchDTOs.get(3).setNr(ligaSyncMatchDTOs.get(3).getMatchNr().longValue());

        // Set List<PasseDTO> in MatchDTO
        for (int i = 0; i < expectedMatchDTOs.size(); i++) {
            List<PasseDTO> passen = new ArrayList<>();
            passen.add(expectedPasseDTOs.get(i));

            expectedMatchDTOs.get(i).setPassen(passen);
        }

        when(matchComponent.findById(anyLong())).thenReturn(getMatchDO());

        WettkampfDO w = getWettkampfDO();
        w.setOfflineToken("OfflineTest");
        when(wettkampfComponent.findById(anyLong())).thenReturn(w);
        List<MatchDTO> matchDTOs = new ArrayList<>();
        matchDTOs.add(getMatchDTO());
        matchDTOs.add(getMatchDTO());
        when(matchService.saveMatches(matchDTOs, principal)).thenReturn(matchDTOs);
        //when(wettkampfComponent.deleteOfflineToken();)


        try {
            List<MatchDTO> actual = underTest.synchronizeMatchesAndPassen(ligaSyncMatchDTOs, ligaSyncPasseDTOs, principal);
            assertThat(actual).isNotNull().isNotEmpty().hasSize(4);

            for (int i = 0; i < expectedMatchDTOs.size(); i++) {

                assertThat(actual.get(i).getPassen()).isNotNull().isNotEmpty().hasSize(1);
                assertThat(actual.get(i).getId()).isEqualTo(expectedMatchDTOs.get(i).getId());
                assertThat(actual.get(i).getPassen().get(0).getMatchId()).isEqualTo(expectedMatchDTOs.get(i).getPassen().get(0).getMatchId());
                assertThat(actual.get(i).getNr()).isEqualTo(expectedMatchDTOs.get(i).getNr());
            }

        } catch (NoPermissionException e) {

        }
    }

     @Test
    public void synchronizeMatchesAndPassen_MatchesNull() {
        LigaSyncPasseDTO ligaSyncPasseDTO = getLigaSyncPasseDTO();
        ArrayList<LigaSyncPasseDTO> ligaSyncPasseDTOS = new ArrayList<>();
        ligaSyncPasseDTOS.add(ligaSyncPasseDTO);
        ligaSyncPasseDTOS.add(ligaSyncPasseDTO);

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.synchronizeMatchesAndPassen(null, ligaSyncPasseDTOS, principal));
    }

   @Test
    public void synchronizeMatchesAndPassen_PasseNull() {
        LigaSyncMatchDTO ligaSyncMatchDTO = getLigaSyncMatchDTO();
        ArrayList<LigaSyncMatchDTO> ligaSyncMatchDTOS = new ArrayList<>();
        ligaSyncMatchDTOS.add(ligaSyncMatchDTO);
        ligaSyncMatchDTOS.add(ligaSyncMatchDTO);

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.synchronizeMatchesAndPassen(ligaSyncMatchDTOS, null, principal));
    }

    @Test
    public void updateNoPermission() {
        // configure mocks: Wettkampf is already offline
        when(wettkampfComponent.wettkampfIsOffline(anyLong())).thenReturn(true);
        long tmpLong = anyLong();
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getToken(tmpLong, principal));
    }

    @Test
    public void goOnlineUnconditionally() {
        WettkampfDTO input = getWettkampfDTO();
        long id = input.getId();
        WettkampfDO expected = getWettkampfDO();

        when(wettkampfComponent.deleteOfflineToken(any(), anyLong())).thenReturn(expected);

        WettkampfExtDTO actual = underTest.goOnlineUnconditionally(id, principal);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getOfflineToken()).isNull();
    }

    @Test
    public void testCheckOfflineTokenAndSynchronizeMannschaftsMitglieder() throws Exception {

        List<LigaSyncMannschaftsmitgliedDTO> input = getLigaSyncMannschaftsMitgliederDTO();
        MannschaftsMitgliedDTO toBeSaved = getMannschaftsmitgliedDTO();

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.checkOfflineTokenAndSynchronizeMannschaftsMitglieder(-1, input, principal));


        when(mannschaftsMitgliedService.create(any(), any())).thenReturn(toBeSaved);


        ResponseEntity responseEntity = underTest.checkOfflineTokenAndSynchronizeMannschaftsMitglieder(wettkampfId, input, principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(responseEntity.getBody()).isNotNull();
        List<LigaSyncMannschaftsmitgliedDTO> i = (List<LigaSyncMannschaftsmitgliedDTO>) responseEntity.getBody();
        assertEquals(input.get(0).getId(),i.get(0).getId());

    }

    @Test
    public void testSaveMannschaftsmitgliederNull() {

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.checkOfflineTokenAndSynchronizeMannschaftsMitglieder(wettkampfId, null, principal));
    }
}

