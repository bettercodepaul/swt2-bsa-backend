package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.VerfuegbarerSchuetzeDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeStammdatenDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Schuetzenmeldung state - Shooter registration required.
 * 
 * <h2>STATE BEHAVIOR</h2>
 * <ul>
 *   <li>Requires exactly 3 deployed shooters to be registered</li>
 *   <li>Validates team roster and prevents duplicates</li>
 *   <li>Prepares available shooter data for UI</li>
 *   <li>Marks shooters as deployed for current match</li>
 *   <li>Transitions to SATZEINGABE after successful registration</li>
 * </ul>
 *
 * @author Marty Lauterbach - Enhanced with complete business logic
 */
public class Schuetzenmeldung extends State {
    private static final Logger LOGGER = LoggerFactory.getLogger(Schuetzenmeldung.class);
    private static final int SHOOTERS_PER_TEAM = 3;
    
    @Override
    public boolean isValidState(StateContext context) {
        // Validate basic session state first
        StateContext.ValidationResult sessionValidation = context.validateSessionState();
        if (!sessionValidation.isValid()) {
            LOGGER.warn("Session state validation failed: {}", sessionValidation.getErrorMessage());
            return false;
        }
        // Schuetzenmeldung is always a valid state to be in
        return true;
    }
    
    @Override
    public boolean canTransitionTo(StateContext context, String targetState) {
        // Can only transition to SATZEINGABE from SCHUETZENMELDUNG
        return STATUS_SATZEINGABE.equals(targetState);
    }
    
