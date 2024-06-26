package de.bogenliga.application.services.v1.liga.service;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.liga.mapper.LigaDTOMapper;
import de.bogenliga.application.services.v1.liga.model.LigaDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I'm a REST resource and handle liga CRUD requests over the HTTP protocol
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
@RestController
@CrossOrigin
@RequestMapping("v1/liga")
public class LigaService implements ServiceFacade {
    private static final String PRECONDITION_MSG_LIGA = "Liga must not be null";
    private static final String PRECONDITION_MSG_LIGA_ID = "Liga Id must not be negative";
    private static final String PRECONDITION_MSG_LIGA_DISZIPLIN_ID = "Disziplin Id must not be null";
    private static final String PRECONDITION_MSG_LIGA_REGION = "Region can not be null";
    private static final String PRECONDITION_MSG_LIGA_REGION_ID_NEG = "Region id can not be negative";
    private static final String PRECONDITION_MSG_LIGA_UEBERGEORDNET_ID_NEG = "Region id can not be negative";
    private static final String PRECONDITION_MSG_LIGA_VERANTWORTLICH_ID_NEG = "Verantwortlich id can not be negative";

    private final LigaComponent ligaComponent;






    /**
     * Constructor with dependency injection
     *
     * @param ligaComponent to handle the database CRUD requests
     */
    @Autowired
    public LigaService(final LigaComponent ligaComponent) {
        this.ligaComponent = ligaComponent;
    }


    /**
     * I return all klasse entries of the database.
     *
     * @return lost of {@link de.bogenliga.application.services.v1.liga.model.LigaDTO} as JSON
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LigaDTO> findAll() {
        final List<LigaDO> ligaDOList = ligaComponent.findAll();
        return ligaDOList.stream().map(LigaDTOMapper.toDTO).toList();
    }

    @GetMapping(value = "/search/{searchstring}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<LigaDTO> findBySearch(@PathVariable("searchstring") final String searchTerm) {
        final List<LigaDO> ligaDOList = ligaComponent.findBySearch(searchTerm);
        return ligaDOList.stream().map(LigaDTOMapper.toDTO).toList();
    }

    /**
     * Returns the Lowest Liga
     *
     * @Return the lowestLiga with specific id if id not in lowest Liga retun empty liga
     */
    @GetMapping(
            value ="/lowest/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public LigaDTO findByLowest(@PathVariable("id") final long id){

        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_LIGA_ID);

        final LigaDO lowestDo = ligaComponent.findByLowest(id);
        return LigaDTOMapper.toDTO.apply(lowestDo);
    }

    /**
     * Returns a liga entry of the given id
     *
     * @param id id of the klasse to be returned
     *
     * @return returns a klasse
     */
    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public LigaDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_LIGA_ID);


        final LigaDO ligaDO = ligaComponent.findById(id);

        return LigaDTOMapper.toDTO.apply(ligaDO);
    }


    /**
     * Returns a liga entry of the given id
     *
     * @param id id of the klasse to be returned
     *
     * @return returns a klasse
     */
    @GetMapping(value = "/checkExist/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public LigaDTO checkExist(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_LIGA_ID);

        final LigaDO ligaExist = ligaComponent.checkExist(id);
        return LigaDTOMapper.toDTO.apply(ligaExist);
    }



    /**
     * Returns a liga entry of the given liga name
     *
     * @param liganame liga name of the klasse to be returned
     *
     * @return returns a klasse
     */
    @GetMapping(value = "/checkExistsLigaName/{liganame}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public LigaDTO checkExistsLigaName(@PathVariable("liganame") final String liganame) {

        final LigaDO ligaExist = ligaComponent.checkExistsLigaName(liganame);
        return LigaDTOMapper.toDTO.apply(ligaExist);
    }






    /**
     * I persist a new liga and return this liga entry
     *
     * @param ligaDTO Data to be stored to DB
     * @param principal User saving the data
     *
     * @return list of {@link LigaDTO} as JSON
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_STAMMDATEN, UserPermission.CAN_CREATE_MY_LIGA})
    public LigaDTO create(@RequestBody final LigaDTO ligaDTO, final Principal principal) {

        checkPreconditions(ligaDTO);

        final LigaDO newLigaDO = LigaDTOMapper.toDO.apply(ligaDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);


        final LigaDO savedLigaDO = ligaComponent.create(newLigaDO,
                currentDsbMitglied);
        return LigaDTOMapper.toDTO.apply(savedLigaDO);
    }


    /**
     * I persist a newer version of the CompetitionClass in the database.
     */
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN_LIGALEITER, UserPermission.CAN_MODIFY_STAMMDATEN})
    public LigaDTO update(@RequestBody final LigaDTO ligaDTO,
                          final Principal principal) {



        final LigaDO newLigaDO = LigaDTOMapper.toDO.apply(ligaDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);

        final LigaDO updatedLigaDO = ligaComponent.update(newLigaDO,
                currentDsbMitglied);
        return LigaDTOMapper.toDTO.apply(updatedLigaDO);

    }

    /**
     * I delete an existing Liga entry from the DB.
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete (@PathVariable("id") final Long id, final Principal principal){
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");


        final LigaDO ligaDO = new LigaDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);
        ligaComponent.delete(ligaDO,userId);
    }


    private void checkPreconditions(@RequestBody final LigaDTO ligaDTO) {
        Preconditions.checkNotNull(ligaDTO, PRECONDITION_MSG_LIGA);
        Preconditions.checkNotNull(ligaDTO.getRegionId(), PRECONDITION_MSG_LIGA_REGION);
        Preconditions.checkNotNull(ligaDTO.getDisziplinId(), PRECONDITION_MSG_LIGA_DISZIPLIN_ID);
        Preconditions.checkArgument(ligaDTO.getRegionId() >= 0, PRECONDITION_MSG_LIGA_REGION_ID_NEG);

        // These are not mandatory fields. Only check if filled.
        if (ligaDTO.getLigaUebergeordnetId() != null) {
            Preconditions.checkArgument(ligaDTO.getLigaUebergeordnetId() >= 0,
                    PRECONDITION_MSG_LIGA_UEBERGEORDNET_ID_NEG);
        } else if (ligaDTO.getLigaVerantwortlichId() != null) {
            Preconditions.checkArgument(ligaDTO.getLigaVerantwortlichId() >= 0,
                    PRECONDITION_MSG_LIGA_VERANTWORTLICH_ID_NEG);
        }
    }
}
