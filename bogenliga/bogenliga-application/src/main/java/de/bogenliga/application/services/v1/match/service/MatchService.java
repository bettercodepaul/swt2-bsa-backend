package de.bogenliga.application.services.v1.match.service;

import java.security.Principal;
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


    @RequestMapping(value = "schusszettel/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public MatchDTO findById(@PathVariable("id") Long matchId) {
        Preconditions.checkArgument(matchId >= 0, "Match ID must not be negative.");
        Preconditions.checkNotNull(matchId, "Match ID must not be null.");

        LOG.debug("Receive 'findById' request with ID '{}'", matchId);

        final MatchDO matchDo = matchComponent.findById(matchId);
        MatchDTO matchDTO = MatchDTOMapper.toDTO.apply(matchDo);

        List<PasseDO> passeDOs = passeComponent.findByMatchId(matchId);
        List<PasseDTO> passen = passeDOs.stream().map(PasseDTOMapper.toDTO).collect(Collectors.toList());
        matchDTO.setPassen(passen);
        return matchDTO;
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


    private void checkPreconditions(@RequestBody final MatchDTO matchDTO) {
        Preconditions.checkArgument(matchDTO.getBegegnung() >= 0, MatchComponentImpl.PRECONDITION_MSG_BEGEGNUNG);
        Preconditions.checkNotNull(matchDTO.getBegegnung(), MatchComponentImpl.PRECONDITION_MSG_BEGEGNUNG);

        Preconditions.checkArgument(matchDTO.getId() >= 0, MatchComponentImpl.PRECONDITION_MSG_MATCH_ID);
        Preconditions.checkNotNull(matchDTO.getId(), MatchComponentImpl.PRECONDITION_MSG_MATCH_ID);

        Preconditions.checkArgument(matchDTO.getMannschaftId() >= 0, MatchComponentImpl.PRECONDITION_MSG_MANNSCHAFT_ID);
        Preconditions.checkNotNull(matchDTO.getMannschaftId(), MatchComponentImpl.PRECONDITION_MSG_MANNSCHAFT_ID);

        Preconditions.checkArgument(matchDTO.getMatchpunkte() >= 0, MatchComponentImpl.PRECONDITION_MSG_MATCHPUNKTE);
        Preconditions.checkNotNull(matchDTO.getMatchpunkte(), MatchComponentImpl.PRECONDITION_MSG_MATCHPUNKTE);

        Preconditions.checkArgument(matchDTO.getSatzpunkte() >= 0, MatchComponentImpl.PRECONDITION_MSG_SATZPUNKTE);
        Preconditions.checkNotNull(matchDTO.getSatzpunkte(), MatchComponentImpl.PRECONDITION_MSG_SATZPUNKTE);

        Preconditions.checkArgument(matchDTO.getScheibenNummer() >= 0,
                MatchComponentImpl.PRECONDITION_MSG_SCHEIBENNUMMER);
        Preconditions.checkNotNull(matchDTO.getScheibenNummer(), MatchComponentImpl.PRECONDITION_MSG_SCHEIBENNUMMER);

        Preconditions.checkArgument(matchDTO.getWettkampfId() >= 0, MatchComponentImpl.PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkNotNull(matchDTO.getWettkampfId(), MatchComponentImpl.PRECONDITION_MSG_WETTKAMPF_ID);

        Preconditions.checkArgument(matchDTO.getNr() >= 0, MatchComponentImpl.PRECONDITION_MSG_MATCH_NR);
        Preconditions.checkNotNull(matchDTO.getNr(), MatchComponentImpl.PRECONDITION_MSG_MATCH_NR);
    }

}
