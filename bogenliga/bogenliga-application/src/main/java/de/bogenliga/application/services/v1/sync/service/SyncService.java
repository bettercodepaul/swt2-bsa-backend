package de.bogenliga.application.services.v1.sync.service;

import de.bogenliga.application.business.ligatabelle.api.LigatabelleComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.business.match.api.types.LigamatchDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.mannschaftsmitglied.mapper.MannschaftsMitgliedDTOMapper;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import de.bogenliga.application.services.v1.mannschaftsmitglied.service.MannschaftsMitgliedService;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.services.v1.match.service.MatchService;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncLigatabelleDTOMapper;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncPasseDTOMapper;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncMannschaftsmitgliedDTOMapper;
import de.bogenliga.application.services.v1.sync.mapper.WettkampfExtDTOMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncLigatabelleDTO;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncMatchDTOMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMannschaftsmitgliedDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncPasseDTO;
import de.bogenliga.application.services.v1.sync.model.SyncWrapper;
import de.bogenliga.application.services.v1.sync.model.WettkampfExtDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.naming.NoPermissionException;
import javax.print.attribute.standard.Media;

/**
 * I'm a REST resource and handle liga CRUD requests over the HTTP protocol
 *
 * We are implementing SyncServices independent to Online-Sevices as Services may be
 * updated any time - but data structure in offline Client DB should not be affected
 * Therefor we will seperate online DTOs drom Offline DTOs
 */
@RestController
@CrossOrigin
@RequestMapping("v1/sync")
public class SyncService implements ServiceFacade {
    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "Veranstaltung Id must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf Id must not be negative";
    private static final String ERR_NOT_NULL_TEMPLATE = "MatchService: %s: %s must not be null.";
    private static final String SERVICE_FIND_MATCHES_BY_IDS = "findMatchesByIds";
    private static final String SERVICE_SYNCHRONIZE_MATCHES_AND_PASSEN = "synchronizeMatchesAndPassen";
    private static final String CHECKED_PARAM_MATCH_ID = "Match ID";
    private static final String ERR_NOT_NEGATIVE_TEMPLATE = "MatchService: %s: %s must not be negative.";
    private static final String ERR_WETTKAMPF_ALREADY_OFFLINE = "Cannot got offline. Wettkampf is already offline";


    private static final String PRECONDITION_MSG_OFFLINE_TOKEN = "Offlinetoken must not be null";

    private final Logger logger = LoggerFactory.getLogger(
            de.bogenliga.application.services.v1.sync.service.SyncService.class);

    private final LigatabelleComponent ligatabelleComponent;
    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final WettkampfComponent wettkampfComponent;
    private final MannschaftsMitgliedService mannschaftsMitgliedService;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final MatchService matchService;


    /**
     * Constructor with dependency injection
     *
     * @param ligatabelleComponent to handle the database access
     */
    @Autowired
    public SyncService(final LigatabelleComponent ligatabelleComponent,
                       final MatchComponent matchComponent,
                       final MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                       final PasseComponent passeComponent,
                       final RequiresOnePermissionAspect requiresOnePermissionAspect,
                       final WettkampfComponent wettkampfComponent,
                       final MannschaftsMitgliedService mannschaftsMitgliedService,
                       final MatchService matchService) {
        this.ligatabelleComponent = ligatabelleComponent;
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.matchService = matchService;
        this.mannschaftsMitgliedService = mannschaftsMitgliedService;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
    }


    /**
     * I return the current "ligatabelle" for a "wettkampftid (tag)" entries of the database.
     *
     * @return list of {@link LigaSyncLigatabelleDTO} as JSON
     */
    @GetMapping(
            value = "veranstaltung={id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LigaSyncLigatabelleDTO> getLigatabelleVeranstaltungOffline(@PathVariable("id") final long id) {

        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);
        logger.debug("Receive 'Ligatabelle für Veranstaltung' request with ID '{}'", id);

