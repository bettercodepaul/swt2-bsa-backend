package de.bogenliga.application.business.schusszettel.impl.business.domain;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.schusszettel.impl.business.MatchAnalysisService;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * State machine implementation for Tablet Schusszettel session state transitions.
 * 
 * <h2>FUNCTIONALITY</h2>
 * Encapsulates state transition logic for archery competition sessions. Database serves
 * as authoritative state source through TabletSchusszettelEntity. Provides domain layer
 * abstraction over persistent session state.
 * 
 * <h2>STATE MACHINE</h2>
 * 
 * <h3>States:</h3>
 * <ul>
 *   <li>SCHUETZENMELDUNG: Shooter registration required</li>
 *   <li>SATZEINGABE: Score entry in progress</li> 
 *   <li>WARTE: Waiting for opponent team completion</li>
 *   <li>WETTKAMPF_ENDE: Competition finished</li>
 * </ul>
 * 
 * <h3>Transition Methods:</h3>
 * <ul>
 *   <li>nudgeAlong(): Simple forward transitions after POST operations</li>
 *   <li>evaluateWithOpponent(): WARTE state resolution with opponent synchronization</li>
 * </ul>
 * 
 * <h2>TRANSITION LOGIC</h2>
 * 
 * <h3>Simple Transitions (nudgeAlong):</h3>
 * <pre>
 * SCHUETZENMELDUNG → SATZEINGABE  (after shooter registration)
 * SATZEINGABE      → WARTE        (after score submission)
 * </pre>
 * 
 * <h3>WARTE Resolution (evaluateWithOpponent):</h3>
 * <pre>
 * if (both teams in WARTE state) {
 *     if (current match complete using MatchAnalysisService) {
 *         if (hasMoreMatches() using LigamatchBE.naechsteMatchId) {
 *             → advanceToNextMatch() using LigamatchBE progression
 *             → reset to SCHUETZENMELDUNG
 *         } else {
 *             → set WETTKAMPF_ENDE
 *         }
 *     } else {
 *         → increment passe number
 *         → set SATZEINGABE
 *     }
 * }
 * </pre>
 * 
 * <h2>DESYNCHRONIZATION HANDLING</h2>
 * 
 * <h3>Recovery Scenarios:</h3>
 * <ul>
 *   <li>Missing opponent session: Allows unilateral progression</li>
 *   <li>Opponent in different state: Allows progression when team ahead</li>
 *   <li>Passe number mismatch: Synchronizes to higher passe number</li>
 *   <li>State inconsistencies: Continues with fallback behavior and logging</li>
 * </ul>
 * 
 * <h2>DATABASE PERSISTENCE</h2>
 * 
 * <h3>Persistence Model:</h3>
 * <ul>
 *   <li>All state changes immediately persisted via TabletSchusszettelDAO</li>
 *   <li>State transitions are atomic database operations</li>
 *   <li>Database state takes precedence over memory state</li>
 * </ul>
 * 
 * <h3>Data Dependencies:</h3>
 * <ul>
 *   <li>TabletSchusszettelEntity: Session state (managed by this class)</li>
 *   <li>LigamatchBE: Match progression data (read-only)</li>
 *   <li>PasseDO: Score analysis data (read-only)</li>
 * </ul>
 * 
 * <h2>MATCH PROGRESSION</h2>
 * 
 * <h3>Rules Implementation:</h3>
 * <ul>
 *   <li>Match completion via MatchAnalysisService using LigamatchBE</li>
 *   <li>Match advancement via LigamatchBE.naechsteMatchId field</li>
 *   <li>Pass numbering: 1-5 per match</li>
 * </ul>
 * 
 * <h3>Synchronization Requirements:</h3>
 * <ul>
 *   <li>Both teams must reach WARTE state before advancement</li>
 *   <li>Exception: progression allowed when opponent unavailable</li>
 *   <li>Pass number synchronization maintains team coordination</li>
 * </ul>
 * 
 * <h2>COMPONENT DEPENDENCIES</h2>
 * <ul>
 *   <li>TabletSchusszettelDAO: Database persistence operations</li>
 *   <li>MatchComponent: LigamatchBE queries and match progression</li>
 *   <li>PasseComponent: Pass data analysis</li>
 *   <li>MatchAnalysisService: Match completion logic using LigamatchBE</li>
 * </ul>
 * 
 * <h2>USAGE PATTERN</h2>
 * <pre>
 * // Load from database
 * SessionRuntime runtime = SessionRuntime.loadFromDatabase(...);
 * 
 * // Simple state advancement
 * runtime.nudgeAlong();
 * 
 * // Opponent-aware evaluation
 * TabletSchusszettelEntity opponent = dao.findByWettkampfUndTeam(...);
 * boolean advanced = runtime.evaluateWithOpponent(opponent);
 * </pre>
 * 
 * <h2>THREAD SAFETY</h2>
 * <ul>
 *   <li>Stateless method design - no shared mutable state</li>
 *   <li>Database handles concurrent access via locking</li>
 *   <li>Instance variables immutable after construction</li>
 * </ul>
 * 
 * @author Marty Lauterbach
 * @version 3.0 - Optimized with LigamatchBE integration for match progression
 * @version 2.0 - Enhanced desynchronization recovery
 * @since 1.0 - Initial state machine implementation
 */
