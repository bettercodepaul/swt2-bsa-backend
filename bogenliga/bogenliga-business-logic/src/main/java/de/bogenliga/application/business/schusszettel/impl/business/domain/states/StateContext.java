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
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.WettkampfInfoDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

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
        session.setStatus(newStatus);
        sessionDAO.updateStatus(session, 0L);
        LOGGER.debug("StateContext updated session {} status to {}", session.getTeamId(), newStatus);
    }
    
    public void updatePasseNumber(int newPasseNumber) {
        session.setCurrentPasseNumber(newPasseNumber);
        sessionDAO.updateStatus(session, 0L);
        LOGGER.debug("StateContext updated session {} passe to {}", session.getTeamId(), newPasseNumber);
    }
    
    public void advanceToNextMatch(LigamatchBE nextMatch, long opponentId) {
        session.setCurrentMatchId(nextMatch.getMatchId());
        session.setCurrentMatchNumber(Math.toIntExact(nextMatch.getMatchNr()));
        session.setCurrentPasseNumber(1);
        session.setStatus(State.STATUS_SCHUETZENMELDUNG);
        session.setGegnerTeamId(opponentId);
        sessionDAO.updateStatus(session, 0L);
        
        LOGGER.info("StateContext advanced team {} from match to match {} (opponent: {})",
                   session.getTeamId(), nextMatch.getMatchId(), opponentId);
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
    
    // === VALIDATION HELPERS ===
    
    public boolean validateArrowValue(Integer arrowValue) {
        return arrowValue != null && arrowValue >= 0 && arrowValue <= 10;
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