package de.bogenliga.application.services.v1.passe.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.match.service.MatchService;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
@RestController
@CrossOrigin
@RequestMapping("v1/passen")
public class PasseService implements ServiceFacade {
    private static final Logger LOG = LoggerFactory.getLogger(PasseService.class);

    private static final String SERVICE_FIND_BY_ID = "findById";
    private static final String SERVICE_CREATE = "create";
    private static final String SERVICE_UPDATE = "update";

    private final PasseComponent passeComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;


    /**
     * Constructor with dependency injection
     *
     * @param passeComponent to handle the database CRUD requests
     */
    @Autowired
    public PasseService(final PasseComponent passeComponent,
                        final MannschaftsmitgliedComponent mannschaftsmitgliedComponent) {
        this.passeComponent = passeComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
    }


    @GetMapping(value = "",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<PasseDTO> findAll(){
        final List<PasseDO> passeDOList = passeComponent.findAll();

        return passeDOList.stream().map(PasseDTOMapper.toDTO).toList();
    }



    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public PasseDTO findById(@PathVariable("id") Long passeId) {
        PasseDTO passeDTO = PasseDTOMapper.toDTO.apply(passeComponent.findById(passeId));
        this.log(passeDTO, SERVICE_FIND_BY_ID);
        return passeDTO;
    }

    @GetMapping(value = "findByMatchId/matchid={matchId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public List<PasseDTO> findByMatchId(@PathVariable("matchId") final long matchId) {
        Preconditions.checkArgument(matchId >= 0, "matchId must not be negative");

        LOG.debug("Received 'findByMatchId' request with matchId: '{}'", matchId);

        final List<PasseDO> passeDOList = this.passeComponent.findByMatchId(matchId);
        return passeDOList.stream().map(PasseDTOMapper.toDTO).toList();
    }

    @GetMapping(value = "findByWettkampfId/wettkampfid={wettkampfId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<PasseDTO> findByWettkampfId(@PathVariable("wettkampfId") final long wettkampfId) {
        Preconditions.checkArgument(wettkampfId>= 0, "wettkampfId must not be negative");

        LOG.debug("Received 'findByWettkampfId' request with wettkampfId: '{}'", wettkampfId);

        final List<PasseDO> passeDOList = this.passeComponent.findByWettkampfId(wettkampfId);
        return passeDOList.stream().map(PasseDTOMapper.toDTO).toList();
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_WETTKAMPF)
    public PasseDTO create(@RequestBody final PasseDTO passeDTO, final Principal principal) {
        MatchService.checkPreconditions(passeDTO, MatchService.passeConditionErrors);

        List<MannschaftsmitgliedDO> mannschaftsmitgliedDOS =
                mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(passeDTO.getMannschaftId());

        passeDTO.setDsbMitgliedId(MatchService.getMemberIdFor(passeDTO, mannschaftsmitgliedDOS));
        final long userId = UserProvider.getCurrentUserId(principal);
        PasseDO passeDO = passeComponent.create(PasseDTOMapper.toDO.apply(passeDTO), userId);
        this.log(passeDTO, SERVICE_CREATE);
        return PasseDTOMapper.toDTO.apply(passeDO);
    }


    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_WETTKAMPF)
    public PasseDTO update(@RequestBody final PasseDTO passeDTO, final Principal principal) {
        MatchService.checkPreconditions(passeDTO, MatchService.passeConditionErrors);

        final long userId = UserProvider.getCurrentUserId(principal);
        PasseDO passeDO = passeComponent.update(PasseDTOMapper.toDO.apply(passeDTO), userId);
        this.log(passeDTO, SERVICE_UPDATE);
        return PasseDTOMapper.toDTO.apply(passeDO);
    }

    @GetMapping(path = "byWettkampfIdAndDsbMitgliedId/{wettkampfId}/{dsbMitgliedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public List<PasseDTO> findAllByWettkampfIdAndDsbMitgliedId(@PathVariable("wettkampfId") final long wettkampfId,
                                                            @PathVariable("dsbMitgliedId") final long dsbMitgliedId) {
        Preconditions.checkArgument(wettkampfId >= 0, "wettkampfId must not be negative");
        Preconditions.checkArgument(dsbMitgliedId >= 0, "dsbMitgliedId must not be negative");

        LOG.debug("Received 'findAllByWettkampfIdAndDsbMitgliedId' request with WettkampfId: '{}' and DsbMitgliedId: '{}'", wettkampfId, dsbMitgliedId);

        final List<PasseDO> passeDOList = this.passeComponent.findByWettkampfIdAndMitgliedId(wettkampfId, dsbMitgliedId);
        return passeDOList.stream().map(PasseDTOMapper.toDTO).toList();

    }


    /**
     * Logs received data when request arrives
     *
     * @param passeDTO
     * @param fromService: name of the service the log came from
     */
    private void log(PasseDTO passeDTO, String fromService) {
        LOG.debug(
                "Received '{}' request for passe with ID: '{}', WettkampfID: '{}', LfdNr: '{}', DsbMitgliedID: '{}'," +
                        " SchuetzeNr: '{}', MannschaftId: '{}', MatchID: '{}', MatchNr: '{}'",
                fromService,
                passeDTO.getId(),
                passeDTO.getWettkampfId(),
                passeDTO.getLfdNr(),
                passeDTO.getDsbMitgliedId(),
                passeDTO.getRueckennummer(),
                passeDTO.getMatchId(),
                passeDTO.getMatchNr(),
                passeDTO.getMannschaftId()
        );
    }
}
