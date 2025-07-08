package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.business.schusszettel.api.types.inside.*;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.business.domain.SessionRuntime;
import de.bogenliga.application.business.schusszettel.impl.business.domain.states.State;
import de.bogenliga.application.business.schusszettel.impl.business.domain.states.StateContext;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.schusszettel.api.types.inside.WettkampfInfoDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Tablet schusszettel orchestration layer - delegates all business logic to state objects.
 *
 * <h2>CURRENT ARCHITECTURE</h2>
 * This component is a thin orchestration layer that delegates all business logic to state objects.
 * It handles request/response transformation and coordinates tablet operations through the state pattern.
 *
 * <h2>CURRENT RESPONSIBILITIES</h2>
 * <ul>
 *   <li>HTTP request validation and DTO transformation</li>
 *   <li>SessionRuntime creation and token validation</li>
 *   <li>State object delegation for all business operations</li>
 *   <li>Response data assembly from state object results</li>
 * </ul>
 *
 * <h2>STATE OBJECT DELEGATION</h2>
 * <ul>
 *   <li>GET operations: SessionRuntime state evaluation + state.prepareResponseData()</li>
 *   <li>POST operations: state.validateOperation() + state.handlePostOperation()</li>
 *   <li>State transitions: Handled entirely within state objects</li>
 *   <li>Opponent synchronization: Coordinated through SessionRuntime</li>
 * </ul>
 *
 * @author Marty Lauterbach - Clean architecture implementation
 */
