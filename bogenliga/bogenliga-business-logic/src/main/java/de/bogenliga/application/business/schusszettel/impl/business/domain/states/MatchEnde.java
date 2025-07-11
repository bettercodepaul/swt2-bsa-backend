package de.bogenliga.application.business.schusszettel.impl.business.domain.states;

import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MatchEnde state - Match completed, showing results before next match progression.
 * 
 * <h2>STATE BEHAVIOR</h2>
 * <ul>
 *   <li>Shows completed match data for team review</li>
 *   <li>Displays full match results (sets, scores, team info)</li>
 *   <li>Progresses to next match or WETTKAMPF_ENDE on subsequent GET requests</li>
 *   <li>Intermediate state between match completion and next match start</li>
 * </ul>
 * 
 * <h2>PROGRESSION LOGIC</h2>
 * <ul>
 *   <li>Entry: When match completes (5 passes or 6+ Satzpunkte)</li>
 *   <li>Display: Shows complete match results to frontend</li>
 *   <li>Exit: On next GET request, advances to next match or tournament end</li>
 * </ul>
 *
 * @author Marty Lauterbach - Match completion state implementation
 */
public class MatchEnde extends State {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchEnde.class);
    private static final int MAX_SETS = 5;
    
    @Override
    public boolean isValidState(StateContext context) {
        // MatchEnde is valid when current match is complete
        return context.isMatchComplete();
    }
    
    @Override
    public boolean canTransitionTo(StateContext context, String targetState) {
        // Can transition to next match (SCHUETZENMELDUNG) or WETTKAMPF_ENDE
        return STATUS_SCHUETZENMELDUNG.equals(targetState) || STATUS_WETTKAMPF_ENDE.equals(targetState);
    }
    
    @Override
    public boolean isDatabaseReadyForTransition(StateContext context, String targetState) {
        // Always ready for transition since match is complete
        return true;
    }
    
    @Override
    public boolean canNudgeAlong() {
        // MATCH_ENDE requires evaluation for next match progression, not simple nudging
        return false;
    }
    
    @Override
    public Map<String, Object> prepareResponseData(StateContext context) {
        Map<String, Object> data = super.prepareResponseData(context);
        
        try {
            // Show completed match data
            List<SatzErgebnisDO> satzErgebnisse = buildCurrentMatchResults(context);
            data.put("satzErgebnisse", satzErgebnisse);
            
            // Show team match info for completed match
            List<TeamMatchInfoDO> matchInfo = buildCurrentMatchInfo(context, satzErgebnisse);
            data.put("matchErgebnis", matchInfo);
            
            // Clear shooter and scoring data since match is complete
            data.put("schuetzenMatchPunkte", Collections.emptyList());
            data.put("schuetzeStammDaten", Collections.emptyList());
            data.put("verfuegbareSchuetzen", Collections.emptyList());
            
            LOGGER.info("Prepared MATCH_ENDE data for team {} - match {} completed with {} sets",
                       context.getTeamId(), context.getCurrentMatchId(), satzErgebnisse.size());
            
        } catch (Exception e) {
            LOGGER.error("Error preparing MATCH_ENDE response data: {}", e.getMessage());
            data.put("satzErgebnisse", Collections.emptyList());
            data.put("matchErgebnis", Collections.emptyList());
            data.put("schuetzenMatchPunkte", Collections.emptyList());
            data.put("schuetzeStammDaten", Collections.emptyList());
            data.put("verfuegbareSchuetzen", Collections.emptyList());
        }
        
        return data;
    }
    
    @Override
    public boolean validateOperation(StateContext context, String operation, Object data) {
        // No operations allowed in MATCH_ENDE state - just display completed match
        LOGGER.debug("Operation {} not allowed in MATCH_ENDE state - match is complete", operation);
        return false;
    }
    
    @Override
    public boolean handlePostOperation(StateContext context, String operation, Object data) {
        // No POST operations allowed in MATCH_ENDE state
        LOGGER.debug("POST operation {} rejected in MATCH_ENDE state", operation);
        return false;
    }
    
    /**
     * Handle progression from MATCH_ENDE to next match or tournament end.
     * This is called on GET requests to advance from the match completion view.
     */
    @Override
    public boolean handleWarteEvaluation(StateContext context, TabletSchusszettelEntity opponent) {
        LOGGER.info("MATCH_ENDE progression evaluation for team {} - match {} completed", 
                   context.getTeamId(), context.getCurrentMatchId());
        
        try {
            // Check if there are more matches for this team
            if (context.hasMoreMatches()) {
                // Advance to next match
                LigamatchBE nextMatch = context.getNextMatch();
                if (nextMatch != null) {
                    long opponentId = context.findOpponentTeamId(nextMatch.getMatchId());
                    context.advanceToNextMatch(nextMatch, opponentId);
                    LOGGER.info("Advanced team {} from completed match to next match {} (opponent: {})", 
                               context.getTeamId(), nextMatch.getMatchId(), opponentId);
                    return true;
                } else {
                    LOGGER.warn("hasMoreMatches() returned true but getNextMatch() returned null for team {}", 
                               context.getTeamId());
                }
            }
            
            // No more matches - end competition
            context.updateSessionStatus(STATUS_WETTKAMPF_ENDE);
            LOGGER.info("Competition ended for team {} - no more matches available", context.getTeamId());
            return true;
            
        } catch (Exception e) {
            LOGGER.error("Error in MATCH_ENDE progression for team {}: {}", context.getTeamId(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Builds match results for the current completed match.
     */
    private List<SatzErgebnisDO> buildCurrentMatchResults(StateContext context) {
        List<SatzErgebnisDO> results = new ArrayList<>();
        
        try {
            long matchId = context.getCurrentMatchId();
            long teamId = context.getTeamId();
            long opponentId = context.getOpponentTeamId();
            
            // Get all passes for both teams in current match
            List<PasseDO> teamPasses = context.getAllMatchPasses();
            List<PasseDO> opponentPasses = context.getPasseComponent()
                .findByMannschaftMatchId(opponentId, matchId);
            
            // Group passes by set number
            Map<Long, List<PasseDO>> teamPassesBySet = teamPasses.stream()
                .collect(Collectors.groupingBy(PasseDO::getPasseLfdnr));
            Map<Long, List<PasseDO>> oppPassesBySet = opponentPasses.stream()
                .collect(Collectors.groupingBy(PasseDO::getPasseLfdnr));
            
            // Build results for each completed set
            for (long setNumber = 1; setNumber <= MAX_SETS; setNumber++) {
                List<PasseDO> teamSetPasses = teamPassesBySet.getOrDefault(setNumber, new ArrayList<>());
                List<PasseDO> oppSetPasses = oppPassesBySet.getOrDefault(setNumber, new ArrayList<>());
                
                // Only include sets where both teams have data
                if (!teamSetPasses.isEmpty() && !oppSetPasses.isEmpty()) {
                    int teamPoints = calculateSetPoints(teamSetPasses);
                    int oppPoints = calculateSetPoints(oppSetPasses);
                    
                    SatzErgebnisDO satzResult = new SatzErgebnisDO(
                        Math.toIntExact(setNumber),
                        teamPoints,
                        oppPoints
                    );
                    satzResult.setTeam1Id(teamId);
                    satzResult.setTeam2Id(opponentId);
                    results.add(satzResult);
                    
                    LOGGER.debug("Added set {} results: team {} = {} points, opponent {} = {} points",
                               setNumber, teamId, teamPoints, opponentId, oppPoints);
                }
            }
            
        } catch (Exception e) {
            LOGGER.error("Error building current match results: {}", e.getMessage());
        }
        
        return results;
    }
    
    /**
     * Builds team match info for the current completed match.
     */
    private List<TeamMatchInfoDO> buildCurrentMatchInfo(StateContext context, List<SatzErgebnisDO> satzErgebnisse) {
        List<TeamMatchInfoDO> matchInfo = new ArrayList<>();
        
        try {
            long teamId = context.getTeamId();
            long opponentId = context.getOpponentTeamId();
            
            int teamSatzpunkte = 0;
            int oppSatzpunkte = 0;
            
            // Calculate Satzpunkte for each team based on set results
            for (SatzErgebnisDO set : satzErgebnisse) {
                int teamSetPoints, oppSetPoints;
                
                if (set.getTeam1Id() == teamId) {
                    teamSetPoints = set.getTeam1Punkte();
                    oppSetPoints = set.getTeam2Punkte();
                } else {
                    teamSetPoints = set.getTeam2Punkte();
                    oppSetPoints = set.getTeam1Punkte();
                }
                
                // Award Satzpunkte based on official archery rules
                if (teamSetPoints > oppSetPoints) {
                    teamSatzpunkte += 2; // Winner gets 2 Satzpunkte
                } else if (oppSetPoints > teamSetPoints) {
                    oppSatzpunkte += 2; // Winner gets 2 Satzpunkte
                } else {
                    teamSatzpunkte += 1; // Tie: both teams get 1 Satzpunkt
                    oppSatzpunkte += 1;
                }
            }
            
            // Create team match info entries
            matchInfo.add(new TeamMatchInfoDO(teamId, getTeamName(teamId), teamSatzpunkte));
            matchInfo.add(new TeamMatchInfoDO(opponentId, getTeamName(opponentId), oppSatzpunkte));
            
            LOGGER.debug("Built match info: team {} = {} Satzpunkte, opponent {} = {} Satzpunkte",
                        teamId, teamSatzpunkte, opponentId, oppSatzpunkte);
            
        } catch (Exception e) {
            LOGGER.error("Error building current match info: {}", e.getMessage());
        }
        
        return matchInfo;
    }
    
    /**
     * Gets team name using established pattern.
     */
    private String getTeamName(long teamId) {
        try {
            // Simple fallback approach - return standardized team name
            // In a real implementation, you'd access team components directly
            return "Team " + teamId; // Simplified for now
            
        } catch (Exception e) {
            LOGGER.warn("Could not determine team name for team {}: {}", teamId, e.getMessage());
            return "Team " + teamId; // Fallback name
        }
    }
}
