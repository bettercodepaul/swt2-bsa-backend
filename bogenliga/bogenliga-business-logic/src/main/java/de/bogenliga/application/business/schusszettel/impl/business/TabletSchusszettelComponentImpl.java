package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.business.schusszettel.api.types.inside.*;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.business.domain.SessionRuntime;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tablet Schusszettel component implementation for archery competition score entry workflow.
 * 
 * <h2>FUNCTIONALITY</h2>
 * This component manages tablet-based score entry sessions for archery competitions using
 * a state machine pattern. Session state persists in TabletSchusszettelEntity table.
 * State machine logic is delegated to SessionRuntime domain class.
 * 
 * <h2>STATE MACHINE</h2>
 * 
 * <h3>States:</h3>
 * <pre>
 * NOT_ALLOWED      → Invalid access token
 * SCHUETZENMELDUNG → Shooter registration required  
 * SATZEINGABE      → Score entry in progress
 * WARTE            → Waiting for opponent team completion
 * WETTKAMPF_ENDE   → Competition completed
 * </pre>
 * 
 * <h3>Transitions:</h3>
 * <pre>
 * SCHUETZENMELDUNG --[POST shooters]--> SATZEINGABE
 * SATZEINGABE      --[POST scores]----> WARTE
 * WARTE            --[evaluate]-------> SATZEINGABE (next passe)
 *                                   --> SCHUETZENMELDUNG (next match)  
 *                                   --> WETTKAMPF_ENDE (tournament end)
 * </pre>
 * 
 * <h3>WARTE State Resolution:</h3>
 * <pre>
 * if (both teams in WARTE state) {
 *     if (match complete: team ≥6 Satzpunkte) {
 *         if (more matches exist for team) {
 *             → advance to next match: SCHUETZENMELDUNG
 *         } else {
 *             → tournament complete: WETTKAMPF_ENDE  
 *         }
 *     } else {
 *         → continue current match: increment passe, SATZEINGABE
 *     }
 * }
 * </pre>
 * 
 * <h2>API OPERATIONS</h2>
 * 
 * <h3>GET</h3>
 * Returns current session state and context data. Validates access token and evaluates
 * WARTE state with opponent session. Returns state-specific data including available
 * shooters, current scores, or match results based on current state.
 * 
 * <h3>POST (Shooter Registration)</h3>
 * Registers exactly 3 shooters for current passe. Validates shooters are deployed
 * team members (eingesetzt ≥ 1) and no duplicates exist. Creates placeholder
 * pass entries for all 5 sets. Transitions SCHUETZENMELDUNG → SATZEINGABE.
 * 
 * <h3>POST (Score Entry)</h3>
 * Submits arrow scores for current passe. Validates exactly 3 shooters with
 * arrow values 0-10. Updates pass entries with scores. Transitions
 * SATZEINGABE → WARTE, then evaluates with opponent for next state.
 * 
 * <h2>SYNCHRONIZATION HANDLING</h2>
 * 
 * <h3>Team Desynchronization:</h3>
 * Handles cases where teams have different passe numbers or states due to:
 * <ul>
 *   <li>Network interruption during state transitions</li>
 *   <li>External score entry via web application</li>
 *   <li>Application restart or failure recovery</li>
 * </ul>
 * 
 * <h3>Recovery Mechanisms:</h3>
 * <ul>
 *   <li>GET requests validate and correct passe numbers against pass data</li>
 *   <li>WARTE evaluation allows progression when opponent session unavailable</li>
 *   <li>SessionRuntime.evaluateWithOpponent() handles desynchronized states</li>
 * </ul>
 * 
 * <h2>COMPONENT DEPENDENCIES</h2>
 * 
 * <ul>
 *   <li>SessionRuntime: State machine logic and transitions</li>
 *   <li>MatchAnalysisService: Match completion analysis using LigamatchBE</li>
 *   <li>PasseComponent: Pass data queries (findByMannschaftMatchId)</li>
 *   <li>MatchComponent: LigamatchBE queries for match progression</li>
 *   <li>TabletSchusszettelDAO: Session persistence operations</li>
 * </ul>
 * 
 * <h2>BUSINESS RULES</h2>
 * 
 * <h3>Competition Rules:</h3>
 * <ul>
 *   <li>Maximum 5 passes per match</li>
 *   <li>Exactly 3 shooters per team per pass</li>
 *   <li>2 arrows per shooter (ARROWS_PER_SHOOTER constant)</li>
 *   <li>Arrow values: 0-10 points</li>
 *   <li>Match completion: 6+ Satzpunkte for either team</li>
 * </ul>
 * 
 * <h3>Data Validation:</h3>
 * <ul>
 *   <li>Shooters must be deployed team members (eingesetzt ≥ 1)</li>
 *   <li>No duplicate shooter registration per pass</li>
 *   <li>Score entry only allowed after shooter registration</li>
 * </ul>
 * 
 * @author Marty Lauterbach
 * @version 3.0 - Optimized with LigamatchBE integration and eliminated facade pattern
 * @version 2.0 - Enhanced with robustness features and comprehensive state management
 * @since 1.0 - Initial implementation
 * @see SessionRuntime State machine implementation
 * @see MatchAnalysisService Optimized match analysis using LigamatchBE
 * @see TabletSchusszettelAdminComponentImpl Session initialization
 */
