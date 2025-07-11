package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
    public static final String STATUS_MATCH_ENDE = "MATCH_ENDE";
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
        
        return switch (status) {
            case STATUS_SCHUETZENMELDUNG -> new Schuetzenmeldung();
            case STATUS_SATZEINGABE -> new Satzeingabe();
            case STATUS_WARTE -> new Warte();
            case STATUS_MATCH_ENDE -> new MatchEnde();
            case STATUS_WETTKAMPF_ENDE -> new WettkampfEnde();
            default -> {
                LOGGER.warn("Unknown status '{}' provided to state factory, defaulting to Schuetzenmeldung", status);
                yield new Schuetzenmeldung();
            }
        };
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
     * Validate state-specific business rules.
     * Default implementation returns true.
     */
    public boolean isValidState(StateContext context) {
        return true;
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
     * Calculates total arrow points for a set of passes.
     * This is a shared utility method used by multiple states.
     * 
     * @param passes List of PasseDO objects for a single set
     * @return Total arrow points for the set
     */
    protected int calculateSetPoints(List<PasseDO> passes) {
        return passes.stream()
            .mapToInt(p -> {
                int a = p.getPfeil1() != null ? p.getPfeil1() : 0;
                int b = p.getPfeil2() != null ? p.getPfeil2() : 0;
                int c = p.getPfeil3() != null ? p.getPfeil3() : 0;
                return a + b + c;
            })
            .sum();
    }
}
