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
    public String getStateName() {
        return STATUS_SATZEINGABE;
    }
    
    @Override
    public boolean isValidState(StateContext context) {
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
        
        if (!(data instanceof SatzEingabeDO)) {
            LOGGER.warn("Invalid data type for score submission: {}", data.getClass());
            return false;
        }
        
        SatzEingabeDO eingabe = (SatzEingabeDO) data;
        
        try {
            // Validate payload structure
            if (eingabe.getSatzeingabe() == null || eingabe.getSatzeingabe().size() != SHOOTERS_PER_TEAM) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Exactly " + SHOOTERS_PER_TEAM + " shooters' scores required");
            }
            
            // Validate match is not complete
            if (context.isMatchComplete()) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Cannot enter scores - match is already complete");
            }
            
            // Validate each shooter's data
            for (SchuetzenSatzDO satz : eingabe.getSatzeingabe()) {
                validateArrowValues(satz);
                validateShooterRegistration(context, satz.getSchuetzenId());
            }
            
            return true;
            
        } catch (BusinessException e) {
            LOGGER.warn("Score submission validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean handlePostOperation(StateContext context, String operation, Object data) {
        if (!"submitSatz".equals(operation) || !(data instanceof SatzEingabeDO)) {
            return false;
        }
        
        SatzEingabeDO eingabe = (SatzEingabeDO) data;
        
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
     */
    private void createPassesWithScores(StateContext context, SatzEingabeDO eingabe) {
        try {
            long matchId = context.getCurrentMatchId();
            int currentPasse = context.getCurrentPasseNumber();
            long currentMatchNr = context.getMatchComponent().findById(matchId).getNr();
            long wettkampfId = context.getWettkampfId();
            
            for (SchuetzenSatzDO satz : eingabe.getSatzeingabe()) {
                LOGGER.debug("Creating pass on-demand: wettkampfId={}, matchNr={}, teamId={}, passeNr={}, schuetzeId={}",
                            wettkampfId, currentMatchNr, context.getTeamId(), currentPasse, satz.getSchuetzenId());
                
                PasseDO passe = new PasseDO(
                    null,                           // id (generated)
                    context.getTeamId(),           // passeMannschaftId
                    wettkampfId,                   // passeWettkampfId
                    currentMatchNr,                // passeMatchNr
                    matchId,                       // passeMatchId
                    (long) currentPasse,           // passeLfdnr
                    satz.getSchuetzenId(),         // passeDsbMitgliedId
                    (ARROWS_PER_SHOOTER >= 1 ? satz.getSchuss1() : null),  // pfeil1
                    (ARROWS_PER_SHOOTER >= 2 ? satz.getSchuss2() : null),  // pfeil2
                    (ARROWS_PER_SHOOTER >= 3 ? satz.getSchuss3() : null),  // pfeil3
                    null, null, null               // pfeil4-6 not used in tablet scoring
                );
                
                context.getPasseComponent().create(passe, 0L);
            }
            
        } catch (Exception e) {
            LOGGER.error("Error creating passes with scores: {}", e.getMessage());
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR,
                "Fehler beim Speichern der Passe: " + e.getMessage());
        }
    }
    
    /**
     * Updates match scores in database for consistency with LigamatchBE.
     */
    private void updateMatchScoresAfterSetCompletion(StateContext context) {
        try {
            int completedPasseNr = context.getCurrentPasseNumber();
            long matchId = context.getCurrentMatchId();
            long teamId = context.getTeamId();
            long opponentTeamId = context.getOpponentTeamId();
            
            LOGGER.debug("Updating match scores for team {} after completing passe {}", teamId, completedPasseNr);
            
            // Get passes for completed set from both teams
            List<PasseDO> teamPasses = context.getPasseComponent()
                .findByMannschaftMatchId(teamId, matchId).stream()
                .filter(p -> p.getPasseLfdnr() == completedPasseNr)
                .toList();
                
            List<PasseDO> oppPasses = context.getPasseComponent()
                .findByMannschaftMatchId(opponentTeamId, matchId).stream()
                .filter(p -> p.getPasseLfdnr() == completedPasseNr)
                .toList();
            
            // Only update if both teams have completed the set (3 shooters each)
            if (teamPasses.size() >= SHOOTERS_PER_TEAM && oppPasses.size() >= SHOOTERS_PER_TEAM) {
                int teamSetPoints = calculateSetPoints(teamPasses);
                int oppSetPoints = calculateSetPoints(oppPasses);
                
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
                
                // Update match scores
                updateTeamMatchScores(context, matchId, teamId, teamSatzpunkte);
                updateTeamMatchScores(context, matchId, opponentTeamId, oppSatzpunkte);
                
                LOGGER.info("Updated match scores: Team {} (+{} Satzpunkte), Opponent {} (+{} Satzpunkte) for set {}",
                           teamId, teamSatzpunkte, opponentTeamId, oppSatzpunkte, completedPasseNr);
            }
            
        } catch (Exception e) {
            LOGGER.error("Error updating match scores: {}", e.getMessage());
            // Don't throw - score calculation failure shouldn't break session progression
        }
    }
    
    /**
     * Calculates total arrow points for a set.
     */
    private int calculateSetPoints(List<PasseDO> passes) {
        return passes.stream()
            .mapToInt(p -> {
                int a = p.getPfeil1() != null ? p.getPfeil1() : 0;
                int b = p.getPfeil2() != null ? p.getPfeil2() : 0;
                int c = p.getPfeil3() != null ? p.getPfeil3() : 0;
                return a + b + c;
            })
            .sum();
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
    private void validateArrowValues(SchuetzenSatzDO satz) {
        if (satz == null) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                "Satzeingabe cannot be null");
        }
        
        validateSingleArrowValue(satz.getSchuss1(), "Schuss 1");
        validateSingleArrowValue(satz.getSchuss2(), "Schuss 2");
        
        if (ARROWS_PER_SHOOTER >= 3) {
            validateSingleArrowValue(satz.getSchuss3(), "Schuss 3");
        }
    }
    
    /**
     * Validates a single arrow value.
     */
    private void validateSingleArrowValue(Integer arrowValue, String arrowName) {
        if (arrowValue != null && (arrowValue < 0 || arrowValue > 10)) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                arrowName + " value " + arrowValue + " is invalid. Must be between 0 and 10.");
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
}