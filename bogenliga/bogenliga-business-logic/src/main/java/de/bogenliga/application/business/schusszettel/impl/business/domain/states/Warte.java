package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Warte state - Waiting for opponent team completion.
 * 
 * <h2>STATE BEHAVIOR</h2>
 * This is the most complex state, handling opponent synchronization and progression logic.
 * <ul>
 *   <li>Waits for opponent team to also reach WARTE state</li>
 *   <li>Handles desynchronized passe numbers between teams</li>
 *   <li>Coordinates progression to next passe or next match</li>
 *   <li>Manages edge cases: missing opponents, desync recovery</li>
 * </ul>
 * 
 * <h2>SYNCHRONIZATION LOGIC</h2>
 * <pre>
 * if (opponent ahead in passe) → catch up unilaterally
 * if (we ahead in passe) → help opponent catch up  
 * if (opponent not in WARTE) → wait for opponent
 * if (both in WARTE, synchronized) → progress both teams together
 * </pre>
 *
 * @author Marty Lauterbach - Enhanced with complex opponent synchronization logic
 */
public class Warte extends State {
    private static final Logger LOGGER = LoggerFactory.getLogger(Warte.class);
    
    @Override
    public String getStateName() {
        return STATUS_WARTE;
    }
    
    @Override
    public boolean canNudgeAlong() {
        // WARTE state requires opponent evaluation, not simple nudging
        return false;
    }
    
    @Override
    public boolean isValidState(StateContext context) {
        // WARTE is valid when current passe is complete but match not finished
        return !context.isMatchComplete();
    }
    
