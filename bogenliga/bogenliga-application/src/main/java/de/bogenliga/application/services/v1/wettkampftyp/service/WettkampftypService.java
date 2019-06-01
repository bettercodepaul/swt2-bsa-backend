package de.bogenliga.application.services.v1.wettkampftyp.service;
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
import de.bogenliga.application.business.wettkampftyp.api.WettkampftypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampftypDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.wettkampftyp.mapper.WettkampftypDTOMapper;
import de.bogenliga.application.services.v1.wettkampftyp.model.WettkampftypDTO;
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
public class WettkampftypService implements ServiceFacade {
    private static final String PRECONDITION_MSG_WETTKAMPFTYP = "WettkampftypDO must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPFTYP_ID = "Wettkampftyp ID must not be negative and must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPFTYP_NAME = "Wettkampftyp name must not be null";


    private static final Logger LOG = LoggerFactory.getLogger(
            WettkampftypService.class);

    private final WettkampftypComponent wettkampftypComponent;


    /**
     * Constructor with dependency injection
     *
     * @param wettkampftypComponent to handle the database CRUD requests
     */

    @Autowired
    public WettkampftypService (final WettkampftypComponent wettkampftypComponent){
        this.wettkampftypComponent = wettkampftypComponent;
    }

    /**
     * findAll-Method gives back all Wettkämpfe safed in the Database.
     *
     * @return wettkampftypDoList - List filled with Data Objects of Wettkämpfe
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WettkampftypDTO> findAll() {
        final List<WettkampftypDO> wettkampftypDoList = wettkampftypComponent.findAll();
        return wettkampftypDoList.stream().map(WettkampftypDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * findByID-Method gives back a specific Wettkampftyp according to a single Wettkampftyp_ID
     *
     * @param id - single id of the Wettkampftyp you want te access
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public WettkampftypDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with ID '{}'", id);

        final WettkampftypDO wettkampftypDO= wettkampftypComponent.findById(id);
        return WettkampftypDTOMapper.toDTO.apply(wettkampftypDO);
    }

    /**
     * create-Method() writes a new entry of Wettkampftyp into the database
     * @param wettkampftypDTO
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public WettkampftypDTO create(@RequestBody final WettkampftypDTO wettkampftypDTO, final Principal principal) {

        checkPreconditions(wettkampftypDTO);

        LOG.debug("Received 'create' request with id '{}', name '{}' ",
                wettkampftypDTO.getId(),
                wettkampftypDTO.getName());

        final WettkampftypDO newDsbMitgliedDO = WettkampftypDTOMapper.toDO.apply(wettkampftypDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final WettkampftypDO savedWettkampftypDO= wettkampftypComponent.create(newDsbMitgliedDO, userId);
        return WettkampftypDTOMapper.toDTO.apply(savedWettkampftypDO);
    }

    /**
     * Update-Method changes the chosen Wettkampftyp entry in the Database
     * @param wettkampftypDTO
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public WettkampftypDTO update(@RequestBody final WettkampftypDTO wettkampftypDTO, final Principal principal) {
        checkPreconditions(wettkampftypDTO);
        Preconditions.checkArgument(wettkampftypDTO.getId() >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);

                LOG.debug("Received 'update' request with id '{}', Datum '{}', VeranstaltungsID'{}', WettkampftypDisziplinID'{}', Wettkampftyport'{}'," +
                                " WettkampftypTag '{}', WettkampftypBeginn'{}', WettkampftypTypID '{}' ",
                        wettkampftypDTO.getId(),
                        wettkampftypDTO.getName());

        final WettkampftypDO newWettkampftypDO = WettkampftypDTOMapper.toDO.apply(wettkampftypDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final WettkampftypDO updatedWettkampftypDO = wettkampftypComponent.update(newWettkampftypDO, userId);
        return WettkampftypDTOMapper.toDTO.apply(updatedWettkampftypDO);
    }

    /**
     * Delete-Method removes an entry from the database
     * @param id
     * @param principal
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final WettkampftypDO wettkampftypDO = new WettkampftypDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        wettkampftypComponent.delete(wettkampftypDO, userId);
    }

    /**
     * checks the preconditions defined above in this class
     * @param wettkampftypDTO
     */
    private void checkPreconditions(@RequestBody final WettkampftypDTO wettkampftypDTO) {
        Preconditions.checkNotNull(wettkampftypDTO, PRECONDITION_MSG_WETTKAMPFTYP);
        Preconditions.checkNotNull(wettkampftypDTO.getName(), PRECONDITION_MSG_WETTKAMPFTYP_NAME);
        Preconditions.checkNotNull(wettkampftypDTO.getId() >=0,PRECONDITION_MSG_WETTKAMPFTYP_ID);
    }
}
