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
import de.bogenliga.application.business.Passe.api.PasseComponent;
import de.bogenliga.application.business.Passe.api.types.PasseDO;
import de.bogenliga.application.business.Passe.impl.business.PasseComponentImpl;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.business.MatchComponentImpl;
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
        passeConditionErrors.put("getDsbMitgliedId", PasseComponentImpl.PRECONDITION_MSG_DSB_MITGLIED_ID);
        passeConditionErrors.put("getMatchNr", PasseComponentImpl.PRECONDITION_MSG_MATCH_NR);
    }

    private static final String SERVICE_FIND_BY_ID = "findById";
    private static final String SERVICE_FIND_MATCHES_BY_IDS = "findMatchesByIds";
    private static final String SERVICE_SAVE_MATCHES = "saveMatches";
    private static final String SERVICE_CREATE = "create";
    private static final String SERVICE_UPDATE = "update";

    private static final String CHECKED_PARAM_MATCH_ID = "Match ID";
    private static final String CHECKED_PARAM_MATCH_DTO_LIST = "matchDTOs";
    private static final String CHECKED_PARAM_PRINCIPAL = "principal";


    private final MatchComponent matchComponent;
    private final PasseComponent passeComponent;

    /**
     * Constructor with dependency injection
     *
     * @param matchComponent to handle the database CRUD requests
     */
    @Autowired
    public MatchService(final MatchComponent matchComponent, final PasseComponent passeComponent) {
        this.matchComponent = matchComponent;
        this.passeComponent = passeComponent;
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
    public List<MatchDTO> findMatchesByIds(@PathVariable("matchId1") Long matchId1, @PathVariable("matchId2") Long matchId2) {
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
     * Save the two edited matches from the findMatchesByIds service.
     * Also save the passe objects in case there are some.
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
        Preconditions.checkNotNull(matchDTOs, String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_SAVE_MATCHES, CHECKED_PARAM_MATCH_DTO_LIST));
        Preconditions.checkArgument(matchDTOs.size() == 2, String.format(
                ERR_SIZE_TEMPLATE, SERVICE_SAVE_MATCHES, CHECKED_PARAM_MATCH_DTO_LIST, 2
        ));
        Preconditions.checkNotNull(principal, String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_SAVE_MATCHES, CHECKED_PARAM_PRINCIPAL));

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

        for (MatchDTO matchDTO : matches) {
            MatchDO matchDO = MatchDTOMapper.toDO.apply(matchDTO);
            matchComponent.update(matchDO, userId);
            for (PasseDTO passeDTO : matchDTO.getPassen()) {
                checkPreconditions(passeDTO, passeConditionErrors);
                PasseDO passeDO = PasseDTOMapper.toDO.apply(passeDTO);
                if (passeExists(passeDO)) {
                    passeComponent.update(passeDO, userId);
                } else {
                    passeComponent.create(passeDO, userId);
                }
            }
        }

        return matches;
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
        Preconditions.checkNotNull(principal, String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_CREATE, CHECKED_PARAM_PRINCIPAL));
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
        Preconditions.checkNotNull(principal, String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_UPDATE, CHECKED_PARAM_PRINCIPAL));
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
            if (m.getName().startsWith("get")) {
                String errMsg = conditionErrors.get(m.getName());
                if (errMsg != null) {
                    try {
                        Object returnValue = m.invoke(dto);
                        Preconditions.checkNotNull(returnValue, errMsg);
                        Preconditions.checkArgument(((Long) returnValue) >= 0, errMsg);
                    } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                        LOG.debug(
                                "Couldn't check precondition on object {} for method {}",
                                dto.getClass().getSimpleName(),
                                m.getName()
                        );
                    }
                }
            }
        }
    }


    private MatchDTO getMatchFromId(Long matchId, boolean addPassen) {
        final MatchDO matchDo = matchComponent.findById(matchId);
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDo);

        if (addPassen) {
            List<PasseDO> passeDOs = passeComponent.findByMatchId(matchId);
            List<PasseDTO> passeDTOs = passeDOs.stream().map(PasseDTOMapper.toDTO).collect(Collectors.toList());
            matchDTO.setPassen(passeDTOs);
        }

        return matchDTO;
    }


    private void checkMatchId (Long matchId) {
        Preconditions.checkNotNull(matchId, String.format(ERR_NOT_NULL_TEMPLATE, SERVICE_FIND_MATCHES_BY_IDS, CHECKED_PARAM_MATCH_ID));
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
