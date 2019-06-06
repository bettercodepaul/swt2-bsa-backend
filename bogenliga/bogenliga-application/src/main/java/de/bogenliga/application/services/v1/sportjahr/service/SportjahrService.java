package de.bogenliga.application.services.v1.sportjahr.service;

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
import de.bogenliga.application.business.sportjahr.api.SportjahrComponent;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.sportjahr.mapper.SportjahrDTOMapper;
import de.bogenliga.application.services.v1.sportjahr.model.SportjahrDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;


/**
 * I'm a REST resource and handle Sportjahr CRUD requests over the HTTP protocol
 *
 * @author Marcel Schneider
 */
@RestController
@CrossOrigin
@RequestMapping("v1/sportjahr")
public class SportjahrService implements ServiceFacade {
    private static final String PRECONDITION_MSG_SPORTJAHR = "SportjahrDO must not be null";
    private static final String PRECONDITION_MSG_SPORTJAHR_ID = "Sportjahr ID must not be negative and must not be null";
    private static final String PRECONDITION_MSG_SPORTJAHR_NAME = "Sportjahr name must not be null";


    private static final Logger LOG = LoggerFactory.getLogger(
            SportjahrService.class);

    private final SportjahrComponent sportjahrComponent;


    /**
     * Constructor with dependency injection
     *
     * @param sportjahrComponent to handle the database CRUD requests
     */

    @Autowired
    public SportjahrService(final SportjahrComponent sportjahrComponent) {
        this.sportjahrComponent = sportjahrComponent;
    }


    /**
     * findAll-Method gives back all Sportjahre safed in the Database.
     *
     * @return sportjahrDoList - List filled with Data Objects of Sportjahr
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SportjahrDTO> findAll() {
        final List<SportjahrDO> sportjahrDoList = sportjahrComponent.findAll();
        return sportjahrDoList.stream().map(SportjahrDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * findByID-Method gives back a specific Sportjahr according to a single Sportjahr_ID
     *
     * @param id - single id of the Sportjahr you want te access
     *
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SportjahrDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with ID '{}'", id);

        final SportjahrDO sportjahrDO = sportjahrComponent.findById(id);
        return SportjahrDTOMapper.toDTO.apply(sportjahrDO);
    }


    /**
     * create-Method() writes a new entry of Sportjahr into the database
     *
     * @param sportjahrDTO
     * @param principal
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public SportjahrDTO create(@RequestBody final SportjahrDTO sportjahrDTO, final Principal principal) {

        checkPreconditions(sportjahrDTO);

        LOG.debug("Received 'create' request with id '{}', name '{}' ",
                sportjahrDTO.getId(),
                sportjahrDTO.getName());

        final SportjahrDO newDsbMitgliedDO = SportjahrDTOMapper.toDO.apply(sportjahrDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final SportjahrDO savedSportjahrDO = sportjahrComponent.create(newDsbMitgliedDO, userId);
        return SportjahrDTOMapper.toDTO.apply(savedSportjahrDO);
    }


    /**
     * Update-Method changes the chosen Wettkampftyp entry in the Database
     *
     * @param sportjahrDTO
     * @param principal
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public SportjahrDTO update(@RequestBody final SportjahrDTO sportjahrDTO, final Principal principal) {
        checkPreconditions(sportjahrDTO);
        Preconditions.checkArgument(sportjahrDTO.getId() >= 0, PRECONDITION_MSG_SPORTJAHR_ID);

        LOG.debug(
                "Received 'update' request with id '{}', Datum '{}', VeranstaltungsID'{}', WettkampftypDisziplinID'{}', Wettkampftyport'{}'," +
                        " SPortjahrTag '{}', SportjahrBeginn'{}', SportjahrTypID '{}' ",
                sportjahrDTO.getId(),
                sportjahrDTO.getName());

        final SportjahrDO newSportjahrDO = SportjahrDTOMapper.toDO.apply(sportjahrDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final SportjahrDO updatedSportjahrDO = sportjahrComponent.update(newSportjahrDO, userId);
        return SportjahrDTOMapper.toDTO.apply(updatedSportjahrDO);
    }


    /**
     * Delete-Method removes an entry from the database
     *
     * @param id
     * @param principal
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final SportjahrDO sportjahrDO = new SportjahrDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        sportjahrComponent.delete(sportjahrDO, userId);
    }


    /**
     * checks the preconditions defined above in this class
     *
     * @param sportjahrDTO
     */
    private void checkPreconditions(@RequestBody final SportjahrDTO sportjahrDTO) {
        Preconditions.checkNotNull(sportjahrDTO, PRECONDITION_MSG_SPORTJAHR);
        Preconditions.checkNotNull(sportjahrDTO.getName(), PRECONDITION_MSG_SPORTJAHR_NAME);
        Preconditions.checkNotNull(sportjahrDTO.getId() >= 0, PRECONDITION_MSG_SPORTJAHR_ID);
    }
}
