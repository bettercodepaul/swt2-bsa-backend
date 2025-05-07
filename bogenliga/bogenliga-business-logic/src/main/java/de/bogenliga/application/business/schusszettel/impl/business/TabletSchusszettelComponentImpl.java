package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.business.schusszettel.api.types.inside.*;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSessionDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSessionEntity;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Core implementation of the Tablet Schusszettel workflow.
 * Handles session state (GET), shooter registration (POST), shot entry (POST), and transitions.
 */
@Service
public class TabletSchusszettelComponentImpl implements TabletSchusszettelComponent {

    // Constants controlling match logic
    private static final int MAX_SETS = 5;                     // Maximum number of sets per match
    private static final int SHOOTERS_PER_TEAM = 3;            // Exactly three shooters per set

    // State identifiers for session
    private static final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";
    private static final String STATUS_SATZEINGABE      = "SATZEINGABE";
    private static final String STATUS_WARTE           = "WARTE";
    private static final String STATUS_WETTKAMPF_ENDE  = "WETTKAMPF_ENDE";

    // Injected business components / DAOs
    private final TabletSessionDAO            sessionDAO;
    private final PasseComponent              passeComponent;
    private final MatchComponent              matchComponent;
    private final MannschaftsmitgliedComponent mmComponent;
    private final DsbMitgliedComponent        mitgliedComponent;
    private final DsbMannschaftComponent      mannschaftComponent;
    private final VereinComponent             vereinComponent;

    @Autowired
    public TabletSchusszettelComponentImpl(
            TabletSessionDAO sessionDAO,
            PasseComponent passeComponent,
            MatchComponent matchComponent,
            MannschaftsmitgliedComponent mmComponent,
            DsbMitgliedComponent mitgliedComponent,
            DsbMannschaftComponent mannschaftComponent,
            VereinComponent vereinComponent) {
        this.sessionDAO        = sessionDAO;
        this.passeComponent    = passeComponent;
        this.matchComponent    = matchComponent;
        this.mmComponent       = mmComponent;
        this.mitgliedComponent = mitgliedComponent;
        this.mannschaftComponent = mannschaftComponent;
        this.vereinComponent     = vereinComponent;
    }

    /**
     * GET /api/tablet-schusszettel
     * -> Validate token & session
     * -> Build full match history and state-specific details
     */
    @Override
    public TabletSchusszettelDO getStatus(long wettkampfId, long teamId, String token) {
        // 1) Token presence / emptiness check
        if (token == null || token.isEmpty()) {
            return notAllowed();
        }
        // 2) Lookup session, return NOT_ALLOWED if none
        TabletSessionEntity session = sessionDAO.findByToken(wettkampfId, teamId, token)
                .orElse(null);
        if (session == null) {
            return notAllowed();
        }

        // Extract match and opponent IDs
        long matchId = session.getCurrentMatchId();
        long oppTeam = session.getGegnerTeamId();
        // Fetch all recorded passes for this match
        List<PasseDO> passen = passeComponent.findByMatchId(matchId);
        // Build full set history (Satz-Ergebnisse)
        List<SatzErgebnisDO> satzHistory = buildSatzErgebnisse(passen, teamId, oppTeam, Integer.MAX_VALUE);

        // Populate base DTO
        TabletSchusszettelDO result = new TabletSchusszettelDO();
        result.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(session.getStatus()));
        result.setEigenesTeam(getTeamInfo(teamId));
        result.setGegnerischesTeam(getTeamInfo(oppTeam));
        result.setCurrentMatchId(matchId);
        result.setCurrentPasseNumber(session.getCurrentPasseNumber());
        result.setSatzErgebnisse(satzHistory);
        result.setSchuetzenMatchPunkte(buildMatchPunkte(passen, teamId));
        result.setMatchErgebnis(buildTeamMatchInfo(satzHistory, teamId, oppTeam));

