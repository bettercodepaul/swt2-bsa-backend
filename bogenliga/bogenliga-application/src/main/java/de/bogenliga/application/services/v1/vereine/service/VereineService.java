package de.bogenliga.application.services.v1.vereine.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.services.v1.vereine.mapper.VereineDTOMapper;
import de.bogenliga.application.services.v1.vereine.model.VereineDTO;
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
    private static final String PRECONDITION_MSG__REGION_ID = "Verein regio ID must not be negative";

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

}
