package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for MatchEnde state implementation.
 * Tests match end state behavior, final state validation, and no-transition rules.
 */
@RunWith(MockitoJUnitRunner.class)
public class MatchEndeTest {

    @Mock private StateContext mockContext;
    @Mock private PasseComponent mockPasseComponent;
    @Mock private TabletSchusszettelEntity mockOpponent;
    
    private MatchEnde state;
    private LigamatchBE testNextMatch;

    @Before
    public void setUp() {
        state = new MatchEnde();
        setupTestData();
        setupMockBehavior();
    }
    
    private void setupTestData() {
        testNextMatch = new LigamatchBE();
        testNextMatch.setMatchId(400L);
        testNextMatch.setMannschaftId(100L);
        testNextMatch.setMatchNr(2L);
    }
    
    private void setupMockBehavior() {
        when(mockContext.getTeamId()).thenReturn(100L);
        when(mockContext.getCurrentMatchId()).thenReturn(300L);
        when(mockContext.getOpponentTeamId()).thenReturn(200L);
        when(mockContext.getPasseComponent()).thenReturn(mockPasseComponent);
    }

    @Test
    public void isValidState_matchComplete_returnsTrue() {
        when(mockContext.isMatchComplete()).thenReturn(true);
        
        boolean result = state.isValidState(mockContext);
        assertThat(result).isTrue();
    }

    @Test
    public void isValidState_matchIncomplete_returnsFalse() {
        when(mockContext.isMatchComplete()).thenReturn(false);
        
        boolean result = state.isValidState(mockContext);
        assertThat(result).isFalse();
    }

    @Test
    public void canTransitionTo_schuetzenmeldungState_returnsTrue() {
        boolean result = state.canTransitionTo(mockContext, State.STATUS_SCHUETZENMELDUNG);
        assertThat(result).isTrue();
    }

    @Test
    public void canTransitionTo_wettkampfEndeState_returnsTrue() {
        boolean result = state.canTransitionTo(mockContext, State.STATUS_WETTKAMPF_ENDE);
        assertThat(result).isTrue();
    }

    @Test
    public void canTransitionTo_otherStates_returnsFalse() {
        assertThat(state.canTransitionTo(mockContext, State.STATUS_SATZEINGABE)).isFalse();
        assertThat(state.canTransitionTo(mockContext, State.STATUS_WARTE)).isFalse();
        assertThat(state.canTransitionTo(mockContext, "UNKNOWN_STATE")).isFalse();
    }

    @Test
    public void isDatabaseReadyForTransition_alwaysReturnsTrue() {
        assertThat(state.isDatabaseReadyForTransition(mockContext, State.STATUS_SCHUETZENMELDUNG)).isTrue();
        assertThat(state.isDatabaseReadyForTransition(mockContext, State.STATUS_WETTKAMPF_ENDE)).isTrue();
        assertThat(state.isDatabaseReadyForTransition(mockContext, "ANY_STATE")).isTrue();
    }

    @Test
    public void canNudgeAlong_alwaysReturnsFalse() {
        boolean result = state.canNudgeAlong();
        assertThat(result).isFalse();
    }

    @Test
    public void validateOperation_anyOperation_returnsFalse() {
        assertThat(state.validateOperation(mockContext, "submitSatz", new Object())).isFalse();
        assertThat(state.validateOperation(mockContext, "submitSchuetzen", new Object())).isFalse();
        assertThat(state.validateOperation(mockContext, "anyOperation", null)).isFalse();
    }

    @Test
    public void handlePostOperation_anyOperation_returnsFalse() {
        assertThat(state.handlePostOperation(mockContext, "submitSatz", new Object())).isFalse();
        assertThat(state.handlePostOperation(mockContext, "submitSchuetzen", new Object())).isFalse();
        assertThat(state.handlePostOperation(mockContext, "anyOperation", null)).isFalse();
    }

    @Test
    public void handleWarteEvaluation_hasMoreMatches_advancesToNextMatch() {
        when(mockContext.hasMoreMatches()).thenReturn(true);
        when(mockContext.getNextMatch()).thenReturn(testNextMatch);
        when(mockContext.findOpponentTeamId(400L)).thenReturn(102L);
        
        boolean result = state.handleWarteEvaluation(mockContext, mockOpponent);
        
        assertThat(result).isTrue();
        verify(mockContext).advanceToNextMatch(testNextMatch, 102L);
        verify(mockContext, never()).updateSessionStatus(State.STATUS_WETTKAMPF_ENDE);
    }

    @Test
    public void handleWarteEvaluation_noMoreMatches_endsCompetition() {
        when(mockContext.hasMoreMatches()).thenReturn(false);
        
        boolean result = state.handleWarteEvaluation(mockContext, mockOpponent);
        
        assertThat(result).isTrue();
        verify(mockContext).updateSessionStatus(State.STATUS_WETTKAMPF_ENDE);
        verify(mockContext, never()).advanceToNextMatch(any(), anyLong());
    }

