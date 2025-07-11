package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.business.domain.SessionRuntime;
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.WettkampfInfoDO;
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
    private final SessionRuntime sessionRuntime;
    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final MatchAnalysisService matchAnalysisService;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    
    public StateContext(TabletSchusszettelEntity session,
                       SessionRuntime sessionRuntime,
                       MatchComponent matchComponent,
                       PasseComponent passeComponent,
                       MatchAnalysisService matchAnalysisService,
                       MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                       DsbMitgliedComponent dsbMitgliedComponent,
                       WettkampfComponent wettkampfComponent,
                       VeranstaltungComponent veranstaltungComponent) {
        this.session = session;
        this.sessionRuntime = sessionRuntime;
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
        // Delegate to SessionRuntime for database operations
        sessionRuntime.updateSessionStatus(newStatus);
        LOGGER.debug("StateContext delegated status update to SessionRuntime for team {}", session.getTeamId());
    }
    
    
    public void updatePasseNumber(int newPasseNumber) {
        // Delegate to SessionRuntime for database operations
        sessionRuntime.updatePasseNumber(newPasseNumber);
        LOGGER.debug("StateContext delegated passe number update to SessionRuntime for team {}", session.getTeamId());
    }
    
    private static final int MAX_SETS_PER_MATCH = 5; // From official archery rules
    
    public void advanceToNextMatch(LigamatchBE nextMatch, long opponentId) {
        // Delegate to SessionRuntime for database operations
        sessionRuntime.advanceToNextMatch(nextMatch, opponentId);
        LOGGER.debug("StateContext delegated match advancement to SessionRuntime for team {}", session.getTeamId());
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
    
    // Access to DAO through SessionRuntime
    public de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO getSessionDAO() {
        return sessionRuntime.getSessionDAO();
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
            // CRITICAL FIX: Use proper Setzliste-aware tournament progression
            // instead of flawed naechsteMatchId calculation
            LigamatchBE nextMatch = matchAnalysisService.findCorrectNextMatch(session.getCurrentMatchId(), session.getTeamId());
            return nextMatch != null;
        } catch (Exception e) {
            LOGGER.error("Error checking for more matches using Setzliste-aware logic: {}", e.getMessage());
            return false;
        }
    }
    
    public LigamatchBE getNextMatch() {
        try {
            // CRITICAL FIX: Use proper Setzliste-aware tournament progression
            // instead of flawed naechsteMatchId calculation
            return matchAnalysisService.findCorrectNextMatch(session.getCurrentMatchId(), session.getTeamId());
        } catch (Exception e) {
            LOGGER.error("Error getting next match using Setzliste-aware logic: {}", e.getMessage());
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
        if (session.getGegnerTeamId() == null || session.getGegnerTeamId().equals(0L)) {
            return null;
        }
        
        // Delegate to SessionRuntime for database operations
        return sessionRuntime.loadOpponentSessionByTeamId(session.getGegnerTeamId());
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
        if (session.getCurrentPasseNumber() == null || session.getCurrentPasseNumber() < 1 || session.getCurrentPasseNumber() > MAX_SETS_PER_MATCH) {
            return ValidationResult.invalid("Invalid passe number: " + session.getCurrentPasseNumber());
        }
        if (session.getStatus() == null || session.getStatus().trim().isEmpty()) {
            return ValidationResult.invalid("Session status cannot be null or empty");
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