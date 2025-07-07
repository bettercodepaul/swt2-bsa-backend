package de.bogenliga.application.business.schusszettel.impl.business;

import java.util.List;
import java.util.Objects;

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
 * THIN FACADE: Match analysis service that delegates to existing infrastructure.
 * 
 * <h2>DESIGN PRINCIPLE</h2>
 * This service acts as a minimal abstraction layer over existing business logic.
 * It does NOT duplicate functionality but rather coordinates existing infrastructure
 * to provide match analysis specifically for the schusszettel state machine.
 * 
 * <h2>EXISTING INFRASTRUCTURE USAGE</h2>
 * <ul>
 *   <li>LigamatchBE: Uses pre-calculated Satzpunkte and Matchpunkte from database view</li>
 *   <li>LigamatchBE.matchIdGegner: Leverages built-in opponent resolution</li>
 *   <li>LigamatchBE.naechsteMatchId: Uses existing tournament progression data</li>
 *   <li>PasseComponent: Delegates to established pass retrieval methods</li>
 *   <li>MatchComponent: Uses existing LigamatchBE access methods</li>
 * </ul>
 * 
 * <h2>THIN FACADE RESPONSIBILITIES</h2>
 * <ul>
 *   <li>Coordinate existing infrastructure for match state analysis</li>
 *   <li>Provide schusszettel-specific business logic abstraction</li>
 *   <li>Handle session state machine requirements only</li>
 *   <li>Delegate all calculations to existing business logic</li>
 * </ul>
 * 
 * <h2>WHAT THIS SERVICE DOES NOT DO</h2>
 * <ul>
 *   <li>Calculate scores (uses LigamatchBE pre-calculated fields)</li>
 *   <li>Implement pass counting logic (delegates to existing patterns)</li>
 *   <li>Handle database access directly (uses existing components)</li>
 *   <li>Duplicate existing business rules (reuses established constants)</li>
 * </ul>
 * 
 * @author Marty Lauterbach 
 * @version 3.0 - Refactored as thin facade over existing infrastructure
 * @version 2.0 - Used LigamatchBE database view (contained duplicated logic)
 * @since 1.0 - Custom calculation implementation (deprecated)
 */
@Service
public class MatchAnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchAnalysisService.class);
    
    // WA Official archery rules constants
    public static final int MATCH_POINTS_TO_WIN = 6;
    public static final int MAX_SETS_PER_MATCH = 5;

    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;

    @Autowired
    public MatchAnalysisService(MatchComponent matchComponent, PasseComponent passeComponent) {
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
    }

    /**
     * Simplified match analysis result using LigamatchBE data.
     */
    public static class MatchAnalysisResult {
        private final boolean isComplete;
        private final int currentPasse;
        private final long team1Satzpunkte;
        private final long team2Satzpunkte;
        private final String statusReason;

        public MatchAnalysisResult(boolean isComplete, int currentPasse, 
                                 long team1Satzpunkte, long team2Satzpunkte, String statusReason) {
            this.isComplete = isComplete;
            this.currentPasse = currentPasse;
            this.team1Satzpunkte = team1Satzpunkte;
            this.team2Satzpunkte = team2Satzpunkte;
            this.statusReason = statusReason;
        }

        // Getters
        public boolean isComplete() { return isComplete; }
        public int getCurrentPasse() { return currentPasse; }
        public long getTeam1Satzpunkte() { return team1Satzpunkte; }
        public long getTeam2Satzpunkte() { return team2Satzpunkte; }
        public String getStatusReason() { return statusReason; }
        
        // Legacy compatibility
        public boolean isInProgress() { return !isComplete; }
        public boolean isNotStarted() { return currentPasse == 1 && team1Satzpunkte == 0 && team2Satzpunkte == 0; }
    }

    /**
     * THIN FACADE: Match analysis using existing infrastructure.
     * Delegates to LigamatchBE pre-calculated data and existing business logic.
     */
    public MatchAnalysisResult analyzeMatch(long matchId, long team1Id, long team2Id) {
        try {
            LOGGER.debug("Analyzing match {} using existing infrastructure", matchId);

            // REUSE: Get pre-calculated data from existing ligamatch view
            LigamatchBE ligamatch = matchComponent.getLigamatchById(matchId);
            if (ligamatch == null) {
                LOGGER.warn("Ligamatch {} not found", matchId);
                return new MatchAnalysisResult(false, 1, 0, 0, "Match not found");
            }

            // REUSE: Database-calculated scores (no manual calculation needed)
            long satzpunkte = ligamatch.getSatzpunkte() != null ? ligamatch.getSatzpunkte() : 0;
            
            // REUSE: Built-in opponent resolution from ligamatch view
            long opponentSatzpunkte = 0;
            if (ligamatch.getMatchIdGegner() != null) {
                LigamatchBE opponentMatch = matchComponent.getLigamatchById(ligamatch.getMatchIdGegner());
                if (opponentMatch != null && opponentMatch.getSatzpunkte() != null) {
                    opponentSatzpunkte = opponentMatch.getSatzpunkte();
                }
            }
            
            // REUSE: Standard match completion rules (existing constants)
            boolean isComplete = satzpunkte >= MATCH_POINTS_TO_WIN || opponentSatzpunkte >= MATCH_POINTS_TO_WIN;
            
            // DELEGATE: Pass counting to existing infrastructure
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
     * THIN FACADE: Calculate current passe using existing infrastructure.
     * Delegates to established PasseComponent methods and existing patterns.
     * 
     * FIXED: Find first incomplete passe instead of highest passe number.
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
                
                // If either team has fewer than 3 shooters with actual scores, this passe needs completion
                if (team1ShootersWithScores < 3 || team2ShootersWithScores < 3) {
                    LOGGER.debug("Passe {} incomplete: team1={} shooters, team2={} shooters with scores", 
                               passeNr, team1ShootersWithScores, team2ShootersWithScores);
                    return passeNr; // This passe needs completion
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
     * OPTIMIZED: Find opponent team ID using LigamatchBE built-in opponent data.
     * Much simpler than complex begegnung logic.
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
     * THIN FACADE: Match completion using existing infrastructure.
     * Directly uses LigamatchBE pre-calculated scores and existing constants.
     */
    public boolean isMatchComplete(long matchId, long team1Id, long team2Id) {
        try {
            // REUSE: Get pre-calculated data from existing ligamatch view
            LigamatchBE ligamatch = matchComponent.getLigamatchById(matchId);
            if (ligamatch == null) {
                return false;
            }
            
            // REUSE: Database-calculated scores (no manual calculation)
            long satzpunkte = ligamatch.getSatzpunkte() != null ? ligamatch.getSatzpunkte() : 0;
            
            // REUSE: Built-in opponent resolution from ligamatch view
            long opponentSatzpunkte = 0;
            if (ligamatch.getMatchIdGegner() != null) {
                LigamatchBE opponentMatch = matchComponent.getLigamatchById(ligamatch.getMatchIdGegner());
                if (opponentMatch != null && opponentMatch.getSatzpunkte() != null) {
                    opponentSatzpunkte = opponentMatch.getSatzpunkte();
                }
            }
            
            // REUSE: Standard completion rules (existing constants)
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
     * THIN FACADE: Get current passe number using existing infrastructure.
     * Delegates to established pass counting methods.
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
}