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
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.services.v1.match.service.MatchService;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncLigatabelleDTOMapper;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncPasseDTOMapper;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncMannschaftsmitgliedDTOMapper;
import de.bogenliga.application.services.v1.sync.mapper.WettkampfExtDTOMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncLigatabelleDTO;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncMatchDTOMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMannschaftsmitgliedDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncPasseDTO;
import de.bogenliga.application.services.v1.sync.model.WettkampfExtDTO;
import de.bogenliga.application.services.v1.wettkampf.mapper.WettkampfDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.naming.NoPermissionException;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;

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

    private final Logger logger = LoggerFactory.getLogger(
            de.bogenliga.application.services.v1.sync.service.SyncService.class);

    private final LigatabelleComponent ligatabelleComponent;
    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final WettkampfComponent wettkampfComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final RequiresOnePermissionAspect requiresOnePermissionAspect;
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
                       final WettkampfComponent wettkampfComponent,
                       RequiresOnePermissionAspect requiresOnePermissionAspect,
                       final MatchService matchService) {
        this.ligatabelleComponent = ligatabelleComponent;
        this.matchComponent = matchComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.passeComponent = passeComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.requiresOnePermissionAspect = requiresOnePermissionAspect;
        this.matchService = matchService;
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


    /* TODO
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
    @PutMapping(
            value = "wettkampf/{id}/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF})
    public WettkampfExtDTO update(@PathVariable("id") final long wettkampfId, @RequestBody final WettkampfDTO wettkampfDTO,
                                  final Principal principal) {
        Preconditions.checkArgument(wettkampfDTO.getId() >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        logger.debug("Received 'update' request with id '{}' to add offline token", wettkampfDTO.getId());
        // Check if it is the ligaleiter of the wettkampfs liga (not sure if possible); generic check done in permissions

        // prevent going offline for a wettkampf that is already offline
        // as getCurrentUserId does not work, it does so categorically: any wettkampf that is offline, can not be accessed.
        // once it works, we could allow the same user that went offline before to send a second token to cover
        // the edge case of and offline token being created and saved but not received by the frontend
        // TODO use Kathrins wettkampfComponent.checkOfflineToken() Function here; evtl so anpassen dass wenn nicht null token returned

        // create token in business layer and persist it + return to frontend
        final long userId = UserProvider.getCurrentUserId(principal);
        String offlineToken = wettkampfComponent.generateOfflineToken(userId);

        final WettkampfDO wettkampfDO = WettkampfDTOMapper.toDO.apply(wettkampfDTO);
        wettkampfDO.setOfflineToken(offlineToken);
        final WettkampfDO updatedWettkampfDO = wettkampfComponent.update(wettkampfDO, userId);

        return WettkampfExtDTOMapper.toDTO.apply(updatedWettkampfDO);
    }


    /* TODO
     * I will recieve the OfflineToken from Client
     * and a list of new Mannschaftmitglieder (identified by missing IDs)
     * the follwing checks will be performed:
     * - are all new Manschaftsmitglieder existing in Backend -> otherwise one Error per missing entry
     *   including Rückennummern und Name der Mannschaft
     * - is the Offline identical to the stored in Backend Wettkampf Table -> otherwise this Dataset is not
     *   the most recent one --> Gero sould advised what to do.
     * @return ok or list of errors
     */

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
    //@RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF,UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public ResponseEntity synchronizeMatchesAndPassen(List<LigaSyncMatchDTO> ligaSyncMatchDTOs,
                                                      List<LigaSyncPasseDTO> ligaSyncPasseDTOs,
                                                      Principal principal) throws NoPermissionException {

        // Check for valid Offline-Token

        // No need to check for VersionID, already checked in Frontend OfflineDB
        // Only Matches with new VersionID will be sent

        final Long userId = UserProvider.getCurrentUserId(principal);

        List<MatchDTO> matchDTOs = new ArrayList<>();


        // Map Matches and Passen
        // Connect Passen to Matches
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

            // Set begegnung and WettkampfTag in MatchDTO
            matchDTO.setBegegnung(begegnung);
            matchDTO.setWettkampfTag(wettkampfDO.getWettkampfTag());

            // Map LigaSyncPasseDTO to PasseDTO
            // Set List<PasseDTO> to MatchDTO where MatchId is the same
            matchDTO.setPassen(ligaSyncPasseDTOs.stream().map(LigaSyncPasseDTOMapper.toPasseDTO)
                    .filter(passeDTO -> passeDTO.getMatchId().equals(ligasyncmatchDTO.getId()))
                    .collect(Collectors.toList()));

            matchDTOs.add(matchDTO);
        }


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

        // Set Offline Token to null
        WettkampfDO wettkampfDO = wettkampfComponent.findById(matchDTOs.get(0).getWettkampfId());
        wettkampfComponent.deleteOfflineToken(wettkampfDO, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
