package de.bogenliga.application.services.v1.match.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.business.MatchComponentImpl;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
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
@RequestMapping("v1/match")
public class MatchService implements ServiceFacade {
    private static final Logger LOG = LoggerFactory.getLogger(MatchService.class);

    private static final String ERR_NOT_NULL_TEMPLATE = "MatchService: %s: %s must not be null.";
    private static final String ERR_NOT_NEGATIVE_TEMPLATE = "MatchService: %s: %s must not be negative.";
    private static final String ERR_EQUAL_TEMPLATE = "MatchService: %s: %s must be equal.";

    // a simple map mapping the dtos methods to related error messages
    // used in checkPreconditions
    private static final HashMap<String, String> conditionErrors = new HashMap<>();
    static {
        conditionErrors.put("getBegegnung", MatchComponentImpl.PRECONDITION_MSG_BEGEGNUNG);
        conditionErrors.put("getId", MatchComponentImpl.PRECONDITION_MSG_MATCH_ID);
        conditionErrors.put("getMannschaftId", MatchComponentImpl.PRECONDITION_MSG_MANNSCHAFT_ID);
        conditionErrors.put("getMatchpunkte", MatchComponentImpl.PRECONDITION_MSG_MATCHPUNKTE);
        conditionErrors.put("getSatzpunkte", MatchComponentImpl.PRECONDITION_MSG_SATZPUNKTE);
        conditionErrors.put("getScheibenNummer", MatchComponentImpl.PRECONDITION_MSG_SCHEIBENNUMMER);
        conditionErrors.put("getWettkampfId", MatchComponentImpl.PRECONDITION_MSG_WETTKAMPF_ID);
        conditionErrors.put("getNr", MatchComponentImpl.PRECONDITION_MSG_MATCH_NR);
    }

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


