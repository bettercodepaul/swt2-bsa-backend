package de.bogenliga.application.services.v1.match.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NoPermissionException;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.ligamatch.impl.mapper.LigamatchToMatchMapper;
import de.bogenliga.application.business.ligapasse.impl.entity.LigapasseBE;
import de.bogenliga.application.business.ligapasse.impl.mapper.LigapasseToPasseMapper;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.services.v1.veranstaltung.model.VeranstaltungDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.business.MatchComponentImpl;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.passe.impl.business.PasseComponentImpl;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.match.mapper.MatchDTOMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import de.bogenliga.application.services.v1.wettkampf.mapper.WettkampfDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.services.v1.wettkampftyp.mapper.WettkampfTypDTOMapper;
import de.bogenliga.application.services.v1.wettkampftyp.model.WettkampfTypDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
@RestController
@CrossOrigin
@RequestMapping("v1/match/")
public class MatchService implements ServiceFacade {
    private static final Logger LOG = LoggerFactory.getLogger(MatchService.class);

    private static final String ERR_NOT_NULL_TEMPLATE = "MatchService: %s: %s must not be null.";
    private static final String ERR_NOT_NEGATIVE_TEMPLATE = "MatchService: %s: %s must not be negative.";
    private static final String ERR_EQUAL_TEMPLATE = "MatchService: %s: %s must be equal.";
    private static final String ERR_SIZE_TEMPLATE = "MatchService: %s: %s must have a size of %d.";

    // a simple map mapping DTO's methods to related error messages
    // used in checkPreconditions
    static final Map<String, String> matchConditionErrors = new HashMap<>();

    static {
        matchConditionErrors.put("getBegegnung", MatchComponentImpl.PRECONDITION_MSG_BEGEGNUNG);
        matchConditionErrors.put("getMannschaftId", MatchComponentImpl.PRECONDITION_MSG_MANNSCHAFT_ID);
        matchConditionErrors.put("getMatchScheibennummer", MatchComponentImpl.PRECONDITION_MSG_SCHEIBENNUMMER);
        matchConditionErrors.put("getWettkampfId", MatchComponentImpl.PRECONDITION_MSG_WETTKAMPF_ID);
        matchConditionErrors.put("getNr", MatchComponentImpl.PRECONDITION_MSG_MATCH_NR);
    }

    public static final Map<String, String> passeConditionErrors = new HashMap<>();

    static {
        passeConditionErrors.put("getLfdNr", PasseComponentImpl.PRECONDITION_MSG_LFD_NR);
        passeConditionErrors.put("getMannschaftId", PasseComponentImpl.PRECONDITION_MSG_MANNSCHAFT_ID);
        matchConditionErrors.put("getWettkampfId", PasseComponentImpl.PRECONDITION_MSG_WETTKAMPF_ID);
        passeConditionErrors.put("getMatchNr", PasseComponentImpl.PRECONDITION_MSG_MATCH_NR);
    }

    private static final String SERVICE_FIND_BY_ID = "findById";
    private static final String SERVICE_FIND_MATCHES_BY_IDS = "findMatchesByIds";
    private static final String SERVICE_FIND_BY_MANNSCHAFT_ID = "findByMannschaftId";
    private static final String SERVICE_SAVE_MATCHES = "saveMatches";
    private static final String SERVICE_CREATE = "create";
    private static final String SERVICE_NEXT = "next";
    private static final String SERVICE_UPDATE = "update";

    private static final String CHECKED_PARAM_MATCH_ID = "Match ID";
    private static final String CHECKED_PARAM_MATCH_DTO_LIST = "matchDTOs";
    private static final String CHECKED_PARAM_PRINCIPAL = "principal";

    private static final String PRECONDITION_MSG_VERANSTALTUNGS_ID = "Veranstaltungs-ID must not be null or negative";
    private static final String PRECONDITION_MSG_USER_ID = "Users-ID must not be negative";

    private static final int PLATZHALTER_ID = 99;

    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final WettkampfComponent wettkampfComponent;
    private final WettkampfTypComponent wettkampfTypComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final DsbMannschaftComponent mannschaftComponent;
    private final VereinComponent vereinComponent;
    private final RequiresOnePermissionAspect requiresOnePermissionAspect;
    private final VeranstaltungComponent veranstaltungComponent;

