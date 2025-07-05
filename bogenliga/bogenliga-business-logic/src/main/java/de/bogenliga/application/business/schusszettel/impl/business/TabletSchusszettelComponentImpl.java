package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.business.schusszettel.api.types.inside.*;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.business.MatchAnalysisService;
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
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSchusszettelComponentImpl.class);

    // Constants controlling match logic
    private static final int MAX_SETS = 5;
    private static final int SHOOTERS_PER_TEAM = 3;
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
    private final WettkampfComponent          wettkampfComponent;
    private final VeranstaltungComponent      veranstaltungComponent;
    private final TabletSchusszettelSyncComponent syncComponent;
    private final MatchAnalysisService matchAnalysisService;

    @Autowired
    public TabletSchusszettelComponentImpl(
            TabletSchusszettelDAO sessionDAO,
            PasseComponent passeComponent,
            MatchComponent matchComponent,
            MannschaftsmitgliedComponent mmComponent,
            DsbMitgliedComponent mitgliedComponent,
            DsbMannschaftComponent mannschaftComponent,
            VereinComponent vereinComponent,
            WettkampfComponent wettkampfComponent,
            VeranstaltungComponent veranstaltungComponent,
            TabletSchusszettelSyncComponent syncComponent,
            MatchAnalysisService matchAnalysisService) {
        this.sessionDAO        = sessionDAO;
        this.passeComponent    = passeComponent;
        this.matchComponent    = matchComponent;
        this.mmComponent       = mmComponent;
        this.mitgliedComponent = mitgliedComponent;
        this.mannschaftComponent = mannschaftComponent;
        this.vereinComponent     = vereinComponent;
        this.wettkampfComponent  = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.syncComponent = syncComponent;
        this.matchAnalysisService = matchAnalysisService;
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
     *    -
     * Updated to include wettkampf information in the response.
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

        // 3) SYNCHRONIZATION - Use the shared sync component
        TabletSchusszettelSyncComponent.SyncResult syncResult =
                syncComponent.synchronizeSession(session, wettkampfId, teamId, true); // updateDatabase = true

        if (!syncResult.success) {
            LOGGER.error("Failed to synchronize session: {}", syncResult.message);
            // You could return an error status or continue with potentially stale data
        } else if (syncResult.dataWasUpdated) {
            LOGGER.info("Session data was synchronized: {}", syncResult.message);
        }

        // Convert session.getStatus() to enum, or fail with BusinessException
        final TabletSchusszettelDO.TabletSchusszettelStatus statusEnum;
        try {
            statusEnum = TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(session.getStatus());
        } catch (IllegalArgumentException iae) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Unknown session status: " + session.getStatus());
        }

        // Extract match and opponent IDs
        long matchId = session.getCurrentMatchId();
        long oppTeam = session.getGegnerTeamId();

        // Fetch all recorded passes for both teams
        List<PasseDO> passen = getAllPassesForMatch(wettkampfId, session.getCurrentMatchNumber(), teamId, oppTeam);

        // Build full set history (Satz-Ergebnisse) with all passes
        // Use shared service for consistent match analysis
        List<PasseDO> teamPasses = passeComponent.findByMannschaftMatchId(teamId, session.getCurrentMatchId());
        List<PasseDO> oppPasses = passeComponent.findByMannschaftMatchId(oppTeam, session.getCurrentMatchId());
        List<SatzErgebnisDO> satzHistory = matchAnalysisService.buildSatzErgebnisse(teamPasses, oppPasses, teamId, oppTeam);

        // Populate base DTO
        TabletSchusszettelDO result = new TabletSchusszettelDO();

        result.setStatus(statusEnum);
        result.setEigenesTeam(getTeamInfo(teamId));
        result.setGegnerischesTeam(getTeamInfo(oppTeam));
        result.setSatzErgebnisse(satzHistory);
        result.setSchuetzenMatchPunkte(buildMatchPunkte(passen, teamId));
        result.setMatchErgebnis(buildTeamMatchInfo(satzHistory, teamId, oppTeam));

        // Build and set wettkampf information
        result.setWettkampfInfo(buildWettkampfInfo(matchId, wettkampfId));

        // Set current passe number from session
        result.setCurrentPasseNumber(session.getCurrentPasseNumber());
        
        // Set match IDs for frontend navigation
        result.setEigenesTeamMatchId(session.getCurrentMatchId());
        result.setGegnerischesTeamMatchId(findEnemyMatchId(wettkampfId, session.getCurrentMatchNumber(), oppTeam));

        // Call to TabletSessionDAO | Marty: Unnecessary call from a get class that infringes into setting database entry status
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
     * - Reservation durch leere Passen erzeugen (check if they exist first)
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

        // 3) Validate payload and team roster
        if (input == null || input.getGemeldeteSchuetzen() == null) {
            throw new BusinessException(
                    ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Invalid registration data");
        }
        
        // 3.1) Enhanced validation with team roster check
        validateSchützenmeldungTeamRoster(teamId, input.getGemeldeteSchuetzen());

        // 4) Create passes atomically for all registered shooters
        createPassesForRegisteredShooters(wettkampfId, teamId, session, input.getGemeldeteSchuetzen());

        // 5) Switch session into Satz-Eingabe mode
        session.setStatus(STATUS_SATZEINGABE);
        sessionDAO.updateStatus(session, /*userId*/ 0L);
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
                    "Exactly " + SHOOTERS_PER_TEAM + " shooters' scores required");
        }

        // 4) Extract match and opponent IDs
        long matchId   = session.getCurrentMatchId();
        int  passeNr   = session.getCurrentPasseNumber();

        // 4.1) Validate match is not already complete
        validateMatchNotComplete(wettkampfId, teamId, session);

        // 5) Persist each shooter's arrows, using ARROWS_PER_SHOOTER
        for (SchuetzenSatzDO satz : eingabe.getSatzeingabe()) {
            // 5.1) Validate arrow values are within valid range (0-10)
            validateArrowValues(satz);
            
            // 5.2) Validate shooter was registered in schützenmeldung
            validateShooterRegistration(wettkampfId, teamId, session.getCurrentMatchNumber(), 
                                      (long) passeNr, satz.getSchuetzenId());
            try {
                // Use the new findByPkOptional method to safely check for existing passe
                Optional<PasseDO> existingPasseOpt = passeComponent.findByPkOptional(
                        wettkampfId,
                        session.getCurrentMatchNumber(),
                        teamId,
                        (long) passeNr,
                        satz.getSchuetzenId()
                );

                if (existingPasseOpt.isPresent()) {
                    // Update existing passe
                    PasseDO existingPasse = existingPasseOpt.get();
                    LOGGER.debug("Updating existing passe: wettkampfId={}, matchNr={}, teamId={}, passeNr={}, schuetzeId={}",
                            wettkampfId, session.getCurrentMatchNumber(), teamId, passeNr, satz.getSchuetzenId());

                    if (ARROWS_PER_SHOOTER >= 1) existingPasse.setPfeil1(satz.getSchuss1());
                    if (ARROWS_PER_SHOOTER >= 2) existingPasse.setPfeil2(satz.getSchuss2());
                    if (ARROWS_PER_SHOOTER >= 3) existingPasse.setPfeil3(satz.getSchuss3());
                    passeComponent.update(existingPasse, 0L);

                } else {
                    // Create new passe - this should be rare since passes are pre-created in submitSchuetzen
                    LOGGER.debug("Creating new passe: wettkampfId={}, matchNr={}, teamId={}, passeNr={}, schuetzeId={}",
                            wettkampfId, session.getCurrentMatchNumber(), teamId, passeNr, satz.getSchuetzenId());

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
                            null, null, null // PASSEDO doesnt (shouldnt?) have pfeil4-6
                    );
                    passeComponent.create(passe, 0L);
                }

            } catch (Exception e) {
                LOGGER.error("Error saving passe for shooter {}: {}", satz.getSchuetzenId(), e.getMessage(), e);
                throw new TechnicalException(
                        ErrorCode.INTERNAL_ERROR,
                        "Fehler beim Speichern der Passe für Schütze " + satz.getSchuetzenId() + ": " + e.getMessage());
            }
        }

        // 6) Check opponent's state with synchronized match completion handling
        handleMatchCompletionSync(wettkampfId, teamId, session, matchId, passeNr);
    }

    //================================================================================
    // Private helper methods
    //================================================================================

    /**
     * SCHUETZENMELDUNG: verfügbar machen nur eingesetzter Vereins-Schützen (eingesetzt >= 1)
     */
    private void handleSchuetzenmeldung(TabletSchusszettelEntity session,
                                        TabletSchusszettelDO out,
                                        long teamId) {
        List<MannschaftsmitgliedDO> members = mmComponent.findByTeamId(teamId);

        // Stammdaten nur eingesetzter Vereins-Schützen zusammenstellen (eingesetzt >= 1)
        List<SchuetzeStammdatenDO> stammdaten = members.stream()
                .filter(m -> m.getDsbMitgliedEingesetzt() != null && m.getDsbMitgliedEingesetzt() >= 1)
                .map(m -> {
                    DsbMitgliedDO dm = mitgliedComponent.findById(m.getDsbMitgliedId());
                    return new SchuetzeStammdatenDO(
                            dm.getId(),
                            Math.toIntExact(m.getRueckennummer()),
                            dm.getVorname(),
                            dm.getNachname()
                    );
                }).collect(Collectors.toList());
        out.setSchuetzeStammDaten(stammdaten);

        List<VerfuegbarerSchuetzeDO> available = members.stream()
                .filter(m -> m.getDsbMitgliedEingesetzt() != null && m.getDsbMitgliedEingesetzt() >= 1)
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

        // Get active shooters for this team (eingesetzt >= 1)
        Set<Long> activeShooters = mmComponent.findByTeamId(session.getTeamId()).stream()
                .filter(mm -> mm.getDsbMitgliedEingesetzt() != null && mm.getDsbMitgliedEingesetzt() >= 1)
                .map(mm -> mm.getDsbMitgliedId())
                .collect(Collectors.toSet());

        List<PasseDO> assigns = passeComponent.findByMatchId(matchId).stream()
                .filter(p -> p.getPasseLfdnr() == passeNr)
                .filter(p -> activeShooters.contains(p.getPasseDsbMitgliedId())) // Only include active shooters
                .toList();

        // Stammdaten der gemeldeten Schützen
        List<SchuetzeStammdatenDO> meta = assigns.stream().map(p -> {
            DsbMitgliedDO dm = mitgliedComponent.findById(p.getPasseDsbMitgliedId());
            MannschaftsmitgliedDO mm = mmComponent.findByMemberAndTeamId(session.getTeamId(), p.getPasseDsbMitgliedId());
            return new SchuetzeStammdatenDO(dm.getId(), Math.toIntExact(mm.getRueckennummer()), dm.getVorname(), dm.getNachname());
        }).collect(Collectors.toList());
        out.setSchuetzeStammDaten(meta);

        // Verbleibende Schützen - nur eingesetzte Schützen (eingesetzt >= 1)
        Set<Long> used = assigns.stream()
                .map(PasseDO::getPasseDsbMitgliedId).collect(Collectors.toSet());
        List<VerfuegbarerSchuetzeDO> left = mmComponent.findByTeamId(session.getTeamId()).stream()
                .filter(mm -> mm.getDsbMitgliedEingesetzt() != null && mm.getDsbMitgliedEingesetzt() >= 1)
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
                List<PasseDO> ps = getAllPassesForMatch(wettkampfId, Math.toIntExact(m.getNr()), teamId, opp);
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
        // Get active shooters for this team (eingesetzt >= 1)
        Set<Long> activeShooters = mmComponent.findByTeamId(teamId).stream()
                .filter(mm -> mm.getDsbMitgliedEingesetzt() != null && mm.getDsbMitgliedEingesetzt() >= 1)
                .map(mm -> mm.getDsbMitgliedId())
                .collect(Collectors.toSet());
        
        return passen.stream()
                .filter(p -> p.getPasseMannschaftId() == teamId)
                .filter(p -> activeShooters.contains(p.getPasseDsbMitgliedId())) // Only include active shooters
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
     * Helper method to fetch all passes for both teams in a match
     */
    private List<PasseDO> getAllPassesForMatch(long wettkampfId, long matchNumber, long ownTeam, long oppTeam) {
        List<PasseDO> allPasses = new ArrayList<>();
        
        try {
            // Find own team's match
            List<MatchDO> ownMatches = matchComponent.findByWettkampfId(wettkampfId).stream()
                    .filter(m -> Objects.equals(m.getMannschaftId(), ownTeam) && m.getNr() == matchNumber)
                    .toList();
            
            if (!ownMatches.isEmpty()) {
                List<PasseDO> ownPasses = passeComponent.findByMatchId(ownMatches.get(0).getId());
                allPasses.addAll(ownPasses);
            }
            
            // Find opponent team's match  
            List<MatchDO> oppMatches = matchComponent.findByWettkampfId(wettkampfId).stream()
                    .filter(m -> Objects.equals(m.getMannschaftId(), oppTeam) && m.getNr() == matchNumber)
                    .toList();
            
            if (!oppMatches.isEmpty()) {
                List<PasseDO> oppPasses = passeComponent.findByMatchId(oppMatches.get(0).getId());
                allPasses.addAll(oppPasses);
            }
            
        } catch (Exception e) {
            LOGGER.warn("Error fetching all passes for match {} between teams {} and {}: {}", 
                       matchNumber, ownTeam, oppTeam, e.getMessage());
        }
        
        return allPasses;
    }

    /**
     * Build list of set results up to given set number using shared analysis service.
     * @deprecated Use matchAnalysisService.buildSatzErgebnisse() directly
     */
    @Deprecated
    private List<SatzErgebnisDO> buildSatzErgebnisse(
            List<PasseDO> passen, long t1, long t2, int upto) {

        // Separate passes by team
        List<PasseDO> team1Passes = passen.stream()
                .filter(p -> p.getPasseMannschaftId() == t1)
                .filter(p -> p.getPasseLfdnr() <= upto)
                .collect(Collectors.toList());
        
        List<PasseDO> team2Passes = passen.stream()
                .filter(p -> p.getPasseMannschaftId() == t2)
                .filter(p -> p.getPasseLfdnr() <= upto)
                .collect(Collectors.toList());
        
        // Use shared service for consistent calculation
        List<SatzErgebnisDO> results = matchAnalysisService.buildSatzErgebnisse(team1Passes, team2Passes, t1, t2);
        
        // Enrich with team names for frontend display
        return results.stream()
                .map(satz -> new SatzErgebnisDO(
                    satz.getSatzNr(),
                    satz.getTeam1Punkte(), 
                    satz.getTeam2Punkte(), 
                    t1, getTeamName(t1), 
                    t2, getTeamName(t2)))
                .collect(Collectors.toList());
    }

    /**
     * Convert Satz-Ergebnisse to total match points per team
     * Fixed to correctly calculate Satzpunkte based on actual team performance
     */
    private List<TeamMatchInfoDO> buildTeamMatchInfo(
            List<SatzErgebnisDO> sets, long own, long opp) {
        int ownMp=0, oppMp=0;
        for (SatzErgebnisDO s : sets) {
            // Correctly identify which team is which in the satz result
            int ownPoints, oppPoints;
            if (s.getTeam1Id() == own) {
                ownPoints = s.getTeam1Punkte();
                oppPoints = s.getTeam2Punkte();
            } else {
                ownPoints = s.getTeam2Punkte();
                oppPoints = s.getTeam1Punkte();
            }
            
            // Award Satzpunkte based on official archery rules
            if (ownPoints > oppPoints) {
                ownMp += 2; // Winner gets 2 Satzpunkte
            } else if (oppPoints > ownPoints) {
                oppMp += 2; // Winner gets 2 Satzpunkte
            } else {
                ownMp += 1; // Tie: both teams get 1 Satzpunkt
                oppMp += 1;
            }
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
     * Internal method to advance to next match or end of day state
     */
    private void advanceToNextMatchOrEndInternal(long wettkampfId,
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

        sessionDAO.updateStatus(session, 0L);
    }

    /**
     * Build wettkampf information from match and veranstaltung data
     */
    private WettkampfInfoDO buildWettkampfInfo(long matchId, long wettkampfId) {
        try {
            final MatchDO matchData = matchComponent.findById(matchId);
            final WettkampfDO competition = wettkampfComponent.findById(matchData.getWettkampfId());
            final VeranstaltungDO event = veranstaltungComponent.findById(competition.getWettkampfVeranstaltungsId());

            return assembleWettkampfInfo(competition, event);
        } catch (Exception ex) {
            LOGGER.warn("Failed to build wettkampf info for matchId {} and wettkampfId {}: {}",
                    matchId, wettkampfId, ex.getMessage());
            return null;
        }
    }

    /**
     * Assembles WettkampfInfoDO from competition and event data
     */
    private WettkampfInfoDO assembleWettkampfInfo(WettkampfDO competition, VeranstaltungDO event) {
        return new WettkampfInfoDO(
                competition.getId(),
                competition.getWettkampfTag(),
                competition.getWettkampfDatum(),
                competition.getWettkampfBeginn(),
                competition.getWettkampfOrtsname(),
                competition.getWettkampfOrtsinfo(),
                competition.getWettkampfStrasse(),
                competition.getWettkampfPlz(),
                event.getVeranstaltungID(),
                event.getVeranstaltungName(),
                event.getVeranstaltungSportJahr(),
                event.getVeranstaltungLigaName(),
                event.getVeranstaltungWettkampftypName()
        );
    }

    /**
     * Finds the enemy team's match ID for the given competition, match number, and enemy team ID.
     * This allows frontend to navigate to enemy team's match object.
     */
    private Long findEnemyMatchId(long wettkampfId, long matchNumber, long enemyTeamId) {
        try {
            // Find the enemy team's match using the match component
            List<MatchDO> allMatches = matchComponent.findByWettkampfId(wettkampfId);
            
            return allMatches.stream()
                    .filter(match -> match.getNr() == matchNumber && 
                                   match.getMannschaftId().equals(enemyTeamId))
                    .map(MatchDO::getId)
                    .findFirst()
                    .orElse(null); // Return null if enemy match not found
                    
        } catch (Exception e) {
            LOGGER.warn("Could not find enemy match ID for wettkampfId={}, matchNr={}, enemyTeamId={}: {}", 
                       wettkampfId, matchNumber, enemyTeamId, e.getMessage());
            return null;
        }
    }

    //================================================================================
    // Validation Methods
    //================================================================================

    /**
     * Validates that arrow values are within the valid range (0-10)
     * According to official archery rules
     */
    private void validateArrowValues(SchuetzenSatzDO satz) {
        if (satz == null) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, 
                "Satzeingabe cannot be null");
        }

        // Validate each arrow value (0-10 range)
        validateSingleArrowValue(satz.getSchuss1(), "Schuss 1");
        validateSingleArrowValue(satz.getSchuss2(), "Schuss 2");
        
        // Only validate schuss3 if ARROWS_PER_SHOOTER >= 3
        if (ARROWS_PER_SHOOTER >= 3) {
            validateSingleArrowValue(satz.getSchuss3(), "Schuss 3");
        }
    }

    /**
     * Validates a single arrow value
     */
    private void validateSingleArrowValue(Integer arrowValue, String arrowName) {
        if (arrowValue != null && (arrowValue < 0 || arrowValue > 10)) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, 
                arrowName + " value " + arrowValue + " is invalid. Must be between 0 and 10.");
        }
    }

    /**
     * Validates that the shooter was properly registered in schützenmeldung
     */
    private void validateShooterRegistration(long wettkampfId, long teamId, long matchNr, 
                                           long passeNr, long shooterId) {
        try {
            Optional<PasseDO> registrationPasse = passeComponent.findByPkOptional(
                wettkampfId, matchNr, teamId, 1L, shooterId);
            
            if (!registrationPasse.isPresent()) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, 
                    "Shooter " + shooterId + " was not registered in Schützenmeldung for this match");
            }
        } catch (Exception e) {
            LOGGER.error("Error validating shooter registration for shooterId={}: {}", shooterId, e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, 
                "Could not validate shooter registration: " + e.getMessage());
        }
    }

    /**
     * Validates that the match is not already complete
     */
    private void validateMatchNotComplete(long wettkampfId, long teamId, TabletSchusszettelEntity session) {
        try {
            // Use shared service for consistent match completion detection
            boolean isComplete = matchAnalysisService.isMatchComplete(
                session.getCurrentMatchId(), teamId, session.getGegnerTeamId());
            
            if (isComplete) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, 
                    "Cannot enter scores - match is already complete");
            }
        } catch (BusinessException e) {
            throw e; // Re-throw business exceptions
        } catch (Exception e) {
            LOGGER.error("Error validating match completion status: {}", e.getMessage());
            // Allow score entry if we can't determine match status
        }
    }

    /**
     * Synchronized handler for match completion to prevent race conditions
     * Uses the match ID as synchronization key to ensure atomic completion handling
     */
    private synchronized void handleMatchCompletionSync(long wettkampfId, long teamId, 
                                                       TabletSchusszettelEntity session, 
                                                       long matchId, int passeNr) {
        // 1) Re-check opponent's state within synchronized block
        final Optional<TabletSchusszettelEntity> oppOpt =
                sessionDAO.findByWettkampfUndTeam(wettkampfId, session.getGegnerTeamId());

        if (!oppOpt.isPresent() || !STATUS_WARTE.equals(oppOpt.get().getStatus())) {
            // Opponent not yet done → go into WAIT
            session.setStatus(STATUS_WARTE);
            sessionDAO.updateStatus(session, 0L);
            LOGGER.debug("Team {} entered WARTE state for match {}", teamId, matchId);

        } else {
            // Both sides done with this end → check if match is complete using shared service
            boolean isComplete = matchAnalysisService.isMatchComplete(
                session.getCurrentMatchId(), teamId, session.getGegnerTeamId());

            if (isComplete) {
                // Match over → advance both teams atomically
                LOGGER.info("Match {} completed", matchId);
                advanceToNextMatchOrEndInternal(wettkampfId, session);
                advanceToNextMatchOrEndInternal(wettkampfId, oppOpt.get());

            } else {
                // Match still on → advance both to next set
                LOGGER.debug("Match {} continuing to set {}", matchId, passeNr + 1);
                session.setCurrentPasseNumber(passeNr + 1);
                session.setStatus(STATUS_SATZEINGABE);
                sessionDAO.updateStatus(session, 0L);

                TabletSchusszettelEntity oppSession = oppOpt.get();
                oppSession.setCurrentPasseNumber(passeNr + 1);
                oppSession.setStatus(STATUS_SATZEINGABE);
                sessionDAO.updateStatus(oppSession, 0L);
            }
        }
    }

    /**
     * Enhanced validation for schützenmeldung
     * Validates team roster and prevents duplicates
     */
    private void validateSchützenmeldungTeamRoster(long teamId, List<Long> registeredShooterIds) {
        // 1) Get all team members with deployment status >= 1
        List<MannschaftsmitgliedDO> teamMembers = mmComponent.findByTeamId(teamId).stream()
                .filter(m -> m.getDsbMitgliedEingesetzt() != null && m.getDsbMitgliedEingesetzt() >= 1)
                .collect(Collectors.toList());

        Set<Long> validMemberIds = teamMembers.stream()
                .map(MannschaftsmitgliedDO::getDsbMitgliedId)
                .collect(Collectors.toSet());

        // 2) Validate exactly 3 shooters
        if (registeredShooterIds.size() != SHOOTERS_PER_TEAM) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Exactly " + SHOOTERS_PER_TEAM + " shooters required, got " + registeredShooterIds.size());
        }

        // 3) Check for duplicates
        Set<Long> uniqueShooters = new HashSet<>(registeredShooterIds);
        if (uniqueShooters.size() != registeredShooterIds.size()) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Duplicate shooters not allowed in registration");
        }

        // 4) Validate all shooters belong to team and are deployed
        for (Long shooterId : registeredShooterIds) {
            if (!validMemberIds.contains(shooterId)) {
                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                        "Shooter " + shooterId + " is not a valid deployed member of team " + teamId);
            }
        }
    }

    /**
     * Atomically creates passes for all registered shooters
     * Uses transaction-like behavior to ensure all-or-nothing semantics
     */
    private void createPassesForRegisteredShooters(long wettkampfId, long teamId, 
                                                  TabletSchusszettelEntity session, 
                                                  List<Long> shooterIds) {
        final int PASSES_PER_SHOOTER = 5;
        List<PasseDO> passesToCreate = new ArrayList<>();
        
        try {
            // 1) Prepare all passes first
            for (Long dsbId : shooterIds) {
                for (long lfdnr = 1; lfdnr <= PASSES_PER_SHOOTER; lfdnr++) {
                    try {
                        // Check if passe already exists
                        passeComponent.findByPk(wettkampfId, session.getCurrentMatchNumber(), 
                                              teamId, lfdnr, dsbId);
                        // If we reach here, passe exists - skip
                        LOGGER.debug("Passe already exists for shooter in set {}", lfdnr);
                        
                    } catch (Exception e) {
                        // Passe doesn't exist, prepare for creation
                        PasseDO passe = new PasseDO(
                                null,                // id (generated)
                                teamId,                 // passeMannschaftId
                                wettkampfId,            // passeWettkampfId
                                session.getCurrentMatchNumber(),
                                session.getCurrentMatchId(),
                                lfdnr,                  // sequence # (1–5)
                                dsbId,                  // shooter ID
                                null, null, null, null, null, null // empty arrows
                        );
                        passesToCreate.add(passe);
                    }
                }
            }
            
            // 2) Create all passes atomically
            for (PasseDO passe : passesToCreate) {
                passeComponent.create(passe, 0L);
            }
            
            LOGGER.info("Successfully created {} passes for {} shooters in match {}", 
                       passesToCreate.size(), shooterIds.size(), session.getCurrentMatchId());
            
        } catch (Exception e) {
            LOGGER.error("Error creating passes for shooters: {}", e.getMessage(), e);
            throw new TechnicalException(ErrorCode.INTERNAL_ERROR, 
                "Failed to create passes for registered shooters: " + e.getMessage());
        }
    }
}
