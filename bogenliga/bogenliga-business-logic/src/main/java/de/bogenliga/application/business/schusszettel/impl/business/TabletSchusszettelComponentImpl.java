package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.passe.impl.dao.PasseDAO;
import de.bogenliga.application.business.passe.impl.entity.PasseBE;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeMatchPunkteDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.VerfuegbarerSchuetzeDO;
import de.bogenliga.application.business.schusszettel.impl.dao.MatchShooterDAO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSessionDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSessionEntity;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAOext;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
public class TabletSchusszettelComponentImpl implements TabletSchusszettelComponent {

    private static final int MAX_SETS = 5;
    private static final int SHOOTERS_PER_TEAM = 3;

    private static final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";
    private static final String STATUS_SATZEINGABE      = "SATZEINGABE";
    private static final String STATUS_WARTE           = "WARTE";
    private static final String STATUS_WETTKAMPF_ENDE  = "WETTKAMPF_ENDE";

    private final TabletSessionDAO sessionDAO;
    private final PasseDAO passeDAO;
    private final MatchDAO matchDAO;
    private final MannschaftsmitgliedDAO mmDAO;
    private final MatchShooterDAO mzDAO;
    private final DsbMitgliedDAO mitgliedDAO;
    private final DsbMannschaftDAOext mannschaftDAO;
    private final MatchShooterDAO matchShooterDAO;

    public class MatchShooter {
        private long dsbMitgliedId;
        private int  rueckennummer;

        public MatchShooter(long dsbMitgliedId, int rueckennummer) {
            this.dsbMitgliedId = dsbMitgliedId;
            this.rueckennummer = rueckennummer;
        }

        public long getDsbMitgliedId() {
            return dsbMitgliedId;
        }

        public int getRueckennummer() {
            return rueckennummer;
        }
    }

    @Autowired
    public TabletSchusszettelComponentImpl(
            TabletSessionDAO sessionDAO,
            PasseDAO passeDAO,
            MatchDAO matchDAO,
            MannschaftsmitgliedDAO mmDAO,
            MatchShooterDAO mzDAO,
            DsbMitgliedDAO mitgliedDAO,
            DsbMannschaftDAOext mannschaftDAO,
            MatchShooterDAO matchShooterDAO) {
        this.sessionDAO   = sessionDAO;
        this.passeDAO     = passeDAO;
        this.matchDAO     = matchDAO;
        this.mmDAO        = mmDAO;
        this.mzDAO        = mzDAO;
        this.mitgliedDAO  = mitgliedDAO;
        this.mannschaftDAO= mannschaftDAO;
        this.matchShooterDAO = matchShooterDAO;
    }

    @Override
    public TabletSchusszettelDO getStatus(long wettkampfId, long teamId, String token) {
        // Token & session validation
        if (token == null || token.isEmpty()) {
            return notAllowedResponse();
        }
        Optional<TabletSessionEntity> optSession = sessionDAO.findByToken(wettkampfId, teamId, token);
        if (optSession.isEmpty()) {
            return notAllowedResponse();
        }
        TabletSessionEntity session = optSession.get();

        // Prepare response
        TabletSchusszettelDO out = new TabletSchusszettelDO();
        out.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(session.getStatus()));
        out.setEigenesTeam(getTeamInfo(teamId));
        out.setGegnerischesTeam(getTeamInfo(session.getGegnerTeamId()));
        out.setCurrentMatchId(session.getCurrentMatchId());
        out.setCurrentPasseNumber(session.getCurrentPasseNumber());

        long matchId   = session.getCurrentMatchId();
        long oppTeamId = session.getGegnerTeamId();
        // Always return full Satz-Ergebnisse regardless of current passe
        List<PasseBE> allPassen = passeDAO.findByMatchId(matchId);
        List<SatzErgebnisDO> satzErg = buildSatzErgebnisse(allPassen, teamId, oppTeamId, Integer.MAX_VALUE);
        out.setSatzErgebnisse(satzErg);
        out.setSchuetzenMatchPunkte(buildMatchPunkte(allPassen, teamId));
        out.setMatchErgebnis(buildTeamMatchInfo(satzErg, teamId, oppTeamId));

        // Handle specific states
        switch (session.getStatus()) {
            case STATUS_SCHUETZENMELDUNG:
                handleSchuetzenmeldung(session, out, teamId);
                break;
            case STATUS_SATZEINGABE:
                handleSatzeingabe(session, out);
                break;
            case STATUS_WARTE:
                handleWarteState(wettkampfId, session, out);
                break;
            case STATUS_WETTKAMPF_ENDE:
                handleEnde(session, out, wettkampfId, teamId);
                break;
            default:
                // no-op
        }