    /**
     * Constructor with dependency injection
     *
     * @param matchComponent          to handle the database CRUD requests
     * @param veranstaltungComponent
     */
    @Autowired
    public MatchService(final MatchComponent matchComponent,
                        final PasseComponent passeComponent,
                        final VereinComponent vereinComponent,
                        final WettkampfComponent wettkampfComponent,
                        final DsbMannschaftComponent mannschaftComponent,
                        final MannschaftsmitgliedComponent mannschaftsmitgliedComponent,
                        final WettkampfTypComponent wettkampftypComponent,
                        final VeranstaltungComponent veranstaltungComponent,
                        RequiresOnePermissionAspect requiresOnePermissionAspect) {
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfComponent = wettkampfComponent;
        this.mannschaftComponent = mannschaftComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.wettkampfTypComponent = wettkampftypComponent;
        this.requiresOnePermissionAspect = requiresOnePermissionAspect;
        this.veranstaltungComponent = veranstaltungComponent;
    }

    /**
     * Finds all matches
     * @return MatchDTO-Liste
     */

    @GetMapping(value = "",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<MatchDTO> findAll(){
        final List<MatchDO> matchDOList = matchComponent.findAll();

        return matchDOList.stream().map(MatchDTOMapper.toDTO).toList();
    }

    /**
     * Finds matche by the given match id
     * @param matchId id des Matches, das gelsenden werden soll
     * @return MatchDTO
     */

    @GetMapping(value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public MatchDTO findById(@PathVariable("id") Long matchId) {
        this.checkMatchId(matchId);

        MatchDTO matchDTO = getMatchFromId(matchId, false);
        this.log(matchDTO, SERVICE_FIND_BY_ID);
        return matchDTO;
    }

    /**
     * Finds all matches by the given id from a wettkampf
     * @param wettkampfid Id des Wettkampfs zu dem gelesen werden soll
     * @return Liste der Matches im Wettkampf
     */
    @GetMapping(value = "findByWettkampfId/wettkampfid={id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<MatchDTO> findByWettkampfId(@PathVariable("id") Long wettkampfid) {
        this.checkMatchId(wettkampfid);

        if(this.checkIfLigamatch(wettkampfid)){
            List<LigamatchBE> wettkampfMatches = matchComponent.getLigamatchesByWettkampfId(wettkampfid);

            final List<MatchDTO> matchDTOs = new ArrayList<>();


            for( LigamatchBE einmatch: wettkampfMatches) {
                MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(LigamatchToMatchMapper.LigamatchToMatchDO.apply(einmatch));
                matchDTO.setMannschaftName(einmatch.getMannschaftName());
                matchDTOs.add(matchDTO);
            }

            return matchDTOs;
        }else{
            List<MatchDO> wettkampfMatches = matchComponent.findByWettkampfId(wettkampfid);

            final List<MatchDTO> matchDTOs = new ArrayList<>();

            for( MatchDO einmatch: wettkampfMatches) {
                MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(einmatch);
                DsbMannschaftDO mannschaftDO = mannschaftComponent.findById(matchDTO.getMannschaftId());
                VereinDO vereinDO = vereinComponent.findById(mannschaftDO.getVereinId());
                matchDTO.setMannschaftName(vereinDO.getName() + '-' + mannschaftDO.getNummer());
                matchDTOs.add(matchDTO);
            }

            return matchDTOs;
            
        }


    }

    /**
     * There are always 2 matches on a schusszettel form
     *
     * @param matchId1 das erste Match auf dem Schusszettel
     * @param matchId2 das zweite Match auf dem Schusszettel
     *
     * @return MatchDTOs zu den IDs (2 Stück)
     */
    @GetMapping(value = "schusszettel/{matchId1}/{matchId2}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public List<MatchDTO> findMatchesByIds(@PathVariable("matchId1") Long matchId1,
                                           @PathVariable("matchId2") Long matchId2) {
        this.checkMatchId(matchId1);
        this.checkMatchId(matchId2);

        MatchDTO matchDTO1 = getMatchFromId(matchId1, true);
        MatchDTO matchDTO2 = getMatchFromId(matchId2, true);

        checkPreconditions(matchDTO1, matchConditionErrors);
        checkPreconditions(matchDTO2, matchConditionErrors);

        List<MatchDTO> matches = new ArrayList<>();
        matches.add(matchDTO1);
        matches.add(matchDTO2);
        this.log(matchDTO1, SERVICE_FIND_MATCHES_BY_IDS);
        this.log(matchDTO2, SERVICE_FIND_MATCHES_BY_IDS);


        return matches;
    }


    /**
     * I return the match entries of the database with the given mannschaftId.
     * <p>
     * Usage:
     * <pre>{@Code Request: GET /v1/match/byMannschaftsId/{id}}</pre>
     * <pre>{@Code Response:
     * [
     *  {
     *      "id": "app.bogenliga.frontend.autorefresh.active",
     *      "value": "true"
     *  },
     *  {
     *      "id": "app.bogenliga.frontend.autorefresh.interval",
     *      "value": 10
     *  }
     * ]
     * }</pre>
     *
     * @param id the given mannschaftId
     *
     * @return list of {@link MatchDTO} as JSON
     */

    @GetMapping(value = "byMannschaftsId/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public List<MatchDTO> findAllByMannschaftId(@PathVariable("id") final Long id) {
        Preconditions.checkNotNull(id,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_FIND_BY_MANNSCHAFT_ID, CHECKED_PARAM_MATCH_ID));
        Preconditions.checkArgument(id >= 0,
                String.format(ERR_NOT_NEGATIVE_TEMPLATE, SERVICE_FIND_BY_MANNSCHAFT_ID, CHECKED_PARAM_MATCH_ID));

        LOG.debug("Receive 'findAllByMannschaftId' request with ID '{}'", id);

        List<MatchDO> matchDOList = matchComponent.findByMannschaftId(id);
        return matchDOList.stream().map(MatchDTOMapper.toDTO).toList();
    }



    /**
     * Save the two edited matches from the findMatchesByIds service.
     * Also save the passe objects in case there are some.
     *
     * @param matchDTOs die abzuspeichernden Matches
     * @param principal User Id der ändernden Users
     *
     * @return Liste der gespeicherten MatchDTOs  (2 Stück)
     */
    @PostMapping(value = "schusszettel",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF,UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public List<MatchDTO> saveMatches(@RequestBody final List<MatchDTO> matchDTOs, final Principal principal) throws NoPermissionException {
        // darf der User die Funktion aufrufen, da er das allgemein Recht hat?
        final Long userId = UserProvider.getCurrentUserId(principal);
        Preconditions.checkArgument(userId >= 0, PRECONDITION_MSG_USER_ID);
        // wir müssen die Prüfung auf die Rechte ein zweites Mal durchführen, da
        // wir für die rechte "_MY_..." die Daten prüfen müssen, d.h. die UserID mit
        // LigaleiterID bzw. AusrichterID vergleichen müssen

        //dazu bestimmen wir den Wettkampf-Datensatz
        WettkampfDO wettkampfDO = this.wettkampfComponent.findById(matchDTOs.get(0).getWettkampfId());
        if (!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_WETTKAMPF)&&
                !this.requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(
                        UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, wettkampfDO.getWettkampfVeranstaltungsId())&&
                !this.requiresOnePermissionAspect.hasSpecificPermissionAusrichter(
                        UserPermission.CAN_MODIFY_MY_WETTKAMPF, wettkampfDO.getWettkampfTypId())) {
            throw new NoPermissionException();
        }

        Preconditions.checkNotNull(matchDTOs,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_SAVE_MATCHES, CHECKED_PARAM_MATCH_DTO_LIST));
        Preconditions.checkArgument(matchDTOs.size() == 2, String.format(
                ERR_SIZE_TEMPLATE, SERVICE_SAVE_MATCHES, CHECKED_PARAM_MATCH_DTO_LIST, 2
        ));
        Preconditions.checkNotNull(principal,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_SAVE_MATCHES, CHECKED_PARAM_PRINCIPAL));

