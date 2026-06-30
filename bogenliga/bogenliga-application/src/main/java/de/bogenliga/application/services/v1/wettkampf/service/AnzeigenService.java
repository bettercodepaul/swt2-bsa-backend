package de.bogenliga.application.services.v1.wettkampf.service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

/* Diese Api sollte von Ausrichtern, Ligaleitern und Admins verwendet werden können,
   kann (Stand SoSe 2026), aber leider nur von Ligaleitern und Admins verwendet werden.
   Die entsprechenden Permissions für die Ausrichter müssen in Zukunft erstellt,
   und hier hinzugefügt werden*/

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
    @RequiresOnePermissions(perm={UserPermission.CAN_READ_STAMMDATEN})
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
    @RequiresOnePermissions(perm={UserPermission.CAN_READ_STAMMDATEN})
    public AnzeigenDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with id '{}'", id);

        final AnzeigenDO anzeigenDO = anzeigenComponent.findById(id);
        return AnzeigenDTOMapper.toDTO.apply(anzeigenDO);
    }

    /**
     * I return all Anzeigen entries of the database with a specific WettkampfId.
     *
     * @return list of {@link AnzeigenDTO} as JSON
     */
    @GetMapping(value = "byWettkampfId/{wettkampfId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm={UserPermission.CAN_READ_STAMMDATEN})
    public List<AnzeigenDTO> findByWettkampfId(@PathVariable("wettkampfId") final long wettkampfId) {
        Preconditions.checkArgument(wettkampfId > 0, "ID must not be negative.");

        LOG.debug("Receive 'findByWettkampfId' request with id '{}'", wettkampfId);

        final List<AnzeigenDO> anzeigenDOList = anzeigenComponent.findByWettkampfId(wettkampfId);
        return anzeigenDOList.stream().map(AnzeigenDTOMapper.toDTO).toList();
    }

    /**
     * create-Method() writes a new entry of Anzeigen into the database
     *
     * @param wettkampfId Wettkampf ID der anzulegenden Anzeige
     * @param principal User der arbeitet
     *
     * @return angelegter anzeigenDTO
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_STAMMDATEN, UserPermission.CAN_CREATE_STAMMDATEN_LIGALEITER})
    public long create(@RequestBody final Long wettkampfId, final Principal principal) {

        Preconditions.checkNotNull(wettkampfId, "Wettkampf ID must not be null.");
        final AnzeigenDO newAnzeigenDO = AnzeigenDTOMapper.toDO.apply(new AnzeigenDTO());
        final long userId = UserProvider.getCurrentUserId(principal);

        newAnzeigenDO.setWettkampfId(wettkampfId); // Setze ID direkt im 1. Aufkommen des AnzeigenDOs

        final AnzeigenDO savedAnzeigenDO = anzeigenComponent.create(newAnzeigenDO, userId);

        final AnzeigenDTO savedAnzeigenDTO = AnzeigenDTOMapper.toDTO.apply(savedAnzeigenDO);

        return savedAnzeigenDTO.getId();
    }

    /**
     * update-method() changes the chosen Anzeige entry in the Database
     *
     * @param anzeigenDTO Anzeige mit zu aktualisierenden Daten
     * @param principal ändernder User
     *
     * @return aktualisierter anzeigenDTO
     */
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_STAMMDATEN_LIGALEITER})
    public AnzeigenDTO update(@RequestBody final AnzeigenDTO anzeigenDTO, final Principal principal) {

        LOG.debug("Received 'update' request with id '{}'", anzeigenDTO.getId());





        final AnzeigenDO newAnzeigenDO = AnzeigenDTOMapper.toDO.apply(anzeigenDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final AnzeigenDO updatedAnzeigenDO = anzeigenComponent.update(newAnzeigenDO, userId);


        return AnzeigenDTOMapper.toDTO.apply(updatedAnzeigenDO);
    }

    @GetMapping(
            value = "getNewPhysischeBildschirmID",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getNewPhysischeBildschirmID() {
        String randomId = anzeigenComponent.generatePhysischeBildschirmId();
        return Collections.singletonMap("id", randomId);
    }

    /**
     * delete-method() deletes the chosen Anzeige entry in the Database
     *
     * @param id id der zu löschenden Anzeige
     * @param principal Auftrag gebender User
     *
     */
    @DeleteMapping(value = "{id}")
    @RequiresOnePermissions(perm = {UserPermission.CAN_DELETE_STAMMDATEN, UserPermission.CAN_MODIFY_STAMMDATEN_LIGALEITER})
    public void delete(@PathVariable final Long id, final Principal principal) {
        Preconditions.checkNotNull(id, "ID must not be null.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        final long userId = UserProvider.getCurrentUserId(principal);
        final AnzeigenDO anzeigenDO = anzeigenComponent.findById(id);

        anzeigenComponent.delete(anzeigenDO, userId);
    }


}
