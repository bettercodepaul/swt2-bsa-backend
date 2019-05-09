package de.bogenliga.application.services.v1.match.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.passe.impl.business.PasseComponentImpl;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.business.MatchComponentImpl;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.match.mapper.MatchDTOMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
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
        matchConditionErrors.put("getScheibenNummer", MatchComponentImpl.PRECONDITION_MSG_SCHEIBENNUMMER);
        matchConditionErrors.put("getWettkampfId", MatchComponentImpl.PRECONDITION_MSG_WETTKAMPF_ID);
        matchConditionErrors.put("getNr", MatchComponentImpl.PRECONDITION_MSG_MATCH_NR);
    }

    private static final Map<String, String> passeConditionErrors = new HashMap<>();

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
    private static final String SERVICE_UPDATE = "update";

    private static final String CHECKED_PARAM_MATCH_ID = "Match ID";
    private static final String CHECKED_PARAM_MATCH_DTO_LIST = "matchDTOs";
    private static final String CHECKED_PARAM_PRINCIPAL = "principal";


    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final DsbMannschaftComponent mannschaftComponent;
    private final VereinComponent vereinComponent;


    /**
     * Constructor with dependency injection
     *
     * @param matchComponent to handle the database CRUD requests
     */
    @Autowired
    public MatchService(final MatchComponent matchComponent,
                        final PasseComponent passeComponent,
                        final VereinComponent vereinComponent,
                        final DsbMannschaftComponent mannschaftComponent,
                        final MannschaftsmitgliedComponent mannschaftsmitgliedComponent) {
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
        this.vereinComponent = vereinComponent;
        this.mannschaftComponent = mannschaftComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
    }


    @RequestMapping(value = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public MatchDTO findById(@PathVariable("id") Long matchId) {
        this.checkMatchId(matchId);

        MatchDTO matchDTO = getMatchFromId(matchId, false);
        this.log(matchDTO, SERVICE_FIND_BY_ID);
        return matchDTO;
    }


    /**
     * There are always 2 matches on a schusszettel form
     *
     * @param matchId1
     * @param matchId2
     *
     * @return
     */
    @RequestMapping(value = "schusszettel/{matchId1}/{matchId2}",
            method = RequestMethod.GET,
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
     *
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
     * @param id the given mannschaftId
     * @return list of {@link MatchDTO} as JSON
     */

    @RequestMapping(value = "byMannschaftsId/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public List<MatchDTO> findAllByMannschaftId(@PathVariable("id") final Long id) {
        Preconditions.checkArgument(id >= 0, String.format(ERR_NOT_NEGATIVE_TEMPLATE, SERVICE_FIND_BY_MANNSCHAFT_ID, CHECKED_PARAM_MATCH_ID));
        Preconditions.checkNotNull(id, String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_FIND_BY_MANNSCHAFT_ID, CHECKED_PARAM_MATCH_ID));

        LOG.debug("Receive 'findAllByMannschaftId' request with ID '{}'", id);

        List <MatchDO> matchDOList = matchComponent.findByMannschaftId(id);
        return matchDOList.stream().map(MatchDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * Save the two edited matches from the findMatchesByIds service. Also save the passe objects in case there are
     * some.
     *
     * @param matchDTOs
     *
     * @return
     */
    @RequestMapping(value = "schusszettel",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_WETTKAMPF)
    public List<MatchDTO> saveMatches(@RequestBody final List<MatchDTO> matchDTOs, final Principal principal) {
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

        final long userId = UserProvider.getCurrentUserId(principal);

        List<MatchDTO> matches = new ArrayList<>();
        matches.add(matchDTO1);
        matches.add(matchDTO2);

        saveMatch(matchDTO1, userId);
        saveMatch(matchDTO2, userId);

        return matches;
    }


    private void saveMatch(MatchDTO matchDTO, Long userId) {
        MatchDO matchDO = MatchDTOMapper.toDO.apply(matchDTO);
        matchComponent.update(matchDO, userId);
        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOS =
                mannschaftsmitgliedComponent.findAllSchuetzeInTeam(matchDTO.getMannschaftId());

        LOG.debug("Anzahl Schützen: {}", mannschaftsmitgliedDOS.size());
        for (MannschaftsmitgliedDO mmdo: mannschaftsmitgliedDOS) {
            LOG.debug("Schütze: {} mit dsbMitgliedId {}", mmdo.getId(), mmdo.getDsbMitgliedId());
        }

        Preconditions.checkArgument(mannschaftsmitgliedDOS.size() >= 3,
                String.format(ERR_SIZE_TEMPLATE, SERVICE_SAVE_MATCHES, "mannschaftsmitgliedDOS", 3));

        for (PasseDTO passeDTO : matchDTO.getPassen()) {
            createOrUpdatePasse(passeDTO, userId, mannschaftsmitgliedDOS);
        }
    }


    private void createOrUpdatePasse(PasseDTO passeDTO, Long userId,
                                     List<MannschaftsmitgliedDO> mannschaftsmitgliedDOS) {
        checkPreconditions(passeDTO, passeConditionErrors);
        passeDTO.setDsbMitgliedId(getMemberIdFor(passeDTO, mannschaftsmitgliedDOS));

        Preconditions.checkArgument(passeDTO.getDsbMitgliedId() != null,
                String.format(ERR_NOT_NULL_TEMPLATE, "createOrUpdatePasse", "dsbMitgliedId"));

        PasseDO passeDO = PasseDTOMapper.toDO.apply(passeDTO);
        if (passeExists(passeDO)) {
            passeComponent.update(passeDO, userId);
        } else {
            passeComponent.create(passeDO, userId);
        }
    }


    /**
     * There's a mapping from nr->id like:
     * schuetzeNr. 1 -> mmgId: 125152,
     * schuetzeNr. 2 -> mmgId: 125153,
     * schuetzeNr. 3 -> mmgId: 125154 etc..
     *
     * @param passeDTO
     * @param mannschaftsmitgliedDOS
     *
     * @return
     */
    private Long getMemberIdFor(PasseDTO passeDTO, List<MannschaftsmitgliedDO> mannschaftsmitgliedDOS) {
        Preconditions.checkNotNull(passeDTO.getSchuetzeNr(),
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_SAVE_MATCHES, "schuetzeNr"));
        // -1 as this is an index, not the human readable number
        return mannschaftsmitgliedDOS.get(passeDTO.getSchuetzeNr() - 1).getDsbMitgliedId();
    }

    private Integer getSchuetzeNrFor(PasseDTO passeDTO, List<MannschaftsmitgliedDO> mannschaftsmitgliedDOS) {
        int idx = 0;
        Integer schuetzeNr = 0;
        for (MannschaftsmitgliedDO mmdo: mannschaftsmitgliedDOS) {
            if (mmdo.getDsbMitgliedId().equals(passeDTO.getDsbMitgliedId())) {
                schuetzeNr = idx + 1;
            }
            idx++;
        }
        return schuetzeNr;
    }


    /**
     * create-Method() writes a new entry of match into the database
     *
     * @param matchDTO
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_WETTKAMPF)
    public MatchDTO create(@RequestBody final MatchDTO matchDTO, final Principal principal) {
        Preconditions.checkNotNull(principal,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_CREATE, CHECKED_PARAM_PRINCIPAL));
        checkPreconditions(matchDTO, matchConditionErrors);

        this.log(matchDTO, SERVICE_CREATE);

        final MatchDO newMatch = MatchDTOMapper.toDO.apply(matchDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final MatchDO savedNewMatch = matchComponent.create(newMatch, userId);
        return MatchDTOMapper.toDTO.apply(savedNewMatch);
    }


    /**
     * Update-Method changes the chosen match entry in the Database
     *
     * @param matchDTO
     * @param principal
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_WETTKAMPF)
    public MatchDTO update(@RequestBody final MatchDTO matchDTO, final Principal principal) {
        Preconditions.checkNotNull(principal,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_UPDATE, CHECKED_PARAM_PRINCIPAL));
        checkPreconditions(matchDTO, matchConditionErrors);

        this.log(matchDTO, SERVICE_UPDATE);

        final MatchDO matchDO = MatchDTOMapper.toDO.apply(matchDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final MatchDO updatedMatchDO = matchComponent.update(matchDO, userId);
        return MatchDTOMapper.toDTO.apply(updatedMatchDO);
    }


    /**
     * A generic way to validate the getter results of each dto method.
     *
     * @param dto
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


    private MatchDTO getMatchFromId(Long matchId, boolean addPassen) {
        final MatchDO matchDo = matchComponent.findById(matchId);
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDo);

        // the match is shown on the Schusszettel, add passen and mannschaft name
        if (addPassen) {
            DsbMannschaftDO mannschaftDO = mannschaftComponent.findById(matchDTO.getMannschaftId());
            VereinDO vereinDO = vereinComponent.findById(mannschaftDO.getVereinId());
            matchDTO.setMannschaftName(vereinDO.getName());

            List<PasseDO> passeDOs = passeComponent.findByMatchId(matchId);
            List<PasseDTO> passeDTOs = passeDOs.stream().map(PasseDTOMapper.toDTO).collect(Collectors.toList());

            // reverse map the schuetzeNr to the passeDTO
            List<MannschaftsmitgliedDO> mannschaftsmitgliedDOS =
                    mannschaftsmitgliedComponent.findAllSchuetzeInTeam(matchDTO.getMannschaftId());
            for (PasseDTO passeDTO: passeDTOs) {
                passeDTO.setSchuetzeNr(getSchuetzeNrFor(passeDTO, mannschaftsmitgliedDOS));
                Preconditions.checkArgument(passeDTO.getDsbMitgliedId() != null,
                        String.format(ERR_NOT_NULL_TEMPLATE, "getMatchFromId", "dsbMitgliedId"));
            }

            matchDTO.setPassen(passeDTOs);
        }

        return matchDTO;
    }


    private void checkMatchId(Long matchId) {
        Preconditions.checkNotNull(matchId,
                String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_FIND_MATCHES_BY_IDS, CHECKED_PARAM_MATCH_ID));
        Preconditions.checkArgument(matchId >= 0,
                String.format(ERR_NOT_NEGATIVE_TEMPLATE, SERVICE_FIND_MATCHES_BY_IDS, CHECKED_PARAM_MATCH_ID));
    }


    private boolean passeExists(PasseDO passeDO) {
        return passeDO.getId() != null;
    }


    /**
     * Logs received data when request arrives
     *
     * @param matchDTO
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
                matchDTO.getScheibenNummer(),
                matchDTO.getSatzpunkte(),
                matchDTO.getMatchpunkte()
        );
    }

}
