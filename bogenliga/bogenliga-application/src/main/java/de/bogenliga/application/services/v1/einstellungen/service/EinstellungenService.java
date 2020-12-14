package de.bogenliga.application.services.v1.einstellungen.service;

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
import de.bogenliga.application.business.einstellungen.api.EinstellungenComponent;
import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.einstellungen.mapper.EinstellungenDTOMapper;
import de.bogenliga.application.services.v1.einstellungen.model.EinstellungenDTO;
import de.bogenliga.application.services.v1.feedback.service.FeedbackClassService;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I´m a REST resource and handle einstellungen CRUD requests over the HTTP protocol.
 *
 * @author Lars Bahnmüller, Lars_Herbert.Bahnmueller@Student.Reutlingen-University.DE
 */

@RestController
@CrossOrigin
@RequestMapping("v1/einstellungen")
public class EinstellungenService implements ServiceFacade {

    private final EinstellungenComponent einstellungenComponent;

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackClassService.class);

    /**
     * Create a intance of UserRoleComponent
     */
    @Autowired
    public EinstellungenService(EinstellungenComponent einstellungenComponent) {
        this.einstellungenComponent = einstellungenComponent;
    }


    /**
     * I return all einstellungen entries of the database.
     *
     * @return list of {@link EinstellungenDTO} as JSON
     */
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EinstellungenDTO> findAll() {
        final List<EinstellungenDO> einstellungenDOList = einstellungenComponent.findAll();
        LOGGER.debug("Receive Einstellungen request");
        return einstellungenDOList.stream().map(EinstellungenDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * I return the einstellungen entry of the database with a specific id.
     *
     * @return list of {@link EinstellungenDTO} as JSON
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DSBMITGLIEDER)
    public EinstellungenDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        final EinstellungenDO einstellungenDO = einstellungenComponent.findById(id);
        return EinstellungenDTOMapper.toDTO.apply(einstellungenDO);
    }


    /**
     * I persist a newer version of the einstellungen in the database.
     */
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public EinstellungenDTO update(@RequestBody final EinstellungenDTO einstellungenDTO, final Principal principal) {
        final EinstellungenDO einstellungenDO = EinstellungenDTOMapper.toDO.apply(einstellungenDTO);
        final long userId = UserProvider.getCurrentUserId(principal);
        final EinstellungenDO einstellungenDO1 = einstellungenComponent.update(einstellungenDO, userId);
        return EinstellungenDTOMapper.toDTO.apply(einstellungenDO1);

    }

    /**
     * I persist a new einstellungen and return this einstellungen entry.
     *
     * @param einstellungenDTO of the request body
     * @param principal        authenticated user
     *
     * @return list of {@link EinstellungenDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EinstellungenDTO create(@RequestBody final EinstellungenDTO einstellungenDTO, final Principal principal) {

        LOGGER.debug("Receive 'create' request with id {} ,key {}, value {}",
                einstellungenDTO.getId(),
                einstellungenDTO.getKey(),
                einstellungenDTO.getValue());

        final EinstellungenDO einstellungenDO = EinstellungenDTOMapper.toDO.apply(einstellungenDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final EinstellungenDO saveEinstellungenDO = einstellungenComponent.create(einstellungenDO, userId);

        return EinstellungenDTOMapper.toDTO.apply(saveEinstellungenDO);
    }


    /**
     * I delete an existing einstellungen entry from the database.
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOGGER.debug("Receive 'delete' request with id '{}'", id);

        final EinstellungenDO einstellungenDO = new EinstellungenDO();
        einstellungenDO.setId(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        einstellungenComponent.delete(einstellungenDO, userId);
    }

}