        final List<LigatabelleDO> ligatabelleDOList = ligatabelleComponent.getLigatabelleVeranstaltung(id);

        return ligatabelleDOList.stream().map(LigaSyncLigatabelleDTOMapper.toDTO).collect(Collectors.toList());
    }

    /* TODO
     * I return the all Matches from "ligamatch"-Table for
     * a "wettkampftid (tag)" entries of the database.
     *
     * @return list of {@link LigaSyncMatchDTO} as JSON
     * vermutlich die folgende Funktion:
     * list<ligamatchDO> ligamatchcomponent.findById(wetkkmapfID)
     **/


    /**
     * I return the all Matches from "ligamatch"-Table for * a "wettkampftid (tag)" entries of the database.
     *
     * @return list of {@link LigaSyncMatchDTO} as JSON
     */

    @GetMapping(
            value = "match/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LigaSyncMatchDTO> findByWettkampfId(@PathVariable("id") final long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        this.checkMatchId(wettkampfid);
        logger.debug("Receive 'Wettkampfid for Matches' request with ID '{}'", wettkampfid);
        List<LigamatchDO> wettkampfMatches = matchComponent.getLigamatchDOsByWettkampfId(wettkampfid);

        return wettkampfMatches.stream().map(LigaSyncMatchDTOMapper.toDTO).collect(Collectors.toList());
    }


    private void checkMatchId(Long matchId) {
        Preconditions.checkNotNull(matchId,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_FIND_MATCHES_BY_IDS, CHECKED_PARAM_MATCH_ID));
        Preconditions.checkArgument(matchId >= 0,
                String.format(ERR_NOT_NEGATIVE_TEMPLATE, SERVICE_FIND_MATCHES_BY_IDS, CHECKED_PARAM_MATCH_ID));
    }

    /* TODO
     * I return the all Passe Entries from "ligapasse"-Table for
     * a "wettkampftid (tag)" entries of the database.
     *
     * list<ligapasseDO> ligapassecomponent.findById(wetkkmapfID)
     * @return list of {@link LigaSyncPasseDTO} as JSON
     */


    /**
     * I return the all Passe Entries for a "wettkampftid (tag)" entries of the database.
     *
     * @return list of {@link LigaSyncPasseDTO} as JSON
     */
    @GetMapping(
            value = "passe/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LigaSyncPasseDTO> getLigapassenOffline(@PathVariable("id") final long wettkampfid) {
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        this.checkMatchId(wettkampfid);
        logger.debug("Receive 'Wettkampfid for Passen' request with ID '{}'", wettkampfid);
        List<PasseDO> wettkampfPassenDO = passeComponent.findByWettkampfId(wettkampfid);
        List<LigaSyncPasseDTO> ligaSyncPasseDTOs = new ArrayList<>();

        for (PasseDO currentPasseDO : wettkampfPassenDO) {
            LigaSyncPasseDTO ligaSyncPasseDTO = LigaSyncPasseDTOMapper.toDTO.apply(currentPasseDO);
            ligaSyncPasseDTOs.add(ligaSyncPasseDTO);
        }
        return ligaSyncPasseDTOs;
    }


    /**
     * I return the all Mannschaftsmitglieder Entries for all Mannschaften
     * participating a "wettkampftid (tag)" entries of the database.
     *
     * Aufruf von WettkampfComponent -> getAllowedMitglieder(wettkampfID)
     * @return list of {@link LigaSyncMannschaftsmitgliedDTO} as JSON
     */


    @GetMapping(
            value = "mannschaftsmitglieder/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LigaSyncMannschaftsmitgliedDTO> getMannschaftsmitgliedernOffline(
            @PathVariable("id") final long wettkampfId) {

        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        logger.debug("Receive 'Mannschaftsmitgliedern für Wettkampf' request with WettkampfID '{}'", wettkampfId);

        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            MatchDO matchDO = matchComponent.findByWettkampfIDMatchNrScheibenNr(wettkampfId, 1L, (long) i);
            mannschaftsmitgliedDOList.addAll(
                    mannschaftsmitgliedComponent.findSchuetzenInUebergelegenerLiga(matchDO.getMannschaftId(),
                            wettkampfId));
        }

        return mannschaftsmitgliedDOList.stream().map(LigaSyncMannschaftsmitgliedDTOMapper.toDTO).collect(
                Collectors.toList());
    }

    /**
     * I will update the dataset of a single Wettkampf and set the OfflineToken
     * Token is generated with current user's id + timestamp
     * only a ligaleiter can go offline
     * @return WettkampfExtDTO as JSON
     * @author Jonas Sigloch, SWT SoSe 2022
     */
    @GetMapping(
            value = "wettkampf/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF})
    public List<WettkampfExtDTO> getToken(
            @PathVariable("id") final long wettkampfId,
            final Principal principal) throws NoPermissionException {
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        logger.debug("Received 'update' request with id '{}' to add offline token", wettkampfId);
        // Check if it is the ligaleiter of the wettkampfs liga (not sure if possible); generic check done in permissions

        // prevent going offline for a wettkampf that is already offline
        // as getCurrentUserId does not work, it does so categorically: any wettkampf that is offline, can not be accessed.
        // once it works, we could allow the same user that went offline before to send a second token to cover
        // the edge case of and offline token being created and saved but not received by the frontend
        if(wettkampfComponent.wettkampfIsOffline(wettkampfId)){
            throw new BusinessException(ErrorCode.ENTITY_CONFLICT_ERROR, ERR_WETTKAMPF_ALREADY_OFFLINE);
        }
        // create token in business layer and persist it + return to frontend
        final long userId = UserProvider.getCurrentUserId(principal);
        String offlineToken = wettkampfComponent.generateOfflineToken(userId);
        WettkampfDO wettkampfDO = wettkampfComponent.findById(wettkampfId);
        wettkampfDO.setOfflineToken(offlineToken);
        final WettkampfDO updatedWettkampfDO = wettkampfComponent.update(wettkampfDO, userId);
        List<WettkampfExtDTO> payload = new ArrayList<>();
        payload.add(WettkampfExtDTOMapper.toDTO.apply(updatedWettkampfDO));
        return payload;
    }


    /* TODO
     * I will recieve the OfflineToken from Client
    /**
     * I will recieve the OfflineToken form Client
     * and a list of new Mannschaftmitglieder (identified by missing IDs)
     * the follwing checks will be performed:
     * - are all new Manschaftsmitglieder existing in Backend -> otherwise one Error per missing entry
     *   including Rückennummern ugnd Name der Mannschaft
     * - is the Offline identical to the stored in Backend Wettkampf Table -> otherwise this Dataset is not
     *   the most recent one --> Gero sould advised what to do.
     * @return ok or list of errors
     */

    @PostMapping(
            value = "mannschaftsmitglieder/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_MANNSCHAFT, UserPermission.CAN_MODIFY_MY_VEREIN})
    public ResponseEntity checkOfflineTokenAndSynchronizeMannschaftsMitglieder(@PathVariable("id") final long wettkampfId,
                                                                 @RequestBody final List<LigaSyncMannschaftsmitgliedDTO> mannschaftsMitgliedDTOList,
                                                                 final Principal principal,
                                                                 final String offlineToken) throws NoPermissionException { //@RequestBody

        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkNotNullOrEmpty(offlineToken, PRECONDITION_MSG_OFFLINE_TOKEN);

        //Offline Token check
        logger.debug("Offlinetoken check: {}", offlineToken);
        wettkampfComponent.checkOfflineToken(wettkampfId, offlineToken);

        // save new team members

        final long currentUserId = UserProvider.getCurrentUserId(principal); //always 0
        List<MannschaftsmitgliedDO>  savedMannschaftsMitglieder = new ArrayList<>();
        logger.debug("list: {}", mannschaftsMitgliedDTOList);
        for(LigaSyncMannschaftsmitgliedDTO ligaSyncMannschaftsmitgliedDTO: mannschaftsMitgliedDTOList){

            MannschaftsMitgliedDTO newMannschaftsMitgliedDTO = LigaSyncMannschaftsmitgliedDTOMapper.toMannschaftsmitgliedDTO.apply(ligaSyncMannschaftsmitgliedDTO);
            logger.debug("creating mitgleid in db: {}", newMannschaftsMitgliedDTO);
            MannschaftsMitgliedDTO addedNewMannschaftsMitgliedDO = mannschaftsMitgliedService.create(newMannschaftsMitgliedDTO, principal);


            savedMannschaftsMitglieder.add( MannschaftsMitgliedDTOMapper.toDO.apply(addedNewMannschaftsMitgliedDO));

        }

        logger.debug("Offlinetoken succes: {}", offlineToken);

       return ResponseEntity.ok(
               savedMannschaftsMitglieder.stream().map(LigaSyncMannschaftsmitgliedDTOMapper.toDTO).collect(
                       Collectors.toList()));
    }

    /* TODO
     * I will recieve both lists: matches and passen to store data consistently in a single transaction
     * when data successfully stored, the offlinetoken in wettkampf table is to be removed
     * @return ok or list of errors
     * in case of ok - client should delete offline data
     */

    @PostMapping(value = "syncSchusszettel",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_WETTKAMPF)
    public List<MatchDTO> synchronizeMatchesAndPassen(List<LigaSyncMatchDTO> ligaSyncMatchDTOs,
                                                      List<LigaSyncPasseDTO> ligaSyncPasseDTOs,
                                                      Principal principal) throws NoPermissionException {

        final Long userId = UserProvider.getCurrentUserId(principal);

        List<MatchDTO> matchDTOs = new ArrayList<>();

        for (LigaSyncMatchDTO ligasyncmatchDTO : ligaSyncMatchDTOs) {

            // Get MatchId and WettkampfId from LigaSyncMatchDTO
            Long matchId = ligasyncmatchDTO.getId();
            Long wettkampfId = ligasyncmatchDTO.getWettkampfId();

            // Find Match and Wettkampf in DB
            MatchDO matchDO = matchComponent.findById(matchId);
            WettkampfDO wettkampfDO = wettkampfComponent.findById(wettkampfId);

            // Get Begegnung from Match
            Long begegnung = matchDO.getBegegnung();

            // Map LigaSyncMatchDTO to MatchDTO
            MatchDTO matchDTO = LigaSyncMatchDTOMapper.toMatchDTO.apply(ligasyncmatchDTO);

            // Set begegnung, WettkampfTag and WettkampfTyp in MatchDTO
            matchDTO.setBegegnung(begegnung);
            matchDTO.setWettkampfTag(wettkampfDO.getWettkampfTag());

            // Map LigaSyncPasseDTO to PasseDTO
            // Set List<PasseDTO> to MatchDTO where MatchId is the same
            matchDTO.setPassen(ligaSyncPasseDTOs.stream().map(LigaSyncPasseDTOMapper.toPasseDTO)
                    .filter(passeDTO -> passeDTO.getMatchId().equals(ligasyncmatchDTO.getId()))
                    .collect(Collectors.toList()));
            for (PasseDTO passeDTO : matchDTO.getPassen()) {
                passeDTO.setMatchNr(matchDTO.getNr());
            }
            matchDTOs.add(matchDTO);
        }

        // Go through received matches (matchDTOs) and search for two matches with same wettkampfID, Nr, Begegnung
        // Two Matches from the same Schusszettel
        // Call MatchService
        for (int i = 0; i < matchDTOs.size(); i++) {

            List<MatchDTO> twoMatchesDTO = new ArrayList<>();
            twoMatchesDTO.add(matchDTOs.get(i));

            for (int j = i + 1; j < matchDTOs.size(); j++) {

                if (twoMatchesDTO.get(0).getWettkampfId().equals(matchDTOs.get(j).getWettkampfId()) &&
                        twoMatchesDTO.get(0).getNr().equals(matchDTOs.get(j).getNr()) &&
                        twoMatchesDTO.get(0).getBegegnung().equals(matchDTOs.get(j).getBegegnung())) {

                    twoMatchesDTO.add(matchDTOs.get(j));
                    matchService.saveMatches(twoMatchesDTO, principal);
                }
            }
        }

        // Delete OfflineToken
        WettkampfDO wettkampfDO = wettkampfComponent.findById(matchDTOs.get(0).getWettkampfId());
        wettkampfComponent.deleteOfflineToken(wettkampfDO, userId);

        return matchDTOs;
    }

    /**
    * I delete the offline token of a wettkampf unconditionally
    * Necessary to reset a wettkampf that has been taken offline and for some reason can not be taken back online
    * any data saved offline will be lost
    * Only an admin can call this function
    * it will return wettkampf without token as confirmation
    * @author Jonas Sigloch, SWT SoSe 2022
    * @return WettkampfExtDTO as JSON
    */
    @GetMapping(
            value = "wettkampf/{id}/reset",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_STAMMDATEN)
    public WettkampfExtDTO goOnlineUnconditionally (
            @PathVariable("id") final long wettkampfId,
            final Principal principal) {

        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        logger.debug("Admin Request to delete Offline Token of Wettkampf with WettkampfID '{}'", wettkampfId);

        final long userId = UserProvider.getCurrentUserId(principal);
        // delete offline token by setting it to null in the passed wettkampf
        final WettkampfDO wettkampfDO =wettkampfComponent.findById(wettkampfId);
        final WettkampfDO updatedWettkampfDO = wettkampfComponent.deleteOfflineToken(wettkampfDO, userId);
        return WettkampfExtDTOMapper.toDTO.apply(updatedWettkampfDO);
    }

    @PostMapping(
            value = "wettkampf/{id}/sync",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_WETTKAMPF)
    public void handleSync(@RequestBody SyncWrapper syncPayload, Principal principal) {
        final long wettkampfId = syncPayload.getWettkampfId();
        final String offlineToken = syncPayload.getOfflineToken();
        // check token -> done in mannschaftsmitglied sync
        logger.debug("token: {}", syncPayload.getOfflineToken());
        logger.debug("id: {}", syncPayload.getWettkampfId());
        logger.debug("match: {}", syncPayload.getMatch());
        logger.debug("passe: {}", syncPayload.getPasse());
        logger.debug("tomitglieder: {}",syncPayload.getMannschaftsmitglied());



        logger.debug("saving mitgleider------------------------------------------------------------------------------------------------------------");
        // handle mannschaftsmitglieder
        final List<LigaSyncMannschaftsmitgliedDTO> mannschaftsmitgliedDTOS = syncPayload.getMannschaftsmitglied();
        try {
            checkOfflineTokenAndSynchronizeMannschaftsMitglieder(wettkampfId, mannschaftsmitgliedDTOS, principal, offlineToken);
        } catch (NoPermissionException e) {
            throw new BusinessException(ErrorCode.UNDEFINED, "error syncing mitglieder");
        }
        logger.debug("start match handling -------------------------------------------------------------------------------------------------");
        // handle matches
        final List<LigaSyncMatchDTO> matchDTOS = syncPayload.getMatch();
        final List<LigaSyncPasseDTO> passeDTOS = syncPayload.getPasse();
        try {
            synchronizeMatchesAndPassen(matchDTOS, passeDTOS, principal);
        } catch (NoPermissionException e) {
            throw new BusinessException(ErrorCode.UNDEFINED, "error syncing matches and passen");
        }
        logger.debug("done syncing--------------------------------------------------------------------------------------------------------------------");
        // delete token -> done in match sync

    }
}
