package de.bogenliga.application.services.v1.liga.service;

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
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.liga.mapper.LigaDTOMapper;
import de.bogenliga.application.services.v1.liga.model.LigaDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
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
    private static final String PRECONDITION_MSG_LIGA_NAME = "Liga name can not be null";
    private static final String PRECONDITION_MSG_LIGA_REGION = "Region can not be null";
    private static final String PRECONDITION_MSG_LIGA_REGION_ID_NEG = "Region id can not be negative";
    private static final String PRECONDITION_MSG_LIGA_UEBERGEORDNET_ID_NEG = "Region id can not be negative";
    private static final String PRECONDITION_MSG_LIGA_VERANTWORTLICH_ID_NEG = "Verantwortlich id can not be negative";

    private final Logger LOGGER = LoggerFactory.getLogger(LigaService.class);

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
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public List<LigaDTO> findAll() {
        final List<LigaDO> ligaDOList = ligaComponent.findAll();
        return ligaDOList.stream().map(LigaDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * Returns a liga entry of the given id
     *
     * @param id id of the klasse to be returned
     *
     * @return returns a klasse
     */
    @RequestMapping(
            value = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public LigaDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_LIGA_ID);

        LOGGER.debug("Receive 'findById' request with ID '{}'", id);

        final LigaDO ligaDO = ligaComponent.findById(id);

        return LigaDTOMapper.toDTO.apply(ligaDO);
    }


    /**
     * I persist a new liga and return this liga entry
     *
     * @param ligaDTO
     * @param principal
     *
     * @return list of {@link LigaDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public LigaDTO create(@RequestBody final LigaDTO ligaDTO, final Principal principal) {
        LOGGER.debug(
                "Receive 'create' request with ligaId '{}', ligaName '{}', regionId '{}', ligaUebergeordnetId '{}', verantwortlichId '{}' ",
                ligaDTO.getId(),
                ligaDTO.getName(),
                ligaDTO.getRegion_id(),
                ligaDTO.getLiga_ubergeordnet_id(),
                ligaDTO.getLiga_verantwortlich_id());

        //checkPreconditions(ligaDTO);

        final LigaDO newLigaDO = LigaDTOMapper.toDO.apply(ligaDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);

        final LigaDO savedLigaDO = ligaComponent.create(newLigaDO,
                currentDsbMitglied);
        return LigaDTOMapper.toDTO.apply(savedLigaDO);
    }


    /**
     * I persist a newer version of the CompetitionClass in the database.
     */
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public LigaDTO update(@RequestBody final LigaDTO ligaDTO,
                          final Principal principal) {
        //checkPreconditions(ligaDTO);

        LOGGER.debug(
                "Receive 'create' request with ligaId '{}', ligaName '{}', regionId '{}', ligaUebergeordnetId '{}', verantwortlichId '{}' ",
                ligaDTO.getId(),
                ligaDTO.getName(),
                ligaDTO.getRegion_id(),
                ligaDTO.getLiga_ubergeordnet_id(),
                ligaDTO.getLiga_verantwortlich_id());


        final LigaDO newLigaDO = LigaDTOMapper.toDO.apply(ligaDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);

        final LigaDO updatedLigaDO = ligaComponent.update(newLigaDO,
                currentDsbMitglied);
        return LigaDTOMapper.toDTO.apply(updatedLigaDO);

    }


    private void checkPreconditions(@RequestBody final LigaDTO ligaDTO) {
        Preconditions.checkNotNull(ligaDTO, PRECONDITION_MSG_LIGA);
        Preconditions.checkNotNull(ligaDTO.getRegion_id(), PRECONDITION_MSG_LIGA_REGION);
        Preconditions.checkNotNull(ligaDTO.getRegion_name(), PRECONDITION_MSG_LIGA_NAME);

        Preconditions.checkArgument(ligaDTO.getRegion_id() >= 0, PRECONDITION_MSG_LIGA_REGION_ID_NEG);

        // These are not mandatory fields. Only check if filled.
        if (ligaDTO.getLiga_ubergeordnet_id() != null) {
            Preconditions.checkArgument(ligaDTO.getLiga_ubergeordnet_id() >= 0,
                    PRECONDITION_MSG_LIGA_UEBERGEORDNET_ID_NEG);
        } else if (ligaDTO.getLiga_verantwortlich_id() != null) {
            Preconditions.checkArgument(ligaDTO.getLiga_verantwortlich_id() >= 0,
                    PRECONDITION_MSG_LIGA_VERANTWORTLICH_ID_NEG);
        }
    }
}
