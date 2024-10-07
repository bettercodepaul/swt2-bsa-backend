package de.bogenliga.application.services.v1.match.service;

import java.security.Principal;
import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.*;
import javax.naming.NoPermissionException;

import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.services.v1.match.mapper.MatchDTOMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;

import static java.lang.Math.toIntExact;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PasseComponent passeComponent;

    @Mock
    private VereinComponent vereinComponent;

    @Mock
    private WettkampfComponent wettkampfComponent;

    @Mock
    private VeranstaltungComponent veranstaltungComponent;

    @Mock
    private WettkampfTypComponent wettkampfTypComponent;

    @Mock
    private DsbMannschaftComponent mannschaftComponent;

    @Mock
    private RequiresOnePermissionAspect requiresOnePermissionAspect;

    @Mock
    private Principal principal;

    @Mock
    private MatchComponent matchComponent;

    @Mock
    private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;


    @InjectMocks
    private MatchService underTest;

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
    protected static final String MATCH_MANNSCHAFT_NAME_GEGNER = "SV_Schwieberdingen";
    protected static final Long MATCH_NAECHSTE_MATCH_ID = 1L;
    protected static final Long MATCH_NAECHSTE_NAECHSTE_MATCH_ID = 1L;
    protected static final String MATCH_WETTKAMP_TYP_ID = "0";
    protected static final Long MATCH_WETTKAMPF_TAG = 1L;
    protected static final Integer MATCH_RUECKENNUMMER = 2;

    protected static final Long PLATZHALTER_ID = 99L;



    private static final Long PASSE_ID_1 = 1L;
    private static final Long PASSE_ID_2 = 2L;
    private static final Long PASSE_LFDR_NR = 2L;
    private static final Integer PASSE_SCHUETZE_NR_1 = 1;
    private static final Integer PASSE_SCHUETZE_NR_2 = 2;
    private static final Long PASSE_DSB_MITGLIED_ID = 1L;
    private static final Integer PASSE_PFEIL_1 = 10;
    private static final Integer PASSE_PFEIL_2 = 5;

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

    protected DsbMannschaftDO getPlatzhalter() {
        return new DsbMannschaftDO(
                MATCH_MANNSCHAFT_ID, "Platzhalter", PLATZHALTER_ID, 696969L, M_benutzerId, 4444L, 8L
        );
    }


    protected PasseDO getPasseDO(Long id) {
        return new PasseDO(
                id,
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


    protected VereinDO getVereinDO(Long id) {
        return new VereinDO(
                id,
                VEREIN_NAME,
                VEREIN_DSB_IDENTIFIER,
                REGION_ID,
                REGION_NAME,
                VEREIN_WEBSITE,
                VEREIN_DESCRIPTION,
                VEREIN_ICON,
                VEREIN_OFFSETDATETIME,
                VEREIN_USER,
                VEREIN_OFFSETDATETIME,
                VEREIN_USER,
                VERSION
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


    protected WettkampfDO getWettkampfDO(Long id) {
        return new WettkampfDO(id, W_vid, W_datum, W_strasse, W_plz, W_ortsname, W_ortsinfo, W_begin, W_tag, W_disId, W_typId, null,null,null, null);
    }


    protected WettkampfTypDO getWettkampfTypDO(Long id) {
        return new WettkampfTypDO(id, W_name);
    }


    protected List<MannschaftsmitgliedDO> getMannschaftsMitglieder() {
        List<MannschaftsmitgliedDO> mmdos = new ArrayList<>();
        mmdos.add(getMMDO(MM_ID_1, 5L));
        mmdos.add(getMMDO(MM_ID_2, 6L));
        mmdos.add(getMMDO(MM_ID_3, 7L));
        return mmdos;
    }


    protected PasseDTO getPasseDTO(Long id, Integer nr) {
        PasseDTO passeDTO = PasseDTOMapper.toDTO.apply(getPasseDO(id));
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
        ligamatchBE.setMannschaftNameGegner(MATCH_MANNSCHAFT_NAME_GEGNER);
        return ligamatchBE;
    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(CURRENT_USER_ID));
    }

    @Test
    public void testFindAll() {
        MatchDO matchDO1 = getMatchDO();

        ArrayList<MatchDO> matchesDO = new ArrayList<>();
        matchesDO.add(matchDO1);
        matchesDO.add(matchDO1);

        when(matchComponent.findAll()).thenReturn(matchesDO);
        final List<MatchDTO> actual = underTest.findAll();
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);

    }


    @Test
    public void findById() {
        MatchDO matchDO1 = getMatchDO();
        DsbMannschaftDO mannschaftDO = getMannschaftDO(M_id);
        WettkampfTypDO wettkampftypDO = getWettkampfTypDO(W_typId);
        WettkampfDO wettkampfDO = getWettkampfDO(W_id);
        VereinDO vereinDO = getVereinDO(VEREIN_ID);
        when(matchComponent.findById(anyLong())).thenReturn(matchDO1);
        when(vereinComponent.findById(anyLong())).thenReturn(vereinDO);
        when(mannschaftComponent.findById(anyLong())).thenReturn(mannschaftDO);
        when(wettkampfComponent.findById(MATCH_WETTKAMPF_ID)).thenReturn(wettkampfDO);
        when(wettkampfTypComponent.findById(W_typId)).thenReturn(wettkampftypDO);
        final MatchDTO actual = underTest.findById(MATCH_ID);
        assertThat(actual).isNotNull();
        MatchService.checkPreconditions(actual, MatchService.matchConditionErrors);
    }


    @Test
    public void findById_Null() {
        when(matchComponent.findById(anyLong())).thenReturn(null);
        // expect a NPE as the null-state should be checked in MatchComponentImpl
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.findById(MATCH_ID));
    }


    @Test
    public void findMatchesByIds() {
        MatchDO matchDO1 = getMatchDO();
        DsbMannschaftDO mannschaftDO = getMannschaftDO(M_id);
        WettkampfTypDO wettkampftypDO = getWettkampfTypDO(W_typId);
        WettkampfDO wettkampfDO = getWettkampfDO(W_id);
        VereinDO vereinDO = getVereinDO(VEREIN_ID);
        LigamatchBE ligamatchBE = getLigamatchBE();
        when(matchComponent.getLigamatchById(anyLong())).thenReturn(ligamatchBE);
        when(matchComponent.findById(anyLong())).thenReturn(matchDO1);
        when(vereinComponent.findById(anyLong())).thenReturn(vereinDO);
        when(mannschaftComponent.findById(anyLong())).thenReturn(mannschaftDO);
        when(wettkampfComponent.findById(MATCH_WETTKAMPF_ID)).thenReturn(wettkampfDO);
        when(wettkampfTypComponent.findById(W_typId)).thenReturn(wettkampftypDO);
        final List<MatchDTO> actual = underTest.findMatchesByIds(MATCH_ID, MATCH_ID);
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        MatchService.checkPreconditions(actual.get(0), MatchService.matchConditionErrors);
    }


    @Test
    public void findMatchesByIds_Null() {
        when(matchComponent.findById(anyLong())).thenReturn(null);
        // expect a NPE as the null-state should be checked in MatchComponentImpl
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.findMatchesByIds(MATCH_ID, MATCH_ID));
     }


    @Test
    public void findByMannschaftId() {
        final MatchDO matchDO = getMatchDO();
        final List<MatchDO> matchDOList = Collections.singletonList(matchDO);
        when(matchComponent.findByMannschaftId(anyLong())).thenReturn(matchDOList);
        final List<MatchDTO> actual = underTest.findAllByMannschaftId(MATCH_MANNSCHAFT_ID);
        assertThat(actual).isNotNull().hasSize(1);

        final MatchDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(matchDO.getId());
        assertThat(actualDTO.getMannschaftId()).isEqualTo(matchDO.getMannschaftId());

        //verify invocations
        verify(matchComponent).findByMannschaftId(MATCH_MANNSCHAFT_ID);

        MatchService.checkPreconditions(actualDTO, MatchService.matchConditionErrors);
    }


    @Test
    public void saveMatches() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);
        ArrayList<MatchDTO> matches = new ArrayList<>();
        matches.add(matchDTO);
        matches.add(matchDTO);

        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeam(anyLong())).thenReturn(getMannschaftsMitglieder());
        try {
            final List<MatchDTO> actual = underTest.saveMatches(matches, principal);
            assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
            MatchService.checkPreconditions(actual.get(0), MatchService.matchConditionErrors);
        } catch (NoPermissionException e) {
        }
    }

    @Test
    public void saveMatchesNoPermission() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);
        ArrayList<MatchDTO> matches = new ArrayList<>();
        matches.add(matchDTO);
        matches.add(matchDTO);

        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeam(anyLong())).thenReturn(getMannschaftsMitglieder());
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionAusrichter(any(), anyLong())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(any(), anyLong())).thenReturn(false);

        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(()-> underTest.saveMatches(matches, principal));
    }


    @Test
    public void saveMatches_Null() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);
        ArrayList<MatchDTO> matches = new ArrayList<>();
        matches.add(null);
        matches.add(matchDTO);

        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeam(anyLong())).thenReturn(getMannschaftsMitglieder());

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.saveMatches(matches, principal));

     }


    @Test
    public void saveMatches_WithPasseUpdate() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);

        PasseDTO passe1 = getPasseDTO(PASSE_ID_1, toIntExact(MM_rueckennummer_1));
        PasseDTO passe2 = getPasseDTO(PASSE_ID_2, toIntExact(MM_rueckennummer_2));
        PasseDO passe1DO = PasseDTOMapper.toDO.apply(passe1);
        PasseDO passe2DO = PasseDTOMapper.toDO.apply(passe2);

        // change lfdnr of passe2 to make them distinguishable
        passe2.setLfdNr(PASSE_LFDR_NR + 1);

        List<PasseDTO> passeDTOS = new ArrayList<>();
        passeDTOS.add(passe1);
        passeDTOS.add(passe2);

        matchDTO.setPassen(passeDTOS);

        ArrayList<MatchDTO> matches = new ArrayList<>();
        matches.add(matchDTO);
        matches.add(matchDTO);

        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeam(anyLong())).thenReturn(getMannschaftsMitglieder());
        when(mannschaftsmitgliedComponent.findByMemberAndTeamId(anyLong(), anyLong())).thenReturn(getMannschaftsMitglieder().get(0));
        when(passeComponent.findById(PASSE_ID_1)).thenReturn(passe1DO);
        when(passeComponent.findById(PASSE_ID_2)).thenReturn(passe2DO);

        try {
        final List<MatchDTO> actual = underTest.saveMatches(matches, principal);
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
        MatchService.checkPreconditions(actual.get(0), MatchService.matchConditionErrors);
        MatchService.checkPreconditions(actual.get(1), MatchService.matchConditionErrors);

        // make sure update was called twice per passed DTO
        verify(passeComponent, times(4)).update(any(PasseDO.class), eq(CURRENT_USER_ID));

        } catch (NoPermissionException e) {
        }
    }


    //test Null -< NullPointerException
    @Test
    public void saveMatches_WithPasseUpdate_Null() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);

        PasseDTO passe1 = getPasseDTO(PASSE_ID_1, PASSE_SCHUETZE_NR_1);
        PasseDTO passe2 = getPasseDTO(PASSE_ID_2, PASSE_SCHUETZE_NR_2);
        // change lfdnr of passe2 to make them distinguishable
        passe2.setLfdNr(PASSE_LFDR_NR + 1);

        List<PasseDTO> passeDTOS = new ArrayList<>();
        passeDTOS.add(passe1);
        passeDTOS.add(passe2);

        passeDTOS.set(0, null);

        matchDTO.setPassen(passeDTOS);

        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeam(anyLong())).thenReturn(getMannschaftsMitglieder());
        when(mannschaftsmitgliedComponent.findByMemberAndTeamId(anyLong(), anyLong())).thenReturn(getMannschaftsMitglieder().get(0));

        ArrayList<MatchDTO> matches = new ArrayList<>();
        matches.add(matchDTO);
        matches.add(matchDTO);
        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(() -> underTest.saveMatches(matches, principal));
    }

    @Test
    public void saveMatches_WithPasseCreate() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);

        PasseDTO passe1 = getPasseDTO(null, toIntExact(MM_rueckennummer_1));
        PasseDTO passe2 = getPasseDTO(null, toIntExact(MM_rueckennummer_2));
        // change lfdnr of passe2 to make them distinguishable
        passe2.setLfdNr(PASSE_LFDR_NR + 1);

        List<PasseDTO> passeDTOS = new ArrayList<>();
        passeDTOS.add(passe1);
        passeDTOS.add(passe2);

        matchDTO.setPassen(passeDTOS);

        ArrayList<MatchDTO> matches = new ArrayList<>();
        matches.add(matchDTO);
        matches.add(matchDTO);

        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeam(anyLong())).thenReturn(getMannschaftsMitglieder());
        when(mannschaftsmitgliedComponent.findByMemberAndTeamId(anyLong(),anyLong())).thenReturn(getMMDO(1L, 5L));
        try {
            final List<MatchDTO> actual = underTest.saveMatches(matches, principal);
            assertThat(actual).isNotNull().isNotEmpty().hasSize(2);
            MatchService.checkPreconditions(actual.get(0), MatchService.matchConditionErrors);
            MatchService.checkPreconditions(actual.get(1), MatchService.matchConditionErrors);

            // make sure create was called twice per passed DTO
            verify(passeComponent, times(4)).create(any(PasseDO.class), eq(CURRENT_USER_ID));

        } catch (NoPermissionException e) {
        }
    }


    @Test
    public void create() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);
        DsbMannschaftDO platzhalter = getPlatzhalter();


        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(matchComponent.create(any(MatchDO.class), anyLong())).thenReturn(matchDO1);
        when(mannschaftComponent.findById(anyLong())).thenReturn(platzhalter);
        try {
            final MatchDTO actual = underTest.create(matchDTO, principal);
            assertThat(actual).isNotNull();
            MatchService.checkPreconditions(actual, MatchService.matchConditionErrors);

        } catch (NoPermissionException e) {
        }
    }

    @Test
    public void createNoPermission() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);

        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(any(), anyLong())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionAusrichter(any(), anyLong())).thenReturn(false);
        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(()-> underTest.create(matchDTO, principal));
    }


    @Test
    public void create_Null() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.create(null, principal));
     }


    @Test
    public void update() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);

        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(true);
        when(matchComponent.update(any(MatchDO.class), anyLong())).thenReturn(matchDO1);
        try {
        final MatchDTO actual = underTest.update(matchDTO, principal);
        assertThat(actual).isNotNull();
        MatchService.checkPreconditions(actual, MatchService.matchConditionErrors);
        } catch (NoPermissionException e) {
        }
    }

    @Test
    public void updateNoPermission() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);

        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        when(requiresOnePermissionAspect.hasPermission(any())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(any(), anyLong())).thenReturn(false);
        when(requiresOnePermissionAspect.hasSpecificPermissionAusrichter(any(), anyLong())).thenReturn(false);
        when(wettkampfComponent.findById(anyLong())).thenReturn(getWettkampfDO(W_id));
        assertThatExceptionOfType(NoPermissionException.class)
                .isThrownBy(()-> underTest.update(matchDTO, principal));
    }


    @Test
    public void update_Null() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.update(null, principal));
     }


    @Test
    public void testFindByWettkampfId() {
        MatchDO matchDO1 = getMatchDO();
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO1);

        ArrayList<MatchDO> matchesDO = new ArrayList<>();
        matchesDO.add(matchDO1);
        matchesDO.add(matchDO1);

        DsbMannschaftDO mannschaftDO = getMannschaftDO(M_id);
        VereinDO vereinDO = getVereinDO(VEREIN_ID);

        matchDTO.setMannschaftName(vereinDO.getName() + '-' + mannschaftDO.getNummer());


        when(matchComponent.findByWettkampfId(anyLong())).thenReturn(matchesDO);
        when(mannschaftComponent.findById(anyLong())).thenReturn(mannschaftDO);
        when(vereinComponent.findById(anyLong())).thenReturn(vereinDO);
        final List<MatchDTO> actual = underTest.findByWettkampfId(1L);
        assertThat(actual).isNotNull().isNotEmpty().hasSize(2);

        final MatchDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(matchDTO.getId());
        assertThat(actualDTO.getMannschaftId()).isEqualTo(matchDTO.getMannschaftId());
        assertThat(actualDTO.getMannschaftName()).isEqualTo(matchDTO.getMannschaftName());

    }
    //erst mal den OK Fall testen
    @Test
    public void testGetMemberIdFor() {
        //zum testen brauchen wir eine Pass DTO mit Rückennummer eines existierenden
        // Mannschaftsmitglieds (6 - ist das zweite Elemente der Liste)
        PasseDTO passeDTOok = getPasseDTO(1L, 6);
        assertEquals(MM_dsbMitgliedId, underTest.getMemberIdFor(passeDTOok, getMannschaftsMitglieder()));
    }
}