public class SessionRuntime {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionRuntime.class);
    
    // State constants - public for use across schusszettel package
    public static final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";
    public static final String STATUS_SATZEINGABE = "SATZEINGABE";
    public static final String STATUS_WARTE = "WARTE";
    public static final String STATUS_WETTKAMPF_ENDE = "WETTKAMPF_ENDE";
    
    private final TabletSchusszettelEntity session;
    private final TabletSchusszettelDAO sessionDAO;
    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final MatchAnalysisService matchAnalysisService;
    
    public SessionRuntime(TabletSchusszettelEntity session,
                         TabletSchusszettelDAO sessionDAO,
                         MatchComponent matchComponent,
                         PasseComponent passeComponent,
                         MatchAnalysisService matchAnalysisService) {
        this.session = session;
        this.sessionDAO = sessionDAO;
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
        this.matchAnalysisService = matchAnalysisService;
    }
    
    /**
     * Get current state from the session
     */
    public String getCurrentState() {
        return session.getStatus();
    }
    
    /**
     * Get the wrapped session entity
     */
    public TabletSchusszettelEntity getSession() {
        return session;
    }
    
    /**
     * Advance state based on current conditions.
     * This is called after successful POST operations.
     */
    public void nudgeAlong() {
        String currentState = session.getStatus();
        
        switch (currentState) {
            case STATUS_SCHUETZENMELDUNG:
                // After shooter registration → SATZEINGABE
                session.setStatus(STATUS_SATZEINGABE);
                break;
                
            case STATUS_SATZEINGABE:
                // After score submission → WARTE
                session.setStatus(STATUS_WARTE);
                break;
                
            case STATUS_WARTE:
                // This state requires checking opponent
                // Handle in evaluateWithOpponent()
                break;
                
            case STATUS_WETTKAMPF_ENDE:
                // Final state - no transitions
                break;
        }
        
        // Save to database
        sessionDAO.updateStatus(session, 0L);
    }
    
    /**
     * Evaluate state considering opponent's state.
     * This is the core logic for WARTE resolution with enhanced backwards compatibility.
     * 
     * ROBUSTNESS IMPROVEMENTS:
     * - Handles desynchronized passe numbers between teams
     * - Allows progression even when opponent is behind
     * - Maintains backwards compatibility for edge cases
     * 
     * @param opponentSession The opponent's session (may be null)
     * @return true if state was changed
     */
    public boolean evaluateWithOpponent(TabletSchusszettelEntity opponentSession) {
        // Only process if we're in WARTE
        if (!STATUS_WARTE.equals(session.getStatus())) {
            return false;
        }
        
        LOGGER.info("Evaluating WARTE state for team {} (passe {}) with opponent {} (status: {}, passe: {})", 
                    session.getTeamId(), session.getCurrentPasseNumber(),
                    opponentSession != null ? opponentSession.getTeamId() : "null",
                    opponentSession != null ? opponentSession.getStatus() : "null",
                    opponentSession != null ? opponentSession.getCurrentPasseNumber() : "null");
        
        try {
            // EDGE CASE HANDLING: Opponent missing or in different state | Can be run with either user or admin gets
            if (opponentSession == null) {
                LOGGER.warn("EDGE CASE: Opponent session not found for team {} - allowing unilateral progression", 
                           session.getTeamId());
                return attemptStateProgression("No opponent session found");
            }
            
            // EDGE CASE HANDLING: Opponent in different state - check if we should wait or progress
            if (!STATUS_WARTE.equals(opponentSession.getStatus())) {
                // If opponent is behind (SATZEINGABE) and we're ahead, allow progression
                if (STATUS_SATZEINGABE.equals(opponentSession.getStatus()) && 
                    session.getCurrentPasseNumber() > opponentSession.getCurrentPasseNumber()) {
                    
                    LOGGER.warn("EDGE CASE: Team {} ahead of opponent {} - allowing progression (backwards compatibility)", 
                               session.getTeamId(), opponentSession.getTeamId());
                    return attemptStateProgression("Opponent behind in passe progression");
                }
                
                // If opponent is ahead, wait for synchronization
                if (opponentSession.getCurrentPasseNumber() > session.getCurrentPasseNumber()) {
                    LOGGER.info("Team {} waiting - opponent {} is ahead (passe {} vs {})", 
                               session.getTeamId(), opponentSession.getTeamId(),
                               session.getCurrentPasseNumber(), opponentSession.getCurrentPasseNumber());
                    return false;
                }
                
                // Default: wait for opponent to reach WARTE
                LOGGER.info("Team {} waiting for opponent {} to reach WARTE state", 
                           session.getTeamId(), opponentSession.getTeamId());
                return false;
            }
            
            // STANDARD CASE: Both teams in WARTE - check passe synchronization
            if (session.getCurrentPasseNumber() != opponentSession.getCurrentPasseNumber()) {
                LOGGER.warn("DESYNC DETECTED: Team {} passe {} vs opponent {} passe {} - synchronizing", 
                           session.getTeamId(), session.getCurrentPasseNumber(),
                           opponentSession.getTeamId(), opponentSession.getCurrentPasseNumber());
                
                // Use the higher passe number for both teams (safer progression)
                int syncedPasse = Math.max(session.getCurrentPasseNumber(), opponentSession.getCurrentPasseNumber());
                session.setCurrentPasseNumber(syncedPasse);
                LOGGER.info("Synchronized team {} to passe {}", session.getTeamId(), syncedPasse);
            }
            
            LOGGER.info("Both teams in WARTE and synchronized - evaluating next state for team {} and opponent {}", 
                        session.getTeamId(), opponentSession.getTeamId());
            
            return attemptStateProgression("Both teams ready for progression");
            
        } catch (Exception e) {
            LOGGER.error("Error evaluating state for team {}: {}", session.getTeamId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Helper method to attempt state progression with consistent logic.
     * Extracted for reuse across different edge case scenarios.
     */
    private boolean attemptStateProgression(String reason) {
        try {
            // Check if current match is complete
            boolean matchComplete = isMatchComplete();
            
            if (matchComplete) {
                LOGGER.info("Match {} complete for team {} - {}", session.getCurrentMatchId(), session.getTeamId(), reason);
                
                // Check if there are more matches
                if (hasMoreMatches()) {
                    // Advance to next match
                    advanceToNextMatch();
                    LOGGER.info("Advanced team {} to next match", session.getTeamId());
                } else {
                    // No more matches - end competition
                    session.setStatus(STATUS_WETTKAMPF_ENDE);
                    LOGGER.info("Competition ended for team {}", session.getTeamId());
                }
            } else {
                // Match not complete - advance to next passe
                int nextPasse = session.getCurrentPasseNumber() + 1;
                session.setCurrentPasseNumber(nextPasse);
                session.setStatus(STATUS_SATZEINGABE);
                LOGGER.info("Advanced team {} to passe {} - {}", session.getTeamId(), nextPasse, reason);
            }
            
            // Save to database
            sessionDAO.updateStatus(session, 0L);
            return true;
            
        } catch (Exception e) {
            LOGGER.error("Error in state progression for team {}: {}", session.getTeamId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if the current match is complete.
     * A match is complete when:
     * - 5 sets have been played, OR
     * - One team has 6 or more match points
     */
    private boolean isMatchComplete() {
        try {
            long matchId = session.getCurrentMatchId();
            long teamId = session.getTeamId();
            long opponentId = session.getGegnerTeamId();
            
            // Use the match analysis service
            return matchAnalysisService.isMatchComplete(matchId, teamId, opponentId);
            
        } catch (Exception e) {
            LOGGER.error("Error checking match completion: {}", e.getMessage());
            // Fallback: check if we've played 5 passes
            return session.getCurrentPasseNumber() >= 5;
        }
    }
    
    /**
     * OPTIMIZED: Check if there are more matches using LigamatchBE naechsteMatchId.
     * Much simpler than complex team match filtering.
     */
    private boolean hasMoreMatches() {
        try {
            long currentMatchId = session.getCurrentMatchId();
            
            // Get current match from LigamatchBE
            LigamatchBE currentMatch = matchComponent.getLigamatchById(currentMatchId);
            if (currentMatch == null) {
                LOGGER.warn("Current match {} not found in LigamatchBE", currentMatchId);
                return false;
            }
            
            // Simply check if naechsteMatchId exists
            boolean hasNext = currentMatch.getNaechsteMatchId() != null;
            LOGGER.debug("Match {} has more matches: {} (naechsteMatchId: {})", 
                        currentMatchId, hasNext, currentMatch.getNaechsteMatchId());
            
            return hasNext;
                    
        } catch (Exception e) {
            LOGGER.error("Error checking for more matches: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * OPTIMIZED: Advance to the next match using LigamatchBE naechsteMatchId.
     * Much simpler than complex match list analysis.
     */
    private void advanceToNextMatch() {
        try {
            long currentMatchId = session.getCurrentMatchId();
            long teamId = session.getTeamId();
            
            // Get current match from LigamatchBE with built-in progression data
            LigamatchBE currentMatch = matchComponent.getLigamatchById(currentMatchId);
            if (currentMatch == null) {
                throw new BusinessException(ErrorCode.INTERNAL_ERROR, 
                    "Current match " + currentMatchId + " not found in LigamatchBE");
            }
            
            // Use built-in naechsteMatchId field for progression
            if (currentMatch.getNaechsteMatchId() != null) {
                LigamatchBE nextMatch = matchComponent.getLigamatchById(currentMatch.getNaechsteMatchId());
                if (nextMatch != null) {
                    // Update session with next match data
                    session.setCurrentMatchId(nextMatch.getMatchId());
                    session.setCurrentMatchNumber(Math.toIntExact(nextMatch.getMatchNr()));
                    session.setCurrentPasseNumber(1);
                    session.setStatus(STATUS_SCHUETZENMELDUNG);
                    
                    // Find and set opponent using LigamatchBE optimized method
                    try {
                        long opponentId = matchAnalysisService.findOpponentTeamId(nextMatch.getMatchId(), teamId);
                        session.setGegnerTeamId(opponentId);
                        LOGGER.info("Advanced team {} from match {} to match {} (opponent: {})", 
                                   teamId, currentMatchId, nextMatch.getMatchId(), opponentId);
                    } catch (Exception e) {
                        LOGGER.warn("Could not find opponent for next match {}: {}", nextMatch.getMatchId(), e.getMessage());
                    }
                } else {
                    LOGGER.warn("Next match {} referenced by current match {} not found", 
                               currentMatch.getNaechsteMatchId(), currentMatchId);
                }
            } else {
                LOGGER.info("No next match available for current match {} (naechsteMatchId is null)", currentMatchId);
            }
            
        } catch (Exception e) {
            LOGGER.error("Error advancing to next match from {}: {}", session.getCurrentMatchId(), e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to advance to next match");
        }
    }
    
    /**
     * Static factory method to create SessionRuntime from database
     */
    public static SessionRuntime loadFromDatabase(long wettkampfId, long teamId, String token,
                                                  TabletSchusszettelDAO sessionDAO,
                                                  MatchComponent matchComponent,
                                                  PasseComponent passeComponent,
                                                  MatchAnalysisService matchAnalysisService) {
        TabletSchusszettelEntity session = sessionDAO
                .findByTokenWettkampfUndTeam(wettkampfId, teamId, token)
                .orElse(null);
                
        if (session == null) {
            return null;
        }
        
        return new SessionRuntime(session, sessionDAO, matchComponent, passeComponent, matchAnalysisService);
    }
}