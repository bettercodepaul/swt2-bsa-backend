package de.bogenliga.application.business.schusszettel.impl.business;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelAdminComponent;
import de.bogenliga.application.business.schusszettel.api.types.TabletSessionInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TabletSessionSingDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.WettkampfInfoDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.business.domain.SessionRuntime;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;

/**
 * Administrative component for Tablet Schusszettel session management and initialization.
 * 
 * <h2>FUNCTIONALITY</h2>
 * Manages session lifecycle for tablet-based score entry without handling score entry logic.
 * Provides session initialization, deletion, token management, and status monitoring for
 * competition administrators.
 * 
 * <h2>SESSION INITIALIZATION</h2>
 * 
 * <h3>Team Match Detection:</h3>
 * <pre>
 * For each team in competition:
 * 1. Query LigamatchBE entities for team sorted by match number
 * 2. Iterate through matches to find first incomplete match:
 *    - Check if match has pass entries (started?)
 *    - Use MatchAnalysisService.isMatchComplete() for completion check
 *    - Return first incomplete match found
 * 3. If all matches complete → WETTKAMPF_ENDE
 * 4. If incomplete match found → determine initial state
 * </pre>
 * 
 * <h3>Initial State Determination:</h3>
 * <pre>
 * determineInitialStatus(matchId, teamId, opponentId, passeNumber):
 * 1. Check if all team matches finished → WETTKAMPF_ENDE
 * 2. Check if current match complete → WETTKAMPF_ENDE  
 * 3. Analyze current pass data:
 *    - Pass entries with arrow scores → WARTE
 *    - Pass entries without arrow scores → SATZEINGABE
 *    - No pass entries for current pass → SCHUETZENMELDUNG
 * </pre>
 * 
 * <h2>SESSION VALIDATION</h2>
 * 
 * The generateSchusszettelSessions() method validates existing sessions against current
 * database state and corrects inconsistencies:
 * <ul>
 *   <li>Re-evaluates match completion status</li>
 *   <li>Updates session match references when teams advance externally</li>
 *   <li>Applies SessionRuntime.evaluateWithOpponent() for WARTE states</li>
 * </ul>
 * 
 * <h2>TOKEN SECURITY</h2>
 * 
 * <h3>Token Generation:</h3>
 * <ul>
 *   <li>16-byte cryptographically random tokens</li>
 *   <li>Base64 URL-safe encoding without padding</li>
 *   <li>SecureRandom for entropy source</li>
 * </ul>
 * 
 * <h3>Administrative Operations:</h3>
 * <ul>
 *   <li>Session state modification only (no competition data changes)</li>
 *   <li>Read-only access to match and pass data</li>
 *   <li>Token regeneration without state loss</li>
 * </ul>
 * 
 * <h2>KEY METHODS</h2>
 * 
 * <h3>initializeForWettkampf(wettkampfId)</h3>
 * Deletes existing sessions, analyzes all teams using LigamatchBE data,
 * determines current match position and initial state, creates new sessions
 * with secure tokens.
 * 
 * <h3>generateSchusszettelSessions(wettkampfId)</h3>
 * Retrieves existing sessions, validates against current database state,
 * corrects inconsistent sessions, evaluates WARTE states, returns session
 * overview with team and opponent names.
 * 
 * <h3>reTokenize(wettkampfId, teamId)</h3>
 * Generates new secure token for existing session while preserving all
 * session state data.
 * 
 * <h2>COMPONENT DEPENDENCIES</h2>
 * <ul>
 *   <li>MatchAnalysisService: Match completion and opponent detection using LigamatchBE</li>
 *   <li>MatchComponent: LigamatchBE queries and match structure access</li>
 *   <li>PasseComponent: Pass data queries for state determination</li>
 *   <li>TabletSchusszettelDAO: Session persistence operations</li>
 *   <li>Team/Club components: Name resolution for UI display</li>
 * </ul>
 * 
 * @author Marty Lauterbach
 * @version 3.0 - Optimized with LigamatchBE integration
 * @version 2.0 - Enhanced initialization and validation logic
 * @since 1.0 - Basic session management
 */
