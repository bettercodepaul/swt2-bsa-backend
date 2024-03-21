package de.bogenliga.application.services.v1.lizenz.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.lizenz.api.LizenzComponent;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.lizenz.mapper.LizenzDTOMapper;
import de.bogenliga.application.services.v1.lizenz.model.LizenzDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I'm a REST resource and handle lizenz CRUD requests over the HTTP protocol
 *
 * @author Manuel Baisch
 */
@RestController
@CrossOrigin
@RequestMapping("v1/lizenz")
public class LizenzService implements ServiceFacade {
    private static final String PRECONDITION_MSG_LIZENZ = "Lizenz must not be null";
    private static final String PRECONDITION_MSG_LIGA_ID = "Liga Id must not be negative";
    private static final String PRECONDITION_MSG_LIZENZTYP = "Lizenztyp must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIEDID = "DSBMITGLIEDSID must not be null";

    private final Logger logger = LoggerFactory.getLogger(LizenzService.class);

    private final LizenzComponent lizenzComponent;


    /**
     * Constructor with dependency injection
     *
     * @param lizenzComponent to handle the database CRUD requests
     */
    @Autowired
    public LizenzService(final LizenzComponent lizenzComponent) {
        this.lizenzComponent = lizenzComponent;
    }


    /**
     * I return all lizenz entries of the database.
     *
     * @return list of {@link LizenzDTO} as JSON
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LizenzDTO> findAll() {
        final List<LizenzDO> lizenzDOList = lizenzComponent.findAll();
        return lizenzDOList.stream().map(LizenzDTOMapper.toDTO).toList();
    }


    /**
     * Returns all Lizenz entries of the given DSBMitglied
     *
     * @param id id of the DSBMitglied
     *
     * @return returns a list with all Lizenz Entires
     */
    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LizenzDTO> findByDsbMitgliedId(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_LIGA_ID);

        logger.debug("Receive 'findById' request with ID '{}'", id);

        List<LizenzDO> lizenzDOlist = lizenzComponent.findByDsbMitgliedId(id);
        if (lizenzDOlist == null) {
            lizenzDOlist = new ArrayList<>();
            logger.debug("Created Empty response");
        }

        return lizenzDOlist.stream().map(LizenzDTOMapper.toDTO).toList();
    }


    /**
     * I persist a new lizenz entiry and return this lizenz entry
     *
     * @param lizenzDTO
     * @param principal
     *
     * @return newly persisted {@link LizenzDTO} as JSON
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, UserPermission.CAN_MODIFY_MANNSCHAFT, UserPermission.CAN_MODIFY_MY_VEREIN})
    public LizenzDTO create(@RequestBody final LizenzDTO lizenzDTO, final Principal principal) {

        checkPreconditions(lizenzDTO);

        final LizenzDO newLizenzDO = LizenzDTOMapper.toDO.apply(lizenzDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);

        final LizenzDO savedLizenzDO = lizenzComponent.create(newLizenzDO,
                currentDsbMitglied);
        return LizenzDTOMapper.toDTO.apply(savedLizenzDO);
    }


    /**
     * I persist a newer version of the Lizenzentry in the database.
     */
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, UserPermission.CAN_MODIFY_MANNSCHAFT, UserPermission.CAN_MODIFY_MY_VEREIN})
    public LizenzDTO update(@RequestBody final LizenzDTO lizenzDTO,
                            final Principal principal) {

        final LizenzDO newLizenzDO = LizenzDTOMapper.toDO.apply(lizenzDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);

        final LizenzDO updatedLizenzDO = lizenzComponent.update(newLizenzDO,
                currentDsbMitglied);
        return LizenzDTOMapper.toDTO.apply(updatedLizenzDO);

    }

    /**
     * I delete an existing Lizenz entry from the DB by its ID.
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission({UserPermission.CAN_DELETE_STAMMDATEN})
    public void delete (@PathVariable("id") final Long id, final Principal principal){
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        logger.debug("Receive 'delete' request with id '{}'", id);

        final LizenzDO lizenzDO = new LizenzDO();
        lizenzDO.setLizenzId(id);
        final long userId = UserProvider.getCurrentUserId(principal);
        lizenzComponent.delete(lizenzDO,userId);
    }


    private void checkPreconditions(@RequestBody final LizenzDTO lizenzDTO) {
        Preconditions.checkNotNull(lizenzDTO, PRECONDITION_MSG_LIZENZ);
        Preconditions.checkNotNull(lizenzDTO.getLizenztyp(), PRECONDITION_MSG_LIZENZTYP);
        Preconditions.checkNotNull(lizenzDTO.getLizenzDsbMitgliedId(), PRECONDITION_MSG_DSBMITGLIEDID);
    }
}
