package de.bogenliga.application.services.v1.sync.service;

import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.ligamatch.impl.mapper.LigamatchToMatchMapper;
import de.bogenliga.application.business.ligatabelle.api.LigatabelleComponent;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;

import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.business.MatchComponentImpl;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.match.impl.mapper.MatchMapper;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.match.mapper.MatchDTOMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncLigatabelleDTOMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncLigatabelleDTO;
import de.bogenliga.application.services.v1.sync.mapper.LigaSyncMatchDTOMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final MatchComponentImpl matchComponentImpl;
    private final MatchComponent matchComponent;

    /**
     * Constructor with dependency injection
     *
     * @param ligatabelleComponent to handle the database access
     */
    @Autowired
    public SyncService(final LigatabelleComponent ligatabelleComponent,
                        final MatchComponentImpl matchComponentImpl,
                       final MatchComponent matchComponent) {
        this.ligatabelleComponent = ligatabelleComponent;
        this.matchComponentImpl = matchComponentImpl;
        this.matchComponent = matchComponent;
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
            value = "findByWettkampfIdOffline/wettkampfid={id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LigaSyncMatchDTO> findByWettkampfId(@PathVariable("id") final long wettkampfid) {
        //getLigaMatchesByWettkampfId
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        this.checkMatchId(wettkampfid);
        logger.debug("Receive 'Wettkampfid for Matches' request with ID '{}'", wettkampfid);

        //List<MatchDO> wettkampfMatches = matchComponentImpl.findByWettkampfId(id);

        //final List<LigatabelleDO> ligatabelleDOList = matchComponentImpl.getLigatabelleVeranstaltung(wettkampfid);

        List<LigamatchBE> wettkampfMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfid);

        final List<MatchDO> matchDOs = new ArrayList<>();

        for( LigamatchBE einmatch: wettkampfMatches) {
            MatchDO matchDO = LigamatchToMatchMapper.LigamatchToMatchDO.apply(einmatch);
            matchDOs.add(matchDO);
        }

        return matchDOs.stream().map(LigaSyncMatchDTOMapper.toDTO).collect(Collectors.toList());
    }

    private void checkMatchId(Long matchId) {
        Preconditions.checkNotNull(matchId,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_FIND_MATCHES_BY_IDS, CHECKED_PARAM_MATCH_ID));
        Preconditions.checkArgument(matchId >= 0,
                String.format(ERR_NOT_NEGATIVE_TEMPLATE, SERVICE_FIND_MATCHES_BY_IDS, CHECKED_PARAM_MATCH_ID));
    }

    /*
    @Override
    public List<MatchDO> findByWettkampfId(Long wettkampfId) {
        checkPreconditions(wettkampfId, PRECONDITION_MSG_WETTKAMPF_ID);

        final List<MatchBE> matchBEList = matchDAO.findByWettkampfId(wettkampfId);
        return matchBEList.stream().map(MatchMapper.toMatchDO).collect(Collectors.toList());
    }
    @Override
    public List<MatchDO> findByWettkampfId(Long wettkampfId) {
        checkPreconditions(wettkampfId, PRECONDITION_MSG_WETTKAMPF_ID);

        final List<MatchBE> matchBEList = matchDAO.findByWettkampfId(wettkampfId);
        return matchBEList.stream().map(MatchMapper.toMatchDO).collect(Collectors.toList());
    }
    */

    /* TODO
     * I return the all Passe Entries from "ligapasse"-Table for
     * a "wettkampftid (tag)" entries of the database.
     *
     * list<ligapasseDO> ligapassecomponent.findById(wetkkmapfID)
     * @return list of {@link LigaSyncPasseDTO} as JSON
     */

    /* TODO
     * I return the all Mannschaftsmitglieder Entries for all Mannschaften
     * participating a "wettkampftid (tag)" entries of the database.
     *
     * Aufruf von WettkampfComponent -> getAllowedMitglieder(wettkampfID)
     * @return list of {@link LigaSyncMannschaftsmitgliedDTO} as JSON
     */

    /* TODO
     * I will update the dataset of a single Wettkampf and set the OfflineToken
     *
     * @return WettkampfExtDTO as JSON
     */

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