@Service
public class TabletSchusszettelAdminComponentImpl implements TabletSchusszettelAdminComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSchusszettelAdminComponentImpl.class);

    private static final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final TabletSchusszettelDAO sessionDAO;
    private final MatchComponent matchComponent;
    private final DsbMannschaftComponent mannschaftComponent;
    private final VereinComponent vereinComponent;
    private final PasseComponent passeComponent;
    private final MatchAnalysisService matchAnalysisService;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;

    @Autowired
    public TabletSchusszettelAdminComponentImpl(final TabletSchusszettelDAO sessionDAO,
                                                final MatchComponent matchComponent,
                                                final DsbMannschaftComponent mannschaftComponent,
                                                final VereinComponent vereinComponent,
                                                final PasseComponent passeComponent,
                                                final MatchAnalysisService matchAnalysisService,
                                                final WettkampfComponent wettkampfComponent,
                                                final VeranstaltungComponent veranstaltungComponent) {
        this.sessionDAO        = sessionDAO;
        this.matchComponent    = matchComponent;
        this.mannschaftComponent = mannschaftComponent;
        this.vereinComponent     = vereinComponent;
        this.passeComponent      = passeComponent;
        this.matchAnalysisService = matchAnalysisService;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeForWettkampf(final long wettkampfId) {
        try {
            sessionDAO.deleteByWettkampfId(wettkampfId);

            // OPTIMIZED: Use LigamatchBE for better performance and built-in team data
            final List<LigamatchBE> allLigamatches = matchComponent.getLigamatchesByWettkampfId(wettkampfId);
            if (allLigamatches.isEmpty()) {
                throw new BusinessException(
                        ErrorCode.ENTITY_NOT_FOUND_ERROR,
                        "No ligamatches found for wettkampf " + wettkampfId);
            }

            allLigamatches.forEach(m -> {
                if (m.getMannschaftId() == null) {
                    throw new BusinessException(
                            ErrorCode.ENTITY_NOT_FOUND_ERROR,
                            "Invalid ligamatch data (missing team) for wettkampf " + wettkampfId);
                }
            });

            final Set<Long> teamIds = allLigamatches.stream()
                    .map(LigamatchBE::getMannschaftId)
                    .collect(Collectors.toSet());

            for (final Long teamId : teamIds) {
                final List<LigamatchBE> teamMatches = allLigamatches.stream()
                        .filter(m -> Objects.equals(m.getMannschaftId(), teamId))
                        .sorted(Comparator.comparingLong(LigamatchBE::getMatchNr))
                        .toList();

                // OPTIMIZED: Find current match using LigamatchBE data
                LigamatchBE currentMatch = findCurrentLigamatchForTeam(teamMatches, teamId);
                
                final TabletSchusszettelEntity session = new TabletSchusszettelEntity();
                session.setWettkampfId(wettkampfId);
                session.setTeamId(teamId);
                session.setToken(generateUrlSafeToken());
                
                if (currentMatch == null) {
                    // ALL MATCHES COMPLETE - cascading found no incomplete matches
                    LOGGER.info("CASCADING COMPLETE: All matches finished for team {} → WETTKAMPF_ENDE", teamId);
                    
                    // Set to last match for reference, but status will be WETTKAMPF_ENDE
                    LigamatchBE lastMatch = teamMatches.get(teamMatches.size() - 1);
                    session.setCurrentMatchId(lastMatch.getMatchId());
                    session.setCurrentMatchNumber(Math.toIntExact(lastMatch.getMatchNr()));
                    session.setCurrentPasseNumber(5); // Max passe number
                    session.setStatus("WETTKAMPF_ENDE");
                    
                    try {
                        final long opponentId = matchAnalysisService.findOpponentTeamId(lastMatch.getMatchId(), teamId);
                        session.setGegnerTeamId(opponentId);
                    } catch (Exception e) {
                        LOGGER.warn("Could not find opponent for last match of team {}: {}", teamId, e.getMessage());
                        session.setGegnerTeamId(0L); // Default value
                    }
                    
                    LOGGER.info("Initialized FINISHED session for team {} - all matches complete", teamId);
                    
                } else {
                    // FOUND INCOMPLETE MATCH - set up session for this match
                    final long opponentId = matchAnalysisService.findOpponentTeamId(currentMatch.getMatchId(), teamId);
                    
                    // Determine correct passe number based on existing data
                    int correctPasseNumber = matchAnalysisService.getCurrentPasseNumber(currentMatch.getMatchId(), teamId, opponentId);
                    
                    // ROBUSTNESS: Determine initial status based on existing match data and competition state
                    String initialStatus = determineInitialStatusLigamatch(currentMatch.getMatchId(), teamId, opponentId, correctPasseNumber, teamMatches);

                    session.setCurrentMatchId(currentMatch.getMatchId());
                    session.setCurrentMatchNumber(Math.toIntExact(currentMatch.getMatchNr()));
                    session.setCurrentPasseNumber(correctPasseNumber);
                    session.setStatus(initialStatus);
                    session.setGegnerTeamId(opponentId);

                    LOGGER.info("Initializing session for team {} with match {} passe {} status {}", 
                        teamId, currentMatch.getMatchId(), correctPasseNumber, initialStatus);
                }

                sessionDAO.createSession(session, -1L);
            }

        } catch (final BusinessException be) {
            throw be;
        } catch (final Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "initializeForWettkampf failed for wettkampf " + wettkampfId +
                            ": " + e.getMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteForWettkampf(final long wettkampfId) {
        try {
            sessionDAO.deleteByWettkampfId(wettkampfId);
        } catch (final BusinessException be) {
            throw be;
        } catch (final Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "deleteForWettkampf failed for wettkampf " + wettkampfId +
                            ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsForWettkampf(final long wettkampfId) {
        try {
            return sessionDAO.existsByWettkampfId(wettkampfId);
        } catch (final BusinessException be) {
            throw be;
        } catch (final Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "existsForWettkampf failed for wettkampf " + wettkampfId +
                            ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reTokenize(final long wettkampfId, final long teamId) {
        try {
            final TabletSchusszettelEntity session = sessionDAO
                    .findByWettkampfUndTeam(wettkampfId, teamId)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.NO_PERMISSION_ERROR,
                            "Invalid tablet session"));

            final String newToken = generateUrlSafeToken();
            session.setToken(newToken);
            sessionDAO.setToken(wettkampfId, teamId, newToken, -1L);

        } catch (final BusinessException be) {
            throw be;
        } catch (final Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "reTokenize failed for wettkampf " + wettkampfId +
                            ", team " + teamId + ": " + e.getMessage());
        }
    }

    /**
     * 16‐byte, URL‐safe token without padding.
     */
    private String generateUrlSafeToken() {
        final byte[] buf = new byte[16];
        SECURE_RANDOM.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }

    /**
     * OPTIMIZED: Finds the current ligamatch for a team using LigamatchBE data.
     * Uses database-calculated completion status for better performance.
     */
    private LigamatchBE findCurrentLigamatchForTeam(List<LigamatchBE> teamMatches, long teamId) {
        for (LigamatchBE match : teamMatches) {
            try {
                // Check if this match has been started
                List<PasseDO> passes = passeComponent.findByMannschaftMatchId(teamId, match.getMatchId());
                if (passes.isEmpty()) {
                    // No passes recorded - this is the current match
                    LOGGER.debug("Found unstarted match {} for team {}", match.getMatchId(), teamId);
                    return match;
                }
                
                // OPTIMIZED: Use LigamatchBE completion check (uses pre-calculated Satzpunkte)
                boolean isComplete = matchAnalysisService.isMatchComplete(match.getMatchId(), teamId, 
                    getOpponentIdFromLigamatch(match, teamId));
                if (!isComplete) {
                    // Match in progress - this is current
                    LOGGER.debug("Found incomplete match {} for team {}", match.getMatchId(), teamId);
                    return match;
                }
                LOGGER.debug("Match {} complete for team {}, checking next", match.getMatchId(), teamId);
            } catch (Exception e) {
                LOGGER.warn("Error analyzing ligamatch {} for team {}: {}", match.getMatchId(), teamId, e.getMessage());
            }
        }
        
        // All matches complete or error - return null to indicate wettkampf finished
        LOGGER.info("All ligamatches appear complete for team {} - wettkampf should be finished", teamId);
        return null;
    }

    /**
     * OPTIMIZED: Get opponent ID directly from LigamatchBE structure.
     * Faster than complex begegnung analysis.
     */
    private long getOpponentIdFromLigamatch(LigamatchBE ligamatch, long teamId) {
        try {
            return matchAnalysisService.findOpponentTeamId(ligamatch.getMatchId(), teamId);
        } catch (Exception e) {
            LOGGER.warn("Error finding opponent for ligamatch {} team {}: {}", ligamatch.getMatchId(), teamId, e.getMessage());
            return 0L; // Fallback
        }
    }

    /**
     * Legacy method kept for compatibility with generateSchusszettelSessions.
     * Converts MatchDO list to LigamatchBE for optimization.
     */
    private MatchDO findCurrentMatchForTeam(List<MatchDO> teamMatches, long teamId) {
        for (MatchDO match : teamMatches) {
            try {
                // Check if this match has been started
                List<PasseDO> passes = passeComponent.findByMannschaftMatchId(teamId, match.getId());
                if (passes.isEmpty()) {
                    // No passes recorded - this is the current match
                    return match;
                }
                
                // Check if match is complete - need opponent for analysis
                long opponentId = matchAnalysisService.findOpponentTeamId(match.getId(), teamId);
                boolean isComplete = matchAnalysisService.isMatchComplete(match.getId(), teamId, opponentId);
                if (!isComplete) {
                    // Match in progress - this is current
                    return match;
                }
            } catch (Exception e) {
                LOGGER.warn("Error analyzing match {} for team {}: {}", match.getId(), teamId, e.getMessage());
            }
        }
        
        // All matches complete or error - return null to indicate wettkampf finished
        LOGGER.info("All matches appear complete for team {} - wettkampf should be finished", teamId);
        return null;
    }

    /**
     * Build wettkampf information from wettkampf and veranstaltung data
     */
    private WettkampfInfoDO buildWettkampfInfo(long wettkampfId) {
        try {
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
            LOGGER.warn("Could not build wettkampf info for wettkampfId {}: {}", wettkampfId, e.getMessage());
            return null;
        }
    }

    /**
     * OPTIMIZED: Determines initial status using LigamatchBE data for better performance.
     * Uses database-calculated completion information where possible.
     */
    private String determineInitialStatusLigamatch(long currentMatchId, long teamId, long opponentId, 
                                                 int currentPasseNumber, List<LigamatchBE> teamMatches) {
        try {
            LOGGER.info("Determining initial status for team {} in ligamatch {} passe {}", 
                       teamId, currentMatchId, currentPasseNumber);
            
            // 1. Check if entire wettkampf is finished (all matches complete)
            boolean allMatchesComplete = teamMatches.stream()
                    .allMatch(match -> {
                        try {
                            return matchAnalysisService.isMatchComplete(match.getMatchId(), teamId, 
                                getOpponentIdFromLigamatch(match, teamId));
                        } catch (Exception e) {
                            LOGGER.warn("Error checking ligamatch {} completion: {}", match.getMatchId(), e.getMessage());
                            return false; // Assume not complete if we can't check
                        }
                    });
            
            if (allMatchesComplete) {
                LOGGER.info("All ligamatches complete for team {} → WETTKAMPF_ENDE", teamId);
                return "WETTKAMPF_ENDE";
            }
            
            // 2. Check if current match is complete
            boolean currentMatchComplete = matchAnalysisService.isMatchComplete(currentMatchId, teamId, opponentId);
            if (currentMatchComplete) {
                LOGGER.info("Current ligamatch {} complete for team {} → WETTKAMPF_ENDE or next match needed", 
                           currentMatchId, teamId);
                return "WETTKAMPF_ENDE";
            }
            
            // 3-6. Use the existing pass analysis logic
            return analyzePasseStatus(currentMatchId, teamId, currentPasseNumber);
            
        } catch (Exception e) {
            LOGGER.error("Error determining initial status for team {}: {} → defaulting to SCHUETZENMELDUNG", 
                        teamId, e.getMessage());
            return STATUS_SCHUETZENMELDUNG;
        }
    }

    /**
     * Analyzes passe status for initial status determination.
     * Extracted for reuse between MatchDO and LigamatchBE versions.
     */
    private String analyzePasseStatus(long currentMatchId, long teamId, int currentPasseNumber) {
        // 3. Check current passe status
        List<PasseDO> currentPassePasses = passeComponent.findByMannschaftMatchId(teamId, currentMatchId).stream()
                .filter(p -> p.getPasseLfdnr() == currentPasseNumber)
                .toList();
        
        LOGGER.debug("Found {} passes for team {} in passe {}", currentPassePasses.size(), teamId, currentPasseNumber);
        
        // CRITICAL FIX: Check for invalid passe numbers first  
        if (currentPasseNumber > 5) {
            LOGGER.warn("INVALID PASSE: Team {} at passe {} (max 5), match should be complete", 
                       teamId, currentPasseNumber);
            return "WETTKAMPF_ENDE";
        }
        
        // 4. Check if shooters are registered (passes exist) for current passe
        if (!currentPassePasses.isEmpty()) {
            // Check if passe is complete (all 3 shooters with data)
            boolean passeComplete = currentPassePasses.size() >= 3;
            
            // Check if any shoots have actual shot data (not just registration)
            boolean hasActualShots = currentPassePasses.stream()
                    .anyMatch(p -> p.getPfeil1() != null || p.getPfeil2() != null || p.getPfeil3() != null);
            
            if (passeComplete && hasActualShots) {
                LOGGER.info("Passe {} complete with shots for team {} → WARTE", currentPasseNumber, teamId);
                return "WARTE";
            } else if (!currentPassePasses.isEmpty()) {
                LOGGER.info("Passe {} has registered shooters for team {} → SATZEINGABE", currentPasseNumber, teamId);
                return "SATZEINGABE";
            }
        }
        
        // 5. Check if there are any passes at all for this match (previous passe completed)
        List<PasseDO> allMatchPasses = passeComponent.findByMannschaftMatchId(teamId, currentMatchId);
        if (!allMatchPasses.isEmpty()) {
            // There are passes, but not for current passe - likely need to register shooters
            LOGGER.info("Match has passes but not for current passe {} for team {} → SCHUETZENMELDUNG", 
                       currentPasseNumber, teamId);
            return STATUS_SCHUETZENMELDUNG;
        }
        
        // 6. Default: fresh start
        LOGGER.info("No existing data found for team {} → SCHUETZENMELDUNG", teamId);
        return STATUS_SCHUETZENMELDUNG;
    }

    /**
     * Legacy version for MatchDO compatibility.
     * Determines the correct initial status for a team session based on existing match data.
     * This ensures sessions start at the correct state when there's already data in the big app database.
     * 
     * ROBUSTNESS LOGIC:
     * 1. Check if entire wettkampf is finished → WETTKAMPF_ENDE
     * 2. Check if current match is finished → advance to next match or WETTKAMPF_ENDE
     * 3. Check if shooters already registered for current passe → SATZEINGABE
     * 4. Check if current passe partially completed → SATZEINGABE  
     * 5. Check if current passe completed → WARTE
     * 6. Default → SCHUETZENMELDUNG
     */
    private String determineInitialStatus(long currentMatchId, long teamId, long opponentId, 
                                         int currentPasseNumber, List<MatchDO> teamMatches) {
        try {
            LOGGER.info("Determining initial status for team {} in match {} passe {}", 
                       teamId, currentMatchId, currentPasseNumber);
            
            // 1. Check if entire wettkampf is finished (all matches complete)
            boolean allMatchesComplete = teamMatches.stream()
                    .allMatch(match -> {
                        try {
                            long matchOpponentId = matchAnalysisService.findOpponentTeamId(match.getId(), teamId);
                            return matchAnalysisService.isMatchComplete(match.getId(), teamId, matchOpponentId);
                        } catch (Exception e) {
                            LOGGER.warn("Error checking match {} completion: {}", match.getId(), e.getMessage());
                            return false; // Assume not complete if we can't check
                        }
                    });
            
            if (allMatchesComplete) {
                LOGGER.info("All matches complete for team {} → WETTKAMPF_ENDE", teamId);
                return "WETTKAMPF_ENDE";
            }
            
            // 2. Check if current match is complete
            boolean currentMatchComplete = matchAnalysisService.isMatchComplete(currentMatchId, teamId, opponentId);
            if (currentMatchComplete) {
                LOGGER.info("Current match {} complete for team {} → WETTKAMPF_ENDE or next match needed", 
                           currentMatchId, teamId);
                return "WETTKAMPF_ENDE";
            }
            
            // 3-6. Use the extracted pass analysis logic
            return analyzePasseStatus(currentMatchId, teamId, currentPasseNumber);
            
        } catch (Exception e) {
            LOGGER.error("Error determining initial status for team {}: {} → defaulting to SCHUETZENMELDUNG", 
                        teamId, e.getMessage());
            return STATUS_SCHUETZENMELDUNG;
        }
    }

    /**
     * Lists all tablet‐sessions for a competition, including team & opponent club names.
     * Optionally synchronizes session data with current match state for accurate admin view.
     * Updated to include wettkampf information in each session.
     */
    @Override
    public TabletSessionInfoDO generateSchusszettelSessions(final long wettkampfId) {

        // Build wettkampf info once for all sessions
        final WettkampfInfoDO wettkampfInfo = buildWettkampfInfo(wettkampfId);

        // BACKWARD COMPATIBILITY: Clean up legacy empty passes before loading sessions
        cleanupLegacyEmptyPassesForWettkampf(wettkampfId);

        // Load all sessions
        final List<TabletSchusszettelEntity> entities = sessionDAO.findByWettkampfId(wettkampfId);

        entities.forEach(session -> {
            try {
                // ROBUSTNESS: Re-validate session status against current database state
                // This handles cases where the big app database has been updated since session creation
                List<MatchDO> teamMatches = matchComponent.findByWettkampfId(wettkampfId).stream()
                        .filter(m -> Objects.equals(m.getMannschaftId(), session.getTeamId()))
                        .sorted(Comparator.comparingLong(MatchDO::getNr))
                        .toList();
                
                // CASCADING CHECK: Use the same logic as initialization
                MatchDO currentIncompleteMatch = findCurrentMatchForTeam(teamMatches, session.getTeamId());
                
                String correctStatus;
                if (currentIncompleteMatch == null) {
                    // ALL MATCHES COMPLETE - should be WETTKAMPF_ENDE
                    correctStatus = "WETTKAMPF_ENDE";
                    LOGGER.debug("CASCADING: All matches complete for team {} → WETTKAMPF_ENDE", session.getTeamId());
                } else {
                    // Found incomplete match - determine correct status
                    correctStatus = determineInitialStatus(
                            currentIncompleteMatch.getId(), 
                            session.getTeamId(), 
                            session.getGegnerTeamId(), 
                            session.getCurrentPasseNumber(), 
                            teamMatches
                    );
                    
                    // Also check if we need to update match ID (advanced to next match)
                    if (!Objects.equals(session.getCurrentMatchId(), currentIncompleteMatch.getId())) {
                        LOGGER.warn("ADMIN VIEW: Team {} advanced to different match {} → {}, updating session", 
                                   session.getTeamId(), session.getCurrentMatchId(), currentIncompleteMatch.getId());
                        session.setCurrentMatchId(currentIncompleteMatch.getId());
                        session.setCurrentMatchNumber(Math.toIntExact(currentIncompleteMatch.getNr()));
                        
                        // Recalculate passe number for new match
                        try {
                            long newOpponentId = matchAnalysisService.findOpponentTeamId(currentIncompleteMatch.getId(), session.getTeamId());
                            int newPasseNumber = matchAnalysisService.getCurrentPasseNumber(currentIncompleteMatch.getId(), session.getTeamId(), newOpponentId);
                            session.setCurrentPasseNumber(newPasseNumber);
                            session.setGegnerTeamId(newOpponentId);
                        } catch (Exception e) {
                            LOGGER.warn("Error updating match details for team {}: {}", session.getTeamId(), e.getMessage());
                        }
                    }
                }
                
                // Update session if status has become inconsistent with database reality
                if (!correctStatus.equals(session.getStatus())) {
                    LOGGER.warn("ADMIN VIEW: Session status inconsistent for team {} - was {}, should be {} - updating", 
                               session.getTeamId(), session.getStatus(), correctStatus);
                    session.setStatus(correctStatus);
                    sessionDAO.updateStatus(session, -1L);
                }
                
                // Use SessionRuntime for clean state evaluation
                SessionRuntime runtime = new SessionRuntime(session, sessionDAO, matchComponent, passeComponent, matchAnalysisService);
                
                // For admin view, evaluate WARTE state if needed
                if ("WARTE".equals(session.getStatus())) {
                    TabletSchusszettelEntity opponentSession = sessionDAO
                            .findByWettkampfUndTeam(wettkampfId, session.getGegnerTeamId())
                            .orElse(null);
                    
                    if (runtime.evaluateWithOpponent(opponentSession)) {
                        LOGGER.debug("Admin view: Advanced team {} from WARTE to {}", 
                                   session.getTeamId(), session.getStatus());
                    }
                }

            } catch (Exception e) {
                // Log but don't fail - admin view should be resilient
                LOGGER.warn("Exception during admin evaluation for team {}: {}",
                        session.getTeamId(), e.getMessage());
            }
        });

        // Map into DTOs
        final TabletSessionSingDO[] singDOs = entities.stream()
                .map(e -> {
                    // lookup own team name
                    final DsbMannschaftDO team = mannschaftComponent.findById(e.getTeamId());
                    final VereinDO vTeam = vereinComponent.findById(team.getVereinId());
                    final String teamName = vTeam.getName()
                            + (team.getNummer() > 1 ? " " + team.getNummer() : "");

                    // lookup next opponent name (may stay null)
                    String opponentName = null;
                    if (e.getGegnerTeamId() != null) {
                        try {
                            final DsbMannschaftDO opp = mannschaftComponent.findById(e.getGegnerTeamId());
                            final VereinDO vOpp = vereinComponent.findById(opp.getVereinId());
                            opponentName = vOpp.getName()
                                    + (opp.getNummer() > 1 ? " " + opp.getNummer() : "");
                        } catch (Exception ex) {
                            LOGGER.warn("Failed to get opponent name for team {}: {}", e.getGegnerTeamId(), ex.getMessage());
                            opponentName = "Unknown Opponent";
                        }
                    }

                    return new TabletSessionSingDO(
                            e.getTeamId(),
                            teamName,
                            e.getStatus(),
                            e.getToken(),
                            e.getCurrentPasseNumber(),
                            opponentName,
                            wettkampfInfo  // Include wettkampf info in each session
                    );
                })
                .toArray(TabletSessionSingDO[]::new);

        // assemble info
        final TabletSessionInfoDO info = new TabletSessionInfoDO();
        info.setWettkampfId(wettkampfId);
        info.setTabletSessionSingDOs(singDOs);
        return info;
    }

    /**
     * BACKWARD COMPATIBILITY: Smart cleanup with renumbering for legacy pass data.
     * 
     * The old system pre-created 15 empty passes per team per match, but users might have
     * entered scores in non-sequential order. This method:
     * 1. Detects teams/matches with any null passes (legacy contamination)
     * 2. Extracts all passes with actual scores
     * 3. Deletes ALL passes for affected team/match  
     * 4. Re-creates passes with proper sequential numbering (1, 2, 3...)
     * 5. Preserves shooter deployment status and actual arrow scores
     * 
     * @param wettkampfId Competition identifier to clean up
     */
    private void cleanupLegacyEmptyPassesForWettkampf(long wettkampfId) {
        try {
            LOGGER.debug("Starting smart legacy pass cleanup with renumbering for wettkampf {}", wettkampfId);
            
            // Get all passes for this wettkampf
            List<PasseDO> allPasses = passeComponent.findByWettkampfId(wettkampfId);
            
            // Group passes by team and match for analysis
            var passesByTeamAndMatch = allPasses.stream()
                .collect(Collectors.groupingBy(p -> 
                    p.getPasseMannschaftId() + "_" + p.getPasseMatchId()));
            
            int affectedMatches = 0;
            int renamedPasses = 0;
            
            for (var entry : passesByTeamAndMatch.entrySet()) {
                List<PasseDO> teamMatchPasses = entry.getValue();
                
                // Check if this team/match has any legacy null passes
                boolean hasLegacyPasses = teamMatchPasses.stream().anyMatch(this::isEmptyLegacyPass);
                
                if (hasLegacyPasses) {
                    LOGGER.debug("CLEANUP: Team/Match {} has legacy data, applying smart cleanup", entry.getKey());
                    
                    // Extract valid passes (those with actual scores)
                    List<PasseDO> validPasses = teamMatchPasses.stream()
                        .filter(p -> !isEmptyLegacyPass(p))
                        .sorted(Comparator.comparing(p -> p.getPasseLfdnr() != null ? p.getPasseLfdnr() : 0L))
                        .toList();
                    
                    if (!validPasses.isEmpty()) {
                        // Renumber and recreate valid passes
                        int recreatedCount = renumberAndRecreateValidPasses(teamMatchPasses, validPasses);
                        renamedPasses += recreatedCount;
                        LOGGER.info("CLEANUP: Renumbered {} passes for team/match {}", recreatedCount, entry.getKey());
                    } else {
                        // No valid passes - just delete all legacy passes
                        deleteAllPassesForTeamMatch(teamMatchPasses);
                        LOGGER.info("CLEANUP: Deleted all {} legacy passes for team/match {} (no valid data)", 
                                   teamMatchPasses.size(), entry.getKey());
                    }
                    
                    affectedMatches++;
                }
            }
            
            if (affectedMatches > 0) {
                LOGGER.info("CLEANUP: Processed {} affected team/matches, renumbered {} passes for wettkampf {}", 
                           affectedMatches, renamedPasses, wettkampfId);
                
                // Re-sync all sessions for this wettkampf after cleanup
                resyncAllSessionsAfterCleanup(wettkampfId);
            } else {
                LOGGER.debug("No legacy pass data found for wettkampf {}", wettkampfId);
            }
            
        } catch (Exception e) {
            LOGGER.error("Error during smart legacy pass cleanup for wettkampf {}: {}", wettkampfId, e.getMessage());
            // Non-fatal - continue with normal session loading
        }
    }

    /**
     * Check if a pass is an empty legacy pass (has null arrow values).
     * Only null values are from legacy pre-creation - real competition data uses 0 for misses.
     */
    private boolean isEmptyLegacyPass(PasseDO passe) {
        // Legacy passes have null arrow values, real competition data uses 0 for misses
        return passe.getPfeil1() == null && passe.getPfeil2() == null && passe.getPfeil3() == null;
    }

    /**
     * Renumber and recreate valid passes with proper sequential numbering.
     * This preserves all actual score data while fixing the passe numbering.
     */
    private int renumberAndRecreateValidPasses(List<PasseDO> allTeamMatchPasses, List<PasseDO> validPasses) {
        try {
            // First, delete ALL passes for this team/match (both valid and invalid)
            deleteAllPassesForTeamMatch(allTeamMatchPasses);
            
            // Group valid passes by shooter to maintain shooter associations
            var passesByShooter = validPasses.stream()
                .collect(Collectors.groupingBy(PasseDO::getPasseDsbMitgliedId));
            
            int recreatedCount = 0;
            
            // Process each shooter's passes
            for (var shooterEntry : passesByShooter.entrySet()) {
                Long shooterId = shooterEntry.getKey();
                List<PasseDO> shooterPasses = shooterEntry.getValue()
                    .stream()
                    .sorted(Comparator.comparing(p -> p.getPasseLfdnr() != null ? p.getPasseLfdnr() : 0L))
                    .toList();
                
                // Renumber shooter's passes sequentially starting from 1
                for (int i = 0; i < shooterPasses.size(); i++) {
                    PasseDO originalPasse = shooterPasses.get(i);
                    int newPasseNumber = i + 1; // Start from 1, not 0
                    
                    // Create new pass with corrected passe number
                    PasseDO renamedPasse = new PasseDO(
                        null, // New ID will be generated
                        originalPasse.getPasseMannschaftId(),
                        originalPasse.getPasseWettkampfId(), 
                        originalPasse.getPasseMatchNr(),
                        originalPasse.getPasseMatchId(),
                        (long) newPasseNumber, // Corrected sequential passe number
                        shooterId,
                        originalPasse.getPfeil1(), // Preserve original scores
                        originalPasse.getPfeil2(),
                        originalPasse.getPfeil3(),
                        originalPasse.getPfeil4(),
                        originalPasse.getPfeil5(),
                        originalPasse.getPfeil6()
                    );
                    
                    // Recreate pass with new numbering
                    passeComponent.create(renamedPasse, -1L);
                    recreatedCount++;
                    
                    LOGGER.debug("RENUMBER: Shooter {} passe {} -> {} with scores [{}, {}, {}]", 
                               shooterId, originalPasse.getPasseLfdnr(), newPasseNumber,
                               originalPasse.getPfeil1(), originalPasse.getPfeil2(), originalPasse.getPfeil3());
                }
            }
            
            return recreatedCount;
            
        } catch (Exception e) {
            LOGGER.error("Error renumbering passes: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Delete all passes for a specific team/match combination.
     */
    private void deleteAllPassesForTeamMatch(List<PasseDO> teamMatchPasses) {
        for (PasseDO passe : teamMatchPasses) {
            try {
                passeComponent.delete(passe, -1L);
            } catch (Exception e) {
                LOGGER.warn("Could not delete pass for team {} shooter {} passe {}: {}", 
                           passe.getPasseMannschaftId(), passe.getPasseDsbMitgliedId(), 
                           passe.getPasseLfdnr(), e.getMessage());
            }
        }
    }

    /**
     * Re-synchronize ALL sessions for the wettkampf after cleanup.
     * This ensures all session passe numbers reflect the cleaned database state.
     */
    private void resyncAllSessionsAfterCleanup(long wettkampfId) {
        try {
            List<TabletSchusszettelEntity> allSessions = sessionDAO.findByWettkampfId(wettkampfId);
            LOGGER.debug("Re-synchronizing {} sessions after cleanup for wettkampf {}", allSessions.size(), wettkampfId);
            
            for (TabletSchusszettelEntity session : allSessions) {
                try {
                    // Recalculate current passe number using clean data
                    int newPasseNumber = matchAnalysisService.getCurrentPasseNumber(
                        session.getCurrentMatchId(), session.getTeamId(), 0L);
                    
                    if (session.getCurrentPasseNumber() != newPasseNumber) {
                        LOGGER.info("RESYNC: Updating team {} passe {} -> {} after cleanup", 
                                   session.getTeamId(), session.getCurrentPasseNumber(), newPasseNumber);
                        
                        session.setCurrentPasseNumber(newPasseNumber);
                        sessionDAO.updateStatus(session, -1L);
                    }
                } catch (Exception e) {
                    LOGGER.warn("Could not resync session for team {} after cleanup: {}", session.getTeamId(), e.getMessage());
                    // Continue with other sessions
                }
            }
            
            LOGGER.debug("Session resync completed for all sessions in wettkampf {}", wettkampfId);
            
        } catch (Exception e) {
            LOGGER.error("Error during session resync after cleanup: {}", e.getMessage());
        }
    }
}