@Service
public class TabletSchusszettelComponentImpl implements TabletSchusszettelComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSchusszettelComponentImpl.class);

    // State constant for synchronization
    private static final String STATUS_WARTE = State.STATUS_WARTE;

    // Essential components for delegation to state objects
    private final TabletSchusszettelDAO sessionDAO;
    private final PasseComponent passeComponent;
    private final MatchComponent matchComponent;
    private final MatchAnalysisService matchAnalysisService;
    private final MannschaftsmitgliedComponent mmComponent;
    private final DsbMitgliedComponent mitgliedComponent;
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
            WettkampfComponent wettkampfComponent,
            VeranstaltungComponent veranstaltungComponent) {

        this.sessionDAO = sessionDAO;
        this.passeComponent = passeComponent;
        this.matchComponent = matchComponent;
        this.matchAnalysisService = matchAnalysisService;
        this.mmComponent = mmComponent;
        this.mitgliedComponent = mitgliedComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
    }

    /**
     * GET request handler - returns current session state with state object data.
     */
    @Override
    public TabletSchusszettelDO getStatus(long wettkampfId, long teamId, String token) {
        // Validate token and load session
        SessionRuntime runtime = validateTokenAndLoadSession(wettkampfId, teamId, token);
        if (runtime == null) {
            return notAllowed();
        }

        // Let session runtime handle state evaluation
        runtime.checkAgainstDatabase();
        
        if (STATUS_WARTE.equals(runtime.getCurrentState())) {
            SessionRuntime opponentRuntime = runtime.loadOpponentSession();
            if (runtime.evaluateWithOpponentWAITstate(opponentRuntime != null ? opponentRuntime.getSession() : null)) {
                LOGGER.info("GET request triggered state advancement for team {} from WARTE", teamId);
            }
        }

        // Build response using state objects
        TabletSchusszettelDO result = buildBaseResponse(wettkampfId, teamId, runtime);
        enrichResponseByState(runtime, result);

        return result;
    }

    /**
     * POST request handler - shooter registration via state objects.
     */
    @Override
    public void submitSchuetzen(long wettkampfId, long teamId, String token, SchuetzenMeldungDO input) {
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR, "Token argument is empty");
        }

        SessionRuntime runtime = SessionRuntime.loadFromDatabase(
                wettkampfId, teamId, token,
                sessionDAO, matchComponent, passeComponent, matchAnalysisService, mmComponent, mitgliedComponent, wettkampfComponent, veranstaltungComponent);

        if (runtime == null) {
            throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR, "Invalid or expired token");
        }

        // Delegate to state object
        try {
            StateContext context = createStateContext(runtime.getSession());
            State currentState = State.fromString(runtime.getCurrentState());
            
            if (input == null || input.getGemeldeteSchuetzen() == null) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Invalid registration data");
            }
            
            List<Long> shooterIds = input.getGemeldeteSchuetzen();
            
            if (!currentState.validateOperation(context, "submitSchuetzen", shooterIds)) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Shooter registration validation failed");
            }
            
            if (!currentState.handlePostOperation(context, "submitSchuetzen", shooterIds)) {
                throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to process shooter registration");
            }
            
            LOGGER.info("Successfully processed shooter registration for team {} via state object", teamId);
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error in submitSchuetzen for team {}: {}", teamId, e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Internal error processing shooter registration: " + e.getMessage());
        }
    }

    /**
     * POST request handler - score submission via state objects.
     */
    @Override
    public void submitSatz(long wettkampfId, long teamId, String token, SatzEingabeDO eingabe) {
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR, "Token argument is empty");
        }

        SessionRuntime runtime = SessionRuntime.loadFromDatabase(
                wettkampfId, teamId, token,
                sessionDAO, matchComponent, passeComponent, matchAnalysisService, mmComponent, mitgliedComponent, wettkampfComponent, veranstaltungComponent);

        if (runtime == null) {
            throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR, "Invalid or expired token");
        }

        // Delegate to state object
        try {
            StateContext context = createStateContext(runtime.getSession());
            State currentState = State.fromString(runtime.getCurrentState());
            
            if (!currentState.validateOperation(context, "submitSatz", eingabe)) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Score submission validation failed");
            }
            
            if (!currentState.handlePostOperation(context, "submitSatz", eingabe)) {
                throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to process score submission");
            }
            
            // Handle opponent synchronization
            handleOpponentSynchronization(wettkampfId, teamId, runtime);
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error in submitSatz for team {}: {}", teamId, e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Internal error processing score submission: " + e.getMessage());
        }
    }

    // ================================================================================
    // Private helper methods
    // ================================================================================

    /**
     * Validates token and loads session runtime.
     */
    private SessionRuntime validateTokenAndLoadSession(long wettkampfId, long teamId, String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        return SessionRuntime.loadFromDatabase(wettkampfId, teamId, token, sessionDAO, matchComponent, passeComponent, matchAnalysisService, mmComponent, mitgliedComponent, wettkampfComponent, veranstaltungComponent);
    }

    /**
     * Builds base response with core match data.
     */
    private TabletSchusszettelDO buildBaseResponse(long wettkampfId, long teamId, SessionRuntime runtime) {
        TabletSchusszettelDO.TabletSchusszettelStatus statusEnum = convertSessionStatus(runtime.getCurrentState());
        
        TabletSchusszettelDO result = new TabletSchusszettelDO();
        result.setStatus(statusEnum);
        
        // Basic match data - let state objects handle detailed data
        result.setEigenesTeam(new TeamInfoDO(teamId, "Team " + teamId));
        result.setGegnerischesTeam(new TeamInfoDO(runtime.getOpponentTeamId(), "Team " + runtime.getOpponentTeamId()));
        result.setSatzErgebnisse(Collections.emptyList());
        result.setSchuetzenMatchPunkte(Collections.emptyList());
        result.setMatchErgebnis(Collections.emptyList());
        
        return result;
    }

    /**
     * Enriches response with state-specific data using state objects.
     */
    private void enrichResponseByState(SessionRuntime runtime, TabletSchusszettelDO result) {
        try {
            StateContext context = createStateContext(runtime.getSession());
            State currentState = State.fromString(runtime.getCurrentState());
            
            Map<String, Object> stateData = currentState.prepareResponseData(context);
            applyStateDataToResult(result, stateData);
            
        } catch (Exception e) {
            LOGGER.error("Error enriching response with state data: {}", e.getMessage());
            result.setSchuetzeStammDaten(Collections.emptyList());
            result.setVerfuegbareSchuetzen(Collections.emptyList());
        }
    }

    /**
     * Creates StateContext for state object operations.
     */
    private StateContext createStateContext(TabletSchusszettelEntity session) {
        return new StateContext(
            session,
            sessionDAO,
            matchComponent,
            passeComponent,
            matchAnalysisService,
            mmComponent,
            mitgliedComponent,
            wettkampfComponent,
            veranstaltungComponent
        );
    }

    /**
     * Applies state object data to result DTO.
     */
    @SuppressWarnings("unchecked")
    private void applyStateDataToResult(TabletSchusszettelDO result, Map<String, Object> stateData) {
        // Core session data (provided by all states)
        if (stateData.containsKey("currentPasseNumber")) {
            result.setCurrentPasseNumber((Integer) stateData.get("currentPasseNumber"));
        }
        if (stateData.containsKey("eigenesTeamMatchId")) {
            result.setEigenesTeamMatchId((Long) stateData.get("eigenesTeamMatchId"));
        }
        if (stateData.containsKey("gegnerischesTeamMatchId")) {
            result.setGegnerischesTeamMatchId((Long) stateData.get("gegnerischesTeamMatchId"));
        }
        if (stateData.containsKey("wettkampfInfo")) {
            result.setWettkampfInfo((WettkampfInfoDO) stateData.get("wettkampfInfo"));
        }
        
        // State-specific data
        if (stateData.containsKey("schuetzeStammDaten")) {
            result.setSchuetzeStammDaten((List<SchuetzeStammdatenDO>) stateData.get("schuetzeStammDaten"));
        }
        if (stateData.containsKey("verfuegbareSchuetzen")) {
            result.setVerfuegbareSchuetzen((List<VerfuegbarerSchuetzeDO>) stateData.get("verfuegbareSchuetzen"));
        }
        if (stateData.containsKey("satzErgebnisse")) {
            result.setSatzErgebnisse((List<SatzErgebnisDO>) stateData.get("satzErgebnisse"));
        }
        if (stateData.containsKey("schuetzenMatchPunkte")) {
            result.setSchuetzenMatchPunkte((List<SchuetzeMatchPunkteDO>) stateData.get("schuetzenMatchPunkte"));
        }
        if (stateData.containsKey("matchErgebnis")) {
            result.setMatchErgebnis((List<TeamMatchInfoDO>) stateData.get("matchErgebnis"));
        }
    }

    /**
     * Handles opponent synchronization after score submission.
     */
    private void handleOpponentSynchronization(long wettkampfId, long teamId, SessionRuntime runtime) {
        try {
            TabletSchusszettelEntity session = runtime.getSession();
            
            if (!STATUS_WARTE.equals(session.getStatus())) {
                return; // No synchronization needed
            }
            
            TabletSchusszettelEntity opponentSession = sessionDAO
                    .findByWettkampfUndTeam(wettkampfId, session.getGegnerTeamId())
                    .orElse(null);
            
            if (opponentSession != null) {
                boolean evaluationResult = runtime.evaluateWithOpponentWAITstate(opponentSession);
                if (evaluationResult) {
                    LOGGER.info("Both teams synchronized - advanced team {} from WARTE", teamId);
                }
            }
            
        } catch (Exception e) {
            LOGGER.warn("Error in opponent synchronization for team {}: {}", teamId, e.getMessage());
        }
    }

    /**
     * Converts session status string to enum.
     */
    private TabletSchusszettelDO.TabletSchusszettelStatus convertSessionStatus(String status) {
        try {
            return TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Unknown session status: " + status);
        }
    }

    /**
     * Returns NOT_ALLOWED response.
     */
    private TabletSchusszettelDO notAllowed() {
        TabletSchusszettelDO dto = new TabletSchusszettelDO();
        dto.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED);
        return dto;
    }
}