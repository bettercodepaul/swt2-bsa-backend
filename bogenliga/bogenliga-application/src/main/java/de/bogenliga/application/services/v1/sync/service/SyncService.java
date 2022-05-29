package de.bogenliga.application.services.v1.sync.service;

import de.bogenliga.application.business.liga.impl.mapper.LigaMapper;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.ligamatch.impl.mapper.LigamatchToMatchMapper;
import de.bogenliga.application.business.ligapasse.impl.entity.LigapasseBE;
import de.bogenliga.application.business.ligapasse.impl.mapper.LigapasseToPasseMapper;
import de.bogenliga.application.business.ligatabelle.api.LigatabelleComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;

import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncLigatabelleDTOMapper;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncPasseDTOMapper;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncMannschaftsmitgliedDTOMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncLigatabelleDTO;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncMatchDTOMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMannschaftsmitgliedDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncPasseDTO;
import de.bogenliga.application.services.v1.sync.model.WettkampfExtDTO;
import de.bogenliga.application.services.v1.wettkampf.mapper.WettkampfDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.naming.NoPermissionException;

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
    private static final String CHECKED_PARAM_MATCH_ID = "Match ID";
    private static final String ERR_NOT_NEGATIVE_TEMPLATE = "MatchService: %s: %s must not be negative.";

    private final Logger logger = LoggerFactory.getLogger(de.bogenliga.application.services.v1.sync.service.SyncService.class);

    private final LigatabelleComponent ligatabelleComponent;
    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final WettkampfComponent wettkampfComponent;

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
                       final WettkampfComponent wettkampfComponent) {
        this.ligatabelleComponent = ligatabelleComponent;
        this.matchComponent = matchComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.passeComponent = passeComponent;
        this.wettkampfComponent = wettkampfComponent;
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

        List<LigamatchBE> wettkampfMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfid);

        List<LigaSyncMatchDTO> ligaSyncMatchDTOList = new ArrayList<LigaSyncMatchDTO>() {};

        for( LigamatchBE currentLigamatchBE: wettkampfMatches) {
            MatchDO matchDO = LigamatchToMatchMapper.LigamatchToMatchDO.apply(currentLigamatchBE);
            LigaSyncMatchDTO ligaSyncMatchDTO = LigaSyncMatchDTOMapper.apply(matchDO);
            ligaSyncMatchDTO.setMannschaftName(currentLigamatchBE.getMannschaftName());
            ligaSyncMatchDTO.setNaechsteMatchId(currentLigamatchBE.getNaechsteMatchId());
            ligaSyncMatchDTO.setNaechsteNaechsteMatchNrMatchId(currentLigamatchBE.getNaechsteNaechsteMatchId());

            LigamatchBE gegnerLigaMatchBE = getGegnerLigaMatchBE(matchDO,  wettkampfMatches);

            if(gegnerLigaMatchBE != null &&
                    ligaSyncMatchDTO.getMannschaftName() != null &&
                    !ligaSyncMatchDTO.getMannschaftName().equals(gegnerLigaMatchBE.getMannschaftName()) &&
                    (ligaSyncMatchDTO.getId() != null && !ligaSyncMatchDTO.getId().equals(gegnerLigaMatchBE.getMatchId())) &&
                    gegnerLigaMatchBE.getScheibennummer() != null &&
                    ligaSyncMatchDTO.getMatchScheibennummer() != Math.toIntExact(gegnerLigaMatchBE.getScheibennummer())){
                ligaSyncMatchDTO.setNameGegner(gegnerLigaMatchBE.getMannschaftName());
                ligaSyncMatchDTO.setMatchIdGegner(gegnerLigaMatchBE.getMatchId());
                ligaSyncMatchDTO.setScheibennummerGegner(Math.toIntExact(gegnerLigaMatchBE.getScheibennummer()));
            }
            ligaSyncMatchDTOList.add(ligaSyncMatchDTO);
        }
        return ligaSyncMatchDTOList;
    }

    private LigamatchBE getGegnerLigaMatchBE(MatchDO matchDO, List<LigamatchBE> wettkampfMatches) {
        List<LigamatchBE> ligaGegnerMatchBEList = wettkampfMatches.stream().
                filter(t -> t.getMatchNr() != null && t.getMatchNr().equals(matchDO.getNr()) &&
                        t.getBegegnung() != null && t.getBegegnung().equals(matchDO.getBegegnung()) &&
                        t.getScheibennummer() != null && !t.getScheibennummer().equals(matchDO.getScheibenNummer()))
                .collect(Collectors.toList());

        LigamatchBE gegnerLigaMatchBE = null;

        if (ligaGegnerMatchBEList != null && ligaGegnerMatchBEList.size() == 1 && ligaGegnerMatchBEList.get(
                0) != null) {
            gegnerLigaMatchBE = ligaGegnerMatchBEList.get(0);
        }
        return gegnerLigaMatchBE;
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
     * I return the all Passe Entries from "ligapasse"-Table for a "wettkampftid (tag)" entries of the database.
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

        //List<LigapasseBE> wettkampfPassen = passeComponent.getLigapassenByLigamatchId(wettkampfid);
        List<PasseDO> wettkampfPassenDO = passeComponent.findByWettkampfId(wettkampfid);

        List<LigaSyncPasseDTO> ligaSyncPasseDTOs = new ArrayList<>();
        for(PasseDO currentPasseDO: wettkampfPassenDO){
            //PasseDO passeDO = LigapasseToPasseMapper.ligapasseToPasseDO.apply(currentPasseDO);
            LigaSyncPasseDTO ligaSyncPasseDTO = LigaSyncPasseDTOMapper.toDTO.apply(currentPasseDO);
            ligaSyncPasseDTOs.add(ligaSyncPasseDTO);
            //ligaSyncPasseDTO.setDsbMigliedName(currentLigapasseBE.getDsbMitgliedName());
            //ligaSyncPasseDTO.setRueckennummer(currentLigapasseBE.getMannschaftsmitgliedRueckennummer());
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
    public List<LigaSyncMannschaftsmitgliedDTO> getMannschaftsmitgliedernOffline(@PathVariable("id") final long wettkampfId) {

        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        logger.debug("Receive 'Mannschaftsmitgliedern für Wettkampf' request with WettkampfID '{}'", wettkampfId);

        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDOList = new ArrayList<>();

        for(int i=1; i <= 8; i++) {
            MatchDO matchDO = matchComponent.findByWettkampfIDMatchNrScheibenNr(wettkampfId, 1L, (long) i);
            mannschaftsmitgliedDOList.addAll(mannschaftsmitgliedComponent.findSchuetzenInUebergelegenerLiga(matchDO.getMannschaftId(), wettkampfId));
        }

        return mannschaftsmitgliedDOList.stream().map(LigaSyncMannschaftsmitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * I will update the dataset of a single Wettkampf and set the OfflineToken
     * Token is generated with current user's id + timestamp
     * only a ligaleiter can go offline
     * @return WettkampfExtDTO as JSON
     * @author Jonas Sigloch, SWT SoSe 2022
     */
    @PutMapping(
            value = "offlinewettkampf",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF})
    public WettkampfExtDTO update(@RequestBody final WettkampfDTO wettkampfDTO,
                                  final Principal principal) throws NoPermissionException {

        Preconditions.checkArgument(wettkampfDTO.getId() >= 0, PRECONDITION_MSG_WETTKAMPF_ID);

        logger.debug("Received 'update' request with id '{}' to add offline token", wettkampfDTO.getId());
        // No extra permission check needed as Ausrichter is not supposed to be able to use offline function

        // TODO: create token (BL), create wettkampfextdo and save it in db + return it
        // create token in business layer and persist it + return to frontend
        long userId = UserProvider.getCurrentUserId(principal);
        String offlineToken = wettkampfComponent.generateOfflineToken(userId);

        //
        final WettkampfDO newWettkampfDO = WettkampfDTOMapper.toDO.apply(wettkampfDTO);

        final WettkampfDO updatedWettkampfDO = wettkampfComponent.update(newWettkampfDO, userId);

        //return WettkampfExtDTOMapper.toDTO.apply(updatedWettkampfDO);
        return new WettkampfExtDTO();
    }

        /* TODO
     * I will recieve the OfflineToken form Client
     * and a list of new Mannschaftmitglieder (identified by missing IDs)
     * the follwing checks will be performed:
     * - are all new Manschaftsmitglieder existing in Backend -> otherwise one Error per missing entry
     *   including Rückennummern und Name der Mannschaft
     * - is the Offline identical to the stored in Backend Wettkampf Table -> otherwise this Dataset is not
     *   the most recent one --> Gero sould advised what to do.
     * @return ok or list of errors
     */

    /* TODO
     * I will recieve both lists: matches and passen to store data consistantly in a single transaction
     * when data successfully stored, the offlinetoken in wettkampf table is to be removed
     * @return ok or list of errors
     * in case of ok - client should delete offline data
     */

}
