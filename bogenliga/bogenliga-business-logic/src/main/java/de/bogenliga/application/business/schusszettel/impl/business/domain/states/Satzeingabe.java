package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.VerfuegbarerSchuetzeDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeStammdatenDO;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzenSatzDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Satzeingabe state - Score entry in progress.
 * 
 * <h2>STATE BEHAVIOR</h2>
 * <ul>
 *   <li>Allows arrow score entry for registered shooters</li>
 *   <li>Validates arrow values (0-10) and shooter registration</li>
 *   <li>Creates passes on-demand with score data</li>
 *   <li>Updates match scores for database consistency</li>
 *   <li>Transitions to WARTE after successful score submission</li>
 * </ul>
 *
 * @author Marty Lauterbach - Enhanced with complete business logic
 */
public class Satzeingabe extends State {
    private static final Logger LOGGER = LoggerFactory.getLogger(Satzeingabe.class);
    private static final int SHOOTERS_PER_TEAM = 3;
    private static final int ARROWS_PER_SHOOTER = 2;
    private static final int MATCH_POINTS_TO_WIN = 6;
    
    @Override
    public boolean isValidState(StateContext context) {
        // Validate basic session state first
        StateContext.ValidationResult sessionValidation = context.validateSessionState();
        if (!sessionValidation.isValid()) {
            LOGGER.warn("Session state validation failed: {}", sessionValidation.getErrorMessage());
            return false;
        }
        // Satzeingabe requires match to not be complete
        return !context.isMatchComplete();
    }
    
    @Override
    public boolean canTransitionTo(StateContext context, String targetState) {
        // Can only transition to WARTE from SATZEINGABE
        return STATUS_WARTE.equals(targetState);
    }
    
