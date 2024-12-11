package de.bogenliga.application.services.v1.veranstaltung.service;

import java.security.Principal;
import java.util.List;
import javax.naming.NoPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungPhase;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.sportjahr.model.SportjahrDTO;
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
                                RequiresOnePermissionAspect requiresOnePermissionAspect) {
        this.veranstaltungComponent = veranstaltungComponent;
        this.requiresOnePermissionAspect = requiresOnePermissionAspect;
    }


    /**
     * I return all the teams (veranstaltung) specified by the phase 'Geplant' and 'Laufend' of the database.
     *
     * @return List of VeranstaltungDTOs
     */
    @GetMapping(value = "GeplantLaufend", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findAllGeplantLaufend() {

        VeranstaltungPhase.Phase[] phaseList = new VeranstaltungPhase.Phase[2];
        phaseList[0] = VeranstaltungPhase.Phase.GEPLANT;
        phaseList[1] = VeranstaltungPhase.Phase.LAUFEND;

        return veranstaltungComponent.findAll(phaseList).stream().map(VeranstaltungDTOMapper.toDTO).toList();
    }


    /**
     * I return all the veranstaltung specified by the Phase 'Laufend' and 'Abgeschlossen' of the database
     *
     * @return List of VeranstaltungDTOs
     */
    @GetMapping(value = "LaufendAbgeschlossen", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findAllLaufendAbgeschlossen() {

        VeranstaltungPhase.Phase[] phaseList = new VeranstaltungPhase.Phase[2];
        phaseList[0] = VeranstaltungPhase.Phase.LAUFEND;
        phaseList[1] = VeranstaltungPhase.Phase.ABGESCHLOSSEN;

        return veranstaltungComponent.findAll(phaseList).stream().map(VeranstaltungDTOMapper.toDTO).toList();
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


        return veranstaltungDOList.stream().map(VeranstaltungDTOMapper.toDTO).toList();
    }


    /**
     *
     * @return a list with all sportjahre distinct
     */
    @GetMapping(value = "destinct/sportjahr", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<SportjahrDTO> findAllSportjahrDestinct(){

        List<SportjahrDO> returnList= veranstaltungComponent.findAllSportjahreDestinct();

        return returnList.stream().map(SportjahrDTOMapper.toDTO).toList();
    }


    /**
     * It returns a list of all Veranstaltungen, which has the passed sportyear as parameter in the data, from the
     * database.
     *
     * @param sportjahr - filter for sql-abfrage
     *
     * @return return Veranstaltungen with the same Sportjahr
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "find/by/year/{sportjahr}")
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findBySportjahr(@PathVariable("sportjahr") final long sportjahr) {

        VeranstaltungPhase.Phase[] phaseList = new VeranstaltungPhase.Phase[0];
        return veranstaltungComponent.findBySportjahr(sportjahr, phaseList).stream().map(
                VeranstaltungDTOMapper.toDTO).toList();
    }


    /**
     * It returns a list of all Veranstaltungen with parameters in the data from the database. These parameters are the
     * passed sportyear and 'Laufend' as phase.
     *
     * @param sportjahr - filter for sql-abfrage
     *
     * @return return Veranstaltungen with the same Sportjahr
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "find/by/year/{sportjahr}/Laufend")
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findBySportjahrLaufend(@PathVariable("sportjahr") final long sportjahr) {

        VeranstaltungPhase.Phase[] phaseList = new VeranstaltungPhase.Phase[1];
        phaseList[0] = VeranstaltungPhase.Phase.LAUFEND;
        return veranstaltungComponent.findBySportjahr(sportjahr, phaseList).stream().map(
                VeranstaltungDTOMapper.toDTO).toList();
    }


    /**
     * It returns a list of all Veranstaltungen with parameters in the data from the database. These parameters are the
     * passed sportyear and 'Geplant' and 'Laufend' as phase.
     *
     * @param sportjahr - filter for sql-abfrage
     *
     * @return return Veranstaltungen with the same Sportjahr
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "find/by/year/{sportjahr}/GeplantLaufend")
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findBySportjahrGeplantLaufend(@PathVariable("sportjahr") final long sportjahr) {

        VeranstaltungPhase.Phase[] phaseList = new VeranstaltungPhase.Phase[2];
        phaseList[0] = VeranstaltungPhase.Phase.GEPLANT;
        phaseList[1] = VeranstaltungPhase.Phase.LAUFEND;

        return veranstaltungComponent.findBySportjahr(sportjahr, phaseList).stream().map(
                VeranstaltungDTOMapper.toDTO).toList();
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

        return returnList.stream().map(VeranstaltungDTOMapper.toDTO).toList();
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
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_STAMMDATEN, UserPermission.CAN_CREATE_STAMMDATEN_LIGALEITER})
    public VeranstaltungDTO create(@RequestBody final VeranstaltungDTO veranstaltungDTO, final Principal principal)  {


        checkPreconditions(veranstaltungDTO);
        final VeranstaltungDO newVeranstaltungDO = VeranstaltungDTOMapper.toDO.apply(veranstaltungDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);


        final VeranstaltungDO savedVeranstaltungDO = veranstaltungComponent.create(newVeranstaltungDO,
                currentDsbMitglied);
        // savedVeranstlatungsDO Ligaleiter ist falsch und sollte der vom empfangenen veranstaltungsDTO sein
        savedVeranstaltungDO.setVeranstaltungLigaleiterID(veranstaltungDTO.getLigaleiterId());
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
    public VeranstaltungDTO findLastVeranstaltungById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id >= 0, "Veranstaltung ID must not be negative");

        LOG.debug("Receive 'findLastVeranstaltungById' with requested ID '{}'", id);

        VeranstaltungPhase.Phase[] phaseList = new VeranstaltungPhase.Phase[0];

        final VeranstaltungDO veranstaltungDO = veranstaltungComponent.findLastVeranstaltungById(id, phaseList);
        return VeranstaltungDTOMapper.toDTO.apply(veranstaltungDO);
    }

    /**
     * I delete an existing Veranstaltung entry from the DB.
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete (@PathVariable("id") final Long id, final Principal principal) {
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

    /**
     * I return the veranstaltung Entry of the database with a specific liga id and sportyear
     *
     * @return list of {@link VeranstaltungDTO} as JSON
     */
    @GetMapping(value = "{id}/{sportjahr}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public VeranstaltungDTO findByLigaIdAndSportjahr(@PathVariable ("id") final long id, @PathVariable ("sportjahr") final long sportjahr){
        Preconditions.checkArgument(id >= 0 , "ID must not be negative");
        Preconditions.checkArgument(sportjahr >= 0 , "Sport year must not be negative");


        final VeranstaltungDO veranstaltungDO = veranstaltungComponent.findByLigaIDAndSportjahr(id, sportjahr);
        return VeranstaltungDTOMapper.toDTO.apply(veranstaltungDO);
    }

}
