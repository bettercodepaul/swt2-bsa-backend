package de.bogenliga.application.services.v1.veranstaltung.service;

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
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.veranstaltung.mapper.VeranstaltungDTOMapper;
import de.bogenliga.application.services.v1.veranstaltung.model.VeranstaltungDTO;
import de.bogenliga.application.services.v1.veranstaltung.mapper.VeranstaltungDTOMapper;
import de.bogenliga.application.services.v1.veranstaltung.model.VeranstaltungDTO;
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
    private static final Logger LOG = LoggerFactory.getLogger(
            VeranstaltungService.class);

    private final VeranstaltungComponent veranstaltungComponent;

    private static final String PRECONDITION_MSG_VERANSTALTUNG = "Veranstaltung must not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "Veranstaltung Id must not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_NAME = "Veranstaltung Name can not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPFTYP_ID = "Veranstaltung Wettkampftyp id can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_SPORTJARHR = "Veranstaltung Sportjahr can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_MELDEDEADLINE = "Veranstaltung Meldedeadline can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGALEITER_ID= "Veranstaltung Ligaleiter IF id can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGA_ID= "Veranstaltung Liga id can not be negative";

    /**
     * Constructor with dependency injection
     *
     * @param veranstaltungComponent to handle the database CRUD requests
     */
    @Autowired
    public VeranstaltungService(final VeranstaltungComponent veranstaltungComponent){
        this.veranstaltungComponent = veranstaltungComponent;
    }


    /**
     * I return all the teams (veranstaltung) of the database.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VeranstaltungDTO> findAll(){

        LOG.debug("Received 'findAll' request for Veranstaltung");
        final List<VeranstaltungDO> VeranstaltungDOList = veranstaltungComponent.findAll();
        return VeranstaltungDOList.stream().map(VeranstaltungDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * I return the veranstaltung Entry of the database with a specific id
     *
     * @return list of {@link VeranstaltungDTO} as JSON
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_STAMMDATEN)
    public VeranstaltungDTO findById(@PathVariable ("id") final long id){
        Preconditions.checkArgument(id >= 0 , "ID must not be negative");

        LOG.debug("Receive 'findById' with requested ID '{}'", id);

        final VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(id);
        return VeranstaltungDTOMapper.toDTO.apply(veranstaltungDO);
    }


    
    
    
    
    /**
     * I persist a new veranstaltung and return this veranstaltung entry
     *
     * @param veranstaltungDTO
     * @param principal
     *
     * @return list of {@link VeranstaltungDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public VeranstaltungDTO create(@RequestBody final VeranstaltungDTO veranstaltungDTO, final Principal principal) {
        LOG.debug(
                "Receive 'create' request with veranstaltungId '{}', veranstaltungName '{}', wettkampftypid '{}', sportjahr '{}', meldedeadline '{}', lialeiterid '{}', ligaid '{}' ",
                veranstaltungDTO.getId(),
                veranstaltungDTO.getName(),
                veranstaltungDTO.getWettkampfTypId(),
                veranstaltungDTO.getSportjahr(),
                veranstaltungDTO.getMeldeDeadline(),
                veranstaltungDTO.getLigaleiterID(),
                veranstaltungDTO.getLigaID());

        checkPreconditions(veranstaltungDTO);

        final VeranstaltungDO newVeranstaltungDO = VeranstaltungDTOMapper.toDO.apply(veranstaltungDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);

        final VeranstaltungDO savedVeranstaltungDO = veranstaltungComponent.create(newVeranstaltungDO,
                currentDsbMitglied);
        return VeranstaltungDTOMapper.toDTO.apply(savedVeranstaltungDO);
    }


    /**
     * I persist a newer version of the CompetitionClass in the database.
     */
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public VeranstaltungDTO update(@RequestBody final VeranstaltungDTO veranstaltungDTO,
                          final Principal principal) {

        LOG.debug(
                "Receive 'create' request with veranstaltungId '{}', veranstaltungName '{}', wettkampftypId '{}', sportjahr '{}', meldedeadline '{}', ligaleiterId '{}', ligaId '{}'",
                veranstaltungDTO.getId(),
                veranstaltungDTO.getName(),
                veranstaltungDTO.getWettkampfTypId(),
                veranstaltungDTO.getSportjahr(),
                veranstaltungDTO.getMeldeDeadline(),
                veranstaltungDTO.getLigaleiterID(),
                veranstaltungDTO.getLigaID()
                );




        final VeranstaltungDO newVeranstaltungDO = VeranstaltungDTOMapper.toDO.apply(veranstaltungDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);

        final VeranstaltungDO updatedVeranstaltungDO = veranstaltungComponent.update(newVeranstaltungDO,
                currentDsbMitglied);
        return VeranstaltungDTOMapper.toDTO.apply(updatedVeranstaltungDO);

    }

    /**
     * I delete an existing Veranstaltung entry from the DB.
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public void delete (@PathVariable("id") final Long id, final Principal principal){
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

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
        Preconditions.checkArgument(veranstaltungDTO.getLigaleiterID() >= 0, PRECONDITION_MSG_VERANSTALTUNG_LIGALEITER_ID);
        Preconditions.checkArgument(veranstaltungDTO.getLigaID() >= 0, PRECONDITION_MSG_VERANSTALTUNG_LIGA_ID);
    }
    
}
