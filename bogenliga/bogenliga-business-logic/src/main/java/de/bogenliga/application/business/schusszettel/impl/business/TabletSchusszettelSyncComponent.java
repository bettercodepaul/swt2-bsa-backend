package de.bogenliga.application.business.schusszettel.impl.business;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * Synchronization component for tablet sessions.
 * Handles synchronization logic that can be shared between admin and standard components.
 *
 * @author Marty Lauterbach
 */
@Component
public class TabletSchusszettelSyncComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSchusszettelSyncComponent.class);

    private final TabletSchusszettelDAO sessionDAO;
    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final DsbMannschaftComponent mannschaftComponent;

    @Autowired
    public TabletSchusszettelSyncComponent(TabletSchusszettelDAO sessionDAO,
                                           MatchComponent matchComponent,
                                           PasseComponent passeComponent,
                                           DsbMannschaftComponent mannschaftComponent) {
        this.sessionDAO = sessionDAO;
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
        this.mannschaftComponent = mannschaftComponent;
    }

    /**
     * Synchronizes tablet session data with the actual match/wettkampf data.
     * This is the main synchronization method that can be called from both admin and standard components.
     *
     * @param session The current tablet session
     * @param wettkampfId The wettkampf ID
     * @param teamId The team ID
     * @param updateDatabase Whether to update the database with corrected data
     * @return SyncResult containing the results of the synchronization
     */
    public SyncResult synchronizeSession(TabletSchusszettelEntity session, long wettkampfId,
                                         long teamId, boolean updateDatabase) {
        LOGGER.debug("Starting synchronization for wettkampfId={}, teamId={}, updateDb={}",
                wettkampfId, teamId, updateDatabase);

        try {
            // 1. Verify the team/mannschaft still exists and is valid
            if (!validateTeam(teamId)) {
                return SyncResult.failure("Team " + teamId + " no longer exists or is invalid");
            }

            // 2. Check if current match is still valid
            MatchValidationResult matchValidation = validateCurrentMatch(session, wettkampfId, teamId);

            if (matchValidation.isValid) {
                // Current match is still valid, no update needed
                LOGGER.debug("Current match {} is still valid for team {}", session.getCurrentMatchId(), teamId);
                return SyncResult.success("Session data is already synchronized", false);
            }

            // 3. Find the correct current match
            MatchDO correctMatch = findCorrectCurrentMatch(wettkampfId, teamId);

            if (correctMatch == null) {
                return SyncResult.failure("No valid matches found for team " + teamId + " in wettkampf " + wettkampfId);
            }

            // 4. Update session with correct match data
            SyncResult updateResult = updateSessionData(session, correctMatch, teamId, updateDatabase);

            LOGGER.info("Synchronization completed for team {} in wettkampf {}: {}",
                    teamId, wettkampfId, updateResult.message);

            return updateResult;

        } catch (Exception e) {
            String errorMsg = "Critical error during synchronization for wettkampfId=" + wettkampfId +
                    ", teamId=" + teamId + ": " + e.getMessage();
            LOGGER.error(errorMsg, e);
            return SyncResult.failure(errorMsg);
        }
    }

    /**
     * Validates that a team still exists and is valid
     */
    private boolean validateTeam(long teamId) {
        try {
            DsbMannschaftDO mannschaft = mannschaftComponent.findById(teamId);
            return mannschaft != null;
        } catch (Exception e) {
            LOGGER.error("Failed to verify team {}: {}", teamId, e.getMessage());
            return false;
        }
    }

    /**
     * Validates if the current match in the session is still valid
     */
    private MatchValidationResult validateCurrentMatch(TabletSchusszettelEntity session,
                                                       long wettkampfId, long teamId) {
        long currentMatchId = session.getCurrentMatchId();

        try {
            MatchDO currentMatch = matchComponent.findById(currentMatchId);

            if (currentMatch == null) {
                return new MatchValidationResult(false, "Match " + currentMatchId + " no longer exists");
            }

            // Verify match belongs to correct wettkampf and team
            if (!Objects.equals(currentMatch.getWettkampfId(), wettkampfId) ||
                    !Objects.equals(currentMatch.getMannschaftId(), teamId)) {
                return new MatchValidationResult(false,
                        "Match " + currentMatchId + " doesn't match session data");
            }

            return new MatchValidationResult(true, "Match is valid");

        } catch (Exception e) {
            return new MatchValidationResult(false, "Failed to verify match " + currentMatchId + ": " + e.getMessage());
        }
    }

    /**
     * Finds the correct current match for a team based on actual progress
     */
    private MatchDO findCorrectCurrentMatch(long wettkampfId, long teamId) {
        List<MatchDO> teamMatches = matchComponent.findByWettkampfId(wettkampfId).stream()
                .filter(m -> Objects.equals(m.getMannschaftId(), teamId))
                .sorted(Comparator.comparingLong(MatchDO::getNr))
                .toList();

        return findCurrentMatchForTeam(teamMatches, teamId);
    }

    /**
     * Updates the session with correct match data
     */
    private SyncResult updateSessionData(TabletSchusszettelEntity session, MatchDO correctMatch,
                                         long teamId, boolean updateDatabase) {
        try {
            LOGGER.info("Updating session for team {} to match {} (Nr: {})",
                    teamId, correctMatch.getId(), correctMatch.getNr());

            // Update session with correct match data
            session.setCurrentMatchId(correctMatch.getId());
            session.setCurrentMatchNumber(Math.toIntExact(correctMatch.getNr()));

            // Find and set correct opponent
            long opponentTeamId = findOpponentTeamId(correctMatch, teamId);
            session.setGegnerTeamId(opponentTeamId);

            // Determine correct passe number based on existing data
            int correctPasseNumber = determineCorrectPasseNumber(correctMatch.getId(), teamId);
            session.setCurrentPasseNumber(correctPasseNumber);

            // Update the session in database if requested
            if (updateDatabase) {
                sessionDAO.updateStatus(session, 0L);
                LOGGER.info("Session database updated for team {} to match {} passe {}",
                        teamId, correctMatch.getId(), correctPasseNumber);
            }

            return SyncResult.success(
                    "Session synchronized to match " + correctMatch.getId() + " passe " + correctPasseNumber,
                    true);

        } catch (Exception e) {
            String errorMsg = "Failed to update session data for team " + teamId + ": " + e.getMessage();
            LOGGER.error(errorMsg, e);
            return SyncResult.failure(errorMsg);
        }
    }

    /**
     * Finds the current match for a team based on actual match progress and completion status.
     * This analyzes existing passe data to determine which match is actually in progress.
     */
    private MatchDO findCurrentMatchForTeam(List<MatchDO> teamMatches, long teamId) {
        if (teamMatches.isEmpty()) {
            return null;
        }

        LOGGER.debug("Finding current match for team {} among {} matches", teamId, teamMatches.size());

        // Analyze each match to determine its completion status
        for (MatchDO match : teamMatches) {
            try {
                MatchAnalysis analysis = analyzeMatchProgress(match, teamId);

                LOGGER.debug("Match {} (Nr: {}) analysis: status={}, setsCompleted={}, totalPasses={}",
                        match.getId(), match.getNr(), analysis.status, analysis.completedSets, analysis.totalPasses);

                switch (analysis.status) {
                    case NOT_STARTED:
                        // First match that hasn't started yet - this is our current match
                        LOGGER.info("Found current match {} (Nr: {}) - not started yet", match.getId(), match.getNr());
                        return match;

                    case IN_PROGRESS:
                        // Match is actively being played - definitely our current match
                        LOGGER.info("Found current match {} (Nr: {}) - in progress (passe {})",
                                match.getId(), match.getNr(), analysis.currentPasse);
                        return match;

                    case COMPLETED:
                        // This match is done, continue to next
                        LOGGER.debug("Match {} (Nr: {}) is completed, checking next match", match.getId(), match.getNr());
                        continue;

                    case INVALID:
                        // Something wrong with this match, skip it
                        LOGGER.warn("Match {} (Nr: {}) has invalid data, skipping", match.getId(), match.getNr());
                        continue;
                }

            } catch (Exception e) {
                LOGGER.warn("Error analyzing match {} for team {}: {}", match.getId(), teamId, e.getMessage());
                continue;
            }
        }

        // All matches are completed or invalid - return the last valid match
        MatchDO lastMatch = teamMatches.get(teamMatches.size() - 1);
        LOGGER.info("All matches appear completed, returning last match {} (Nr: {})",
                lastMatch.getId(), lastMatch.getNr());
        return lastMatch;
    }

    /**
     * Analyzes a single match to determine its current progress and status
     */
    private MatchAnalysis analyzeMatchProgress(MatchDO match, long teamId) {
        try {
            // Get all passes for this match and team
            List<PasseDO> teamPasses = passeComponent.findByMannschaftMatchId(teamId, match.getId());

            if (teamPasses.isEmpty()) {
                return new MatchAnalysis(MatchStatus.NOT_STARTED, 0, 0, 1);
            }

            // Find opponent team ID for this match
            long opponentId;
            try {
                opponentId = findOpponentTeamId(match, teamId);
            } catch (Exception e) {
                LOGGER.warn("Could not find opponent for match {}: {}", match.getId(), e.getMessage());
                return new MatchAnalysis(MatchStatus.INVALID, 0, 0, 1);
            }

            // Get opponent's passes too
            List<PasseDO> opponentPasses = passeComponent.findByMannschaftMatchId(opponentId, match.getId());

            // Analyze the passes to determine match status
            return analyzePasseData(teamPasses, opponentPasses, teamId, opponentId);

        } catch (Exception e) {
            LOGGER.error("Error analyzing match progress for match {}, team {}: {}",
                    match.getId(), teamId, e.getMessage());
            return new MatchAnalysis(MatchStatus.INVALID, 0, 0, 1);
        }
    }

    /**
     * Analyzes passe data to determine match status and progress
     */
    private MatchAnalysis analyzePasseData(List<PasseDO> teamPasses, List<PasseDO> opponentPasses,
                                           long teamId, long opponentId) {

        // Group passes by passe number (lfdnr) to analyze sets
        Map<Long, List<PasseDO>> teamPassesBySet = teamPasses.stream()
                .collect(Collectors.groupingBy(PasseDO::getPasseLfdnr));

        Map<Long, List<PasseDO>> opponentPassesBySet = opponentPasses.stream()
                .collect(Collectors.groupingBy(PasseDO::getPasseLfdnr));

        // Find the highest passe number with data
        int maxTeamPasse = teamPasses.stream()
                .mapToInt(p -> Math.toIntExact(p.getPasseLfdnr()))
                .max().orElse(0);

        int maxOpponentPasse = opponentPasses.stream()
                .mapToInt(p -> Math.toIntExact(p.getPasseLfdnr()))
                .max().orElse(0);

        int maxPasse = Math.max(maxTeamPasse, maxOpponentPasse);

        // Calculate completed sets and match status
        int completedSets = 0;
        int currentPasse = 1;
        boolean matchInProgress = false;

        // Check each passe to see if it's completed (both teams have shot data)
        for (int passe = 1; passe <= maxPasse; passe++) {
            boolean teamHasData = hasActualShotData(teamPassesBySet.get((long) passe));
            boolean opponentHasData = hasActualShotData(opponentPassesBySet.get((long) passe));

            if (teamHasData && opponentHasData) {
                // Both teams completed this passe
                completedSets++;
            } else if (teamHasData || opponentHasData) {
                // One team has shot, match is in progress
                matchInProgress = true;
                currentPasse = passe;
                break;
            } else {
                // Neither team has shot yet
                currentPasse = passe;
                break;
            }
        }

        // Determine overall match status
        MatchStatus status;
        if (completedSets == 0 && !matchInProgress) {
            status = MatchStatus.NOT_STARTED;
        } else if (completedSets >= 5 || isMatchWon(completedSets, teamPassesBySet, opponentPassesBySet, teamId, opponentId)) {
            status = MatchStatus.COMPLETED;
        } else {
            status = MatchStatus.IN_PROGRESS;
        }

        return new MatchAnalysis(status, completedSets, teamPasses.size() + opponentPasses.size(), currentPasse);
    }

    /**
     * Checks if a list of passes has actual shot data (non-null arrow values)
     */
    private boolean hasActualShotData(List<PasseDO> passes) {
        if (passes == null || passes.isEmpty()) {
            return false;
        }

        return passes.stream().anyMatch(p ->
                p.getPfeil1() != null || p.getPfeil2() != null || p.getPfeil3() != null);
    }

    /**
     * Determines if a match is won based on completed sets and match points
     */
    private boolean isMatchWon(int completedSets, Map<Long, List<PasseDO>> teamPasses,
                               Map<Long, List<PasseDO>> opponentPasses, long teamId, long opponentId) {
        if (completedSets < 3) {
            return false; // Need at least 3 sets to potentially win
        }

        // Calculate match points for each completed set
        int teamMatchPoints = 0;
        int opponentMatchPoints = 0;

        for (int set = 1; set <= completedSets; set++) {
            int teamSetPoints = calculateSetPoints(teamPasses.get((long) set));
            int opponentSetPoints = calculateSetPoints(opponentPasses.get((long) set));

            if (teamSetPoints > opponentSetPoints) {
                teamMatchPoints += 2;
            } else if (opponentSetPoints > teamSetPoints) {
                opponentMatchPoints += 2;
            } else {
                teamMatchPoints += 1;
                opponentMatchPoints += 1;
            }
        }

        // Match is won if either team has 6+ match points
        return teamMatchPoints >= 6 || opponentMatchPoints >= 6;
    }

    /**
     * Calculates total points for a set (sum of all arrows)
     */
    private int calculateSetPoints(List<PasseDO> setPasses) {
        if (setPasses == null) {
            return 0;
        }

        return setPasses.stream()
                .mapToInt(p -> {
                    int points = 0;
                    if (p.getPfeil1() != null) points += p.getPfeil1();
                    if (p.getPfeil2() != null) points += p.getPfeil2();
                    if (p.getPfeil3() != null) points += p.getPfeil3();
                    return points;
                })
                .sum();
    }

    /**
     * Determines the correct passe number for a given match and team
     */
    private int determineCorrectPasseNumber(long matchId, long teamId) {
        List<PasseDO> teamPasses = passeComponent.findByMannschaftMatchId(teamId, matchId);

        if (teamPasses.isEmpty()) {
            return 1; // No passes yet, start with passe 1
        }

        // Find the highest passe number with actual data (non-null arrows)
        int maxPasseWithData = teamPasses.stream()
                .filter(p -> p.getPfeil1() != null || p.getPfeil2() != null) // Has actual shot data
                .mapToInt(p -> Math.toIntExact(p.getPasseLfdnr()))
                .max()
                .orElse(0);

        // The current passe is the next one after the last completed passe
        return maxPasseWithData + 1;
    }

    /**
     * Find opponent by matching round & pairing.
     */
    private long findOpponentTeamId(MatchDO m, long own) {
        return matchComponent.findByWettkampfId(m.getWettkampfId()).stream()
                .filter(o ->
                        Objects.equals(o.getNr(), m.getNr()) &&
                                Objects.equals(o.getBegegnung(), m.getBegegnung()) &&
                                !Objects.equals(o.getMannschaftId(), own))
                .findFirst()
                .map(MatchDO::getMannschaftId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INTERNAL_ERROR,
                        "Opponent not found for match " + m.getId()));
    }

    // Data classes and enums

    /**
     * Result of a synchronization operation
     */
    public static class SyncResult {
        public final boolean success;
        public final String message;
        public final boolean dataWasUpdated;

        private SyncResult(boolean success, String message, boolean dataWasUpdated) {
            this.success = success;
            this.message = message;
            this.dataWasUpdated = dataWasUpdated;
        }

        public static SyncResult success(String message, boolean dataWasUpdated) {
            return new SyncResult(true, message, dataWasUpdated);
        }

        public static SyncResult failure(String message) {
            return new SyncResult(false, message, false);
        }
    }

    /**
     * Result of match validation
     */
    private static class MatchValidationResult {
        final boolean isValid;
        final String reason;

        MatchValidationResult(boolean isValid, String reason) {
            this.isValid = isValid;
            this.reason = reason;
        }
    }

    /**
     * Enum representing match status
     */
    private enum MatchStatus {
        NOT_STARTED,    // No passes recorded yet
        IN_PROGRESS,    // Some passes recorded, not complete
        COMPLETED,      // Match finished (5 sets or 6+ match points)
        INVALID         // Error in data or analysis
    }

    /**
     * Data class holding match analysis results
     */
    private static class MatchAnalysis {
        final MatchStatus status;
        final int completedSets;
        final int totalPasses;
        final int currentPasse;

        MatchAnalysis(MatchStatus status, int completedSets, int totalPasses, int currentPasse) {
            this.status = status;
            this.completedSets = completedSets;
            this.totalPasses = totalPasses;
            this.currentPasse = currentPasse;
        }
    }
}