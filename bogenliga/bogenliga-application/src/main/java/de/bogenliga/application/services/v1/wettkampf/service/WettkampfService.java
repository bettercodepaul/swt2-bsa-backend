package de.bogenliga.application.services.v1.wettkampf.service;

import java.security.Principal;
import java.util.List;
import javax.naming.NoPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.wettkampf.mapper.WettkampfDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;


/**
 * I'm a REST resource and handle Wettkampf CRUD requests over the HTTP protocol
 *
 * @author Marvin Holm, Daniel Schott
 */
@RestController
@CrossOrigin
@RequestMapping("v1/wettkampf")
public class WettkampfService implements ServiceFacade {
    private static final String PRECONDITION_MSG_WETTKAMPF = "WettkampfDO must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID = "Wettkampfveranstaltungsid must not be negative and must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_DATUM = "Format: YYYY-MM-DD Format must be correct,  Wettkampfdatum must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_STRASSE = "WettkampfStraße must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_PLZ = "Wettkampfplz must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_ORTSNAME = "WettkampfNamemust not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_BEGINN = "Format: HH:MM, Format must be correct, Wettkampfbeginn must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_TAG = "Must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_DISZIPLIN_ID = "Must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_TYP_ID = "Must not be null and must not be negative";

    private static final Logger LOG = LoggerFactory.getLogger(WettkampfService.class);

    private final WettkampfComponent wettkampfComponent;
    private final RequiresOnePermissionAspect requiresOnePermissionAspect;



    /**
     * Constructor with dependency injection
     *
     * @param wettkampfComponent to handle the database CRUD requests
     * @param requiresOnePermissionAspect Zugang zur datenspezifischen Berechtigungsprüfung
     */

    @Autowired
    public WettkampfService(final WettkampfComponent wettkampfComponent,
                            RequiresOnePermissionAspect requiresOnePermissionAspect) {
        this.wettkampfComponent = wettkampfComponent;
        this.requiresOnePermissionAspect = requiresOnePermissionAspect;
    }


    /**
     * findAll-Method gives back all Wettkämpfe safed in the Database.
     *
     * @return wettkampfDoList - List filled with Data Objects of Wettkämpfe
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<WettkampfDTO> findAll() {
        final List<WettkampfDO> wettkampfDoList = wettkampfComponent.findAll();
        return wettkampfDoList.stream().map(WettkampfDTOMapper.toDTO).toList();
    }


    /**
     * findByID-Method gives back a specific Wettkampf according to a single Wettkampf_ID
     *
     * @param id - single id of the Wettkampf you want te access
     *
     * @return wettkampfDTO
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public WettkampfDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with ID '{}'", id);

        final WettkampfDO wettkampfDO = wettkampfComponent.findById(id);
        return WettkampfDTOMapper.toDTO.apply(wettkampfDO);
    }


    /**
     * findAllWettkaempfeByMannschaftsId-Method gives back all Wettkämpfe according to a MannschaftsId.
     *
     * @return wettkampfDoList - List filled with Data Objects of Wettkämpfe
     */
    @GetMapping(value = "byMannschaftsId/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<WettkampfDTO> findAllWettkaempfeByMannschaftsId(@PathVariable("id") final long id) {
        final List<WettkampfDO> wettkampfDoList = wettkampfComponent.findAllWettkaempfeByMannschaftsId(id);
        return wettkampfDoList.stream().map(WettkampfDTOMapper.toDTO).toList();
    }


    @GetMapping(value = "byVeranstaltungId/{veranstaltungId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<WettkampfDTO> findAllByVeranstaltungId(@PathVariable("veranstaltungId") final long veranstaltungId) {
        Preconditions.checkArgument(veranstaltungId >= 0, "Veranstaltung ID must not be negative.");

        LOG.debug("GET request for findAllByVeranstaltungId with ID '{}'", veranstaltungId);
        final List<WettkampfDO> wettkampfDOList = this.wettkampfComponent.findAllByVeranstaltungId(veranstaltungId);
        return wettkampfDOList.stream().map(WettkampfDTOMapper.toDTO).toList();
    }