        MatchDTO matchDTO1 = matchDTOs.get(0);
        MatchDTO matchDTO2 = matchDTOs.get(1);

        checkPreconditions(matchDTO1, matchConditionErrors);
        checkPreconditions(matchDTO2, matchConditionErrors);

        // make sure the matches are from the same wettkampf, begegnung and have the same number.
        // checkout the Setzliste example for more information
        Preconditions.checkArgument(matchDTO1.getWettkampfId().equals(matchDTO2.getWettkampfId()),
                String.format(ERR_EQUAL_TEMPLATE, SERVICE_SAVE_MATCHES, "WettkampfId"));
        Preconditions.checkArgument(matchDTO1.getBegegnung().equals(matchDTO2.getBegegnung()),
                String.format(ERR_EQUAL_TEMPLATE, SERVICE_SAVE_MATCHES, "Begegnung"));
        Preconditions.checkArgument(matchDTO1.getNr().equals(matchDTO2.getNr()),
                String.format(ERR_EQUAL_TEMPLATE, SERVICE_SAVE_MATCHES, "Numbers"));

        this.log(matchDTO1, SERVICE_SAVE_MATCHES);
        this.log(matchDTO2, SERVICE_SAVE_MATCHES);

        saveMatch(matchDTO1, userId);
        saveMatch(matchDTO2, userId);