    @Override
    public boolean isDatabaseReadyForTransition(StateContext context, String targetState) {
        if (!STATUS_SATZEINGABE.equals(targetState)) {
            return false;
        }
        
        if (context == null) {
            return false;
        }
        
        // Check that exactly 3 shooters are deployed for current match
        try {
            List<MannschaftsmitgliedDO> deployedMembers = getDeployedMembersForCurrentMatch(context);
            return deployedMembers.size() == SHOOTERS_PER_TEAM;
        } catch (Exception e) {
            LOGGER.error("Error checking database readiness for transition: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public Map<String, Object> prepareResponseData(StateContext context) {
        Map<String, Object> data = super.prepareResponseData(context);
        
        try {
            // Get deployed team members (eingesetzt >= 1)
            List<MannschaftsmitgliedDO> deployedMembers = context.getDeployedTeamMembers();
            
            // Prepare Stammdaten for deployed shooters
            List<SchuetzeStammdatenDO> stammdaten = deployedMembers.stream()
                .map(m -> {
                    DsbMitgliedDO member = context.getDsbMitgliedComponent().findById(m.getDsbMitgliedId());
                    return new SchuetzeStammdatenDO(
                        member.getId(),
                        Math.toIntExact(m.getRueckennummer()),
                        member.getVorname(),
                        member.getNachname()
                    );
                }).collect(Collectors.toList());
            
            // Prepare available shooters list
            List<VerfuegbarerSchuetzeDO> available = deployedMembers.stream()
                .map(m -> {
                    DsbMitgliedDO member = context.getDsbMitgliedComponent().findById(m.getDsbMitgliedId());
                    return new VerfuegbarerSchuetzeDO(member.getId(), member.getVorname() + " " + member.getNachname());
                }).collect(Collectors.toList());
                
            data.put("schuetzeStammDaten", stammdaten);
            data.put("verfuegbareSchuetzen", available);
            
            LOGGER.debug("Prepared SCHUETZENMELDUNG data: {} deployed shooters available", deployedMembers.size());
            
        } catch (Exception e) {
            LOGGER.error("Error preparing SCHUETZENMELDUNG response data: {}", e.getMessage());
            data.put("schuetzeStammDaten", Collections.emptyList());
            data.put("verfuegbareSchuetzen", Collections.emptyList());
        }
        
        return data;
    }
    
    @Override
    public boolean validateOperation(StateContext context, String operation, Object data) {
        if (!"submitSchuetzen".equals(operation)) {
            return false; // Only supports shooter registration
        }
        
        if (!(data instanceof List)) {
            LOGGER.warn("Invalid data type for shooter registration: {}", data.getClass());
            return false;
        }
        
        @SuppressWarnings("unchecked")
        List<Long> shooterIds = (List<Long>) data;
        
        try {
            validateSchuetzenmeldungTeamRoster(context, shooterIds);
            return true;
        } catch (BusinessException e) {
            LOGGER.warn("Shooter registration validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean handlePostOperation(StateContext context, String operation, Object data) {
        if (!"submitSchuetzen".equals(operation) || !(data instanceof List)) {
            return false;
        }
        
        @SuppressWarnings("unchecked")
        List<Long> shooterIds = (List<Long>) data;
        
        try {
            // Mark shooters as deployed for current match
            markShootersAsDeployed(context, shooterIds);
            
            // Transition to SATZEINGABE
            context.updateSessionStatus(STATUS_SATZEINGABE);
            
            LOGGER.info("Successfully registered {} shooters for team {} and transitioned to SATZEINGABE",
                       shooterIds.size(), context.getTeamId());
            return true;
            
        } catch (Exception e) {
            LOGGER.error("Error handling shooter registration for team {}: {}", context.getTeamId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Validates team roster for shooter registration.
     */
    private void validateSchuetzenmeldungTeamRoster(StateContext context, List<Long> registeredShooterIds) {
        // Get all team members with deployment status >= 1
        List<MannschaftsmitgliedDO> teamMembers = context.getDeployedTeamMembers();
        
        Set<Long> validMemberIds = teamMembers.stream()
            .map(MannschaftsmitgliedDO::getDsbMitgliedId)
            .collect(Collectors.toSet());
        
        // Validate exactly 3 shooters
        if (registeredShooterIds.size() != SHOOTERS_PER_TEAM) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                "Exactly " + SHOOTERS_PER_TEAM + " shooters required, got " + registeredShooterIds.size());
        }
        
        // Check for duplicates
        Set<Long> uniqueShooters = new HashSet<>(registeredShooterIds);
        if (uniqueShooters.size() != registeredShooterIds.size()) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                "Duplicate shooters not allowed in registration");
        }
        
        // Validate all shooters belong to team and are deployed
        for (Long shooterId : registeredShooterIds) {
            if (!validMemberIds.contains(shooterId)) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Shooter " + shooterId + " is not a valid deployed member of team " + context.getTeamId());
            }
            // Use StateContext helper for deployment validation
            if (!context.isShooterDeployed(shooterId)) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Shooter " + shooterId + " is not deployed for team " + context.getTeamId());
            }
        }
    }
    
    /**
     * Marks shooters as deployed for current match using match number.
     */
    private void markShootersAsDeployed(StateContext context, List<Long> shooterIds) {
        try {
            // Get current match number from session
            long currentMatchId = context.getCurrentMatchId();
            long currentMatchNr = context.getMatchComponent().findById(currentMatchId).getNr();
            
            for (Long shooterId : shooterIds) {
                try {
                    MannschaftsmitgliedDO member = context.getMannschaftsmitgliedComponent()
                        .findByMemberAndTeamId(context.getTeamId(), shooterId);
                    member.setDsbMitgliedEingesetzt(Math.toIntExact(currentMatchNr));
                    context.getMannschaftsmitgliedComponent().update(member, 0L);
                    
                    LOGGER.debug("Marked shooter {} as deployed for match {} (team {})",
                               shooterId, currentMatchNr, context.getTeamId());
                } catch (Exception e) {
                    LOGGER.warn("Could not update deployment status for shooter {} in team {}: {}",
                              shooterId, context.getTeamId(), e.getMessage());
                    // Continue with other shooters - non-critical failure
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error marking shooters as deployed: {}", e.getMessage());
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, "Failed to mark shooters as deployed", e);
        }
    }
    
    /**
     * Gets team members deployed for current match.
     */
    private List<MannschaftsmitgliedDO> getDeployedMembersForCurrentMatch(StateContext context) {
        try {
            long currentMatchId = context.getCurrentMatchId();
            long currentMatchNr = context.getMatchComponent().findById(currentMatchId).getNr();
            
            return context.getMannschaftsmitgliedComponent().findByTeamId(context.getTeamId()).stream()
                .filter(mm -> mm.getDsbMitgliedEingesetzt() != null &&
                             mm.getDsbMitgliedEingesetzt().equals(Math.toIntExact(currentMatchNr)))
                .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Error getting deployed members for current match: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}