        // Append additional data depending on session state
        switch (session.getStatus()) {
            case STATUS_SCHUETZENMELDUNG:
                handleSchuetzenmeldung(session, result, teamId);
                break;
            case STATUS_SATZEINGABE:
                handleSatzeingabe(session, result);
                break;
            case STATUS_WARTE:
                handleWarte(session, result, wettkampfId, teamId);
                break;
            case STATUS_WETTKAMPF_ENDE:
                handleEnde(session, result, wettkampfId, teamId);
                break;
            default:
                // Unexpected state => ignore
        }
        return result;
    }

    /**
     * Convenience for NOT_ALLOWED response
     */
    private TabletSchusszettelDO notAllowed() {
        TabletSchusszettelDO dto = new TabletSchusszettelDO();
        dto.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED);
        return dto;
    }

    /**
     * POST /api/tablet-schusszettel (Schuetzenmeldung)
     * -> Validate exactly 3 shooters
     * -> Verify membership
     * -> Create placeholder passes to lock-in registration
     * -> Transition session to SATZEINGABE
     */
    @Override
    public void submitSchuetzen(long wettkampfId, long teamId, String token, SchuetzenMeldungDO input) {
        // Validate payload
        if (input == null
                || input.getGemeldeteSchuetzen() == null
                || input.getGemeldeteSchuetzen().size() != SHOOTERS_PER_TEAM) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Exactly 3 shooters must be selected");
        }

        // Validate session exists
        TabletSessionEntity session = sessionDAO.findByToken(wettkampfId, teamId, token)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.NO_PERMISSION_ERROR,
                        "Invalid or expired token"));

        // Register each shooter
        for (Long dsbId : input.getGemeldeteSchuetzen()) {
            // Membership check via component
            if (mmComponent.findByMemberAndTeamId(teamId, dsbId) == null) {
                throw new BusinessException(
                        ErrorCode.NO_PERMISSION_ERROR,
                        "Shooter " + dsbId + " is not on team " + teamId);
            }
            // Create a 'registration' PasseDO with zero scores to reserve the slot
            PasseDO passe = new PasseDO(
                    null,
                    teamId,
                    wettkampfId,
                    session.getCurrentPasseNumber(),
                    session.getCurrentMatchId(),
                    session.getCurrentPasseNumber(),
                    dsbId,
                    0, 0, 0, 0, 0, 0);
            passeComponent.create(passe, /*userId*/ -1L);
        }
        // Move to shot entry
        session.setStatus(STATUS_SATZEINGABE);
        session.setCurrentPasseNumber(1);
        sessionDAO.updateStatus(session, /*userId*/ -1L);
    }

    /**
     * POST /api/tablet-schusszettel (Satzeingabe)
     * -> Persist each shooter’s three arrows
     * -> Check if both teams completed this set
     *    • if yes: evaluate match completeness and advance set/match/day
     *    • if no: set state to WARTE for waiting team
     */
    @Override
    public void submitSatz(long wettkampfId, long teamId, String token, SatzEingabeDO eingabe) {
        // Validate payload
        if (eingabe == null || eingabe.getSatzeingabe() == null
                || eingabe.getSatzeingabe().size() != SHOOTERS_PER_TEAM) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Three shooters’ scores required");
        }

        // Retrieve session
        TabletSessionEntity session = sessionDAO.findByToken(wettkampfId, teamId, token)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.NO_PERMISSION_ERROR,
                        "Invalid or expired token"));

        long matchId = session.getCurrentMatchId();
        long passeNr = session.getCurrentPasseNumber();

        // Persist each shot set
        for (SchuetzenSatzDO s : eingabe.getSatzeingabe()) {
            PasseDO passe = new PasseDO(
                    null,
                    teamId,
                    wettkampfId,
                    session.getCurrentMatchNumber(),
                    matchId,
                    passeNr,
                    s.getSchuetzenId(),
                    s.getSchuss1(),
                    s.getSchuss2(),
                    s.getSchuss3(),
                    0, 0);
            passeComponent.create(passe, /*userId*/ -1L);
        }

        // Count entries for this set
        long ownCount = passeComponent.findByMatchId(matchId).stream()
                .filter(p -> p.getPasseMannschaftId() == teamId && p.getPasseLfdnr() == passeNr)
                .count();
        long oppCount = passeComponent.findByMatchId(matchId).stream()
                .filter(p -> p.getPasseMannschaftId() == session.getGegnerTeamId() && p.getPasseLfdnr() == passeNr)
                .count();

        if (ownCount == SHOOTERS_PER_TEAM && oppCount == SHOOTERS_PER_TEAM) {
            // Both completed: evaluate match status
            List<SatzErgebnisDO> fullHistory = buildSatzErgebnisse(
                    passeComponent.findByMatchId(matchId),
                    teamId, session.getGegnerTeamId(), Integer.MAX_VALUE);
            if (isMatchComplete(fullHistory)) {
                // Match done → next match or end of day
                advanceToNextMatchOrEnd(wettkampfId, session);
            } else {
                // Next set
                session.setCurrentPasseNumber(passeNr + 1);
                session.setStatus(STATUS_SATZEINGABE);
                sessionDAO.updateStatus(session, -1L);
            }
        } else {
            // One side done → wait for opponent
            session.setStatus(STATUS_WARTE);
            sessionDAO.updateStatus(session, -1L);
        }
    }

    /**
     * Initialize sessions at start of competition day for a team
     */
    @Override
    public void initializeForWettkampf(long wettkampfId, long teamId) {
        // Get all team matches sorted by round number
        List<MatchDO> teamMatches = matchComponent.findByWettkampfId(wettkampfId).stream()
                .filter(m -> m.getMannschaftId() == teamId)
                .sorted(Comparator.comparingLong(MatchDO::getNr))
                .collect(Collectors.toList());
        if (teamMatches.isEmpty()) {
            throw new BusinessException(
                    ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    "No matches for team " + teamId);
        }
        TabletSessionEntity session = new TabletSessionEntity();
        session.setWettkampfId(wettkampfId);
        session.setTeamId(teamId);
        session.setCurrentMatchId(teamMatches.get(0).getId());
        session.setCurrentPasseNumber(1);
        session.setStatus(STATUS_SCHUETZENMELDUNG);
        session.setGegnerTeamId(findOpponentTeamId(teamMatches.get(0), teamId));
        sessionDAO.createSession(session, -1L);
    }

    @Override
    public void deleteForWettkampf(long wettkampfId) {
        sessionDAO.deleteByWettkampfId(wettkampfId);
    }

    @Override
    public boolean existsForWettkampf(long wettkampfId) {
        return sessionDAO.existsByWettkampfId(wettkampfId);
    }

    //================================================================================
    // Private helpers
    //================================================================================

    /**
     * SCHUETZENMELDUNG: return list of selectable shooters with IDs & names
     */
    private void handleSchuetzenmeldung(TabletSessionEntity session,
                                        TabletSchusszettelDO out,
                                        long teamId) {
        List<MannschaftsmitgliedDO> members = mmComponent.findByTeamId(teamId);
        List<VerfuegbarerSchuetzeDO> available = members.stream()
                .map(m -> {
                    DsbMitgliedDO dm = mitgliedComponent.findById(m.getDsbMitgliedId());
                    return new VerfuegbarerSchuetzeDO(dm.getId(), dm.getVorname() + " " + dm.getNachname());
                }).collect(Collectors.toList());
        out.setVerfuegbareSchuetzen(available);
    }

    /**
     * SATZEINGABE: show assigned shooters (Stammdaten) + remaining selectable ones
     */
    private void handleSatzeingabe(TabletSessionEntity session, TabletSchusszettelDO out) {
        long matchId = session.getCurrentMatchId();
        long passeNr = session.getCurrentPasseNumber();
        List<PasseDO> assigns = passeComponent.findByMatchId(matchId).stream()
                .filter(p -> p.getPasseLfdnr() == passeNr)
                .collect(Collectors.toList());
        // Map assigned passes to shooter metadata
        List<SchuetzenStammdatenDO> meta = assigns.stream().map(p -> {
            DsbMitgliedDO dm = mitgliedComponent.findById(p.getPasseDsbMitgliedId());
            MannschaftsmitgliedDO mm = mmComponent.findByMemberAndTeamId(session.getTeamId(), p.getPasseDsbMitgliedId());
            return new SchuetzenStammdatenDO(dm.getId(), mm.getRueckennummer(), dm.getVorname(), dm.getNachname());
        }).collect(Collectors.toList());
        out.setSchuetzenStammDaten(meta);
        // Compute remaining
        Set<Long> used = assigns.stream().map(PasseDO::getPasseDsbMitgliedId).collect(Collectors.toSet());
        List<VerfuegbarerSchuetzeDO> left = mmComponent.findByTeamId(session.getTeamId()).stream()
                .map(MannschaftsmitgliedDO::getDsbMitgliedId)
                .filter(id -> !used.contains(id))
                .map(id -> {
                    DsbMitgliedDO dm = mitgliedComponent.findById(id);
                    return new VerfuegbarerSchuetzeDO(dm.getId(), dm.getVorname() + " " + dm.getNachname());
                }).collect(Collectors.toList());
        out.setVerfuegbareSchuetzen(left);
    }

    /**
     * WARTE: if opponent also waiting → advance set or match/day; else repeat SATZEINGABE view
     */
    private void handleWarte(TabletSessionEntity session,
                             TabletSchusszettelDO out,
                             long wettkampfId,
                             long teamId) {
        Optional<TabletSessionEntity> oppSession = sessionDAO.findByWettkampfUndTeam(
                wettkampfId, session.getGegnerTeamId());
        // Both sides waiting?
        if (oppSession.isPresent() && STATUS_WARTE.equals(oppSession.get().getStatus())) {
            List<SatzErgebnisDO> full = buildSatzErgebnisse(
                    passeComponent.findByMatchId(session.getCurrentMatchId()),
                    teamId, session.getGegnerTeamId(), Integer.MAX_VALUE);
            if (isMatchComplete(full)) {
                // match over
                advanceToNextMatchOrEnd(wettkampfId, session);
                out.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(session.getStatus()));
                handleSchuetzenmeldung(session, out, teamId);
            } else {
                // next set
                session.setCurrentPasseNumber(session.getCurrentPasseNumber() + 1);
                session.setStatus(STATUS_SATZEINGABE);
                sessionDAO.updateStatus(session, -1L);
                out.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);
                handleSatzeingabe(session, out);
            }
        } else {
            // still waiting for opponent → show SATZEINGABE view
            handleSatzeingabe(session, out);
        }
    }

    /**
     * WETTKAMPF_ENDE: clear input-related fields and show final match recap
     */
    private void handleEnde(TabletSessionEntity session,
                            TabletSchusszettelDO out,
                            long wettkampfId,
                            long teamId) {
        // clear transient data
        out.setSatzErgebnisse(Collections.emptyList());
        out.setSchuetzenMatchPunkte(Collections.emptyList());
        out.setSchuetzenStammDaten(Collections.emptyList());
        out.setVerfuegbareSchuetzen(Collections.emptyList());

        // build recap across all team matches
        List<TeamMatchInfoDO> recap = matchComponent.findByWettkampfId(wettkampfId).stream()
                .filter(m -> m.getMannschaftId() == teamId)
                .flatMap(m -> {
                    long opp = findOpponentTeamId(m, teamId);
                    List<PasseDO> ps = passeComponent.findByMatchId(m.getId());
                    return buildTeamMatchInfo(
                            buildSatzErgebnisse(ps, teamId, opp, Integer.MAX_VALUE),
                            teamId, opp).stream();
                }).collect(Collectors.toList());
        out.setMatchErgebnis(recap);
    }

    /**
     * Find opponent team ID using match round & pairing logic
     */
    private long findOpponentTeamId(MatchDO m, long own) {
        return matchComponent.findByWettkampfId(m.getWettkampfId()).stream()
                .filter(o -> o.getNr() == m.getNr()
                        && o.getBegegnung() == m.getBegegnung()
                        && o.getMannschaftId() != own)
                .findFirst()
                .map(MatchDO::getMannschaftId)
                .orElseThrow(() -> new TechnicalException(
                        ErrorCode.INTERNAL_ERROR,
                        "Opponent not found for match " + m.getId()));
    }

    /**
     * Sum up arrow points per shooter in this match
     */
    private List<SchuetzeMatchPunkteDO> buildMatchPunkte(List<PasseDO> passen, long teamId) {
        return passen.stream()
                .filter(p -> p.getPasseMannschaftId() == teamId)
                .collect(Collectors.groupingBy(
                        PasseDO::getPasseDsbMitgliedId,
                        Collectors.summingInt(x -> x.getPfeil1() + x.getPfeil2() + x.getPfeil3())))
                .entrySet().stream()
                .map(e -> new SchuetzeMatchPunkteDO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Build list of set results up to given set number
     */
    private List<SatzErgebnisDO> buildSatzErgebnisse(
            List<PasseDO> passen, long t1, long t2, int upto) {
        return passen.stream()
                .filter(p -> p.getPasseLfdnr() <= upto)
                .collect(Collectors.groupingBy(PasseDO::getPasseLfdnr))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    int set = e.getKey();
                    int sum1 = e.getValue().stream()
                            .filter(p -> p.getPasseMannschaftId() == t1)
                            .mapToInt(p -> p.getPfeil1() + p.getPfeil2() + p.getPfeil3())
                            .sum();
                    int sum2 = e.getValue().stream()
                            .filter(p -> p.getPasseMannschaftId() == t2)
                            .mapToInt(p -> p.getPfeil1() + p.getPfeil2() + p.getPfeil3())
                            .sum();
                    return new SatzErgebnisDO(set, sum1, sum2);
                }).collect(Collectors.toList());
    }

    /**
     * Checks if match is won by points (>=6) or max sets reached
     */
    private boolean isMatchComplete(List<SatzErgebnisDO> sets) {
        int mp1=0, mp2=0;
        for (SatzErgebnisDO s : sets) {
            if (s.getTeam1Punkte() > s.getTeam2Punkte()) mp1 += 2;
            else if (s.getTeam1Punkte() < s.getTeam2Punkte()) mp2 += 2;
            else { mp1++; mp2++; }
        }
        return mp1 >= 6 || mp2 >= 6 || sets.size() >= MAX_SETS;
    }

    /**
     * Convert Satz-Ergebnisse to total match points per team
     */
    private List<TeamMatchInfoDO> buildTeamMatchInfo(
            List<SatzErgebnisDO> sets, long own, long opp) {
        int ownMp=0, oppMp=0;
        for (SatzErgebnisDO s : sets) {
            if (s.getTeam1Punkte() > s.getTeam2Punkte()) ownMp += 2;
            else if (s.getTeam1Punkte() < s.getTeam2Punkte()) oppMp += 2;
            else { ownMp++; oppMp++; }
        }
        return Arrays.asList(
                new TeamMatchInfoDO(own, getTeamName(own), ownMp),
                new TeamMatchInfoDO(opp, getTeamName(opp), oppMp)
        );
    }

    /**
     * Helper to fetch team name via DsbMannschaft and Verein
     */
    private TeamInfoDO getTeamInfo(long teamId) {
        DsbMannschaftDO md = mannschaftComponent.findById(teamId);
        VereinDO v = vereinComponent.findById(md.getVereinId());
        String name = v.getName() + (md.getNummer() > 1 ? " " + md.getNummer() : "");
        return new TeamInfoDO(teamId, name);
    }

    /**
     * Advance to next match or end of day state
     */
    private void advanceToNextMatchOrEnd(long wettkampfId, TabletSessionEntity session) {
        // Find all team matches ordered
        List<MatchDO> teamMatches = matchComponent.findByWettkampfId(wettkampfId).stream()
                .filter(m -> m.getMannschaftId() == session.getTeamId())
                .sorted(Comparator.comparingLong(MatchDO::getNr))
                .collect(Collectors.toList());
        int idx = teamMatches.indexOf(
                teamMatches.stream() .filter(m -> m.getId() == session.getCurrentMatchId()).findFirst().get());
        if (idx + 1 < teamMatches.size()) {
            MatchDO next = teamMatches.get(idx + 1);
            session.setCurrentMatchId(next.getId());
            session.setCurrentPasseNumber(1);
            session.setStatus(STATUS_SCHUETZENMELDUNG);
        } else {
            session.setStatus(STATUS_WETTKAMPF_ENDE);
        }
        sessionDAO.updateStatus(session, -1L);
    }
}