        return matchDTOs;
    }


    /**
     * Save a single match, also creates/updates related passe objects.
     * @param matchDTO das einzelne Match, das zu speichern ist
     * @param userId des ändernden Users
     */
    private void saveMatch(MatchDTO matchDTO, Long userId) {

        MatchDO matchDO = MatchDTOMapper.toDO.apply(matchDTO);


        matchComponent.update(matchDO, userId);
        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOS =
                mannschaftsmitgliedComponent.findAllSchuetzeInTeam(matchDTO.getMannschaftId());

        LOG.debug("Anzahl Schützen: {}", mannschaftsmitgliedDOS.size());
        for (MannschaftsmitgliedDO mmdo : mannschaftsmitgliedDOS) {
            LOG.debug("Schütze: {} mit dsbMitgliedId {}", mmdo.getId(), mmdo.getDsbMitgliedId());
        }

        Preconditions.checkArgument(mannschaftsmitgliedDOS.size() >= 3,
                String.format(ERR_SIZE_TEMPLATE, SERVICE_SAVE_MATCHES, "mannschaftsmitgliedDOS", 3));


        for (PasseDTO passeDTO : matchDTO.getPassen()) {
            createOrUpdatePasse(passeDTO, userId, mannschaftsmitgliedDOS);
        }
    }


    /**
     * Checks whether the given passe object already exists.
     * If so, just update it, if not, create it.
     * @param passeDTO die Passe die zu aktualiseren ist
     * @param userId ändernder User
     * @param mannschaftsmitgliedDOS Liste der Mannschaftsmitglieder
     */
    private void createOrUpdatePasse(PasseDTO passeDTO, Long userId,
                                     List<MannschaftsmitgliedDO> mannschaftsmitgliedDOS){

        checkPreconditions(passeDTO, passeConditionErrors);

        passeDTO.setDsbMitgliedId(getMemberIdFor(passeDTO, mannschaftsmitgliedDOS));

        Preconditions.checkArgument(passeDTO.getDsbMitgliedId() != null,
                String.format(ERR_NOT_NULL_TEMPLATE, "createOrUpdatePasse", "dsbMitgliedId"));

        PasseDO passeDO = PasseDTOMapper.toDO.apply(passeDTO);


        if (passeExists(passeDO)) {
            LOG.debug("updating passe: {}", passeDO.getId());
            passeComponent.update(passeDO, userId);
        } else {
            LOG.debug("Trying to create passe {}", passeDO.getId());
            // erst prüfen ob alle relevanten Parameter befüllt sind pk-passe!!
            if(passeDO.getPasseDsbMitgliedId()!=null &&
                    passeDO.getPasseMannschaftId()!= null &&
                    passeDO.getPasseWettkampfId()!= null&&
                    passeDO.getPasseMatchNr() !=null &&
                    passeDO.getPasseLfdnr() !=null) {
                List<PasseDO> passen=passeComponent.findByWettkampfIdAndMitgliedId(passeDO.getPasseWettkampfId(),passeDO.getPasseDsbMitgliedId());
                if(passen.isEmpty()){
                    MannschaftsmitgliedDO mitglied=mannschaftsmitgliedComponent.findByMemberAndTeamId(passeDO.getPasseMannschaftId(),passeDO.getPasseDsbMitgliedId());

                    mitglied.setDsbMitgliedEingesetzt(mitglied.getDsbMitgliedEingesetzt()+1);

                    mannschaftsmitgliedComponent.update(mitglied,userId);
                }
                passeComponent.create(passeDO, userId);
            }
        }
    }


    /**
     * There's a mapping from nr->id like:
     * schuetzeNr. 1 -> mmgId: 125152,
     * schuetzeNr. 2 -> mmgId: 125153,
     * schuetzeNr. 3 -> mmgId: 125154
     * etc..
     *
     * @param passeDTO passe für die die Mannschaftsmitlgider zu identifizieren sind
     * @param mannschaftsmitgliedDOS liste in der die die Rückennummern zuzuordnen sind
     *
     * @return DsbMitgliedsID der Mannschaftsmitglieds
     */
    public static Long getMemberIdFor(PasseDTO passeDTO, List<MannschaftsmitgliedDO> mannschaftsmitgliedDOS) {

        Preconditions.checkNotNull(mannschaftsmitgliedDOS,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_SAVE_MATCHES, "mannschaftsmitgliedDOS"));
        Preconditions.checkNotNull(passeDTO,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_SAVE_MATCHES, "passeDTO"));
        Preconditions.checkNotNull(passeDTO.getRueckennummer(),
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_SAVE_MATCHES, "schuetzeNr"));

        MannschaftsmitgliedDO mannschaftsmitglied = new MannschaftsmitgliedDO(-1L);

        for (MannschaftsmitgliedDO item : mannschaftsmitgliedDOS){

            if (item.getRueckennummer() == (int)passeDTO.getRueckennummer()){
                mannschaftsmitglied = item;
                break;
            }
        }

        Preconditions.checkArgument(mannschaftsmitglied.getId() != -1L,
                String.format(ERR_NOT_NULL_TEMPLATE, "getMemberIdFor", "mannschaftsmitglied"));

        return mannschaftsmitglied.getDsbMitgliedId();

    }


    /**
     * Sucht nach dem zweiten Match einer Begegnung - das erste wird als Parameter mitgegeben
     * in der Liste aller Matches des Wettkampftages, der zugehörigen andren Scheibe und identischer Begegnungs-nr.
     * @param matchId Match zu welchem der Partner gesucht wird
     *
     * @return Liste der zusammengehörigen Matches
     */
    @GetMapping(value = "{matchId}/pair",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public List<Long> pair(@PathVariable final Long matchId) {

        Preconditions.checkNotNull(matchId,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_NEXT, CHECKED_PARAM_MATCH_ID));

        MatchDO matchDO = matchComponent.findById(matchId);
        this.log(MatchDTOMapper.toDTO.apply(matchDO), SERVICE_CREATE);

        List<MatchDO> wettkampfMatches = matchComponent.findByWettkampfId(matchDO.getWettkampfId());
        Long scheibeNr = matchDO.getMatchScheibennummer();
        // Scheibennummern: [(1,2),(3,4),(5,6),(7,8)]
        // Die gruppierten Nummern bilden eine Begegnung aus 2 Matches, die hier ermittelt werden
        // ist das gegebene match an Scheibe nr 2 -> andere Scheibe ist nr 1, und andersherum, daher das überprüfen auf gerade/ungerade
        Long otherScheibeNr = scheibeNr % 2 == 0 ? scheibeNr - 1 : scheibeNr + 1;
        return wettkampfMatches
                .stream()
                .filter(mDO -> mDO.getNr().equals(matchDO.getNr()))
                .filter(mDO -> (
                        scheibeNr.equals(mDO.getMatchScheibennummer())
                                || otherScheibeNr.equals(mDO.getMatchScheibennummer())
                ))
                .sorted(Comparator.comparing(MatchDO::getMatchScheibennummer))
                .map(MatchDO::getId)
                .toList();
    }

    /**
     * Sucht nach dem zweiten Match einer Begegnung - das erste wird als Parameter mitgegeben
     * in der Liste aller Matches des Wettkampftages, der zugehörigen andren Scheibe und identischer Begegnungs-nr.
     * @param matchId das zugehörige Match
     *
     * @return eine Liste der zusammengehörigen Matches
     * List (MatchDO) oder null wenn es im gleichen Wettkampf das letzte Match ist.
     */
    @GetMapping(value = "{matchId}/pairToFollow",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public List<Long> pairToFollow(@PathVariable final Long matchId) {
        Preconditions.checkNotNull(matchId,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_NEXT, CHECKED_PARAM_MATCH_ID));
        MatchDO matchDO = matchComponent.findById(matchId);

        this.log(MatchDTOMapper.toDTO.apply(matchDO), SERVICE_CREATE);
        long scheibeNr;
        if(matchDO.getMatchScheibennummer() <7 ){
            //wir suchen im gleichen Match die nächste Scheiben-Paarung (+2)
            scheibeNr = matchDO.getMatchScheibennummer() +2;
        }
        else if(matchDO.getMatchScheibennummer() >=7 && matchDO.getNr() <7){
            //wir sind noch nicht am Ende des Wettkampfs angekommen
            scheibeNr = 1L;
            matchDO.setNr(matchDO.getNr()+1);
        }
        else {
            //Ende - wir geben eine leere Liste zurück
            return Collections.emptyList();
        }

        List<MatchDO> wettkampfMatches = matchComponent.findByWettkampfId(matchDO.getWettkampfId());
        // Scheibennummern: [(1,2),(3,4),(5,6),(7,8)]
        // Die gruppierten Nummern bilden eine Begegnung aus 2 Matches, die hier ermittelt werden
        // ist das gegebene match an Scheibe nr 2 -> andere Scheibe ist nr 1, und andersherum, daher das überprüfen auf gerade/ungerade
        final Long ersteScheibe = scheibeNr;
        final Long otherScheibeNr = scheibeNr % 2 == 0 ? scheibeNr - 1 : scheibeNr + 1;
        return wettkampfMatches
                .stream()
                .filter(mDO -> mDO.getNr().equals(matchDO.getNr()))
                .filter(mDO -> (
                        ersteScheibe.equals(mDO.getMatchScheibennummer())
                                || otherScheibeNr.equals(mDO.getMatchScheibennummer())
                ))
                .sorted(Comparator.comparing(MatchDO::getMatchScheibennummer))
                .map(MatchDO::getId)
                .toList();
    }


    @GetMapping(value = "{matchId}/previousPair",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public List<Long> previousPair(@PathVariable final Long matchId) {
        Preconditions.checkNotNull(matchId,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_NEXT, CHECKED_PARAM_MATCH_ID));
        MatchDO matchDO = matchComponent.findById(matchId);

        this.log(MatchDTOMapper.toDTO.apply(matchDO), SERVICE_CREATE);
        long scheibeNr;
        if(matchDO.getMatchScheibennummer() > 2){
            //wir suchen im gleichen Match die vorherige Scheiben-Paarung (-2)
            scheibeNr = matchDO.getMatchScheibennummer() - 2;
        }
        else if(matchDO.getMatchScheibennummer() <= 2 && matchDO.getNr() > 1){
            //wir sind noch nicht am Anfang des Wettkampfs angekommen
            scheibeNr = 8L;
            matchDO.setNr(matchDO.getNr() - 1);
        }
        else {
            //am Anfang des Wettkampfs angekommen, eine leere Liste wird zurückgegeben

            return Collections.emptyList();
        }

        List<MatchDO> wettkampfMatches = matchComponent.findByWettkampfId(matchDO.getWettkampfId());
        // Scheibennummern: [(1,2),(3,4),(5,6),(7,8)]
        // Die gruppierten Nummern bilden eine Begegnung aus 2 Matches, die hier ermittelt werden
        // ist das gegebene match an Scheibe nr 2 -> andere Scheibe ist nr 1, und andersherum, daher das überprüfen auf gerade/ungerade
        final Long ersteScheibe = scheibeNr;
        final Long otherScheibeNr = scheibeNr % 2 == 0 ? scheibeNr - 1 : scheibeNr + 1;
        return wettkampfMatches
                .stream()
                .filter(mDO -> mDO.getNr().equals(matchDO.getNr()))
                .filter(mDO -> (
                        ersteScheibe.equals(mDO.getMatchScheibennummer())
                                || otherScheibeNr.equals(mDO.getMatchScheibennummer())
                ))
                .sorted(Comparator.comparing(MatchDO::getMatchScheibennummer))
                .map(MatchDO::getId)
                .toList();
    }



    /**
     * Update-Method changes the chosen match entry in the Database
     *
     * @param matchDTO Match das upzudaten ist
     * @param principal ändernden user
     *
     * @return MatchDTO
     */
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF,UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public MatchDTO update(@RequestBody final MatchDTO matchDTO, final Principal principal) throws NoPermissionException {
        final Long userId = UserProvider.getCurrentUserId(principal);
        Preconditions.checkArgument(userId >= 0, PRECONDITION_MSG_USER_ID);
        //wir müssen die Prüfung auf die Rechte ein zweites Mal durchführen, da
        // wir für die rechte "_MY_..." die Datenprüfen müssen, d.h. die UserID mit
        // LigaleiterID bzw. AusrichterID vergleichen müssen

        //dazu bestimmen wir den Wettkampf-Datensatz
        WettkampfDO wettkampfDO = this.wettkampfComponent.findById(matchDTO.getWettkampfId());
        if (!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_WETTKAMPF)&&
                !this.requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(
                        UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, wettkampfDO.getWettkampfVeranstaltungsId())&&
                !this.requiresOnePermissionAspect.hasSpecificPermissionAusrichter(
                        UserPermission.CAN_MODIFY_MY_WETTKAMPF, wettkampfDO.getWettkampfTypId())) {
            //keines der Rechte besitzt der user - wir machen nicht weiter
            throw new NoPermissionException();
        }
        Preconditions.checkNotNull(principal,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_UPDATE, CHECKED_PARAM_PRINCIPAL));
        checkPreconditions(matchDTO, matchConditionErrors);

        this.log(matchDTO, SERVICE_UPDATE);

        final MatchDO matchDO = MatchDTOMapper.toDO.apply(matchDTO);

        final MatchDO updatedMatchDO = matchComponent.update(matchDO, userId);
        return MatchDTOMapper.toDTO.apply(updatedMatchDO);
    }


    /**
     * Return the ids of the next two matches happening after the given match on a single Wettkampftag.
     *
     * @param matchId zu welchem die beiden folgenden Matches gesucht werden
     *
     * @return Liste der MatchIDs
     */
    @GetMapping(value = "{matchId}/next",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public List<Long> next(@PathVariable final Long matchId) {
        Preconditions.checkNotNull(matchId,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_NEXT, CHECKED_PARAM_MATCH_ID));
        MatchDO matchDO = matchComponent.findById(matchId);

        List<MatchDO> wettkampfMatches = matchComponent.findByWettkampfId(matchDO.getWettkampfId());
        Long scheibeNr = matchDO.getMatchScheibennummer();
        // Scheibennummern: [(1,2),(3,4),(5,6),(7,8)]
        // Die gruppierten Nummern bilden eine Begegnung aus 2 Matches, die hier ermittelt werden
        // ist das gegebene match an Scheibe nr 2 -> andere Scheibe ist nr 1, und andersherum, daher das überprüfen auf gerade/ungerade
        Long otherScheibeNr = scheibeNr % 2 == 0 ? scheibeNr - 1 : scheibeNr + 1;
        return wettkampfMatches
                .stream()
                .filter(mDO -> mDO.getNr() == matchDO.getNr() + 1)
                .filter(mDO -> (
                        scheibeNr.equals(mDO.getMatchScheibennummer())
                                || otherScheibeNr.equals(mDO.getMatchScheibennummer())
                ))
                .sorted(Comparator.comparing(MatchDO::getMatchScheibennummer))
                .map(MatchDO::getId)
                .toList();
    }


    /**
     * create-Method() writes a new entry of match into the database
     *
     * @param matchDTO anzulegenden Match
     *
     * @return MatchDTO
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF,UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public MatchDTO create(@RequestBody final MatchDTO matchDTO, final Principal principal) throws NoPermissionException {
        final Long userId = UserProvider.getCurrentUserId(principal);
        Preconditions.checkArgument(userId >= 0, PRECONDITION_MSG_USER_ID);
        //wir müssen die Prüfung auf die Rechte ein zweites Mal durchführen, da
        // wir für die rechte "_MY_..." die Datenprüfen müssen, d.h. die UserID mit
        // LigaleiterID bzw. AusrichterID vergleichen müssen

        //dazu bestimmen wir den Wettkampf-Datensatz
        WettkampfDO wettkampfDO = this.wettkampfComponent.findById(matchDTO.getWettkampfId());
        if (!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_WETTKAMPF)&&
                !this.requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(
                        UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, wettkampfDO.getWettkampfVeranstaltungsId())&&
                !this.requiresOnePermissionAspect.hasSpecificPermissionAusrichter(
                        UserPermission.CAN_MODIFY_MY_WETTKAMPF, wettkampfDO.getId())) {
            //keines der Rechte besitzt der user
            throw new NoPermissionException();
        }
        Preconditions.checkNotNull(principal,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_CREATE, CHECKED_PARAM_PRINCIPAL));
        checkPreconditions(matchDTO, matchConditionErrors);

        this.log(matchDTO, SERVICE_CREATE);

        try {
            // Check if the Mannschaft of this match is a Platzhalter
            DsbMannschaftDO checkForVereinsID =  mannschaftComponent.findById(matchDTO.getMannschaftId());
            if(checkForVereinsID.getVereinId() == PLATZHALTER_ID) {
                matchDTO.setMatchpunkte(0L);
                matchDTO.setSatzpunkte(0L);
            }
        }catch (NullPointerException ignored) {}


        final MatchDO newMatch = MatchDTOMapper.toDO.apply(matchDTO);

        final MatchDO savedNewMatch = matchComponent.create(newMatch, userId);
        return MatchDTOMapper.toDTO.apply(savedNewMatch);
    }

    /**
     *
     *
     * @param veranstaltungDTO mehrere intiale Matches  für den WT0 anlegen
     *
     * @return VeranstaltungDTO
     */
    @PostMapping(value = "WT0",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF,UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public VeranstaltungDTO createInitialMatchesWT0(@RequestBody final VeranstaltungDTO veranstaltungDTO, final Principal principal) {
        Preconditions.checkNotNull(principal,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_CREATE, CHECKED_PARAM_PRINCIPAL));
        Preconditions.checkNotNull(veranstaltungDTO,PRECONDITION_MSG_VERANSTALTUNGS_ID);
        Preconditions.checkNotNull(veranstaltungDTO.getId(),PRECONDITION_MSG_VERANSTALTUNGS_ID);
        Preconditions.checkArgument(veranstaltungDTO.getId() >= 0,PRECONDITION_MSG_VERANSTALTUNGS_ID);

        final long userId = UserProvider.getCurrentUserId(principal);

        matchComponent.createInitialMatchesWT0(veranstaltungDTO.getId(), userId);
        veranstaltungComponent.setPhase(veranstaltungDTO.getId(),"Laufend" , userId);
        return veranstaltungDTO;
    }



    /**
     * A generic way to validate the getter results of each dto method.
     *
     * @param dto daten die übergeben und geprüft werden
     */
    public static void checkPreconditions(final DataTransferObject dto, final Map<String, String> conditionErrors) {
        Preconditions.checkNotNull(dto, String.format(ERR_NOT_NULL_TEMPLATE, "checkPreconditions", "matchDTO"));
        Method[] methods = dto.getClass().getDeclaredMethods();
        for (Method m : methods) {
            String methodName = m.getName();
            if (methodName.startsWith("get")) {
                String errMsg = conditionErrors.get(methodName);
                if (errMsg != null) {
                    try {
                        Object returnValue = m.invoke(dto);
                        Preconditions.checkNotNull(returnValue, errMsg);
                        Preconditions.checkArgument(((Long) returnValue) >= 0, errMsg);
                    } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                        LOG.debug(
                                "Couldn't check precondition on object {} for method {}",
                                dto.getClass().getSimpleName(),
                                methodName
                        );
                    }
                }
            }
        }
    }


    /**
     * Gets and enriches a match from the DB.
     * Control the presence of related passe objects with the addPassen parameter.
     * Each passe object then gets its RueckenNr.
     * @param matchId Match ID, die gelesen werden soll
     * @param addPassen True: dann werden auch die Daten der Passen mit gelesen
     * @return MatchDTO
     */

    private MatchDTO getMatchFromId(Long matchId, boolean addPassen) {

        // the match is shown on the Schusszettel, add passen and mannschaft name from the views ligamatch and ligapasse
        // if the match is not shown on the Schusszettel, enrich matchDTO with data from the tables
        if (addPassen) {
            final LigamatchBE ligamatchBE = matchComponent.getLigamatchById(matchId);
            MatchDO matchDO = LigamatchToMatchMapper.LigamatchToMatchDO.apply(ligamatchBE);
            MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDO);
            matchDTO.setWettkampfTyp(ligamatchBE.getWettkampftypId());
            matchDTO.setWettkampfTag(ligamatchBE.getWettkampfTag());
            matchDTO.setMannschaftName(ligamatchBE.getMannschaftName());


            List<LigapasseBE> ligapasseBEList = passeComponent.getLigapassenByLigamatchId(matchId);


            List<PasseDO> passeDOs = ligapasseBEList.stream().map(LigapasseToPasseMapper.ligapasseToPasseDO).toList();
            List<PasseDTO> passeDTOs = passeDOs.stream().map(PasseDTOMapper.toDTO).toList();


            for (int i = 0; i < passeDTOs.size(); i++){
                passeDTOs.get(i).setRueckennummer(ligapasseBEList.get(i).getMannschaftsmitgliedRueckennummer());
                Preconditions.checkArgument(passeDTOs.get(i).getDsbMitgliedId() != null,
                        String.format(ERR_NOT_NULL_TEMPLATE, "getMatchFromId", "dsbMitgliedId"));
            }



            matchDTO.setPassen(passeDTOs);
            return matchDTO;
        }else{
            final MatchDO matchDo = matchComponent.findById(matchId);
            final WettkampfDTO wettkampfDTO = WettkampfDTOMapper.toDTO.apply(
                    wettkampfComponent.findById(matchDo.getWettkampfId()));
            MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDo);
            WettkampfTypDO wettDO = wettkampfTypComponent.findById(wettkampfDTO.getWettkampfTypId());
            final WettkampfTypDTO wettkampfTypDTO = WettkampfTypDTOMapper.toDTO.apply(wettDO);
            matchDTO.setWettkampfTyp(wettkampfTypDTO.getName());
            matchDTO.setWettkampfTag(wettkampfDTO.getWettkampfTag());
            return matchDTO;
        }


    }




    private void checkMatchId(Long matchId) {
        Preconditions.checkNotNull(matchId,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_FIND_MATCHES_BY_IDS, CHECKED_PARAM_MATCH_ID));
        Preconditions.checkArgument(matchId >= 0,
                String.format(ERR_NOT_NEGATIVE_TEMPLATE, SERVICE_FIND_MATCHES_BY_IDS, CHECKED_PARAM_MATCH_ID));
    }

    private boolean checkIfLigamatch(Long id){
        return matchComponent.checkIfLigamatch(id);
    }


    private boolean passeExists(PasseDO passeDO) {
        boolean exists = false;
        if (passeDO.getId() != null) { // Is passeDO id already null
            // If no, check if it actually exists in DB
            PasseDO queriedPasseDO = passeComponent.findById(passeDO.getId());
            LOG.debug("got passe ------------------------------------------------------------------- ");
            exists = queriedPasseDO != null;
        }
        return exists;
    }


    /**
     * Logs received data when request arrives
     *
     * @param matchDTO Match das ins log geschriben werden soll
     * @param fromService: name of the service the log came from
     */
    private void log(MatchDTO matchDTO, String fromService) {
        LOG.debug(
                "Received '{}' request for match with id: '{}', WettkampfID: '{}', Begegnung: '{}', MannschaftId: '{}'," +
                        " ScheibenNummer: '{}', Satzpunkte: '{}', Matchpunkte: '{}'",
                fromService,
                matchDTO.getId(),
                matchDTO.getWettkampfId(),
                matchDTO.getBegegnung(),
                matchDTO.getMannschaftId(),
                matchDTO.getMatchScheibennummer(),
                matchDTO.getSatzpunkte(),
                matchDTO.getMatchpunkte()
        );
    }
 }
