package de.bogenliga.application.services.v1.regionen.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.services.v1.regionen.mapper.RegionenDTOMapper;
import de.bogenliga.application.services.v1.regionen.model.RegionenDTO;
import de.bogenliga.application.services.v1.regionen.types.RegionTypes;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I'm a REST resource and handle region CRUD requests over the HTTP protocol
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
@RestController
@CrossOrigin
@RequestMapping("v1/regionen")

public class RegionenService implements ServiceFacade {

    private static final String PRECONDITION_MSG_REGION_TYPE = "Unknown type for region";

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
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public List<RegionenDTO> findAll() {
        final List<RegionenDO> vereinDOList = regionenComponent.findAll();
        return vereinDOList.stream().map(RegionenDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * I return all regions of a specific type from the database
     * @param type of the regions
     * @return list of {@RegionenDTO}
     */
    @RequestMapping(method = RequestMethod.GET,
            value = "{type}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public List<RegionenDTO> findAllByType(@PathVariable("type") final String type) {

        final String upperCaseType = type.toUpperCase();

        checkRegionType(upperCaseType);

        final List<RegionenDO> regionenDOList = regionenComponent.findAllByType(upperCaseType);
        return regionenDOList.stream().map(RegionenDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * Checks if given type is a member of {@RegionTypes}
     * @param type to be checked
     */
    private void checkRegionType(final String type) {

        try {
            RegionTypes.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, PRECONDITION_MSG_REGION_TYPE);
        }

    }
}
