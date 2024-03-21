package de.bogenliga.application.services.v1.wettkampftyp.service;
import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.wettkampftyp.mapper.WettkampfTypDTOMapper;
import de.bogenliga.application.services.v1.wettkampftyp.model.WettkampfTypDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;


/**
 * I'm a REST resource and handle Wettkampftyp CRUD requests over the HTTP protocol
 *
 * @author Marvin Holm, Daniel Schott
 */
@RestController
@CrossOrigin
@RequestMapping("v1/wettkampftyp")
public class WettkampfTypService implements ServiceFacade {
    private static final String PRECONDITION_MSG_WETTKAMPFTYP = "WettkampfTypDO must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPFTYP_ID = "Wettkampftyp ID must not be negative and must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPFTYP_NAME = "Wettkampftyp name must not be null";


    private static final Logger LOG = LoggerFactory.getLogger(
            WettkampfTypService.class);

    private final WettkampfTypComponent wettkampftypComponent;


    /**
     * Constructor with dependency injection
     *
     * @param wettkampftypComponent to handle the database CRUD requests
     */

    @Autowired
    public WettkampfTypService(final WettkampfTypComponent wettkampftypComponent){
        this.wettkampftypComponent = wettkampftypComponent;
    }

    /**
     * findAll-Method gives back all Wettkämpfe safed in the Database.
     *
     * @return wettkampftypDoList - List filled with Data Objects of Wettkämpfe
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<WettkampfTypDTO> findAll() {
        final List<WettkampfTypDO> wettkampftypDoList = wettkampftypComponent.findAll();
        return wettkampftypDoList.stream().map(WettkampfTypDTOMapper.toDTO).toList();
    }

    /**
     * findByID-Method gives back a specific Wettkampftyp according to a single Wettkampftyp_ID
     *
     * @param id - single id of the Wettkampftyp you want te access
     * @return WettkampfTypDTO zur ID
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public WettkampfTypDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with ID '{}'", id);

        final WettkampfTypDO wettkampftypDO= wettkampftypComponent.findById(id);
        return WettkampfTypDTOMapper.toDTO.apply(wettkampftypDO);
    }

    /**
     * create-Method() writes a new entry of Wettkampftyp into the database
     * @param wettkampftypDTO zum Anlegen auf der DB
     * @param principal User der speichert
     * @return WettkampfTypDTO der angelegt wurde
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_STAMMDATEN)
    public WettkampfTypDTO create(@RequestBody final WettkampfTypDTO wettkampftypDTO, final Principal principal) {

        checkPreconditions(wettkampftypDTO);

         final WettkampfTypDO newDsbMitgliedDO = WettkampfTypDTOMapper.toDO.apply(wettkampftypDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final WettkampfTypDO savedWettkampfTypDO= wettkampftypComponent.create(newDsbMitgliedDO, userId);
        return WettkampfTypDTOMapper.toDTO.apply(savedWettkampfTypDO);
    }

    /**
     * Update-Method changes the chosen Wettkampftyp entry in the Database
     * @param wettkampftypDTO
     * @param principal
     * @return
     */
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_STAMMDATEN)
    public WettkampfTypDTO update(@RequestBody final WettkampfTypDTO wettkampftypDTO, final Principal principal) {
        checkPreconditions(wettkampftypDTO);
        Preconditions.checkArgument(wettkampftypDTO.getId() >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);

        final WettkampfTypDO newWettkampfTypDO = WettkampfTypDTOMapper.toDO.apply(wettkampftypDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final WettkampfTypDO updatedWettkampfTypDO = wettkampftypComponent.update(newWettkampfTypDO, userId);
        return WettkampfTypDTOMapper.toDTO.apply(updatedWettkampfTypDO);
    }

    /**
     * Delete-Method removes an entry from the database
     * @param id
     * @param principal
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final WettkampfTypDO wettkampftypDO = new WettkampfTypDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        wettkampftypComponent.delete(wettkampftypDO, userId);
    }

    /**
     * checks the preconditions defined above in this class
     * @param wettkampftypDTO
     */
    private void checkPreconditions(@RequestBody final WettkampfTypDTO wettkampftypDTO) {
        Preconditions.checkNotNull(wettkampftypDTO, PRECONDITION_MSG_WETTKAMPFTYP);
        Preconditions.checkNotNull(wettkampftypDTO.getName(), PRECONDITION_MSG_WETTKAMPFTYP_NAME);
        Preconditions.checkNotNull(wettkampftypDTO.getId() >=0,PRECONDITION_MSG_WETTKAMPFTYP_ID);
    }
}