        return out;
    }

    private TabletSchusszettelDO notAllowedResponse() {
        TabletSchusszettelDO out = new TabletSchusszettelDO();
        out.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED);
        return out;
    }

    @Override
    public void submitSchuetzen(long wettkampfId, long teamId, String token, SchuetzenMeldungDO input) {
        if (input == null || input.getGemeldeteSchuetzen() == null || input.getGemeldeteSchuetzen().size() != SHOOTERS_PER_TEAM) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Exactly 3 shooters must be selected");
        }
        TabletSessionEntity session = sessionDAO.findByToken(wettkampfId, teamId, token)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.NO_PERMISSION_ERROR,
                        "Invalid token or session expired"));

        // Validate and assign shooters
        input.getGemeldeteSchuetzen().forEach(dsbId -> {
            if (!mmDAO.isMemberOfTeam(dsbId, teamId)) {
                throw new BusinessException(
                        ErrorCode.NO_PERMISSION_ERROR,
                        "Shooter " + dsbId + " is not a member of team " + teamId);
            }
            mzDAO.assignToMatch(session.getCurrentMatchId(), dsbId);
        });

        // Move to shot entry
        session.setStatus(STATUS_SATZEINGABE);
        session.setCurrentPasseNumber(1);
        sessionDAO.updateStatus(session, -1L);
    }

    @Override
    public void submitSatz(long wettkampfId, long teamId, String token, SatzEingabeDO eingabe) {
        if (eingabe == null || eingabe.getSatzeingabe() == null) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Satz input cannot be null");
        }
        if (eingabe.getSatzeingabe().size() != SHOOTERS_PER_TEAM) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "Exactly 3 shooter entries are required");
        }
        TabletSessionEntity session = sessionDAO.findByToken(wettkampfId, teamId, token)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.NO_PERMISSION_ERROR,
                        "Invalid token or session expired"));

        long matchId    = session.getCurrentMatchId();
        int  currentSet = session.getCurrentPasseNumber();

        // Persist shot data
        eingabe.getSatzeingabe().forEach(s ->
            matchShooterDAO.insertPasse(matchId, teamId, currentSet,
                                 s.getSchuetzenId(), s.getSchuss1(), s.getSchuss2(), s.getSchuss3()));

        // Check completion status
        long ownCount = passeDAO.countByMatchAndTeam(matchId, teamId, currentSet);
        long oppCount = passeDAO.countByMatchAndTeam(matchId, session.getGegnerTeamId(), currentSet);
        boolean bothDone = (ownCount == SHOOTERS_PER_TEAM) && (oppCount == SHOOTERS_PER_TEAM);

        if (bothDone) {
            List<SatzErgebnisDO> full = buildSatzErgebnisse(
                    passeDAO.findByMatchId(matchId), teamId, session.getGegnerTeamId(), Integer.MAX_VALUE);
            if (isMatchComplete(full)) {
                advanceToNextMatchOrEnd(wettkampfId, session);
                // In submitSatz there is no outgoing DTO; session changes will be picked up in a subsequent GET.
            } else {
                session.setCurrentPasseNumber(currentSet + 1);
                session.setStatus(STATUS_SATZEINGABE);
                sessionDAO.updateStatus(session, -1L);
            }
        } else {
            session.setStatus(STATUS_WARTE);
            sessionDAO.updateStatus(session, -1L);
        }
    }

    @Override
    public void initializeForWettkampf(long wettkampfId, long teamId) {
        // Initialize a session for the given team
        List<Long> matches = matchDAO.findMatchIdsByWettkampfAndTeam(wettkampfId, teamId);
        if (matches.isEmpty()) {
            throw new BusinessException(
                    ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    "No matches found for team " + teamId + " and competition " + wettkampfId);
        }
        TabletSessionEntity session = new TabletSessionEntity();
        session.setWettkampfId(wettkampfId);
        session.setTeamId(teamId);
        session.setCurrentMatchId(matches.get(0));
        session.setCurrentPasseNumber(1);
        session.setStatus(STATUS_SCHUETZENMELDUNG);
        // Determine opponent from first match
        long opp = matchDAO.findOpponentTeamId(matches.get(0), teamId);
        session.setGegnerTeamId(opp);
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

    /* === Handlers === */
    private void handleSchuetzenmeldung(TabletSessionEntity session,
                                        TabletSchusszettelDO out,
                                        long teamId) {
        var members = mmDAO.findByTeamId(teamId);
        List<VerfuegbarerSchuetzeDO> list = members.stream().map(m -> {
            var be = mitgliedDAO.findById(m.getDsbMitgliedId());
            if (be == null) throw new TechnicalException(
                    ErrorCode.DATABASE_ERROR,
                    "Missing member data for ID " + m.getDsbMitgliedId());
            return new VerfuegbarerSchuetzeDO(
                    be.getDsbMitgliedId(),
                    be.getDsbMitgliedVorname() + " " + be.getDsbMitgliedNachname());
        }).collect(Collectors.toList());
        out.setVerfuegbareSchuetzen(list);
    }

    private void handleSatzeingabe(TabletSessionEntity session,
                                   TabletSchusszettelDO out) {
        long matchId = session.getCurrentMatchId();
        var assigns = mzDAO.findByMatchId(matchId);
        out.setSchuetzenStammDaten(assigns.stream().map(a -> {
            var be = mitgliedDAO.findById(a.getDsbMitgliedId());
            if (be == null) throw new TechnicalException(
                    ErrorCode.DATABASE_ERROR,
                    "Missing shooter data for ID " + a.getDsbMitgliedId());
            return new SchuetzenStammdatenDO(
                    be.getDsbMitgliedId(), a.getRueckennummer(),
                    be.getDsbMitgliedVorname(), be.getDsbMitgliedNachname());
        }).collect(Collectors.toList()));

        // available shooters remaining
        Set<Long> used = assigns.stream().map(a -> a.getDsbMitgliedId()).collect(Collectors.toSet());
        List<VerfuegbarerSchuetzeDO> rest = mmDAO.findByTeamId(session.getTeamId()).stream()
                .map(m -> m.getDsbMitgliedId())
                .filter(id -> !used.contains(id))
                .map(id -> {
                    var be = mitgliedDAO.findById(id);
                    if (be == null) throw new TechnicalException(
                            ErrorCode.DATABASE_ERROR,
                            "Missing shooter data for ID " + id);
                    return new VerfuegbarerSchuetzeDO(
                            id, be.getDsbMitgliedVorname() + " " + be.getDsbMitgliedNachname());
                }).collect(Collectors.toList());
        out.setVerfuegbareSchuetzen(rest);
    }

    private void handleWarteState(long wettkampfId,
                                  TabletSessionEntity session,
                                  TabletSchusszettelDO out) {
        long matchId   = session.getCurrentMatchId();
        long oppTeamId = session.getGegnerTeamId();
        Optional<TabletSessionEntity> opp = sessionDAO.findByWettkampfUndTeam(wettkampfId, oppTeamId);
        if (opp.isPresent() && STATUS_WARTE.equals(opp.get().getStatus())) {
            List<SatzErgebnisDO> full = buildSatzErgebnisse(
                    passeDAO.findByMatchId(matchId), session.getTeamId(), oppTeamId, Integer.MAX_VALUE);
            if (isMatchComplete(full)) {
                advanceToNextMatchOrEnd(wettkampfId, session);
                updateOutWithSession(session, out);
                // Repopulate shooter roster for the new SCHUETZENMELDUNG state
                handleSchuetzenmeldung(session, out, session.getTeamId());
            } else {
                session.setCurrentPasseNumber(session.getCurrentPasseNumber() + 1);
                session.setStatus(STATUS_SATZEINGABE);
                sessionDAO.updateStatus(session, -1L);
                out.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);
                handleSatzeingabe(session, out);
            }
        } else {
            handleSatzeingabe(session, out);
        }
    }

    private void handleEnde(TabletSessionEntity session,
                            TabletSchusszettelDO out,
                            long wettkampfId,
                            long teamId) {
        // Clear fields not applicable at end-of-day
        out.setSatzErgebnisse(Collections.emptyList());
        out.setSchuetzenMatchPunkte(Collections.emptyList());
        out.setSchuetzenStammDaten(Collections.emptyList());
        out.setVerfuegbareSchuetzen(Collections.emptyList());
        // Build recap using dynamic opponent lookup per match
        List<Long> matchIds = matchDAO.findMatchIdsByWettkampfAndTeam(wettkampfId, teamId);
        List<TeamMatchInfoDO> recap = matchIds.stream()
                .map(mid -> {
                    long oppTeam = matchDAO.findOpponentTeamId(mid, teamId);
                    List<PasseBE> passen = passeDAO.findByMatchId(mid);
                    List<SatzErgebnisDO> sets = buildSatzErgebnisse(passen, teamId, oppTeam, Integer.MAX_VALUE);
                    return buildTeamMatchInfo(sets, teamId, oppTeam);
                }).flatMap(Collection::stream).collect(Collectors.toList());
        out.setMatchErgebnis(recap);
        // Reflect session values in DTO
        out.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(session.getStatus()));
        out.setCurrentMatchId(session.getCurrentMatchId());
        out.setCurrentPasseNumber(session.getCurrentPasseNumber());
    }

    // Helper: Update the outgoing DTO based on the current session state.
    private void updateOutWithSession(TabletSessionEntity session, TabletSchusszettelDO out) {
        out.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(session.getStatus()));
        out.setCurrentMatchId(session.getCurrentMatchId());
        out.setCurrentPasseNumber(session.getCurrentPasseNumber());
        long oppTeamId = matchDAO.findOpponentTeamId(session.getCurrentMatchId(), session.getTeamId());
        List<PasseBE> newPassen = passeDAO.findByMatchId(session.getCurrentMatchId());
        List<SatzErgebnisDO> newSatzErg = buildSatzErgebnisse(newPassen, session.getTeamId(), oppTeamId, Integer.MAX_VALUE);
        out.setSatzErgebnisse(newSatzErg);
        out.setSchuetzenMatchPunkte(buildMatchPunkte(newPassen, session.getTeamId()));
        out.setMatchErgebnis(buildTeamMatchInfo(newSatzErg, session.getTeamId(), oppTeamId));
    }

    private boolean isMatchComplete(List<SatzErgebnisDO> satzErg) {
        int mp1 = 0, mp2 = 0;
        for (SatzErgebnisDO s : satzErg) {
            if (s.getTeam1Punkte() > s.getTeam2Punkte()) mp1 += 2;
            else if (s.getTeam1Punkte() < s.getTeam2Punkte()) mp2 += 2;
            else { mp1++; mp2++; }
        }
        return mp1 >= 6 || mp2 >= 6 || satzErg.size() >= MAX_SETS;
    }

    private List<SchuetzeMatchPunkteDO> buildMatchPunkte(List<PasseBE> passen, long teamId) {
        return passen.stream()
                .filter(p -> p.getMannschaftId() == teamId)
                .collect(Collectors.groupingBy(
                        PasseBE::getDsbMitgliedId,
                        Collectors.summingInt(p -> p.getPfeil1() + p.getPfeil2() + p.getPfeil3())))
                .entrySet().stream()
                .map(en -> new SchuetzeMatchPunkteDO(en.getKey(), en.getValue()))
                .collect(Collectors.toList());
    }

    private List<SatzErgebnisDO> buildSatzErgebnisse(
            List<PasseBE> passen,
            long teamA,
            long teamB,
            int uptoSet) {
        return passen.stream()
                .filter(p -> p.getLfdnr() <= uptoSet)
                .collect(Collectors.groupingBy(PasseBE::getLfdnr))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(en -> {
                    int set = en.getKey();
                    int sumA = en.getValue().stream().filter(p -> p.getMannschaftId() == teamA)
                            .mapToInt(p -> p.getPfeil1() + p.getPfeil2() + p.getPfeil3()).sum();
                    int sumB = en.getValue().stream().filter(p -> p.getMannschaftId() == teamB)
                            .mapToInt(p -> p.getPfeil1() + p.getPfeil2() + p.getPfeil3()).sum();
                    return new SatzErgebnisDO(set, sumA, sumB);
                }).collect(Collectors.toList());
    }

    private List<TeamMatchInfoDO> buildTeamMatchInfo(
            List<SatzErgebnisDO> ergebnisse,
            long ownTeam,
            long oppTeam) {
        int mpOwn = 0, mpOpp = 0;
        for (SatzErgebnisDO s : ergebnisse) {
            if (s.getTeam1Punkte() > s.getTeam2Punkte()) {
                mpOwn += 2;
            } else if (s.getTeam1Punkte() < s.getTeam2Punkte()) {
                mpOpp += 2;
            } else {
                mpOwn++; mpOpp++;
            }
        }
        return Arrays.asList(
                new TeamMatchInfoDO(ownTeam, getTeamName(ownTeam), mpOwn),
                new TeamMatchInfoDO(oppTeam, getTeamName(oppTeam), mpOpp)
        );
    }

    private TeamInfoDO getTeamInfo(long teamId) {
        return new TeamInfoDO(teamId, getTeamName(teamId));
    }

    private String getTeamName(long teamId) {
        return mannschaftDAO.findById(teamId)
                .map(be -> be.getName())
                .orElse("<unknown>");
    }

    private void advanceToNextMatchOrEnd(long wettkampfId, TabletSessionEntity session) {
        List<Long> ids = matchDAO.findMatchIdsByWettkampfAndTeam(wettkampfId, session.getTeamId());
        int idx = ids.indexOf(session.getCurrentMatchId());
        if (idx >= 0 && idx + 1 < ids.size()) {
            session.setCurrentMatchId(ids.get(idx + 1));
            session.setStatus(STATUS_SCHUETZENMELDUNG);
            session.setCurrentPasseNumber(1);
        } else {
            session.setStatus(STATUS_WETTKAMPF_ENDE);
        }
        sessionDAO.updateStatus(session, -1L);
    }
}

