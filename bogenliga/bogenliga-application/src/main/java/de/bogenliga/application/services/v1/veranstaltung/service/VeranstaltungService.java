package de.bogenliga.application.services.v1.veranstaltung.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import javax.naming.NoPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.sportjahr.SportjahrDTO;
import de.bogenliga.application.services.v1.sportjahr.mapper.SportjahrDTOMapper;
import de.bogenliga.application.services.v1.veranstaltung.mapper.VeranstaltungDTOMapper;
import de.bogenliga.application.services.v1.veranstaltung.model.VeranstaltungDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;



/**
 * I'm a REST resource and handle veranstaltung CRUD requests over the HTTP protocol
 *
 * @author Marvin Holm
 */
@RestController
@CrossOrigin
@RequestMapping("v1/veranstaltung")
public class VeranstaltungService implements ServiceFacade {

    private final VeranstaltungComponent veranstaltungComponent;

    private static final String PRECONDITION_MSG_VERANSTALTUNG = "Veranstaltung must not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_NAME = "Veranstaltung Name can not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPFTYP_ID = "Veranstaltung Wettkampftyp id can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_SPORTJARHR = "Veranstaltung Sportjahr can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_MELDEDEADLINE = "Veranstaltung Meldedeadline can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGALEITER_ID= "Veranstaltung Ligaleiter IF id can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGA_ID= "Veranstaltung Liga id can not be negative";
    private final RequiresOnePermissionAspect requiresOnePermissionAspect;


    private static final Logger LOG = LoggerFactory.getLogger(VeranstaltungService.class);

    /**
     * Constructor with dependency injection
     *
     * @param veranstaltungComponent to handle the database CRUD requests
     */
    @Autowired
    public VeranstaltungService(final VeranstaltungComponent veranstaltungComponent,
                                RequiresOnePermissionAspect requiresOnePermissionAspect){
        this.veranstaltungComponent = veranstaltungComponent;
        this.requiresOnePermissionAspect = requiresOnePermissionAspect;
    }