    /**
     * Complex WARTE state evaluation with opponent synchronization.
     * This is the core logic extracted from SessionRuntime.evaluateWithOpponentWAITstate().
     */
    @Override
    public boolean handleWarteEvaluation(StateContext context, TabletSchusszettelEntity opponent) {
        LOGGER.info("WARTE state evaluation for team {} (passe {}) with opponent {} (status: {}, passe: {})", 
                    context.getTeamId(), context.getCurrentPasseNumber(),
                    opponent != null ? opponent.getTeamId() : "null",
                    opponent != null ? opponent.getStatus() : "null",
                    opponent != null ? opponent.getCurrentPasseNumber() : "null");

        try {
            // EDGE CASE: Opponent missing
            if (opponent == null) {
                LOGGER.warn("EDGE CASE: Opponent session not found for team {} - not allowing unilateral progression",
                           context.getTeamId());
                return false;
            }
            
            // Check if opponent is ahead in passe number (regardless of state)
            if (context.getCurrentPasseNumber() < opponent.getCurrentPasseNumber()) {
                LOGGER.warn("EDGE CASE: Opponent team {} ahead in passe {} while we are in passe {} - allowing self-progression",
                        opponent.getTeamId(), opponent.getCurrentPasseNumber(), 
                        context.getCurrentPasseNumber());

                // Opponent is ahead - we need to catch up
                return attemptStateProgression(context, "Opponent ahead in passe progression");
            }
            
            // Check if we are ahead in passe number - help opponent catch up
            if (context.getCurrentPasseNumber() > opponent.getCurrentPasseNumber()) {
                LOGGER.warn("EDGE CASE: We are ahead in passe {} while opponent team {} is in passe {} - helping opponent catch up",
                        context.getCurrentPasseNumber(), opponent.getTeamId(), 
                        opponent.getCurrentPasseNumber());

                // We are ahead - help opponent catch up
                return forceOpponentAdvancement(context, opponent, "Current team ahead - helping opponent catch up");
            }

            // CASE: Opponent in different state but same/lower passe - check if we should wait or progress  
            if (!STATUS_WARTE.equals(opponent.getStatus())) {
                // Opponent not also in WARTE
                LOGGER.info("Team {} waiting for opponent {} to reach WARTE state, same passe",
                        context.getTeamId(), opponent.getTeamId());
                return false;
            }

            // STANDARD CASE: Both teams in WARTE and synchronized - progress both teams together
            LOGGER.info("Both teams in WARTE and synchronized - progressing both team {} and opponent {}", 
                        context.getTeamId(), opponent.getTeamId());
            
            // Progress current team
            boolean currentTeamProgressed = attemptStateProgression(context, "Both teams ready for progression");
            
            // Progress opponent team as well
            boolean opponentProgressed = forceOpponentAdvancement(context, opponent, "Both teams ready for progression");
            
            LOGGER.info("Progression results: current team {} = {}, opponent team {} = {}", 
                       context.getTeamId(), currentTeamProgressed, 
                       opponent.getTeamId(), opponentProgressed);
            
            // Return true if both teams progressed
            return currentTeamProgressed && opponentProgressed;
            
        } catch (Exception e) {
            LOGGER.error("Error in WARTE state evaluation for team {}: {}", context.getTeamId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Attempt state progression for current team.
     * Handles match completion check and advancement logic.
     */
    private boolean attemptStateProgression(StateContext context, String reason) {
        try {
            // Check if current match is complete
            boolean matchComplete = context.isMatchComplete();
            
            if (matchComplete) {
                LOGGER.info("Match {} complete for team {} - {}", context.getCurrentMatchId(), context.getTeamId(), reason);
                
                // Check if there are more matches
                if (context.hasMoreMatches()) {
                    // Advance to next match
                    advanceToNextMatch(context);
                    LOGGER.info("Advanced team {} to next match", context.getTeamId());
                } else {
                    // No more matches - end competition
                    context.updateSessionStatus(STATUS_WETTKAMPF_ENDE);
                    LOGGER.info("Competition ended for team {}", context.getTeamId());
                }
            } else {
                // Match not complete - advance to next passe
                int nextPasse = context.getCurrentPasseNumber() + 1;
                context.updatePasseNumber(nextPasse);
                context.updateSessionStatus(STATUS_SATZEINGABE);
                LOGGER.info("Advanced team {} to passe {} - {}", context.getTeamId(), nextPasse, reason);
            }
            
            return true;
            
        } catch (Exception e) {
            LOGGER.error("Error in state progression for team {}: {}", context.getTeamId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Force advancement of opponent team when synchronized.
     */
    private boolean forceOpponentAdvancement(StateContext context, TabletSchusszettelEntity opponent, String reason) {
        // Check if opponent is actually in WARTE state
        if (!STATUS_WARTE.equals(opponent.getStatus())) {
            LOGGER.warn("Cannot force advancement for opponent team {} - not in WARTE state (current state: {})",
                    opponent.getTeamId(), opponent.getStatus());
            return false;
        }

        LOGGER.info("Forcing advancement for opponent team {} - reason: {}", opponent.getTeamId(), reason);

        try {
            // Create context for opponent and attempt progression
            StateContext opponentContext = new StateContext(
                opponent, 
                context.getSessionDAO(), 
                context.getMatchComponent(), 
                context.getPasseComponent(), 
                context.getMatchAnalysisService(),
                context.getMannschaftsmitgliedComponent(),
                context.getDsbMitgliedComponent(),
                context.getWettkampfComponent(),
                context.getVeranstaltungComponent()
            );
            
            return attemptStateProgression(opponentContext, "Forced advancement: " + reason);
            
        } catch (Exception e) {
            LOGGER.error("Error forcing opponent advancement: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Advance to the next match using LigamatchBE progression.
     */
    private void advanceToNextMatch(StateContext context) {
        try {
            LigamatchBE nextMatch = context.getNextMatch();
            if (nextMatch != null) {
                long opponentId = context.findOpponentTeamId(nextMatch.getMatchId());
                context.advanceToNextMatch(nextMatch, opponentId);
            } else {
                LOGGER.warn("No next match available for team {}", context.getTeamId());
            }
        } catch (Exception e) {
            LOGGER.error("Error advancing to next match for team {}: {}", context.getTeamId(), e.getMessage());
            throw new RuntimeException("Failed to advance to next match", e);
        }
    }
}
