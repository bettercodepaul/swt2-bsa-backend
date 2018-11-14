package de.bogenliga.application.services.v1.vereine.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import org.slf4j.Logger;

/**
 * TODO [AL] class documentation
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
@RestController
@CrossOrigin
@RequestMapping("v1/vereine")

public class VereineService implements ServiceFacade {
    private static final String PRECONDITION_MSG_VEREIN = "Verein must not be null";
    private static final String PRECONDITION_MSG__VEREIN_ID = "Verein ID must not be negative";
    private static final String PRECONDITION_MSG_NAME = "Name must not be null ";
    private static final String PRECONDITION_MSG__VEREIN_DSB_IDENTIFIER = "Verein dsb Identifier must not be null";
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
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VereineDTO> findAll(){
        final List<VereinDO> vereinDOList = vereinComponent.findAll();
        return vereinDOList.stream().map(VereineDTOMapper.toDTO).collect(Collectors.toList());
    }


    @RequestMapping(method = RequestMethod.POST,
                produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public VereineDTO create(@RequestBody final VereineDTO vereineDTO, final Principal principal) {
        checkPreconditions(vereineDTO);
        Preconditions.checkArgument(vereineDTO.getId() >= 0 , PRECONDITION_MSG__VEREIN_ID);
        final long userId = UserProvider.getCurrentUserId(principal);

        LOG.debug("Receive 'create' request with id '{}', name '{}', identifier '{}', region id '{}', version '{}', createdBy '{}'" ,
                vereineDTO.getId(),
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
        Preconditions.checkNotNull(vereinDTO.getIdentifier(), PRECONDITION_MSG__VEREIN_DSB_IDENTIFIER);
        Preconditions.checkNotNull(vereinDTO.getRegionId(), PRECONDITION_MSG_REGION_ID);

        Preconditions.checkArgument(vereinDTO.getRegionId() >= 0, PRECONDITION_MSG_REGION_ID_NOT_NEG);
    }

}