    /**
     * I return all the teams (veranstaltung) of the database.
     * @return List of VeranstaltungDTOs
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findAll(){


        return veranstaltungComponent.findAll().stream().map(VeranstaltungDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * I return the veranstaltung Entry of the database with a specific id
     *
     * @return list of {@link VeranstaltungDTO} as JSON
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public VeranstaltungDTO findById(@PathVariable ("id") final long id){
        Preconditions.checkArgument(id >= 0 , "ID must not be negative");


        final VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(id);
        return VeranstaltungDTOMapper.toDTO.apply(veranstaltungDO);
    }


    /**
     * I return the veranstaltung Entry of the database with a specific id
     *
     * @return list of {@link VeranstaltungDTO} as JSON
     */
    @GetMapping(value = "findByLigaID/{ligaID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findByLigaId(@PathVariable ("ligaID") final long ligaID){
        Preconditions.checkArgument(ligaID >= 0 , "ID must not be negative");

        final List<VeranstaltungDO> veranstaltungDOList = veranstaltungComponent.findByLigaID(ligaID);

        return veranstaltungDOList.stream().map(VeranstaltungDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     *
     * @return a list with all sportjahre distinct
     */
    @GetMapping(value = "destinct/sportjahr", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<SportjahrDTO> findAllSportjahrDestinct(){

        List<SportjahrDO> returnList= veranstaltungComponent.findAllSportjahreDestinct();

        return returnList.stream().map(SportjahrDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     *
     * @param sportjahr - filterr for sql-abfrage
     * @return return Veranstaltungen with the same Sportjahr
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "find/by/year/{sportjahr}")
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findBySportjahr(@PathVariable ("sportjahr") final long sportjahr){


        return veranstaltungComponent.findBySportjahr(sportjahr).stream().map(VeranstaltungDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     *
     * @param sportjahr - filter for sql-abfrage
     * @return retrun Veranstaltung sorted by exisiting data, in descending order based on the last modification date and "veranslatung_id".
     * Returned "VeranstaltungDTO" objects only have the following attributes assigned: name(veranstaltung_name), id(veranstaltung_id) and ligaName(liga_name)
     * Not assinged attributes are null.
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "find/by/sorted/{sportjahr}")
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findBySportjahrDestinct(@PathVariable ("sportjahr") final long sportjahr){

        List <VeranstaltungDO> returnList = veranstaltungComponent.findBySportjahrDestinct(sportjahr);

        return returnList.stream().map(VeranstaltungDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * I persist a new veranstaltung and return this veranstaltung entry
     *
     * You are only able to create a Veranstaltung, if you have the explicit permission
     * to Create it or if you are the Ligaleiter of the Veranstaltung.
     *
     * @param veranstaltungDTO die Veranstaltung die angelegt werden soll
     * @param principal der aktuell schreibende User
     *
     * @return list of {@link VeranstaltungDTO} as JSON
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_CREATE_STAMMDATEN)
    public VeranstaltungDTO create(@RequestBody final VeranstaltungDTO veranstaltungDTO, final Principal principal) {

        checkPreconditions(veranstaltungDTO);


        final VeranstaltungDO newVeranstaltungDO = VeranstaltungDTOMapper.toDO.apply(veranstaltungDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);


        final VeranstaltungDO savedVeranstaltungDO = veranstaltungComponent.create(newVeranstaltungDO,
                currentDsbMitglied);
        return VeranstaltungDTOMapper.toDTO.apply(savedVeranstaltungDO);
    }


    /**
     * I persist a newer version of the CompetitionClass in the database.
     *
     * You can only update a Competition, if you have the permission to Modify Stammdaten or if
     * you are the Ligaleiter of the Veranstaltung.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public VeranstaltungDTO update(@RequestBody final VeranstaltungDTO veranstaltungDTO,
                          final Principal principal) throws NoPermissionException {


        //da die Berechtiung "modify-my-veranstaltung" abhängig ist von den Daten der Veranstaltung,
        //ist hier nochmal zu prüfen, ob die Veranstaltung wirkling über die LigaleiterID dem User zugeordnet ist
        if(!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_STAMMDATEN)
                && !this.requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG,veranstaltungDTO.getId())){
            throw new NoPermissionException();
        }
        final VeranstaltungDO newVeranstaltungDO = VeranstaltungDTOMapper.toDO.apply(veranstaltungDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);
        final VeranstaltungDO updatedVeranstaltungDO = veranstaltungComponent.update(newVeranstaltungDO,
                currentDsbMitglied);

        return VeranstaltungDTOMapper.toDTO.apply(updatedVeranstaltungDO);
    }

    /**
     * I return the last veranstaltung Entry of the database with a current veranstaltung id
     *
     * @return list of {@link VeranstaltungDTO} as JSON
     */
    @GetMapping(value = "findLastVeranstaltungBy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public VeranstaltungDTO findLastVeranstaltungById(@PathVariable ("id") final long id){
        Preconditions.checkArgument(id >= 0 , "Veranstaltung ID must not be negative");

        LOG.debug("Receive 'findLastVeranstaltungById' with requested ID '{}'", id);

        final VeranstaltungDO veranstaltungDO = veranstaltungComponent.findLastVeranstaltungById(id);
        return VeranstaltungDTOMapper.toDTO.apply(veranstaltungDO);
    }

    /**
     * I delete an existing Veranstaltung entry from the DB.
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete (@PathVariable("id") final Long id, final Principal principal){
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");


        final VeranstaltungDO veranstaltungDO = new VeranstaltungDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);
        veranstaltungComponent.delete(veranstaltungDO,userId);
    }


    private void checkPreconditions(@RequestBody final VeranstaltungDTO veranstaltungDTO) {
        Preconditions.checkNotNull(veranstaltungDTO, PRECONDITION_MSG_VERANSTALTUNG);

        Preconditions.checkNotNull(veranstaltungDTO.getName(), PRECONDITION_MSG_VERANSTALTUNG_NAME);
        Preconditions.checkArgument(veranstaltungDTO.getWettkampfTypId() >= 0, PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPFTYP_ID);
        Preconditions.checkArgument(veranstaltungDTO.getSportjahr() >= 0, PRECONDITION_MSG_VERANSTALTUNG_SPORTJARHR);
        Preconditions.checkNotNull(veranstaltungDTO.getMeldeDeadline(), PRECONDITION_MSG_VERANSTALTUNG_MELDEDEADLINE);
        Preconditions.checkNotNull(veranstaltungDTO.getLigaleiterEmail(), PRECONDITION_MSG_VERANSTALTUNG_LIGALEITER_ID);
        Preconditions.checkArgument(veranstaltungDTO.getLigaId() >= 0, PRECONDITION_MSG_VERANSTALTUNG_LIGA_ID);
    }

}
