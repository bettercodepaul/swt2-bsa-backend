package de.bogenliga.application.business.schusszettel.impl.business;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
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
import de.bogenliga.application.business.schusszettel.impl.business.serviceAdapter.MatchAnalysisService;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
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
 * Administrative component for tablet schusszettel session lifecycle management.
 * 
 * <h2>CURRENT RESPONSIBILITIES</h2>
 * <ul>
 *   <li>Session initialization and deletion for competitions</li>
 *   <li>Token management and regeneration</li>
 *   <li>Admin UI session overview with team names</li>
 *   <li>Legacy pass data cleanup and migration</li>
 * </ul>
 * 
 * <h2>CLEAN ARCHITECTURE DESIGN</h2>
 * This component serves as the admin interface layer that delegates session creation
 * to SessionRuntime and provides UI-friendly session listings. It does not contain
 * business logic for score entry or state transitions.
 * 
 * <h2>SESSION INITIALIZATION FLOW</h2>
 * <ul>
 *   <li>Delete existing sessions for clean start</li>
 *   <li>Identify all teams in competition via LigamatchBE</li>
 *   <li>Delegate to SessionRuntime.initializeSession() for each team</li>
 *   <li>SessionRuntime determines initial state using MatchAnalysisService</li>
 * </ul>
 * 
 * <h2>ADMIN SESSION OVERVIEW</h2>
 * <ul>
 *   <li>Lists all active sessions with team and opponent names</li>
 *   <li>Shows current state and pass number</li>
 *   <li>Provides tokens for tablet access</li>
 *   <li>Minimal WARTE evaluation for display accuracy</li>
 * </ul>
 * 
 * <h2>TOKEN SECURITY</h2>
 * <ul>
 *   <li>16-byte cryptographically secure random tokens</li>
 *   <li>URL-safe Base64 encoding without padding</li>
 *   <li>Token regeneration preserves all session state</li>
 * </ul>
 * 
 * <h2>LEGACY COMPATIBILITY</h2>
 * <ul>
 *   <li>Automatic cleanup of pre-created empty passes</li>
 *   <li>Smart renumbering of existing pass data</li>
 *   <li>Session resynchronization after cleanup</li>
 * </ul>
 * 
 * @author Marty Lauterbach - Clean architecture implementation
 */
