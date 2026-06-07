package de.bogenliga.application.services.v1.wettkampf.service;

import java.security.Principal;
import java.util.List;

import de.bogenliga.application.business.wettkampf.api.AnzeigenComponent;
import de.bogenliga.application.business.wettkampf.api.types.AnzeigenDO;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.services.v1.wettkampf.mapper.AnzeigenDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.model.AnzeigenDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

@RestController
@RequestMapping("v1/anzeigen")
public class AnzeigenService implements ServiceFacade {

    private static final Logger LOG = LoggerFactory.getLogger(AnzeigenService.class);

    private final AnzeigenComponent anzeigenComponent;


    @Autowired
    public AnzeigenService(final AnzeigenComponent anzeigenComponent) {
        this.anzeigenComponent = anzeigenComponent;
    }


    /**
     * I return all Anzeigen entries of the database.
     *
     * @return list of {@link AnzeigenDTO} as JSON
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm={UserPermission.CAN_READ_SYSTEMDATEN})
    public List<AnzeigenDTO> findAll() {
        final List<AnzeigenDO> anzeigenDOList = anzeigenComponent.findAll();
        LOG.debug("Received Anzeigen request");
        return anzeigenDOList.stream().map(AnzeigenDTOMapper.toDTO).toList();
    }


    /**
     * I return the Anzeigen entry of the database with a specific id.
     *
     * @return list of {@link AnzeigenDTO} as JSON
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm={UserPermission.CAN_READ_SYSTEMDATEN})
    public AnzeigenDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with id '{}'", id);

        final AnzeigenDO anzeigenDO = anzeigenComponent.findById(id);
        return AnzeigenDTOMapper.toDTO.apply(anzeigenDO);
    }

    /**
     * I return all Anzeigen entries of the database with a specific VeranstaltungsId.
     *
     * @return list of {@link AnzeigenDTO} as JSON
     */
    @GetMapping(value = "{veranstaltungsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm={UserPermission.CAN_READ_SYSTEMDATEN})
    public List<AnzeigenDTO> findByVeranstaltungsId(@PathVariable("veranstaltungsId") final long veranstaltungsId) {
        Preconditions.checkArgument(veranstaltungsId > 0, "ID must not be negative.");

        LOG.debug("Receive 'findByVeranstaltungsId' request with id '{}'", veranstaltungsId);

        final List<AnzeigenDO> anzeigenDOList = anzeigenComponent.findByVeranstaltungsId(veranstaltungsId);
        return anzeigenDOList.stream().map(AnzeigenDTOMapper.toDTO).toList();
    }

    /**
     * create-Method() writes a new entry of Anzeigen into the database
     *
     * @param anzeigenDTO anzulegender anzeige
     * @param principal user der arbeitet
     *
     * @return angelegter anzeigenDTO
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_SYSTEMDATEN})
    public long create(@RequestBody final AnzeigenDTO anzeigenDTO, final Principal principal) {

        LOG.debug("Received 'create' request with id '{}' ", anzeigenDTO.getId());

        final AnzeigenDO newAnzeigenDO = AnzeigenDTOMapper.toDO.apply(anzeigenDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final AnzeigenDO savedAnzeigenDO = anzeigenComponent.create(newAnzeigenDO, userId);

        final AnzeigenDTO savedAnzeigenDTO = AnzeigenDTOMapper.toDTO.apply(savedAnzeigenDO);

        return savedAnzeigenDTO.getId();
    }

}
