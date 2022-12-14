package de.bogenliga.application.services.v1.disziplin.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.disziplin.mapper.DisziplinDTOMapper;
import de.bogenliga.application.services.v1.disziplin.model.DisziplinDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I´m a REST resource and handle Disziplin CRUD requests over the HTTP protocol.
 *
 * @author Nico Keilig
 */
@RestController
@RequestMapping("v1/disziplin")
public class DisziplinService implements ServiceFacade {

    private static final Logger LOG = LoggerFactory.getLogger(DisziplinService.class);

    private final DisziplinComponent disziplinComponent;


    @Autowired
    public DisziplinService(final DisziplinComponent disziplinComponent) {
        this.disziplinComponent = disziplinComponent;
    }


    /**
     * I return all Disziplin entries of the database.
     *
     * @return list of {@link DisziplinDTO} as JSON
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public List<DisziplinDTO> findAll() {
        final List<DisziplinDO> disziplinDOList = disziplinComponent.findAll();
        LOG.debug("Received Disziplin request");
        return disziplinDOList.stream().map(DisziplinDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * I return the Disziplin entry of the database with a specific id.
     *
     * @return list of {@link DisziplinDTO} as JSON
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public DisziplinDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with id '{}'", id);

        final DisziplinDO disziplinDO = disziplinComponent.findById(id);
        return DisziplinDTOMapper.toDTO.apply(disziplinDO);
    }


    /**
     * I persist a new Disziplin and return this Disziplin entry.

     * @param disziplinDTO of the request body
     * @param principal authenticated user
     * @return list of {@link DisziplinDTO} as JSON
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public DisziplinDTO create(@RequestBody final DisziplinDTO disziplinDTO, final Principal principal) {
        Preconditions.checkNotNull(disziplinDTO, "DisziplinDTO must not null");
        Preconditions.checkArgument(disziplinDTO.getDisziplinId() >= 0, "DisziplinDTO id must not be negative");
        Preconditions.checkNotNullOrEmpty(disziplinDTO.getDisziplinName(), "DisziplinDTO name must not null or empty");

        final DisziplinDO newDisziplinDO = DisziplinDTOMapper.toDO.apply(disziplinDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final DisziplinDO savedDisziplinDO = disziplinComponent.create(newDisziplinDO, userId);
        return DisziplinDTOMapper.toDTO.apply(savedDisziplinDO);
    }

    /**
     * I persist a newer version of the Disziplin in the database.
     *
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public DisziplinDTO update(@RequestBody final DisziplinDTO disziplinDTO, final Principal principal) {
        Preconditions.checkNotNull(disziplinDTO, "ConfigurationDTO must not null");
        Preconditions.checkArgument(disziplinDTO.getDisziplinId() >= 0, "DisziplinDTO id must not be negative");
        Preconditions.checkNotNullOrEmpty(disziplinDTO.getDisziplinName(), "DisziplinDTO name must not null or empty");


        final DisziplinDO newDisziplinDO = DisziplinDTOMapper.toDO.apply(disziplinDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final DisziplinDO updatedDiziplinDO = disziplinComponent.update(newDisziplinDO, userId);
        return DisziplinDTOMapper.toDTO.apply(updatedDiziplinDO);
    }

    /**
     * I delete an existing Disziplin entry from the database.
     *
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_SYSTEMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final DisziplinDO disziplinDO = new DisziplinDO();
        disziplinDO.setDisziplinID(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        disziplinComponent.delete(disziplinDO, userId);
    }

}
