package de.bogenliga.application.services.v1.wettkampf.service;
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
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.wettkampf.mapper.WettkampfDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;


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
    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf ID must not be negative and must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID = "Wettkampfveranstaltungsid must not be negative and must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_DATUM = "Format: YYYY-MM-DD Format must be correct,  Wettkampfdatum must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_ORT = "WettkampfOrt must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_BEGINN = "Format: HH:MM, Format must be correct, Wettkampfbeginn must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_TAG = "Must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_DISZIPLIN_ID = "Must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_TYP_ID = "Must not be null and must not be negative";

    private static final Logger LOG = LoggerFactory.getLogger(WettkampfService.class);

    private final WettkampfComponent wettkampfComponent;


    /**
     * Constructor with dependency injection
     *
     * @param wettkampfComponent to handle the database CRUD requests
     */

    @Autowired
    public WettkampfService (final WettkampfComponent wettkampfComponent){
        this.wettkampfComponent = wettkampfComponent;
    }

    /**
     * findAll-Method gives back all Wettkämpfe safed in the Database.
     *
     * @return wettkampfDoList - List filled with Data Objects of Wettkämpfe
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WettkampfDTO> findAll() {
        final List<WettkampfDO> wettkampfDoList = wettkampfComponent.findAll();
        return wettkampfDoList.stream().map(WettkampfDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * findByID-Method gives back a specific Wettkampf according to a single Wettkampf_ID
     *
     * @param id - single id of the Wettkampf you want te access
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public WettkampfDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with ID '{}'", id);

        final WettkampfDO wettkampfDO= wettkampfComponent.findById(id);
        return WettkampfDTOMapper.toDTO.apply(wettkampfDO);
    }

    /**
     * create-Method() writes a new entry of Wettkampf into the database
     * @param wettkampfDTO
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public WettkampfDTO create(@RequestBody final WettkampfDTO wettkampfDTO, final Principal principal) {

        checkPreconditions(wettkampfDTO);

        LOG.debug("Received 'create' request with id '{}', Datum '{}', VeranstaltungsID'{}', WettkampfDisziplinID'{}', Wettkampfort'{}'," +
                        " WettkampfTag '{}', WettkampfBeginn'{}', WettkampfTypID '{}' ",
                wettkampfDTO.getId(),
                wettkampfDTO.getDatum(),
                wettkampfDTO.getVeranstaltungsId(),
                wettkampfDTO.getWettkampfDisziplinId(),
                wettkampfDTO.getWettkampfOrt(),
                wettkampfDTO.getWettkampfTag(),
                wettkampfDTO.getWettkampfBeginn(),
                wettkampfDTO.getWettkampfTypId());

        final WettkampfDO newDsbMitgliedDO = WettkampfDTOMapper.toDO.apply(wettkampfDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final WettkampfDO savedWettkampfDO= wettkampfComponent.create(newDsbMitgliedDO, userId);
        return WettkampfDTOMapper.toDTO.apply(savedWettkampfDO);
    }

    /**
     * Update-Method changes the chosen Wettkampf entry in the Database
     * @param wettkampfDTO
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public WettkampfDTO update(@RequestBody final WettkampfDTO wettkampfDTO, final Principal principal) {
        checkPreconditions(wettkampfDTO);
        Preconditions.checkArgument(wettkampfDTO.getId() >= 0, PRECONDITION_MSG_WETTKAMPF_ID);

                LOG.debug("Received 'update' request with id '{}', Datum '{}', VeranstaltungsID'{}', WettkampfDisziplinID'{}', Wettkampfort'{}'," +
                                " WettkampfTag '{}', WettkampfBeginn'{}', WettkampfTypID '{}' ",
                        wettkampfDTO.getId(),
                        wettkampfDTO.getDatum(),
                        wettkampfDTO.getVeranstaltungsId(),
                        wettkampfDTO.getWettkampfDisziplinId(),
                        wettkampfDTO.getWettkampfOrt(),
                        wettkampfDTO.getWettkampfTag(),
                        wettkampfDTO.getWettkampfBeginn(),
                        wettkampfDTO.getWettkampfTypId());

        final WettkampfDO newWettkampfDO = WettkampfDTOMapper.toDO.apply(wettkampfDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final WettkampfDO updatedWettkampfDO = wettkampfComponent.update(newWettkampfDO, userId);
        return WettkampfDTOMapper.toDTO.apply(updatedWettkampfDO);
    }

    /**
     * Delete-Method removes an entry from the database
     * @param id
     * @param principal
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final WettkampfDO wettkampfDO = new WettkampfDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        wettkampfComponent.delete(wettkampfDO, userId);
    }

    /**
     * checks the preconditions defined above in this class
     * @param wettkampfDTO
     */
    private void checkPreconditions(@RequestBody final WettkampfDTO wettkampfDTO) {
        Preconditions.checkNotNull(wettkampfDTO, PRECONDITION_MSG_WETTKAMPF);
        Preconditions.checkNotNull(wettkampfDTO.getDatum(), PRECONDITION_MSG_WETTKAMPF_DATUM);
        Preconditions.checkNotNull(wettkampfDTO.getId() >=0,PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfBeginn(),PRECONDITION_MSG_WETTKAMPF_BEGINN);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfOrt(),PRECONDITION_MSG_WETTKAMPF_ORT);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfDisziplinId()>=0, PRECONDITION_MSG_WETTKAMPF_DISZIPLIN_ID);
        Preconditions.checkNotNull(wettkampfDTO.getVeranstaltungsId()>=0, PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID);
        Preconditions.checkArgument(wettkampfDTO.getWettkampfTypId() >= 0, PRECONDITION_MSG_WETTKAMPF_TYP_ID);
        Preconditions.checkArgument(wettkampfDTO.getWettkampfTag() >= 0, PRECONDITION_MSG_WETTKAMPF_TAG);
    }
}
