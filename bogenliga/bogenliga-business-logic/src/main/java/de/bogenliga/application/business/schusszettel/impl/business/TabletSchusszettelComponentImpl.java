package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.match.api.types.MatchDO;
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
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
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

    // Tablet sessions are token-based and have no logged-in user; persisted
    // changes are attributed to the system user (id 0, see test data created_by)
    private static final long SYSTEM_USER_ID = 0L;

    // Essential components for delegation to state objects
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
        } else if (State.STATUS_MATCH_ENDE.equals(runtime.getCurrentState())) {
            // Handle MATCH_ENDE progression - advance to next match or tournament end
            if (runtime.evaluateWithOpponentWAITstate(null)) {
                LOGGER.info("GET request triggered state advancement for team {} from MATCH_ENDE to next match", teamId);
            }
        }

        // Build response using state objects
        TabletSchusszettelDO result = buildBaseResponse(teamId, runtime);
        enrichResponseByState(runtime, result);

        // eigenesTeamMatchId wird erst im State-Enrichment gesetzt
        if (result.getEigenesTeamMatchId() != null) {
            try {
                final MatchDO ownMatch = matchComponent.findById(result.getEigenesTeamMatchId());
                if (ownMatch != null) {
                    result.setEigenesTeamScheibennummer(ownMatch.getMatchScheibennummer());
                }
            } catch (Exception e) {
                LOGGER.debug("Could not load own team Scheibennummer for matchId={}", result.getEigenesTeamMatchId(), e);
            }
        }

        return result;
    }

    /**
     * POST request handler - shooter registration via state objects.
     */
    @Override
    public void submitSchuetzen(long wettkampfId, long teamId, String token, SchuetzenMeldungDO input) {
        SessionRuntime runtime = validateTokenAndLoadSession(wettkampfId, teamId, token);
        if (runtime == null) {
            throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR, "Invalid or expired token");
        }

        // Delegate to state object
        try {
            StateContext context = createStateContext(runtime);
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
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Internal error processing shooter registration: " + e.getMessage());
        }
    }

    /**
     * POST request handler - score submission via state objects.
     */
    @Override
    public void submitSatz(long wettkampfId, long teamId, String token, SatzEingabeDO eingabe) {
        SessionRuntime runtime = validateTokenAndLoadSession(wettkampfId, teamId, token);
        if (runtime == null) {
            throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR, "Invalid or expired token");
        }

        // Delegate to state object
        try {
            StateContext context = createStateContext(runtime);
            State currentState = State.fromString(runtime.getCurrentState());
            
            if (!currentState.validateOperation(context, "submitSatz", eingabe)) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Score submission validation failed");
            }
            
            if (!currentState.handlePostOperation(context, "submitSatz", eingabe)) {
                throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Failed to process score submission");
            }
            
            // CRITICAL: Update match scores after set completion
            updateMatchScoresAfterSetCompletion(runtime.getCurrentMatchId(), teamId, runtime.getOpponentTeamId());
            
            // Handle opponent synchronization
            handleOpponentSynchronization(wettkampfId, teamId, runtime);
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Internal error processing score submission: " + e.getMessage());
        }
    }

    // ================================================================================
    // Private helper methods
    // ================================================================================

    /**
     * Validates token and loads session runtime.
     * 
     * @param wettkampfId Competition ID
     * @param teamId Team ID
     * @param token Authentication token
     * @return SessionRuntime instance or null if invalid
     */
    private SessionRuntime validateTokenAndLoadSession(long wettkampfId, long teamId, String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        return SessionRuntime.loadFromDatabase(wettkampfId, teamId, token, sessionDAO, matchComponent, passeComponent, matchAnalysisService, mmComponent, mitgliedComponent, wettkampfComponent, veranstaltungComponent);
    }

    @Override
    public boolean isValidToken(long wettkampfid, long teamid, String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return sessionDAO.findByTokenWettkampfUndTeam(wettkampfid, teamid, token).isPresent();
    }

    /**
     * Builds base response with core match data.
     * 
     * @param teamId Team ID
     * @param runtime Session runtime instance
     * @return Base response with core match data
     */
    private TabletSchusszettelDO buildBaseResponse(long teamId, SessionRuntime runtime) {
        TabletSchusszettelDO.TabletSchusszettelStatus statusEnum = convertSessionStatus(runtime.getCurrentState());
        
        TabletSchusszettelDO result = new TabletSchusszettelDO();
        result.setStatus(statusEnum);
        
        // Basic match data with actual team names
        result.setEigenesTeam(new TeamInfoDO(teamId, getTeamName(teamId)));
        result.setGegnerischesTeam(new TeamInfoDO(runtime.getOpponentTeamId(), getTeamName(runtime.getOpponentTeamId())));

        // Load match numbers for frontend navigation over matchComponent
        loadAndSetMatchNumbers(result, runtime);

        result.setSatzErgebnisse(Collections.emptyList());
        result.setSchuetzenMatchPunkte(Collections.emptyList());
        result.setMatchErgebnis(Collections.emptyList());
        
        return result;
    }

    /**
     * Get actual team name - just the verein name without team number.
     */
    private String getTeamName(long teamId) {
        try {
            final DsbMannschaftDO team = mannschaftComponent.findById(teamId);
            final VereinDO verein = vereinComponent.findById(team.getVereinId());
            return verein.getName();
        } catch (Exception e) {
            return "Team " + teamId; // Fallback to technical name
        }
    }

    private void loadAndSetMatchNumbers(TabletSchusszettelDO result, SessionRuntime runtime) {
        try {
            // Load eigenes Team Match-Nr
            long eigenesTeamMatchId = runtime.getCurrentMatchId();
            if (eigenesTeamMatchId > 0) {
                MatchDO eigenesTeamMatch = matchComponent.findById(eigenesTeamMatchId);
                if (eigenesTeamMatch != null && eigenesTeamMatch.getNr() != null) {
                    result.setEigenesTeamMatchNr(Math.toIntExact(eigenesTeamMatch.getNr()));
                } else {
                    result.setEigenesTeamMatchNr(null);
                }
            } else {
                result.setEigenesTeamMatchNr(null);
            }
        } catch (Exception e) {
            LOGGER.warn("Match Nummern konnten nicht geladen werden: {}", e.getMessage());
            result.setEigenesTeamMatchNr(null);
        }
    }


    /**
     * Enriches response with state-specific data using state objects.
     */
    private void enrichResponseByState(SessionRuntime runtime, TabletSchusszettelDO result) {
        try {
            StateContext context = createStateContext(runtime);
            State currentState = State.fromString(runtime.getCurrentState());
            
            Map<String, Object> stateData = currentState.prepareResponseData(context);
            applyStateDataToResult(result, stateData);
            
        } catch (Exception e) {
            result.setSchuetzeStammDaten(Collections.emptyList());
            result.setVerfuegbareSchuetzen(Collections.emptyList());
        }
    }

    /**
     * Updates match scores after set completion.
     * Without this, PasseDO records are updated but match.satzpunkte stays stale,
     * causing LigamatchBE to show incorrect scores and state machine to use wrong completion data.
     * 
     * @param matchId Current match ID
     * @param teamId Team that just completed the set
     * @param opponentTeamId Opponent team ID
     */
    void updateMatchScoresAfterSetCompletion(long matchId, long teamId, long opponentTeamId) {
        try {
            // Each team of a Begegnung has its own match row - the opponent's passes are
            // stored under the opponent's match id, not under our matchId.
            MatchDO teamMatch = matchComponent.findById(matchId);
            if (teamMatch == null) {
                return;
            }
            MatchDO opponentMatch = findOpponentMatch(teamMatch, opponentTeamId);

            // 1. Calculate set winner using existing business logic
            // Get all passe records for current match to determine set results
            List<PasseDO> teamPasses = passeComponent.findByMannschaftMatchId(teamId, matchId);
            List<PasseDO> opponentPasses = opponentMatch != null
                ? passeComponent.findByMannschaftMatchId(opponentTeamId, opponentMatch.getId())
                : Collections.emptyList();

            // Calculate satzpunkte for each completed set
            int teamSatzpunkte = 0;
            int opponentSatzpunkte = 0;
            
            // Group passes by passe number (set number) - using proper DO methods
            Map<Integer, List<PasseDO>> teamPassesBySet = teamPasses.stream()
                .collect(Collectors.groupingBy(p -> p.getPasseLfdnr().intValue()));
            Map<Integer, List<PasseDO>> opponentPassesBySet = opponentPasses.stream()
                .collect(Collectors.groupingBy(p -> p.getPasseLfdnr().intValue()));
            
            // Calculate score for each completed set (max 5 sets)
            int completedSets = 0;
            for (int setNumber = 1; setNumber <= 5; setNumber++) {
                List<PasseDO> teamSetPasses = teamPassesBySet.get(setNumber);
                List<PasseDO> opponentSetPasses = opponentPassesBySet.get(setNumber);

                if (teamSetPasses != null && opponentSetPasses != null &&
                    teamSetPasses.size() >= 3 && opponentSetPasses.size() >= 3) {
                    completedSets++;

                    // Calculate total score for this set
                    int teamSetScore = calculateSetScore(teamSetPasses);
                    int opponentSetScore = calculateSetScore(opponentSetPasses);

                    // Award satzpunkte according to archery rules
                    if (teamSetScore > opponentSetScore) {
                        teamSatzpunkte += 2; // Winner gets 2 points
                    } else if (opponentSetScore > teamSetScore) {
                        opponentSatzpunkte += 2; // Winner gets 2 points
                    } else {
                        // Tie - each team gets 1 point
                        teamSatzpunkte += 1;
                        opponentSatzpunkte += 1;
                    }
                }
            }

            // 2. Update match.satzpunkte via MatchComponent.update()
            teamMatch.setSatzpunkte((long) teamSatzpunkte);

            // 3. Set Matchpunkte once the match is decided (6+ Satzpunkte or 5:5 after 5 sets)
            if (teamSatzpunkte >= 6) {
                teamMatch.setMatchpunkte(2L); // Winner gets 2 match points
                LOGGER.info("Team {} won match {} with {} satzpunkte", teamId, matchId, teamSatzpunkte);
            } else if (opponentSatzpunkte >= 6) {
                teamMatch.setMatchpunkte(0L); // Loser gets 0 match points
                LOGGER.info("Team {} lost match {} with {} satzpunkte (opponent: {})",
                           teamId, matchId, teamSatzpunkte, opponentSatzpunkte);
            } else if (completedSets >= 5 && teamSatzpunkte == opponentSatzpunkte) {
                teamMatch.setMatchpunkte(1L); // 5:5 draw - both teams get 1 match point
            }

            // Update the match in database
            // System-User 0 verwenden: MatchComponentImpl.update() lehnt negative
            // User-IDs per Precondition ab, die Exception wuerde hier still
            // verschluckt und die Satzpunkte blieben dauerhaft NULL.
            matchComponent.update(teamMatch, SYSTEM_USER_ID);

            // Also update opponent's match record
            if (opponentMatch != null) {
                opponentMatch.setSatzpunkte((long) opponentSatzpunkte);

                if (opponentSatzpunkte >= 6) {
                    opponentMatch.setMatchpunkte(2L); // Winner gets 2 match points
                } else if (teamSatzpunkte >= 6) {
                    opponentMatch.setMatchpunkte(0L); // Loser gets 0 match points
                } else if (completedSets >= 5 && teamSatzpunkte == opponentSatzpunkte) {
                    opponentMatch.setMatchpunkte(1L); // 5:5 draw - both teams get 1 match point
                }

                matchComponent.update(opponentMatch, SYSTEM_USER_ID);
            }

            // 4. LigamatchBE view will now reflect correct scores
            LOGGER.info("Match scores updated: Team {} = {} satzpunkte, Opponent {} = {} satzpunkte",
                       teamId, teamSatzpunkte, opponentTeamId, opponentSatzpunkte);

        } catch (Exception e) {
            // Don't throw exception here - score update failure shouldn't break the workflow
            LOGGER.error("Error updating match scores for match {}: {}", matchId, e.getMessage());
        }
    }

    /**
     * Finds the opponent's own match row (same Wettkampf, same Match-Nr, opponent team).
     */
    private MatchDO findOpponentMatch(MatchDO teamMatch, long opponentTeamId) {
        if (opponentTeamId <= 0) {
            return null;
        }
        return matchComponent.findByWettkampfId(teamMatch.getWettkampfId()).stream()
            .filter(m -> m.getMannschaftId() != null && m.getMannschaftId().equals(opponentTeamId)
                && m.getNr() != null && m.getNr().equals(teamMatch.getNr()))
            .findFirst()
            .orElse(null);
    }

    /**
     * Calculate total score for a set (3 shooters, 2-3 arrows each).
     * Uses proper PasseDO methods for accessing arrow scores.
     */
    private int calculateSetScore(List<PasseDO> setPasses) {
        return setPasses.stream()
            .mapToInt(p -> {
                int score = 0;
                if (p.getPfeil1() != null) score += p.getPfeil1();
                if (p.getPfeil2() != null) score += p.getPfeil2();
                if (p.getPfeil3() != null) score += p.getPfeil3();
                return score;
            })
            .sum();
    }

    /**
     * Creates StateContext for state object operations.
     */
    private StateContext createStateContext(SessionRuntime runtime) {
        return new StateContext(
            runtime.getSession(),
            runtime,
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
    public void handleOpponentSynchronization(long wettkampfId, long teamId, SessionRuntime runtime) {
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
