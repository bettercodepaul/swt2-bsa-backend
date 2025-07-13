package de.bogenliga.application.business.schusszettel.impl.business.domain;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.schusszettel.impl.business.domain.states.State;
import de.bogenliga.application.business.schusszettel.impl.business.domain.states.StateContext;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session runtime - domain entity for tablet session state management with state pattern delegation.
 * 
 * <h2>CURRENT ARCHITECTURE</h2>
 * This class is the ONLY domain entity allowed to access TabletSchusszettelEntity and DAO.
 * It coordinates session state transitions by delegating complex logic to state objects
 * while maintaining session lifecycle and opponent synchronization.
 * 
 * <h2>CURRENT RESPONSIBILITIES</h2>
 * <ul>
 *   <li>Session entity access and persistence (exclusive responsibility)</li>
 *   <li>State machine coordination with state pattern delegation</li>
 *   <li>Opponent synchronization in WARTE state</li>
 *   <li>Session initialization and database self-correction</li>
 * </ul>
 * 
 * <h2>STATE PATTERN INTEGRATION</h2>
 * <ul>
 *   <li>Simple transitions: nudgeAlong() with state object validation</li>
 *   <li>Complex logic: Delegates to state.handleWarteEvaluation()</li>
 *   <li>State objects handle all business rules and transitions</li>
 *   <li>SessionRuntime provides infrastructure access via StateContext</li>
 * </ul>
 * 
 * <h2>STATE TRANSITIONS</h2>
 * <ul>
 *   <li>SCHUETZENMELDUNG → SATZEINGABE (after shooter registration)</li>
 *   <li>SATZEINGABE → WARTE (after score submission)</li>
 *   <li>WARTE → SATZEINGABE|SCHUETZENMELDUNG|WETTKAMPF_ENDE (via state objects)</li>
 * </ul>
 * 
 * <h2>OPPONENT SYNCHRONIZATION</h2>
 * <ul>
 *   <li>Primary gate: POST operations trigger WARTE evaluation</li>
 *   <li>Secondary gate: GET requests handle desync recovery</li>
 *   <li>Complex logic delegated to Warte state object</li>
 *   <li>Graceful handling of missing opponents</li>
 * </ul>
 * 
 * <h2>EXTERNAL DATA ACCESS</h2>
 * <ul>
 *   <li>ALL external data access via MatchAnalysisService</li>
 *   <li>Match completion, opponent resolution, pass tracking</li>
 *   <li>Session self-correction based on database changes</li>
 * </ul>
 * 
 * <h2>USAGE PATTERNS</h2>
 * <ul>
 *   <li>ComponentImpl creates via loadFromDatabase() or initializeSession()</li>
 *   <li>AdminComponentImpl uses for session initialization</li>
 *   <li>State objects access via StateContext, not directly</li>
 * </ul>
 * 
 * @author Marty Lauterbach - State pattern implementation
 */
