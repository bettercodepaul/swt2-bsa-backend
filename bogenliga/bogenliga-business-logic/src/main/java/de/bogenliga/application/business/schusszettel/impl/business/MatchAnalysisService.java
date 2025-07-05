package de.bogenliga.application.business.schusszettel.impl.business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * Centralized service for match analysis and completion logic.
 * 
 * This service provides the single source of truth for:
 * - Match completion detection
 * - Set points calculation
 * - Satz results building
 * - Match progress analysis
 * 
 * Used by both TabletSchusszettelComponentImpl and TabletSchusszettelSyncComponent
 * to ensure consistent match analysis logic across the entire system.
 * 
 * @author System Refactoring
 */
@Service
public class MatchAnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchAnalysisService.class);

    // Official archery rules constants
    private static final int MAX_SETS = 5;
    private static final int SHOOTERS_PER_TEAM = 3;
    private static final int MATCH_POINTS_TO_WIN = 6;
    private static final int SET_WIN_POINTS = 2;
    private static final int SET_TIE_POINTS = 1;

    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;

    @Autowired
    public MatchAnalysisService(MatchComponent matchComponent, PasseComponent passeComponent) {
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
    }

    /**
     * Comprehensive match analysis result containing all relevant match state information.
     */
    public static class MatchAnalysisResult {
        private final MatchStatus status;
        private final int completedSets;
        private final int currentPasse;
        private final int team1Satzpunkte;
        private final int team2Satzpunkte;
        private final List<SatzErgebnisDO> satzErgebnisse;
        private final String statusReason;

        public MatchAnalysisResult(MatchStatus status, int completedSets, int currentPasse, 
                                 int team1Satzpunkte, int team2Satzpunkte, 
                                 List<SatzErgebnisDO> satzErgebnisse, String statusReason) {
            this.status = status;
            this.completedSets = completedSets;
            this.currentPasse = currentPasse;
            this.team1Satzpunkte = team1Satzpunkte;
            this.team2Satzpunkte = team2Satzpunkte;
            this.satzErgebnisse = satzErgebnisse != null ? satzErgebnisse : new ArrayList<>();
            this.statusReason = statusReason;
        }

        // Getters
        public MatchStatus getStatus() { return status; }
        public int getCompletedSets() { return completedSets; }
        public int getCurrentPasse() { return currentPasse; }
        public int getTeam1Satzpunkte() { return team1Satzpunkte; }
        public int getTeam2Satzpunkte() { return team2Satzpunkte; }
        public List<SatzErgebnisDO> getSatzErgebnisse() { return satzErgebnisse; }
        public String getStatusReason() { return statusReason; }

        public boolean isComplete() { return status == MatchStatus.COMPLETED; }
        public boolean isInProgress() { return status == MatchStatus.IN_PROGRESS; }
        public boolean isNotStarted() { return status == MatchStatus.NOT_STARTED; }
    }

    /**
     * Match status enumeration
     */
    public enum MatchStatus {
        NOT_STARTED,    // No passes recorded yet
        IN_PROGRESS,    // Some passes recorded, not complete
        COMPLETED,      // Match finished (6+ Satzpunkte or 5 sets)
        INVALID         // Error in data or analysis
    }

    /**
     * Main method: Comprehensive match analysis for any match and team combination.
     * This is the single source of truth for match state analysis.
     */
    public MatchAnalysisResult analyzeMatch(long matchId, long team1Id, long team2Id) {
        try {
            LOGGER.debug("Analyzing match {} for teams {} vs {}", matchId, team1Id, team2Id);

            // Get all passes for both teams in this match
            List<PasseDO> team1Passes = passeComponent.findByMannschaftMatchId(team1Id, matchId);
            List<PasseDO> team2Passes = passeComponent.findByMannschaftMatchId(team2Id, matchId);

            return analyzeMatchFromPasses(team1Passes, team2Passes, team1Id, team2Id);

        } catch (Exception e) {
            LOGGER.error("Error analyzing match {} for teams {} vs {}: {}", 
                        matchId, team1Id, team2Id, e.getMessage());
            return new MatchAnalysisResult(MatchStatus.INVALID, 0, 1, 0, 0, new ArrayList<>(), 
                                         "Error during analysis: " + e.getMessage());
        }
    }

    /**
     * Analyze match from pass data - core analysis logic
     */
    public MatchAnalysisResult analyzeMatchFromPasses(List<PasseDO> team1Passes, List<PasseDO> team2Passes, 
                                                     long team1Id, long team2Id) {
        
        // Build satz results from raw pass data
        List<SatzErgebnisDO> satzErgebnisse = buildSatzErgebnisse(team1Passes, team2Passes, team1Id, team2Id);
        
        if (satzErgebnisse.isEmpty()) {
            return new MatchAnalysisResult(MatchStatus.NOT_STARTED, 0, 1, 0, 0, satzErgebnisse, 
                                         "No sets completed yet");
        }

        // Calculate Satzpunkte from completed sets
        int team1Satzpunkte = 0;
        int team2Satzpunkte = 0;
        int completedSets = satzErgebnisse.size();

        for (SatzErgebnisDO satz : satzErgebnisse) {
            if (satz.getTeam1Punkte() > satz.getTeam2Punkte()) {
                team1Satzpunkte += SET_WIN_POINTS;
            } else if (satz.getTeam2Punkte() > satz.getTeam1Punkte()) {
                team2Satzpunkte += SET_WIN_POINTS;
            } else {
                // Tie - both teams get 1 point
                team1Satzpunkte += SET_TIE_POINTS;
                team2Satzpunkte += SET_TIE_POINTS;
            }
        }

        // Determine match status based on official rules
        MatchStatus status;
        String statusReason;
        int currentPasse = completedSets + 1;

        // Check for early termination (6+ Satzpunkte)
        if (team1Satzpunkte >= MATCH_POINTS_TO_WIN || team2Satzpunkte >= MATCH_POINTS_TO_WIN) {
            status = MatchStatus.COMPLETED;
            statusReason = String.format("Match completed: Team scores %d-%d Satzpunkte", 
                                       team1Satzpunkte, team2Satzpunkte);
            currentPasse = completedSets; // Don't advance past completion
        } 
        // Check for maximum sets reached
        else if (completedSets >= MAX_SETS) {
            status = MatchStatus.COMPLETED;
            statusReason = String.format("Match completed: Maximum %d sets reached (%d-%d)", 
                                       MAX_SETS, team1Satzpunkte, team2Satzpunkte);
            currentPasse = completedSets;
        }
        // Check if current set is in progress
        else if (hasPartialSetData(team1Passes, team2Passes, currentPasse)) {
            status = MatchStatus.IN_PROGRESS;
            statusReason = String.format("Match in progress: Set %d partially completed", currentPasse);
        }
        // Ready for next set
        else {
            status = MatchStatus.IN_PROGRESS;
            statusReason = String.format("Match in progress: Ready for set %d", currentPasse);
        }

        return new MatchAnalysisResult(status, completedSets, currentPasse, 
                                     team1Satzpunkte, team2Satzpunkte, satzErgebnisse, statusReason);
    }

    /**
     * Build SatzErgebnisDO list from raw pass data with comprehensive validation.
     * This replaces the duplicated logic in both main and sync components.
     */
    public List<SatzErgebnisDO> buildSatzErgebnisse(List<PasseDO> team1Passes, List<PasseDO> team2Passes, 
                                                   long team1Id, long team2Id) {
        
        // Combine and group all passes by set number
        List<PasseDO> allPasses = new ArrayList<>();
        allPasses.addAll(team1Passes);
        allPasses.addAll(team2Passes);

        Map<Long, List<PasseDO>> passesBySet = allPasses.stream()
                .collect(Collectors.groupingBy(PasseDO::getPasseLfdnr));

        return passesBySet.entrySet().stream()
                .filter(entry -> isSetComplete(entry.getValue(), team1Id, team2Id))
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    int setNumber = Math.toIntExact(entry.getKey());
                    List<PasseDO> setPasses = entry.getValue();

                    int team1Points = calculateSetPoints(setPasses, team1Id);
                    int team2Points = calculateSetPoints(setPasses, team2Id);

                    return new SatzErgebnisDO(setNumber, team1Points, team2Points);
                })
                .collect(Collectors.<SatzErgebnisDO>toList());
    }

    /**
     * Calculate total points for a team in a specific set with validation.
     * This is the definitive method for set point calculation.
     */
    public int calculateSetPoints(List<PasseDO> setPasses, long teamId) {
        if (setPasses == null || setPasses.isEmpty()) {
            return 0;
        }

        List<PasseDO> teamPasses = setPasses.stream()
                .filter(p -> Objects.equals(p.getPasseMannschaftId(), teamId))
                .collect(Collectors.toList());

        int totalPoints = 0;
        for (PasseDO passe : teamPasses) {
            totalPoints += getValidatedArrowValue(passe.getPfeil1(), "Pfeil1");
            totalPoints += getValidatedArrowValue(passe.getPfeil2(), "Pfeil2"); 
            totalPoints += getValidatedArrowValue(passe.getPfeil3(), "Pfeil3");
        }

        return totalPoints;
    }

    /**
     * Validate arrow values according to archery rules (0-10 points)
     */
    private int getValidatedArrowValue(Integer arrowValue, String arrowName) {
        if (arrowValue == null) {
            return 0;
        }
        
        if (arrowValue < 0 || arrowValue > 10) {
            LOGGER.warn("Invalid arrow value detected: {} = {} (outside 0-10 range)", arrowName, arrowValue);
            return 0; // Treat invalid values as misses
        }
        
        return arrowValue;
    }

    /**
     * Check if a set is complete (both teams have exactly 3 shooters with shot data)
     */
    private boolean isSetComplete(List<PasseDO> setPasses, long team1Id, long team2Id) {
        if (setPasses == null || setPasses.isEmpty()) {
            return false;
        }

        // Count shooters for each team
        long team1Shooters = setPasses.stream()
                .filter(p -> Objects.equals(p.getPasseMannschaftId(), team1Id))
                .filter(this::hasActualShotData)
                .count();

        long team2Shooters = setPasses.stream()
                .filter(p -> Objects.equals(p.getPasseMannschaftId(), team2Id))
                .filter(this::hasActualShotData)
                .count();

        // Both teams must have exactly 3 shooters with shot data
        boolean isComplete = team1Shooters == SHOOTERS_PER_TEAM && team2Shooters == SHOOTERS_PER_TEAM;
        
        if (!isComplete) {
            LOGGER.debug("Set incomplete: Team {} has {} shooters, Team {} has {} shooters", 
                        team1Id, team1Shooters, team2Id, team2Shooters);
        }
        
        return isComplete;
    }

    /**
     * Check if pass has actual shot data (non-null arrow values)
     */
    private boolean hasActualShotData(PasseDO passe) {
        return passe.getPfeil1() != null || passe.getPfeil2() != null || passe.getPfeil3() != null;
    }

    /**
     * Check if current set has partial data (some but not all shooters completed)
     */
    private boolean hasPartialSetData(List<PasseDO> team1Passes, List<PasseDO> team2Passes, int setNumber) {
        List<PasseDO> currentSetPasses = new ArrayList<>();
        currentSetPasses.addAll(team1Passes.stream()
                .filter(p -> p.getPasseLfdnr() == setNumber)
                .collect(Collectors.toList()));
        currentSetPasses.addAll(team2Passes.stream()
                .filter(p -> p.getPasseLfdnr() == setNumber)
                .collect(Collectors.toList()));

        // Has partial data if there are some passes but set is not complete
        boolean hasData = currentSetPasses.stream().anyMatch(this::hasActualShotData);
        boolean isComplete = isSetComplete(currentSetPasses, 
                team1Passes.isEmpty() ? 0 : team1Passes.get(0).getPasseMannschaftId(),
                team2Passes.isEmpty() ? 0 : team2Passes.get(0).getPasseMannschaftId());

        return hasData && !isComplete;
    }

    /**
     * Quick check if match is complete - convenience method
     */
    public boolean isMatchComplete(long matchId, long team1Id, long team2Id) {
        MatchAnalysisResult result = analyzeMatch(matchId, team1Id, team2Id);
        return result.isComplete();
    }

    /**
     * Get current passe number for a match - convenience method
     */
    public int getCurrentPasseNumber(long matchId, long team1Id, long team2Id) {
        MatchAnalysisResult result = analyzeMatch(matchId, team1Id, team2Id);
        return result.getCurrentPasse();
    }
}