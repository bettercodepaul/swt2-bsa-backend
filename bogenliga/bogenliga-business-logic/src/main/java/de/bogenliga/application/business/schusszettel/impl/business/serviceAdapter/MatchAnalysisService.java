package de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * Service layer interface between schusszettel module and existing infrastructure.
 * 
 * <h2>CURRENT ROLE</h2>
 * This service provides the ONLY interface for schusszettel components to access external
 * match and pass data. It delegates to existing infrastructure while providing schusszettel-specific
 * analysis methods for match completion, opponent resolution, and pass tracking.
 * 
 * <h2>CURRENT RESPONSIBILITIES</h2>
 * <ul>
 *   <li>Match completion analysis using LigamatchBE database views</li>
 *   <li>Current pass number calculation with legacy data handling</li>
 *   <li>Opponent team resolution via LigamatchBE structure</li>
 *   <li>Match progression and tournament state analysis</li>
 * </ul>
 * 
 * <h2>INFRASTRUCTURE DELEGATION</h2>
 * <ul>
 *   <li>Delegates to MatchComponent for LigamatchBE queries</li>
 *   <li>Delegates to PasseComponent for pass data access</li>
 *   <li>Uses database-calculated scores instead of manual calculation</li>
 *   <li>Leverages existing business rules and constants</li>
 * </ul>
 * 
 * <h2>USAGE</h2>
 * <ul>
 *   <li>Called exclusively by SessionRuntime for external data access</li>
 *   <li>Used by AdminComponentImpl for session initialization</li>
 *   <li>State objects access data through StateContext, not directly</li>
 * </ul>
 * 
 * @author Marty Lauterbach - Infrastructure delegation implementation
 */
