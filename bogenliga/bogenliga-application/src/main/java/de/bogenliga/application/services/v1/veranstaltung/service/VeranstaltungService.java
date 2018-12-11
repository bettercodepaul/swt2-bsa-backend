package de.bogenliga.application.services.v1.veranstaltung.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.veranstaltung.mapper.VeranstaltungDTOMapper;
import de.bogenliga.application.services.v1.veranstaltung.model.VeranstaltungDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I'm a REST resource and handle veranstaltung CRUD requests over the HTTP protocol
 *
 * @author Marvin Holm
 */
@RestController
@CrossOrigin
@RequestMapping("v1/veranstaltung")
public class VeranstaltungService implements ServiceFacade {
    private static final Logger LOG = LoggerFactory.getLogger(
            VeranstaltungService.class);

    private final VeranstaltungComponent veranstaltungComponent;


    /**
     * Constructor with dependency injection
     *
     * @param veranstaltungComponent to handle the database CRUD requests
     */
    @Autowired
    public VeranstaltungService(final VeranstaltungComponent veranstaltungComponent){
        this.veranstaltungComponent = veranstaltungComponent;
    }


    /**
     * I return all the teams (veranstaltung) of the database.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VeranstaltungDTO> findAll(){
        final List<VeranstaltungDO> VeranstaltungDOList = veranstaltungComponent.findAll();
        return VeranstaltungDOList.stream().map(VeranstaltungDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * I return the veranstaltung Entry of the database with a specific id
     *
     * @return list of {@link VeranstaltungDTO} as JSON
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public VeranstaltungDTO findById(@PathVariable ("id") final long id){
        Preconditions.checkArgument(id >= 0 , "ID must not be negative");

        LOG.debug("Receive 'findById' with requested ID '{}'", id);

        final VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(id);

        return VeranstaltungDTOMapper.toDTO.apply(veranstaltungDO);
    }
}