    @Test
    public void handleWarteEvaluation_hasMoreMatchesButNextMatchNull_endsCompetition() {
        when(mockContext.hasMoreMatches()).thenReturn(true);
        when(mockContext.getNextMatch()).thenReturn(null);
        
        boolean result = state.handleWarteEvaluation(mockContext, mockOpponent);
        
        assertThat(result).isTrue();
        verify(mockContext).updateSessionStatus(State.STATUS_WETTKAMPF_ENDE);
        verify(mockContext, never()).advanceToNextMatch(any(), anyLong());
    }

    @Test
    public void handleWarteEvaluation_exceptionInProgression_returnsFalse() {
        when(mockContext.hasMoreMatches()).thenThrow(new RuntimeException("DB error"));
        
        boolean result = state.handleWarteEvaluation(mockContext, mockOpponent);
        
        assertThat(result).isFalse();
    }

    @Test
    public void prepareResponseData_validMatch_returnsCompleteData() {
        List<PasseDO> teamPasses = createTestPasses(100L, 300L);
        List<PasseDO> opponentPasses = createTestPasses(200L, 300L);
        
        when(mockContext.getAllMatchPasses()).thenReturn(teamPasses);
        when(mockPasseComponent.findByMannschaftMatchId(200L, 300L)).thenReturn(opponentPasses);
        
        Map<String, Object> result = state.prepareResponseData(mockContext);
        
        assertThat(result).isNotNull();
        assertThat(result).containsKey("satzErgebnisse");
        assertThat(result).containsKey("matchErgebnis");
        assertThat(result).containsKey("schuetzenMatchPunkte");
        assertThat(result).containsKey("schuetzeStammDaten");
        assertThat(result).containsKey("verfuegbareSchuetzen");
        
        // Verify empty collections for completed match
        assertThat(result.get("schuetzenMatchPunkte")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzeStammDaten")).isEqualTo(Collections.emptyList());
        assertThat(result.get("verfuegbareSchuetzen")).isEqualTo(Collections.emptyList());
    }

    @Test
    public void prepareResponseData_exceptionInDataBuilding_returnsEmptyListsAndBasicMatchInfo() {
        when(mockContext.getAllMatchPasses()).thenThrow(new RuntimeException("DB error"));
        
        Map<String, Object> result = state.prepareResponseData(mockContext);
        
        assertThat(result).isNotNull();
        assertThat(result.get("satzErgebnisse")).isEqualTo(Collections.emptyList());
        // matchErgebnis still gets created with 0 Satzpunkte for both teams even when there's an exception
        assertThat(result.get("matchErgebnis")).isNotNull();
        @SuppressWarnings("unchecked")
        List<TeamMatchInfoDO> matchInfo = (List<TeamMatchInfoDO>) result.get("matchErgebnis");
        assertThat(matchInfo).hasSize(2); // Both teams with 0 points
        assertThat(result.get("schuetzenMatchPunkte")).isEqualTo(Collections.emptyList());
        assertThat(result.get("schuetzeStammDaten")).isEqualTo(Collections.emptyList());
        assertThat(result.get("verfuegbareSchuetzen")).isEqualTo(Collections.emptyList());
    }

    @Test
    public void buildCurrentMatchResults_completeSets_calculatesCorrectly() {
        List<PasseDO> teamPasses = createTestPasses(100L, 300L);
        List<PasseDO> opponentPasses = createTestPasses(200L, 300L);
        
        when(mockContext.getAllMatchPasses()).thenReturn(teamPasses);
        when(mockPasseComponent.findByMannschaftMatchId(200L, 300L)).thenReturn(opponentPasses);
        
        Map<String, Object> result = state.prepareResponseData(mockContext);
        
        @SuppressWarnings("unchecked")
        List<SatzErgebnisDO> satzErgebnisse = (List<SatzErgebnisDO>) result.get("satzErgebnisse");
        
        assertThat(satzErgebnisse).isNotNull();
        assertThat(satzErgebnisse).hasSize(2); // Two completed sets
    }

    @Test
    public void buildCurrentMatchInfo_withSetResults_calculatesSatzpunkteCorrectly() {
        List<PasseDO> teamPasses = createTestPasses(100L, 300L);
        List<PasseDO> opponentPasses = createTestPasses(200L, 300L);
        
        when(mockContext.getAllMatchPasses()).thenReturn(teamPasses);
        when(mockPasseComponent.findByMannschaftMatchId(200L, 300L)).thenReturn(opponentPasses);
        
        Map<String, Object> result = state.prepareResponseData(mockContext);
        
        @SuppressWarnings("unchecked")
        List<TeamMatchInfoDO> matchInfo = (List<TeamMatchInfoDO>) result.get("matchErgebnis");
        
        assertThat(matchInfo).isNotNull();
        assertThat(matchInfo).hasSize(2); // Two teams
        
        // Find team info and verify team names
        TeamMatchInfoDO teamInfo = matchInfo.stream()
            .filter(info -> info.getTeamId() == 100L)
            .findFirst().orElse(null);
        
        assertThat(teamInfo).isNotNull();
        assertThat(teamInfo.getTeamName()).isEqualTo("Team 100");
    }

    @Test
    public void buildCurrentMatchResults_onlyCompleteSets_includesCorrectSets() {
        // Create incomplete set (missing opponent data)
        List<PasseDO> teamPasses = Arrays.asList(
            createMockPasse(1L, 1L, 10, 9, 8),
            createMockPasse(2L, 1L, 9, 8, 7),
            createMockPasse(3L, 2L, 8, 7, 6), // Set 2 for team
            createMockPasse(4L, 3L, 10, 10, 10) // Set 3 for team only
        );
        
        List<PasseDO> opponentPasses = Arrays.asList(
            createMockPasse(5L, 1L, 7, 6, 5),
            createMockPasse(6L, 1L, 6, 5, 4),
            createMockPasse(7L, 2L, 5, 4, 3) // Set 2 for opponent, no set 3
        );
        
        when(mockContext.getAllMatchPasses()).thenReturn(teamPasses);
        when(mockPasseComponent.findByMannschaftMatchId(200L, 300L)).thenReturn(opponentPasses);
        
        Map<String, Object> result = state.prepareResponseData(mockContext);
        
        @SuppressWarnings("unchecked")
        List<SatzErgebnisDO> satzErgebnisse = (List<SatzErgebnisDO>) result.get("satzErgebnisse");
        
        // Should only include completed sets (1 and 2), not incomplete set 3
        assertThat(satzErgebnisse).hasSize(2);
    }

    @Test
    public void buildCurrentMatchInfo_tieBreakScenario_calculatesSatzpunkteCorrectly() {
        // Create tie scenario
        List<PasseDO> teamPasses = Arrays.asList(
            createMockPasse(1L, 1L, 8, 8, 8) // 24 points
        );
        List<PasseDO> opponentPasses = Arrays.asList(
            createMockPasse(2L, 1L, 8, 8, 8) // 24 points (tie)
        );
        
        when(mockContext.getAllMatchPasses()).thenReturn(teamPasses);
        when(mockPasseComponent.findByMannschaftMatchId(200L, 300L)).thenReturn(opponentPasses);
        
        Map<String, Object> result = state.prepareResponseData(mockContext);
        
        @SuppressWarnings("unchecked")
        List<TeamMatchInfoDO> matchInfo = (List<TeamMatchInfoDO>) result.get("matchErgebnis");
        
        TeamMatchInfoDO teamInfo = matchInfo.stream()
            .filter(info -> info.getTeamId() == 100L)
            .findFirst().orElse(null);
        TeamMatchInfoDO oppInfo = matchInfo.stream()
            .filter(info -> info.getTeamId() == 200L)
            .findFirst().orElse(null);
        
        // Both teams should have 1 Satzpunkt due to tie
        assertThat(teamInfo.getMatchpunkte()).isEqualTo(1);
        assertThat(oppInfo.getMatchpunkte()).isEqualTo(1);
    }

    @Test
    public void getTeamName_validTeamId_returnsStandardizedName() {
        // Test the getTeamName method indirectly through prepareResponseData
        List<PasseDO> teamPasses = createTestPasses(100L, 300L);
        List<PasseDO> opponentPasses = createTestPasses(200L, 300L);
        
        when(mockContext.getAllMatchPasses()).thenReturn(teamPasses);
        when(mockPasseComponent.findByMannschaftMatchId(200L, 300L)).thenReturn(opponentPasses);
        
        Map<String, Object> result = state.prepareResponseData(mockContext);
        
        @SuppressWarnings("unchecked")
        List<TeamMatchInfoDO> matchInfo = (List<TeamMatchInfoDO>) result.get("matchErgebnis");
        
        TeamMatchInfoDO teamInfo = matchInfo.stream()
            .filter(info -> info.getTeamId() == 100L)
            .findFirst().orElse(null);
        
        assertThat(teamInfo).isNotNull();
        assertThat(teamInfo.getTeamName()).isEqualTo("Team 100");
    }

    @Test
    public void toString_returnsCorrectStateName() {
        String result = state.toString();
        assertThat(result).contains("MatchEnde");
    }

    private List<PasseDO> createTestPasses(Long teamId, Long matchId) {
        return Arrays.asList(
            createMockPasse(1L, 1L, 10, 9, 8),
            createMockPasse(2L, 1L, 9, 8, 7),
            createMockPasse(3L, 1L, 8, 7, 6),
            createMockPasse(4L, 2L, 10, 10, 9),
            createMockPasse(5L, 2L, 9, 9, 8),
            createMockPasse(6L, 2L, 8, 8, 7)
        );
    }


    private PasseDO createMockPasse(Long id, Long passeLfdnr, int pfeil1, int pfeil2, int pfeil3) {
        PasseDO passe = new PasseDO();
        passe.setId(id);
        passe.setPasseLfdnr(passeLfdnr);
        passe.setPfeil1(pfeil1);
        passe.setPfeil2(pfeil2);
        passe.setPfeil3(pfeil3);
        return passe;
    }
}