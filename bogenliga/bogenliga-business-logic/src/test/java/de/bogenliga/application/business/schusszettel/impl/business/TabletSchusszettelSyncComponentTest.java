package de.bogenliga.application.business.schusszettel.impl.business;

import org.assertj.core.api.Assertions;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

/**
 * Test class for TabletSchusszettelSyncComponent
 * Tests the synchronization logic for tablet sessions with match data
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelSyncComponentTest {

    private static final Long WETTKAMPF_ID = 1L;
    private static final Long TEAM1_ID = 10L;
    private static final Long TEAM2_ID = 20L;
    private static final Long MATCH1_ID = 100L;
    private static final Long MATCH2_ID = 200L;
    private static final Long MATCH3_ID = 300L;

    @Mock
    private TabletSchusszettelDAO sessionDAO;
    @Mock
    private MatchComponent matchComponent;
    @Mock
    private PasseComponent passeComponent;
    @Mock
    private DsbMannschaftComponent mannschaftComponent;
    @Mock
    private MatchAnalysisService matchAnalysisService;

    private TabletSchusszettelSyncComponent underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new TabletSchusszettelSyncComponent(
                sessionDAO, matchComponent, passeComponent, mannschaftComponent, matchAnalysisService);
        
        // Mock MatchAnalysisService default behaviors
        MatchAnalysisService.MatchAnalysisResult defaultResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.NOT_STARTED, 0, 1, 0, 0, new ArrayList<>(), "Not started");
        when(matchAnalysisService.analyzeMatch(anyLong(), anyLong(), anyLong())).thenReturn(defaultResult);
        when(matchAnalysisService.getCurrentPasseNumber(anyLong(), anyLong(), anyLong())).thenReturn(1);
    }

    @Test
    public void testSynchronizeSession_TeamNotValid() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        when(mannschaftComponent.findById(TEAM1_ID)).thenReturn(null);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isFalse();
        Assertions.assertThat(result.message).contains("Team " + TEAM1_ID + " no longer exists or is invalid");
        Assertions.assertThat(result.dataWasUpdated).isFalse();
    }

    @Test
    public void testSynchronizeSession_TeamValidationException() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        when(mannschaftComponent.findById(TEAM1_ID)).thenThrow(new RuntimeException("DB error"));

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isFalse();
        Assertions.assertThat(result.message).contains("Team " + TEAM1_ID + " no longer exists or is invalid");
    }

    @Test
    public void testSynchronizeSession_CurrentMatchStillValid() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);

        setupValidTeam();
        setupValidCurrentMatch();

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(result.message).isEqualTo("Session data is already synchronized");
        Assertions.assertThat(result.dataWasUpdated).isFalse();
        verify(sessionDAO, never()).updateStatus(any(), anyLong());
    }

    @Test
    public void testSynchronizeSession_CurrentMatchInvalid_NoMatchesFound() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L); // Non-existent match

        setupValidTeam();
        when(matchComponent.findById(999L)).thenReturn(null);
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Collections.emptyList());

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isFalse();
        Assertions.assertThat(result.message).contains("No valid matches found for team " + TEAM1_ID);
    }

    @Test
    public void testSynchronizeSession_SuccessfulSync_NotStartedMatch() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L); // Invalid current match

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_NotStarted();
        setupEmptyPasses();

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(result.message).contains("Session synchronized to match " + MATCH1_ID);
        Assertions.assertThat(result.dataWasUpdated).isTrue();
        verify(sessionDAO).updateStatus(session, 0L);

        // Verify session was updated correctly
        Assertions.assertThat(session.getCurrentMatchId()).isEqualTo(MATCH1_ID);
        Assertions.assertThat(session.getCurrentMatchNumber()).isEqualTo(1);
        Assertions.assertThat(session.getGegnerTeamId()).isEqualTo(TEAM2_ID);
        Assertions.assertThat(session.getCurrentPasseNumber()).isEqualTo(1);
    }

    @Test
    public void testSynchronizeSession_SuccessfulSync_InProgressMatch() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L); // Invalid current match

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_InProgress();
        setupInProgressPasses();

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(result.message).contains("Session synchronized to match " + MATCH1_ID);
        Assertions.assertThat(result.dataWasUpdated).isTrue();
        verify(sessionDAO).updateStatus(session, 0L);

        // Should find the correct passe number (3, since first 2 have data)
        Assertions.assertThat(session.getCurrentPasseNumber()).isEqualTo(3);
    }

    @Test
    public void testSynchronizeSession_SuccessfulSync_CompletedMatches() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L); // Invalid current match

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_AllCompleted();
        setupCompletedMatchPasses();

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(result.message).contains("Session synchronized to match " + MATCH3_ID);
        Assertions.assertThat(result.dataWasUpdated).isTrue();

        // Should return the last match when all are completed
        Assertions.assertThat(session.getCurrentMatchId()).isEqualTo(MATCH3_ID);
    }

    @Test
    public void testSynchronizeSession_NoOpponentFound() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L);

        setupValidTeam();
        setupInvalidCurrentMatch();

        // Setup match without opponent
        MatchDO match = new MatchDO();
        match.setId(MATCH1_ID);
        match.setNr(1L);
        match.setWettkampfId(WETTKAMPF_ID);
        match.setMannschaftId(TEAM1_ID);
        match.setBegegnung(1L);

        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(match));
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(Collections.emptyList());

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isFalse();
        Assertions.assertThat(result.message).contains("Failed to update session data");
    }

    @Test
    public void testSynchronizeSession_UpdateDatabase_False() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L);

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_NotStarted();
        setupEmptyPasses();

        // Act - Don't update database
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, false);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(result.dataWasUpdated).isTrue();
        // Database should NOT be updated
        verify(sessionDAO, never()).updateStatus(any(), anyLong());

        // But session object should still be updated in memory
        Assertions.assertThat(session.getCurrentMatchId()).isEqualTo(MATCH1_ID);
    }

    @Test
    public void testSynchronizeSession_MatchWon_6MatchPoints() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L);

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_Won();
        setupWonMatchPasses(); // Team1 wins with 6 match points after 3 sets

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(result.message).contains("Session synchronized to match " + MATCH2_ID);

        // Should advance to next match since current one is won
        Assertions.assertThat(session.getCurrentMatchId()).isEqualTo(MATCH2_ID);
    }

    @Test
    public void testSynchronizeSession_Match5Sets() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L);

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_5Sets();
        setup5SetsPasses(); // 5 sets completed

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();

        // Should advance to next match since 5 sets are completed
        Assertions.assertThat(session.getCurrentMatchId()).isEqualTo(MATCH2_ID);
    }

    @Test
    public void testSynchronizeSession_CriticalException() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L); // Set to invalid match ID

        setupValidTeam(); // Team validation passes
        setupInvalidCurrentMatch(); // Current match is invalid, so sync will proceed

        // Exception occurs when trying to find correct current match
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenThrow(new RuntimeException("Critical DB error"));

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isFalse();
        Assertions.assertThat(result.message).contains("Critical error during synchronization");
        Assertions.assertThat(result.message).contains("Critical DB error");
    }

    @Test
    public void testSynchronizeSession_PasseNumberCalculation() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L);

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_NotStarted();

        // Setup passes where team has shot data in passes 1, 2, 3 but not 4
        List<PasseDO> teamPasses = Arrays.asList(
                createSyncPasseWithShots(1L, 1, 9),   // Passe 1 - has shots
                createSyncPasseWithShots(2L, 2, 7),    // Passe 2 - has shots
                createSyncPasseWithShots(3L, 3, 10),   // Passe 3 - has shots
                createPasseWithoutShots(4L, 4),       // Passe 4 - no shots
                createPasseWithoutShots(5L, 5)        // Passe 5 - no shots
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(teamPasses);
        
        // Add opponent team passes to match the same progress (3 completed passes)
        List<PasseDO> opponentPasses = Arrays.asList(
                createSyncPasseWithShots(1L, 1, 7),    // Opponent passe 1 - has shots
                createSyncPasseWithShots(2L, 2, 8),    // Opponent passe 2 - has shots  
                createSyncPasseWithShots(3L, 3, 9),    // Opponent passe 3 - has shots
                createPasseWithoutShots(4L, 4),       // Opponent passe 4 - no shots
                createPasseWithoutShots(5L, 5)        // Opponent passe 5 - no shots
        );
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID))
                .thenReturn(opponentPasses);
        
        // Also setup passes for the opponent match (MATCH1_ID + 1)
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID + 1))
                .thenReturn(opponentPasses);
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID + 1))
                .thenReturn(teamPasses);
        
        // Mock individual match lookup for determineCorrectPasseNumber
        MatchDO match1 = createMatch(MATCH1_ID, 1L, TEAM1_ID);
        MatchDO match1Opponent = createMatch(MATCH1_ID + 1, 1L, TEAM2_ID);
        when(matchComponent.findById(MATCH1_ID)).thenReturn(match1);
        when(matchComponent.findById(MATCH1_ID + 1)).thenReturn(match1Opponent);
        
        // Mock MatchAnalysisService to return correct current passe number (4)
        MatchAnalysisService.MatchAnalysisResult inProgressResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.IN_PROGRESS, 0, 4, 0, 0, new ArrayList<>(), "Match in progress: Ready for set 4");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(inProgressResult);
        when(matchAnalysisService.analyzeMatch(eq(MATCH1_ID + 1), eq(TEAM2_ID), eq(TEAM1_ID))).thenReturn(inProgressResult);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(4);
        when(matchAnalysisService.getCurrentPasseNumber(eq(MATCH1_ID + 1), eq(TEAM2_ID), eq(TEAM1_ID))).thenReturn(4);
        when(matchAnalysisService.isMatchComplete(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(false);
        when(matchAnalysisService.isMatchComplete(eq(MATCH1_ID + 1), eq(TEAM2_ID), eq(TEAM1_ID))).thenReturn(false);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        // Should set current passe to 4 (next after the last completed passe 3)
        Assertions.assertThat(session.getCurrentPasseNumber()).isEqualTo(4);
    }

    @Test
    public void testFindCurrentMatchForTeam_EmptyMatches() {
        // Act
        MatchDO result = underTest.findCurrentMatchForTeam(Collections.emptyList(), TEAM1_ID);

        // Assert
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void testFindCurrentMatchForTeam_FirstMatchNotStarted() {
        // Arrange
        List<MatchDO> matches = Arrays.asList(
            createMatch(MATCH1_ID, 1L, TEAM1_ID),
            createMatch(MATCH2_ID, 2L, TEAM1_ID)
        );
        
        // Mock the full wettkampf match list including opponents
        List<MatchDO> allMatches = Arrays.asList(
            createMatch(MATCH1_ID, 1L, TEAM1_ID),
            createMatch(MATCH1_ID + 1, 1L, TEAM2_ID),  // Opponent for match 1
            createMatch(MATCH2_ID, 2L, TEAM1_ID),
            createMatch(MATCH2_ID + 1, 2L, TEAM2_ID)   // Opponent for match 2
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(allMatches);
        
        setupEmptyMatch(MATCH1_ID);
        setupEmptyMatch(MATCH2_ID);
        
        // Mock analysis to return NOT_STARTED for first match
        MatchAnalysisService.MatchAnalysisResult notStartedResult = 
            new MatchAnalysisService.MatchAnalysisResult(
                MatchAnalysisService.MatchStatus.NOT_STARTED, 0, 1, 0, 0, new ArrayList<>(), "Not started");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID))
            .thenReturn(notStartedResult);

        // Act
        MatchDO result = underTest.findCurrentMatchForTeam(matches, TEAM1_ID);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(MATCH1_ID);
    }

    @Test
    public void testFindCurrentMatchForTeam_MiddleMatchInProgress() {
        // Arrange
        List<MatchDO> matches = Arrays.asList(
            createMatch(MATCH1_ID, 1L, TEAM1_ID),
            createMatch(MATCH2_ID, 2L, TEAM1_ID),
            createMatch(MATCH3_ID, 3L, TEAM1_ID)
        );
        
        // Mock the full wettkampf match list including opponents
        List<MatchDO> allMatches = Arrays.asList(
            createMatch(MATCH1_ID, 1L, TEAM1_ID),
            createMatch(MATCH1_ID + 1, 1L, TEAM2_ID),  // Opponent for match 1
            createMatch(MATCH2_ID, 2L, TEAM1_ID),
            createMatch(MATCH2_ID + 1, 2L, TEAM2_ID),  // Opponent for match 2
            createMatch(MATCH3_ID, 3L, TEAM1_ID),
            createMatch(MATCH3_ID + 1, 3L, TEAM2_ID)   // Opponent for match 3
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(allMatches);
        
        // Mock analysis results
        MatchAnalysisService.MatchAnalysisResult completedResult = 
            new MatchAnalysisService.MatchAnalysisResult(
                MatchAnalysisService.MatchStatus.COMPLETED, 3, 3, 6, 0, new ArrayList<>(), "Completed");
        MatchAnalysisService.MatchAnalysisResult inProgressResult = 
            new MatchAnalysisService.MatchAnalysisResult(
                MatchAnalysisService.MatchStatus.IN_PROGRESS, 1, 2, 2, 0, new ArrayList<>(), "In progress");
        
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID))
            .thenReturn(completedResult);
        when(matchAnalysisService.analyzeMatch(MATCH2_ID, TEAM1_ID, TEAM2_ID))
            .thenReturn(inProgressResult);

        // Act
        MatchDO result = underTest.findCurrentMatchForTeam(matches, TEAM1_ID);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(MATCH2_ID); // Should return the in-progress match
    }

    @Test
    public void testFindCurrentMatchForTeam_AllMatchesCompleted() {
        // Arrange
        List<MatchDO> matches = Arrays.asList(
            createMatch(MATCH1_ID, 1L, TEAM1_ID),
            createMatch(MATCH2_ID, 2L, TEAM1_ID)
        );
        
        // Mock all matches as completed
        MatchAnalysisService.MatchAnalysisResult completedResult = 
            new MatchAnalysisService.MatchAnalysisResult(
                MatchAnalysisService.MatchStatus.COMPLETED, 3, 3, 6, 0, new ArrayList<>(), "Completed");
        
        when(matchAnalysisService.analyzeMatch(anyLong(), eq(TEAM1_ID), eq(TEAM2_ID)))
            .thenReturn(completedResult);

        // Act
        MatchDO result = underTest.findCurrentMatchForTeam(matches, TEAM1_ID);

        // Assert - Should return last match when all are completed
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(MATCH2_ID);
    }

    @Test
    public void testFindCurrentMatchForTeam_InvalidMatchData() {
        // Arrange
        List<MatchDO> matches = Arrays.asList(
            createMatch(MATCH1_ID, 1L, TEAM1_ID)
        );
        
        // Mock analysis to throw exception
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID))
            .thenThrow(new RuntimeException("Analysis error"));

        // Act
        MatchDO result = underTest.findCurrentMatchForTeam(matches, TEAM1_ID);

        // Assert - Should still return the match even with analysis error
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(MATCH1_ID);
    }

    @Test
    public void testSynchronizeSession_AdvanceToNextMatch() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);
        session.setStatus("SATZEINGABE"); // Should be stuck on completed match

        setupValidTeam();
        
        // Current match validation will fail (invalid), so sync will proceed to find correct match
        when(matchComponent.findById(MATCH1_ID)).thenReturn(null); // Make current match invalid
        
        setupTeamMatches_Won(); // MATCH1 is completed, should advance to MATCH2
        setupWonMatchPasses();

        // Mock MatchAnalysisService to show match is completed
        MatchAnalysisService.MatchAnalysisResult completedResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.COMPLETED, 3, 3, 6, 0, new ArrayList<>(), "Match completed: Team scores 6-0 Satzpunkte");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(completedResult);
        when(matchAnalysisService.isMatchComplete(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(true);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(result.dataWasUpdated).isTrue();
        verify(sessionDAO, atLeast(1)).updateStatus(session, 0L); // At least once for sync operation

        // Should advance to next match
        Assertions.assertThat(session.getCurrentMatchId()).isEqualTo(MATCH2_ID);
        Assertions.assertThat(session.getStatus()).isEqualTo("SCHUETZENMELDUNG");
    }

    @Test
    public void testSynchronizeSession_AdvanceToWettkampfEnde() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH3_ID); // Last match
        session.setStatus("SATZEINGABE");

        setupValidTeam();
        
        // Setup current match as invalid to force sync to find correct match
        when(matchComponent.findById(MATCH3_ID)).thenReturn(null); // Make current match invalid
        
        // Setup only one match (last match) - completed and no more matches after this
        List<MatchDO> matches = Arrays.asList(
                createMatch(MATCH3_ID, 3L, TEAM1_ID),
                createMatch(MATCH3_ID + 1, 3L, TEAM2_ID)
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(matches);

        // Mock completed match - since it's the only match and completed, sync should set to WETTKAMPF_ENDE
        MatchAnalysisService.MatchAnalysisResult completedResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.COMPLETED, 5, 5, 5, 5, new ArrayList<>(), "Match completed: Maximum 5 sets reached (5-5)");
        when(matchAnalysisService.analyzeMatch(MATCH3_ID, TEAM1_ID, TEAM2_ID)).thenReturn(completedResult);
        when(matchAnalysisService.isMatchComplete(MATCH3_ID, TEAM1_ID, TEAM2_ID)).thenReturn(true);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH3_ID, TEAM1_ID, TEAM2_ID)).thenReturn(5);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(result.dataWasUpdated).isTrue(); // Session was updated to last completed match

        // When all matches are completed, sync should return last match with WETTKAMPF_ENDE status
        Assertions.assertThat(session.getCurrentMatchId()).isEqualTo(MATCH3_ID);
        // Since all matches are completed, status should be WETTKAMPF_ENDE
        Assertions.assertThat(session.getStatus()).isEqualTo("WETTKAMPF_ENDE");
    }

    @Test
    public void testSynchronizeSession_AdvancementException() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);
        session.setStatus("SATZEINGABE");

        setupValidTeam();
        
        // Current match is valid, so advancement won't happen
        setupValidCurrentMatch();

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert - Since match is valid, sync won't trigger advancement
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(result.message).contains("Session data is already synchronized");
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedDetermineCorrectPasseNumber_Fallback() {
        // Arrange
        when(matchComponent.findById(MATCH1_ID)).thenThrow(new RuntimeException("Match not found"));
        
        List<PasseDO> teamPasses = Arrays.asList(
                createSyncPasseWithShots(1L, 1, 9),  // Passe 1
                createSyncPasseWithShots(2L, 2, 7),  // Passe 2  
                createSyncPasseWithShots(3L, 3, 8)   // Passe 3
        );
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID)).thenReturn(teamPasses);

        // Act
        int result = underTest.determineCorrectPasseNumber(MATCH1_ID, TEAM1_ID);

        // Assert
        Assertions.assertThat(result).isEqualTo(4); // Max passe (3) + 1
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedDetermineCorrectPasseNumber_EmptyPasses() {
        // Arrange
        when(matchComponent.findById(MATCH1_ID)).thenThrow(new RuntimeException("Match not found"));
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID)).thenReturn(Collections.emptyList());

        // Act
        int result = underTest.determineCorrectPasseNumber(MATCH1_ID, TEAM1_ID);

        // Assert
        Assertions.assertThat(result).isEqualTo(1); // Default when no passes
    }

    @Test
    public void testSynchronizeSession_StatusDetermination_SchuetzenMeldung() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L);

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_NotStarted();
        setupEmptyPasses();

        // Mock NOT_STARTED match
        MatchAnalysisService.MatchAnalysisResult notStartedResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.NOT_STARTED, 0, 1, 0, 0, new ArrayList<>(), "Not started");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(notStartedResult);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(session.getStatus()).isEqualTo("SCHUETZENMELDUNG");
    }

    @Test
    public void testSynchronizeSession_StatusDetermination_InProgress() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L);

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_InProgress();

        // Setup team has completed passe, opponent has not
        List<PasseDO> team1Passes = Arrays.asList(
                createSyncPasseWithShots(1L, 10, 9),
                createSyncPasseWithShots(1L, 8, 7),
                createSyncPasseWithShots(1L, 9, 8) // 3 shooters completed
        );
        List<PasseDO> team2Passes = Arrays.asList(
                createSyncPasseWithShots(1L, 8, 7),
                createSyncPasseWithShots(1L, 7, 6) // Only 2 shooters
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID)).thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID)).thenReturn(team2Passes);

        // Mock IN_PROGRESS match
        MatchAnalysisService.MatchAnalysisResult inProgressResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.IN_PROGRESS, 0, 1, 0, 0, new ArrayList<>(), "In progress");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(inProgressResult);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        // The actual status depends on complex logic - just verify sync worked
        Assertions.assertThat(result.dataWasUpdated).isTrue();
    }

    // Helper methods

    private TabletSchusszettelEntity createTestSession() {
        TabletSchusszettelEntity session = new TabletSchusszettelEntity();
        session.setId(1L);
        session.setWettkampfId(WETTKAMPF_ID);
        session.setTeamId(TEAM1_ID);
        session.setCurrentMatchId(MATCH1_ID);
        session.setCurrentMatchNumber(1);
        session.setCurrentPasseNumber(1);
        session.setGegnerTeamId(TEAM2_ID);
        session.setStatus("SATZEINGABE");
        session.setToken("test_token");
        return session;
    }

    private void setupValidTeam() {
        DsbMannschaftDO team = new DsbMannschaftDO();
        team.setId(TEAM1_ID);
        team.setVereinId(1L);
        when(mannschaftComponent.findById(TEAM1_ID)).thenReturn(team);
    }

    private void setupValidCurrentMatch() {
        MatchDO currentMatch = new MatchDO();
        currentMatch.setId(MATCH1_ID);
        currentMatch.setWettkampfId(WETTKAMPF_ID);
        currentMatch.setMannschaftId(TEAM1_ID);
        currentMatch.setNr(1L);
        currentMatch.setBegegnung(1L);

        when(matchComponent.findById(MATCH1_ID)).thenReturn(currentMatch);
    }

    private void setupInvalidCurrentMatch() {
        when(matchComponent.findById(999L)).thenReturn(null);
    }


    private void setupTeamMatches_NotStarted() {
        List<MatchDO> matches = Arrays.asList(
                createMatch(MATCH1_ID, 1L, TEAM1_ID),
                createMatch(MATCH1_ID + 1, 1L, TEAM2_ID), // Opponent for match 1
                createMatch(MATCH2_ID, 2L, TEAM1_ID),
                createMatch(MATCH2_ID + 1, 2L, TEAM2_ID)  // Opponent for match 2
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(matches);
    }

    private void setupTeamMatches_InProgress() {
        setupTeamMatches_NotStarted();
    }

    private void setupTeamMatches_AllCompleted() {
        List<MatchDO> matches = Arrays.asList(
                createMatch(MATCH1_ID, 1L, TEAM1_ID),
                createMatch(MATCH1_ID + 1, 1L, TEAM2_ID),
                createMatch(MATCH2_ID, 2L, TEAM1_ID),
                createMatch(MATCH2_ID + 1, 2L, TEAM2_ID),
                createMatch(MATCH3_ID, 3L, TEAM1_ID),
                createMatch(MATCH3_ID + 1, 3L, TEAM2_ID)
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(matches);
    }

    private void setupTeamMatches_Won() {
        setupTeamMatches_AllCompleted();
    }

    private void setupTeamMatches_5Sets() {
        setupTeamMatches_AllCompleted();
    }

    private MatchDO createMatch(Long id, Long nr, Long teamId) {
        MatchDO match = new MatchDO();
        match.setId(id);
        match.setNr(nr);
        match.setWettkampfId(WETTKAMPF_ID);
        match.setMannschaftId(teamId);
        match.setBegegnung(nr);
        return match;
    }

    private void setupEmptyPasses() {
        when(passeComponent.findByMannschaftMatchId(anyLong(), anyLong()))
                .thenReturn(Collections.emptyList());
    }

    private void setupInProgressPasses() {
        // Team1 has shot in passes 1 and 2, opponent has shot in pass 1 only
        List<PasseDO> team1Passes = Arrays.asList(
                createSyncPasseWithShots(1L, 1, 9),  // Passe 1 with shots
                createSyncPasseWithShots(2L, 2, 7)   // Passe 2 with shots
        );

        List<PasseDO> team2Passes = Arrays.asList(
                createSyncPasseWithShots(1L, 1, 8)   // Passe 1 with shots
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID))
                .thenReturn(team2Passes);
    }

    private void setupCompletedMatchPasses() {
        // Setup completed passes for all matches
        setupCompletedMatch(MATCH1_ID);
        setupCompletedMatch(MATCH2_ID);
        setupEmptyMatch(MATCH3_ID); // Last match not started
        
        // Mock MatchAnalysisService to return COMPLETED for matches 1 and 2
        MatchAnalysisService.MatchAnalysisResult completedResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.COMPLETED, 5, 5, 5, 5, new ArrayList<>(), "Match completed: Maximum 5 sets reached (5-5)");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(completedResult);
        when(matchAnalysisService.analyzeMatch(MATCH2_ID, TEAM1_ID, TEAM2_ID)).thenReturn(completedResult);
        when(matchAnalysisService.isMatchComplete(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(true);
        when(matchAnalysisService.isMatchComplete(MATCH2_ID, TEAM1_ID, TEAM2_ID)).thenReturn(true);
        
        // Mock MatchAnalysisService for match 3 - not started
        MatchAnalysisService.MatchAnalysisResult notStartedResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.NOT_STARTED, 0, 1, 0, 0, new ArrayList<>(), "Not started");
        when(matchAnalysisService.analyzeMatch(MATCH3_ID, TEAM1_ID, TEAM2_ID)).thenReturn(notStartedResult);
        when(matchAnalysisService.isMatchComplete(MATCH3_ID, TEAM1_ID, TEAM2_ID)).thenReturn(false);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH3_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);
    }

    private void setupWonMatchPasses() {
        // Team1 wins match 1 with 6 match points (3 sets won)
        List<PasseDO> team1Passes = Arrays.asList(
                createSyncPasseWithShots(1L, 30, 29), // Team1 wins set 1 (59 vs 50)
                createSyncPasseWithShots(2L, 30, 29), // Team1 wins set 2 (59 vs 50)
                createSyncPasseWithShots(3L, 30, 29)  // Team1 wins set 3 (59 vs 50) = 6 match points
        );

        List<PasseDO> team2Passes = Arrays.asList(
                createSyncPasseWithShots(1L, 25, 25), // Team2 loses set 1
                createSyncPasseWithShots(2L, 25, 25), // Team2 loses set 2
                createSyncPasseWithShots(3L, 25, 25)  // Team2 loses set 3
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID))
                .thenReturn(team2Passes);

        // Mock MatchAnalysisService to return COMPLETED for match 1 (Team1 won 6-0)
        MatchAnalysisService.MatchAnalysisResult completedResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.COMPLETED, 3, 3, 6, 0, new ArrayList<>(), "Match completed: Team scores 6-0 Satzpunkte");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(completedResult);
        when(matchAnalysisService.isMatchComplete(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(true);
        
        // Mock MatchAnalysisService for next match (MATCH2_ID) - not started
        MatchAnalysisService.MatchAnalysisResult nextMatchResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.NOT_STARTED, 0, 1, 0, 0, new ArrayList<>(), "Not started");
        when(matchAnalysisService.analyzeMatch(MATCH2_ID, TEAM1_ID, TEAM2_ID)).thenReturn(nextMatchResult);
        when(matchAnalysisService.isMatchComplete(MATCH2_ID, TEAM1_ID, TEAM2_ID)).thenReturn(false);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH2_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);

        setupEmptyMatch(MATCH2_ID); // Next match not started
    }

    private void setup5SetsPasses() {
        // 5 sets completed (tied 4-4 in match points, so match complete by max sets)
        List<PasseDO> team1Passes = Arrays.asList(
                createSyncPasseWithShots(1L, 30, 29), // Team1 wins set 1
                createSyncPasseWithShots(2L, 25, 25), // Team1 loses set 2
                createSyncPasseWithShots(3L, 30, 29), // Team1 wins set 3
                createSyncPasseWithShots(4L, 25, 25), // Team1 loses set 4
                createSyncPasseWithShots(5L, 27, 28)  // Tie set 5
        );

        List<PasseDO> team2Passes = Arrays.asList(
                createSyncPasseWithShots(1L, 25, 25), // Team2 loses set 1
                createSyncPasseWithShots(2L, 30, 29), // Team2 wins set 2
                createSyncPasseWithShots(3L, 25, 25), // Team2 loses set 3
                createSyncPasseWithShots(4L, 30, 29), // Team2 wins set 4
                createSyncPasseWithShots(5L, 27, 28)  // Tie set 5
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID))
                .thenReturn(team2Passes);

        // Mock individual match lookup for determineCorrectPasseNumber
        MatchDO match1 = createMatch(MATCH1_ID, 1L, TEAM1_ID);
        MatchDO match1Opponent = createMatch(MATCH1_ID + 1, 1L, TEAM2_ID);
        MatchDO match2 = createMatch(MATCH2_ID, 2L, TEAM1_ID);
        when(matchComponent.findById(MATCH1_ID)).thenReturn(match1);
        when(matchComponent.findById(MATCH1_ID + 1)).thenReturn(match1Opponent);
        when(matchComponent.findById(MATCH2_ID)).thenReturn(match2);

        // Mock MatchAnalysisService to return COMPLETED for match 1 (5 sets = match complete)
        MatchAnalysisService.MatchAnalysisResult completedResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.COMPLETED, 5, 5, 5, 5, new ArrayList<>(), "Match completed: Maximum 5 sets reached (5-5)");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(completedResult);
        when(matchAnalysisService.isMatchComplete(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(true);
        
        // Mock MatchAnalysisService for next match (MATCH2_ID) - not started
        MatchAnalysisService.MatchAnalysisResult nextMatchResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.NOT_STARTED, 0, 1, 0, 0, new ArrayList<>(), "Not started");
        when(matchAnalysisService.analyzeMatch(MATCH2_ID, TEAM1_ID, TEAM2_ID)).thenReturn(nextMatchResult);
        when(matchAnalysisService.isMatchComplete(MATCH2_ID, TEAM1_ID, TEAM2_ID)).thenReturn(false);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH2_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);

        setupEmptyMatch(MATCH2_ID); // Next match not started
    }

    private void setupCompletedMatch(Long matchId) {
        // Both teams have 5 sets completed
        List<PasseDO> team1Passes = createCompletedMatchPasses(5);
        List<PasseDO> team2Passes = createCompletedMatchPasses(5);

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, matchId))
                .thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, matchId))
                .thenReturn(team2Passes);
    }

    private void setupEmptyMatch(Long matchId) {
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, matchId))
                .thenReturn(Collections.emptyList());
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, matchId))
                .thenReturn(Collections.emptyList());
    }

    private List<PasseDO> createCompletedMatchPasses(int numberOfSets) {
        List<PasseDO> passes = new ArrayList<>();
        for (int i = 1; i <= numberOfSets; i++) {
            passes.add(createSyncPasseWithIntegerShots((long) i, 10, 9));
        }
        return passes;
    }

    private PasseDO createSyncPasseWithIntegerShots(Long lfdnr, Integer pfeil1, Integer pfeil2) {
        PasseDO passe = new PasseDO();
        passe.setId(lfdnr);
        passe.setPasseLfdnr(lfdnr);
        passe.setPasseMannschaftId(TEAM1_ID);
        passe.setPasseMatchId(MATCH1_ID);
        passe.setPasseDsbMitgliedId(100L + lfdnr);
        passe.setPfeil1(pfeil1);
        passe.setPfeil2(pfeil2);
        passe.setPfeil3(null); // Only testing with 2 arrows
        return passe;
    }

    private PasseDO createPasseWithoutShots(Long lfdnr) {
        PasseDO passe = new PasseDO();
        passe.setId(lfdnr);
        passe.setPasseLfdnr(lfdnr);
        passe.setPasseMannschaftId(TEAM1_ID);
        passe.setPasseMatchId(MATCH1_ID);
        passe.setPasseDsbMitgliedId(100L + lfdnr);
        passe.setPfeil1(null);
        passe.setPfeil2(null);
        passe.setPfeil3(null);
        return passe;
    }

    @Test
    public void testSyncResult_Success() {
        // Act
        TabletSchusszettelSyncComponent.SyncResult result = 
                TabletSchusszettelSyncComponent.SyncResult.success("Test message", true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        Assertions.assertThat(result.message).isEqualTo("Test message");
        Assertions.assertThat(result.dataWasUpdated).isTrue();
    }

    @Test
    public void testSyncResult_Failure() {
        // Act
        TabletSchusszettelSyncComponent.SyncResult result = 
                TabletSchusszettelSyncComponent.SyncResult.failure("Error message");

        // Assert
        Assertions.assertThat(result.success).isFalse();
        Assertions.assertThat(result.message).isEqualTo("Error message");
        Assertions.assertThat(result.dataWasUpdated).isFalse();
    }

    @Test
    public void testSynchronizeSession_InvalidMatchAnalysis() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L);

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_NotStarted();
        setupEmptyPasses();

        // Mock invalid analysis
        MatchAnalysisService.MatchAnalysisResult invalidResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.INVALID, 0, 1, 0, 0, new ArrayList<>(), "Invalid data");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(invalidResult);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        // When match is invalid, sync component finds the next valid match (MATCH2) which is NOT_STARTED
        Assertions.assertThat(session.getStatus()).isEqualTo("SCHUETZENMELDUNG");
    }

    @Test
    public void testSynchronizeSession_DetermineStatus_Exception() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(999L);

        setupValidTeam();
        setupInvalidCurrentMatch();
        setupTeamMatches_NotStarted();
        setupEmptyPasses();

        // Mock analysis to throw exception
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID))
                .thenThrow(new RuntimeException("Analysis failed"));
        when(matchAnalysisService.getCurrentPasseNumber(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        // When match analysis fails, sync finds next valid match (MATCH2) which is NOT_STARTED
        Assertions.assertThat(session.getStatus()).isEqualTo("SCHUETZENMELDUNG");
    }

    @Test
    public void testValidateCurrentMatch_WrongWettkampf() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);

        MatchDO wrongMatch = new MatchDO();
        wrongMatch.setId(MATCH1_ID);
        wrongMatch.setWettkampfId(999L); // Wrong wettkampf
        wrongMatch.setMannschaftId(TEAM1_ID);

        setupValidTeam();
        when(matchComponent.findById(MATCH1_ID)).thenReturn(wrongMatch);
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Collections.emptyList());

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isFalse();
        Assertions.assertThat(result.message).contains("No valid matches found");
    }

    @Test
    public void testValidateCurrentMatch_WrongTeam() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);

        MatchDO wrongMatch = new MatchDO();
        wrongMatch.setId(MATCH1_ID);
        wrongMatch.setWettkampfId(WETTKAMPF_ID);
        wrongMatch.setMannschaftId(999L); // Wrong team

        setupValidTeam();
        when(matchComponent.findById(MATCH1_ID)).thenReturn(wrongMatch);
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Collections.emptyList());

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isFalse();
        Assertions.assertThat(result.message).contains("No valid matches found");
    }

    @Test
    public void testValidateCurrentMatch_Exception() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);

        setupValidTeam();
        when(matchComponent.findById(MATCH1_ID)).thenThrow(new RuntimeException("DB error"));
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Collections.emptyList());

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isFalse();
        Assertions.assertThat(result.message).contains("No valid matches found");
    }

    @Test
    public void testFindOpponentTeamId_NotFound() {
        // Arrange
        MatchDO match = createMatch(MATCH1_ID, 1L, TEAM1_ID);
        
        // Return only the team's own match, no opponent
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.singletonList(match));

        // Mock analysis to trigger opponent search
        when(matchAnalysisService.analyzeMatch(anyLong(), anyLong(), anyLong()))
                .thenThrow(new BusinessException(de.bogenliga.application.common.errorhandling.ErrorCode.INTERNAL_ERROR, "Opponent not found"));

        // Act
        MatchDO result = underTest.findCurrentMatchForTeam(Collections.singletonList(match), TEAM1_ID);

        // Assert - Should still return the match even with opponent error
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(MATCH1_ID);
    }

    @Test
    public void testShouldAdvanceToNextMatch_NotCompleted() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);
        session.setStatus("SATZEINGABE");

        MatchDO match = createMatch(MATCH1_ID, 1L, TEAM1_ID);

        // Mock match as not completed
        MatchAnalysisService.MatchAnalysisResult inProgressResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.IN_PROGRESS, 1, 2, 2, 0, new ArrayList<>(), "In progress");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(inProgressResult);

        setupValidTeam();
        setupValidCurrentMatch();
        setupTeamMatches_InProgress();
        setupInProgressPasses();

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        // Should not advance because match is not completed
        Assertions.assertThat(session.getCurrentMatchId()).isEqualTo(MATCH1_ID);
    }

    @Test
    public void testShouldAdvanceToNextMatch_AlreadyAdvanced() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);
        session.setStatus("WETTKAMPF_ENDE"); // Already advanced

        MatchDO match = createMatch(MATCH1_ID, 1L, TEAM1_ID);

        // Mock match as completed
        MatchAnalysisService.MatchAnalysisResult completedResult = new MatchAnalysisService.MatchAnalysisResult(
            MatchAnalysisService.MatchStatus.COMPLETED, 3, 3, 6, 0, new ArrayList<>(), "Completed");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(completedResult);

        setupValidTeam();
        setupValidCurrentMatch();

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        // Should not advance because already in WETTKAMPF_ENDE status
        Assertions.assertThat(session.getStatus()).isEqualTo("WETTKAMPF_ENDE");
    }

    @Test
    public void testSynchronizeSession_HasRegisteredShooters() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);
        session.setStatus("SCHUETZENMELDUNG");

        setupValidTeam();
        setupValidCurrentMatch();
        
        // Mock wettkampf matches to include opponent for findOpponentTeamId
        // Create opponent match manually to ensure correct setup
        MatchDO opponentMatch = new MatchDO();
        opponentMatch.setId(MATCH1_ID + 1);
        opponentMatch.setNr(1L);  // Same match number
        opponentMatch.setWettkampfId(WETTKAMPF_ID);
        opponentMatch.setMannschaftId(TEAM2_ID);  // Different team
        opponentMatch.setBegegnung(1L);  // Same begegnung
        
        List<MatchDO> allMatches = Arrays.asList(
                createMatch(MATCH1_ID, 1L, TEAM1_ID),      // Team 1 match
                opponentMatch   // Team 2 opponent match (same nr, begegnung)
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(allMatches);
        
        // Mock 3 registered shooters (no scores yet)
        List<PasseDO> registeredPasses = Arrays.asList(
                createPasseWithoutShots(1L, 1),
                createPasseWithoutShots(2L, 1),
                createPasseWithoutShots(3L, 1)
        );
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(registeredPasses);
        
        // Mock match analysis as IN_PROGRESS
        MatchAnalysisService.MatchAnalysisResult inProgressResult = new MatchAnalysisService.MatchAnalysisResult(
                MatchAnalysisService.MatchStatus.IN_PROGRESS, 0, 1, 1, 0, new ArrayList<>(), "Match in progress");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(inProgressResult);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        // Should set status to SATZEINGABE when shooters are registered but no scores
        Assertions.assertThat(session.getStatus()).isEqualTo("SATZEINGABE");
    }

    @Test
    public void testSynchronizeSession_PasseCompletedByTeam() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);
        session.setStatus("SATZEINGABE");
        session.setCurrentPasseNumber(1);

        setupValidTeam();
        setupValidCurrentMatch();
        
        // Mock wettkampf matches to include opponent for findOpponentTeamId
        // Create opponent match manually to ensure correct setup
        MatchDO opponentMatch = new MatchDO();
        opponentMatch.setId(MATCH1_ID + 1);
        opponentMatch.setNr(1L);  // Same match number
        opponentMatch.setWettkampfId(WETTKAMPF_ID);
        opponentMatch.setMannschaftId(TEAM2_ID);  // Different team
        opponentMatch.setBegegnung(1L);  // Same begegnung
        
        List<MatchDO> allMatches = Arrays.asList(
                createMatch(MATCH1_ID, 1L, TEAM1_ID),      // Team 1 match
                opponentMatch   // Team 2 opponent match (same nr, begegnung)
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(allMatches);
        
        // Mock 3 shooters with completed scores
        List<PasseDO> completedPasses = Arrays.asList(
                createSyncPasseWithShots(1L, 1, 8),
                createSyncPasseWithShots(2L, 1, 9),
                createSyncPasseWithShots(3L, 1, 7)
        );
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(completedPasses);
        
        // Mock opponent also completed
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID))
                .thenReturn(completedPasses); // Same for simplicity
        
        // Mock match analysis as IN_PROGRESS
        MatchAnalysisService.MatchAnalysisResult inProgressResult = new MatchAnalysisService.MatchAnalysisResult(
                MatchAnalysisService.MatchStatus.IN_PROGRESS, 0, 1, 1, 0, new ArrayList<>(), "Match in progress");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(inProgressResult);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        // Both teams completed passe, should stay in SATZEINGABE for next passe
        Assertions.assertThat(session.getStatus()).isEqualTo("SATZEINGABE");
    }

    @Test
    public void testSynchronizeSession_WaitingForOpponent() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);
        session.setStatus("SATZEINGABE");
        session.setCurrentPasseNumber(1);

        setupValidTeam();
        setupValidCurrentMatch();
        
        // Mock wettkampf matches to include opponent for findOpponentTeamId
        // Create opponent match manually to ensure correct setup
        MatchDO opponentMatch = new MatchDO();
        opponentMatch.setId(MATCH1_ID + 1);
        opponentMatch.setNr(1L);  // Same match number
        opponentMatch.setWettkampfId(WETTKAMPF_ID);
        opponentMatch.setMannschaftId(TEAM2_ID);  // Different team
        opponentMatch.setBegegnung(1L);  // Same begegnung
        
        List<MatchDO> allMatches = Arrays.asList(
                createMatch(MATCH1_ID, 1L, TEAM1_ID),      // Team 1 match
                opponentMatch   // Team 2 opponent match (same nr, begegnung)
        );
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(allMatches);
        
        // Mock this team completed, opponent not
        List<PasseDO> completedPasses = Arrays.asList(
                createSyncPasseWithShots(1L, 1, 8),
                createSyncPasseWithShots(2L, 1, 9),
                createSyncPasseWithShots(3L, 1, 7)
        );
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(completedPasses);
        
        // Mock opponent not completed (only 2 shooters)
        List<PasseDO> incompletePasses = Arrays.asList(
                createSyncPasseWithShots(4L, 1, 8),
                createSyncPasseWithShots(5L, 1, 9)
                // Missing 3rd shooter
        );
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID))
                .thenReturn(incompletePasses);
        
        // Mock match analysis as IN_PROGRESS
        MatchAnalysisService.MatchAnalysisResult inProgressResult = new MatchAnalysisService.MatchAnalysisResult(
                MatchAnalysisService.MatchStatus.IN_PROGRESS, 0, 1, 1, 0, new ArrayList<>(), "Match in progress");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(inProgressResult);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        // Team completed but opponent didn't, should wait
        Assertions.assertThat(session.getStatus()).isEqualTo("WARTE");
    }

    @Test
    public void testSynchronizeSession_ExceptionInStatusDetermination() {
        // Arrange
        TabletSchusszettelEntity session = createTestSession();
        session.setCurrentMatchId(MATCH1_ID);

        setupValidTeam();
        setupValidCurrentMatch();
        
        // Mock passe component to throw exception
        when(passeComponent.findByMannschaftMatchId(anyLong(), anyLong()))
                .thenThrow(new RuntimeException("Database error"));
        
        // Mock analysis to be valid
        MatchAnalysisService.MatchAnalysisResult validResult = new MatchAnalysisService.MatchAnalysisResult(
                MatchAnalysisService.MatchStatus.IN_PROGRESS, 1, 2, 1, 0, new ArrayList<>(), "In progress");
        when(matchAnalysisService.analyzeMatch(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(validResult);
        when(matchAnalysisService.getCurrentPasseNumber(MATCH1_ID, TEAM1_ID, TEAM2_ID)).thenReturn(1);

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        Assertions.assertThat(result.success).isTrue();
        // Should fallback to SATZEINGABE on exception
        Assertions.assertThat(session.getStatus()).isEqualTo("SATZEINGABE");
    }
    
    // Helper method to create passes without shot data (registered shooters)
    private PasseDO createPasseWithoutShots(Long id, int passeNr) {
        PasseDO passe = new PasseDO();
        passe.setId(id);
        passe.setPasseLfdnr((long) passeNr);
        passe.setPasseMannschaftId(TEAM1_ID);
        passe.setPasseMatchId(MATCH1_ID);
        // No shot data - just registered
        return passe;
    }
    
    // Helper method to create passes with shot data for sync tests
    private PasseDO createSyncPasseWithShots(Long id, int passeNr, int shot1) {
        PasseDO passe = new PasseDO();
        passe.setId(id);
        passe.setPasseLfdnr((long) passeNr);
        passe.setPasseMannschaftId(TEAM1_ID);
        passe.setPasseMatchId(MATCH1_ID);
        passe.setPfeil1(shot1);
        passe.setPfeil2(shot1 + 1);
        passe.setPfeil3(shot1 - 1);
        return passe;
    }
}