    /**
     * create-Method() writes a new entry of Wettkampf into the database
     *
     * @param wettkampfDTO anzulegender wettkampf
     * @param principal user der arbeitet
     *
     * @return angelegter wettkampf DTO
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_STAMMDATEN, UserPermission.CAN_CREATE_STAMMDATEN_LIGALEITER})
    public WettkampfDTO create(@RequestBody final WettkampfDTO wettkampfDTO, final Principal principal) {

        checkPreconditions(wettkampfDTO);

        LOG.debug("Received 'create' request with id '{}' ", wettkampfDTO.getId());

        final WettkampfDO newWettkampfDO = WettkampfDTOMapper.toDO.apply(wettkampfDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final WettkampfDO savedWettkampfDO = wettkampfComponent.create(newWettkampfDO, userId);
        return WettkampfDTOMapper.toDTO.apply(savedWettkampfDO);
    }



    /**
     * Delete-Method removes an entry from the database
     *
     * @param id Datensatz zu löschen
     * @param principal ändernder User
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final WettkampfDO wettkampfDO = new WettkampfDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        wettkampfComponent.delete(wettkampfDO, userId);
    }


    /**
     * Update-Method changes the chosen Wettkampf entry in the Database
     *
     * @param wettkampfDTO wettkmapf mit zu aktualiserenden Daten
     * @param principal ändernder User
     *
     * @return aktualisierter WettkampfDto
     */
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF})
    public WettkampfDTO update(@RequestBody final WettkampfDTO wettkampfDTO,
                               final Principal principal) throws NoPermissionException {
        checkPreconditions(wettkampfDTO);

        LOG.debug(
                "Received 'update' request with id '{}'", wettkampfDTO.getId());
      
        //sowohl der Liagleiter als auch der Ausrichter dürfen hier updaten - wenn
        //es sich um ihre zugewiesenen Wettkämpfe /Ligen handelt...
        //daher eine datenspezische Berechtigungsprüfung zusätzlich..
        WettkampfDO wettkampfDO = this.wettkampfComponent.findById(wettkampfDTO.getId());

        if (!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_WETTKAMPF)&&
                !this.requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(
                        UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, wettkampfDO.getWettkampfVeranstaltungsId())&&
                !this.requiresOnePermissionAspect.hasSpecificPermissionAusrichter(
                        UserPermission.CAN_MODIFY_MY_WETTKAMPF, wettkampfDO.getId())) {
            //keines der Rechte besitzt der user
            throw new NoPermissionException();
        }
        final WettkampfDO newWettkampfDO = WettkampfDTOMapper.toDO.apply(wettkampfDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final WettkampfDO updatedWettkampfDO = wettkampfComponent.update(newWettkampfDO, userId);
        return WettkampfDTOMapper.toDTO.apply(updatedWettkampfDO);
    }


    /**
     * checks the preconditions defined above in this class
     *
     * @param wettkampfDTO Datensatz zu prüfen
     */
    private void checkPreconditions(@RequestBody final WettkampfDTO wettkampfDTO) {
        Preconditions.checkNotNull(wettkampfDTO, PRECONDITION_MSG_WETTKAMPF);
        Preconditions.checkNotNull(wettkampfDTO.getDatum(), PRECONDITION_MSG_WETTKAMPF_DATUM);

        Preconditions.checkNotNull(wettkampfDTO.getWettkampfBeginn(), PRECONDITION_MSG_WETTKAMPF_BEGINN);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfStrasse(), PRECONDITION_MSG_WETTKAMPF_STRASSE);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfPlz(), PRECONDITION_MSG_WETTKAMPF_PLZ);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfOrtsname(), PRECONDITION_MSG_WETTKAMPF_ORTSNAME);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfDisziplinId() >= 0,
                PRECONDITION_MSG_WETTKAMPF_DISZIPLIN_ID);
        Preconditions.checkNotNull(wettkampfDTO.getwettkampfVeranstaltungsId() >= 0,
                PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID);
        Preconditions.checkArgument(wettkampfDTO.getWettkampfTypId() >= 0, PRECONDITION_MSG_WETTKAMPF_TYP_ID);
        Preconditions.checkArgument(wettkampfDTO.getWettkampfTag() >= 0, PRECONDITION_MSG_WETTKAMPF_TAG);
    }


    /**
     * Generates a list of id's of allowed contestants for the given contest
     * @param id Id of a contest
     * @param id Id of a Mannschaft 1
     * @param id Id of a Mannschaft 2
     * @return List of Miglied id's allowed to participate of Mannschaft 1 and Mannschaft 2
     */
    @GetMapping(value = "{id}/{mannschaft1ID}/{mannschaft2ID}/allowedContestants", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = UserPermission.CAN_READ_DEFAULT)
    public List<Long> getAllowedMitgliedForWettkampf(@PathVariable long id, @PathVariable long mannschaft1ID, @PathVariable long mannschaft2ID){
        return wettkampfComponent.getAllowedMitglieder(id, mannschaft1ID, mannschaft2ID);
    }


    /**
     * Generates a list of id's of allowed contestants for the given contest
     * @param id Id of a contest
     * @return List of Miglied id's allowed to participate
     */
    @GetMapping(value = "{id}/allowedContestants", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = UserPermission.CAN_READ_DEFAULT)
    public List<Long> getAllowedMitgliedForWettkampf(@PathVariable long id){
        return wettkampfComponent.getAllowedMitglieder(id);
    }
 }