@Service
public class TabletSchusszettelComponentImpl implements TabletSchusszettelComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSchusszettelComponentImpl.class);

    // Constants controlling match logic
    private static final int MAX_SETS = 5;
    private static final int SHOOTERS_PER_TEAM = 3;
    private static final int ARROWS_PER_SHOOTER = 2;

    // State identifiers for session (now using SessionRuntime constants)
    private static final String STATUS_SCHUETZENMELDUNG = SessionRuntime.STATUS_SCHUETZENMELDUNG;
    private static final String STATUS_SATZEINGABE      = SessionRuntime.STATUS_SATZEINGABE;
    private static final String STATUS_WARTE           = SessionRuntime.STATUS_WARTE;
    private static final String STATUS_WETTKAMPF_ENDE  = SessionRuntime.STATUS_WETTKAMPF_ENDE;

    // OPTIMIZED: Direct component injection (no facade pattern)
    private final TabletSchusszettelDAO sessionDAO;
    private final PasseComponent passeComponent;
    private final MatchComponent matchComponent;
    private final MatchAnalysisService matchAnalysisService;
    private final MannschaftsmitgliedComponent mmComponent;
    private final DsbMitgliedComponent mitgliedComponent;
    private final DsbMannschaftComponent mannschaftComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;


    @Autowired
    public TabletSchusszettelComponentImpl(
            TabletSchusszettelDAO sessionDAO,
            PasseComponent passeComponent,
            MatchComponent matchComponent,
            MatchAnalysisService matchAnalysisService,
            MannschaftsmitgliedComponent mmComponent,
            DsbMitgliedComponent mitgliedComponent,
            DsbMannschaftComponent mannschaftComponent,
            VereinComponent vereinComponent,
            WettkampfComponent wettkampfComponent,
            VeranstaltungComponent veranstaltungComponent) {
        
        // OPTIMIZED: Direct injection eliminates facade dependency
        this.sessionDAO = sessionDAO;
        this.passeComponent = passeComponent;
        this.matchComponent = matchComponent;
        this.matchAnalysisService = matchAnalysisService;
        this.mmComponent = mmComponent;
        this.mitgliedComponent = mitgliedComponent;
        this.mannschaftComponent = mannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
    }

    /**
     * GET-Handler: Tablet Status abfragen
     * @author Marty Lauterbach
     */
    @Override
    public TabletSchusszettelDO getStatus(long wettkampfId, long teamId, String token) {
        // 1) Validate token and load session
        SessionRuntime runtime = validateTokenAndLoadSession(wettkampfId, teamId, token);
        if (runtime == null) {
            return notAllowed();
        }
        
        // 2) Evaluate current session state
        TabletSchusszettelEntity session = evaluateSessionState(wettkampfId, teamId, runtime);
        
        // 3) Build base response with core data
        TabletSchusszettelDO result = buildBaseResponse(wettkampfId, teamId, session);
        
        // 4) Enrich response based on current state
        enrichResponseByState(wettkampfId, teamId, session, result);
        
        return result;
    }

    /**
     * Validates token and loads session runtime from database.
     * @return SessionRuntime if valid, null if invalid token
     */
    private SessionRuntime validateTokenAndLoadSession(long wettkampfId, long teamId, String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        
        return SessionRuntime.loadFromDatabase(
                wettkampfId, teamId, token, 
                sessionDAO, matchComponent, passeComponent, matchAnalysisService);
    }
    
    /**
     * Evaluates and potentially updates the current session state.
     * Handles WARTE state evaluation with opponent and passe synchronization.
     * 
     * ROBUSTNESS IMPROVEMENTS:
     * - Proactive WARTE state evaluation on every GET request
     * - Handles desynchronized team scenarios
     * - Maintains database consistency as source of truth
     */
    private TabletSchusszettelEntity evaluateSessionState(long wettkampfId, long teamId, SessionRuntime runtime) {
        TabletSchusszettelEntity session = runtime.getSession();
        
        // PHASE 4: Session cache synchronization
        syncSessionPasseNumber(session, teamId);
        
        // EDGE CASE HANDLING: Always check for opponent session state on GET requests
        TabletSchusszettelEntity opponentSession = sessionDAO
                .findByWettkampfUndTeam(wettkampfId, session.getGegnerTeamId())
                .orElse(null);
        
        // Log current state for debugging edge cases
        LOGGER.debug("GET request state evaluation: Team {} (status: {}, passe: {}), Opponent {} (status: {}, passe: {})", 
                    teamId, session.getStatus(), session.getCurrentPasseNumber(),
                    opponentSession != null ? opponentSession.getTeamId() : "null",
                    opponentSession != null ? opponentSession.getStatus() : "null",
                    opponentSession != null ? opponentSession.getCurrentPasseNumber() : "null");
        
        // ROBUSTNESS: Evaluate WARTE state regardless of current status
        // This handles edge cases where teams got stuck due to system issues
        if (STATUS_WARTE.equals(session.getStatus())) {
            if (runtime.evaluateWithOpponent(opponentSession)) {
                LOGGER.info("GET request triggered state advancement: Team {} from WARTE to {}", 
                           teamId, session.getStatus());
                session = runtime.getSession(); // Reload updated session
            }
        }
        
        // CRITICAL FIX: Check for invalid passe numbers (beyond maximum)
        if (session.getCurrentPasseNumber() > MAX_SETS) {
            LOGGER.error("INVALID STATE: Team {} at passe {} (max {}), match should be complete", 
                        teamId, session.getCurrentPasseNumber(), MAX_SETS);
            
            // Force state evaluation to fix this
            try {
                boolean matchComplete = matchAnalysisService.isMatchComplete(
                    session.getCurrentMatchId(), teamId, session.getGegnerTeamId());
                
                if (matchComplete) {
                    LOGGER.warn("FIXING INVALID STATE: Team {} match is complete, forcing advancement", teamId);
                    session.setStatus(STATUS_WARTE);
                    session.setCurrentPasseNumber(MAX_SETS); // Cap at max
                    sessionDAO.updateStatus(session, 0L);
                    
                    // Now evaluate with opponent to advance properly
                    if (runtime.evaluateWithOpponent(opponentSession)) {
                        LOGGER.info("INVALID STATE FIXED: Advanced team {} to proper state", teamId);
                        session = runtime.getSession();
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error fixing invalid passe state for team {}: {}", teamId, e.getMessage());
            }
        }
        
        // EDGE CASE: If opponent is in WARTE but we're not, check if we should advance too
        if (opponentSession != null && STATUS_WARTE.equals(opponentSession.getStatus()) && 
            STATUS_SATZEINGABE.equals(session.getStatus())) {
            
            LOGGER.info("DESYNC RECOVERY: Opponent in WARTE but team {} still in SATZEINGABE - checking advancement", 
                       teamId);
            
            // Check if our current passe is actually complete
            try {
                boolean canAdvance = isCurrentPasseComplete(session, teamId);
                if (canAdvance) {
                    LOGGER.warn("DESYNC FIX: Auto-advancing team {} from SATZEINGABE to WARTE for synchronization", 
                               teamId);
                    runtime.nudgeAlong(); // SATZEINGABE -> WARTE
                    
                    // Now try to evaluate with opponent again
                    if (runtime.evaluateWithOpponent(opponentSession)) {
                        LOGGER.info("DESYNC FIX: Successfully synchronized and advanced team {}", teamId);
                    }
                    session = runtime.getSession(); // Reload updated session
                }
            } catch (Exception e) {
                LOGGER.warn("Error during desync recovery for team {}: {}", teamId, e.getMessage());
            }
        }
        
        return session;
    }

    /**
     * PHASE 4: Synchronize session cache with source of truth.
     * Compares session's cached currentPasseNumber with actual calculated value
     * and updates the session if they differ.
     */
    private void syncSessionPasseNumber(TabletSchusszettelEntity session, long teamId) {
        try {
            // Get actual current passe from MatchAnalysisService (source of truth)
            int actualCurrentPasse = matchAnalysisService.getCurrentPasseNumber(
                session.getCurrentMatchId(), teamId, 0L);
            
            // Compare with cached value in session
            if (session.getCurrentPasseNumber() != actualCurrentPasse) {
                LOGGER.info("CACHE SYNC: Updating session passe {} -> {} for team {} (cache was stale)", 
                           session.getCurrentPasseNumber(), actualCurrentPasse, teamId);
                
                session.setCurrentPasseNumber(actualCurrentPasse);
                sessionDAO.updateStatus(session, 0L);
                
                LOGGER.debug("Session cache synchronized for team {}: passe number is now {}", 
                           teamId, actualCurrentPasse);
            }
        } catch (Exception e) {
            LOGGER.warn("Error synchronizing session cache for team {}: {}", teamId, e.getMessage());
            // Non-fatal - continue with cached value
        }
    }
    
    /**
     * Helper method to check if current passe is complete for a team.
     * Used for desynchronization recovery.
     */
    private boolean isCurrentPasseComplete(TabletSchusszettelEntity session, long teamId) {
        try {
            // Get passes for current passe
            List<PasseDO> currentPassePasses = passeComponent.findByMatchId(session.getCurrentMatchId()).stream()
                    .filter(p -> p.getPasseMannschaftId() == teamId)
                    .filter(p -> p.getPasseLfdnr() == (long) session.getCurrentPasseNumber())
                    .toList();
            
            // Check if we have exactly 3 shooters with data
            return currentPassePasses.size() == SHOOTERS_PER_TEAM;
        } catch (Exception e) {
            LOGGER.warn("Error checking passe completion for team {}: {}", teamId, e.getMessage());
            return false;
        }
    }
    
    /**
     * Builds the base response DTO with core match and team data.
     */
    private TabletSchusszettelDO buildBaseResponse(long wettkampfId, long teamId, TabletSchusszettelEntity session) {
        // Convert session status to enum
        TabletSchusszettelDO.TabletSchusszettelStatus statusEnum = convertSessionStatus(session.getStatus());
        
        // Extract IDs for data fetching
        long matchId = session.getCurrentMatchId();
        long oppTeam = session.getGegnerTeamId();
        
        // Fetch match data
        MatchDataContext matchData = fetchMatchData(wettkampfId, teamId, session, oppTeam);
        
        // Build base DTO
        TabletSchusszettelDO result = new TabletSchusszettelDO();
        result.setStatus(statusEnum);
        result.setEigenesTeam(getTeamInfo(teamId));
        result.setGegnerischesTeam(getTeamInfo(oppTeam));
        result.setSatzErgebnisse(matchData.satzHistory);
        result.setSchuetzenMatchPunkte(buildMatchPunkte(matchData.allPasses, teamId));
        result.setMatchErgebnis(buildTeamMatchInfo(matchData.satzHistory, teamId, oppTeam));
        result.setWettkampfInfo(buildWettkampfInfo(matchId, wettkampfId));
        
        // Set correct passe number
        setCorrectPasseNumber(teamId, oppTeam, session, result);
        
        // Set match IDs for navigation
        result.setEigenesTeamMatchId(session.getCurrentMatchId());
        result.setGegnerischesTeamMatchId(matchData.oppMatchId != null ? matchData.oppMatchId : 0L);
        
        return result;
    }
    
    /**
     * Enriches the response with state-specific data.
     */
    private void enrichResponseByState(long wettkampfId, long teamId, TabletSchusszettelEntity session, TabletSchusszettelDO result) {
        switch (session.getStatus()) {
            case STATUS_SCHUETZENMELDUNG:
                handleSchuetzenmeldung(session, result, teamId);
                break;
            case STATUS_SATZEINGABE:
                handleSatzeingabe(session, result);
                break;
            case STATUS_WARTE:
                handleWarte(session, result);
                break;
            case STATUS_WETTKAMPF_ENDE:
                handleEnde(session, result, wettkampfId, teamId);
                break;
            default:
                // Unexpected state => ignore
        }
    }
    
    /**
     * Converts session status string to enum, with proper error handling.
     */
    private TabletSchusszettelDO.TabletSchusszettelStatus convertSessionStatus(String status) {
        try {
            return TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(status);
        } catch (IllegalArgumentException iae) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Unknown session status: " + status);
        }
    }
    
    /**
     * Fetches all match-related data in one operation.
     */
    private MatchDataContext fetchMatchData(long wettkampfId, long teamId, TabletSchusszettelEntity session, long oppTeam) {
        // Fetch team-specific passes for begegnung architecture using PasseComponent directly
        List<PasseDO> teamPasses = passeComponent.findByMannschaftMatchId(teamId, session.getCurrentMatchId());
        
        // FIXED: Get opponent match ID from our metadata table instead of buggy LigamatchBE.matchIdGegner
        Long oppMatchId = null;
        try {
            // Use our own DAO to find opponent's session and get their match ID
            Optional<TabletSchusszettelEntity> opponentSession = 
                sessionDAO.findByWettkampfUndTeam(wettkampfId, oppTeam);
            
            if (opponentSession.isPresent()) {
                oppMatchId = opponentSession.get().getCurrentMatchId();
                LOGGER.debug("Found opponent match ID {} for team {} via our DAO", oppMatchId, oppTeam);
                
                // Validation: ensure we got a different match ID
                if (oppMatchId != null && oppMatchId.equals(session.getCurrentMatchId())) {
                    LOGGER.warn("Opponent match ID {} same as our match ID {} - possible data issue", 
                               oppMatchId, session.getCurrentMatchId());
                }
            } else {
                LOGGER.warn("No session found for opponent team {} in wettkampf {}", oppTeam, wettkampfId);
            }
        } catch (Exception e) {
            LOGGER.warn("Error finding opponent match ID via DAO for team {} in wettkampf {}: {}", 
                       oppTeam, wettkampfId, e.getMessage());
            oppMatchId = null; // Fallback to null if resolution fails
        }
        List<PasseDO> oppPasses = oppMatchId != null ? 
            passeComponent.findByMannschaftMatchId(oppTeam, oppMatchId) : 
            new ArrayList<>();
        
        // Combine all passes for legacy compatibility
        List<PasseDO> allPasses = new ArrayList<>();
        allPasses.addAll(teamPasses);
        allPasses.addAll(oppPasses);
        
        LOGGER.debug("Pass retrieval for begegnung: Team {} uses match {}, Opponent {} uses match {}", 
                    teamId, session.getCurrentMatchId(), oppTeam, oppMatchId);
        LOGGER.debug("Retrieved passes: Team {} = {} passes, Opponent {} = {} passes", 
                    teamId, teamPasses.size(), oppTeam, oppPasses.size());
        
        // Build satz history using simplified logic (buildSatzErgebnisse was removed)
        List<SatzErgebnisDO> satzHistory = buildSatzErgebnisseLocal(teamPasses, oppPasses, teamId, oppTeam);
        LOGGER.debug("Built satz history: {} completed sets for teams {} vs {}", 
                    satzHistory.size(), teamId, oppTeam);
        
        return new MatchDataContext(allPasses, satzHistory, oppMatchId);
    }
    
    /**
     * Sets the correct passe number using match analysis service.
     */
    private void setCorrectPasseNumber(long teamId, long oppTeam, TabletSchusszettelEntity session, TabletSchusszettelDO result) {
        try {
            int correctPasseNumber = matchAnalysisService.getCurrentPasseNumber(session.getCurrentMatchId(), teamId, oppTeam);
            result.setCurrentPasseNumber(correctPasseNumber);
            
            // Update session if passe number is incorrect
            if (session.getCurrentPasseNumber() != correctPasseNumber) {
                LOGGER.warn("Session passe number {} incorrect for team {}, updating to {}", 
                          session.getCurrentPasseNumber(), teamId, correctPasseNumber);
                session.setCurrentPasseNumber(correctPasseNumber);
                sessionDAO.updateStatus(session, 0L);
            }
        } catch (Exception e) {
            LOGGER.warn("Error calculating correct passe number for team {}, using session value: {}", 
                       teamId, e.getMessage());
            result.setCurrentPasseNumber(session.getCurrentPasseNumber());
        }
    }


    /**
     * Helper class to hold match data context.
     *
     * @param oppMatchId Changed to Long to handle null values
     */
        private record MatchDataContext(List<PasseDO> allPasses, List<SatzErgebnisDO> satzHistory, Long oppMatchId) {
    }

    /**
     * Hilfsmethode für NOT_ALLOWED
     */
    private TabletSchusszettelDO notAllowed() {
        TabletSchusszettelDO dto = new TabletSchusszettelDO();
        dto.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED);
        return dto;
    }

    /**
     * POST-Handler: Schützenmeldung
     * - Prüfe genau 3 Schützen
     * - Mitgliedschaft validieren
     * - Reservation durch leere Passen erzeugen (check if they exist first)
     * - Statuswechsel -> SATZEINGABE
     * @author Marty Lauterbach
     */
    @Override
    public void submitSchuetzen(long wettkampfId,
                                long teamId,
                                String token,
                                SchuetzenMeldungDO input) {

        // 1) Token presence / emptiness check
        if (token == null || token.isEmpty()) {
            throw new BusinessException(
                    ErrorCode.NO_PERMISSION_ERROR,
                    "Token argument is empty");
        }

        // 2) Create SessionRuntime from database
        SessionRuntime runtime = SessionRuntime.loadFromDatabase(
                wettkampfId, teamId, token,
                sessionDAO, matchComponent, passeComponent, matchAnalysisService);
                
        if (runtime == null) {
            throw new BusinessException(
                    ErrorCode.NO_PERMISSION_ERROR,
                    "Invalid or expired token");
        }
        
        TabletSchusszettelEntity session = runtime.getSession();

        // 3) Validate payload and team roster
        if (input == null || input.getGemeldeteSchuetzen() == null) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Invalid registration data");
        }
        
        // 3.1) Enhanced validation with team roster check
        validateSchützenmeldungTeamRoster(teamId, input.getGemeldeteSchuetzen());

        // 4) Mark shooters as deployed for this match using MannschaftsmitgliedComponent
        for (Long shooterId : input.getGemeldeteSchuetzen()) {
            try {
                // Note: Current eingesetzt field is a general counter, not match-specific
                // Using existing deployment logic that auto-increments on first score entry
                MannschaftsmitgliedDO member = mmComponent.findByMemberAndTeamId(teamId, shooterId);
                if (member.getDsbMitgliedEingesetzt() == null || member.getDsbMitgliedEingesetzt() < 1) {
                    member.setDsbMitgliedEingesetzt(1); // Mark as deployed
                    mmComponent.update(member, 0L);
                    LOGGER.debug("Marked shooter {} as deployed for team {}", shooterId, teamId);
                }
            } catch (Exception e) {
                LOGGER.warn("Could not update deployment status for shooter {} in team {}: {}", shooterId, teamId, e.getMessage());
                // Continue with other shooters - non-critical failure
            }
        }

        // 5) Use SessionRuntime to advance state (NO pass creation here)
        runtime.nudgeAlong(); // SCHUETZENMELDUNG -> SATZEINGABE
    }

    /**
     * POST-Handler: Satzeingabe
     * - Schussdaten speichern
     * - Prüfen, ob beide Teams fertig -> ggf. Satz-, Match- oder Tag beenden
     * @author Marty Lauterbach
     */
    @Override
    public void submitSatz(long wettkampfId, long teamId, String token, SatzEingabeDO eingabe) {
        // 1) Token presence / emptiness check
        if (token == null || token.isEmpty()) {
            throw new BusinessException(
                    ErrorCode.NO_PERMISSION_ERROR,
                    "Token argument is empty");
        }

        // 2) Create SessionRuntime from database
        SessionRuntime runtime = SessionRuntime.loadFromDatabase(
                wettkampfId, teamId, token,
                sessionDAO, matchComponent, passeComponent, matchAnalysisService);
                
        if (runtime == null) {
            throw new BusinessException(
                    ErrorCode.NO_PERMISSION_ERROR,
                    "Invalid or expired token");
        }
        
        TabletSchusszettelEntity session = runtime.getSession();

        // 3) Validate payload: exactly SHOOTERS_PER_TEAM shooters
        if (eingabe == null
                || eingabe.getSatzeingabe() == null
                || eingabe.getSatzeingabe().size() != SHOOTERS_PER_TEAM) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Exactly " + SHOOTERS_PER_TEAM + " shooters' scores required");
        }

        // 4) Calculate current passe number dynamically using MatchAnalysisService
        long matchId = session.getCurrentMatchId();
        int currentPasse = matchAnalysisService.getCurrentPasseNumber(matchId, teamId, 0L); // Get dynamic passe number

        // 4.1) Validate match is not already complete
        validateMatchNotComplete(teamId, session);

        // 5) Create passes on-demand for all shooters (no more update/create branching)
        for (SchuetzenSatzDO satz : eingabe.getSatzeingabe()) {
            // 5.1) Validate arrow values are within valid range (0-10)
            validateArrowValues(satz);
            
            // 5.2) Validate shooter was registered in schützenmeldung
            validateShooterRegistration(wettkampfId, teamId, session.getCurrentMatchNumber(),
                    currentPasse, satz.getSchuetzenId());
            
            try {
                // SIMPLIFIED: Always create new pass (on-demand creation pattern)
                LOGGER.debug("Creating pass on-demand: wettkampfId={}, matchNr={}, teamId={}, passeNr={}, schuetzeId={}",
                        wettkampfId, session.getCurrentMatchNumber(), teamId, currentPasse, satz.getSchuetzenId());

                PasseDO passe = new PasseDO(
                        null,                   // id (generated)
                        teamId,                 // passeMannschaftId
                        wettkampfId,            // passeWettkampfId
                        session.getCurrentMatchNumber(),
                        matchId,
                        (long) currentPasse,    // Dynamic passe number from MatchAnalysisService
                        satz.getSchuetzenId(),
                        (ARROWS_PER_SHOOTER >= 1 ? satz.getSchuss1() : null),
                        (ARROWS_PER_SHOOTER >= 2 ? satz.getSchuss2() : null),
                        (ARROWS_PER_SHOOTER >= 3 ? satz.getSchuss3() : null),
                        null, null, null // pfeil4-6 not used in tablet scoring
                );
                passeComponent.create(passe, 0L);

            } catch (Exception e) {
                LOGGER.error("Error saving passe for shooter {}: {}", satz.getSchuetzenId(), e.getMessage(), e);
                throw new TechnicalException(
                        ErrorCode.INTERNAL_ERROR,
                        "Fehler beim Speichern der Passe für Schütze " + satz.getSchuetzenId() + ": " + e.getMessage());
            }
        }

        // 6) CRITICAL: Update match scores in database for LigamatchBE consistency
        updateMatchScoresAfterSetCompletion(wettkampfId, teamId, session, currentPasse);
        
        // 7) Use SessionRuntime to advance state and check opponent
        runtime.nudgeAlong(); // SATZEINGABE -> WARTE
        
        // Check if opponent is also in WARTE
        TabletSchusszettelEntity opponentSession = sessionDAO
                .findByWettkampfUndTeam(wettkampfId, session.getGegnerTeamId())
                .orElse(null);
                
        if (runtime.evaluateWithOpponent(opponentSession)) {
            LOGGER.info("Both teams in WARTE - advanced team {} to {}", 
                       teamId, session.getStatus());
        }
    }

    //================================================================================
    // Private helper methods
    //================================================================================

    /**
     * SCHUETZENMELDUNG: verfügbar machen nur eingesetzter Vereins-Schützen (eingesetzt >= 1)
     */
    private void handleSchuetzenmeldung(TabletSchusszettelEntity session,
                                        TabletSchusszettelDO out,
                                        long teamId) {
        List<MannschaftsmitgliedDO> members = mmComponent.findByTeamId(teamId);

        // Stammdaten nur eingesetzter Vereins-Schützen zusammenstellen (eingesetzt >= 1)
        List<SchuetzeStammdatenDO> stammdaten = members.stream()
                .filter(m -> m.getDsbMitgliedEingesetzt() != null && m.getDsbMitgliedEingesetzt() >= 1)
                .map(m -> {
                    DsbMitgliedDO dm = mitgliedComponent.findById(m.getDsbMitgliedId());
                    return new SchuetzeStammdatenDO(
                            dm.getId(),
                            Math.toIntExact(m.getRueckennummer()),
                            dm.getVorname(),
                            dm.getNachname()
                    );
                }).collect(Collectors.toList());
        out.setSchuetzeStammDaten(stammdaten);

        List<VerfuegbarerSchuetzeDO> available = members.stream()
                .filter(m -> m.getDsbMitgliedEingesetzt() != null && m.getDsbMitgliedEingesetzt() >= 1)
                .map(m -> {
                    DsbMitgliedDO dm = mitgliedComponent.findById(m.getDsbMitgliedId());
                    return new VerfuegbarerSchuetzeDO(dm.getId(), dm.getVorname() + " " + dm.getNachname());
                }).collect(Collectors.toList());
        out.setVerfuegbareSchuetzen(available);
    }

    /**
     * SATZEINGABE: bereits registrierte Schützen + verbleibende zum Auswählen
     */
    private void handleSatzeingabe(TabletSchusszettelEntity session, TabletSchusszettelDO out) {
        long passeNr = session.getCurrentPasseNumber();
        long matchId = session.getCurrentMatchId();

        // Get active shooters for this team (eingesetzt >= 1)
        Set<Long> activeShooters = mmComponent.findByTeamId(session.getTeamId()).stream()
                .filter(mm -> mm.getDsbMitgliedEingesetzt() != null && mm.getDsbMitgliedEingesetzt() >= 1)
                .map(MannschaftsmitgliedDO::getDsbMitgliedId)
                .collect(Collectors.toSet());

        List<PasseDO> assigns = passeComponent.findByMatchId(matchId).stream()
                .filter(p -> p.getPasseLfdnr() == passeNr)
                .filter(p -> activeShooters.contains(p.getPasseDsbMitgliedId())) // Only include active shooters
                .toList();

        // Stammdaten der gemeldeten Schützen
        List<SchuetzeStammdatenDO> meta = assigns.stream().map(p -> {
            DsbMitgliedDO dm = mitgliedComponent.findById(p.getPasseDsbMitgliedId());
            MannschaftsmitgliedDO mm = mmComponent.findByMemberAndTeamId(session.getTeamId(), p.getPasseDsbMitgliedId());
            return new SchuetzeStammdatenDO(dm.getId(), Math.toIntExact(mm.getRueckennummer()), dm.getVorname(), dm.getNachname());
        }).collect(Collectors.toList());
        out.setSchuetzeStammDaten(meta);

        // Verbleibende Schützen - nur eingesetzte Schützen (eingesetzt >= 1)
        Set<Long> used = assigns.stream()
                .map(PasseDO::getPasseDsbMitgliedId).collect(Collectors.toSet());
        List<VerfuegbarerSchuetzeDO> left = mmComponent.findByTeamId(session.getTeamId()).stream()
                .filter(mm -> mm.getDsbMitgliedEingesetzt() != null && mm.getDsbMitgliedEingesetzt() >= 1)
                .map(MannschaftsmitgliedDO::getDsbMitgliedId)
                .filter(id -> !used.contains(id))
                .map(id -> {
                    DsbMitgliedDO dm = mitgliedComponent.findById(id);
                    return new VerfuegbarerSchuetzeDO(dm.getId(), dm.getVorname() + " " + dm.getNachname());
                }).collect(Collectors.toList());
        out.setVerfuegbareSchuetzen(left);
    }

    /**
     * WARTE: Show current data while waiting (simplified since SessionRuntime handles state transitions)
     */
    private void handleWarte(TabletSchusszettelEntity session,
                             TabletSchusszettelDO out) {
        handleSatzeingabe(session, out);
    }

    /**
     * WETTKAMPF_ENDE: Aufräumen und finale Recaps zeigen
     */
    private void handleEnde(TabletSchusszettelEntity session,
                            TabletSchusszettelDO out,
                            long wettkampfId,
                            long teamId) {

        // clear any per‐set or shooter‐detail data
        out.setSatzErgebnisse(Collections.emptyList());
        out.setSchuetzenMatchPunkte(Collections.emptyList());
        out.setSchuetzeStammDaten(Collections.emptyList());
        out.setVerfuegbareSchuetzen(Collections.emptyList());

        // Recap aller Matches des Tages, skipping any without a valid opponent
        List<TeamMatchInfoDO> recap = new ArrayList<>();
        for (MatchDO m : matchComponent.findByWettkampfId(wettkampfId)) {
            if (m.getMannschaftId() != teamId) {
                continue;
            }
            try {
                long opp = matchAnalysisService.findOpponentTeamId(m.getId(), teamId);
                // Get passes for both teams using PasseComponent directly
                List<PasseDO> teamMatchPasses = passeComponent.findByMannschaftMatchId(teamId, m.getId());
                // OPTIMIZED: Use established begegnung architecture (both teams share match)
                Long oppMatchId = m.getId(); // Follows established pattern from fetchMatchData()
                List<PasseDO> oppMatchPasses = oppMatchId != null ? 
                    passeComponent.findByMannschaftMatchId(opp, oppMatchId) : new ArrayList<>();
                
                List<PasseDO> ps = new ArrayList<>();
                ps.addAll(teamMatchPasses);
                ps.addAll(oppMatchPasses);
                
                // Separate passes by team for match analysis service
                List<PasseDO> teamPasses = ps.stream()
                    .filter(p -> p.getPasseMannschaftId() == teamId)
                    .collect(Collectors.toList());
                List<PasseDO> oppPasses = ps.stream()
                    .filter(p -> p.getPasseMannschaftId() == opp)
                    .collect(Collectors.toList());
                    
                List<SatzErgebnisDO> sets = buildSatzErgebnisseLocal(teamPasses, oppPasses, teamId, opp);

                recap.addAll(buildTeamMatchInfo(sets, teamId, opp));
            } catch (BusinessException be) {
                // Keine gültige Gegner-Session gefunden → match überspringen
            }
        }
        out.setMatchErgebnis(recap);
    }

    /**
     * Sum up arrow points per shooter in this match
     */
    private List<SchuetzeMatchPunkteDO> buildMatchPunkte(List<PasseDO> passen, long teamId) {
        // Get active shooters for this team (eingesetzt >= 1)
        Set<Long> activeShooters = mmComponent.findByTeamId(teamId).stream()
                .filter(mm -> mm.getDsbMitgliedEingesetzt() != null && mm.getDsbMitgliedEingesetzt() >= 1)
                .map(MannschaftsmitgliedDO::getDsbMitgliedId)
                .collect(Collectors.toSet());
        
        return passen.stream()
                .filter(p -> p.getPasseMannschaftId() == teamId)
                .filter(p -> activeShooters.contains(p.getPasseDsbMitgliedId())) // Only include active shooters
                .collect(Collectors.groupingBy(
                        PasseDO::getPasseDsbMitgliedId,
                        Collectors.summingInt(p -> {
                            int a = p.getPfeil1() != null ? p.getPfeil1() : 0;
                            int b = p.getPfeil2() != null ? p.getPfeil2() : 0;
                            int c = p.getPfeil3() != null ? p.getPfeil3() : 0;
                            return a + b + c;
                        })))
                .entrySet().stream()
                .map(e -> new SchuetzeMatchPunkteDO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Convert Satz-Ergebnisse to total match points per team
     * Fixed to correctly calculate Satzpunkte based on actual team performance
     */
    private List<TeamMatchInfoDO> buildTeamMatchInfo(
            List<SatzErgebnisDO> sets, long own, long opp) {
        int ownMp=0, oppMp=0;
        for (SatzErgebnisDO s : sets) {
            // Correctly identify which team is which in the satz result
            int ownPoints, oppPoints;
            if (s.getTeam1Id() == own) {
                ownPoints = s.getTeam1Punkte();
                oppPoints = s.getTeam2Punkte();
            } else {
                ownPoints = s.getTeam2Punkte();
                oppPoints = s.getTeam1Punkte();
            }
            
            // Award Satzpunkte based on official archery rules
            if (ownPoints > oppPoints) {
                ownMp += 2; // Winner gets 2 Satzpunkte
            } else if (oppPoints > ownPoints) {
                oppMp += 2; // Winner gets 2 Satzpunkte
            } else {
                ownMp += 1; // Tie: both teams get 1 Satzpunkt
                oppMp += 1;
            }
        }
        return Arrays.asList(
                new TeamMatchInfoDO(own, getTeamName(own), ownMp),
                new TeamMatchInfoDO(opp, getTeamName(opp), oppMp)
        );
    }

    private String getTeamName(long teamId) {
        // 1) Lade die Mannschaft, um an die vereins-ID zu kommen
        DsbMannschaftDO md = mannschaftComponent.findById(teamId);
        // 2) Nutze die wirkliche vereins-ID
        VereinDO v = vereinComponent.findById(md.getVereinId());
        return v.getName();
    }

    /**
     * Helper to fetch team name via DsbMannschaft and Verein
     */
    private TeamInfoDO getTeamInfo(long teamId) {
        DsbMannschaftDO md = mannschaftComponent.findById(teamId);
        VereinDO v = vereinComponent.findById(md.getVereinId());
        String name = v.getName() + (md.getNummer() > 1 ? " " + md.getNummer() : "");
        return new TeamInfoDO(teamId, name);
    }

    /**
     * Build wettkampf information from match and veranstaltung data
     */
    private WettkampfInfoDO buildWettkampfInfo(long matchId, long wettkampfId) {
        try {
            final MatchDO matchData = matchComponent.findById(matchId);
            final WettkampfDO competition = wettkampfComponent.findById(matchData.getWettkampfId());
            final VeranstaltungDO event = veranstaltungComponent.findById(competition.getWettkampfVeranstaltungsId());

            return assembleWettkampfInfo(competition, event);
        } catch (Exception ex) {
            LOGGER.warn("Failed to build wettkampf info for matchId {} and wettkampfId {}: {}",
                    matchId, wettkampfId, ex.getMessage());
            return null;
        }
    }

    /**
     * Assembles WettkampfInfoDO from competition and event data
     */
    private WettkampfInfoDO assembleWettkampfInfo(WettkampfDO competition, VeranstaltungDO event) {
        return new WettkampfInfoDO(
                competition.getId(),
                competition.getWettkampfTag(),
                competition.getWettkampfDatum(),
                competition.getWettkampfBeginn(),
                competition.getWettkampfOrtsname(),
                competition.getWettkampfOrtsinfo(),
                competition.getWettkampfStrasse(),
                competition.getWettkampfPlz(),
                event.getVeranstaltungID(),
                event.getVeranstaltungName(),
                event.getVeranstaltungSportJahr(),
                event.getVeranstaltungLigaName(),
                event.getVeranstaltungWettkampftypName()
        );
    }

    //================================================================================
    // Validation Methods
    //================================================================================

    /**
     * Validates that arrow values are within the valid range (0-10)
     */
    private void validateArrowValues(SchuetzenSatzDO satz) {
        if (satz == null) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, 
                "Satzeingabe cannot be null");
        }

        // Validate each arrow value (0-10 range)
        validateSingleArrowValue(satz.getSchuss1(), "Schuss 1");
        validateSingleArrowValue(satz.getSchuss2(), "Schuss 2");
        
        // Only validate schuss3 if ARROWS_PER_SHOOTER >= 3
        if (ARROWS_PER_SHOOTER >= 3) {
            validateSingleArrowValue(satz.getSchuss3(), "Schuss 3");
        }
    }

    /**
     * Validates a single arrow value
     */
    private void validateSingleArrowValue(Integer arrowValue, String arrowName) {
        if (arrowValue != null && (arrowValue < 0 || arrowValue > 10)) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, 
                arrowName + " value " + arrowValue + " is invalid. Must be between 0 and 10.");
        }
    }

    /**
     * Validates that the shooter was properly registered in schützenmeldung
     */
    private void validateShooterRegistration(long wettkampfId, long teamId, long matchNr, 
                                           long passeNr, long shooterId) {
        try {
            Optional<PasseDO> registrationPasse = passeComponent.findByPkOptional(
                wettkampfId, matchNr, teamId, 1L, shooterId);
            
            if (registrationPasse.isEmpty()) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, 
                    "Shooter " + shooterId + " was not registered in Schützenmeldung for this match");
            }
        } catch (Exception e) {
            LOGGER.error("Error validating shooter registration for shooterId={}: {}", shooterId, e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, 
                "Could not validate shooter registration: " + e.getMessage());
        }
    }

    /**
     * Validates that the match is not already complete
     */
    private void validateMatchNotComplete(long teamId, TabletSchusszettelEntity session) {
        try {
            // Use shared service for consistent match completion detection
            boolean isComplete = matchAnalysisService.isMatchComplete(
                session.getCurrentMatchId(), teamId, session.getGegnerTeamId());
            
            if (isComplete) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, 
                    "Cannot enter scores - match is already complete");
            }
        } catch (BusinessException e) {
            throw e; // Re-throw business exceptions
        } catch (Exception e) {
            LOGGER.error("Error validating match completion status: {}", e.getMessage());
            // Allow score entry if we can't determine match status
        }
    }

    /**
     * validation for schützenmeldung
     * Validates team roster and prevents duplicates
     */
    private void validateSchützenmeldungTeamRoster(long teamId, List<Long> registeredShooterIds) {
        // 1) Get all team members with deployment status >= 1
        List<MannschaftsmitgliedDO> teamMembers = mmComponent.findByTeamId(teamId).stream()
                .filter(m -> m.getDsbMitgliedEingesetzt() != null && m.getDsbMitgliedEingesetzt() >= 1)
                .toList();

        Set<Long> validMemberIds = teamMembers.stream()
                .map(MannschaftsmitgliedDO::getDsbMitgliedId)
                .collect(Collectors.toSet());

        // 2) Validate exactly 3 shooters
        if (registeredShooterIds.size() != SHOOTERS_PER_TEAM) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Exactly " + SHOOTERS_PER_TEAM + " shooters required, got " + registeredShooterIds.size());
        }

        // 3) Check for duplicates
        Set<Long> uniqueShooters = new HashSet<>(registeredShooterIds);
        if (uniqueShooters.size() != registeredShooterIds.size()) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Duplicate shooters not allowed in registration");
        }

        // 4) Validate all shooters belong to team and are deployed
        for (Long shooterId : registeredShooterIds) {
            if (!validMemberIds.contains(shooterId)) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                        "Shooter " + shooterId + " is not a valid deployed member of team " + teamId);
            }
        }
    }

    /**
     * OPTIMIZED: Build set results using existing infrastructure.
     * Uses established patterns from MatchAnalysisService and PasseComponent.
     */
    private List<SatzErgebnisDO> buildSatzErgebnisseLocal(List<PasseDO> teamPasses, List<PasseDO> oppPasses, 
                                                         long teamId, long oppTeamId) {
        List<SatzErgebnisDO> results = new ArrayList<>();
        
        // Use established pattern from MatchAnalysisService for grouping passes
        Map<Long, List<PasseDO>> teamPassesBySet = teamPasses.stream()
                .collect(Collectors.groupingBy(PasseDO::getPasseLfdnr));
        Map<Long, List<PasseDO>> oppPassesBySet = oppPasses.stream()
                .collect(Collectors.groupingBy(PasseDO::getPasseLfdnr));
        
        // Process each set using MAX_SETS constant (matches existing patterns)
        for (long setNumber = 1; setNumber <= MAX_SETS; setNumber++) {
            List<PasseDO> teamSetPasses = teamPassesBySet.getOrDefault(setNumber, new ArrayList<>());
            List<PasseDO> oppSetPasses = oppPassesBySet.getOrDefault(setNumber, new ArrayList<>());
            
            // Only include sets where both teams have data (follows existing logic)
            if (!teamSetPasses.isEmpty() && !oppSetPasses.isEmpty()) {
                // Use the existing pattern from buildMatchPunkte for arrow summation
                int teamPoints = calculateSetPointsUsingEstablishedPattern(teamSetPasses);
                int oppPoints = calculateSetPointsUsingEstablishedPattern(oppSetPasses);
                
                // Use SatzErgebnisDO constructor pattern from existing code
                SatzErgebnisDO satzResult = new SatzErgebnisDO(
                        Math.toIntExact(setNumber),
                        teamPoints,
                        oppPoints
                );
                satzResult.setTeam1Id(teamId);
                satzResult.setTeam2Id(oppTeamId);
                results.add(satzResult);
            }
        }
        
        return results;
    }
    
    /**
     * REUSED: Calculate set points using the same pattern as buildMatchPunkte().
     * Follows established arrow summation logic from existing codebase.
     */
    private int calculateSetPointsUsingEstablishedPattern(List<PasseDO> passes) {
        // Use the same null-safe arrow summation pattern as buildMatchPunkte()
        return passes.stream()
                .mapToInt(p -> {
                    int a = p.getPfeil1() != null ? p.getPfeil1() : 0;
                    int b = p.getPfeil2() != null ? p.getPfeil2() : 0;
                    int c = p.getPfeil3() != null ? p.getPfeil3() : 0;
                    // Only sum first 3 arrows (follows archery rules and existing patterns)
                    return a + b + c;
                })
                .sum();
    }
    
    /**
     * CRITICAL: Updates match.satzpunkte in database after set completion.
     * This ensures LigamatchBE view shows correct scores for state machine logic.
     */
    private void updateMatchScoresAfterSetCompletion(long wettkampfId, long teamId, 
                                                    TabletSchusszettelEntity session, int completedPasseNr) {
        try {
            LOGGER.debug("Updating match scores for team {} after completing passe {}", teamId, completedPasseNr);
            
            // Get all passes for the completed set from both teams
            long matchId = session.getCurrentMatchId();
            long opponentTeamId = session.getGegnerTeamId();
            
            // REUSE: Use existing infrastructure to get passes
            List<PasseDO> teamPasses = passeComponent.findByMannschaftMatchId(teamId, matchId).stream()
                    .filter(p -> p.getPasseLfdnr() == completedPasseNr)
                    .toList();
            List<PasseDO> oppPasses = passeComponent.findByMannschaftMatchId(opponentTeamId, matchId).stream()
                    .filter(p -> p.getPasseLfdnr() == completedPasseNr)
                    .toList();
            
            // Only update if both teams have completed the set (3 shooters each)
            if (teamPasses.size() >= SHOOTERS_PER_TEAM && oppPasses.size() >= SHOOTERS_PER_TEAM) {
                // REUSE: Use established calculation pattern
                int teamSetPoints = calculateSetPointsUsingEstablishedPattern(teamPasses);
                int oppSetPoints = calculateSetPointsUsingEstablishedPattern(oppPasses);
                
                // Calculate Satzpunkte using official archery rules
                int teamSatzpunkte = 0;
                int oppSatzpunkte = 0;
                
                if (teamSetPoints > oppSetPoints) {
                    teamSatzpunkte = 2; // Winner gets 2 Satzpunkte
                    oppSatzpunkte = 0;  // Loser gets 0 Satzpunkte
                } else if (oppSetPoints > teamSetPoints) {
                    teamSatzpunkte = 0; // Loser gets 0 Satzpunkte
                    oppSatzpunkte = 2;  // Winner gets 2 Satzpunkte
                } else {
                    teamSatzpunkte = 1; // Tie: both teams get 1 Satzpunkt
                    oppSatzpunkte = 1;
                }
                
                // REUSE: Use existing infrastructure to update match scores
                updateTeamMatchScores(matchId, teamId, teamSatzpunkte);
                updateTeamMatchScores(matchId, opponentTeamId, oppSatzpunkte);
                
                LOGGER.info("Updated match scores: Team {} (+{} Satzpunkte), Opponent {} (+{} Satzpunkte) for set {}", 
                           teamId, teamSatzpunkte, opponentTeamId, oppSatzpunkte, completedPasseNr);
            } else {
                LOGGER.debug("Set {} not complete for both teams - Team {}: {} passes, Opponent {}: {} passes", 
                           completedPasseNr, teamId, teamPasses.size(), opponentTeamId, oppPasses.size());
            }
            
        } catch (Exception e) {
            LOGGER.error("Error updating match scores for team {} after passe {}: {}", 
                        teamId, completedPasseNr, e.getMessage());
            // Don't throw - score calculation failure shouldn't break session progression
        }
    }
    
    /**
     * Updates a single team's match record with additional Satzpunkte using existing infrastructure.
     */
    private void updateTeamMatchScores(long matchId, long teamId, int additionalSatzpunkte) {
        try {
            // REUSE: Get existing match data using established infrastructure
            MatchDO match = matchComponent.findById(matchId);
            if (match == null || !Objects.equals(match.getMannschaftId(), teamId)) {
                LOGGER.warn("Match {} not found for team {} - cannot update scores", matchId, teamId);
                return;
            }
            
            // Update Satzpunkte (add to existing value)
            int currentSatzpunkte = Math.toIntExact(match.getSatzpunkte() != null ? match.getSatzpunkte() : 0);
            int newSatzpunkte = currentSatzpunkte + additionalSatzpunkte;
            match.setSatzpunkte((long) newSatzpunkte);
            
            // Check if match is won (6+ Satzpunkte) and update Matchpunkte
            if (newSatzpunkte >= MatchAnalysisService.MATCH_POINTS_TO_WIN) {
                match.setMatchpunkte(2L); // Winner gets 2 Matchpunkte
                LOGGER.info("Team {} won match {} with {} Satzpunkte", teamId, matchId, newSatzpunkte);
            }
            
            // REUSE: Use existing infrastructure to persist changes
            matchComponent.update(match, 0L);
            
            LOGGER.debug("Updated match {} for team {}: Satzpunkte={}, Matchpunkte={}", 
                        matchId, teamId, newSatzpunkte, match.getMatchpunkte());
            
        } catch (Exception e) {
            LOGGER.error("Error updating match record {} for team {}: {}", matchId, teamId, e.getMessage());
        }
    }
}
