package de.bogenliga.application.services.v1.regionen.service;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
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
    private static final String PRECONDITION_MSG_REGION = "Region must not be null";
    private static final String PRECONDITION_MSG_REGION_ID = "Region ID must not be negative";
    private static final String PRECONDITION_MSG_NAME = "Name must not be null ";
    private static final String PRECONDITION_MSG_REGION_KUERZEL = "Region Contraction must not be null";

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
     * @return List>RegionenDTO> Liste aller Regionen
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<RegionenDTO> findAll() {
        final List<RegionenDO> regionDOList = regionenComponent.findAll();

        return regionDOList.stream().map(RegionenDTOMapper.toDTO).toList();
    }

    @GetMapping(value = "/search/{searchstring}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<RegionenDTO> findBySearch(@PathVariable("searchstring") final String searchTerm) {
        final List<RegionenDO> regionDOList = regionenComponent.findBySearch(searchTerm);


        return regionDOList.stream().map(RegionenDTOMapper.toDTO).toList();
    }

    @GetMapping(value = "ID/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public RegionenDTO findById(@PathVariable ("id") final long id){
        Preconditions.checkArgument(id >= 0 , "ID must not be negative");


        final RegionenDO regionenDO = regionenComponent.findById(id);

        return RegionenDTOMapper.toDTO.apply(regionenDO);
    }


    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_STAMMDATEN)
    public RegionenDTO update (@RequestBody final RegionenDTO regionenDTO, final Principal principal){
        checkPreconditions(regionenDTO);
        Preconditions.checkArgument(regionenDTO.getId() >= 0, PRECONDITION_MSG_REGION_ID);


        final RegionenDO newRegionenDo = RegionenDTOMapper.toDO.apply(regionenDTO);
        final long userID = UserProvider.getCurrentUserId(principal);

        final RegionenDO updateRegionenDO = regionenComponent.update(newRegionenDo,userID);
        return RegionenDTOMapper.toDTO.apply(updateRegionenDO);
    }


    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete (@PathVariable("id") final long id, final Principal principal){
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");


        final RegionenDO regionenDO = new RegionenDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);
        regionenComponent.delete(regionenDO,userId);
    }


    /**
     * I am creating a new Region/RegionenDTO to be saved in the Database
     * @param regionenDTO the new RegionenDTO with the new values.
     * @param principal the current User, who is locked in
     * @return the new RegionenDTO
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_CREATE_STAMMDATEN)
    public RegionenDTO create(@RequestBody final RegionenDTO regionenDTO, final Principal principal) {
        checkPreconditions(regionenDTO);
        final long userId = UserProvider.getCurrentUserId(principal);


        final RegionenDO regionenDO = RegionenDTOMapper.toDO.apply(regionenDTO);
        final RegionenDO persistedRegionenDO = regionenComponent.create(regionenDO, userId);

        return RegionenDTOMapper.toDTO.apply(persistedRegionenDO);
    }



    /**
     * I return all regions of a specific type from the database
     * @param type of the regions
     * @return List</RegionenDTO> Liste aller Region dieses Typs
     */
    @GetMapping(
            value = "{type}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public List<RegionenDTO> findAllByType(@PathVariable("type") final String type) {

        final String upperCaseType = type.toUpperCase();

        checkRegionType(upperCaseType);

        final List<RegionenDO> regionenDOList = regionenComponent.findAllByType(upperCaseType);


        return regionenDOList.stream().map(RegionenDTOMapper.toDTO).toList();
    }


    /**
     * Checks if given type is a member of </RegionTypes>
     * @param type to be checked
     */
    private void checkRegionType(final String type) {

        try {
            RegionTypes.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, PRECONDITION_MSG_REGION_TYPE);
        }
    }

    private void checkPreconditions(@RequestBody final RegionenDTO regionenDTO) {
        Preconditions.checkNotNull(regionenDTO, PRECONDITION_MSG_REGION);
        Preconditions.checkNotNull(regionenDTO.getRegionName(), PRECONDITION_MSG_NAME);
        Preconditions.checkNotNull(regionenDTO.getRegionKuerzel(), PRECONDITION_MSG_REGION_KUERZEL);
        Preconditions.checkNotNull(regionenDTO.getRegionTyp(), PRECONDITION_MSG_REGION_TYPE);
    }

}
