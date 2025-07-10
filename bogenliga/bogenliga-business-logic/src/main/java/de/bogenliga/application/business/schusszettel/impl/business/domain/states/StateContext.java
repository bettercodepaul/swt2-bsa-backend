package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.WettkampfInfoDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Context object providing controlled access to SessionRuntime data and services.
 * 
 * <h2>DESIGN PRINCIPLE</h2>
 * This class encapsulates access to data and services needed by state objects,
 * preventing direct coupling between states and SessionRuntime internals.
 * 
 * <h2>RESPONSIBILITIES</h2>
 * <ul>
 *   <li>Provide read access to session data</li>
 *   <li>Delegate database operations through SessionRuntime</li>
 *   <li>Expose necessary services for state logic</li>
 *   <li>Maintain encapsulation boundaries</li>
 * </ul>
 * 
 * @author Marty Lauterbach - State context implementation
 */
public class StateContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateContext.class);
    
    private final TabletSchusszettelEntity session;
    private final TabletSchusszettelDAO sessionDAO;
    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final MatchAnalysisService matchAnalysisService;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    
    public StateContext(TabletSchusszettelEntity session,
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
    
    // === READ ACCESS TO SESSION DATA ===
    
    public long getTeamId() {
        return session.getTeamId();
    }
    
    public long getOpponentTeamId() {
        return session.getGegnerTeamId();
    }
    
    public long getCurrentMatchId() {
        return session.getCurrentMatchId();
    }
    
    public int getCurrentPasseNumber() {
        return session.getCurrentPasseNumber();
    }
    
    public long getWettkampfId() {
        return session.getWettkampfId();
    }
    
    public String getCurrentStatus() {
        return session.getStatus();
    }
    
    // === CONTROLLED WRITE ACCESS ===
    
    public void updateSessionStatus(String newStatus) {
        // Enhanced validation before update
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "New status cannot be null or empty");
        }
        
        // Validate session state
        ValidationResult sessionValidation = validateSessionState();
        if (!sessionValidation.isValid()) {
            LOGGER.warn("Session validation failed before status update: {}", sessionValidation.getErrorMessage());
        }
        
        long startTime = System.currentTimeMillis();
        try {
            session.setStatus(newStatus);
            sessionDAO.updateStatus(session, 0L);
            
            long duration = System.currentTimeMillis() - startTime;
            if (duration > WARNING_THRESHOLD_MS) {
                LOGGER.warn("Slow status update for team {}: {}ms", session.getTeamId(), duration);
            } else {
                LOGGER.debug("StateContext updated session {} status to {} in {}ms", session.getTeamId(), newStatus, duration);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to update session status for team {} after {}ms: {}", 
                        session.getTeamId(), System.currentTimeMillis() - startTime, e.getMessage());
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, "Database error updating session status", e);
        }
    }
    
    // Performance threshold constants
    private static final long WARNING_THRESHOLD_MS = 50L;
    private static final long ERROR_THRESHOLD_MS = 200L;
    
    public void updatePasseNumber(int newPasseNumber) {
        // Enhanced validation
        if (newPasseNumber < 1 || newPasseNumber > MAX_SETS_PER_MATCH) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Invalid passe number: " + newPasseNumber + 
                                             ". Must be between 1 and " + MAX_SETS_PER_MATCH);
        }
        
        long startTime = System.currentTimeMillis();
        try {
            session.setCurrentPasseNumber(newPasseNumber);
            sessionDAO.updateStatus(session, 0L);
            
            long duration = System.currentTimeMillis() - startTime;
            if (duration > WARNING_THRESHOLD_MS) {
                LOGGER.warn("Slow passe update for team {}: {}ms", session.getTeamId(), duration);
            } else {
                LOGGER.debug("StateContext updated session {} passe to {} in {}ms", session.getTeamId(), newPasseNumber, duration);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to update passe number for team {} after {}ms: {}", 
                        session.getTeamId(), System.currentTimeMillis() - startTime, e.getMessage());
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, "Database error updating passe number", e);
        }
    }
    
    private static final int MAX_SETS_PER_MATCH = 5; // From official archery rules
    
    public void advanceToNextMatch(LigamatchBE nextMatch, long opponentId) {
        // Enhanced validation
        ValidationResult matchValidation = validateMatchProgression(nextMatch);
        if (!matchValidation.isValid()) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Invalid match progression: " + matchValidation.getErrorMessage());
        }
        
        if (opponentId <= 0) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Invalid opponent ID: " + opponentId);
        }
        
        long startTime = System.currentTimeMillis();
        try {
            // Store previous state for rollback if needed
            Long previousMatchId = session.getCurrentMatchId();
            String previousStatus = session.getStatus();
            Integer previousPasse = session.getCurrentPasseNumber();
            
            session.setCurrentMatchId(nextMatch.getMatchId());
            session.setCurrentMatchNumber(Math.toIntExact(nextMatch.getMatchNr()));
            session.setCurrentPasseNumber(1);
            session.setStatus(State.STATUS_SCHUETZENMELDUNG);
            session.setGegnerTeamId(opponentId);
            sessionDAO.updateStatus(session, 0L);
            
            long duration = System.currentTimeMillis() - startTime;
            if (duration > WARNING_THRESHOLD_MS) {
                LOGGER.warn("Slow match advancement for team {}: {}ms", session.getTeamId(), duration);
            }
            
            LOGGER.info("StateContext advanced team {} from match {} to match {} (opponent: {}) in {}ms",
                       session.getTeamId(), previousMatchId, nextMatch.getMatchId(), opponentId, duration);
                       
        } catch (Exception e) {
            LOGGER.error("Failed to advance team {} to next match after {}ms: {}", 
                        session.getTeamId(), System.currentTimeMillis() - startTime, e.getMessage());
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, "Database error advancing to next match", e);
        }
    }
    
    // === SERVICE ACCESS ===
    
    public MatchAnalysisService getMatchAnalysisService() {
        return matchAnalysisService;
    }
    
    public MatchComponent getMatchComponent() {
        return matchComponent;
    }
    
    public PasseComponent getPasseComponent() {
        return passeComponent;
    }
    
    public TabletSchusszettelDAO getSessionDAO() {
        return sessionDAO;
    }
    
    // === CONVENIENCE METHODS ===
    
    public boolean isMatchComplete() {
        try {
            return matchAnalysisService.isMatchComplete(
                session.getCurrentMatchId(),
                session.getTeamId(), 
                session.getGegnerTeamId());
        } catch (Exception e) {
            LOGGER.error("Error checking match completion in StateContext: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean hasMoreMatches() {
        try {
            LigamatchBE currentMatch = matchComponent.getLigamatchById(session.getCurrentMatchId());
            return currentMatch != null && currentMatch.getNaechsteMatchId() != null;
        } catch (Exception e) {
            LOGGER.error("Error checking for more matches in StateContext: {}", e.getMessage());
            return false;
        }
    }
    
    public LigamatchBE getNextMatch() {
        try {
            LigamatchBE currentMatch = matchComponent.getLigamatchById(session.getCurrentMatchId());
            if (currentMatch != null && currentMatch.getNaechsteMatchId() != null) {
                return matchComponent.getLigamatchById(currentMatch.getNaechsteMatchId());
            }
            return null;
        } catch (Exception e) {
            LOGGER.error("Error getting next match in StateContext: {}", e.getMessage());
            return null;
        }
    }
    
    public long findOpponentTeamId(long matchId) {
        try {
            return matchAnalysisService.findOpponentTeamId(matchId, session.getTeamId());
        } catch (Exception e) {
            LOGGER.error("Error finding opponent in StateContext: {}", e.getMessage());
            return 0L;
        }
    }
    
    public long getOpponentMatchId() {
        try {
            // Get current match to find opponent's match ID
            LigamatchBE currentMatch = matchComponent.getLigamatchById(session.getCurrentMatchId());
            if (currentMatch != null && currentMatch.getMatchIdGegner() != null) {
                return currentMatch.getMatchIdGegner();
            }
            LOGGER.warn("No opponent match ID found for current match {}", session.getCurrentMatchId());
            return 0L;
        } catch (Exception e) {
            LOGGER.error("Error getting opponent match ID in StateContext: {}", e.getMessage());
            return 0L;
        }
    }
    
    // === TEAM AND SHOOTER DATA ACCESS ===
    
    public MannschaftsmitgliedComponent getMannschaftsmitgliedComponent() {
        return mannschaftsmitgliedComponent;
    }
    
    public DsbMitgliedComponent getDsbMitgliedComponent() {
        return dsbMitgliedComponent;
    }

    public WettkampfComponent getWettkampfComponent() {
        return wettkampfComponent;
    }

    public VeranstaltungComponent getVeranstaltungComponent() {
        return veranstaltungComponent;
    }
    
    public List<MannschaftsmitgliedDO> getTeamMembers() {
        return mannschaftsmitgliedComponent.findByTeamId(session.getTeamId());
    }
    
    public List<MannschaftsmitgliedDO> getDeployedTeamMembers() {
        return getTeamMembers().stream()
            .filter(m -> m.getDsbMitgliedEingesetzt() != null && m.getDsbMitgliedEingesetzt() >= 1)
            .collect(Collectors.toMap(
                MannschaftsmitgliedDO::getDsbMitgliedId, // Key: member ID
                m -> m,                                   // Value: the member
                (existing, replacement) -> existing       // Keep first if duplicate
            ))
            .values()
            .stream()
            .sorted(Comparator.comparing(MannschaftsmitgliedDO::getRueckennummer))
            .toList();
    }
    
    public List<PasseDO> getCurrentPasseData() {
        try {
            return passeComponent.findByMannschaftMatchId(session.getTeamId(), session.getCurrentMatchId())
                .stream()
                .filter(p -> p.getPasseLfdnr() == (long) session.getCurrentPasseNumber())
                .toList();
        } catch (Exception e) {
            LOGGER.error("Error getting current passe data in StateContext: {}", e.getMessage());
            return List.of();
        }
    }
    
    public List<PasseDO> getAllMatchPasses() {
        try {
            return passeComponent.findByMannschaftMatchId(session.getTeamId(), session.getCurrentMatchId());
        } catch (Exception e) {
            LOGGER.error("Error getting all match passes in StateContext: {}", e.getMessage());
            return List.of();
        }
    }
    
    public boolean isCurrentPasseComplete() {
        try {
            List<PasseDO> currentPassePasses = getCurrentPasseData();
            return currentPassePasses.size() >= 3; // 3 shooters per team
        } catch (Exception e) {
            LOGGER.error("Error checking passe completion in StateContext: {}", e.getMessage());
            return false;
        }
    }
    
    public TabletSchusszettelEntity loadOpponentSession() {
        if (session.getGegnerTeamId() == null || session.getGegnerTeamId() == 0L) {
            return null;
        }
        
        return sessionDAO.findByWettkampfUndTeam(session.getWettkampfId(), session.getGegnerTeamId())
                .orElse(null);
    }
    
    // === ENHANCED VALIDATION HELPERS ===
    
    /**
     * Enhanced arrow value validation with detailed error reporting.
     */
    public ValidationResult validateArrowValue(Integer arrowValue) {
        if (arrowValue == null) {
            return ValidationResult.invalid("Arrow value cannot be null");
        }
        if (arrowValue < 0) {
            return ValidationResult.invalid("Arrow value cannot be negative: " + arrowValue);
        }
        if (arrowValue > 10) {
            return ValidationResult.invalid("Arrow value cannot exceed 10: " + arrowValue);
        }
        return ValidationResult.valid();
    }
    
    /**
     * Validation result container for enhanced error reporting.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        
        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }
        
        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }
        
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    /**
     * Enhanced session state validation.
     */
    public ValidationResult validateSessionState() {
        if (session.getTeamId() == null || session.getTeamId() <= 0) {
            return ValidationResult.invalid("Invalid team ID: " + session.getTeamId());
        }
        if (session.getWettkampfId() == null || session.getWettkampfId() <= 0) {
            return ValidationResult.invalid("Invalid wettkampf ID: " + session.getWettkampfId());
        }
        if (session.getCurrentMatchId() == null || session.getCurrentMatchId() <= 0) {
            return ValidationResult.invalid("Invalid current match ID: " + session.getCurrentMatchId());
        }
        if (session.getCurrentPasseNumber() == null || session.getCurrentPasseNumber() < 1 || session.getCurrentPasseNumber() > 5) {
            return ValidationResult.invalid("Invalid passe number: " + session.getCurrentPasseNumber());
        }
        if (session.getStatus() == null || session.getStatus().trim().isEmpty()) {
            return ValidationResult.invalid("Session status cannot be null or empty");
        }
        return ValidationResult.valid();
    }
    
    /**
     * Validate match progression constraints.
     */
    public ValidationResult validateMatchProgression(LigamatchBE nextMatch) {
        if (nextMatch == null) {
            return ValidationResult.invalid("Next match cannot be null");
        }
        if (nextMatch.getMatchId() == null || nextMatch.getMatchId() <= 0) {
            return ValidationResult.invalid("Invalid next match ID: " + nextMatch.getMatchId());
        }
        if (nextMatch.getWettkampfId() != session.getWettkampfId()) {
            return ValidationResult.invalid("Match wettkampf mismatch: expected " + session.getWettkampfId() + 
                                           ", got " + nextMatch.getWettkampfId());
        }
        return ValidationResult.valid();
    }
    
    public boolean isShooterRegistered(long shooterId, int passeNumber) {
        try {
            List<PasseDO> passes = passeComponent.findByMannschaftMatchId(session.getTeamId(), session.getCurrentMatchId())
                .stream()
                .filter(p -> p.getPasseLfdnr() == passeNumber)
                .filter(p -> p.getPasseDsbMitgliedId().equals(shooterId))
                .toList();
            return !passes.isEmpty();
        } catch (Exception e) {
            LOGGER.error("Error checking shooter registration in StateContext: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean isShooterDeployed(long shooterId) {
        return getDeployedTeamMembers().stream()
            .anyMatch(m -> m.getDsbMitgliedId().equals(shooterId));
    }
    
    // === COMPOSITE DATA BUILDERS ===
    
    /**
     * Builds wettkampf information for the current session.
     */
    public WettkampfInfoDO buildWettkampfInfo() {
        try {
            long wettkampfId = getWettkampfId();
            WettkampfDO wettkampf = wettkampfComponent.findById(wettkampfId);
            VeranstaltungDO veranstaltung = veranstaltungComponent.findById(wettkampf.getWettkampfVeranstaltungsId());

            return new WettkampfInfoDO(
                    wettkampf.getId(),
                    wettkampf.getWettkampfTag(),
                    wettkampf.getWettkampfDatum(),
                    wettkampf.getWettkampfBeginn(),
                    wettkampf.getWettkampfOrtsname(),
                    wettkampf.getWettkampfOrtsinfo(),
                    wettkampf.getWettkampfStrasse(),
                    wettkampf.getWettkampfPlz(),
                    veranstaltung.getVeranstaltungID(),
                    veranstaltung.getVeranstaltungName(),
                    veranstaltung.getVeranstaltungSportJahr(),
                    veranstaltung.getVeranstaltungLigaName(),
                    veranstaltung.getVeranstaltungWettkampftypName()
            );
        } catch (Exception e) {
            LOGGER.warn("Could not build wettkampf info for wettkampfId {}: {}", getWettkampfId(), e.getMessage());
            return null;
        }
    }
}