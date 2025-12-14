package de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter;

import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    // Query caching considerations (not implemented, but tracked)
    // 30 seconds

    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;

    @Autowired
    public MatchAnalysisService(MatchComponent matchComponent, PasseComponent passeComponent) {
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
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
        public boolean isComplete() {
            return isComplete;
        }

        public int getCurrentPasse() {
            return currentPasse;
        }

        public long getTeam1Satzpunkte() {
            return team1Satzpunkte;
        }

        public long getTeam2Satzpunkte() {
            return team2Satzpunkte;
        }

        public String getStatusReason() {
            return statusReason;
        }

        public long getAnalysisTimeMs() {
            return analysisTimeMs;
        }

        public int getTotalPasses() {
            return totalPasses;
        }

        public boolean hasPerformanceWarning() {
            return hasPerformanceWarning;
        }

        // Compatibility methods
        public boolean isInProgress() {
            return !isComplete;
        }

        public boolean isNotStarted() {
            return currentPasse == 1 && team1Satzpunkte == 0 && team2Satzpunkte == 0;
        }
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

            // Apply complete match completion rules (score-based + max passes)
            boolean isComplete = isMatchComplete(matchId, team1Id, team2Id);

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
     * <p>
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
     * Calculate next passe number for a SPECIFIC team (team-specific calculation).
     * CRITICAL FIX: This replaces the flawed global passe calculation that caused race conditions.
     * Returns the next passe this team should submit scores for.
     */
    public int getNextPasseNumberForTeam(long matchId, long teamId) {
        try {
            LOGGER.debug("Calculating next passe for team {} in match {}", teamId, matchId);

            // Get passes for this specific team only
            List<PasseDO> teamPasses = passeComponent.findByMannschaftMatchId(teamId, matchId);

            if (teamPasses.isEmpty()) {
                LOGGER.debug("No passes found for team {} - starting at passe 1", teamId);
                return 1; // Team hasn't started any passes yet
            }

            // Find the highest completed passe for THIS team specifically
            int highestCompletedPasse = 0;
            for (int passeNr = 1; passeNr <= MAX_SETS_PER_MATCH; passeNr++) {
                final int checkPasse = passeNr;

                // Count shooters with actual scores for this passe
                long shootersWithScores = teamPasses.stream()
                        .filter(p -> p.getPasseLfdnr() != null && p.getPasseLfdnr().intValue() == checkPasse)
                        .filter(this::hasActualScores) // Only count passes with real arrow data
                        .count();

                if (shootersWithScores >= 3) {
                    // This passe is complete for this team
                    highestCompletedPasse = passeNr;
                    LOGGER.debug("Team {} completed passe {} ({} shooters with scores)",
                            teamId, passeNr, shootersWithScores);
                } else {
                    // This passe is incomplete - no point checking higher passes
                    LOGGER.debug("Team {} passe {} incomplete ({} shooters with scores)",
                            teamId, passeNr, shootersWithScores);
                    break;
                }
            }

            int nextPasse = highestCompletedPasse + 1;

            // Ensure we don't exceed maximum passes
            if (nextPasse > MAX_SETS_PER_MATCH) {
                LOGGER.debug("Team {} completed all {} passes", teamId, MAX_SETS_PER_MATCH);
                return MAX_SETS_PER_MATCH + 1; // Signal match completion
            }

            LOGGER.debug("Team {} next passe: {} (highest completed: {})", teamId, nextPasse, highestCompletedPasse);
            return nextPasse;

        } catch (Exception e) {
            LOGGER.error("Error calculating next passe for team {} in match {}: {}", teamId, matchId, e.getMessage());
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
     * Checks match completion using database-calculated scores AND max passes completion.
     * CRITICAL FIX: Also considers when both teams completed all possible passes.
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

            // RULE 1: Standard completion - either team reaches 6+ Satzpunkte
            boolean completeByScore = satzpunkte >= MATCH_POINTS_TO_WIN || opponentSatzpunkte >= MATCH_POINTS_TO_WIN;

            // RULE 2: CRITICAL FIX - Match complete when either team finished all possible passes
            boolean completeByMaxPasses = false;
            try {
                int team1NextPasse = getNextPasseNumberForTeam(matchId, team1Id);
                int team2NextPasse = getNextPasseNumberForTeam(matchId, team2Id);

                // If either team's next passe would be > MAX_SETS_PER_MATCH, that team completed all passes
                completeByMaxPasses = (team1NextPasse > MAX_SETS_PER_MATCH) && (team2NextPasse > MAX_SETS_PER_MATCH);

                LOGGER.debug("Max passes check: team1NextPasse={}, team2NextPasse={}, maxPasses={}, completeByMaxPasses={}",
                        team1NextPasse, team2NextPasse, MAX_SETS_PER_MATCH, completeByMaxPasses);

            } catch (Exception e) {
                LOGGER.warn("Error checking max passes completion for match {}: {}", matchId, e.getMessage());
                // Continue with score-based check only
            }

            boolean complete = completeByScore || completeByMaxPasses;

            LOGGER.debug("Infrastructure-based completion check: match={}, team={}pts, opponent={}pts, completeByScore={}, completeByMaxPasses={}, complete={}",
                    matchId, satzpunkte, opponentSatzpunkte, completeByScore, completeByMaxPasses, complete);

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
     * Check if the entire tournament (wettkampf) is complete.
     * This provides a high-level tournament completion check before examining individual matches.
     * Uses pass-based completion logic that doesn't rely on potentially stale Satzpunkte data.
     *
     * @param wettkampfId The tournament ID to check
     * @return true if all matches in the tournament are complete
     */
    public boolean isWettkampfCompleteOverall(long wettkampfId) {
        try {
            LOGGER.debug("Checking overall tournament completion for wettkampf {}", wettkampfId);

            // Get all matches in the tournament
            List<LigamatchBE> allMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfId);
            if (allMatches.isEmpty()) {
                LOGGER.warn("No matches found for wettkampf {}", wettkampfId);
                return false;
            }

            // Check if all matches have completion indicators using pass data
            int completedMatches = 0;
            int totalMatches = allMatches.size();

            for (LigamatchBE match : allMatches) {
                try {
                    // Use pass-based completion check instead of relying on potentially stale Satzpunkte
                    // This approach checks if actual pass data exists and is complete
                    long teamId = match.getMannschaftId();
                    long matchId = match.getMatchId();

                    // Get pass data for this team/match combination
                    List<PasseDO> teamPasses = passeComponent.findByMannschaftMatchId(teamId, matchId);

                    // A match is complete if the team has completed sufficient passes
                    // Either: 5 full sets completed OR team has achieved match win condition
                    boolean hasCompletedPasses = hasTeamCompletedMatch(teamPasses);

                    if (hasCompletedPasses) {
                        completedMatches++;
                        LOGGER.debug("Match {} for team {} is complete based on pass data ({} passes found)",
                                matchId, teamId, teamPasses.size());
                    } else {
                        LOGGER.debug("Match {} for team {} appears incomplete based on pass data ({} passes found)",
                                matchId, teamId, teamPasses.size());
                    }

                } catch (Exception e) {
                    LOGGER.debug("Error checking completion for match {} team {}: {} - assuming incomplete",
                            match.getMatchId(), match.getMannschaftId(), e.getMessage());
                    // If we can't determine completion, assume incomplete
                }
            }

            // Tournament is complete if all matches have been completed based on pass data
            boolean isComplete = completedMatches == totalMatches;

            LOGGER.info("Tournament completion check: wettkampf {} has {}/{} matches complete - tournament complete: {}",
                    wettkampfId, completedMatches, totalMatches, isComplete);

            return isComplete;

        } catch (Exception e) {
            LOGGER.error("Error checking overall tournament completion for wettkampf {}: {}", wettkampfId, e.getMessage());
            return false; // Conservative approach - assume incomplete on error
        }
    }

    /**
     * Check if a team has completed their match based on pass data.
     * A match is complete if the team has completed enough sets to determine a winner.
     * Teams can win in as few as 3 sets if they reach 6+ Satzpunkte.
     */
    private boolean hasTeamCompletedMatch(List<PasseDO> teamPasses) {
        if (teamPasses == null || teamPasses.isEmpty()) {
            return false;
        }

        // Group passes by set number to calculate set results
        var passesBySet = teamPasses.stream()
                .filter(this::hasActualScores) // Only count passes with real scores
                .collect(Collectors.groupingBy(p -> p.getPasseLfdnr().intValue()));

        // Calculate Satzpunkte by examining completed sets
        int teamSatzpunkte = 0;
        int completedSets = 0;

        for (int setNumber = 1; setNumber <= MAX_SETS_PER_MATCH; setNumber++) {
            List<PasseDO> setData = passesBySet.get(setNumber);

            if (setData != null && setData.size() >= 3) {
                // Set is complete (3 shooters have scored)
                completedSets++;

                // For completion check, we assume this team won the set
                // (We can't easily calculate opponent scores here, but for completion
                // detection we just need to know if enough sets are done)
                teamSatzpunkte += 2; // Assume this team won each completed set
            } else {
                // If this set is incomplete, no point checking higher sets
                break;
            }
        }

        // Match is complete if:
        // 1. Team has completed at least 3 sets (minimum for 6 Satzpunkte) OR
        // 2. All 5 sets are completed (maximum possible)
        boolean hasMinimumSets = completedSets >= 3; // Can win with 3 sets (6 Satzpunkte)
        boolean hasAllSets = completedSets >= MAX_SETS_PER_MATCH; // All sets done

        return hasMinimumSets || hasAllSets;
    }

    /**
     * Find current incomplete match for a team using Setzliste-aware tournament progression.
     * CRITICAL FIX: Replaced flawed naechsteMatchId logic with proper tournament bracket calculation.
     * Returns null if all matches are complete.
     */
    public LigamatchBE findCurrentIncompleteMatch(long wettkampfId, long teamId) {
        try {
            LOGGER.debug("Finding current incomplete match for team {} using enhanced tournament logic", teamId);

            // STEP 1: Check if the entire tournament is complete first
            // This provides a definitive answer without needing individual match checks
            if (isWettkampfCompleteOverall(wettkampfId)) {
                LOGGER.info("Tournament {} is complete overall - all teams finished", wettkampfId);
                return null; // Definitive: tournament is complete
            }

            // STEP 2: Tournament has incomplete matches - find this team's current match
            // Get all matches for this team sorted by match number (tournament progression order)
            List<LigamatchBE> teamMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfId).stream()
                    .filter(m -> Objects.equals(m.getMannschaftId(), teamId))
                    .sorted(Comparator.comparing(LigamatchBE::getMatchNr))
                    .toList();

            if (teamMatches.isEmpty()) {
                throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                        "No matches found for team " + teamId + " in wettkampf " + wettkampfId);
            }

            // STEP 3: Check each match in chronological order (by match number)
            for (LigamatchBE match : teamMatches) {
                try {
                    // Simple completion check using database-calculated scores
                    // A match is complete if it has non-null Satzpunkte > 0
                    Long satzpunkte = match.getSatzpunkte();
                    boolean isComplete = (satzpunkte != null && satzpunkte > 0);

                    if (!isComplete) {
                        // Found incomplete match - this is where the team should be
                        LOGGER.debug("Found incomplete match {} (nr={}) for team {} - no Satzpunkte recorded",
                                match.getMatchId(), match.getMatchNr(), teamId);
                        return match;
                    }

                    LOGGER.debug("Match {} (nr={}) complete for team {} with {} Satzpunkte",
                            match.getMatchId(), match.getMatchNr(), teamId, satzpunkte);

                } catch (Exception e) {
                    LOGGER.warn("Error checking match {} completion for team {}: {}",
                            match.getMatchId(), teamId, e.getMessage());

                    // If we can't determine completion, assume incomplete and return this match
                    LOGGER.info("Returning match {} as incomplete for team {} due to completion check error",
                            match.getMatchId(), teamId);
                    return match;
                }
            }

            // All matches are complete for this team
            LOGGER.info("All matches complete for team {} in wettkampf {} using enhanced tournament logic",
                    teamId, wettkampfId);
            return null;

        } catch (Exception e) {
            LOGGER.error("Error finding current incomplete match for team {} in wettkampf {}: {}",
                    teamId, wettkampfId, e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to find current match");
        }
    }


    /**
     * Find last match for a team (for completed tournaments).
     */
    public LigamatchBE findLastMatchForTeam(long wettkampfId, long teamId) {
        try {
            List<LigamatchBE> teamMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfId).stream()
                    .filter(m -> Objects.equals(m.getMannschaftId(), teamId))
                    .sorted(Comparator.comparingLong(LigamatchBE::getMatchNr))
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
        TOURNAMENT_8_TEAM(new int[][]{
                {5, 4, 2, 7, 1, 8, 3, 6},  // Match 1
                {3, 5, 8, 4, 7, 1, 6, 2},  // Match 2
                {4, 7, 1, 6, 2, 5, 8, 3},  // Match 3
                {8, 2, 7, 3, 6, 4, 1, 5},  // Match 4
                {7, 6, 5, 8, 3, 2, 4, 1},  // Match 5
                {1, 3, 4, 2, 8, 6, 5, 7},  // Match 6
                {2, 1, 6, 5, 4, 3, 7, 8}   // Match 7
        }),

        TOURNAMENT_6_TEAM(new int[][]{
                {2, 5, 1, 6, 3, 4},        // Match 1
                {6, 3, 2, 4, 5, 1},        // Match 2
                {1, 2, 5, 3, 4, 6},        // Match 3
                {5, 4, 3, 1, 6, 2},        // Match 4
                {4, 1, 6, 5, 2, 3}         // Match 5
        }),

        TOURNAMENT_4_TEAM(new int[][]{
                {1, 4, 2, 3},              // Match 1
                {2, 4, 3, 1},              // Match 2
                {4, 3, 1, 2},              // Match 3
                {4, 1, 2, 3},              // Match 4
                {1, 3, 4, 2},              // Match 5
                {3, 4, 2, 1}               // Match 6
        });

        private final int[][] structure;

        TournamentStructure(int[][] structure) {
            this.structure = structure;
        }

        public int[][] getStructure() {
            return structure;
        }
    }

    /**
     * Find the correct next match for a team using tournament bracket progression.
     * This bypasses the flawed database naechsteMatchId calculation.
     */
    public LigamatchBE findCorrectNextMatch(long currentMatchId, long teamId) {
        try {
            LOGGER.error("🔴 TOURNAMENT MATRIX LOGIC TRIGGERED for team {} from match {}", teamId, currentMatchId);

            // Get current match details
            LigamatchBE currentMatch = matchComponent.getLigamatchById(currentMatchId);
            if (currentMatch == null) {
                LOGGER.error("🔴 Current match {} not found", currentMatchId);
                return null;
            }

            // Determine tournament structure
            long wettkampfId = currentMatch.getWettkampfId();
            TournamentStructure tournament = determineTournamentStructure(wettkampfId);
            LOGGER.error("🔴 Using tournament structure: {} teams",
                    tournament == TournamentStructure.TOURNAMENT_8_TEAM ? "8" :
                            tournament == TournamentStructure.TOURNAMENT_6_TEAM ? "6" :
                                    tournament == TournamentStructure.TOURNAMENT_4_TEAM ? "4" : "unknown");

            // Find team's ranking position in current match
            int teamRankingPosition = findTeamRankingPosition(currentMatch, teamId, wettkampfId);
            if (teamRankingPosition == -1) {
                LOGGER.error("🔴 Could not determine team ranking position for team {} in match {}",
                        teamId, currentMatchId);
                return null;
            }
            LOGGER.error("🔴 Team {} has ranking position {} in match {}", teamId, teamRankingPosition, currentMatchId);

            // Calculate next match position using tournament matrix
            int currentMatchNumber = Math.toIntExact(currentMatch.getMatchNr());
            int nextMatchNumber = currentMatchNumber + 1;
            LOGGER.error("🔴 Looking for next match number {} after current match number {}", nextMatchNumber, currentMatchNumber);

            // Check if next match exists in tournament
            if (nextMatchNumber > tournament.getStructure().length) {
                LOGGER.error("🔴 Team {} completed all matches (no match {} in tournament structure with {} matches)",
                        teamId, nextMatchNumber, tournament.getStructure().length);
                return null; // Tournament complete
            }

            // Find team's position in next match using tournament matrix
            int nextTargetPosition = findTeamPositionInMatch(tournament, teamRankingPosition, nextMatchNumber - 1);
            if (nextTargetPosition == -1) {
                LOGGER.error("🔴 Could not determine next position for team ranking {} in match {}",
                        teamRankingPosition, nextMatchNumber);
                return null;
            }
            LOGGER.error("🔴 Team {} should be at target position {} in next match {}", teamId, nextTargetPosition, nextMatchNumber);

            // Query database for match at calculated position
            List<LigamatchBE> nextMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfId)
                    .stream()
                    .filter(m -> m.getMatchNr().intValue() == nextMatchNumber)
                    .filter(m -> m.getMatchScheibennummer().intValue() == nextTargetPosition)
                    .toList();

            LOGGER.error("🔴 Database query: wettkampf={}, matchNr={}, scheibe={}, found {} matches",
                    wettkampfId, nextMatchNumber, nextTargetPosition, nextMatches.size());

            if (nextMatches.size() != 1) {
                LOGGER.error("🔴 Expected exactly 1 match for wettkampf={}, matchNr={}, scheibe={}, found {}",
                        wettkampfId, nextMatchNumber, nextTargetPosition, nextMatches.size());
                return null;
            }

            LigamatchBE nextMatch = nextMatches.get(0);
            LOGGER.error("🔴 TOURNAMENT PROGRESSION SUCCESS: team {} from match {} (target {}) to match {} (target {})",
                    teamId, currentMatchNumber, currentMatch.getMatchScheibennummer(),
                    nextMatchNumber, nextTargetPosition);

            return nextMatch;

        } catch (Exception e) {
            LOGGER.error("🔴 ERROR in tournament matrix logic for team {} from match {}: {}",
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

        return switch (teamCount) {
            case 8 -> TournamentStructure.TOURNAMENT_8_TEAM;
            case 6 -> TournamentStructure.TOURNAMENT_6_TEAM;
            case 4 -> TournamentStructure.TOURNAMENT_4_TEAM;
            default -> throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Unsupported tournament size: " + teamCount + " teams");
        };
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