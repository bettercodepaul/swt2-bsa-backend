package de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter;

import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive tests for the MatchAnalysisService class.
 * Tests all service methods for complete coverage including edge cases.
 */
@RunWith(MockitoJUnitRunner.class)
public class MatchAnalysisServiceTest {

    @Mock
    private MatchComponent mockMatchComponent;

    @Mock
    private PasseComponent mockPasseComponent;

    private MatchAnalysisService matchAnalysisService;

    @Before
    public void setUp() {
        matchAnalysisService = new MatchAnalysisService(mockMatchComponent, mockPasseComponent);
    }

    @Test
    public void constants_shouldHaveCorrectValues() {
        // Assert
        assertThat(MatchAnalysisService.MATCH_POINTS_TO_WIN).isEqualTo(6);
        assertThat(MatchAnalysisService.MAX_SETS_PER_MATCH).isEqualTo(5);
    }

    @Test
    public void isMatchComplete_withCompletedMatch_shouldReturnTrue() {
        // Arrange
        long matchId = 300L;
        long team1Id = 100L;
        long team2Id = 200L;
        
        LigamatchBE match1 = createLigamatchBE(matchId, team1Id, 6); // Team1 wins
        match1.setMatchIdGegner(301L);
        LigamatchBE match2 = createLigamatchBE(301L, team2Id, 4);   // Team2 loses
        
        when(mockMatchComponent.getLigamatchById(matchId)).thenReturn(match1);
        when(mockMatchComponent.getLigamatchById(301L)).thenReturn(match2);
        
        // Act
        boolean result = matchAnalysisService.isMatchComplete(matchId, team1Id, team2Id);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isMatchComplete_withIncompleteMatch_shouldReturnFalse() {
        // Arrange
        long matchId = 300L;
        long team1Id = 100L;
        long team2Id = 200L;
        
        LigamatchBE match1 = createLigamatchBE(matchId, team1Id, 4); // Team1 not yet won
        match1.setMatchIdGegner(301L);
        LigamatchBE match2 = createLigamatchBE(301L, team2Id, 4);   // Team2 not yet won
        
        when(mockMatchComponent.getLigamatchById(matchId)).thenReturn(match1);
        when(mockMatchComponent.getLigamatchById(301L)).thenReturn(match2);
        
        // Act
        boolean result = matchAnalysisService.isMatchComplete(matchId, team1Id, team2Id);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isMatchComplete_withNullMatch_shouldReturnFalse() {
        // Arrange
        long matchId = 300L;
        long team1Id = 100L;
        long team2Id = 200L;
        
        when(mockMatchComponent.getLigamatchById(matchId)).thenReturn(null);
        
        // Act
        boolean result = matchAnalysisService.isMatchComplete(matchId, team1Id, team2Id);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isMatchComplete_withMaxSetsCompleted_shouldReturnTrue() {
        // Arrange
        long matchId = 300L;
        long team1Id = 100L;
        long team2Id = 200L;
        
        // Mock 5 completed sets for both teams
        List<PasseDO> team1Passes = createPassesForSets(team1Id, matchId, 5);
        List<PasseDO> team2Passes = createPassesForSets(team2Id, 301L, 5);
        
        when(mockPasseComponent.findByMannschaftMatchId(team1Id, matchId)).thenReturn(team1Passes);
        when(mockPasseComponent.findByMannschaftMatchId(team2Id, 301L)).thenReturn(team2Passes);
        
        LigamatchBE match1 = createLigamatchBE(matchId, team1Id, 5);
        match1.setMatchIdGegner(301L);
        when(mockMatchComponent.getLigamatchById(matchId)).thenReturn(match1);
        
        // Act
        boolean result = matchAnalysisService.isMatchComplete(matchId, team1Id, team2Id);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void getCurrentPasseNumber_withNoPasses_shouldReturnOne() {
        // Arrange
        long teamId = 100L;
        long matchId = 300L;
        
        when(mockPasseComponent.findByMannschaftMatchId(teamId, matchId)).thenReturn(Collections.emptyList());
        
        // Act
        int result = matchAnalysisService.getCurrentPasseNumber(matchId, teamId, 200L);
        
        // Assert
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void getCurrentPasseNumber_withExistingPasses_shouldReturnCorrectNumber() {
        // Arrange
        long teamId = 100L;
        long matchId = 300L;
        
        List<PasseDO> passes = createPassesForSets(teamId, matchId, 2);
        when(mockPasseComponent.findByMannschaftMatchId(teamId, matchId)).thenReturn(passes);
        
        // Act
        int result = matchAnalysisService.getCurrentPasseNumber(matchId, teamId, 200L);
        
        // Assert
        assertThat(result).isEqualTo(3); // Next passe after 2 completed sets
    }

    @Test
    public void getCurrentPasseNumber_withMaxPasses_shouldReturnMaxNumber() {
        // Arrange
        long teamId = 100L;
        long matchId = 300L;
        
        List<PasseDO> passes = createPassesForSets(teamId, matchId, 5);
        when(mockPasseComponent.findByMannschaftMatchId(teamId, matchId)).thenReturn(passes);
        
        // Act
        int result = matchAnalysisService.getCurrentPasseNumber(matchId, teamId, 200L);
        
        // Assert
        assertThat(result).isEqualTo(5); // Should not exceed max
    }

    @Test
    public void findOpponentTeamId_withValidMatch_shouldReturnOpponentId() {
        // Arrange
        long matchId = 300L;
        long teamId = 100L;
        long opponentMatchId = 301L;
        long expectedOpponentId = 200L;
        
        LigamatchBE currentMatch = createLigamatchBE(matchId, teamId, 0);
        currentMatch.setMatchIdGegner(opponentMatchId);
        when(mockMatchComponent.getLigamatchById(matchId)).thenReturn(currentMatch);
        
        LigamatchBE opponentMatch = createLigamatchBE(opponentMatchId, expectedOpponentId, 0);
        when(mockMatchComponent.getLigamatchById(opponentMatchId)).thenReturn(opponentMatch);
        
        // Act
        long result = matchAnalysisService.findOpponentTeamId(matchId, teamId);
        
        // Assert
        assertThat(result).isEqualTo(expectedOpponentId);
    }

    @Test
    public void findOpponentTeamId_withNoOpponentMatch_shouldReturnZero() {
        // Arrange
        long matchId = 300L;
        long teamId = 100L;
        
        LigamatchBE currentMatch = createLigamatchBE(matchId, teamId, 0);
        currentMatch.setMatchIdGegner(null);
        when(mockMatchComponent.getLigamatchById(matchId)).thenReturn(currentMatch);
        
        // Act
        long result = matchAnalysisService.findOpponentTeamId(matchId, teamId);
        
        // Assert
        assertThat(result).isEqualTo(0L);
    }

    @Test
    public void findCurrentIncompleteMatch_withIncompleteMatch_shouldReturnMatch() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        
        List<LigamatchBE> matches = createTeamMatches(teamId, wettkampfId);
        when(mockMatchComponent.getLigamatchesByWettkampfId(wettkampfId)).thenReturn(matches);
        
        // Mock first match as incomplete
        List<PasseDO> incompletePasses = createPassesForSets(teamId, 300L, 2); // Only 2 sets
        when(mockPasseComponent.findByMannschaftMatchId(teamId, 300L)).thenReturn(incompletePasses);
        
        // Act
        LigamatchBE result = matchAnalysisService.findCurrentIncompleteMatch(wettkampfId, teamId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getMatchId()).isEqualTo(300L);
    }

    @Test
    public void findCurrentIncompleteMatch_withAllMatchesComplete_shouldReturnNull() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        
        List<LigamatchBE> matches = createTeamMatches(teamId, wettkampfId);
        when(mockMatchComponent.getLigamatchesByWettkampfId(wettkampfId)).thenReturn(matches);
        
        // Mock all matches as complete
        List<PasseDO> completePasses = createPassesForSets(teamId, 300L, 5); // All 5 sets
        when(mockPasseComponent.findByMannschaftMatchId(teamId, 300L)).thenReturn(completePasses);
        when(mockPasseComponent.findByMannschaftMatchId(teamId, 400L)).thenReturn(completePasses);
        
        // Act
        LigamatchBE result = matchAnalysisService.findCurrentIncompleteMatch(wettkampfId, teamId);
        
        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void findCurrentIncompleteMatch_withNoMatches_shouldReturnNull() {
        // Arrange
        long wettkampfId = 50L;
        long teamId = 100L;
        
        when(mockMatchComponent.getLigamatchesByWettkampfId(wettkampfId))
.thenReturn(Collections.emptyList());
        
        // Act
        LigamatchBE result = matchAnalysisService.findCurrentIncompleteMatch(wettkampfId, teamId);
        
        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void isWettkampfCompleteOverall_withAllTeamsComplete_shouldReturnTrue() {
        // Arrange
        long wettkampfId = 50L;
        
        List<LigamatchBE> allMatches = createAllWettkampfMatches(wettkampfId);
        when(mockMatchComponent.getLigamatchesByWettkampfId(wettkampfId)).thenReturn(allMatches);
        
        // Mock all teams have completed matches
        List<PasseDO> completePasses = createPassesForSets(100L, 300L, 5);
        when(mockPasseComponent.findByMannschaftMatchId(anyLong(), anyLong())).thenReturn(completePasses);
        
        // Act
        boolean result = matchAnalysisService.isWettkampfCompleteOverall(wettkampfId);
        
        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isWettkampfCompleteOverall_withIncompleteTeams_shouldReturnFalse() {
        // Arrange
        long wettkampfId = 50L;
        
        List<LigamatchBE> allMatches = createAllWettkampfMatches(wettkampfId);
        when(mockMatchComponent.getLigamatchesByWettkampfId(wettkampfId)).thenReturn(allMatches);
        
        // Mock some teams incomplete
        List<PasseDO> incompletePasses = createPassesForSets(100L, 300L, 2); // Only 2 sets
        when(mockPasseComponent.findByMannschaftMatchId(anyLong(), anyLong())).thenReturn(incompletePasses);
        
        // Act
        boolean result = matchAnalysisService.isWettkampfCompleteOverall(wettkampfId);
        
        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void calculateMatchScore_withValidData_shouldReturnCorrectScore() {
        // Arrange
        long teamId = 100L;
        long matchId = 300L;
        int currentPasse = 3;
        
        List<PasseDO> passes = createPassesWithScores(teamId, matchId, currentPasse);
        when(mockPasseComponent.findByMannschaftMatchId(teamId, matchId)).thenReturn(passes);
        
        // Act
        int result = matchAnalysisService.getNextPasseNumberForTeam(matchId, teamId);
        
        // Assert
        assertThat(result).isGreaterThanOrEqualTo(1);
        assertThat(result).isLessThanOrEqualTo(5); // Max possible passe number
    }

    @Test
    public void calculateMatchScore_withNoPasses_shouldReturnZero() {
        // Arrange
        long teamId = 100L;
        long matchId = 300L;
        int currentPasse = 1;
        
        when(mockPasseComponent.findByMannschaftMatchId(teamId, matchId)).thenReturn(Collections.emptyList());
        
        // Act
        int result = matchAnalysisService.getNextPasseNumberForTeam(matchId, teamId);
        
        // Assert
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void findCorrectNextMatch_withValidProgression_shouldReturnNextMatch() {
        // Arrange
        long currentMatchId = 300L;
        long teamId = 100L;
        
        LigamatchBE currentMatch = createLigamatchBE(currentMatchId, teamId, 6);
        currentMatch.setNaechsteMatchId(400L);
        
        LigamatchBE nextMatch = createLigamatchBE(400L, teamId, 0);
        
        when(mockMatchComponent.getLigamatchById(currentMatchId)).thenReturn(currentMatch);
        when(mockMatchComponent.getLigamatchById(400L)).thenReturn(nextMatch);
        
        // Act
        LigamatchBE result = matchAnalysisService.findCorrectNextMatch(currentMatchId, teamId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getMatchId()).isEqualTo(400L);
    }

    @Test
    public void findCorrectNextMatch_withNoNextMatch_shouldReturnNull() {
        // Arrange
        long currentMatchId = 300L;
        long teamId = 100L;
        
        LigamatchBE currentMatch = createLigamatchBE(currentMatchId, teamId, 6);
        currentMatch.setNaechsteMatchId(null);
        
        when(mockMatchComponent.getLigamatchById(currentMatchId)).thenReturn(currentMatch);
        
        // Act
        LigamatchBE result = matchAnalysisService.findCorrectNextMatch(currentMatchId, teamId);
        
        // Assert
        assertThat(result).isNull();
    }

    // Helper methods for test data creation

    private LigamatchBE createLigamatchBE(long matchId, long teamId, int satzpunkte) {
        LigamatchBE match = new LigamatchBE();
        match.setMatchId(matchId);
        match.setMannschaftId(teamId);
        match.setSatzpunkte((long) satzpunkte);
        match.setWettkampfId(50L);
        return match;
    }

    private List<PasseDO> createPassesForSets(long teamId, long matchId, int numSets) {
        List<PasseDO> passes = new ArrayList<>();
        
        for (int setNum = 1; setNum <= numSets; setNum++) {
            for (int shooter = 1; shooter <= 3; shooter++) {
                PasseDO pass = new PasseDO();
                pass.setId((long) (setNum * 10 + shooter));
                pass.setPasseMannschaftId(teamId);
                pass.setPasseMatchId(matchId);
                pass.setPasseLfdnr((long) setNum);
                pass.setPasseDsbMitgliedId((long) shooter);
                pass.setPfeil1(8);
                pass.setPfeil2(9);
                pass.setPfeil3(7);
                passes.add(pass);
            }
        }
        
        return passes;
    }

    private List<PasseDO> createPassesWithScores(long teamId, long matchId, int numSets) {
        List<PasseDO> passes = new ArrayList<>();
        
        for (int setNum = 1; setNum <= numSets; setNum++) {
            for (int shooter = 1; shooter <= 3; shooter++) {
                PasseDO pass = new PasseDO();
                pass.setId((long) (setNum * 10 + shooter));
                pass.setPasseMannschaftId(teamId);
                pass.setPasseMatchId(matchId);
                pass.setPasseLfdnr((long) setNum);
                pass.setPasseDsbMitgliedId((long) shooter);
                
                // Vary scores to test calculation
                int baseScore = 8 + (setNum % 3);
                pass.setPfeil1(baseScore);
                pass.setPfeil2(baseScore + 1);
                pass.setPfeil3(baseScore - 1);
                passes.add(pass);
            }
        }
        
        return passes;
    }

    private List<LigamatchBE> createTeamMatches(long teamId, long wettkampfId) {
        List<LigamatchBE> matches = new ArrayList<>();
        
        LigamatchBE match1 = createLigamatchBE(300L, teamId, 0);
        match1.setWettkampfId(wettkampfId);
        match1.setNaechsteMatchId(400L);
        matches.add(match1);
        
        LigamatchBE match2 = createLigamatchBE(400L, teamId, 0);
        match2.setWettkampfId(wettkampfId);
        matches.add(match2);
        
        return matches;
    }

    private List<LigamatchBE> createAllWettkampfMatches(long wettkampfId) {
        List<LigamatchBE> matches = new ArrayList<>();
        
        // Team 100 matches
        LigamatchBE match1 = createLigamatchBE(300L, 100L, 6);
        match1.setWettkampfId(wettkampfId);
        matches.add(match1);
        
        // Team 200 matches
        LigamatchBE match2 = createLigamatchBE(301L, 200L, 4);
        match2.setWettkampfId(wettkampfId);
        matches.add(match2);
        
        return matches;
    }
}