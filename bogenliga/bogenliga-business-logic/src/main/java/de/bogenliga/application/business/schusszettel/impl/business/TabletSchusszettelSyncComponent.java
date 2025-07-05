package de.bogenliga.application.business.schusszettel.impl.business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final MatchAnalysisService matchAnalysisService;

    @Autowired
    public TabletSchusszettelSyncComponent(TabletSchusszettelDAO sessionDAO,
                                           MatchComponent matchComponent,
                                           PasseComponent passeComponent,
                                           DsbMannschaftComponent mannschaftComponent,
                                           MatchAnalysisService matchAnalysisService) {
        this.sessionDAO = sessionDAO;
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
        this.mannschaftComponent = mannschaftComponent;
        this.matchAnalysisService = matchAnalysisService;
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
                // Current match is still valid, but we should still check if status needs updating
                LOGGER.debug("Current match {} is still valid for team {}", session.getCurrentMatchId(), teamId);
                
                try {
                    // Don't override final statuses like WETTKAMPF_ENDE
                    if ("WETTKAMPF_ENDE".equals(session.getStatus())) {
                        LOGGER.debug("Session for team {} already at WETTKAMPF_ENDE, no status update needed", teamId);
                        return SyncResult.success("Session data is already synchronized", false);
                    }
                    
                    // Get current match for status determination
                    MatchDO currentMatch = matchComponent.findById(session.getCurrentMatchId());
                    if (currentMatch != null) {
                        // Determine correct passe number and status
                        int correctPasseNumber = determineCorrectPasseNumber(currentMatch.getId(), teamId);
                        String correctStatus = determineCorrectStatus(currentMatch, teamId, correctPasseNumber);
                        
                        // Check if status or passe number needs updating
                        boolean needsUpdate = !Objects.equals(session.getStatus(), correctStatus) || 
                                            !Objects.equals(session.getCurrentPasseNumber(), correctPasseNumber);
                        
                        if (needsUpdate && updateDatabase) {
                            session.setCurrentPasseNumber(correctPasseNumber);
                            session.setStatus(correctStatus);
                            sessionDAO.updateStatus(session, 0L);
                            LOGGER.info("Updated session status for team {} to {} passe {}", 
                                       teamId, correctStatus, correctPasseNumber);
                            return SyncResult.success("Session status synchronized", true);
                        } else if (needsUpdate) {
                            // Update in memory only
                            session.setCurrentPasseNumber(correctPasseNumber);
                            session.setStatus(correctStatus);
                            return SyncResult.success("Session status synchronized", true);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.warn("Error checking status for valid match {}: {}", session.getCurrentMatchId(), e.getMessage());
                }
                
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

            // Store original state for comparison
            long originalMatchId = session.getCurrentMatchId();
            String originalStatus = session.getStatus();

            // Update session with correct match data
            session.setCurrentMatchId(correctMatch.getId());
            session.setCurrentMatchNumber(Math.toIntExact(correctMatch.getNr()));

            // Find and set correct opponent
            long opponentTeamId = findOpponentTeamId(correctMatch, teamId);
            session.setGegnerTeamId(opponentTeamId);

            // Determine correct passe number based on existing data
            int correctPasseNumber = determineCorrectPasseNumber(correctMatch.getId(), teamId);
            session.setCurrentPasseNumber(correctPasseNumber);

            // CRITICAL FIX: Determine and set the correct status based on match analysis
            String correctStatus = determineCorrectStatus(correctMatch, teamId, correctPasseNumber);
            
            // Check if we need to advance due to match completion
            boolean shouldAdvance = shouldAdvanceToNextMatch(session, correctMatch, teamId, correctStatus);
            
            if (shouldAdvance && updateDatabase) {
                LOGGER.info("Match {} is completed for team {}, advancing to next match or ending competition", 
                           correctMatch.getId(), teamId);
                
                // Implement match advancement logic directly to avoid circular dependency
                // This handles both SCHUETZENMELDUNG (next match) and WETTKAMPF_ENDE (no more matches)
                try {
                    advanceToNextMatchOrEndInternal(session);
                    LOGGER.info("Successfully advanced team {} from match {} to next state", 
                               teamId, originalMatchId);
                } catch (Exception e) {
                    LOGGER.error("Failed to advance team {} from completed match {}: {}", 
                                teamId, originalMatchId, e.getMessage());
                    // Continue with status update even if advancement fails
                    session.setStatus(correctStatus);
                }
            } else {
                // Normal status update without advancement
                session.setStatus(correctStatus);
            }

            // Update the session in database if requested
            if (updateDatabase) {
                sessionDAO.updateStatus(session, 0L);
                LOGGER.info("Session database updated for team {} to match {} passe {} status {}",
                        teamId, session.getCurrentMatchId(), session.getCurrentPasseNumber(), session.getStatus());
            }

            return SyncResult.success(
                    "Session synchronized to match " + session.getCurrentMatchId() + " passe " + session.getCurrentPasseNumber(),
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
     * Made public for use by admin initialization.
     */
    public MatchDO findCurrentMatchForTeam(List<MatchDO> teamMatches, long teamId) {
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
     * Analyzes a single match to determine its current progress and status using shared service
     */
    private MatchAnalysis analyzeMatchProgress(MatchDO match, long teamId) {
        try {
            // Find opponent team ID for this match
            long opponentId;
            try {
                opponentId = findOpponentTeamId(match, teamId);
            } catch (Exception e) {
                LOGGER.warn("Could not find opponent for match {}: {}", match.getId(), e.getMessage());
                return new MatchAnalysis(MatchStatus.INVALID, 0, 0, 1);
            }

            // Use shared service for comprehensive analysis
            MatchAnalysisService.MatchAnalysisResult result = 
                matchAnalysisService.analyzeMatch(match.getId(), teamId, opponentId);
            
            // Convert to sync component's format
            MatchStatus status;
            switch (result.getStatus()) {
                case NOT_STARTED:
                    status = MatchStatus.NOT_STARTED;
                    break;
                case IN_PROGRESS:
                    status = MatchStatus.IN_PROGRESS;
                    break;
                case COMPLETED:
                    status = MatchStatus.COMPLETED;
                    break;
                case INVALID:
                default:
                    status = MatchStatus.INVALID;
                    break;
            }
            
            return new MatchAnalysis(status, result.getCompletedSets(), 
                                   result.getSatzErgebnisse().size(), result.getCurrentPasse());

        } catch (Exception e) {
            LOGGER.error("Error analyzing match progress for match {}, team {}: {}",
                    match.getId(), teamId, e.getMessage());
            return new MatchAnalysis(MatchStatus.INVALID, 0, 0, 1);
        }
    }

    /**
     * @deprecated Replaced by MatchAnalysisService - kept for reference only
     */
    @Deprecated
    private void placeholderDeprecatedMethod1() {
        // Placeholder for deprecated methods - removed
    }

    /**
     * @deprecated All calculation methods replaced by MatchAnalysisService
     */
    @Deprecated
    private void placeholderDeprecatedMethod2() {
        // Placeholder for deprecated methods - removed
    }

    /**
     * Determines the correct passe number for a given match and team using shared service.
     * Made public for use by admin initialization.
     */
    @Deprecated
    public int determineCorrectPasseNumber(long matchId, long teamId) {
        try {
            // Find opponent to use comprehensive analysis
            MatchDO match = matchComponent.findById(matchId);
            long opponentId = findOpponentTeamId(match, teamId);
            
            // Use shared service for accurate analysis
            return matchAnalysisService.getCurrentPasseNumber(matchId, teamId, opponentId);
            
        } catch (Exception e) {
            LOGGER.warn("Error determining correct passe number for match {} team {}: {}", 
                       matchId, teamId, e.getMessage());
            
            // Fallback to simple analysis
            List<PasseDO> teamPasses = passeComponent.findByMannschaftMatchId(teamId, matchId);
            if (teamPasses.isEmpty()) {
                return 1;
            }
            
            // Find highest passe with data + 1
            int maxPasse = teamPasses.stream()
                    .mapToInt(p -> Math.toIntExact(p.getPasseLfdnr()))
                    .max().orElse(0);
            
            return maxPasse + 1;
        }
    }

    /**
     * Find opponent by matching round & pairing.
     * Enhanced with better error handling and logging.
     */
    private long findOpponentTeamId(MatchDO m, long own) {
        try {
            return matchComponent.findByWettkampfId(m.getWettkampfId()).stream()
                    .filter(o ->
                            Objects.equals(o.getNr(), m.getNr()) &&
                                    Objects.equals(o.getBegegnung(), m.getBegegnung()) &&
                                    !Objects.equals(o.getMannschaftId(), own))
                    .findFirst()
                    .map(MatchDO::getMannschaftId)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.INTERNAL_ERROR,
                            "Opponent not found for match " + m.getId() + 
                            " (team=" + own + ", nr=" + m.getNr() + ", begegnung=" + m.getBegegnung() + ")"));
        } catch (Exception e) {
            LOGGER.error("Error finding opponent for team {} in match {}: {}", own, m.getId(), e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR,
                    "Failed to find opponent team: " + e.getMessage());
        }
    }

    /**
     * Validates that an arrow value is within the valid range (0-10)
     */
    private void validateArrowValue(Integer arrowValue, String arrowName) {
        if (arrowValue != null && (arrowValue < 0 || arrowValue > 10)) {
            LOGGER.warn("Invalid arrow value detected: {} = {} (outside 0-10 range)", arrowName, arrowValue);
            // In sync component, we log warnings but don't throw exceptions to avoid breaking sync
            // The main component validation will prevent such values from being saved
        }
    }

    /**
     * Checks if a set is complete according to official rules:
     * - Must have exactly 3 shooters (passes)
     * - Each shooter must have at least some shot data
     */
    private boolean isSetComplete(List<PasseDO> setPasses) {
        if (setPasses == null || setPasses.size() != 3) {
            return false; // Must have exactly 3 shooters per set
        }

        // Check that all 3 shooters have at least some shot data
        return setPasses.stream()
                .allMatch(p -> p.getPfeil1() != null || p.getPfeil2() != null || p.getPfeil3() != null);
    }

    /**
     * Enhanced validation for team roster composition
     * Checks that team has sufficient active shooters for competition
     */
    private boolean validateTeamComposition(long teamId) {
        try {
            // This would need access to MannschaftsmitgliedComponent
            // For now, we assume teams are valid if they exist
            DsbMannschaftDO team = mannschaftComponent.findById(teamId);
            return team != null;
        } catch (Exception e) {
            LOGGER.warn("Could not validate team composition for team {}: {}", teamId, e.getMessage());
            return false;
        }
    }

    /**
     * CRITICAL METHOD: Determines the correct status for a team based on match analysis
     * This is the missing piece that sets the actual team state!
     */
    private String determineCorrectStatus(MatchDO match, long teamId, int currentPasseNumber) {
        try {
            // Constants from the main component
            final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";
            final String STATUS_SATZEINGABE = "SATZEINGABE";
            final String STATUS_WARTE = "WARTE";
            final String STATUS_WETTKAMPF_ENDE = "WETTKAMPF_ENDE";

            // Analyze current match to determine proper status
            MatchAnalysis analysis = analyzeMatchProgress(match, teamId);

            // If match is completed, check if there are more matches
            if (analysis.status == MatchStatus.COMPLETED) {
                if (hasMoreMatches(match, teamId)) {
                    return STATUS_SCHUETZENMELDUNG; // Next match needs shooter registration
                } else {
                    return STATUS_WETTKAMPF_ENDE; // All matches completed
                }
            }

            // If match not started, need shooter registration
            if (analysis.status == MatchStatus.NOT_STARTED) {
                return STATUS_SCHUETZENMELDUNG;
            }

            // Match is in progress - determine if we're in SATZEINGABE or WARTE
            if (analysis.status == MatchStatus.IN_PROGRESS) {
                // Check if current passe has registered shooters
                if (!hasRegisteredShooters(match.getId(), teamId, currentPasseNumber)) {
                    return STATUS_SCHUETZENMELDUNG; // Need to register shooters first
                }

                // Check if current passe is completed by this team
                if (isPasseCompletedByTeam(match.getId(), teamId, currentPasseNumber)) {
                    try {
                        // Check if opponent is also done with this passe
                        long opponentId = findOpponentTeamId(match, teamId);
                        if (isPasseCompletedByTeam(match.getId(), opponentId, currentPasseNumber)) {
                            // Both teams done - advance or complete match
                            return STATUS_SATZEINGABE; // Ready for next passe
                        } else {
                            return STATUS_WARTE; // Wait for opponent
                        }
                    } catch (Exception e) {
                        LOGGER.warn("Could not check opponent status for team {} match {}, defaulting to SATZEINGABE: {}", 
                                   teamId, match.getId(), e.getMessage());
                        return STATUS_SATZEINGABE; // Safe fallback when opponent lookup fails
                    }
                } else {
                    return STATUS_SATZEINGABE; // Current passe not completed
                }
            }

            // Fallback - if we can't determine, assume SATZEINGABE
            LOGGER.warn("Could not determine correct status for team {} match {}, defaulting to SATZEINGABE", 
                       teamId, match.getId());
            return STATUS_SATZEINGABE;

        } catch (Exception e) {
            LOGGER.error("Error determining correct status for team {} match {}: {}", 
                        teamId, match.getId(), e.getMessage());
            return "SATZEINGABE"; // Safe fallback
        }
    }

    /**
     * Checks if team has more matches after the current one
     */
    private boolean hasMoreMatches(MatchDO currentMatch, long teamId) {
        try {
            List<MatchDO> teamMatches = matchComponent.findByWettkampfId(currentMatch.getWettkampfId()).stream()
                    .filter(m -> Objects.equals(m.getMannschaftId(), teamId))
                    .sorted(Comparator.comparingLong(MatchDO::getNr))
                    .toList();

            // Check if there are matches with higher numbers
            return teamMatches.stream()
                    .anyMatch(m -> m.getNr() > currentMatch.getNr());
        } catch (Exception e) {
            LOGGER.warn("Error checking for more matches: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks if team has registered shooters for a specific passe
     */
    private boolean hasRegisteredShooters(long matchId, long teamId, int passeNumber) {
        try {
            List<PasseDO> passes = passeComponent.findByMannschaftMatchId(teamId, matchId).stream()
                    .filter(p -> p.getPasseLfdnr() == passeNumber)
                    .toList();
            
            // Should have exactly 3 registered shooters (even if no scores yet)
            return passes.size() == 3;
        } catch (Exception e) {
            LOGGER.warn("Error checking registered shooters: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a team has completed a specific passe (all shooters have scores)
     */
    private boolean isPasseCompletedByTeam(long matchId, long teamId, int passeNumber) {
        try {
            List<PasseDO> passes = passeComponent.findByMannschaftMatchId(teamId, matchId).stream()
                    .filter(p -> p.getPasseLfdnr() == passeNumber)
                    .toList();
            
            if (passes.size() != 3) {
                return false; // Must have exactly 3 shooters
            }

            // All 3 shooters must have at least some shot data
            return passes.stream()
                    .allMatch(p -> p.getPfeil1() != null || p.getPfeil2() != null);
        } catch (Exception e) {
            LOGGER.warn("Error checking passe completion: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Determines if a team should advance to the next match based on match completion analysis.
     * Only advances when there's a genuine state mismatch to ensure idempotent behavior.
     */
    private boolean shouldAdvanceToNextMatch(TabletSchusszettelEntity session, MatchDO currentMatch, 
                                           long teamId, String calculatedStatus) {
        try {
            // Analyze current match progress
            MatchAnalysis analysis = analyzeMatchProgress(currentMatch, teamId);
            
            // Only advance if the current match is actually completed
            if (analysis.status != MatchStatus.COMPLETED) {
                LOGGER.debug("Match {} for team {} is not completed (status: {}), no advancement needed", 
                           currentMatch.getId(), teamId, analysis.status);
                return false;
            }
            
            // Check if session is stuck on a completed match (state mismatch)
            // This prevents multiple advancements from repeated sync calls
            boolean isStuckOnCompletedMatch = session.getCurrentMatchId().equals(currentMatch.getId()) && 
                                            (!"WETTKAMPF_ENDE".equals(session.getStatus()));
            
            if (!isStuckOnCompletedMatch) {
                LOGGER.debug("Team {} is not stuck on completed match {}, no advancement needed", 
                           teamId, currentMatch.getId());
                return false;
            }
            
            LOGGER.info("Team {} is stuck on completed match {} with status {}, advancement required", 
                       teamId, currentMatch.getId(), session.getStatus());
            return true;
            
        } catch (Exception e) {
            LOGGER.error("Error determining if team {} should advance from match {}: {}", 
                        teamId, currentMatch.getId(), e.getMessage());
            return false; // Default to no advancement on errors
        }
    }

    /**
     * Internal method to advance a team session to the next match or end the competition.
     * This is a copy of the logic from TabletSchusszettelComponentImpl to avoid circular dependency.
     */
    private void advanceToNextMatchOrEndInternal(TabletSchusszettelEntity session) {
        // Status constants (matching main component)
        final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";
        final String STATUS_WETTKAMPF_ENDE = "WETTKAMPF_ENDE";
        
        long wettkampfId = session.getWettkampfId();
        long teamId = session.getTeamId();
        
        // 1) Load all this team's matches, sorted by Nr
        List<MatchDO> teamMatches = matchComponent.findByWettkampfId(wettkampfId).stream()
                .filter(m -> Objects.equals(m.getMannschaftId(), teamId))
                .sorted(Comparator.comparingLong(MatchDO::getNr))
                .toList();

        // 2) Find index of the current match
        OptionalInt currentIdx = IntStream.range(0, teamMatches.size())
                .filter(i -> Objects.equals(teamMatches.get(i).getId(), session.getCurrentMatchId()))
                .findFirst();

        if (!currentIdx.isPresent()) {
            throw new BusinessException(
                    ErrorCode.INTERNAL_ERROR,
                    "Current match not found for team " + teamId + " in wettkampf " + wettkampfId);
        }

        int idx = currentIdx.getAsInt();
        if (idx + 1 < teamMatches.size()) {
            // Advance to the next match
            MatchDO next = teamMatches.get(idx + 1);
            session.setCurrentMatchId(next.getId());
            session.setCurrentMatchNumber(Math.toIntExact(next.getNr()));
            session.setCurrentPasseNumber(1);

            // Recompute opponent
            long opponentTeamId = findOpponentTeamId(next, teamId);
            session.setGegnerTeamId(opponentTeamId);

            session.setStatus(STATUS_SCHUETZENMELDUNG);
            LOGGER.info("Advanced team {} to next match {} (Nr: {})", teamId, next.getId(), next.getNr());
        } else {
            // No more matches → end of competition
            session.setStatus(STATUS_WETTKAMPF_ENDE);
            LOGGER.info("Team {} has completed all matches, setting status to WETTKAMPF_ENDE", teamId);
        }

        sessionDAO.updateStatus(session, 0L);
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