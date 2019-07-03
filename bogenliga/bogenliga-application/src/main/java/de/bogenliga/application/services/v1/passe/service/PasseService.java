package de.bogenliga.application.services.v1.passe.service;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
@RestController
@CrossOrigin
@RequestMapping("v1/passen/")
public class PasseService implements ServiceFacade {
    private static final Logger LOG = LoggerFactory.getLogger(PasseService.class);

    private static final String SERVICE_FIND_BY_ID = "findById";

    private final PasseComponent passeComponent;


    /**
     * Constructor with dependency injection
     *
     * @param passeComponent to handle the database CRUD requests
     */
    @Autowired
    public PasseService(final PasseComponent passeComponent) {
        this.passeComponent = passeComponent;
    }


    @RequestMapping(value = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public PasseDTO findById(@PathVariable("id") Long passeId) {
        PasseDTO passeDTO = PasseDTOMapper.toDTO.apply(passeComponent.findById(passeId));
        this.log(passeDTO, SERVICE_FIND_BY_ID);
        return passeDTO;
    }


    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public PasseDTO create(PasseDTO passeDTO) {
        return passeDTO;
    }


    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public PasseDTO update(PasseDTO passeDTO, final Principal principal) {
        final long userId = UserProvider.getCurrentUserId(principal);
        passeComponent.update(PasseDTOMapper.toDO.apply(passeDTO), userId);
        this.log(passeDTO, SERVICE_FIND_BY_ID);
        return passeDTO;
    }


    /**
     * Logs received data when request arrives
     *
     * @param passeDTO
     * @param fromService: name of the service the log came from
     */
    private void log(PasseDTO passeDTO, String fromService) {
        LOG.debug(
                "Received '{}' request for passe with id: '{}', WettkampfID: '{}', LfdNr: '{}', DsbMitgliedId: '{}'," +
                        " SchuetzeNr: '{}', MannschaftId: '{}', MatchId: '{}', MatchNr: '{}'",
                fromService,
                passeDTO.getId(),
                passeDTO.getWettkampfId(),
                passeDTO.getLfdNr(),
                passeDTO.getDsbMitgliedId(),
                passeDTO.getSchuetzeNr(),
                passeDTO.getMatchId(),
                passeDTO.getMatchNr(),
                passeDTO.getMannschaftId()
        );
    }

}
