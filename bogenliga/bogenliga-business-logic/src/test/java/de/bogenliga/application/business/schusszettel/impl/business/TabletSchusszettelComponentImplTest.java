package de.bogenliga.application.business.schusszettel.impl.business;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzenSatzDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Testet die Funktionalität der TabletSchusszettelComponentImpl.
 * @author Marty Lauterbach
 */
public class TabletSchusszettelComponentImplTest {

    private static final Long WETTKAMPF_ID = 1L;
    private static final Long TEAM1_ID = 10L;
    private static final Long TEAM2_ID = 20L;
    private static final Long MATCH_ID = 100L;
    private static final String VALID_TOKEN = "valid_token";

    @Mock
    private TabletSchusszettelDAO sessionDAO;
    @Mock
    private PasseComponent passeComponent;
    @Mock
    private MatchComponent matchComponent;
    @Mock
    private MannschaftsmitgliedComponent mmComponent;
    @Mock
    private DsbMitgliedComponent mitgliedComponent;
    @Mock
    private DsbMannschaftComponent mannschaftComponent;
    @Mock
    private VereinComponent vereinComponent;

    private TabletSchusszettelComponentImpl underTest;
    private List<PasseDO> inMemoryPasses;

    // Used for tracking sessions in tests that need to access them
    private Map<String, TabletSchusszettelEntity> sessionsMap = new HashMap<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new TabletSchusszettelComponentImpl(
                sessionDAO, passeComponent, matchComponent, mmComponent,
                mitgliedComponent, mannschaftComponent, vereinComponent);

