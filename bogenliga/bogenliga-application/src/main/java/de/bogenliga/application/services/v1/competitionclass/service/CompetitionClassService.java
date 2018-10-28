package de.bogenliga.application.services.v1.competitionclass.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.competitionclass.api.CompetitionClassComponent;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.services.v1.competitionclass.mapper.CompetitionClassDTOMapper;
import de.bogenliga.application.services.v1.competitionclass.model.CompetitionClassDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I'm a REST resource and handle competition class CRUD requests over the HTTP protocol
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */

@RestController
@CrossOrigin
@RequestMapping("v1/competitionclass")
public class CompetitionClassService implements ServiceFacade {

    private final Logger LOGGER = LoggerFactory.getLogger(CompetitionClassService.class);

    private final CompetitionClassComponent competitionClassComponent;


    /**
     * Constructor with dependency injection
     *
     * @param competitionClassComp to handle the database CRUD requests
     */
    @Autowired
    public CompetitionClassService(final CompetitionClassComponent competitionClassComp) {
        this.competitionClassComponent = competitionClassComp;
    }


    /**
     * I return all klasse entries of the database.
     *
     * @return lost of {@link CompetitionClassDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_STAMMDATEN)
    public List<CompetitionClassDTO> findAll() {
        final List<CompetitionClassDO> competitionClassDOList = competitionClassComponent.findAll();
        return competitionClassDOList.stream().map(CompetitionClassDTOMapper.toDTO).collect(Collectors.toList());
    }
}
