package de.bogenliga.application.business.schusszettel.impl.business;

import org.assertj.core.api.Assertions;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

/**
 * Comprehensive test class for MatchAnalysisService
 * Tests all match analysis functionality including completion detection,
 * set point calculation, and archery rules validation.
 *
 * @author Test Generator
 */
public class MatchAnalysisServiceTest {

    private static final Long MATCH_ID = 100L;
    private static final Long TEAM1_ID = 10L;
    private static final Long TEAM2_ID = 20L;

    @Mock
    private MatchComponent matchComponent;
    @Mock
    private PasseComponent passeComponent;

    private MatchAnalysisService underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new MatchAnalysisService(matchComponent, passeComponent);
    }

    @Test
    public void testAnalyzeMatch_NotStarted() {
        // Arrange
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID))
                .thenReturn(Collections.emptyList());
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID))
                .thenReturn(Collections.emptyList());

        // Act
        MatchAnalysisService.MatchAnalysisResult result = 
            underTest.analyzeMatch(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result.getStatus()).isEqualTo(MatchAnalysisService.MatchStatus.NOT_STARTED);
        Assertions.assertThat(result.getCompletedSets()).isEqualTo(0);
        Assertions.assertThat(result.getCurrentPasse()).isEqualTo(1);
        Assertions.assertThat(result.getTeam1Satzpunkte()).isEqualTo(0);
        Assertions.assertThat(result.getTeam2Satzpunkte()).isEqualTo(0);
        Assertions.assertThat(result.getSatzErgebnisse()).isEmpty();
        Assertions.assertThat(result.getStatusReason()).isEqualTo("No sets completed yet");
        Assertions.assertThat(result.isNotStarted()).isTrue();
        Assertions.assertThat(result.isInProgress()).isFalse();
        Assertions.assertThat(result.isComplete()).isFalse();
    }

    @Test
    public void testAnalyzeMatch_InProgress_PartialSets() {
        // Arrange - Team1 has 2 complete shooters, Team2 has 1 complete shooter in set 1
        List<PasseDO> team1Passes = Arrays.asList(
            createPasse(TEAM1_ID, 1L, 101L, 10, 9, 8),  // Shooter 1, Set 1
            createPasse(TEAM1_ID, 1L, 102L, 9, 8, 7)    // Shooter 2, Set 1
        );
        List<PasseDO> team2Passes = Arrays.asList(
            createPasse(TEAM2_ID, 1L, 201L, 8, 7, 6)    // Shooter 1, Set 1
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID)).thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID)).thenReturn(team2Passes);

        // Act
        MatchAnalysisService.MatchAnalysisResult result = 
            underTest.analyzeMatch(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result.getStatus()).isEqualTo(MatchAnalysisService.MatchStatus.IN_PROGRESS);
        Assertions.assertThat(result.getCompletedSets()).isEqualTo(0); // No complete sets yet
        Assertions.assertThat(result.getCurrentPasse()).isEqualTo(1);
        Assertions.assertThat(result.getSatzErgebnisse()).isEmpty(); // No complete sets
        Assertions.assertThat(result.getStatusReason()).contains("Set 1 partially completed");
        Assertions.assertThat(result.isInProgress()).isTrue();
    }

    @Test
    public void testAnalyzeMatch_InProgress_CompleteSets() {
        // Arrange - Both teams have complete shooters for set 1
        List<PasseDO> team1Passes = Arrays.asList(
            createPasse(TEAM1_ID, 1L, 101L, 10, 9, 8),  // 27 points
            createPasse(TEAM1_ID, 1L, 102L, 9, 8, 7),   // 24 points  
            createPasse(TEAM1_ID, 1L, 103L, 8, 7, 6)    // 21 points = 72 total
        );
        List<PasseDO> team2Passes = Arrays.asList(
            createPasse(TEAM2_ID, 1L, 201L, 8, 7, 6),   // 21 points
            createPasse(TEAM2_ID, 1L, 202L, 7, 6, 5),   // 18 points
            createPasse(TEAM2_ID, 1L, 203L, 6, 5, 4)    // 15 points = 54 total
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID)).thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID)).thenReturn(team2Passes);

        // Act
        MatchAnalysisService.MatchAnalysisResult result = 
            underTest.analyzeMatch(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result.getStatus()).isEqualTo(MatchAnalysisService.MatchStatus.IN_PROGRESS);
        Assertions.assertThat(result.getCompletedSets()).isEqualTo(1);
        Assertions.assertThat(result.getCurrentPasse()).isEqualTo(2);
        Assertions.assertThat(result.getTeam1Satzpunkte()).isEqualTo(2); // Won set 1
        Assertions.assertThat(result.getTeam2Satzpunkte()).isEqualTo(0); // Lost set 1
        Assertions.assertThat(result.getSatzErgebnisse()).hasSize(1);
        Assertions.assertThat(result.getSatzErgebnisse().get(0).getTeam1Punkte()).isEqualTo(72);
        Assertions.assertThat(result.getSatzErgebnisse().get(0).getTeam2Punkte()).isEqualTo(54);
        Assertions.assertThat(result.getStatusReason()).contains("Ready for set 2");
    }

    @Test
    public void testAnalyzeMatch_Complete_6Satzpunkte() {
        // Arrange - Team1 wins 3 sets (6 Satzpunkte)
        List<PasseDO> team1Passes = createWinningPasses(TEAM1_ID, 3);
        List<PasseDO> team2Passes = createLosingPasses(TEAM2_ID, 3);

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID)).thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID)).thenReturn(team2Passes);

        // Act
        MatchAnalysisService.MatchAnalysisResult result = 
            underTest.analyzeMatch(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result.getStatus()).isEqualTo(MatchAnalysisService.MatchStatus.COMPLETED);
        Assertions.assertThat(result.getCompletedSets()).isEqualTo(3);
        Assertions.assertThat(result.getCurrentPasse()).isEqualTo(3); // Don't advance past completion
        Assertions.assertThat(result.getTeam1Satzpunkte()).isEqualTo(6); // 3 sets won
        Assertions.assertThat(result.getTeam2Satzpunkte()).isEqualTo(0); // 0 sets won
        Assertions.assertThat(result.getSatzErgebnisse()).hasSize(3);
        Assertions.assertThat(result.getStatusReason()).contains("Match completed: Team scores 6-0 Satzpunkte");
        Assertions.assertThat(result.isComplete()).isTrue();
    }

    @Test
    public void testAnalyzeMatch_Complete_5Sets() {
        // Arrange - Match goes to 5 sets (2-2-1 score = 5-5)
        List<PasseDO> team1Passes = createTiedMatchPasses(TEAM1_ID, 5);
        List<PasseDO> team2Passes = createTiedMatchPasses(TEAM2_ID, 5);

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID)).thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID)).thenReturn(team2Passes);

        // Act
        MatchAnalysisService.MatchAnalysisResult result = 
            underTest.analyzeMatch(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result.getStatus()).isEqualTo(MatchAnalysisService.MatchStatus.COMPLETED);
        Assertions.assertThat(result.getCompletedSets()).isEqualTo(5);
        Assertions.assertThat(result.getCurrentPasse()).isEqualTo(5);
        Assertions.assertThat(result.getTeam1Satzpunkte()).isEqualTo(5); // 2 wins + 1 tie
        Assertions.assertThat(result.getTeam2Satzpunkte()).isEqualTo(5); // 2 wins + 1 tie
        Assertions.assertThat(result.getSatzErgebnisse()).hasSize(5);
        Assertions.assertThat(result.getStatusReason()).contains("Maximum 5 sets reached (5-5)");
        Assertions.assertThat(result.isComplete()).isTrue();
    }

    @Test
    public void testAnalyzeMatch_TiedSets() {
        // Arrange - One tied set
        List<PasseDO> team1Passes = Arrays.asList(
            createPasse(TEAM1_ID, 1L, 101L, 10, 9, 8),  // 27 points
            createPasse(TEAM1_ID, 1L, 102L, 9, 8, 7),   // 24 points  
            createPasse(TEAM1_ID, 1L, 103L, 8, 7, 6)    // 21 points = 72 total
        );
        List<PasseDO> team2Passes = Arrays.asList(
            createPasse(TEAM2_ID, 1L, 201L, 10, 9, 8),  // 27 points
            createPasse(TEAM2_ID, 1L, 202L, 9, 8, 7),   // 24 points
            createPasse(TEAM2_ID, 1L, 203L, 8, 7, 6)    // 21 points = 72 total (TIE!)
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID)).thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID)).thenReturn(team2Passes);

        // Act
        MatchAnalysisService.MatchAnalysisResult result = 
            underTest.analyzeMatch(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result.getCompletedSets()).isEqualTo(1);
        Assertions.assertThat(result.getTeam1Satzpunkte()).isEqualTo(1); // Tied set = 1 point each
        Assertions.assertThat(result.getTeam2Satzpunkte()).isEqualTo(1);
        Assertions.assertThat(result.getSatzErgebnisse().get(0).getTeam1Punkte()).isEqualTo(72);
        Assertions.assertThat(result.getSatzErgebnisse().get(0).getTeam2Punkte()).isEqualTo(72);
    }

    @Test
    public void testAnalyzeMatch_InvalidArrows() {
        // Arrange - Passes with invalid arrow values
        List<PasseDO> team1Passes = Arrays.asList(
            createPasse(TEAM1_ID, 1L, 101L, 15, -5, 8),  // Invalid values > 10 and < 0
            createPasse(TEAM1_ID, 1L, 102L, 9, 8, 7),    // Valid values
            createPasse(TEAM1_ID, 1L, 103L, null, null, null) // Null values
        );
        List<PasseDO> team2Passes = Arrays.asList(
            createPasse(TEAM2_ID, 1L, 201L, 8, 7, 6),    // Valid values
            createPasse(TEAM2_ID, 1L, 202L, 7, 6, 5),    // Valid values
            createPasse(TEAM2_ID, 1L, 203L, 6, 5, 4)     // Valid values
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID)).thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID)).thenReturn(team2Passes);

        // Act
        MatchAnalysisService.MatchAnalysisResult result = 
            underTest.analyzeMatch(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert - Invalid arrows should be treated as 0
        Assertions.assertThat(result.getCompletedSets()).isEqualTo(1);
        Assertions.assertThat(result.getSatzErgebnisse().get(0).getTeam1Punkte()).isEqualTo(32); // (0+0+8) + (9+8+7) + (0+0+0) = 8 + 24 + 0 = 32
        Assertions.assertThat(result.getSatzErgebnisse().get(0).getTeam2Punkte()).isEqualTo(54); // (8+7+6) + (7+6+5) + (6+5+4) = 21 + 18 + 15 = 54
        Assertions.assertThat(result.getTeam2Satzpunkte()).isEqualTo(2); // Team2 wins
    }

    @Test
    public void testAnalyzeMatch_IncompleteTeam() {
        // Arrange - Team1 has only 2 shooters, Team2 has 3
        List<PasseDO> team1Passes = Arrays.asList(
            createPasse(TEAM1_ID, 1L, 101L, 10, 9, 8),  // Shooter 1
            createPasse(TEAM1_ID, 1L, 102L, 9, 8, 7)    // Shooter 2 (missing shooter 3)
        );
        List<PasseDO> team2Passes = Arrays.asList(
            createPasse(TEAM2_ID, 1L, 201L, 8, 7, 6),   // Shooter 1
            createPasse(TEAM2_ID, 1L, 202L, 7, 6, 5),   // Shooter 2
            createPasse(TEAM2_ID, 1L, 203L, 6, 5, 4)    // Shooter 3
        );

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID)).thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID)).thenReturn(team2Passes);

        // Act
        MatchAnalysisService.MatchAnalysisResult result = 
            underTest.analyzeMatch(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert - Set is not complete because Team1 missing shooter
        Assertions.assertThat(result.getCompletedSets()).isEqualTo(0);
        Assertions.assertThat(result.getStatus()).isEqualTo(MatchAnalysisService.MatchStatus.IN_PROGRESS);
        Assertions.assertThat(result.getSatzErgebnisse()).isEmpty();
    }

    @Test
    public void testAnalyzeMatch_Exception() {
        // Arrange - Mock to throw exception
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        MatchAnalysisService.MatchAnalysisResult result = 
            underTest.analyzeMatch(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result.getStatus()).isEqualTo(MatchAnalysisService.MatchStatus.INVALID);
        Assertions.assertThat(result.getStatusReason()).contains("Error during analysis: Database error");
        Assertions.assertThat(result.getCompletedSets()).isEqualTo(0);
        Assertions.assertThat(result.getCurrentPasse()).isEqualTo(1);
    }

    @Test
    public void testBuildSatzErgebnisse_NoData() {
        // Arrange
        List<PasseDO> team1Passes = Collections.emptyList();
        List<PasseDO> team2Passes = Collections.emptyList();

        // Act
        List<SatzErgebnisDO> result = underTest.buildSatzErgebnisse(team1Passes, team2Passes, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testBuildSatzErgebnisse_MultipleSets() {
        // Arrange - 2 complete sets
        List<PasseDO> team1Passes = Arrays.asList(
            // Set 1
            createPasse(TEAM1_ID, 1L, 101L, 10, 9, 8),
            createPasse(TEAM1_ID, 1L, 102L, 9, 8, 7),
            createPasse(TEAM1_ID, 1L, 103L, 8, 7, 6),
            // Set 2
            createPasse(TEAM1_ID, 2L, 101L, 9, 8, 7),
            createPasse(TEAM1_ID, 2L, 102L, 8, 7, 6),
            createPasse(TEAM1_ID, 2L, 103L, 7, 6, 5)
        );
        List<PasseDO> team2Passes = Arrays.asList(
            // Set 1
            createPasse(TEAM2_ID, 1L, 201L, 8, 7, 6),
            createPasse(TEAM2_ID, 1L, 202L, 7, 6, 5),
            createPasse(TEAM2_ID, 1L, 203L, 6, 5, 4),
            // Set 2  
            createPasse(TEAM2_ID, 2L, 201L, 7, 6, 5),
            createPasse(TEAM2_ID, 2L, 202L, 6, 5, 4),
            createPasse(TEAM2_ID, 2L, 203L, 5, 4, 3)
        );

        // Act
        List<SatzErgebnisDO> result = underTest.buildSatzErgebnisse(team1Passes, team2Passes, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get(0).getSatzNr()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getTeam1Punkte()).isEqualTo(72); // 27+24+21
        Assertions.assertThat(result.get(0).getTeam2Punkte()).isEqualTo(54); // 21+18+15
        Assertions.assertThat(result.get(1).getSatzNr()).isEqualTo(2);
        Assertions.assertThat(result.get(1).getTeam1Punkte()).isEqualTo(63); // 24+21+18 = 63 (corrected)
        Assertions.assertThat(result.get(1).getTeam2Punkte()).isEqualTo(45); // 18+15+12
    }

    @Test
    public void testCalculateSetPoints_ValidData() {
        // Arrange
        List<PasseDO> setPasses = Arrays.asList(
            createPasse(TEAM1_ID, 1L, 101L, 10, 9, 8),  // 27 points
            createPasse(TEAM1_ID, 1L, 102L, 9, 8, 7),   // 24 points
            createPasse(TEAM2_ID, 1L, 201L, 8, 7, 6)    // 21 points (different team)
        );

        // Act
        int result = underTest.calculateSetPoints(setPasses, TEAM1_ID);

        // Assert
        Assertions.assertThat(result).isEqualTo(51); // 27 + 24 (only Team1's passes)
    }

    @Test
    public void testCalculateSetPoints_EmptyData() {
        // Act
        int result = underTest.calculateSetPoints(Collections.emptyList(), TEAM1_ID);

        // Assert
        Assertions.assertThat(result).isEqualTo(0);
    }

    @Test
    public void testCalculateSetPoints_NullData() {
        // Act
        int result = underTest.calculateSetPoints(null, TEAM1_ID);

        // Assert
        Assertions.assertThat(result).isEqualTo(0);
    }

    @Test
    public void testIsMatchComplete_True() {
        // Arrange - Setup 3 winning sets for Team1
        List<PasseDO> team1Passes = createWinningPasses(TEAM1_ID, 3);
        List<PasseDO> team2Passes = createLosingPasses(TEAM2_ID, 3);

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID)).thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID)).thenReturn(team2Passes);

        // Act
        boolean result = underTest.isMatchComplete(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void testIsMatchComplete_False() {
        // Arrange - Setup incomplete match
        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID)).thenReturn(Collections.emptyList());
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID)).thenReturn(Collections.emptyList());

        // Act
        boolean result = underTest.isMatchComplete(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void testGetCurrentPasseNumber() {
        // Arrange - Setup 2 complete sets
        List<PasseDO> team1Passes = createWinningPasses(TEAM1_ID, 2);
        List<PasseDO> team2Passes = createLosingPasses(TEAM2_ID, 2);

        when(passeComponent.findByMannschaftMatchId(TEAM1_ID, MATCH_ID)).thenReturn(team1Passes);
        when(passeComponent.findByMannschaftMatchId(TEAM2_ID, MATCH_ID)).thenReturn(team2Passes);

        // Act
        int result = underTest.getCurrentPasseNumber(MATCH_ID, TEAM1_ID, TEAM2_ID);

        // Assert
        Assertions.assertThat(result).isEqualTo(3); // Next passe after 2 completed sets
    }

    // Helper methods

    private PasseDO createPasse(Long teamId, Long setNr, Long shooterId, Integer pfeil1, Integer pfeil2, Integer pfeil3) {
        PasseDO passe = new PasseDO();
        passe.setId(shooterId);
        passe.setPasseLfdnr(setNr);
        passe.setPasseMannschaftId(teamId);
        passe.setPasseMatchId(MATCH_ID);
        passe.setPasseDsbMitgliedId(shooterId);
        passe.setPfeil1(pfeil1);
        passe.setPfeil2(pfeil2);
        passe.setPfeil3(pfeil3);
        return passe;
    }

    private List<PasseDO> createWinningPasses(Long teamId, int sets) {
        List<PasseDO> passes = new ArrayList<>();
        for (int set = 1; set <= sets; set++) {
            for (int shooter = 1; shooter <= 3; shooter++) {
                Long shooterId = teamId + shooter;
                passes.add(createPasse(teamId, (long) set, shooterId, 10, 10, 10)); // High scores
            }
        }
        return passes;
    }

    private List<PasseDO> createLosingPasses(Long teamId, int sets) {
        List<PasseDO> passes = new ArrayList<>();
        for (int set = 1; set <= sets; set++) {
            for (int shooter = 1; shooter <= 3; shooter++) {
                Long shooterId = teamId + shooter;
                passes.add(createPasse(teamId, (long) set, shooterId, 5, 5, 5)); // Low scores
            }
        }
        return passes;
    }

    private List<PasseDO> createTiedMatchPasses(Long teamId, int sets) {
        List<PasseDO> passes = new ArrayList<>();
        for (int set = 1; set <= sets; set++) {
            for (int shooter = 1; shooter <= 3; shooter++) {
                Long shooterId = teamId + shooter;
                if (set == 5) {
                    // Set 5 is tied
                    passes.add(createPasse(teamId, (long) set, shooterId, 8, 8, 8));
                } else if ((set % 2) == (teamId.intValue() % 2)) {
                    // Alternating wins
                    passes.add(createPasse(teamId, (long) set, shooterId, 10, 10, 10));
                } else {
                    passes.add(createPasse(teamId, (long) set, shooterId, 5, 5, 5));
                }
            }
        }
        return passes;
    }
}