        inMemoryPasses = new ArrayList<>();
        when(passeComponent.findByMatchId(anyLong())).thenAnswer(invocation -> {
            Long matchId = invocation.getArgument(0);
            List<PasseDO> result = new ArrayList<>();
            for (PasseDO p : inMemoryPasses) {
                if (Objects.equals(p.getPasseMatchId(), matchId)) {
                    result.add(p);
                }
            }
            return result;
        });
        when(passeComponent.create(any(PasseDO.class), anyLong())).thenAnswer(invocation -> {
            PasseDO passeDO = invocation.getArgument(0);
            passeDO.setId((long) (inMemoryPasses.size() + 1));
            inMemoryPasses.add(passeDO);
            return passeDO;
        });
        when(passeComponent.update(any(PasseDO.class), anyLong())).thenAnswer(invocation -> {
            PasseDO updatedPasse = invocation.getArgument(0);
            inMemoryPasses.removeIf(p -> Objects.equals(p.getId(), updatedPasse.getId()));
            inMemoryPasses.add(updatedPasse);
            return updatedPasse;
        });
    }

    @Test
    public void testGetStatus_InvalidToken() {
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, "invalid")).thenReturn(Optional.empty());
        TabletSchusszettelDO result = underTest.getStatus(WETTKAMPF_ID, TEAM1_ID, "invalid");
        Assertions.assertThat(result.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED);
    }

    @Test
    public void testGetStatus_NullToken() {
        TabletSchusszettelDO result = underTest.getStatus(WETTKAMPF_ID, TEAM1_ID, null);
        Assertions.assertThat(result.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED);
    }

    @Test
    public void testGetStatus_ValidToken_Schuetzenmeldung() {
        TabletSchusszettelEntity session = createSessionEntity("SCHUETZENMELDUNG");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        setupTeamInfoMocks();
        setupEmptyPasses();
        MannschaftsmitgliedDO mmDO = new MannschaftsmitgliedDO(1L, TEAM1_ID, 50L, 0, "Max", "Mustermann", 1L);
        when(mmComponent.findByTeamId(TEAM1_ID)).thenReturn(Collections.singletonList(mmDO));
        DsbMitgliedDO dsbMitglied = new DsbMitgliedDO();
        dsbMitglied.setId(50L);
        dsbMitglied.setVorname("Max");
        dsbMitglied.setNachname("Mustermann");
        when(mitgliedComponent.findById(50L)).thenReturn(dsbMitglied);
        TabletSchusszettelDO result = underTest.getStatus(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN);
        Assertions.assertThat(result.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.SCHUETZENMELDUNG);
        Assertions.assertThat(result.getVerfuegbareSchuetzen()).hasSize(1);
        Assertions.assertThat(result.getVerfuegbareSchuetzen().get(0).getName()).isEqualTo("Max Mustermann");
    }

    @Test
    public void testGetStatus_ValidToken_Satzeingabe() {
        TabletSchusszettelEntity session = createSessionEntity("SATZEINGABE");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        setupTeamInfoMocks();
        List<PasseDO> passes = new ArrayList<>();
        PasseDO pass = new PasseDO();
        pass.setId(1L);
        pass.setPasseLfdnr(1L);
        pass.setPasseMannschaftId(TEAM1_ID);
        pass.setPasseDsbMitgliedId(101L);
        pass.setPasseMatchId(MATCH_ID);
        passes.add(pass);
        inMemoryPasses.add(pass);
        when(passeComponent.findByMatchId(MATCH_ID)).thenReturn(passes);
        DsbMitgliedDO dsbMitglied = new DsbMitgliedDO();
        dsbMitglied.setId(101L);
        dsbMitglied.setVorname("Max");
        dsbMitglied.setNachname("Mustermann");
        when(mitgliedComponent.findById(101L)).thenReturn(dsbMitglied);
        MannschaftsmitgliedDO mm = new MannschaftsmitgliedDO(1L, TEAM1_ID, 101L, 0, "Max", "Mustermann", 1L);
        when(mmComponent.findByMemberAndTeamId(TEAM1_ID, 101L)).thenReturn(mm);
        when(mmComponent.findByTeamId(TEAM1_ID)).thenReturn(Collections.singletonList(mm));
        TabletSchusszettelDO result = underTest.getStatus(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN);
        Assertions.assertThat(result.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);
        Assertions.assertThat(result.getSchuetzeStammDaten()).hasSize(1);
        Assertions.assertThat(result.getSchuetzeStammDaten().get(0).getVorname()).isEqualTo("Max");
    }

    @Test
    public void testGetStatus_ValidToken_Warte() {
        TabletSchusszettelEntity session = createSessionEntity("WARTE");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        setupTeamInfoMocks();
        setupEmptyPasses();
        TabletSchusszettelDO result = underTest.getStatus(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN);
        Assertions.assertThat(result.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE);
    }

    @Test
    public void testGetStatus_ValidToken_WettkampfEnde() {
        TabletSchusszettelEntity session = createSessionEntity("WETTKAMPF_ENDE");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        setupTeamInfoMocks();
        setupEmptyPasses();
        List<MatchDO> matches = new ArrayList<>();
        MatchDO match = new MatchDO();
        match.setId(MATCH_ID);
        match.setWettkampfId(WETTKAMPF_ID);
        match.setMannschaftId(TEAM1_ID);
        match.setNr(1L);
        match.setBegegnung(1L);
        matches.add(match);
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(matches);
        TabletSchusszettelDO result = underTest.getStatus(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN);
        Assertions.assertThat(result.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE);
    }

    @Test
    public void testInitializeForWettkampf() {
        List<MatchDO> matches = new ArrayList<>();
        MatchDO match1 = new MatchDO();
        match1.setId(1L);
        match1.setNr(1L);
        match1.setWettkampfId(WETTKAMPF_ID);
        match1.setMannschaftId(TEAM1_ID);
        match1.setBegegnung(1L);
        MatchDO match2 = new MatchDO();
        match2.setId(2L);
        match2.setNr(1L);
        match2.setWettkampfId(WETTKAMPF_ID);
        match2.setMannschaftId(TEAM2_ID);
        match2.setBegegnung(1L);
        matches.add(match1);
        matches.add(match2);
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(matches);
        when(matchComponent.findById(anyLong())).thenReturn(match1);
        underTest.initializeForWettkampf(WETTKAMPF_ID);
        verify(sessionDAO).deleteByWettkampfId(WETTKAMPF_ID);
        verify(sessionDAO, times(2)).createSession(any(TabletSchusszettelEntity.class), eq(-1L));
    }

    @Test(expected = BusinessException.class)
    public void testInitializeForWettkampf_NoMatches() {
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Collections.emptyList());
        underTest.initializeForWettkampf(WETTKAMPF_ID);
    }

    @Test
    public void testDeleteForWettkampf() {
        underTest.deleteForWettkampf(WETTKAMPF_ID);
        verify(sessionDAO).deleteByWettkampfId(WETTKAMPF_ID);
    }

    @Test
    public void testExistsForWettkampf() {
        when(sessionDAO.existsByWettkampfId(WETTKAMPF_ID)).thenReturn(true);
        boolean result = underTest.existsForWettkampf(WETTKAMPF_ID);
        Assertions.assertThat(result).isTrue();
        verify(sessionDAO).existsByWettkampfId(WETTKAMPF_ID);
    }

    @Test
    public void testReTokenize() {
        TabletSchusszettelEntity session = new TabletSchusszettelEntity();
        session.setWettkampfId(WETTKAMPF_ID);
        session.setTeamId(TEAM1_ID);
        session.setToken(VALID_TOKEN);
        when(sessionDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID)).thenReturn(Optional.of(session));
        underTest.reTokenize(WETTKAMPF_ID, TEAM1_ID);
        verify(sessionDAO).setToken(eq(WETTKAMPF_ID), eq(TEAM1_ID), anyString(), eq(-1L));
    }

    @Test(expected = BusinessException.class)
    public void testReTokenize_InvalidSession() {
        when(sessionDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID)).thenReturn(Optional.empty());
        underTest.reTokenize(WETTKAMPF_ID, TEAM1_ID);
    }

    @Test
    public void testSubmitSchuetzen() {
        TabletSchusszettelEntity session = createSessionEntity("SCHUETZENMELDUNG");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        List<Long> schuetzenIds = Arrays.asList(101L, 102L, 103L);
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(schuetzenIds);
        for (Long id : schuetzenIds) {
            MannschaftsmitgliedDO mm = new MannschaftsmitgliedDO(id * 10, TEAM1_ID, id, 0, "Vor", "Nach", 1L);
            when(mmComponent.findByMemberAndTeamId(TEAM1_ID, id)).thenReturn(mm);
        }
        underTest.submitSchuetzen(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, meldung);
        verify(sessionDAO).updateStatus(any(TabletSchusszettelEntity.class), eq(-1L));
        verify(passeComponent, times(15)).create(any(PasseDO.class), eq(-1L));
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSchuetzen_InvalidToken() {
        underTest.submitSchuetzen(WETTKAMPF_ID, TEAM1_ID, "", new SchuetzenMeldungDO());
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSchuetzen_InvalidSession() {
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.empty());
        underTest.submitSchuetzen(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, new SchuetzenMeldungDO());
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSchuetzen_NotEnoughShooters() {
        TabletSchusszettelEntity session = createSessionEntity("SCHUETZENMELDUNG");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Collections.singletonList(101L));
        underTest.submitSchuetzen(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, meldung);
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSchuetzen_InvalidMember() {
        TabletSchusszettelEntity session = createSessionEntity("SCHUETZENMELDUNG");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Arrays.asList(101L, 102L, 103L));
        when(mmComponent.findByMemberAndTeamId(TEAM1_ID, 101L)).thenReturn(null);
        when(mmComponent.findByMemberAndTeamId(TEAM1_ID, 102L)).thenReturn(new MannschaftsmitgliedDO(1020L, TEAM1_ID, 102L, 0, "Vor", "Nach", 2L));
        when(mmComponent.findByMemberAndTeamId(TEAM1_ID, 103L)).thenReturn(new MannschaftsmitgliedDO(1030L, TEAM1_ID, 103L, 0, "Vor", "Nach", 3L));
        underTest.submitSchuetzen(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, meldung);
    }

    @Test
    public void testSubmitSatz() {
        TabletSchusszettelEntity session = createSessionEntity("SATZEINGABE");
        session.setCurrentPasseNumber(1);
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        List<SchuetzenSatzDO> satzeingabe = Arrays.asList(
                new SchuetzenSatzDO(101L, 10, 9, 9),
                new SchuetzenSatzDO(102L, 10, 10, 8),
                new SchuetzenSatzDO(103L, 9, 9, 9)
        );
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(satzeingabe);
        for (SchuetzenSatzDO s : satzeingabe) {
            PasseDO p = new PasseDO();
            p.setId((long) (inMemoryPasses.size() + 1));
            p.setPasseMannschaftId(TEAM1_ID);
            p.setPasseLfdnr(1L);
            p.setPasseDsbMitgliedId(s.getSchuetzenId());
            p.setPasseMatchId(MATCH_ID);
            inMemoryPasses.add(p);
        }
        underTest.submitSatz(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, eingabe);
        verify(passeComponent, atLeast(3)).update(any(PasseDO.class), eq(-1L));
        verify(sessionDAO).updateStatus(any(TabletSchusszettelEntity.class), eq(-1L));
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSatz_InvalidToken() {
        underTest.submitSatz(WETTKAMPF_ID, TEAM1_ID, "", new SatzEingabeDO());
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSatz_InvalidSession() {
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.empty());
        underTest.submitSatz(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, new SatzEingabeDO());
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSatz_InvalidPayload() {
        TabletSchusszettelEntity session = createSessionEntity("SATZEINGABE");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(Collections.singletonList(new SchuetzenSatzDO(101L, 10, 9, 9)));
        underTest.submitSatz(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, eingabe);
    }

    @Test
    public void testFullWorkflow_Complete_TIE() {
        final long uniqueWettkampfId = 999L;
        final long team1Id        = 1001L;
        final long team2Id        = 1002L;
        final Long match1Id       = 5001L;
        final Long match2Id       = 5002L;
        inMemoryPasses.clear();
        setupFullCompetitionMocks(uniqueWettkampfId, team1Id, team2Id, match1Id, match2Id);

        // keep sessionsMap up to date
        doAnswer(inv -> {
            TabletSchusszettelEntity e = inv.getArgument(0);
            sessionsMap.keySet().removeIf(k -> k.startsWith(e.getTeamId() + "_"));
            sessionsMap.put(e.getTeamId() + "_" + e.getStatus(), e);
            return null;
        }).when(sessionDAO).updateStatus(any(), anyLong());

        // initialize and register shooters
        underTest.initializeForWettkampf(uniqueWettkampfId);

        List<Long> team1Shooters = List.of(101L, 102L, 103L);
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(team1Shooters);
        underTest.submitSchuetzen(uniqueWettkampfId, team1Id, VALID_TOKEN,
                meldung);

        List<Long> team2Shooters = List.of(201L, 202L, 203L);
        meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(team2Shooters);
        underTest.submitSchuetzen(uniqueWettkampfId, team2Id, VALID_TOKEN,
                meldung);

        // Prepare a single "tie" SatzEingabe (same for both teams)
        List<SchuetzenSatzDO> tieShots = new ArrayList<>();
        for (Long id : team1Shooters) {
            tieShots.add(new SchuetzenSatzDO(id, 10, 9, 8));
        }
        SatzEingabeDO tie = new SatzEingabeDO();
        tie.setSatzeingabe(tieShots);

        // shoot 5 completely tied sets
        for (int round = 1; round <= 5; round++) {
            underTest.submitSatz(uniqueWettkampfId, team1Id, VALID_TOKEN, tie);
            underTest.submitSatz(uniqueWettkampfId, team2Id, VALID_TOKEN, tie);

            // after the 5th tie, both must advance to WETTKAMPF_ENDE
            if (round == 5) {
                TabletSchusszettelDO s1 = underTest.getStatus(uniqueWettkampfId, team1Id, VALID_TOKEN);
                TabletSchusszettelDO s2 = underTest.getStatus(uniqueWettkampfId, team2Id, VALID_TOKEN);
                Assertions.assertThat(s1.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE);
                Assertions.assertThat(s2.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE);
            }
        }
    }

    @Test
    public void testFullWorkflow_Complete_ONEWINNER() {
        final long uniqueWettkampfId = 999L;
        final long team1Id = 1001L;
        final long team2Id = 1002L;
        final Long match1Id = 5001L;
        final Long match2Id = 5002L;
        inMemoryPasses.clear();
        setupFullCompetitionMocks(uniqueWettkampfId, team1Id, team2Id, match1Id, match2Id);

        doAnswer(inv -> {
            TabletSchusszettelEntity e = inv.getArgument(0);
            // remove any old entry for this team
            sessionsMap.keySet().removeIf(k -> k.startsWith(e.getTeamId() + "_"));
            // now store the fresh one
            sessionsMap.put(e.getTeamId() + "_" + e.getStatus(), e);
            return null;
        }).when(sessionDAO).updateStatus(any(TabletSchusszettelEntity.class), anyLong());

        underTest.initializeForWettkampf(uniqueWettkampfId);
        verify(sessionDAO).deleteByWettkampfId(uniqueWettkampfId);
        verify(sessionDAO, times(2)).createSession(any(TabletSchusszettelEntity.class), eq(-1L));
        TabletSchusszettelDO team1Status = underTest.getStatus(uniqueWettkampfId, team1Id, VALID_TOKEN);
        Assertions.assertThat(team1Status.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.SCHUETZENMELDUNG);
        TabletSchusszettelDO team2Status = underTest.getStatus(uniqueWettkampfId, team2Id, VALID_TOKEN);
        Assertions.assertThat(team2Status.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.SCHUETZENMELDUNG);
        List<Long> team1Shooters = Arrays.asList(101L, 102L, 103L);
        SchuetzenMeldungDO team1Meldung = new SchuetzenMeldungDO();
        team1Meldung.setGemeldeteSchuetzen(team1Shooters);
        underTest.submitSchuetzen(uniqueWettkampfId, team1Id, VALID_TOKEN, team1Meldung);
        List<Long> team2Shooters = Arrays.asList(201L, 202L, 203L);
        SchuetzenMeldungDO team2Meldung = new SchuetzenMeldungDO();
        team2Meldung.setGemeldeteSchuetzen(team2Shooters);
        underTest.submitSchuetzen(uniqueWettkampfId, team2Id, VALID_TOKEN, team2Meldung);
        team1Status = underTest.getStatus(uniqueWettkampfId, team1Id, VALID_TOKEN);
        Assertions.assertThat(team1Status.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);
        team2Status = underTest.getStatus(uniqueWettkampfId, team2Id, VALID_TOKEN);
        Assertions.assertThat(team2Status.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);
        for (int round = 1; round <= 6; round++) {
            SatzEingabeDO team1Scores = createSatzEingabe(team1Shooters);
            team1Status = underTest.getStatus(uniqueWettkampfId, team1Id, VALID_TOKEN);
            System.out.println("Status Team1 before Eingabe: " + team1Status.getStatus());
            underTest.submitSatz(uniqueWettkampfId, team1Id, VALID_TOKEN, team1Scores);
            System.out.println("Satz Nummer: " + round);
            team1Status = underTest.getStatus(uniqueWettkampfId, team1Id, VALID_TOKEN);
            System.out.println("Status Team1 after Eingabe: " + team1Status.getStatus());
            Assertions.assertThat(team1Status.getStatus()).isIn(
                    TabletSchusszettelDO.TabletSchusszettelStatus.WARTE,
                    TabletSchusszettelDO.TabletSchusszettelStatus.SCHUETZENMELDUNG,
                    TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE
            );
            SatzEingabeDO team2Scores = createSatzEingabe(team2Shooters);
            team2Status = underTest.getStatus(uniqueWettkampfId, team2Id, VALID_TOKEN);
            System.out.println("Status Team2 before Eingabe: " + team2Status.getStatus());
            underTest.submitSatz(uniqueWettkampfId, team2Id, VALID_TOKEN, team2Scores);
            team2Status = underTest.getStatus(uniqueWettkampfId, team2Id, VALID_TOKEN);
            System.out.println("Status Team2 after Eingabe: " + team2Status.getStatus());
            if (round == 3) {
                team1Status = underTest.getStatus(uniqueWettkampfId, team1Id, VALID_TOKEN);
                System.out.println("Status Team1 Round 5: " + team1Status.getStatus());
                team2Status = underTest.getStatus(uniqueWettkampfId, team2Id, VALID_TOKEN);
                System.out.println("Status Team2 Round 5: " + team2Status.getStatus());
                Assertions.assertThat(team1Status.getStatus())
                        .isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE);
                Assertions.assertThat(team2Status.getStatus())
                        .isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE);
                break;
            }
            team1Status = underTest.getStatus(uniqueWettkampfId, team1Id, VALID_TOKEN);
            Assertions.assertThat(team1Status.getStatus()).isIn(
                    TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE,
                    TabletSchusszettelDO.TabletSchusszettelStatus.WARTE
            );
            team2Status = underTest.getStatus(uniqueWettkampfId, team2Id, VALID_TOKEN);
            Assertions.assertThat(team2Status.getStatus()).isIn(
                    TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE,
                    TabletSchusszettelDO.TabletSchusszettelStatus.WARTE
            );
        }
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSchuetzen_NullInput() {
        TabletSchusszettelEntity session = createSessionEntity("SCHUETZENMELDUNG");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        underTest.submitSchuetzen(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, null);
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSchuetzen_EmptyShooterList() {
        TabletSchusszettelEntity session = createSessionEntity("SCHUETZENMELDUNG");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Collections.emptyList());
        underTest.submitSchuetzen(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, meldung);
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSchuetzen_TooManyShooters() {
        TabletSchusszettelEntity session = createSessionEntity("SCHUETZENMELDUNG");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        SchuetzenMeldungDO meldung = new SchuetzenMeldungDO();
        meldung.setGemeldeteSchuetzen(Arrays.asList(1L, 2L, 3L, 4L));
        underTest.submitSchuetzen(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, meldung);
    }

    @Test(expected = TechnicalException.class)
    public void testSubmitSatz_TechnicalExceptionOnUpdate() {
        TabletSchusszettelEntity session = createSessionEntity("SATZEINGABE");
        session.setCurrentPasseNumber(1);
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        List<SchuetzenSatzDO> satzeingabe = Arrays.asList(
                new SchuetzenSatzDO(101L, 10, 9, 9),
                new SchuetzenSatzDO(102L, 10, 10, 8),
                new SchuetzenSatzDO(103L, 9, 9, 9)
        );
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(satzeingabe);
        PasseDO p = new PasseDO();
        p.setId(1L);
        p.setPasseMannschaftId(TEAM1_ID);
        p.setPasseLfdnr(1L);
        p.setPasseDsbMitgliedId(101L);
        p.setPasseMatchId(MATCH_ID);
        inMemoryPasses.add(p);
        when(passeComponent.update(any(PasseDO.class), anyLong())).thenThrow(new RuntimeException("DB error"));
        underTest.submitSatz(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, eingabe);
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSatz_NullInput() {
        TabletSchusszettelEntity session = createSessionEntity("SATZEINGABE");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        underTest.submitSatz(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, null);
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSatz_EmptySatzeingabe() {
        TabletSchusszettelEntity session = createSessionEntity("SATZEINGABE");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(Collections.emptyList());
        underTest.submitSatz(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, eingabe);
    }

    @Test(expected = BusinessException.class)
    public void testSubmitSatz_TooManySatzeingabe() {
        TabletSchusszettelEntity session = createSessionEntity("SATZEINGABE");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        List<SchuetzenSatzDO> satzeingabe = Arrays.asList(
                new SchuetzenSatzDO(101L, 10, 9, 9),
                new SchuetzenSatzDO(102L, 10, 10, 8),
                new SchuetzenSatzDO(103L, 9, 9, 9),
                new SchuetzenSatzDO(104L, 8, 8, 8)
        );
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(satzeingabe);
        underTest.submitSatz(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN, eingabe);
    }

    @Test(expected = BusinessException.class)
    public void testGetStatus_UnknownSessionStatus() {
        TabletSchusszettelEntity session = createSessionEntity("UNKNOWN_STATUS");
        when(sessionDAO.findByTokenWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN)).thenReturn(Optional.of(session));
        // Should not throw, but should not crash or set any fields
        TabletSchusszettelDO result = underTest.getStatus(WETTKAMPF_ID, TEAM1_ID, VALID_TOKEN);
        Assertions.assertThat(result.getStatus()).isEqualTo(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf("UNKNOWN_STATUS"));
    }

    @Test(expected = BusinessException.class)
    public void testAdvanceToNextMatchOrEnd_NoOpponentFound() {
        // Simulate a match with no opponent
        TabletSchusszettelEntity session = createSessionEntity("SATZEINGABE");
        session.setCurrentMatchId(9999L);
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Collections.singletonList(new MatchDO()));
        when(matchComponent.findById(9999L)).thenReturn(new MatchDO());
        underTest.initializeForWettkampf(WETTKAMPF_ID);
    }

    @Test(expected = TechnicalException.class)
    public void testInitializeForWettkampf_TechnicalException() {
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenThrow(new RuntimeException("DB error"));
        underTest.initializeForWettkampf(WETTKAMPF_ID);
    }

    @Test(expected = TechnicalException.class)
    public void testDeleteForWettkampf_TechnicalException() {
        doThrow(new RuntimeException("DB error")).when(sessionDAO).deleteByWettkampfId(WETTKAMPF_ID);
        underTest.deleteForWettkampf(WETTKAMPF_ID);
    }

    @Test(expected = TechnicalException.class)
    public void testExistsForWettkampf_TechnicalException() {
        when(sessionDAO.existsByWettkampfId(WETTKAMPF_ID)).thenThrow(new RuntimeException("DB error"));
        underTest.existsForWettkampf(WETTKAMPF_ID);
    }

    @Test(expected = TechnicalException.class)
    public void testReTokenize_TechnicalException() {
        when(sessionDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID)).thenThrow(new RuntimeException("DB error"));
        underTest.reTokenize(WETTKAMPF_ID, TEAM1_ID);
    }

    @Test(expected = BusinessException.class)
    public void testFindOpponentTeamId_NoOpponent() throws Exception {
        // arrange
        MatchDO match = new MatchDO();
        match.setId(1L);
        match.setWettkampfId(WETTKAMPF_ID);
        match.setNr(1L);
        match.setBegegnung(1L);
        match.setMannschaftId(TEAM1_ID);

        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(match));

        // reflectively call the private method, but unwrap the InvocationTargetException
        Method findOpponent = TabletSchusszettelComponentImpl.class
                .getDeclaredMethod("findOpponentTeamId", MatchDO.class, long.class);
        findOpponent.setAccessible(true);

        try {
            findOpponent.invoke(underTest, match, TEAM1_ID);
            fail("Expected a BusinessException to be thrown");
        } catch (InvocationTargetException ite) {
            // unwrap and re-throw the cause so JUnit sees the BusinessException directly
            Throwable cause = ite.getCause();
            if (cause instanceof BusinessException) {
                throw (BusinessException) cause;
            }
            // something else went wrong
            throw ite;
        }
    }

    @Test
    public void testDeleteForWettkampf_Success() {
        // Should call DAO delete
        underTest.deleteForWettkampf(WETTKAMPF_ID);
        verify(sessionDAO, times(1)).deleteByWettkampfId(WETTKAMPF_ID);
    }

    @Test
    public void testExistsForWettkampf_True() {
        when(sessionDAO.existsByWettkampfId(WETTKAMPF_ID)).thenReturn(true);
        Assertions.assertThat(underTest.existsForWettkampf(WETTKAMPF_ID)).isTrue();
        verify(sessionDAO, times(1)).existsByWettkampfId(WETTKAMPF_ID);
    }

    @Test
    public void testExistsForWettkampf_False() {
        when(sessionDAO.existsByWettkampfId(WETTKAMPF_ID)).thenReturn(false);
        Assertions.assertThat(underTest.existsForWettkampf(WETTKAMPF_ID)).isFalse();
        verify(sessionDAO, times(1)).existsByWettkampfId(WETTKAMPF_ID);
    }

    @Test
    public void testGenerateSchusszettelSessions_Empty() {
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Collections.emptyList());
        Assertions.assertThat(underTest.generateSchusszettelSessions(WETTKAMPF_ID)).isNull();
    }

    @Test
    public void testGenerateSchusszettelSessions_NonEmpty() {
        // arrange
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        entity.setWettkampfId(WETTKAMPF_ID);
        entity.setTeamId(TEAM1_ID);
        entity.setStatus("SATZEINGABE");
        entity.setToken("tok123");
        entity.setCurrentPasseNumber(2);
        entity.setGegnerTeamId(TEAM2_ID);

        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(entity));

        // act & assert: simply calls the method — if it throws, the test will fail
        try {
            underTest.generateSchusszettelSessions(WETTKAMPF_ID);
        } catch (Exception e) {
            fail("generateSchusszettelSessions should not have thrown, but did: " + e.getMessage());
        }
    }

    @Test
    public void testReTokenize_Success() {
        TabletSchusszettelEntity session = new TabletSchusszettelEntity();
        session.setWettkampfId(WETTKAMPF_ID);
        session.setTeamId(TEAM1_ID);
        session.setToken("oldtoken");
        when(sessionDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID)).thenReturn(Optional.of(session));
        underTest.reTokenize(WETTKAMPF_ID, TEAM1_ID);
        verify(sessionDAO, times(1)).setToken(eq(WETTKAMPF_ID), eq(TEAM1_ID), anyString(), eq(-1L));
    }

    @Test(expected = BusinessException.class)
    public void testReTokenize_NoSession() {
        when(sessionDAO.findByWettkampfUndTeam(WETTKAMPF_ID, TEAM1_ID)).thenReturn(Optional.empty());
        underTest.reTokenize(WETTKAMPF_ID, TEAM1_ID);
    }

    /**
     * Helper method to create test data for satz eingabe
     */
    private SatzEingabeDO createSatzEingabe(List<Long> shooterIds) {
        List<SchuetzenSatzDO> satzDOs = new ArrayList<>();
        for (Long id : shooterIds) {
            // Create random scores between 7-10 for each arrow
            int score1 = 7 + new Random().nextInt(4);
            int score2 = 7 + new Random().nextInt(4);
            int score3 = 7 + new Random().nextInt(4);
            satzDOs.add(new SchuetzenSatzDO(id, score1, score2, score3));
        }
        SatzEingabeDO eingabe = new SatzEingabeDO();
        eingabe.setSatzeingabe(satzDOs);
        return eingabe;
    }

    /**
     * Helper method to determine if there's a next match for a team
     */
    private boolean hasNextMatch(long wettkampfId, long teamId) {
        List<MatchDO> matches = matchComponent.findByWettkampfId(wettkampfId).stream()
                .filter(m -> m.getMannschaftId() == teamId)
                .sorted(Comparator.comparing(MatchDO::getNr))
                .toList();

        Long currentMatchId = sessionsMap.values().stream()
                .filter(s -> s.getTeamId() == teamId)
                .map(TabletSchusszettelEntity::getCurrentMatchId)
                .findFirst()
                .orElse(null);

        if (currentMatchId == null) {
            return false;
        }
        int idx = IntStream.range(0, matches.size())
                .filter(i -> Objects.equals(matches.get(i).getId(), currentMatchId))
                .findFirst().orElse(-1);

        return idx >= 0 && idx + 1 < matches.size();
    }

    // Helper to create a session entity for tests
    private TabletSchusszettelEntity createSessionEntity(String status) {
        TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
        entity.setId(1L);
        entity.setWettkampfId(WETTKAMPF_ID);
        entity.setTeamId(TEAM1_ID);
        entity.setCurrentMatchId(MATCH_ID);
        entity.setCurrentMatchNumber(1);
        entity.setCurrentPasseNumber(1);
        entity.setToken(VALID_TOKEN);
        entity.setGegnerTeamId(TEAM2_ID);
        entity.setStatus(status);
        // Add to map for access in tests that need it
        sessionsMap.put(entity.getTeamId() + "_" + entity.getStatus(), entity);
        return entity;
    }

    /**
     * Setup team information mocking
     */
    private void setupTeamInfoMocks() {
        // Setup team 1
        DsbMannschaftDO team1 = new DsbMannschaftDO();
        team1.setId(TEAM1_ID);
        team1.setVereinId(1L);
        team1.setNummer(1L);
        when(mannschaftComponent.findById(TEAM1_ID)).thenReturn(team1);

        // Setup team 2
        DsbMannschaftDO team2 = new DsbMannschaftDO();
        team2.setId(TEAM2_ID);
        team2.setVereinId(2L);
        team2.setNummer(2L);
        when(mannschaftComponent.findById(TEAM2_ID)).thenReturn(team2);

        // Setup vereine
        VereinDO verein1 = new VereinDO();
        verein1.setId(1L);
        verein1.setName("Team 1 Verein");
        VereinDO verein2 = new VereinDO();
        verein2.setId(2L);
        verein2.setName("Team 2 Verein");
        when(vereinComponent.findById(1L)).thenReturn(verein1);
        when(vereinComponent.findById(2L)).thenReturn(verein2);
    }

    /**
     * Setup for empty passes
     */
    private void setupEmptyPasses() {
        when(passeComponent.findByMatchId(MATCH_ID)).thenReturn(Collections.emptyList());
    }

    /**
     * Setup full competition mock data for tests
     */
    private void setupFullCompetitionMocks(final Long wettkampfId,
                                           final Long team1Id,
                                           final Long team2Id,
                                           final Long match1Id,
                                           final Long match2Id) {

        // 1) Stub out both matches for this competition
        MatchDO match1 = new MatchDO();
        match1.setId(match1Id);
        match1.setNr(1L);
        match1.setWettkampfId(wettkampfId);
        match1.setMannschaftId(team1Id);
        match1.setBegegnung(1L);

        MatchDO match2 = new MatchDO();
        match2.setId(match2Id);
        match2.setNr(1L);
        match2.setWettkampfId(wettkampfId);
        match2.setMannschaftId(team2Id);
        match2.setBegegnung(1L);

        when(matchComponent.findByWettkampfId(wettkampfId))
                .thenReturn(Arrays.asList(match1, match2));
        when(matchComponent.findById(match1Id)).thenReturn(match1);
        when(matchComponent.findById(match2Id)).thenReturn(match2);

        // 2) Stub out teams and their club names
        DsbMannschaftDO t1 = new DsbMannschaftDO();
        t1.setId(team1Id);
        t1.setVereinId(101L);
        t1.setNummer(1L);
        when(mannschaftComponent.findById(team1Id)).thenReturn(t1);

        DsbMannschaftDO t2 = new DsbMannschaftDO();
        t2.setId(team2Id);
        t2.setVereinId(102L);
        t2.setNummer(1L);
        when(mannschaftComponent.findById(team2Id)).thenReturn(t2);

        VereinDO v1 = new VereinDO();
        v1.setId(101L);
        v1.setName("Team 1 Verein");
        when(vereinComponent.findById(101L)).thenReturn(v1);

        VereinDO v2 = new VereinDO();
        v2.setId(102L);
        v2.setName("Team 2 Verein");
        when(vereinComponent.findById(102L)).thenReturn(v2);

        // 3) Stub out exactly three shooters per team (membership + personal data)
        List<Long> team1Shooters = Arrays.asList(101L, 102L, 103L);
        List<MannschaftsmitgliedDO> mm1 = new ArrayList<>();
        for (Long sid : team1Shooters) {
            MannschaftsmitgliedDO mm = new MannschaftsmitgliedDO(
                    sid * 10, team1Id, sid, 0,
                    "Vor" + sid, "Nach" + sid, 1L);
            mm1.add(mm);
            when(mmComponent.findByMemberAndTeamId(team1Id, sid)).thenReturn(mm);

            DsbMitgliedDO dsb = new DsbMitgliedDO();
            dsb.setId(sid);
            dsb.setVorname("Vor" + sid);
            dsb.setNachname("Nach" + sid);
            when(mitgliedComponent.findById(sid)).thenReturn(dsb);
        }
        when(mmComponent.findByTeamId(team1Id)).thenReturn(mm1);

        List<Long> team2Shooters = Arrays.asList(201L, 202L, 203L);
        List<MannschaftsmitgliedDO> mm2 = new ArrayList<>();
        for (Long sid : team2Shooters) {
            MannschaftsmitgliedDO mm = new MannschaftsmitgliedDO(
                    sid * 10, team2Id, sid, 0,
                    "Vor" + sid, "Nach" + sid, 1L);
            mm2.add(mm);
            when(mmComponent.findByMemberAndTeamId(team2Id, sid)).thenReturn(mm);

            DsbMitgliedDO dsb = new DsbMitgliedDO();
            dsb.setId(sid);
            dsb.setVorname("Vor" + sid);
            dsb.setNachname("Nach" + sid);
            when(mitgliedComponent.findById(sid)).thenReturn(dsb);
        }
        when(mmComponent.findByTeamId(team2Id)).thenReturn(mm2);

        // 5) Capture createSession(...) into sessionsMap so we can look up "current" sessions by team
        doAnswer(invocation -> {
            TabletSchusszettelEntity e = invocation.getArgument(0);
            sessionsMap.put(e.getTeamId() + "_" + e.getStatus(), e);
            return null;
        }).when(sessionDAO).createSession(any(TabletSchusszettelEntity.class), anyLong());

        // also capture updateStatus(...) *before* any test‐level stubbing overrides it
        doAnswer(inv -> {
            TabletSchusszettelEntity e = inv.getArgument(0);
            // remove any old entry for this team
            sessionsMap.keySet().removeIf(k -> k.startsWith(e.getTeamId() + "_"));
            // now store the fresh one
            sessionsMap.put(e.getTeamId() + "_" + e.getStatus(), e);
            return null;
        }).when(sessionDAO).updateStatus(any(TabletSchusszettelEntity.class), anyLong());

        // 6) Dynamic stub for token lookup: always pull the latest session instance for that team
        when(sessionDAO.findByTokenWettkampfUndTeam(
                eq(wettkampfId),
                anyLong(),
                eq(VALID_TOKEN)))
                .thenAnswer(invocation -> {
                    Long tid = invocation.getArgument(1);
                    // return whichever session object we've most recently put into sessionsMap for this team
                    return sessionsMap.values().stream()
                            .filter(ent -> Objects.equals(ent.getTeamId(), tid))
                            .findFirst();
                });

        when(sessionDAO.findByWettkampfUndTeam(
                eq(wettkampfId),
                anyLong()))
                .thenAnswer(invocation -> {
                    Long tid = invocation.getArgument(1);
                    // return whichever session object we've most recently put into sessionsMap for this team
                    return sessionsMap.values().stream()
                            .filter(ent -> Objects.equals(ent.getTeamId(), tid))
                            .findFirst();
                });
    }
}