    /**
     * A generic way to validate the getter results of each matchDTO method.
     *
     * @param matchDTO
     */
    public static void checkPreconditions(final MatchDTO matchDTO) {
        Method[] methods = matchDTO.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("get")) {
                String errMsg = conditionErrors.get(m.getName());
                if (errMsg != null) {
                    try {
                        Preconditions.checkArgument(((Long) m.invoke(matchDTO)) >= 0, errMsg);
                        Preconditions.checkNotNull(m.invoke(matchDTO), errMsg);
                    } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
                        LOG.debug(
                                "Couldn't check precondition on object {} for method {}",
                                matchDTO.getClass().getSimpleName(),
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


    @RequestMapping(value = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public MatchDTO findById(@PathVariable("id") Long matchId) {
        Preconditions.checkArgument(matchId >= 0, String.format(ERR_NOT_NEGATIVE_TEMPLATE, "findById", "Match ID"));
        Preconditions.checkNotNull(matchId, String.format(ERR_NOT_NULL_TEMPLATE, "findById", "Match ID"));

        MatchDTO matchDTO = getMatchFromId(matchId, false);
        this.log(matchDTO, "findById");
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
    @RequestMapping(value = "schusszettel/{idm1}/{idm2}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public List<MatchDTO> findMatchesByIds(@PathVariable("idm1") Long matchId1, @PathVariable("idm2") Long matchId2) {
        Preconditions.checkArgument(matchId1 >= 0,
                String.format(ERR_NOT_NEGATIVE_TEMPLATE, "findMatchesByIds", "Match ID"));
        Preconditions.checkNotNull(matchId1, String.format(ERR_NOT_NULL_TEMPLATE, "findMatchesByIds", "Match ID"));

        Preconditions.checkArgument(matchId2 >= 0,
                String.format(ERR_NOT_NEGATIVE_TEMPLATE, "findMatchesByIds", "Match ID"));
        Preconditions.checkNotNull(matchId2, String.format(ERR_NOT_NULL_TEMPLATE, "findMatchesByIds", "Match ID"));

        MatchDTO matchDTO1 = getMatchFromId(matchId1, true);
        MatchDTO matchDTO2 = getMatchFromId(matchId2, true);

        Preconditions.checkNotNull(matchDTO1, String.format(ERR_NOT_NULL_TEMPLATE, "saveMatches", "MatchDTO1"));
        Preconditions.checkNotNull(matchDTO2, String.format(ERR_NOT_NULL_TEMPLATE, "saveMatches", "MatchDTO2"));
        checkPreconditions(matchDTO1);
        checkPreconditions(matchDTO2);

        List<MatchDTO> matches = new ArrayList<>();
        matches.add(matchDTO1);
        matches.add(matchDTO2);

        this.log(matchDTO1, "findMatchesByIds");
        this.log(matchDTO2, "findMatchesByIds");

        return matches;
    }


    /**
     * @param matchDTO1
     * @param matchDTO2
     *
     * @return
     */
    @RequestMapping(value = "schusszettel",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public List<MatchDTO> saveMatches(@RequestBody final MatchDTO matchDTO1, @RequestBody final MatchDTO matchDTO2,
                                      final Principal principal) {
        Preconditions.checkNotNull(matchDTO1, String.format(ERR_NOT_NULL_TEMPLATE, "saveMatches", "MatchDTO1"));
        Preconditions.checkNotNull(matchDTO2, String.format(ERR_NOT_NULL_TEMPLATE, "saveMatches", "MatchDTO2"));
        checkPreconditions(matchDTO1);
        checkPreconditions(matchDTO2);

        Preconditions.checkArgument(matchDTO1.getWettkampfId().equals(matchDTO2.getWettkampfId()),
                String.format(ERR_EQUAL_TEMPLATE, "saveMatches", "WettkampfId"));
        Preconditions.checkArgument(matchDTO1.getBegegnung().equals(matchDTO2.getBegegnung()),
                String.format(ERR_EQUAL_TEMPLATE, "saveMatches", "Begegnung"));
        Preconditions.checkArgument(matchDTO1.getNr().equals(matchDTO2.getNr()),
                String.format(ERR_EQUAL_TEMPLATE, "saveMatches", "Numbers"));

        this.log(matchDTO1, "saveMatches");
        this.log(matchDTO2, "saveMatches");

        final long userId = UserProvider.getCurrentUserId(principal);

        List<MatchDTO> matches = new ArrayList<>();
        matches.add(matchDTO1);
        matches.add(matchDTO2);

        for (MatchDTO matchDTO : matches) {
            MatchDO matchDO = MatchDTOMapper.toDO.apply(matchDTO);
            matchComponent.update(matchDO, userId);
            for (PasseDTO passeDTO : matchDTO.getPassen()) {
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


    private boolean passeExists(PasseDO passeDO) {
        PasseDO existingPasseDO = passeComponent.findByPk(passeDO.getPasseWettkampfId(), passeDO.getPasseMatchNr(),
                passeDO.getPasseMannschaftId(), passeDO.getPasseLfdnr(), passeDO.getPasseDsbMitgliedId());
        return existingPasseDO != null;
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
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public MatchDTO create(@RequestBody final MatchDTO matchDTO, final Principal principal) {
        checkPreconditions(matchDTO);

        this.log(matchDTO, "create");

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
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public MatchDTO update(@RequestBody final MatchDTO matchDTO, final Principal principal) {
        checkPreconditions(matchDTO);

        this.log(matchDTO, "update");

        final MatchDO matchDO = MatchDTOMapper.toDO.apply(matchDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final MatchDO updatedMatchDO = matchComponent.update(matchDO, userId);
        return MatchDTOMapper.toDTO.apply(updatedMatchDO);
    }


    /**
     * Logs received data when request arrives
     *
     * @param matchDTO
     * @param logType: type of log: delete, create, update, ...
     */
    private void log(MatchDTO matchDTO, String logType) {
        LOG.debug(
                "Received '{}' request for match with id: '{}', WettkampfID: '{}', Begegnung: '{}', MannschaftId: '{}'," +
                        " ScheibenNummer: '{}', Satzpunkte: '{}', Matchpunkte: '{}'",
                logType,
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