@Service
public class MatchAnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchAnalysisService.class);
    
    // WA Official archery rules constants
    public static final int MATCH_POINTS_TO_WIN = 6;
    public static final int MAX_SETS_PER_MATCH = 5;
    
    // Performance monitoring constants
    private static final long SLOW_QUERY_THRESHOLD_MS = 100L;
    private static final long WARNING_THRESHOLD_MS = 50L;
    
    // Query caching considerations (not implemented, but tracked)
    private static final int CACHE_SIZE_THRESHOLD = 100;
    private static final long CACHE_TTL_MS = 30000L; // 30 seconds

    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;

    @Autowired
    public MatchAnalysisService(MatchComponent matchComponent, PasseComponent passeComponent) {
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
    }
    
    /**
     * Performance monitoring for match analysis operations.
     */
    private void logPerformanceMetrics(String operation, long startTime, long matchId, Object result) {
        long duration = System.currentTimeMillis() - startTime;
        if (duration > 50) { // Log operations taking more than 50ms
            LOGGER.warn("Performance warning: {} for matchId {} took {}ms", operation, matchId, duration);
        } else {
            LOGGER.debug("Performance: {} for matchId {} took {}ms", operation, matchId, duration);
        }
    }
    
    /**
     * Enhanced match analysis with performance monitoring and caching considerations.
     */
    public MatchAnalysisResult analyzeMatchWithMetrics(long matchId, long team1Id, long team2Id) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Basic match analysis
            MatchAnalysisResult basicResult = analyzeMatch(matchId, team1Id, team2Id);
            
            // Performance analysis
            long analysisTime = System.currentTimeMillis() - startTime;
            boolean hasWarning = analysisTime > 100; // Warning threshold
            
            // Get additional metrics
            int totalPasses = getTotalPassCount(matchId, team1Id, team2Id);
            
            return new MatchAnalysisResult(
                basicResult.isComplete(),
                basicResult.getCurrentPasse(),
                basicResult.getTeam1Satzpunkte(),
                basicResult.getTeam2Satzpunkte(),
                basicResult.getStatusReason(),
                analysisTime,
                totalPasses,
                hasWarning
            );
            
        } catch (Exception e) {
            LOGGER.error("Error in enhanced match analysis for matchId {}: {}", matchId, e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Match analysis failed", e);
        }
    }
    
    /**
     * Get total pass count for performance analysis.
     */
    private int getTotalPassCount(long matchId, long team1Id, long team2Id) {
        try {
            List<PasseDO> team1Passes = passeComponent.findByMannschaftMatchId(team1Id, matchId);
            List<PasseDO> team2Passes = passeComponent.findByMannschaftMatchId(team2Id, matchId);
            return team1Passes.size() + team2Passes.size();
        } catch (Exception e) {
            LOGGER.debug("Could not get pass count for performance analysis: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Match analysis result using database-calculated data.
     * Enhanced with performance metrics and additional analysis data.
     */
    public static class MatchAnalysisResult {
        private final boolean isComplete;
        private final int currentPasse;
        private final long team1Satzpunkte;
        private final long team2Satzpunkte;
        private final String statusReason;
        private final long analysisTimeMs;
        private final int totalPasses;
        private final boolean hasPerformanceWarning;

        public MatchAnalysisResult(boolean isComplete, int currentPasse, 
                                 long team1Satzpunkte, long team2Satzpunkte, String statusReason) {
            this.isComplete = isComplete;
            this.currentPasse = currentPasse;
            this.team1Satzpunkte = team1Satzpunkte;
            this.team2Satzpunkte = team2Satzpunkte;
            this.statusReason = statusReason;
            this.analysisTimeMs = 0L;
            this.totalPasses = 0;
            this.hasPerformanceWarning = false;
        }
        
        // Enhanced constructor with performance metrics
        public MatchAnalysisResult(boolean isComplete, int currentPasse, 
                                 long team1Satzpunkte, long team2Satzpunkte, String statusReason,
                                 long analysisTimeMs, int totalPasses, boolean hasPerformanceWarning) {
            this.isComplete = isComplete;
            this.currentPasse = currentPasse;
            this.team1Satzpunkte = team1Satzpunkte;
            this.team2Satzpunkte = team2Satzpunkte;
            this.statusReason = statusReason;
            this.analysisTimeMs = analysisTimeMs;
            this.totalPasses = totalPasses;
            this.hasPerformanceWarning = hasPerformanceWarning;
        }

        // Getters
        public boolean isComplete() { return isComplete; }
        public int getCurrentPasse() { return currentPasse; }
        public long getTeam1Satzpunkte() { return team1Satzpunkte; }
        public long getTeam2Satzpunkte() { return team2Satzpunkte; }
        public String getStatusReason() { return statusReason; }
        public long getAnalysisTimeMs() { return analysisTimeMs; }
        public int getTotalPasses() { return totalPasses; }
        public boolean hasPerformanceWarning() { return hasPerformanceWarning; }
        
        // Compatibility methods
        public boolean isInProgress() { return !isComplete; }
        public boolean isNotStarted() { return currentPasse == 1 && team1Satzpunkte == 0 && team2Satzpunkte == 0; }
    }

    /**
     * Analyzes match using database-calculated scores and existing infrastructure.
     */
    public MatchAnalysisResult analyzeMatch(long matchId, long team1Id, long team2Id) {
        try {
            LOGGER.debug("Analyzing match {} using existing infrastructure", matchId);

            // Get database-calculated data from ligamatch view
            LigamatchBE ligamatch = matchComponent.getLigamatchById(matchId);
            if (ligamatch == null) {
                LOGGER.warn("Ligamatch {} not found", matchId);
                return new MatchAnalysisResult(false, 1, 0, 0, "Match not found");
            }

            // Use database-calculated scores
            long satzpunkte = ligamatch.getSatzpunkte() != null ? ligamatch.getSatzpunkte() : 0;
            
            // Use built-in opponent resolution from ligamatch view
            long opponentSatzpunkte = 0;
            if (ligamatch.getMatchIdGegner() != null) {
                LigamatchBE opponentMatch = matchComponent.getLigamatchById(ligamatch.getMatchIdGegner());
                if (opponentMatch != null && opponentMatch.getSatzpunkte() != null) {
                    opponentSatzpunkte = opponentMatch.getSatzpunkte();
                }
            }
            
            // Apply standard match completion rules
            boolean isComplete = satzpunkte >= MATCH_POINTS_TO_WIN || opponentSatzpunkte >= MATCH_POINTS_TO_WIN;
            
            // Calculate current pass number using infrastructure
            int currentPasse = calculateCurrentPasseUsingExistingInfrastructure(matchId, team1Id, team2Id);
            
            String reason = isComplete ? 
                String.format("Match complete: %d-%d Satzpunkte", satzpunkte, opponentSatzpunkte) :
                String.format("Match in progress: %d-%d Satzpunkte, passe %d", satzpunkte, opponentSatzpunkte, currentPasse);
            
            LOGGER.debug("Infrastructure-based analysis: {} ({})", reason, matchId);
            return new MatchAnalysisResult(isComplete, currentPasse, satzpunkte, opponentSatzpunkte, reason);

        } catch (Exception e) {
            LOGGER.error("Error in infrastructure-based analysis for match {}: {}", matchId, e.getMessage());
            return new MatchAnalysisResult(false, 1, 0, 0, "Error during analysis: " + e.getMessage());
        }
    }

    /**
     * Calculate current passe using existing infrastructure.
     * Delegates to established PasseComponent methods and existing patterns.
     * 
     * Find first incomplete passe instead of highest passe number.
     * This prevents confusion from pre-created empty passes.
     */
    private int calculateCurrentPasseUsingExistingInfrastructure(long matchId, long team1Id, long team2Id) {
        try {
            // Get passes for both teams
            List<PasseDO> team1Passes = passeComponent.findByMannschaftMatchId(team1Id, matchId);
            List<PasseDO> team2Passes = passeComponent.findByMannschaftMatchId(team2Id, matchId);
            
            if (team1Passes.isEmpty() && team2Passes.isEmpty()) {
                return 1; // Fresh match
            }
            
            // Find first incomplete passe (where team has < 3 shooters with actual scores)
            for (int passeNr = 1; passeNr <= MAX_SETS_PER_MATCH; passeNr++) {
                final int checkPasse = passeNr;
                
                // Count shooters with actual scores (not empty pre-created passes)
                long team1ShootersWithScores = team1Passes.stream()
                    .filter(p -> p.getPasseLfdnr() != null && p.getPasseLfdnr().intValue() == checkPasse)
                    .filter(this::hasActualScores) // Only count passes with real arrow data
                    .count();
                    
                long team2ShootersWithScores = team2Passes.stream()
                    .filter(p -> p.getPasseLfdnr() != null && p.getPasseLfdnr().intValue() == checkPasse)
                    .filter(this::hasActualScores) // Only count passes with real arrow data
                    .count();
                
                // If either team has fewer than 3 shooters with actual scores, this passé needs completion
                if (team1ShootersWithScores < 3 || team2ShootersWithScores < 3) {
                    LOGGER.debug("Passe {} incomplete: team1={} shooters, team2={} shooters with scores", 
                               passeNr, team1ShootersWithScores, team2ShootersWithScores);
                    return passeNr; // This passé needs completion
                }
            }
            
            LOGGER.debug("All passes 1-{} complete for match {}", MAX_SETS_PER_MATCH, matchId);
            return MAX_SETS_PER_MATCH + 1; // All passes complete (should trigger match end logic)
            
        } catch (Exception e) {
            LOGGER.warn("Error calculating current passe for match {}: {}", matchId, e.getMessage());
            return 1; // Safe fallback
        }
    }

    /**
     * Check if a pass has actual arrow scores vs being an empty pre-created pass.
     * Pre-created passes have null or 0 values for all arrows.
     */
    private boolean hasActualScores(PasseDO passe) {
        return (passe.getPfeil1() != null && passe.getPfeil1() > 0) ||
               (passe.getPfeil2() != null && passe.getPfeil2() > 0) ||
               (passe.getPfeil3() != null && passe.getPfeil3() > 0);
    }

    /**
     * Finds opponent team ID using LigamatchBE structure.
     */
    public long findOpponentTeamId(long matchId, long teamId) {
        try {
            LigamatchBE ligamatch = matchComponent.getLigamatchById(matchId);
            if (ligamatch == null) {
                throw new BusinessException(ErrorCode.INTERNAL_ERROR, 
                    "Ligamatch " + matchId + " not found when looking for opponent of team " + teamId);
            }
            
            // Check if this match belongs to the requested team
            if (Objects.equals(ligamatch.getMannschaftId(), teamId)) {
                // Get opponent from LigamatchBE - it already has opponent team info!
                if (ligamatch.getMatchIdGegner() != null) {
                    LigamatchBE opponentMatch = matchComponent.getLigamatchById(ligamatch.getMatchIdGegner());
                    if (opponentMatch != null) {
                        LOGGER.debug("Found opponent team {} for team {} using LigamatchBE", 
                                   opponentMatch.getMannschaftId(), teamId);
                        return opponentMatch.getMannschaftId();
                    }
                }
            } else {
                // The matchId might be the opponent's match - check if opponent points to us
                if (ligamatch.getMatchIdGegner() != null) {
                    LigamatchBE potentialTeamMatch = matchComponent.getLigamatchById(ligamatch.getMatchIdGegner());
                    if (potentialTeamMatch != null && Objects.equals(potentialTeamMatch.getMannschaftId(), teamId)) {
                        LOGGER.debug("Found opponent team {} for team {} (reverse lookup)", 
                                   ligamatch.getMannschaftId(), teamId);
                        return ligamatch.getMannschaftId();
                    }
                }
            }
            
            throw new BusinessException(ErrorCode.INTERNAL_ERROR,
                "No opponent found for team " + teamId + " in match " + matchId);
            
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                throw e;
            }
            throw new BusinessException(ErrorCode.INTERNAL_ERROR,
                "Error finding opponent for team " + teamId + " in match " + matchId + ": " + e.getMessage());
        }
    }

    /**
     * Checks match completion using database-calculated scores.
     */
    public boolean isMatchComplete(long matchId, long team1Id, long team2Id) {
        try {
            // Get database-calculated data from ligamatch view
            LigamatchBE ligamatch = matchComponent.getLigamatchById(matchId);
            if (ligamatch == null) {
                return false;
            }
            
            // Use database-calculated scores
            long satzpunkte = ligamatch.getSatzpunkte() != null ? ligamatch.getSatzpunkte() : 0;
            
            // Use built-in opponent resolution from ligamatch view
            long opponentSatzpunkte = 0;
            if (ligamatch.getMatchIdGegner() != null) {
                LigamatchBE opponentMatch = matchComponent.getLigamatchById(ligamatch.getMatchIdGegner());
                if (opponentMatch != null && opponentMatch.getSatzpunkte() != null) {
                    opponentSatzpunkte = opponentMatch.getSatzpunkte();
                }
            }
            
            // Apply standard completion rules
            boolean complete = satzpunkte >= MATCH_POINTS_TO_WIN || opponentSatzpunkte >= MATCH_POINTS_TO_WIN;
            
            LOGGER.debug("Infrastructure-based completion check: match={}, team={}pts, opponent={}pts, complete={}", 
                        matchId, satzpunkte, opponentSatzpunkte, complete);
            
            return complete;
            
        } catch (Exception e) {
            LOGGER.error("Error in infrastructure-based completion check for {}: {}", matchId, e.getMessage());
            return false;
        }
    }

    /**
     * Gets current pass number using infrastructure delegation.
     */
    public int getCurrentPasseNumber(long matchId, long team1Id, long team2Id) {
        try {
            return calculateCurrentPasseUsingExistingInfrastructure(matchId, team1Id, team2Id);
        } catch (Exception e) {
            LOGGER.error("Error in infrastructure-based passe calculation for match {} teams {} vs {}: {}", 
                        matchId, team1Id, team2Id, e.getMessage());
            return 1; // Safe fallback
        }
    }
    
    /**
     * Find current incomplete match for a team by following naechsteMatchId chain.
     * Uses LigamatchBE.naechsteMatchId as single source of truth for match progression.
     * Returns null if all matches are complete.
     */
    public LigamatchBE findCurrentIncompleteMatch(long wettkampfId, long teamId) {
        try {
            // Get all matches for this team to find the starting point
            List<LigamatchBE> teamMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfId).stream()
                    .filter(m -> Objects.equals(m.getMannschaftId(), teamId))
                    .toList();
            
            if (teamMatches.isEmpty()) {
                throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, 
                    "No matches found for team " + teamId + " in wettkampf " + wettkampfId);
            }
            
            // Find the first match (match with no previous match pointing to it)
            LigamatchBE firstMatch = findFirstMatchForTeam(teamMatches);
            
            // Follow the naechsteMatchId chain with cycle detection
            LigamatchBE currentMatch = firstMatch;
            Set<Long> visitedMatches = new HashSet<>();
            
            while (currentMatch != null) {
                // Cycle detection - prevent infinite loops
                if (visitedMatches.contains(currentMatch.getMatchId())) {
                    LOGGER.error("Detected cycle in naechsteMatchId chain for team {} at match {}", 
                               teamId, currentMatch.getMatchId());
                    throw new BusinessException(ErrorCode.INTERNAL_ERROR, 
                        "Cycle detected in match progression chain");
                }
                visitedMatches.add(currentMatch.getMatchId());
                
                // Check if this match is complete
                try {
                    long opponentId = findOpponentTeamId(currentMatch.getMatchId(), teamId);
                    boolean isComplete = isMatchComplete(currentMatch.getMatchId(), teamId, opponentId);
                    
                    if (!isComplete) {
                        // Found incomplete match - this is where the team should be
                        LOGGER.debug("Found incomplete match {} for team {} (following naechsteMatchId chain)", 
                                   currentMatch.getMatchId(), teamId);
                        return currentMatch;
                    }
                    
                    // This match is complete, follow the chain to the next match
                    LOGGER.debug("Match {} complete for team {}, following naechsteMatchId to next match", 
                               currentMatch.getMatchId(), teamId);
                    
                    if (currentMatch.getNaechsteMatchId() != null) {
                        currentMatch = matchComponent.getLigamatchById(currentMatch.getNaechsteMatchId());
                    } else {
                        // No more matches in the chain
                        currentMatch = null;
                    }
                    
                } catch (Exception e) {
                    LOGGER.warn("Error checking match {} completion for team {}: {}", 
                               currentMatch.getMatchId(), teamId, e.getMessage());
                    
                    // For initialization, we should be more tolerant of missing opponents
                    // Return this match as incomplete if we can't determine completion
                    LOGGER.info("Returning match {} as incomplete for team {} due to completion check error", 
                               currentMatch.getMatchId(), teamId);
                    return currentMatch;
                }
            }
            
            // All matches in the chain are complete
            LOGGER.info("All matches complete for team {} in wettkampf {} (followed naechsteMatchId chain)", 
                       teamId, wettkampfId);
            return null;
            
        } catch (Exception e) {
            LOGGER.error("Error finding current incomplete match for team {} in wettkampf {}: {}", 
                        teamId, wettkampfId, e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to find current match");
        }
    }
    
    /**
     * Find the first match for a team (match with no previous match pointing to it).
     */
    private LigamatchBE findFirstMatchForTeam(List<LigamatchBE> teamMatches) {
        // Find match that is not referenced by any other match's naechsteMatchId
        Set<Long> referencedMatches = teamMatches.stream()
                .map(LigamatchBE::getNaechsteMatchId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        for (LigamatchBE match : teamMatches) {
            if (!referencedMatches.contains(match.getMatchId())) {
                LOGGER.debug("Found first match {} for team {}", match.getMatchId(), match.getMannschaftId());
                return match;
            }
        }
        
        // Fallback: if no clear first match, use the one with lowest matchNr
        return teamMatches.stream()
                .min(Comparator.comparing(LigamatchBE::getMatchNr))
                .orElse(teamMatches.get(0));
    }
    
    /**
     * Find last match for a team (for completed tournaments).
     */
    public LigamatchBE findLastMatchForTeam(long wettkampfId, long teamId) {
        try {
            List<LigamatchBE> teamMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfId).stream()
                    .filter(m -> Objects.equals(m.getMannschaftId(), teamId))
                    .sorted((m1, m2) -> Long.compare(m1.getMatchNr(), m2.getMatchNr()))
                    .toList();
            
            if (teamMatches.isEmpty()) {
                throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    "No matches found for team " + teamId + " in wettkampf " + wettkampfId);
            }
            
            return teamMatches.get(teamMatches.size() - 1);
            
        } catch (Exception e) {
            LOGGER.error("Error finding last match for team {} in wettkampf {}: {}", 
                        teamId, wettkampfId, e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to find last match");
        }
    }

    /**
     * Tournament structure matrices replicated from SetzlisteComponentImpl.
     * These define the correct team progression through tournament brackets.
     */
    private enum TournamentStructure {
        TOURNAMENT_8_TEAM(new int[][] {
            {5, 4, 2, 7, 1, 8, 3, 6},  // Match 1
            {3, 5, 8, 4, 7, 1, 6, 2},  // Match 2
            {4, 7, 1, 6, 2, 5, 8, 3},  // Match 3
            {8, 2, 7, 3, 6, 4, 1, 5},  // Match 4
            {7, 6, 5, 8, 3, 2, 4, 1},  // Match 5
            {1, 3, 4, 2, 8, 6, 5, 7},  // Match 6
            {2, 1, 6, 5, 4, 3, 7, 8}   // Match 7
        }),
        
        TOURNAMENT_6_TEAM(new int[][] {
            {2, 5, 1, 6, 3, 4},        // Match 1
            {6, 3, 2, 4, 5, 1},        // Match 2
            {1, 2, 5, 3, 4, 6},        // Match 3
            {5, 4, 3, 1, 6, 2},        // Match 4
            {4, 1, 6, 5, 2, 3}         // Match 5
        }),
        
        TOURNAMENT_4_TEAM(new int[][] {
            {1, 4, 2, 3},              // Match 1
            {2, 4, 3, 1},              // Match 2
            {4, 3, 1, 2},              // Match 3
            {4, 1, 2, 3},              // Match 4
            {1, 3, 4, 2},              // Match 5
            {3, 4, 2, 1}               // Match 6
        });
        
        private final int[][] structure;
        TournamentStructure(int[][] structure) { this.structure = structure; }
        public int[][] getStructure() { return structure; }
    }

    /**
     * Find the correct next match for a team using tournament bracket progression.
     * This bypasses the flawed database naechsteMatchId calculation.
     */
    public LigamatchBE findCorrectNextMatch(long currentMatchId, long teamId) {
        try {
            // Get current match details
            LigamatchBE currentMatch = matchComponent.getLigamatchById(currentMatchId);
            if (currentMatch == null) {
                LOGGER.error("Current match {} not found", currentMatchId);
                return null;
            }
            
            // Determine tournament structure
            long wettkampfId = currentMatch.getWettkampfId();
            TournamentStructure tournament = determineTournamentStructure(wettkampfId);
            
            // Find team's ranking position in current match
            int teamRankingPosition = findTeamRankingPosition(currentMatch, teamId, wettkampfId);
            if (teamRankingPosition == -1) {
                LOGGER.error("Could not determine team ranking position for team {} in match {}", 
                            teamId, currentMatchId);
                return null;
            }
            
            // Calculate next match position using tournament matrix
            int currentMatchNumber = Math.toIntExact(currentMatch.getMatchNr());
            int nextMatchNumber = currentMatchNumber + 1;
            
            // Check if next match exists in tournament
            if (nextMatchNumber > tournament.getStructure().length) {
                LOGGER.debug("Team {} completed all matches (no match {} in tournament)", 
                            teamId, nextMatchNumber);
                return null; // Tournament complete
            }
            
            // Find team's position in next match using tournament matrix
            int nextTargetPosition = findTeamPositionInMatch(tournament, teamRankingPosition, nextMatchNumber - 1);
            if (nextTargetPosition == -1) {
                LOGGER.error("Could not determine next position for team ranking {} in match {}", 
                            teamRankingPosition, nextMatchNumber);
                return null;
            }
            
            // Query database for match at calculated position
            List<LigamatchBE> nextMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfId)
                    .stream()
                    .filter(m -> m.getMatchNr().intValue() == nextMatchNumber)
                    .filter(m -> m.getMatchScheibennummer().intValue() == nextTargetPosition)
                    .toList();
            
            if (nextMatches.size() != 1) {
                LOGGER.error("Expected exactly 1 match for wettkampf={}, matchNr={}, scheibe={}, found {}", 
                            wettkampfId, nextMatchNumber, nextTargetPosition, nextMatches.size());
                return null;
            }
            
            LigamatchBE nextMatch = nextMatches.get(0);
            LOGGER.debug("Tournament progression: team {} from match {} (target {}) to match {} (target {})", 
                        teamId, currentMatchNumber, currentMatch.getMatchScheibennummer(), 
                        nextMatchNumber, nextTargetPosition);
            
            return nextMatch;
            
        } catch (Exception e) {
            LOGGER.error("Error calculating correct next match for team {} from match {}: {}", 
                        teamId, currentMatchId, e.getMessage());
            return null;
        }
    }

    /**
     * Determine tournament structure based on team count.
     */
    private TournamentStructure determineTournamentStructure(long wettkampfId) {
        List<LigamatchBE> allMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfId);
        Set<Long> uniqueTeams = allMatches.stream()
                .map(LigamatchBE::getMannschaftId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        int teamCount = uniqueTeams.size();
        
        switch (teamCount) {
            case 8: return TournamentStructure.TOURNAMENT_8_TEAM;
            case 6: return TournamentStructure.TOURNAMENT_6_TEAM;
            case 4: return TournamentStructure.TOURNAMENT_4_TEAM;
            default:
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, 
                    "Unsupported tournament size: " + teamCount + " teams");
        }
    }

    /**
     * Find team's ranking position that was used in tournament matrix.
     * This reconstructs the ranking from the tournament structure.
     */
    private int findTeamRankingPosition(LigamatchBE currentMatch, long teamId, long wettkampfId) {
        try {
            // Get tournament structure for this wettkampf
            TournamentStructure tournament = determineTournamentStructure(wettkampfId);
            int currentMatchNumber = Math.toIntExact(currentMatch.getMatchNr());
            int currentTargetPosition = Math.toIntExact(currentMatch.getMatchScheibennummer());
            
            // Find ranking position by looking up in tournament matrix
            int[][] matrix = tournament.getStructure();
            if (currentMatchNumber <= matrix.length) {
                int[] matchTargets = matrix[currentMatchNumber - 1]; // Convert to 0-based
                if (currentTargetPosition <= matchTargets.length) {
                    int rankingPosition = matchTargets[currentTargetPosition - 1]; // Convert to 0-based
                    LOGGER.debug("Team {} at match {} target {} has ranking position {}", 
                               teamId, currentMatchNumber, currentTargetPosition, rankingPosition);
                    return rankingPosition;
                }
            }
            
            LOGGER.error("Could not find ranking position for team {} at match {} target {}", 
                        teamId, currentMatchNumber, currentTargetPosition);
            return -1;
            
        } catch (Exception e) {
            LOGGER.error("Error finding team ranking position: {}", e.getMessage());
            return -1;
        }
    }

    /**
     * Find team's position in specific match using tournament matrix.
     */
    private int findTeamPositionInMatch(TournamentStructure tournament, int teamRanking, int matchIndex) {
        int[][] matrix = tournament.getStructure();
        if (matchIndex >= matrix.length) {
            return -1; // Match doesn't exist
        }
        
        // Find where this team ranking appears in the specified match
        for (int position = 0; position < matrix[matchIndex].length; position++) {
            if (matrix[matchIndex][position] == teamRanking) {
                return position + 1; // Convert to 1-based scheibennummer
            }
        }
        
        return -1; // Team not found in this match
    }
}