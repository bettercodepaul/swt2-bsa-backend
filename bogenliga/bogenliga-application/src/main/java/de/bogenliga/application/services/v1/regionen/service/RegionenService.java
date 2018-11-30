package de.bogenliga.application.services.v1.regionen.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.services.v1.regionen.mapper.RegionenDTOMapper;
import de.bogenliga.application.services.v1.regionen.model.RegionenDTO;

/**
 * I'm a REST resource and handle region CRUD requests over the HTTP protocol
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
@RestController
@CrossOrigin
@RequestMapping("v1/regionen")

public class RegionenService implements ServiceFacade {

    private final RegionenComponent regionenComponent;


    /**
     * Constructor with dependency injection
     *
     * @param regionenComponent to handle the database CRUD requests
     */

    @Autowired
    public RegionenService(final RegionenComponent regionenComponent) {
        this.regionenComponent = regionenComponent;
    }


    /**
     * I return all regions of the database.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegionenDTO> findAll() {
        final List<RegionenDO> vereinDOList = regionenComponent.findAll();
        return vereinDOList.stream().map(RegionenDTOMapper.toDTO).collect(Collectors.toList());
    }
}