    @Override
    public boolean isDatabaseReadyForTransition(StateContext context, String targetState) {
        if (!STATUS_WARTE.equals(targetState)) {
            return false;
        }
        
        // Check that current passe is complete (3 shooters with scores)
        try {
            return context.isCurrentPasseComplete();
        } catch (Exception e) {
            LOGGER.error("Error checking database readiness for transition: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public Map<String, Object> prepareResponseData(StateContext context) {
        Map<String, Object> data = super.prepareResponseData(context);
        
        try {
            // Get registered shooters for current match
            List<Long> registeredShooters = getRegisteredShootersForCurrentMatch(context);
            
            // Check which registered shooters already have scores for current passe
            List<PasseDO> existingPasses = context.getCurrentPasseData().stream()
                .filter(p -> registeredShooters.contains(p.getPasseDsbMitgliedId()))
                .toList();
            
            // Prepare Stammdaten for registered shooters
            List<SchuetzeStammdatenDO> stammdaten = registeredShooters.stream().map(shooterId -> {
                DsbMitgliedDO member = context.getDsbMitgliedComponent().findById(shooterId);
                MannschaftsmitgliedDO teamMember = context.getMannschaftsmitgliedComponent()
                    .findByMemberAndTeamId(context.getTeamId(), shooterId);
                return new SchuetzeStammdatenDO(
                    member.getId(), 
                    Math.toIntExact(teamMember.getRueckennummer()), 
                    member.getVorname(), 
                    member.getNachname()
                );
            }).collect(Collectors.toList());
            
            // Prepare available shooters (registered but not yet scored this passe)
            Set<Long> shootersWithScores = existingPasses.stream()
                .map(PasseDO::getPasseDsbMitgliedId)
                .collect(Collectors.toSet());
            List<VerfuegbarerSchuetzeDO> available = registeredShooters.stream()
                .filter(shooterId -> !shootersWithScores.contains(shooterId))
                .map(shooterId -> {
                    DsbMitgliedDO member = context.getDsbMitgliedComponent().findById(shooterId);
                    return new VerfuegbarerSchuetzeDO(member.getId(), member.getVorname() + " " + member.getNachname());
                }).collect(Collectors.toList());
            
            data.put("schuetzeStammDaten", stammdaten);
            data.put("verfuegbareSchuetzen", available);
            
            LOGGER.debug("Prepared SATZEINGABE data: {} registered shooters, {} available for scoring",
                        registeredShooters.size(), available.size());
            
        } catch (Exception e) {
            LOGGER.error("Error preparing SATZEINGABE response data: {}", e.getMessage());
            data.put("schuetzeStammDaten", Collections.emptyList());
            data.put("verfuegbareSchuetzen", Collections.emptyList());
        }
        
        return data;
    }

    @Override
    public boolean validateOperation(StateContext context, String operation, Object data) {
        if (!"submitSatz".equals(operation)) {
            return false; // Only supports score submission
        }

        if (!(data instanceof SatzEingabeDO eingabe)) {
            LOGGER.warn("Invalid data type for score submission: {}", data.getClass());
            return false;
        }

        validateNoInconsistentShooters(context);

        // Validate payload structure
        if (eingabe.getSatzeingabe() == null
                || eingabe.getSatzeingabe().size() != SHOOTERS_PER_TEAM) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Exactly " + SHOOTERS_PER_TEAM + " shooters' scores required"
            );
        }

        // Validate match is not complete
        if (context.isMatchComplete()) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Cannot enter scores - match is already complete"
            );
        }

        // Validate each shooter's data
        for (SchuetzenSatzDO satz : eingabe.getSatzeingabe()) {
            validateArrowValues(context, satz);
            validateShooterRegistration(context, satz.getSchuetzenId());
        }

        return true;
    }
    
    @Override
    public boolean handlePostOperation(StateContext context, String operation, Object data) {
        if (!"submitSatz".equals(operation) || !(data instanceof SatzEingabeDO eingabe)) {
            return false;
        }
        
        try {
            // Create passes with scores
            createPassesWithScores(context, eingabe);
            
            // Update match scores for database consistency
            updateMatchScoresAfterSetCompletion(context);
            
            // Transition to WARTE
            context.updateSessionStatus(STATUS_WARTE);
            
            LOGGER.info("Successfully submitted scores for {} shooters from team {} and transitioned to WARTE",
                       eingabe.getSatzeingabe().size(), context.getTeamId());
            return true;
            
        } catch (Exception e) {
            LOGGER.error("Error handling score submission for team {}: {}", context.getTeamId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Creates pass records with submitted scores.
     * CRITICAL FIX: Uses team-specific passe calculation to avoid race conditions.
     */
    private void createPassesWithScores(StateContext context, SatzEingabeDO eingabe) {
        try {
            long matchId = context.getCurrentMatchId();
            long currentMatchNr = context.getMatchComponent().findById(matchId).getNr();
            long wettkampfId = context.getWettkampfId();
            
            // CRITICAL FIX: Use team-specific passe calculation instead of global/retry logic
            createPassesWithTeamSpecificPasse(context, eingabe, wettkampfId, currentMatchNr, matchId);
            
        } catch (Exception e) {
            LOGGER.error("Error creating passes with scores: {}", e.getMessage());
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                "Fehler beim Speichern der Passe: " + e.getMessage());
        }
    }
    
    /**
     * Creates passes using team-specific passe calculation.
     * CRITICAL FIX: Uses team-specific next passe instead of global calculation.
     */
    private void createPassesWithTeamSpecificPasse(StateContext context, SatzEingabeDO eingabe, 
                                                  long wettkampfId, long currentMatchNr, long matchId) {
        try {
            // CRITICAL FIX: Get the next passe number for THIS specific team
            int nextPasseForTeam = context.getMatchAnalysisService()
                .getNextPasseNumberForTeam(matchId, context.getTeamId());
            
            LOGGER.debug("Creating passes for team {} at team-specific passe {} (wettkampf={}, match={})",
                        context.getTeamId(), nextPasseForTeam, wettkampfId, matchId);
            
            // Check if passes already exist for this team at this specific passe
            if (passesAlreadyExist(context, matchId, nextPasseForTeam)) {
                LOGGER.warn("Passes already exist for team {} at passe {} - this indicates a logic error",
                           context.getTeamId(), nextPasseForTeam);
                updateExistingPasses(context, eingabe, matchId, nextPasseForTeam);
                return;
            }
            
            // Create new passes for this team's next passe
            for (SchuetzenSatzDO satz : eingabe.getSatzeingabe()) {
                PasseDO passe = new PasseDO(
                    null,                           // id (generated)
                    context.getTeamId(),           // passeMannschaftId
                    wettkampfId,                   // passeWettkampfId
                    currentMatchNr,                // passeMatchNr
                    matchId,                       // passeMatchId
                    (long) nextPasseForTeam,       // passeLfdnr - TEAM-SPECIFIC!
                    satz.getSchuetzenId(),         // passeDsbMitgliedId
                    (ARROWS_PER_SHOOTER >= 1 ? satz.getSchuss1() : null),  // pfeil1
                    (ARROWS_PER_SHOOTER >= 2 ? satz.getSchuss2() : null),  // pfeil2
                    (ARROWS_PER_SHOOTER >= 3 ? satz.getSchuss3() : null),  // pfeil3
                    null, null, null               // pfeil4-6 not used in tablet scoring
                );
                
                context.getPasseComponent().create(passe, 0L);
                LOGGER.debug("Created pass for team {} shooter {} at passe {}", 
                           context.getTeamId(), satz.getSchuetzenId(), nextPasseForTeam);
            }
            
            LOGGER.info("Successfully created {} passes for team {} at team-specific passe {}",
                       eingabe.getSatzeingabe().size(), context.getTeamId(), nextPasseForTeam);
            
        } catch (Exception e) {
            LOGGER.error("Error creating passes with team-specific calculation for team {}: {}", 
                        context.getTeamId(), e.getMessage());
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                "Failed to create passes: " + e.getMessage());
        }
    }
    
    /**
     * Checks if passes already exist for this team and passe number.
     */
    private boolean passesAlreadyExist(StateContext context, long matchId, int passeNr) {
        try {
            List<PasseDO> existingPasses = context.getPasseComponent()
                .findByMannschaftMatchId(context.getTeamId(), matchId).stream()
                .filter(p -> p.getPasseLfdnr() != null && p.getPasseLfdnr().intValue() == passeNr)
                .toList();
            
            return !existingPasses.isEmpty();
        } catch (Exception e) {
            LOGGER.warn("Error checking existing passes: {}", e.getMessage());
            return false; // Assume they don't exist if we can't check
        }
    }
    
    /**
     * Updates existing passes instead of creating new ones.
     */
    private void updateExistingPasses(StateContext context, SatzEingabeDO eingabe, 
                                     long matchId, int passeNr) {
        try {
            Map<Long, PasseDO> existingPassesMap = context.getPasseComponent()
                .findByMannschaftMatchId(context.getTeamId(), matchId).stream()
                .filter(p -> p.getPasseLfdnr() != null && p.getPasseLfdnr().intValue() == passeNr)
                .collect(Collectors.toMap(PasseDO::getPasseDsbMitgliedId, p -> p));
            
            for (SchuetzenSatzDO satz : eingabe.getSatzeingabe()) {
                PasseDO existingPasse = existingPassesMap.get(satz.getSchuetzenId());
                if (existingPasse != null) {
                    // Update arrow scores
                    if (ARROWS_PER_SHOOTER >= 1) existingPasse.setPfeil1(satz.getSchuss1());
                    if (ARROWS_PER_SHOOTER >= 2) existingPasse.setPfeil2(satz.getSchuss2());
                    if (ARROWS_PER_SHOOTER >= 3) existingPasse.setPfeil3(satz.getSchuss3());
                    
                    context.getPasseComponent().update(existingPasse, 0L);
                    LOGGER.debug("Updated existing pass for shooter {} in passe {}", 
                               satz.getSchuetzenId(), passeNr);
                }
            }
            
        } catch (Exception e) {
            LOGGER.error("Error updating existing passes: {}", e.getMessage());
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                "Failed to update existing passes: " + e.getMessage());
        }
    }
    
    /**
     * Updates match scores in database for consistency with LigamatchBE.
     * CRITICAL FIX: Uses team-specific passe calculation for score updates.
     */
    private void updateMatchScoresAfterSetCompletion(StateContext context) {
        try {
            // CRITICAL FIX: Get the actual passe number this team just completed
            long matchId = context.getCurrentMatchId();
            long teamId = context.getTeamId();
            long opponentTeamId = context.getOpponentTeamId();
            
            // Get the passe number this team just completed (highest complete passe)
            int teamCompletedPasse = context.getMatchAnalysisService()
                .getNextPasseNumberForTeam(matchId, teamId) - 1;
            
            LOGGER.debug("Updating match scores for team {} after completing passe {}", teamId, teamCompletedPasse);
            
            // Only process if this team actually completed a passe
            if (teamCompletedPasse <= 0) {
                LOGGER.debug("Team {} has not completed any passes yet - skipping score update", teamId);
                return;
            }
            
            // Get passes for completed set from both teams
            List<PasseDO> teamPasses = context.getAllMatchPasses().stream()
                .filter(p -> p.getPasseLfdnr() == teamCompletedPasse)
                .toList();
                
            List<PasseDO> oppPasses = context.getPasseComponent()
                .findByMannschaftMatchId(opponentTeamId, matchId).stream()
                .filter(p -> p.getPasseLfdnr() == teamCompletedPasse)
                .toList();
            
            // Only update if both teams have completed the set (3 shooters each)
            if (teamPasses.size() >= SHOOTERS_PER_TEAM && oppPasses.size() >= SHOOTERS_PER_TEAM) {
                int teamSetPoints = calculateSetPoints(teamPasses);
                int oppSetPoints = calculateSetPoints(oppPasses);
                
                // Calculate Satzpunkte using official archery rules
                int teamSatzpunkte;
                int oppSatzpunkte;
                
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
                
                // Update match scores
                updateTeamMatchScores(context, matchId, teamId, teamSatzpunkte);
                updateTeamMatchScores(context, matchId, opponentTeamId, oppSatzpunkte);
                
                LOGGER.info("Updated match scores: Team {} (+{} Satzpunkte), Opponent {} (+{} Satzpunkte) for set {}",
                           teamId, teamSatzpunkte, opponentTeamId, oppSatzpunkte, teamCompletedPasse);
            }
            
        } catch (Exception e) {
            LOGGER.error("Error updating match scores: {}", e.getMessage());
            // Don't throw - score calculation failure shouldn't break session progression
        }
    }
    
    
    /**
     * Updates a team's match record with additional Satzpunkte.
     */
    private void updateTeamMatchScores(StateContext context, long matchId, long teamId, int additionalSatzpunkte) {
        try {
            MatchDO match = context.getMatchComponent().findById(matchId);
            if (match == null || !Objects.equals(match.getMannschaftId(), teamId)) {
                LOGGER.warn("Match {} not found for team {} - cannot update scores", matchId, teamId);
                return;
            }
            
            // Update Satzpunkte (add to existing value)
            int currentSatzpunkte = Math.toIntExact(match.getSatzpunkte() != null ? match.getSatzpunkte() : 0);
            int newSatzpunkte = currentSatzpunkte + additionalSatzpunkte;
            match.setSatzpunkte((long) newSatzpunkte);
            
            // Check if match is won (6+ Satzpunkte) and update Matchpunkte
            if (newSatzpunkte >= MATCH_POINTS_TO_WIN) {
                match.setMatchpunkte(2L); // Winner gets 2 Matchpunkte
                LOGGER.info("Team {} won match {} with {} Satzpunkte", teamId, matchId, newSatzpunkte);
            }
            
            // Persist changes
            context.getMatchComponent().update(match, 0L);
            
            LOGGER.debug("Updated match {} for team {}: Satzpunkte={}, Matchpunkte={}",
                        matchId, teamId, newSatzpunkte, match.getMatchpunkte());
            
        } catch (Exception e) {
            LOGGER.error("Error updating match record {} for team {}: {}", matchId, teamId, e.getMessage());
        }
    }
    
    /**
     * Validates arrow values are within valid range (0-10).
     */
    private void validateArrowValues(StateContext context, SchuetzenSatzDO satz) {
        if (satz == null) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                "Satzeingabe cannot be null");
        }
        
        validateSingleArrowValue(context, satz.getSchuss1(), "Schuss 1");
        validateSingleArrowValue(context, satz.getSchuss2(), "Schuss 2");
        
        if (ARROWS_PER_SHOOTER >= 3) {
            validateSingleArrowValue(context, satz.getSchuss3(), "Schuss 3");
        }
    }
    
    /**
     * Validates a single arrow value using StateContext helper.
     */
    private void validateSingleArrowValue(StateContext context, Integer arrowValue, String arrowName) {
        StateContext.ValidationResult result = context.validateArrowValue(arrowValue);
        if (!result.isValid()) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                arrowName + ": " + result.getErrorMessage());
        }
    }
    
    /**
     * Validates that shooter was properly registered.
     */
    private void validateShooterRegistration(StateContext context, long shooterId) {
        try {
            long currentMatchId = context.getCurrentMatchId();
            long currentMatchNr = context.getMatchComponent().findById(currentMatchId).getNr();
            
            MannschaftsmitgliedDO member = context.getMannschaftsmitgliedComponent()
                .findByMemberAndTeamId(context.getTeamId(), shooterId);
            
            if (member.getDsbMitgliedEingesetzt() < currentMatchNr) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Shooter " + shooterId + " was not registered in Schützenmeldung for this match");
            }
            
        } catch (BusinessException e) {
            throw e; // Re-throw business exceptions
        } catch (Exception e) {
            LOGGER.error("Error validating shooter registration for shooterId={}: {}", shooterId, e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR,
                "Could not validate shooter registration: " + e.getMessage());
        }
    }
    
    /**
     * Gets shooters registered for the current match.
     */
    private List<Long> getRegisteredShootersForCurrentMatch(StateContext context) {
        try {
            long currentMatchId = context.getCurrentMatchId();
            long currentMatchNr = context.getMatchComponent().findById(currentMatchId).getNr();
            
            return context.getMannschaftsmitgliedComponent().findByTeamId(context.getTeamId()).stream()
                .filter(mm -> mm.getDsbMitgliedEingesetzt() != null &&
                             mm.getDsbMitgliedEingesetzt().equals(Math.toIntExact(currentMatchNr)))
                .map(MannschaftsmitgliedDO::getDsbMitgliedId)
                .distinct() // Remove duplicates
                .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Error getting registered shooters for current match: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Validates that all existing passes in the current match belong to exactly
     * {@value #SHOOTERS_PER_TEAM} distinct shooters.
     *
     * <p>If passes already exist and more than the allowed number of different
     * shooters is detected, the match is considered inconsistent and a
     * {@link BusinessException} is thrown.</p>
     *
     * @param context the current state context containing match data
     * @throws BusinessException if the match contains inconsistent shooter assignments
     */
    private void validateNoInconsistentShooters(StateContext context) {

        // Alle bisherigen Passen dieses Matches laden
        Set<Long> distinctShootersInMatch =
                context.getAllMatchPasses().stream()
                        .map(PasseDO::getPasseDsbMitgliedId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

        // Wenn es bereits Wertungen gibt und mehr als 3 verschiedene Schützen existieren → Fehler
        if (!distinctShootersInMatch.isEmpty()
                && distinctShootersInMatch.size() != SHOOTERS_PER_TEAM) {

            throw new BusinessException(
                    ErrorCode.MATCH_INKONSISTENTE_SCHUETZEN,
                    "Match enthält Wertungen mit mehr als drei unterschiedlichen Schützen"
            );
        }
    }

}