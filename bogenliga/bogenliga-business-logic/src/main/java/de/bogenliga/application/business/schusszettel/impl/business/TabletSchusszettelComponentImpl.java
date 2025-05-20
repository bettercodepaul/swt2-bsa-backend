package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.business.schusszettel.api.types.inside.*;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
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
import java.security.SecureRandom;
import java.util.Base64;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Core implementation of the Tablet Schusszettel workflow.
 * Use Case Overview:
 * 1) GET /api/tablet-schusszettel?token=...&wettkampfid=...&teamid=...
 *    - Liefert aktuellen Status (NOT_ALLOWED, SCHUETZENMELDUNG, SATZEINGABE, WARTE, WETTKAMPF_ENDE)
 *    - Abhängig vom Zustand werden sämtliche Daten für alle Sätze zurückgegeben,
 *      inkl. Schützenlisten, Satz-Ergebnisse, Match-Ergebnisse, verfügbare Schützen.
 * 2) POST /api/tablet-schusszettel?token=...
 *    - Typ SCHUETZENMELDUNG: gemeldete_schuetzen -> Registrierung
 *    - Typ SATZEINGABE: satzeingabe -> Schussdaten erfassen
 *    - Automatische Statuswechsel nach Regeln in advanceToNextMatchOrEnd() und im GET-Handler.
 * State Machine:
 * NOT_ALLOWED -> (ungültiger Token) remains NOT_ALLOWED
 * SCHUETZENMELDUNG -> (nach POST Registrierung) SATZEINGABE
 * SATZEINGABE -> (nach POST volle Sätze) WARTE
 * WARTE -> (Gegner auch WARTE & Match beendet) SCHUETZENMELDUNG oder WETTKAMPF_ENDE
 * WARTE -> (Gegner nicht fertig) SATZEINGABE
 * WETTKAMPF_ENDE -> Endzustand
 * @author Marty Lauterbach
 */
@Service
public class TabletSchusszettelComponentImpl implements TabletSchusszettelComponent {

    // Constants controlling match logic
    private static final int MAX_SETS = 5;                     // Maximum number of sets per match
    private static final int SHOOTERS_PER_TEAM = 3;            // Exactly three shooters per set
    private static final int ARROWS_PER_SHOOTER = 2;

    // State identifiers for session
    private static final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";
    private static final String STATUS_SATZEINGABE      = "SATZEINGABE";
    private static final String STATUS_WARTE           = "WARTE";
    private static final String STATUS_WETTKAMPF_ENDE  = "WETTKAMPF_ENDE";

