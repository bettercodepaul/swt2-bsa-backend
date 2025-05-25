package de.bogenliga.application.business.schusszettel.impl.business;

import static org.assertj.core.api.Assertions.assertThat;
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

    private TabletSchusszettelSyncComponent underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new TabletSchusszettelSyncComponent(
                sessionDAO, matchComponent, passeComponent, mannschaftComponent);
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
        assertThat(result.success).isFalse();
        assertThat(result.message).contains("Team " + TEAM1_ID + " no longer exists or is invalid");
        assertThat(result.dataWasUpdated).isFalse();
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
        assertThat(result.success).isFalse();
        assertThat(result.message).contains("Team " + TEAM1_ID + " no longer exists or is invalid");
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
        assertThat(result.success).isTrue();
        assertThat(result.message).isEqualTo("Session data is already synchronized");
        assertThat(result.dataWasUpdated).isFalse();
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
        assertThat(result.success).isFalse();
        assertThat(result.message).contains("No valid matches found for team " + TEAM1_ID);
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
        assertThat(result.success).isTrue();
        assertThat(result.message).contains("Session synchronized to match " + MATCH1_ID);
        assertThat(result.dataWasUpdated).isTrue();
        verify(sessionDAO).updateStatus(session, 0L);

        // Verify session was updated correctly
        assertThat(session.getCurrentMatchId()).isEqualTo(MATCH1_ID);
        assertThat(session.getCurrentMatchNumber()).isEqualTo(1);
        assertThat(session.getGegnerTeamId()).isEqualTo(TEAM2_ID);
        assertThat(session.getCurrentPasseNumber()).isEqualTo(1);
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
        assertThat(result.success).isTrue();
        assertThat(result.message).contains("Session synchronized to match " + MATCH1_ID);
        assertThat(result.dataWasUpdated).isTrue();
        verify(sessionDAO).updateStatus(session, 0L);

        // Should find the correct passe number (3, since first 2 have data)
        assertThat(session.getCurrentPasseNumber()).isEqualTo(3);
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
        assertThat(result.success).isTrue();
        assertThat(result.message).contains("Session synchronized to match " + MATCH3_ID);
        assertThat(result.dataWasUpdated).isTrue();

        // Should return the last match when all are completed
        assertThat(session.getCurrentMatchId()).isEqualTo(MATCH3_ID);
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
        assertThat(result.success).isFalse();
        assertThat(result.message).contains("Failed to update session data");
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
        assertThat(result.success).isTrue();
        assertThat(result.dataWasUpdated).isTrue();
        // Database should NOT be updated
        verify(sessionDAO, never()).updateStatus(any(), anyLong());

        // But session object should still be updated in memory
        assertThat(session.getCurrentMatchId()).isEqualTo(MATCH1_ID);
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
        assertThat(result.success).isTrue();
        assertThat(result.message).contains("Session synchronized to match " + MATCH2_ID);

        // Should advance to next match since current one is won
        assertThat(session.getCurrentMatchId()).isEqualTo(MATCH2_ID);
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
        assertThat(result.success).isTrue();

        // Should advance to next match since 5 sets are completed
        assertThat(session.getCurrentMatchId()).isEqualTo(MATCH2_ID);
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
        assertThat(result.success).isFalse();
        assertThat(result.message).contains("Critical error during synchronization");
        assertThat(result.message).contains("Critical DB error");
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
                createPasseWithShots(1L, 10, 9),   // Passe 1 - has shots
                createPasseWithShots(2L, 8, 7),    // Passe 2 - has shots
                createPasseWithShots(3L, 9, 10),   // Passe 3 - has shots
                createPasseWithoutShots(4L),       // Passe 4 - no shots
                createPasseWithoutShots(5L)        // Passe 5 - no shots
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(teamPasses);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID))
                .thenReturn(Collections.emptyList());

        // Act
        TabletSchusszettelSyncComponent.SyncResult result =
                underTest.synchronizeSession(session, WETTKAMPF_ID, TEAM1_ID, true);

        // Assert
        assertThat(result.success).isTrue();
        // Should set current passe to 4 (next after the last completed passe 3)
        assertThat(session.getCurrentPasseNumber()).isEqualTo(4);
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
                createPasseWithShots(1L, 10, 9),
                createPasseWithShots(2L, 8, 7)
        );

        List<PasseDO> team2Passes = Arrays.asList(
                createPasseWithShots(1L, 9, 8)
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
    }

    private void setupWonMatchPasses() {
        // Team1 wins match 1 with 6 match points (3 sets won)
        List<PasseDO> team1Passes = Arrays.asList(
                createPasseWithShots(1L, 30, 29), // Team1 wins set 1 (59 vs 50)
                createPasseWithShots(2L, 30, 29), // Team1 wins set 2 (59 vs 50)
                createPasseWithShots(3L, 30, 29)  // Team1 wins set 3 (59 vs 50) = 6 match points
        );

        List<PasseDO> team2Passes = Arrays.asList(
                createPasseWithShots(1L, 25, 25), // Team2 loses set 1
                createPasseWithShots(2L, 25, 25), // Team2 loses set 2
                createPasseWithShots(3L, 25, 25)  // Team2 loses set 3
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID))
                .thenReturn(team2Passes);

        setupEmptyMatch(MATCH2_ID); // Next match not started
    }

    private void setup5SetsPasses() {
        // 5 sets completed (tied 4-4 in match points, so match complete by max sets)
        List<PasseDO> team1Passes = Arrays.asList(
                createPasseWithShots(1L, 30, 29), // Team1 wins set 1
                createPasseWithShots(2L, 25, 25), // Team1 loses set 2
                createPasseWithShots(3L, 30, 29), // Team1 wins set 3
                createPasseWithShots(4L, 25, 25), // Team1 loses set 4
                createPasseWithShots(5L, 27, 28)  // Tie set 5
        );

        List<PasseDO> team2Passes = Arrays.asList(
                createPasseWithShots(1L, 25, 25), // Team2 loses set 1
                createPasseWithShots(2L, 30, 29), // Team2 wins set 2
                createPasseWithShots(3L, 25, 25), // Team2 loses set 3
                createPasseWithShots(4L, 30, 29), // Team2 wins set 4
                createPasseWithShots(5L, 27, 28)  // Tie set 5
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH1_ID))
                .thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH1_ID))
                .thenReturn(team2Passes);

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
            passes.add(createPasseWithShots((long) i, 10, 9));
        }
        return passes;
    }

    private PasseDO createPasseWithShots(Long lfdnr, Integer pfeil1, Integer pfeil2) {
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
}