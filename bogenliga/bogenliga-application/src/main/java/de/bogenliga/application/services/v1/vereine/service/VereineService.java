package de.bogenliga.application.services.v1.vereine.service;

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
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.vereine.mapper.VereineDTOMapper;
import de.bogenliga.application.services.v1.vereine.model.VereineDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I'm a REST resource and handle vereine CRUD requests over the HTTP protocol
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
@RestController
@CrossOrigin
@RequestMapping("v1/vereine")

public class VereineService implements ServiceFacade {
    private static final String PRECONDITION_MSG_VEREIN = "Verein must not be null";
    private static final String PRECONDITION_MSG_VEREIN_ID = "Verein ID must not be negative";
    private static final String PRECONDITION_MSG_NAME = "Name must not be null ";
    private static final String PRECONDITION_MSG_VEREIN_DSB_IDENTIFIER = "Verein dsb Identifier must not be null";
    private static final String PRECONDITION_MSG_REGION_ID_NOT_NEG = "Verein regio ID must not be negative";
    private static final String PRECONDITION_MSG_REGION_ID = "Verein regio ID can not be null";

    private static final Logger LOG = LoggerFactory.getLogger(VereineService.class);

    private final VereinComponent vereinComponent;


    /**
     * Constructor with dependency injection
     *
     * @param vereinComponent to handle the database CRUD requests
     */

    @Autowired
    public VereineService (final VereinComponent vereinComponent){
        this.vereinComponent = vereinComponent;
    }


    /**
     * I return all the teams (Vereine) of the database.
     * @return list of {@link VereineDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VereineDTO> findAll(){
        final List<VereinDO> vereinDOList = vereinComponent.findAll();
        return vereinDOList.stream().map(VereineDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * I return the verein Entry of the database with a specific id
     *
     * @return list of {@link VereineDTO} as JSON
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public VereineDTO findById(@PathVariable ("id") final long id){
        Preconditions.checkArgument(id >= 0 , "ID must not be negative");

        LOG.debug("Receive 'findById' with requested ID '{}'", id);

        final VereinDO vereinDO = vereinComponent.findById(id);

        return VereineDTOMapper.toDTO.apply(vereinDO);
    }

    /**
     * I persist a newer version of the dsbMitglied in the database.
     */

    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_STAMMDATEN)
    public VereineDTO update (@RequestBody final VereineDTO vereineDTO, final Principal principal){
        checkPreconditions(vereineDTO);
        Preconditions.checkArgument(vereineDTO.getId() >= 0, PRECONDITION_MSG_VEREIN_ID);

        LOG.debug("Receive  'update' request with id '{}', name '{}'; dsb_identifier '{}', region_id '{}' ",
                vereineDTO.getId(),
                vereineDTO.getName(),
                vereineDTO.getIdentifier(),
                vereineDTO.getRegionId());


        final VereinDO newVereinDo = VereineDTOMapper.toDO.apply(vereineDTO);
        final long userID = UserProvider.getCurrentUserId(principal);

        final VereinDO updateVereinDO = vereinComponent.update(newVereinDo,userID);
        return VereineDTOMapper.toDTO.apply(updateVereinDO);
    }

    /**
     * I delete an existing Verein entry from the DB.
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete (@PathVariable("id") final long id, final Principal principal){
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        final VereinDO vereinDO = new VereinDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);
        vereinComponent.delete(vereinDO,userId);
    }





    /**
     * I persist a new verein and return this verein entry.
     *
     * @param vereineDTO of the request body
     * @param principal authenticated user
     * @return list of {@link VereineDTO} as JSON
     */

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_CREATE_STAMMDATEN)
    public VereineDTO create(@RequestBody final VereineDTO vereineDTO, final Principal principal) {
        checkPreconditions(vereineDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        LOG.debug("Receive 'create' request with name '{}', identifier '{}', region id '{}', version '{}', createdBy '{}'" ,
                vereineDTO.getName(),
                vereineDTO.getIdentifier(),
                vereineDTO.getRegionId(),
                vereineDTO.getVersion(),
                userId);

        final VereinDO vereinDO = VereineDTOMapper.toDO.apply(vereineDTO);
        final VereinDO persistedVereinDO = vereinComponent.create(vereinDO, userId);

        return VereineDTOMapper.toDTO.apply(persistedVereinDO);
    }




    private void checkPreconditions(@RequestBody final VereineDTO vereinDTO) {
        Preconditions.checkNotNull(vereinDTO, PRECONDITION_MSG_VEREIN);
        Preconditions.checkNotNull(vereinDTO.getName(), PRECONDITION_MSG_NAME);
        Preconditions.checkNotNull(vereinDTO.getIdentifier(), PRECONDITION_MSG_VEREIN_DSB_IDENTIFIER);
        Preconditions.checkNotNull(vereinDTO.getRegionId(), PRECONDITION_MSG_REGION_ID);

        Preconditions.checkArgument(vereinDTO.getRegionId() >= 0, PRECONDITION_MSG_REGION_ID_NOT_NEG);
    }

}
