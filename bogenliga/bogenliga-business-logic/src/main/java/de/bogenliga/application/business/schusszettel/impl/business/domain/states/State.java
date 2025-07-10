package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for schusszettel state objects implementing state pattern.
 * 
 * <h2>DESIGN PRINCIPLE</h2>
 * Each concrete state class encapsulates behavior specific to that state,
 * reducing complexity in SessionRuntime and enabling better testing.
 * 
 * <h2>USAGE PATTERN</h2>
 * States are stateless behavior objects that operate on StateContext data.
 * They handle complex state-specific logic while SessionRuntime orchestrates.
 *
 * @author Marty Lauterbach - Enhanced with behavior methods for state pattern
 */
public abstract class State {
    private static final Logger LOGGER = LoggerFactory.getLogger(State.class);
    
    // State constants matching SessionRuntime
    public static final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";
    public static final String STATUS_SATZEINGABE = "SATZEINGABE";
    public static final String STATUS_WARTE = "WARTE";
    public static final String STATUS_WETTKAMPF_ENDE = "WETTKAMPF_ENDE";
    
    /**
     * Factory method to create state objects from string status.
     * Maintains backward compatibility with string-based persistence.
     */
    public static State fromString(String status) {
        if (status == null) {
            LOGGER.warn("Null status provided to state factory, defaulting to Schuetzenmeldung");
            return new Schuetzenmeldung();
        }
        
        switch (status) {
            case STATUS_SCHUETZENMELDUNG:
                return new Schuetzenmeldung();
            case STATUS_SATZEINGABE:
                return new Satzeingabe();
            case STATUS_WARTE:
                return new Warte();
            case STATUS_WETTKAMPF_ENDE:
                return new WettkampfEnde();
            default:
                LOGGER.warn("Unknown status '{}' provided to state factory, defaulting to Schuetzenmeldung", status);
                return new Schuetzenmeldung();
        }
    }
    
    /**
     * Handle complex WARTE state evaluation with opponent.
     * Default implementation returns false (no special handling).
     * Override in states that need complex opponent logic.
     */
    public boolean handleWarteEvaluation(StateContext context, TabletSchusszettelEntity opponent) {
        return false; // Default: no special handling needed
    }
    
    /**
     * Check if this state can transition via nudgeAlong().
     * Default implementation allows transitions.
     */
    public boolean canNudgeAlong() {
        return true;
    }
    
    /**
     * Validate if transition to target state is allowed.
     * Default implementation allows all transitions.
     */
    public boolean canTransitionTo(StateContext context, String targetState) {
        return true;
    }
    
    /**
     * Check if database is ready for this state transition.
     * Default implementation returns true.
     */
    public boolean isDatabaseReadyForTransition(StateContext context, String targetState) {
        return true;
    }
    
    /**
     * Synchronize this state with current database state.
     * Default implementation returns false (no changes made).
     */
    public boolean synchronizeWithDatabase(StateContext context) {
        return false;
    }
    
    /**
     * Prepare state-specific response data.
     * Base implementation provides common session data.
     * Subclasses should call super and add state-specific data.
     */
    public java.util.Map<String, Object> prepareResponseData(StateContext context) {
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        
        // Add common session data that all states should provide
        data.put("currentPasseNumber", context.getCurrentPasseNumber());
        data.put("eigenesTeamMatchId", context.getCurrentMatchId());
        data.put("gegnerischesTeamMatchId", context.getOpponentMatchId());
        data.put("wettkampfInfo", context.buildWettkampfInfo());
        
        return data;
    }
    
    /**
     * Validate state-specific business operation.
     * Default implementation returns true.
     */
    public boolean validateOperation(StateContext context, String operation, Object data) {
        return true;
    }
    
    /**
     * Handle state-specific POST operation.
     * Default implementation returns false (not handled).
     */
    public boolean handlePostOperation(StateContext context, String operation, Object data) {
        return false;
    }
    
    /**
     * Get the string representation of this state for database persistence.
     * Default implementation uses class name.
     */
    public String getStateName() {
        return getClass().getSimpleName().toUpperCase();
    }
    
    /**
     * Validate state-specific business rules.
     * Default implementation returns true.
     */
    public boolean isValidState(StateContext context) {
        return true;
    }
    
    /**
     * Handle state-specific errors.
     * Default implementation logs error and returns false.
     */
    public boolean handleError(StateContext context, Exception error, String operation) {
        LOGGER.error("Error in state {} during operation {}: {}", 
                    getStateName(), operation, error.getMessage());
        return false;
    }
}
