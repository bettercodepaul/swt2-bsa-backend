package de.bogenliga.application.services.v1.vereine.service;

import java.security.Principal;
import java.util.List;
import javax.naming.NoPermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.vereine.mapper.VereineDTOMapper;
import de.bogenliga.application.services.v1.vereine.model.VereineDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;



/**
 * I'm a REST resource and handle vereine CRUD requests over the HTTP protocol
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
@RestController
@CrossOrigin
@RequestMapping("v1/vereine")

public class VereineService implements ServiceFacade {
    private static final String PRECONDITION_MSG_VEREIN = "Verein must not be null";
    private static final String PRECONDITION_MSG_VEREIN_ID = "Verein ID must not be negative";
    private static final String PRECONDITION_MSG_NAME = "Name must not be null ";
    private static final String PRECONDITION_MSG_VEREIN_DSB_IDENTIFIER = "Verein dsb Identifier must not be null";
    private static final String PRECONDITION_MSG_REGION_ID_NOT_NEG = "Verein regio ID must not be negative";
    private static final String PRECONDITION_MSG_REGION_ID = "Verein regio ID can not be null";
    private static final String PRECONDITION_MSG_PLATZHALTER_DELETE = "You can not delete the Platzhalter (Placeholder) Verein";

    private static final long PLATZHALTER_VEREIN_ID = 99;


    private final VereinComponent vereinComponent;

    private final RequiresOnePermissionAspect requiresOnePermissionAspect;


    /**
     * Constructor with dependency injection
     *
     * @param vereinComponent to handle the database CRUD requests
     */
    @Autowired
    public VereineService(final VereinComponent vereinComponent,
                          final RequiresOnePermissionAspect requiresOnePermissionAspect) {
        this.vereinComponent = vereinComponent;
        this.requiresOnePermissionAspect = requiresOnePermissionAspect;
    }


    /**
     * I return all the teams (Vereine) of the database.
     *
     * @return list of {@link VereineDTO} as JSON
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VereineDTO> findAll() {
        final List<VereinDO> vereinDOList = vereinComponent.findAll();
        return vereinDOList.stream().map(VereineDTOMapper.toDTO).toList();
    }

    @GetMapping(value = "/search/{searchstring}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VereineDTO> findBySearch(@PathVariable("searchstring") final String searchTerm) {
        final List<VereinDO> vereinDOList = vereinComponent.findBySearch(searchTerm);
        return vereinDOList.stream().map(VereineDTOMapper.toDTO).toList();
    }


    /**
     * I return the verein Entry of the database with a specific id
     *
     * @return list of {@link VereineDTO} as JSON
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public VereineDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative");

        final VereinDO vereinDO = vereinComponent.findById(id);

        return VereineDTOMapper.toDTO.apply(vereinDO);
    }


    /**
     * I persist a new verein and return this verein entry.
     *
     * @param vereineDTO of the request body
     * @param principal  authenticated user
     *
     * @return list of {@link VereineDTO} as JSON
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_STAMMDATEN, UserPermission.CAN_CREATE_STAMMDATEN_LIGALEITER})
    public VereineDTO create(@RequestBody final VereineDTO vereineDTO, final Principal principal) {
        checkPreconditions(vereineDTO);
        final long userId = UserProvider.getCurrentUserId(principal);


        final VereinDO vereinDO = VereineDTOMapper.toDO.apply(vereineDTO);
        final VereinDO persistedVereinDO = vereinComponent.create(vereinDO, userId);

        return VereineDTOMapper.toDTO.apply(persistedVereinDO);
    }


    /**
     * I persist a newer version of the dsbMitglied in the database.
     * <p>
     * You are only able to modify the Verein, if you have the explicit permission to Modify it or if you are the
     * Mannschaftsführer/Sportleiter of the Verein.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VEREIN, UserPermission.CAN_MODIFY_STAMMDATEN_LIGALEITER})
    public VereineDTO update(@RequestBody final VereineDTO vereineDTO,
                             final Principal principal) throws NoPermissionException {
        checkPreconditions(vereineDTO);
        Preconditions.checkArgument(vereineDTO.getId() >= 0, PRECONDITION_MSG_VEREIN_ID);


        if (this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_STAMMDATEN) || this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_STAMMDATEN_LIGALEITER)) {
            //der User hat allgemeine Schreibrechte - wir machen weiter
        } else if (this.requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, vereineDTO.getId())) {
            // der user modifiziert seinen eigenen Verein und ist Sportleiter
            VereinDO temp = vereinComponent.findById(vereineDTO.getId());
            // das darf aber aber nur wenn der Verein in der bestehenen Region verbleibt - d.h. diese sich nicht ändert
            if (!temp.getRegionId().equals(vereineDTO.getRegionId())) throw new NoPermissionException();
        } else throw new NoPermissionException();

        final VereinDO newVereinDo = VereineDTOMapper.toDO.apply(vereineDTO);
        final long userID = UserProvider.getCurrentUserId(principal);

        final VereinDO updateVereinDO = vereinComponent.update(newVereinDo, userID);
        return VereineDTOMapper.toDTO.apply(updateVereinDO);
    }


    /**
     * I delete an existing Verein entry from the DB.
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");
        // You can´t delete the Platzhalter Verein
        Preconditions.checkArgument(id != PLATZHALTER_VEREIN_ID, PRECONDITION_MSG_PLATZHALTER_DELETE);

        final VereinDO vereinDO = new VereinDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);
        vereinComponent.delete(vereinDO, userId);
    }


     private void checkPreconditions(@RequestBody final VereineDTO vereinDTO) {
        Preconditions.checkNotNull(vereinDTO, PRECONDITION_MSG_VEREIN);
        Preconditions.checkNotNull(vereinDTO.getName(), PRECONDITION_MSG_NAME);
        Preconditions.checkNotNull(vereinDTO.getIdentifier(), PRECONDITION_MSG_VEREIN_DSB_IDENTIFIER);
        Preconditions.checkNotNull(vereinDTO.getRegionId(), PRECONDITION_MSG_REGION_ID);

        Preconditions.checkArgument(vereinDTO.getRegionId() >= 0, PRECONDITION_MSG_REGION_ID_NOT_NEG);
    }

}