public class SessionRuntime {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionRuntime.class);
    
    // State constants - public for use across schusszettel package
    public static final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";
    public static final String STATUS_SATZEINGABE = "SATZEINGABE";
    public static final String STATUS_WARTE = "WARTE";
    public static final String STATUS_MATCH_ENDE = "MATCH_ENDE";
    public static final String STATUS_WETTKAMPF_ENDE = "WETTKAMPF_ENDE";
    
    private final TabletSchusszettelEntity session;
    private final TabletSchusszettelDAO sessionDAO;
    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final MatchAnalysisService matchAnalysisService;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    
    public SessionRuntime(TabletSchusszettelEntity session,
                         TabletSchusszettelDAO sessionDAO,
                         MatchComponent matchComponent,
                         PasseComponent passeComponent,
                         MatchAnalysisService matchAnalysisService,
                         MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                         DsbMitgliedComponent dsbMitgliedComponent,
                         WettkampfComponent wettkampfComponent,
                         VeranstaltungComponent veranstaltungComponent) {
        this.session = session;
        this.sessionDAO = sessionDAO;
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
        this.matchAnalysisService = matchAnalysisService;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
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
     * Simple state transitions after POST operations with state pattern validation.
     */
    public void nudgeAlong() {
        String currentState = session.getStatus();
        
        try {
            // Check if state allows simple nudging
            State stateObject = State.fromString(currentState);
            if (!stateObject.canNudgeAlong()) {
                LOGGER.debug("State {} does not allow simple nudging for team {}", currentState, session.getTeamId());
                return;
            }
            
            // Simple state transitions (complex logic handled in state objects)
            switch (currentState) {
                case STATUS_SCHUETZENMELDUNG:
                    // After shooter registration → SATZEINGABE
                    session.setStatus(STATUS_SATZEINGABE);
                    persistSession(); // DATABASE PERSISTENCE CRITICAL!
                    LOGGER.debug("Team {} advanced: SCHUETZENMELDUNG → SATZEINGABE", session.getTeamId());
                    break;
                    
                case STATUS_SATZEINGABE:
                    // After score submission → WARTE
                    session.setStatus(STATUS_WARTE);
                    persistSession(); // DATABASE PERSISTENCE CRITICAL!
                    LOGGER.debug("Team {} advanced: SATZEINGABE → WARTE", session.getTeamId());
                    break;
                    
                case STATUS_WARTE:
                    // This state requires opponent evaluation, not simple nudging
                    LOGGER.debug("WARTE state for team {} requires opponent evaluation", session.getTeamId());
                    break;
                    
                case STATUS_MATCH_ENDE:
                    // This state requires evaluation for next match progression, not simple nudging
                    LOGGER.debug("MATCH_ENDE state for team {} requires evaluation for next match progression", session.getTeamId());
                    break;
                    
                case STATUS_WETTKAMPF_ENDE:
                    // Final state - no transitions
                    LOGGER.debug("Team {} already in final state WETTKAMPF_ENDE", session.getTeamId());
                    break;
                    
                default:
                    LOGGER.warn("Unknown state '{}' for team {}", currentState, session.getTeamId());
                    break;
            }
            
            // Save to database
            sessionDAO.updateStatus(session, 0L);
            
        } catch (Exception e) {
            LOGGER.error("Error in nudgeAlong for team {}: {}", session.getTeamId(), e.getMessage());
        }
    }
    
    /**
     * State evaluation with opponent synchronization via state pattern delegation.
     * Handles both WARTE and MATCH_ENDE states that require evaluation.
     * 
     * @param opponentSession The opponent's session (may be null)
     * @return true if state was changed
     */
    public boolean evaluateWithOpponentWAITstate(TabletSchusszettelEntity opponentSession) {
        // Only process if we're in a state that requires evaluation
        if (!STATUS_WARTE.equals(session.getStatus()) && !STATUS_MATCH_ENDE.equals(session.getStatus())) {
            return false;
        }
        
        try {
            // Delegate to state object for complex WARTE logic
            State currentState = State.fromString(session.getStatus());
            StateContext context = new StateContext(session, this, matchComponent, passeComponent, matchAnalysisService, mannschaftsmitgliedComponent, dsbMitgliedComponent, wettkampfComponent, veranstaltungComponent);
            
            // Let the state object handle the complex opponent evaluation
            return currentState.handleWarteEvaluation(context, opponentSession);
            
        } catch (Exception e) {
            LOGGER.error("Error in state pattern delegation for team {}: {}", session.getTeamId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Advance to the next match using tournament-aware progression logic.
     * This bypasses the flawed database naechsteMatchId calculation and uses 
     * tournament bracket structure for correct advancement.
     */
    void advanceToNextMatch() {
        try {
            long currentMatchId = session.getCurrentMatchId();
            long teamId = session.getTeamId();
            
            // Use MatchAnalysisService tournament-aware progression instead of database naechsteMatchId
            LigamatchBE nextMatch = matchAnalysisService.findCorrectNextMatch(currentMatchId, teamId);
            
            if (nextMatch != null) {
                // Update session with next match data
                session.setCurrentMatchId(nextMatch.getMatchId());
                session.setCurrentMatchNumber(Math.toIntExact(nextMatch.getMatchNr()));
                session.setCurrentPasseNumber(1);
                session.setStatus(STATUS_SCHUETZENMELDUNG);
                
                // Find and set opponent using tournament-aware logic
                try {
                    long opponentId = matchAnalysisService.findOpponentTeamId(nextMatch.getMatchId(), teamId);
                    session.setGegnerTeamId(opponentId);
                    LOGGER.info("Advanced team {} to next match {} (opponent: {}) using tournament progression", 
                               teamId, nextMatch.getMatchId(), opponentId);
                } catch (Exception e) {
                    LOGGER.warn("Could not find opponent for next match {}: {}", nextMatch.getMatchId(), e.getMessage());
                }
            } else {
                // No next match available - tournament complete for this team
                session.setCurrentPasseNumber(5);
                session.setStatus(STATUS_WETTKAMPF_ENDE);
                persistSession(); // DATABASE PERSISTENCE CRITICAL!
                LOGGER.info("Team {} completed all matches - setting to WETTKAMPF_ENDE using tournament progression", teamId);
            }
            
        } catch (Exception e) {
            LOGGER.error("Error advancing to next match using tournament progression: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to advance to next match");
        }
    }

    /**
     * Static factory method to create SessionRuntime from database using token
     */
    public static SessionRuntime loadFromDatabase(long wettkampfId, long teamId, String token,
                                                  TabletSchusszettelDAO sessionDAO,
                                                  MatchComponent matchComponent,
                                                  PasseComponent passeComponent,
                                                  MatchAnalysisService matchAnalysisService,
                                                  MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                                  DsbMitgliedComponent dsbMitgliedComponent,
                                                  WettkampfComponent wettkampfComponent,
                                                  VeranstaltungComponent veranstaltungComponent) {
        TabletSchusszettelEntity session = sessionDAO
                .findByTokenWettkampfUndTeam(wettkampfId, teamId, token)
                .orElse(null);
                
        if (session == null) {
            return null;
        }
        
        return new SessionRuntime(session, sessionDAO, matchComponent, passeComponent, matchAnalysisService, mannschaftsmitgliedComponent, dsbMitgliedComponent, wettkampfComponent, veranstaltungComponent);
    }
    
    /**
     * Static factory method to create SessionRuntime from existing session entity
     */
    public static SessionRuntime fromSession(TabletSchusszettelEntity session,
                                           TabletSchusszettelDAO sessionDAO,
                                           MatchComponent matchComponent,
                                           PasseComponent passeComponent,
                                           MatchAnalysisService matchAnalysisService,
                                           MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                           DsbMitgliedComponent dsbMitgliedComponent,
                                           WettkampfComponent wettkampfComponent,
                                           VeranstaltungComponent veranstaltungComponent) {
        return new SessionRuntime(session, sessionDAO, matchComponent, passeComponent, matchAnalysisService, mannschaftsmitgliedComponent, dsbMitgliedComponent, wettkampfComponent, veranstaltungComponent);
    }

    /**
     * Load opponent session for this session
     */
    public SessionRuntime loadOpponentSession() {
        if (session.getGegnerTeamId() == null || session.getGegnerTeamId() == 0L) {
            return null;
        }
        
        TabletSchusszettelEntity opponentSession = sessionDAO
                .findByWettkampfUndTeam(session.getWettkampfId(), session.getGegnerTeamId())
                .orElse(null);
                
        if (opponentSession == null) {
            return null;
        }
        
        return new SessionRuntime(opponentSession, sessionDAO, matchComponent, passeComponent, matchAnalysisService, mannschaftsmitgliedComponent, dsbMitgliedComponent, wettkampfComponent, veranstaltungComponent);
    }
    
    /**
     * Initializes new session with MatchAnalysisService state determination.
     */
    public static SessionRuntime initializeSession(long wettkampfId, long teamId, String token,
                                                 TabletSchusszettelDAO sessionDAO,
                                                 MatchComponent matchComponent,
                                                 PasseComponent passeComponent,
                                                 MatchAnalysisService matchAnalysisService,
                                                 MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                                 DsbMitgliedComponent dsbMitgliedComponent,
                                                 WettkampfComponent wettkampfComponent,
                                                 VeranstaltungComponent veranstaltungComponent) {
        // Create new session entity
        TabletSchusszettelEntity session = new TabletSchusszettelEntity();
        session.setWettkampfId(wettkampfId);
        session.setTeamId(teamId);
        session.setToken(token);
        
        // Use MatchAnalysisService to determine initial state
        try {
            LOGGER.info("INITIALIZING SESSION for team {} in wettkampf {}", teamId, wettkampfId);
            
            // Find current incomplete match for team - this is the single source of truth
            LigamatchBE currentMatch = matchAnalysisService.findCurrentIncompleteMatch(wettkampfId, teamId);
            
            if (currentMatch == null) {
                // All matches complete - set to WETTKAMPF_ENDE
                LOGGER.info("TOURNAMENT COMPLETE for team {} - setting to WETTKAMPF_ENDE", teamId);
                
                LigamatchBE lastMatch = matchAnalysisService.findLastMatchForTeam(wettkampfId, teamId);
                session.setCurrentMatchId(lastMatch.getMatchId());
                session.setCurrentMatchNumber(Math.toIntExact(lastMatch.getMatchNr()));
                session.setCurrentPasseNumber(5);
                session.setStatus(STATUS_WETTKAMPF_ENDE);
                
                long opponentId = matchAnalysisService.findOpponentTeamId(lastMatch.getMatchId(), teamId);
                session.setGegnerTeamId(opponentId);
                
                LOGGER.info("Team {} WETTKAMPF_ENDE initialization: match {} (nr={}), opponent {}, passe 5", 
                           teamId, lastMatch.getMatchId(), lastMatch.getMatchNr(), opponentId);
            } else {
                // Found incomplete match - set up session for this match
                LOGGER.info("ACTIVE MATCH found for team {} - match {} (nr={})", 
                           teamId, currentMatch.getMatchId(), currentMatch.getMatchNr());
                
                long opponentId = matchAnalysisService.findOpponentTeamId(currentMatch.getMatchId(), teamId);
                int currentPasseNumber = matchAnalysisService.getCurrentPasseNumber(currentMatch.getMatchId(), teamId, opponentId);
                
                // Determine appropriate state for this incomplete match
                String initialStatus = determineInitialStatus(currentMatch.getMatchId(), teamId, passeComponent);
                
                session.setCurrentMatchId(currentMatch.getMatchId());
                session.setCurrentMatchNumber(Math.toIntExact(currentMatch.getMatchNr()));
                session.setCurrentPasseNumber(currentPasseNumber);
                session.setStatus(initialStatus);
                session.setGegnerTeamId(opponentId);
                
                LOGGER.info("Team {} ACTIVE initialization: match {} (nr={}), opponent {}, status {}, passe {}", 
                           teamId, currentMatch.getMatchId(), currentMatch.getMatchNr(), opponentId, initialStatus, currentPasseNumber);
            }
            
            // Save to database
            sessionDAO.createSession(session, -1L);
            
            return new SessionRuntime(session, sessionDAO, matchComponent, passeComponent, matchAnalysisService, mannschaftsmitgliedComponent, dsbMitgliedComponent, wettkampfComponent, veranstaltungComponent);
            
        } catch (Exception e) {
            LOGGER.error("Error initializing session for team {}: {}", teamId, e.getMessage());
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "Failed to initialize session", e);
        }
    }
    
    /**
     * Determine initial session status based on current match state using simplified business logic.
     * This method uses the correct business flow for initialization.
     */
    private static String determineInitialStatus(long matchId, long teamId, PasseComponent passeComponent) {
        try {
            // Get all passes for this team and match
            List<PasseDO> passes = passeComponent.findByMannschaftMatchId(teamId, matchId);
            
            // If no passes exist, need to register shooters first
            if (passes.isEmpty()) {
                return STATUS_SCHUETZENMELDUNG;
            }
            
            // If there are passes but match is not complete, continue with SATZEINGABE at correct passe number
            // The match completion check was already done in the calling method
            return STATUS_SATZEINGABE;
            
        } catch (Exception e) {
            LOGGER.error("Error determining initial status for match {}, team {}: {}", matchId, teamId, e.getMessage());
            return STATUS_SCHUETZENMELDUNG; // Safe fallback
        }
    }
    
    /**
     * Check if a pass has actual arrow scores (not empty pre-created passes).
     */
    static boolean hasActualArrowScores(PasseDO passe) {
        return (passe.getPfeil1() != null && passe.getPfeil1() > 0) ||
               (passe.getPfeil2() != null && passe.getPfeil2() > 0) ||
               (passe.getPfeil3() != null && passe.getPfeil3() > 0);
    }

    /**
     * Update session in database with enhanced error handling
     */
    public void persistSession() {
        try {
            sessionDAO.updateStatus(session, 0L);
            LOGGER.debug("Successfully persisted session for team {} - Status: {} Match: {} Passe: {} Opponent: {}",
                        session.getTeamId(), session.getStatus(), session.getCurrentMatchId(), 
                        session.getCurrentPasseNumber(), session.getGegnerTeamId());
        } catch (Exception e) {
            LOGGER.error("Unexpected error updating session status for team {}: {}", session.getTeamId(), e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, 
                "Internal error updating session status: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update session status - delegated from StateContext
     */
    public void updateSessionStatus(String newStatus) {
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "New status cannot be null or empty");
        }
        
        session.setStatus(newStatus);
        persistSession();
        LOGGER.debug("SessionRuntime updated session {} status to {}", session.getTeamId(), newStatus);
    }
    
    /**
     * Update passe number - delegated from StateContext
     */
    public void updatePasseNumber(int newPasseNumber) {
        if (newPasseNumber < 1 || newPasseNumber > 5) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Invalid passe number: " + newPasseNumber);
        }
        
        session.setCurrentPasseNumber(newPasseNumber);
        persistSession();
        LOGGER.debug("SessionRuntime updated session {} passe to {}", session.getTeamId(), newPasseNumber);
    }
    
    /**
     * Advance to next match - delegated from StateContext
     */
    public void advanceToNextMatch(LigamatchBE nextMatch, long opponentId) {
        if (nextMatch == null) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Next match cannot be null");
        }
        if (opponentId <= 0) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Invalid opponent ID: " + opponentId);
        }
        
        session.setCurrentMatchId(nextMatch.getMatchId());
        session.setCurrentMatchNumber(Math.toIntExact(nextMatch.getMatchNr()));
        session.setCurrentPasseNumber(1);
        session.setStatus(STATUS_SCHUETZENMELDUNG);
        session.setGegnerTeamId(opponentId);
        persistSession();
        
        LOGGER.info("SessionRuntime advanced team {} to match {} (opponent: {}) - Status: {} Passe: {}",
                   session.getTeamId(), nextMatch.getMatchId(), opponentId, session.getStatus(), session.getCurrentPasseNumber());
    }
    
    /**
     * Load opponent session - delegated from StateContext
     */
    public TabletSchusszettelEntity loadOpponentSessionByTeamId(long opponentTeamId) {
        if (opponentTeamId <= 0) {
            return null;
        }
        
        return sessionDAO.findByWettkampfUndTeam(session.getWettkampfId(), opponentTeamId)
                .orElse(null);
    }
    
    /**
     * Get current match ID
     */
    public long getCurrentMatchId() {
        return session.getCurrentMatchId();
    }
    
    /**
     * Get current passe number
     */
    public int getCurrentPasseNumber() {
        return session.getCurrentPasseNumber();
    }
    
    /**
     * Get team ID
     */
    public long getTeamId() {
        return session.getTeamId();
    }
    
    /**
     * Get opponent team ID
     */
    public long getOpponentTeamId() {
        return session.getGegnerTeamId();
    }
    
    /**
     * Get wettkampf ID
     */
    public long getWettkampfId() {
        return session.getWettkampfId();
    }
    
    /**
     * Get session token
     */
    public String getToken() {
        return session.getToken();
    }
    
    /**
     * Get session DAO for state pattern access
     */
    public TabletSchusszettelDAO getSessionDAO() {
        return sessionDAO;
    }
    
    /**
     * Session self-correction based on external database changes.
     * CRITICAL FIX: Removed problematic passe number correction that forced teams backwards.
     * Teams should progress independently based on their own passe completion.
     */
    public boolean checkAgainstDatabase() {
        try {
            // REMOVED: Problematic session self-correction that forced teams backwards
            // Old logic: 
            // - Used global passe calculation which returned first incomplete passe globally
            // - Caused teams to be forced back to previous passes
            // - Created "0 available for scoring" scenarios
            
            // New approach: Teams maintain their own progression
            // - Each team progresses based on their own completed passes
            // - No interference from opponent team state
            // - Eliminates race conditions in passe progression
            
            LOGGER.debug("Session check skipped - teams now progress independently for team {}", session.getTeamId());
            return false; // No changes made - teams progress independently
            
        } catch (Exception e) {
            LOGGER.warn("Error during session self-check for team {}: {}", session.getTeamId(), e.getMessage());
            return false;
        }
    }

    /**
     * Check if this session's match is complete using MatchAnalysisService.
     */
    public boolean isMatchComplete() {
        try {
            return matchAnalysisService.isMatchComplete(
                session.getCurrentMatchId(), 
                session.getTeamId(), 
                session.getGegnerTeamId());
        } catch (Exception e) {
            LOGGER.error("Error checking match completion for session {}: {}", session.getTeamId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if this session has more matches available using Setzliste-aware tournament progression.
     */
    public boolean hasMoreMatches() {
        try {
            // CRITICAL FIX: Use proper Setzliste-aware tournament progression
            // instead of flawed naechsteMatchId calculation
            LigamatchBE nextMatch = matchAnalysisService.findCorrectNextMatch(session.getCurrentMatchId(), session.getTeamId());
            return nextMatch != null;
        } catch (Exception e) {
            LOGGER.error("Error checking for more matches using Setzliste-aware logic for session {}: {}", session.getTeamId(), e.getMessage());
            return false;
        }
    }
    
}