@Service
public class TabletSchusszettelAdminComponentImpl implements TabletSchusszettelAdminComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSchusszettelAdminComponentImpl.class);


    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final TabletSchusszettelDAO sessionDAO;
    private final MatchComponent matchComponent;
    private final DsbMannschaftComponent mannschaftComponent;
    private final VereinComponent vereinComponent;
    private final PasseComponent passeComponent;
    private final MatchAnalysisService matchAnalysisService;
    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;

    @Autowired
    public TabletSchusszettelAdminComponentImpl(final TabletSchusszettelDAO sessionDAO,
                                                final MatchComponent matchComponent,
                                                final DsbMannschaftComponent mannschaftComponent,
                                                final VereinComponent vereinComponent,
                                                final PasseComponent passeComponent,
                                                final MatchAnalysisService matchAnalysisService,
                                                final WettkampfComponent wettkampfComponent,
                                                final VeranstaltungComponent veranstaltungComponent,
                                                final MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                                                final DsbMitgliedComponent dsbMitgliedComponent) {
        this.sessionDAO        = sessionDAO;
        this.matchComponent    = matchComponent;
        this.mannschaftComponent = mannschaftComponent;
        this.vereinComponent     = vereinComponent;
        this.passeComponent      = passeComponent;
        this.matchAnalysisService = matchAnalysisService;
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeForWettkampf(final long wettkampfId) {
        try {
            // Delete existing sessions for clean start
            sessionDAO.deleteByWettkampfId(wettkampfId);

            // Get all teams for this wettkampf
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

            // Initialize session for each team
            for (final Long teamId : teamIds) {
                try {
                    String token = generateUrlSafeToken();
                    SessionRuntime runtime = SessionRuntime.initializeSession(
                        wettkampfId, teamId, token,
                        sessionDAO, matchComponent, passeComponent, matchAnalysisService, 
                        mannschaftsmitgliedComponent, dsbMitgliedComponent, wettkampfComponent, veranstaltungComponent);
                    
                    LOGGER.info("Initialized session for team {} with status {}", 
                               teamId, runtime.getCurrentState());
                               
                } catch (Exception e) {
                    LOGGER.error("Failed to initialize session for team {} in wettkampf {}: {}", 
                                teamId, wettkampfId, e.getMessage());
                    throw e;
                }
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
     * Build wettkampf information using StateContext helper method.
     */
    private WettkampfInfoDO buildWettkampfInfo(long wettkampfId) {
        try {
            // Create a minimal StateContext for the helper method
            // This avoids code duplication while maintaining clean architecture
            de.bogenliga.application.business.schusszettel.impl.business.domain.states.StateContext context = 
                new de.bogenliga.application.business.schusszettel.impl.business.domain.states.StateContext(
                    null, // session not needed for this helper
                    null, // runtime not needed for this helper
                    matchComponent,
                    passeComponent,
                    matchAnalysisService,
                    mannschaftsmitgliedComponent,
                    dsbMitgliedComponent,
                    wettkampfComponent,
                    veranstaltungComponent
                ) {
                    @Override
                    public long getWettkampfId() {
                        return wettkampfId;
                    }
                };
            
            return context.buildWettkampfInfo();
        } catch (Exception e) {
            LOGGER.warn("Could not build wettkampf info for wettkampfId {}: {}", wettkampfId, e.getMessage());
            return null;
        }
    }




    /**
     * Generates admin session overview with team names and current status.
     */
    @Override
    public TabletSessionInfoDO generateSchusszettelSessions(final long wettkampfId) {

        // Build wettkampf info and clean legacy data
        final WettkampfInfoDO wettkampfInfo = buildWettkampfInfo(wettkampfId);
        cleanupLegacyEmptyPassesForWettkampf(wettkampfId);

        // Load all sessions for display
        final List<TabletSchusszettelEntity> entities = sessionDAO.findByWettkampfId(wettkampfId);

        // Minimal WARTE evaluation for display accuracy only
        entities.forEach(session -> {
            try {
                // Only evaluate WARTE state for display - no state modification
                if ("WARTE".equals(session.getStatus())) {
                    SessionRuntime runtime = new SessionRuntime(session, sessionDAO, matchComponent, passeComponent, matchAnalysisService, 
                        mannschaftsmitgliedComponent, dsbMitgliedComponent, wettkampfComponent, veranstaltungComponent);
                    TabletSchusszettelEntity opponentSession = sessionDAO
                            .findByWettkampfUndTeam(wettkampfId, session.getGegnerTeamId())
                            .orElse(null);
                    
                    // Evaluate for display only - state changes handled by tablet operations
                    runtime.evaluateWithOpponentWAITstate(opponentSession);
                }
            } catch (Exception e) {
                LOGGER.debug("Minor error during admin display evaluation for team {}: {}", 
                           session.getTeamId(), e.getMessage());
                // Continue with display - don't modify session state on errors
            }
        });

        // Map into DTOs
        final TabletSessionSingDO[] singDOs = entities.stream()
                .map(e -> {
                    // lookup own team name - just verein name
                    final DsbMannschaftDO team = mannschaftComponent.findById(e.getTeamId());
                    final VereinDO vTeam = vereinComponent.findById(team.getVereinId());
                    final String teamName = vTeam.getName();

                    // lookup next opponent name (may stay null)
                    String opponentName = null;
                    if (e.getGegnerTeamId() != null) {
                        try {
                            final DsbMannschaftDO opp = mannschaftComponent.findById(e.getGegnerTeamId());
                            final VereinDO vOpp = vereinComponent.findById(opp.getVereinId());
                            opponentName = vOpp.getName();
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
     * Smart cleanup and renumbering for pre-created empty passes.
     * <p>
     * Handles data migration from systems that pre-created empty passes:
     * 1. Detects matches with null pass entries
     * 2. Extracts passes with actual scores
     * 3. Deletes all passes for affected matches
     * 4. Re-creates passes with sequential numbering (1, 2, 3...)
     * 5. Preserves all actual score data
     * 
     * @param wettkampfId Competition identifier to clean up
     */
    private void cleanupLegacyEmptyPassesForWettkampf(long wettkampfId) {
        try {
            LOGGER.debug("Starting smart pass cleanup with renumbering for wettkampf {}", wettkampfId);
            
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
                
                // Check if this team/match has any empty pre-created passes
                boolean hasLegacyPasses = teamMatchPasses.stream().anyMatch(this::isEmptyLegacyPass);
                
                if (hasLegacyPasses) {
                    LOGGER.debug("CLEANUP: Team/Match {} has pre-created data, applying smart cleanup", entry.getKey());
                    
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