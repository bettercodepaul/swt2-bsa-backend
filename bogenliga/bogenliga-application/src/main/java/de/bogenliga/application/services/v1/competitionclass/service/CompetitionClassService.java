package de.bogenliga.application.services.v1.competitionclass.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.competitionclass.api.CompetitionClassComponent;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.competitionclass.mapper.CompetitionClassDTOMapper;
import de.bogenliga.application.services.v1.competitionclass.model.CompetitionClassDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I'm a REST resource and handle competition class CRUD requests over the HTTP protocol
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */

@RestController
@CrossOrigin
@RequestMapping("v1/competitionclass")
public class CompetitionClassService implements ServiceFacade {
    private static final String PRECONDITION_MSG_KLASSE = "CompetitionClass must not be null";
    private static final String PRECONDITION_MSG_KLASSE_ID = "CompetitionClass ID must not be negative";
    private static final String PRECONDITION_MSG_KLASSE_JAHRGANG_MIN = "Minimum Age must not be negative";
    private static final String PRECONDITION_MSG_KLASSE_JAHRGANG_MAX = "Max Age must not be negative";
    private static final String PRECONDITION_MSG_KLASSE_NR = "Something is wrong with the CompetitionClass Number";
    private static final String PRECONDITION_MSG_NAME = "The CompetitionClass must be given a name";



    private final CompetitionClassComponent competitionClassComponent;



    /**
     * Constructor with dependency injection
     *
     * @param competitionClassComp to handle the database CRUD requests
     */
    @Autowired
    public CompetitionClassService(final CompetitionClassComponent competitionClassComp) {
        this.competitionClassComponent = competitionClassComp;
    }


    /**
     * I return all klasse entries of the database.
     *
     * @return lost of {@link CompetitionClassDTO} as JSON
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public List<CompetitionClassDTO> findAll() {
        final List<CompetitionClassDO> competitionClassDOList = competitionClassComponent.findAll();
        return competitionClassDOList.stream().map(CompetitionClassDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * Returns a klasse entry of the given id
     *
     * @param id id of the klasse to be returned
     * @return returns a klasse
     */
    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public CompetitionClassDTO findById(@PathVariable("id") final long id){
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_KLASSE_ID);

        final CompetitionClassDO competitionClassDO = competitionClassComponent.findById(id);

        return CompetitionClassDTOMapper.toDTO.apply(competitionClassDO);
    }


    /**
     * I persist a new CompetitionClass and return this CompetitionClass entry
     *
     * @param competitionClassDTO COmpetition Data to be stored to DB
     * @param principal user saving data
     *
     * @return list of {@link CompetitionClassDTO} as JSON
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_CREATE_SYSTEMDATEN)
    public CompetitionClassDTO create(@RequestBody final CompetitionClassDTO competitionClassDTO, final Principal principal) {

        checkPreconditions(competitionClassDTO);

        final CompetitionClassDO newCompetitionClassDo = CompetitionClassDTOMapper.toDO.apply(sortClassYears(competitionClassDTO));
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);

        final CompetitionClassDO savedCompetitionClassDo = competitionClassComponent.create(newCompetitionClassDo,
                currentDsbMitglied);
        return CompetitionClassDTOMapper.toDTO.apply(savedCompetitionClassDo);
    }


    /**
     * I persist a newer version of the CompetitionClass in the database.
     *
     */
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public CompetitionClassDTO update(@RequestBody final CompetitionClassDTO competitionClassDTO,
                                      final Principal principal) {


        checkPreconditions(competitionClassDTO);

        // Method sortClassYears will change the year min and max values accordingly
        final CompetitionClassDO newCompetitionClassDO = CompetitionClassDTOMapper.toDO.apply(sortClassYears(competitionClassDTO));
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);

        final CompetitionClassDO updatedCompetitionClassDO = competitionClassComponent.update(newCompetitionClassDO,
                currentDsbMitglied);
        return CompetitionClassDTOMapper.toDTO.apply(updatedCompetitionClassDO);

    }

    // Swaps the value JahrgangMin and JahrgangMax if they are in the wrong order
    // The JahrgangMax will always be the lower (or equal) number compared to JahrgangMin
    private CompetitionClassDTO sortClassYears (CompetitionClassDTO competitionClassDTO) {
        Long classMinYear = competitionClassDTO.getKlasseJahrgangMin();
        Long classMaxYear = competitionClassDTO.getKlasseJahrgangMax();
        competitionClassDTO.setKlasseJahrgangMax(Math.min(classMinYear, classMaxYear));
        competitionClassDTO.setKlasseJahrgangMin(Math.max(classMinYear, classMaxYear));
        return competitionClassDTO;
    }

    private void checkPreconditions(@RequestBody final CompetitionClassDTO competitionClassDTO) {
        Preconditions.checkNotNull(competitionClassDTO, PRECONDITION_MSG_KLASSE);
        Preconditions.checkNotNull(competitionClassDTO.getKlasseJahrgangMin(), PRECONDITION_MSG_KLASSE_JAHRGANG_MIN);
        Preconditions.checkNotNull(competitionClassDTO.getKlasseJahrgangMax(), PRECONDITION_MSG_KLASSE_JAHRGANG_MAX);
        Preconditions.checkNotNull(competitionClassDTO.getKlasseNr(), PRECONDITION_MSG_KLASSE_NR);
        Preconditions.checkNotNull(competitionClassDTO.getKlasseName(), PRECONDITION_MSG_NAME);

        Preconditions.checkArgument(competitionClassDTO.getKlasseJahrgangMin() >= 0,
                PRECONDITION_MSG_KLASSE_JAHRGANG_MIN);
        Preconditions.checkArgument(competitionClassDTO.getKlasseJahrgangMax() >= 0,
                PRECONDITION_MSG_KLASSE_JAHRGANG_MAX);

    }
}
