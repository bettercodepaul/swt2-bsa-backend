package de.bogenliga.application.services.v1.disziplin.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.disziplin.mapper.DisziplinDTOMapper;
import de.bogenliga.application.services.v1.disziplin.model.DisziplinDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I´m a REST resource and handle Disziplin get and getbyID requests over the HTTP protocol.
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
        return disziplinDOList.stream().map(DisziplinDTOMapper.toDTO).toList();
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



}