    // Injected business components / DAOs
    private final TabletSchusszettelDAO            sessionDAO;
    private final PasseComponent              passeComponent;
    private final MatchComponent              matchComponent;
    private final MannschaftsmitgliedComponent mmComponent;
    private final DsbMitgliedComponent        mitgliedComponent;
    private final DsbMannschaftComponent      mannschaftComponent;
    private final VereinComponent             vereinComponent;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Autowired
    public TabletSchusszettelComponentImpl(
            TabletSchusszettelDAO sessionDAO,
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
     * GET-Handler: Tablet Status abfragen
     * 1) Token prüfen -> NOT_ALLOWED oder Session laden
     * 2) Basis DTO befüllen (eigene & gegnerische TeamInfo)
     * 3) Je nach Session-Status spezifische Handler aufrufen
     *    - SCHUETZENMELDUNG: verfügbare Schützen
     *    - SATZEINGABE: bereits gemeldete Schützen + verbleibende
     *    - WARTE: ggf. Statuswechsel, sonst Ansicht wie SATZEINGABE
     *    - WETTKAMPF_ENDE: Finale Zusammenfassung
     * @author Marty Lauterbach
     */
    @Override
    public TabletSchusszettelDO getStatus(long wettkampfId, long teamId, String token) {
        // 1) Token presence / emptiness check
        if (token == null || token.isEmpty()) {
            return notAllowed();
        }
        // 2) Lookup session, return NOT_ALLOWED if none
        TabletSchusszettelEntity session = sessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token)
                .orElse(null);
        if (session == null) {
            return notAllowed();
        }

        // ----------

        // Convert session.getStatus() to enum, or fail with BusinessException
        final TabletSchusszettelDO.TabletSchusszettelStatus statusEnum;
        try {
            statusEnum = TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(session.getStatus());
        } catch (IllegalArgumentException iae) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Unknown session status: " + session.getStatus());
        }

        // ----------

        // Extract match and opponent IDs
        long matchId = session.getCurrentMatchId();
        long oppTeam = session.getGegnerTeamId();

        // Fetch all recorded passes for this match
        List<PasseDO> passen = passeComponent.findByMatchId(matchId);

        // Build full set history (Satz-Ergebnisse)
        List<SatzErgebnisDO> satzHistory = buildSatzErgebnisse(passen, teamId, oppTeam, Integer.MAX_VALUE);

        // Populate base DTO
        TabletSchusszettelDO result = new TabletSchusszettelDO();

        result.setStatus(statusEnum);
        result.setEigenesTeam(getTeamInfo(teamId));
        result.setGegnerischesTeam(getTeamInfo(oppTeam));
        result.setSatzErgebnisse(satzHistory);
        result.setSchuetzenMatchPunkte(buildMatchPunkte(passen, teamId));
        result.setMatchErgebnis(buildTeamMatchInfo(satzHistory, teamId, oppTeam));

        // Call to TabletSessionDAO | Marty: Unnecessary call from a get class that infringes into setting database entry status - Why?
        // TabletSessionDAO.setCurrentMatchId(wettkampfId, teamId, matchId);
        // TabletSessionDAO.setCurrentPasseNumber(wettkampfId, teamId, session.getCurrentPasseNumber());

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
     * Hilfsmethode für NOT_ALLOWED
     */
    private TabletSchusszettelDO notAllowed() {
        TabletSchusszettelDO dto = new TabletSchusszettelDO();
        dto.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED);
        return dto;
    }

    /**
     * POST-Handler: Schützenmeldung
     * - Prüfe genau 3 Schützen
     * - Mitgliedschaft validieren
     * - Reservation durch leere Passen erzeugen
     * - Statuswechsel -> SATZEINGABE
     * @author Marty Lauterbach
     */
    @Override
    public void submitSchuetzen(long wettkampfId,
                                long teamId,
                                String token,
                                SchuetzenMeldungDO input) {

        // 1) Token presence / emptiness check
        if (token == null || token.isEmpty()) {
            throw new BusinessException(
                    ErrorCode.NO_PERMISSION_ERROR,
                    "Token argument is empty");
        }

        // 2) Lookup session, return NOT_ALLOWED if none
        TabletSchusszettelEntity session = sessionDAO
                .findByTokenWettkampfUndTeam(wettkampfId, teamId, token)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.NO_PERMISSION_ERROR,
                                "Invalid or expired token"));

        // 3) Validate payload: exactly 3 shooters
        if (input == null
                || input.getGemeldeteSchuetzen() == null
                || input.getGemeldeteSchuetzen().size() != SHOOTERS_PER_TEAM) {

            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Exactly " + SHOOTERS_PER_TEAM + " shooters must be selected");
        }

        // 4) For each shooter: check membership, then reserve 5 empty passes
        final int PASSES_PER_SHOOTER = 5;
        for (Long dsbId : input.getGemeldeteSchuetzen()) {

            // 4a) Membership check via component
            if (mmComponent.findByMemberAndTeamId(teamId, dsbId) == null) {
                throw new BusinessException(
                        ErrorCode.NO_PERMISSION_ERROR,
                        "Shooter " + dsbId + " is not on team " + teamId);
            }

            // 4b) Reserve empty Passen (lfdnr = 1..5) for this shooter
            for (long lfdnr = 1; lfdnr <= PASSES_PER_SHOOTER; lfdnr++) {
                PasseDO passe = new PasseDO(
                        /* id:            */ null,                   // let the DB generate it
                        /* teamId:        */ teamId,                 // passeMannschaftId
                        /* wettkampfId:   */ wettkampfId,            // passeWettkampfId
                        /* matchNr:       */ session.getCurrentMatchNumber(),
                        /* matchId:       */ session.getCurrentMatchId(),
                        /* lfdnr:         */ lfdnr,                  // this pass’s sequence # (1–5)
                        /* dsbMitgliedId: */ dsbId,                  // which shooter
                        /* pfeil1–6:      */ null, null, null, null, null, null
                );
                passeComponent.create(passe, /*userId*/ -1L);
            }
        }

        // 5) Switch session into Satz-Eingabe mode
        session.setStatus(STATUS_SATZEINGABE);
        sessionDAO.updateStatus(session, /*userId*/ -1L);
    }

    /**
     * POST-Handler: Satzeingabe
     * - Schussdaten speichern
     * - Prüfen, ob beide Teams fertig -> ggf. Satz-, Match- oder Tag beenden
     * @author Marty Lauterbach
     */
    @Override
    public void submitSatz(long wettkampfId, long teamId, String token, SatzEingabeDO eingabe) {
        // 1) Token presence / emptiness check
        if (token == null || token.isEmpty()) {
            throw new BusinessException(
                    ErrorCode.NO_PERMISSION_ERROR,
                    "Token argument is empty");
        }
        // 2) Lookup session, return NOT_ALLOWED if none
        TabletSchusszettelEntity session = sessionDAO.findByTokenWettkampfUndTeam(wettkampfId, teamId, token)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.NO_PERMISSION_ERROR,
                        "Invalid or expired token"));

        // 3) Validate payload: exactly SHOOTERS_PER_TEAM shooters
        if (eingabe == null
                || eingabe.getSatzeingabe() == null
                || eingabe.getSatzeingabe().size() != SHOOTERS_PER_TEAM) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Exactly " + SHOOTERS_PER_TEAM + " shooters’ scores required");
        }

        // 4) Extract match and opponent IDs
        long matchId   = session.getCurrentMatchId();
        int  passeNr   = session.getCurrentPasseNumber();

        // 5) Persist each shooter’s arrows, using ARROWS_PER_SHOOTER
        for (SchuetzenSatzDO satz : eingabe.getSatzeingabe()) {
            try {
                Optional<PasseDO> existing = passeComponent.findByMatchId(matchId).stream()
                        .filter(p ->
                                p.getPasseMannschaftId() == teamId &&
                                        p.getPasseLfdnr()        == passeNr &&
                                        Objects.equals(p.getPasseDsbMitgliedId(), satz.getSchuetzenId())
                        ).findFirst();

                if (existing.isPresent()) {
                    // update only as many arrows as ARROWS_PER_SHOOTER
                    PasseDO passe = existing.get();
                    if (ARROWS_PER_SHOOTER >= 1) passe.setPfeil1(satz.getSchuss1());
                    if (ARROWS_PER_SHOOTER >= 2) passe.setPfeil2(satz.getSchuss2());
                    if (ARROWS_PER_SHOOTER >= 3) passe.setPfeil3(satz.getSchuss3());
                    // TODO PasseDO is missing pfeil4-6, even though the database entry has 6?
                    passeComponent.update(passe, -1L);

                } else {
                    // create new PasseDO, filling exactly ARROWS_PER_SHOOTER slots
                    PasseDO passe = new PasseDO(
                            null,                   // id (generated)
                            teamId,                 // passeMannschaftId
                            wettkampfId,            // passeWettkampfId
                            session.getCurrentMatchNumber(),
                            matchId,
                            (long) passeNr,
                            satz.getSchuetzenId(),
                            (ARROWS_PER_SHOOTER >= 1 ? satz.getSchuss1() : null),
                            (ARROWS_PER_SHOOTER >= 2 ? satz.getSchuss2() : null),
                            (ARROWS_PER_SHOOTER >= 3 ? satz.getSchuss3() : null),
                            null, null, null // TODO PASSEDO doesnt have pfeil4-6
                    );
                    passeComponent.create(passe, -1L);
                }
            } catch (Exception e) {
                throw new TechnicalException(
                        ErrorCode.INTERNAL_ERROR,
                        "Fehler beim Speichern der Passe für Schütze " + satz.getSchuetzenId() + ": " + e.getMessage());
            }
        }

        // 6) Check opponent’s state
        final Optional<TabletSchusszettelEntity> oppOpt =
                sessionDAO.findByWettkampfUndTeam(
                        wettkampfId,
                        session.getGegnerTeamId());

        if (!oppOpt.isPresent() || !STATUS_WARTE.equals(oppOpt.get().getStatus())) {
            // Opponent not yet done → go into WAIT
            session.setStatus(STATUS_WARTE);
            sessionDAO.updateStatus(session, -1L);

        } else {
            // Both sides done with this end → recompute full history
            List<SatzErgebnisDO> fullHistory = buildSatzErgebnisse(
                    passeComponent.findByMatchId(matchId),
                    teamId,
                    session.getGegnerTeamId(),
                    passeNr
            );

            if (isMatchComplete(fullHistory)) {
                // match over → advance both teams
                advanceToNextMatchOrEnd(wettkampfId, session);
                advanceToNextMatchOrEnd(wettkampfId, oppOpt.get());

            } else {
                // match still on → advance both to next end
                session.setCurrentPasseNumber(passeNr + 1);
                session.setStatus(STATUS_SATZEINGABE);
                sessionDAO.updateStatus(session, -1L);

                TabletSchusszettelEntity oppSession = oppOpt.get();
                oppSession.setCurrentPasseNumber(passeNr + 1);
                oppSession.setStatus(STATUS_SATZEINGABE);
                sessionDAO.updateStatus(oppSession, -1L);
            }
        }
    }


    /**
     * Erzeuge Sessions für alle Teams des Wettkampfs
     * @author Marty Lauterbach
     */
    @Override
    public void initializeForWettkampf(long wettkampfId) {
        try {
            // 1) Lösche alte Sessions
            sessionDAO.deleteByWettkampfId(wettkampfId);

            // 2) Lade alle Matches
            List<MatchDO> allMatches = matchComponent.findByWettkampfId(wettkampfId);
            if (allMatches.isEmpty()) {
                throw new BusinessException(
                        ErrorCode.ENTITY_NOT_FOUND_ERROR,
                        "No matches found for wettkampf " + wettkampfId);
            }

            // Wenn irgendein Match keine MannschaftId hat, werfen wir eine BusinessException
            for (MatchDO m : allMatches) {
                if (m.getMannschaftId() == null) {
                    throw new BusinessException(
                            ErrorCode.ENTITY_NOT_FOUND_ERROR,
                            "Invalid match data (missing team) for wettkampf " + wettkampfId);
                }
            }

            // 3) Einmalige Team-IDs extrahieren
            Set<Long> teamIds = allMatches.stream()
                    .map(MatchDO::getMannschaftId)
                    .collect(Collectors.toSet());

            // 4) Für jedes Team die erste Begegnung anlegen
            for (Long teamId : teamIds) {
                List<MatchDO> teamMatches = allMatches.stream()
                        .filter(m -> Objects.equals(m.getMannschaftId(), teamId))
                        .sorted(Comparator.comparingLong(MatchDO::getNr))
                        .toList();

                if (teamMatches.isEmpty()) {
                    throw new BusinessException(
                            ErrorCode.ENTITY_NOT_FOUND_ERROR,
                            "No matches for team " + teamId);
                }

                MatchDO firstMatch = teamMatches.get(0);

                long opponentId = findOpponentTeamId(firstMatch, teamId);

                TabletSchusszettelEntity session = new TabletSchusszettelEntity();

                session.setWettkampfId(wettkampfId);
                session.setTeamId(teamId);
                session.setCurrentMatchId(firstMatch.getId());
                session.setCurrentMatchNumber(Math.toIntExact(firstMatch.getNr()));
                session.setCurrentPasseNumber(1);
                session.setStatus(STATUS_SCHUETZENMELDUNG);
                session.setGegnerTeamId(opponentId);

                session.setToken(generateUrlSafeToken());

                sessionDAO.createSession(session, -1L);
            }

        } catch (BusinessException be) {
            // Test‐fälle, die ausdrücklich eine BusinessException erwarten, greifen hier
            throw be;
        } catch (Exception e) {
            // Alle anderen Fehler werden als TechnicalException weitergereicht
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "initializeForWettkampf failed for wettkampf "
                            + wettkampfId + ": " + e.getMessage());
        }
    }

    /**
     * 16-byte URL-safe token, no padding
     * @author Marty Lauterbach
     */
    private String generateUrlSafeToken() {
        byte[] buf = new byte[16];
        SECURE_RANDOM.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }

    @Override
    public void deleteForWettkampf(long wettkampfId) {
        try {
            sessionDAO.deleteByWettkampfId(wettkampfId);
        } catch (BusinessException be) {
            // let business errors bubble
            throw be;
        } catch (Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "deleteForWettkampf failed for wettkampf " + wettkampfId + ": " + e.getMessage());
        }
    }

    @Override
    public boolean existsForWettkampf(long wettkampfId) {
        try {
            return sessionDAO.existsByWettkampfId(wettkampfId);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "existsForWettkampf failed for wettkampf " + wettkampfId + ": " + e.getMessage());
        }
    }

    /**
     * Re-tokenizes the schusszettel for a given wettkampf and team.
     *
     * @param wettkampfId : Wettkampf ID
     * @param teamId : Team ID
     */
    @Override
    public void reTokenize(long wettkampfId, long teamId) {
        try {
            // 1) Lookup session, return NOT AVALIABLE if none
            TabletSchusszettelEntity session = sessionDAO
                    .findByWettkampfUndTeam(wettkampfId, teamId)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.NO_PERMISSION_ERROR, "Invalid"));
            // 2) Generate new token
            String newToken = generateUrlSafeToken();
            session.setToken(newToken);
            // 3) Update session with new token
            sessionDAO.setToken(wettkampfId, teamId, newToken, -1L);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new TechnicalException(
                    ErrorCode.INTERNAL_ERROR,
                    "reTokenize failed for wettkampf " + wettkampfId
                            + ", team " + teamId + ": " + e.getMessage());
        }
    }

    /**
     * Generates a TabletSessionInfoDO for a given wettkampfId. - WettkampfId Per Team: - TeamId - TeamName - Status -
     * Token
     *
     * @param wettkampfId : Wettkampf ID
     */
    @Override
    public TabletSessionInfoDO generateSchusszettelSessions(long wettkampfId) {

        // 1) Lookup session, return NOT AVALIABLE if none
        List<TabletSchusszettelEntity> sessions = sessionDAO.findByWettkampfId(wettkampfId);


        return null;
    }

    //================================================================================
    // Private helper methods
    //================================================================================

    /**
     * SCHUETZENMELDUNG: verfügbar machen aller Vereins-Schützen
     */
    private void handleSchuetzenmeldung(TabletSchusszettelEntity session,
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
     * SATZEINGABE: bereits registrierte Schützen + verbleibende zum Auswählen
     */
    private void handleSatzeingabe(TabletSchusszettelEntity session, TabletSchusszettelDO out) {
        long passeNr = session.getCurrentPasseNumber();
        long matchId = session.getCurrentMatchId();

        List<PasseDO> assigns = passeComponent.findByMatchId(matchId).stream()
                .filter(p -> p.getPasseLfdnr() == passeNr)
                .toList();

        // Stammdaten der gemeldeten Schützen
        List<SchuetzeStammdatenDO> meta = assigns.stream().map(p -> {
            DsbMitgliedDO dm = mitgliedComponent.findById(p.getPasseDsbMitgliedId());
            MannschaftsmitgliedDO mm = mmComponent.findByMemberAndTeamId(session.getTeamId(), p.getPasseDsbMitgliedId());
            return new SchuetzeStammdatenDO(dm.getId(), Math.toIntExact(mm.getRueckennummer()), dm.getVorname(), dm.getNachname());
        }).collect(Collectors.toList());
        out.setSchuetzeStammDaten(meta);

        // Verbleibende Schützen
        Set<Long> used = assigns.stream()
                .map(PasseDO::getPasseDsbMitgliedId).collect(Collectors.toSet());
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
     * WARTE: Gegner-Status prüfen -> ggf. neue Phase starten oder Eingabeansicht erneut
     */
    private void handleWarte(TabletSchusszettelEntity session,
                             TabletSchusszettelDO out,
                             long wettkampfId,
                             long teamId) {

        // In this status, any logic should not change the database, since even with parallelization, the
        // temporary block on the read on the rows during submitSatz should cause the teams to never
        // both think the other team is not yet mistakenly also in WAIT ; I could be wrong though, since I am
        // not sure the SQL dialect actually includes read-blocks on uncommitted changes | if you ever have a
        // bug, where a team has successfully gone to the next match but the other team is still stuck, then SQL
        // has done the wrong thing and I am right | if you never have that bug, then I am wrong and SQL is right

        // To fix this: add a db call in handleWarte where you check if the saved in callerid row gegnerId is in
        // the next PasseNr or in the next match even (its gegnerID not the callers ID).
        // Then just call nextMatchOrEnde on the caller.

        // Wir lesen noch einmal den aktuellen Status aus der DB,
        // um evtl. bereits vollzogene Wechsel (durch submitSatz) einzufangen:
        final TabletSchusszettelEntity fresh = sessionDAO
                .findByTokenWettkampfUndTeam(
                        session.getWettkampfId(),
                        session.getTeamId(),
                        session.getToken())
                .orElse(session);

        // Wenn der Status inzwischen nicht mehr WARTE ist,
        // bauen wir stattdessen direkt die neue View:
        if (! STATUS_WARTE.equals(fresh.getStatus())) {
            final TabletSchusszettelDO.TabletSchusszettelStatus newStatus =
                    TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(fresh.getStatus());

            out.setStatus(newStatus);

            switch (fresh.getStatus()) {
                case STATUS_SCHUETZENMELDUNG:
                    handleSchuetzenmeldung(fresh, out, teamId);
                    break;
                case STATUS_SATZEINGABE:
                    handleSatzeingabe(fresh, out);
                    break;
                case STATUS_WETTKAMPF_ENDE:
                    handleEnde(fresh, out, wettkampfId, teamId);
                    break;
                default:
                    // sollten wir hier jemals einen neuen status haben, ist das okay
            }
            return;
        }

        // Status ist immer noch WARTE → einfach in SATZEINGABE-DTO werfen (Rest bleibt unverändert)
        out.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.WARTE);
        // just to fill in the DTO with Information for the display if we want to, can be removed if speed is ever
        // an issue
        handleSatzeingabe(session, out);

        /* old logic

        Optional<TabletSchusszettelEntity> oppSession = sessionDAO.findByWettkampfUndTeam(
                wettkampfId, session.getGegnerTeamId());

        boolean opponentWaiting = oppSession.isPresent() && STATUS_WARTE.equals(oppSession.get().getStatus());

        if (opponentWaiting) {

            // Beide Teams fertig -> prüfen Match-Ende
            List<SatzErgebnisDO> full = buildSatzErgebnisse(
                    passeComponent.findByMatchId(session.getCurrentMatchId()),
                    teamId, session.getGegnerTeamId(), Integer.MAX_VALUE);

            if (isMatchComplete(full)) {
                // Match beendet -> nächste Registrierung oder Wettkampf-Ende
                advanceToNextMatchOrEnd(wettkampfId, session);
                out.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(session.getStatus()));
                handleSchuetzenmeldung(session, out, teamId);
            } else {
                // Weiterer Satz -> SATZEINGABE
                session.setCurrentPasseNumber(session.getCurrentPasseNumber() + 1);
                session.setStatus(STATUS_SATZEINGABE);
                sessionDAO.updateStatus(session, -1L);
                out.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);
                handleSatzeingabe(session, out);
            }

        } else {
            // Warten noch -> WARTE WEITER
        }

         */
    }

    /**
     * WETTKAMPF_ENDE: Aufräumen und finale Recaps zeigen
     */
    private void handleEnde(TabletSchusszettelEntity session,
                            TabletSchusszettelDO out,
                            long wettkampfId,
                            long teamId) {

        // clear any per‐set or shooter‐detail data
        out.setSatzErgebnisse(Collections.emptyList());
        out.setSchuetzenMatchPunkte(Collections.emptyList());
        out.setSchuetzeStammDaten(Collections.emptyList());
        out.setVerfuegbareSchuetzen(Collections.emptyList());

        // Recap aller Matches des Tages, skipping any without a valid opponent
        List<TeamMatchInfoDO> recap = new ArrayList<>();
        for (MatchDO m : matchComponent.findByWettkampfId(wettkampfId)) {
            if (m.getMannschaftId() != teamId) {
                continue;
            }
            try {
                long opp = findOpponentTeamId(m, teamId);
                List<PasseDO> ps = passeComponent.findByMatchId(m.getId());
                List<SatzErgebnisDO> sets =
                        buildSatzErgebnisse(ps, teamId, opp, Integer.MAX_VALUE);
                recap.addAll(buildTeamMatchInfo(sets, teamId, opp));
            } catch (BusinessException be) {
                // Keine gültige Gegner-Session gefunden → match überspringen
            }
        }
        out.setMatchErgebnis(recap);
    }

    /**
     * Find opponent team ID using match round & pairing logic
     */
    private long findOpponentTeamId(MatchDO m, long own) {
        return matchComponent.findByWettkampfId(m.getWettkampfId()).stream()
                .filter(o -> Objects.equals(o.getNr(), m.getNr())
                        && Objects.equals(o.getBegegnung(), m.getBegegnung())
                        && !Objects.equals(o.getMannschaftId(), own))
                .findFirst()
                .map(MatchDO::getMannschaftId)
                .orElseThrow(() -> new BusinessException(
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
                        Collectors.summingInt(p -> {
                            int a = p.getPfeil1() != null ? p.getPfeil1() : 0;
                            int b = p.getPfeil2() != null ? p.getPfeil2() : 0;
                            int c = p.getPfeil3() != null ? p.getPfeil3() : 0;
                            return a + b + c;
                        })))
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
                    int set = Math.toIntExact(e.getKey());

                    // safe sum for team1
                    int sum1 = e.getValue().stream()
                            .filter(p -> p.getPasseMannschaftId() == t1)
                            .mapToInt(p ->
                                    (p.getPfeil1() != null ? p.getPfeil1() : 0) +
                                            (p.getPfeil2() != null ? p.getPfeil2() : 0) +
                                            (p.getPfeil3() != null ? p.getPfeil3() : 0)
                            ).sum();

                    // safe sum for team2
                    int sum2 = e.getValue().stream()
                            .filter(p -> p.getPasseMannschaftId() == t2)
                            .mapToInt(p ->
                                    (p.getPfeil1() != null ? p.getPfeil1() : 0) +
                                            (p.getPfeil2() != null ? p.getPfeil2() : 0) +
                                            (p.getPfeil3() != null ? p.getPfeil3() : 0)
                            ).sum();

                    return new SatzErgebnisDO(set, sum1, sum2);
                })
                .collect(Collectors.toList());
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


    private String getTeamName(long teamId) {
        // 1) Lade die Mannschaft, um an die vereins-ID zu kommen
        DsbMannschaftDO md = mannschaftComponent.findById(teamId);
        // 2) Nutze die wirkliche vereins-ID
        VereinDO v = vereinComponent.findById(md.getVereinId());
        return v.getName();
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
    private void advanceToNextMatchOrEnd(long wettkampfId,
                                         TabletSchusszettelEntity session) {
        // 1) load all this team’s matches, sorted by Nr
        List<MatchDO> teamMatches = matchComponent.findByWettkampfId(wettkampfId).stream()
                .filter(m -> Objects.equals(m.getMannschaftId(), session.getTeamId()))
                .sorted(Comparator.comparingLong(MatchDO::getNr))
                .toList();

        // 2) find index of the current match
        OptionalInt currentIdx = IntStream.range(0, teamMatches.size())
                .filter(i -> Objects.equals(teamMatches.get(i).getId(),
                        session.getCurrentMatchId()))
                .findFirst();

        if (!currentIdx.isPresent()) {
            throw new BusinessException(
                    ErrorCode.INTERNAL_ERROR,
                    "Current match not found for team " + session.getTeamId()
                            + " in wettkampf " + wettkampfId);
        }

        int idx = currentIdx.getAsInt();
        if (idx + 1 < teamMatches.size()) {
            // advance to the next match
            MatchDO next = teamMatches.get(idx + 1);
            session.setCurrentMatchId(next.getId());
            session.setCurrentMatchNumber(Math.toIntExact(next.getNr()));
            session.setCurrentPasseNumber(1);

            // recompute opponent
            final MatchDO fullNext = matchComponent.findById(next.getId());
            session.setGegnerTeamId(findOpponentTeamId(fullNext, session.getTeamId()));

            session.setStatus(STATUS_SCHUETZENMELDUNG);
        } else {
            // no more matches → end of day
            session.setStatus(STATUS_WETTKAMPF_ENDE);
        }

        sessionDAO.updateStatus(session, -1L);
    }
}
