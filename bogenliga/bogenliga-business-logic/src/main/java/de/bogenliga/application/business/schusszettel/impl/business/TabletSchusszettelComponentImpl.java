package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSessionDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSessionEntity;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the TabletSchusszettelComponent interface
 *
 * @author Marty Lauterbach, mklemmingen
 */
@Service
public class TabletSchusszettelComponentImpl implements TabletSchusszettelComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSchusszettelComponentImpl.class);

    // Constants for status values
    private static final String STATUS_SATZEINGABE = "SATZEINGABE";
    private static final String STATUS_SCHUETZENMELDUNG = "SCHUETZENMELDUNG";
    private static final String STATUS_WARTE = "WARTE";
    private static final String STATUS_WETTKAMPF_ENDE = "WETTKAMPF_ENDE";

    @Autowired
    private TabletSessionDAO tabletSessionDAO;

    @Autowired
    private TabletSchusszettelDAO tabletSchusszettelDAO;

    @Autowired
    private MannschaftsmitgliedDAO mannschaftsmitgliedDAO;

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private MatchComponent matchComponent;

    @Override
    public TabletSchusszettelDO getStatus(long wettkampfId, long teamId, String token) {
        // Validate inputs
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Token must not be null or empty");
        }

        // Validate token
        TabletSessionEntity session = tabletSessionDAO.findByToken(wettkampfId, teamId, token)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_PERMISSION_ERROR,
                        "Invalid token or session expired for teamId: " + teamId));

        // Validate permissions
        if (!session.getTeamId().equals(teamId) || !session.getWettkampfId().equals(wettkampfId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR,
                    "Team or wettkampf mismatch. Access denied for teamId: " + teamId);
        }

        TabletSchusszettelDO result = new TabletSchusszettelDO();
        result.setStatus(mapStatus(session.getStatus()));

        try {
            // TODO: Implement Use Case 1 - Tablet Status abfragen

            // STEPS:

            // 1. Check current session status:
            //    - For SCHUETZENMELDUNG: Return team info, available shooters with id and name, and return full MATCH INFO

            //    - For SATZEINGABE: Check if passe is open for input and return full MATCH INFO

            //    - For WARTE: Check if opponent is also in WARTE status

            //    - For WETTKAMPF_ENDE: Return wettkampf_ende (WettkampfTag zu ende)

            // 2. Check match progress:

            //    - Use PasseDAO.countByMatchAndTeam() to get total number of passes
            //    - Use PasseDAO.findGroupedBySchuetze() to get grouped set data
            //    - Use MatchDAO.findById() to check if points allocation is complete

            // 3. Check opponent status:

            //    - If own status is WARTE a

            //    - If match is complete, update status to WETTKAMPF_ENDE

            if (STATUS_SATZEINGABE.equals(session.getStatus())) {
                populateStatusData(wettkampfId, teamId, session, result);
            }

            if (STATUS_WARTE.equals(session.getStatus())) {
                handleWaitingState(wettkampfId, session, result);
            }
        } catch (BusinessException e) {
            // Re-throw business exceptions
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error retrieving tablet schusszettel status for wettkampfId: {} and teamId: {}",
                    wettkampfId, teamId, e);
            throw new TechnicalException(ErrorCode.DATABASE_ERROR,
                    "Error retrieving status data for wettkampf: " + wettkampfId + ", team: " + teamId, e);
        }

        return result;
    }

    private void populateStatusData(long wettkampfId, long teamId, TabletSessionEntity session,
                                    TabletSchusszettelDO result) {
        try {
            List<TabletSchusszettelEntity> satzdaten = tabletSchusszettelDAO.findByWettkampfUndTeam(wettkampfId, teamId);

            // Use utility methods to build result components
            result.setSatzErgebnisse(buildSatzErgebnisse(satzdaten, session.getCurrentPasseNumber()));
            result.setEigenesTeam(buildTeamInfo(session.getTeamId()));
            result.setGegnerischesTeam(buildTeamInfo(session.getGegnerTeamId()));
            result.setSchuetzenMatchPunkte(buildMatchPunkte(satzdaten));
            result.setSchuetzeStammDaten(buildSchuetzeStammdaten(session.getCurrentMatchId()));
        } catch (Exception e) {
            LOGGER.error("Failed to populate status data", e);
            throw new TechnicalException(ErrorCode.DATABASE_ERROR,
                    "Failed to populate status data for wettkampf: " + wettkampfId, e);
        }
    }

    private void handleWaitingState(long wettkampfId, TabletSessionEntity session, TabletSchusszettelDO result) {
        // TODO: Implement status transition logic for WARTE status
        // STEPS:
        // 1. Get opponent's session status using TabletSessionDAO.findByWettkampfUndTeam()
        // 2. Check if opponent is also in WARTE status
        // 3. If both are in WARTE status, check if match is complete (all 5 sets done)
        // 4. If match is complete:
        //    - Update session status to WETTKAMPF_ENDE
        //    - Write match results if not already done
        // 5. If not complete, advance to next set:
        //    - Update session status to SATZEINGABE
        //    - Increment currentPasseNumber

        try {
            Optional<TabletSessionEntity> gegner = tabletSessionDAO.findByWettkampfUndTeam(
                    wettkampfId, session.getGegnerTeamId());

            boolean beideWarten = gegner.isPresent() && STATUS_WARTE.equals(gegner.get().getStatus());

            if (beideWarten) {
                List<TabletSchusszettelEntity> satzdaten = tabletSchusszettelDAO.findByWettkampfUndTeam(
                        wettkampfId, session.getTeamId());

                int anzahlSaetze = (int) satzdaten.stream()
                        .map(TabletSchusszettelEntity::getSatzNr)
                        .distinct()
                        .count();

                if (anzahlSaetze >= 5) {
                    session.setStatus(STATUS_WETTKAMPF_ENDE);
                } else {
                    session.setStatus(STATUS_SATZEINGABE);
                    session.setCurrentPasseNumber(session.getCurrentPasseNumber() + 1);
                }

                tabletSessionDAO.updateStatus(session, -1L); // -1L = System user
                result.setStatus(mapStatus(session.getStatus()));
            }
        } catch (Exception e) {
            LOGGER.error("Error processing waiting state for wettkampfId: {}", wettkampfId, e);
            throw new TechnicalException(ErrorCode.DATABASE_ERROR,
                    "Error processing waiting state for wettkampf: " + wettkampfId, e);
        }
    }

    @Override
    public void submitSchuetzen(long wettkampfId, long teamId, String token, SchuetzenMeldungDO input) {
        // Validate inputs
        if (input == null || input.getGemeldeteSchuetzen() == null) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Schützen input cannot be null");
        }

        // Validate token
        TabletSessionEntity session = tabletSessionDAO.findByToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_PERMISSION_ERROR,
                        "Invalid token or session expired for teamId: " + teamId));

        // TODO: Implement Use Case 2 - Schützen melden
        // STEPS:
        // 1. Verify token and session using TabletSessionDAO.findByToken()
        // 2. Validate each shooter in input.getGemeldeteSchuetzen():
        //    - Check if they are members of the team using MannschaftsmitgliedDAO.isMemberOfTeam()
        //    - Verify that the correct number of shooters are selected (typically 3)
        //    - Check if shooters are already assigned to other matches (optional)
        // 3. For each valid shooter:
        //    - Assign to match using MitgliedZuordnungDAO.create() or .assignToMatch()
        //    - Set position in match based on order (1, 2, 3)
        // 4. Update session status:
        //    - Set status to SATZEINGABE
        //    - Set currentPasseNumber to 1
        //    - Use TabletSessionDAO.updateStatus() to persist changes

        try {
            for (Long schuetzenId : input.getGemeldeteSchuetzen()) {
                if (schuetzenId == null) {
                    throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                            "Shooter ID cannot be null");
                }

                if (!mannschaftsmitgliedDAO.isMemberOfTeam(schuetzenId, teamId)) {
                    throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                            "Shooter " + schuetzenId + " is not a member of team " + teamId);
                }

                try {
                    assignSchuetzeToMatch(session.getCurrentMatchId(), schuetzenId);
                } catch (Exception e) {
                    throw new TechnicalException(ErrorCode.DATABASE_ERROR,
                            "Failed to assign shooter " + schuetzenId + " to match", e);
                }
            }

            // Update session status
            session.setStatus(STATUS_SATZEINGABE);
            session.setCurrentPasseNumber(1);
            tabletSessionDAO.updateStatus(session, -1L);

        } catch (BusinessException e) {
            // Re-throw business exceptions
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error submitting shooters for wettkampfId: {} and teamId: {}",
                    wettkampfId, teamId, e);
            throw new TechnicalException(ErrorCode.DATABASE_ERROR,
                    "Error submitting shooters for wettkampf: " + wettkampfId, e);
        }
    }

    @Override
    public void submitSatz(long wettkampfId, long teamId, String token, SatzEingabeDO eingabe) {
        // Validate inputs
        if (eingabe == null || eingabe.getSatzeingabe() == null) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "Satz input cannot be null");
        }

        // Validate token
        TabletSessionEntity session = tabletSessionDAO.findByToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_PERMISSION_ERROR,
                        "Invalid token or session expired for teamId: " + teamId));

        // TODO: Implement Use Case 3 - Satzdaten eingeben
        // STEPS:
        // 1. Verify token and session using TabletSessionDAO.findByToken()
        // 2. Validate input data:
        //    - Check that all required shooters have submitted data
        //    - Validate shot values (0-10 or M for miss)
        //    - Check that the right number of shots per shooter is provided (usually 3)
        // 3. Convert DTO to entities using convertToEntities()
        // 4. For each entity:
        //    - Set the current satz number from session.getCurrentPasseNumber()
        //    - Save using tabletSchusszettelDAO.saveSatzEingabe() or PasseDAO.insertPasse()
        // 5. Check progress (count distinct satzNr values)
        // 6. Update session status:
        //    - If all 5 sets completed, set status to WARTE
        //    - Otherwise, keep SATZEINGABE and increment currentPasseNumber
        //    - Use TabletSessionDAO.updateStatus() to persist changes

        try {
            int satzNr = session.getCurrentPasseNumber();

            // Convert DTO to entities and save
            List<TabletSchusszettelEntity> entities = convertToEntities(wettkampfId, teamId, eingabe);
            entities.forEach(e -> e.setSatzNr(satzNr));

            for (TabletSchusszettelEntity entity : entities) {
                tabletSchusszettelDAO.saveSatzEingabe(entity);
            }

            // Count the number of distinct sets
            int satzCount = (int) tabletSchusszettelDAO
                    .findByWettkampfUndTeam(wettkampfId, teamId)
                    .stream()
                    .map(TabletSchusszettelEntity::getSatzNr)
                    .distinct()
                    .count();

            // Set the appropriate status based on the number of sets
            session.setStatus(satzCount >= 5 ? STATUS_WARTE : STATUS_SATZEINGABE);
            session.setCurrentPasseNumber(satzNr + 1);
            tabletSessionDAO.updateStatus(session, -1L);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error submitting satz for wettkampfId: {} and teamId: {}",
                    wettkampfId, teamId, e);
            throw new TechnicalException(ErrorCode.DATABASE_ERROR,
                    "Error submitting satz data for wettkampf: " + wettkampfId + ", team: " + teamId, e);
        }
    }

    @Override
    public void initializeForWettkampf(long wettkampfId, long teamId) {
        if (wettkampfId <= 0 || teamId <= 0) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "WettkampfId and teamId must be positive values");
        }

        try {
            tabletSchusszettelDAO.initializeForWettkampf(wettkampfId, teamId);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize for wettkampfId: {}", wettkampfId, e);
            throw new TechnicalException(ErrorCode.DATABASE_ERROR,
                    "Failed to initialize tablet schusszettel for wettkampf: " + wettkampfId, e);
        }
    }

    @Override
    public void deleteForWettkampf(long wettkampfId) {
        if (wettkampfId <= 0) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "WettkampfId must be a positive value");
        }

        try {
            tabletSchusszettelDAO.deleteByWettkampfId(wettkampfId);
        } catch (Exception e) {
            LOGGER.error("Failed to delete for wettkampfId: {}", wettkampfId, e);
            throw new TechnicalException(ErrorCode.DATABASE_ERROR,
                    "Failed to delete tablet schusszettel for wettkampf: " + wettkampfId, e);
        }
    }

    @Override
    public boolean existsForWettkampf(long wettkampfId) {
        if (wettkampfId <= 0) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                    "WettkampfId must be a positive value");
        }

        try {
            return tabletSchusszettelDAO.existsByWettkampfId(wettkampfId);
        } catch (Exception e) {
            LOGGER.error("Failed to check existence for wettkampfId: {}", wettkampfId, e);
            throw new TechnicalException(ErrorCode.DATABASE_ERROR,
                    "Failed to check if tablet schusszettel exists for wettkampf: " + wettkampfId, e);
        }
    }

    // Utility methods to replace helper class dependencies
    private List<Object> buildSatzErgebnisse(List<TabletSchusszettelEntity> satzdaten, int currentPasseNumber) {
        // TODO: Implement building set results
        // STEPS:
        // 1. Group satzdaten by satzNr
        // 2. For each completed satz (number < currentPasseNumber):
        //    a. Sum all ring values for each shooter (schuss1 + schuss2 + schuss3)
        //    b. Calculate total rings for team (sum of all shooters)
        //    c. Create a SatzErgebnisDO object with:
        //       - satzNr: The set number
        //       - team1Punkte: Total rings for own team
        //       - team2Punkte: Total rings for opponent team (need to fetch separately)
        // 3. Return list of SatzErgebnisDO objects in sequence order

        // For current satz (number == currentPasseNumber), return partially filled data

        return new ArrayList<>();
    }

    private Object buildTeamInfo(Long teamId) {
        // TODO: Implement building team info
        // STEPS:
        // 1. Use VereinsmannschaftDAO or TeamDAO to get team by ID
        // 2. Create a map/object containing:
        //    - team_id: ID of the team
        //    - team_name: Name of the team (likely requires join with verein/club table)
        //    - verein_id: ID of the club
        //    - verein_name: Name of the club
        // 3. Return the created object for JSON serialization

        return new Object();
    }

    private Object buildMatchPunkte(List<TabletSchusszettelEntity> satzdaten) {
        // TODO: Implement calculating match points
        // STEPS:
        // 1. Group satzdaten by satzNr
        // 2. For each completed satz:
        //    a. Calculate sum of rings for own team
        //    b. Retrieve opponent's rings for same satz (use separate query)
        //    c. Compare rings to determine winner:
        //       - Higher rings gets 2 match points
        //       - Equal rings means 1 point each
        // 3. Sum up total match points for each team
        // 4. Create object with:
        //    - satzpunkte_eigen: Array of own team's set points per satz
        //    - satzpunkte_gegner: Array of opponent's set points per satz
        //    - matchpunkt_eigen: Total match points for own team
        //    - matchpunkt_gegner: Total match points for opponent
        // 5. Return the created object

        return new Object();
    }

    private Object buildSchuetzeStammdaten(Long matchId) {
        // TODO: Implement retrieving shooter data
        // STEPS:
        // 1. Use MitgliedZuordnungDAO to get assigned shooters for this match
        // 2. For each assigned shooter:
        //    a. Get personal details (name, id) from DSBMitglied or other relevant table
        //    b. Create SchuetzeStammdatenDO with:
        //       - schuetzenId: ID of the shooter
        //       - rueckennummer: Position/number in team (from MitgliedZuordnung)
        //       - vorname: First name
        //       - nachname: Last name
        // 3. If no shooters assigned yet (SCHUETZENMELDUNG state), get potential
        //    shooters from MannschaftsmitgliedDAO
        // 4. Return list of SchuetzeStammdatenDO objects

        return new Object();
    }

    private List<TabletSchusszettelEntity> convertToEntities(long wettkampfId, long teamId, SatzEingabeDO eingabe) {
        // TODO: Implement conversion from DTO to entities
        // STEPS:
        // 1. Initialize empty list for entities
        // 2. For each SchuetzenSatzDO in eingabe.getSatzeingabe():
        //    a. Create new TabletSchusszettelEntity
        //    b. Set common fields:
        //       - wettkampfId, teamId from parameters
        //       - schuetzenId from SchuetzenSatzDO
        //    c. Set shot values:
        //       - schuss1, schuss2, schuss3 from SchuetzenSatzDO
        //       - Validate shot values (0-10 or null for miss)
        //    d. The satzNr will be set later from the session's currentPasseNumber
        // 3. Return list of entities

        List<TabletSchusszettelEntity> entities = new ArrayList<>();

        return entities;
    }

    private void assignSchuetzeToMatch(Long matchId, Long schuetzenId) {
        LOGGER.info("Assigning shooter {} to match {}", schuetzenId, matchId);
        // TODO: Implement shooter assignment to match
        // STEPS:
        // 1. Check if MitgliedZuordnung already exists for this match and shooter
        // 2. If not exists:
        //    a. Create new MitgliedZuordnungDO or entity
        //    b. Set matchId, schuetzenId
        //    c. Determine position (rueckennummer) for shooter in match:
        //       - Count existing assignments for this match
        //       - Use count+1 as position (1-based)
        // 3. Call MitgliedZuordnungDAO.create() or similar to persist
        // 4. Handle SQL constraints like unique violations
    }

    private TabletSchusszettelDO.TabletSchusszettelStatus mapStatus(String status) {
        return switch (status) {
            case STATUS_SATZEINGABE -> TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE;
            case STATUS_SCHUETZENMELDUNG -> TabletSchusszettelDO.TabletSchusszettelStatus.SCHUETZENMELDUNG;
            case STATUS_WARTE -> TabletSchusszettelDO.TabletSchusszettelStatus.WARTE;
            case STATUS_WETTKAMPF_ENDE -> TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE;
            default -> TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED;
        };
    }

    // TODO: Add helper method to check if all passen are completed for a match
    // STEPS:
    // 1. Get total number of shooters assigned to match (typically 3)
    // 2. Get total number of sets in match (typically 5)
    // 3. Calculate total expected passen: shooters * sets * 3 shots
    // 4. Count actual passen saved in database
    // 5. Return true if actual count matches expected count

    // TODO: Add helper method to validate shot values
    // STEPS:
    // 1. Check that shot value is between 0-10 or null (for miss)
    // 2. Return true if valid, false if invalid

    // TODO: Add helper method to check if match is complete
    // STEPS:
    // 1. Count distinct satz numbers in database
    // 2. Return true if count >= 5 (all sets completed)
    // 3. Optionally check if match points have been assigned in match table
}
