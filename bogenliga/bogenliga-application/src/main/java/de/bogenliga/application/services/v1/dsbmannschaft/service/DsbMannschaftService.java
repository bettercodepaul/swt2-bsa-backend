package de.bogenliga.application.services.v1.dsbmannschaft.service;

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
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.dsbmannschaft.mapper.DsbMannschaftDTOMapper;
import de.bogenliga.application.services.v1.dsbmannschaft.model.DsbMannschaftDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 *
 * @author Philip Dengler
 */
@RestController
@CrossOrigin
@RequestMapping("v1/dsbmannschaft")
public class DsbMannschaftService implements ServiceFacade {

    private static final String PRECONDITION_MSG_DSBMANNSCHAFT = "DsbMannschaftDO must not be null";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_ID = "DsbMannschaftDO ID must not be null";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID = "DsbMannschaft Verein ID must not be null";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_NUMMER = "DsbMannschaft Nummer must not be null";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_BENUTZER_ID = "DsbMannschaft Benutzer Id must not be null";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_VERANSTALTUNG_ID = "DsbMannschaft Veranstaltung ID must not be null";


    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_ID_NEGATIVE = "DsbMannschaft Id must not be negative";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID_NEGATIVE = "DsbMannschaft Vereins Id must not be negative";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_NUMMER_NEGATIVE = "DsbMannschaft Nummer must not be negative";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_BENUTZER_ID_NEGATIVE = "DsbMannschaft Benutzer Id must not be negative";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_VERANSTALTUNG_ID_NEGATIVE = "DsbMannschaft Veranstaltung Id must not be negative";




    private static final Logger LOG = LoggerFactory.getLogger(DsbMannschaftService.class);



    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final DsbMannschaftComponent dsbMannschaftComponent;


    /**
     * Constructor with dependency injection
     *
     * @param dsbMannschaftComponent to handle the database CRUD requests
     */
    @Autowired
    public DsbMannschaftService(final DsbMannschaftComponent dsbMannschaftComponent) {
        this.dsbMannschaftComponent = dsbMannschaftComponent;
    }


    /**
     * I return all dsbMannschaft entries of the database.
     * TODO ACHTUNG: Darf wegen Datenschutz in dieser Form nur vom Admin oder auf Testdaten verwendet werden!
     *
     * Usage:
     * <pre>{@code Request: GET /v1/dsbmannschaft}</pre>
     * <pre>{@code Response: TODO Beispielpayload bezieht sich auf Config, muss noch für DSBMannschaft angepasst werden
     * [
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  },
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.interval",
     *    "value": "10"
     *  }
     * ]
     * }
     * </pre>
     *
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<DsbMannschaftDTO> findAll() {
        final List<DsbMannschaftDO> dsbMannschaftDOList = dsbMannschaftComponent.findAll();
        return dsbMannschaftDOList.stream().map(DsbMannschaftDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * I return the dsbMannschaft entries of the database with the given vereinsId.
     *
     * Usage:
     * <pre>{@Code Request: GET /v1/dsbmannschaft}</pre>
     * <pre>{@Code Response:
     * [
     *  {
     *      "id": "app.bogenliga.frontend.autorefresh.active",
     *      "value": "true"
     *  },
     *  {
     *      "id": "app.bogenliga.frontend.autorefresh.interval",
     *      "value": 10
     *  }
     * ]
     * }</pre>
     * @param id the given vereinsId
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @RequestMapping(value = "byVereinsID/{vereinsId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<DsbMannschaftDTO> findAllByVereinsId(@PathVariable("vereinsId") final long id) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'findAllByVereinsId' request with ID '{}'", id);

        final List<DsbMannschaftDO> dsbMannschaftDOList  = dsbMannschaftComponent.findAllByVereinsId(id);
        return dsbMannschaftDOList.stream().map(DsbMannschaftDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * I return the dsbMannschaft entries of the database with the given Veranstaltungs-Id.
     *
     * @param id the given Veranstaltungs-Id
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @RequestMapping(value = "byVeranstaltungsID/{veranstaltungsId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<DsbMannschaftDTO> findAllByVeranstaltungsId(@PathVariable("veranstaltungsId") final long id) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'findAllByVeranstaltungsId' request with ID '{}'", id);

        final List<DsbMannschaftDO> dsbMannschaftDOList  = dsbMannschaftComponent.findAllByVeranstaltungsId(id);
        return dsbMannschaftDOList.stream().map(DsbMannschaftDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * I return the dsbMannschaft entry of the database with a specific id.
     *
     * Usage:
     * <pre>{@code Request: GET /v1/dsbmitglied/app.bogenliga.frontend.autorefresh.active}</pre>
     * <pre>{@code Response:
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  }
     * }
     * </pre>
     *
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public DsbMannschaftDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with ID '{}'", id);
        final DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(id);

        return DsbMannschaftDTOMapper.toDTO.apply(dsbMannschaftDO);
    }


    /**
     * I persist a new dsbMannschaft and return this dsbMannschaft entry.
     *
     * Usage:
     * <pre>{@code Request: POST /v1/dsbmannschaft
     * Body:
     * {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     * }
     * }</pre>
     * <pre>{@code Response:
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  }
     * }</pre>
     * @param dsbMannschaftDTO of the request body
     * @param principal authenticated user
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_CREATE_MANNSCHAFT)
    public DsbMannschaftDTO create(@RequestBody final DsbMannschaftDTO dsbMannschaftDTO, final Principal principal) {
        checkPreconditions(dsbMannschaftDTO);
        final Long userId = UserProvider.getCurrentUserId(principal);
        Preconditions.checkArgument(userId >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_BENUTZER_ID_NEGATIVE);

        LOG.debug("Receive 'create' request with verein id '{}', nummer '{}', benutzer id '{}', veranstaltung id '{}',",

                dsbMannschaftDTO.getVereinId(),
                dsbMannschaftDTO.getNummer(),
                userId,
                dsbMannschaftDTO.getVeranstaltungId());

        final DsbMannschaftDO newDsbMannschaftDO = DsbMannschaftDTOMapper.toDO.apply(dsbMannschaftDTO);

        final DsbMannschaftDO savedDsbMannschaftDO = dsbMannschaftComponent.create(newDsbMannschaftDO, userId);
        return DsbMannschaftDTOMapper.toDTO.apply(savedDsbMannschaftDO);
    }


    /**
     * I persist a newer version of the dsbMannschaft in the database.
     *
     * Usage:
     * <pre>{@code Request: PUT /v1/dsbmannschaft
     * Body:
     * {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     * }
     * }</pre>
     */
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_MY_VEREIN)
    public DsbMannschaftDTO update(@RequestBody final DsbMannschaftDTO dsbMannschaftDTO, final Principal principal) {
        checkPreconditions(dsbMannschaftDTO);
        Preconditions.checkArgument(dsbMannschaftDTO.getId() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_ID);

        LOG.debug("Receive 'create' request with verein nummer '{}', mannschaft-nr '{}',  benutzer id '{}', veranstaltung id '{}',",

               // dsbMannschaftDTO.getId(),
                dsbMannschaftDTO.getVereinId(),
                dsbMannschaftDTO.getNummer(),
                dsbMannschaftDTO.getBenutzerId(),
                dsbMannschaftDTO.getVeranstaltungId());

        final DsbMannschaftDO newDsbMannschaftDO = DsbMannschaftDTOMapper.toDO.apply(dsbMannschaftDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final DsbMannschaftDO updatedDsbMannschaftDO = dsbMannschaftComponent.update(newDsbMannschaftDO, dsbMannschaftDTO.getId());
        return DsbMannschaftDTOMapper.toDTO.apply(updatedDsbMannschaftDO);
    }

    /**
     * I delete an existing dsbMannschaft entry from the database.
     *
     * Usage:
     * <pre>{@code Request: DELETE /v1/dsbmitglied/app.bogenliga.frontend.autorefresh.active}</pre>
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        dsbMannschaftComponent.delete(dsbMannschaftDO, userId);
    }

    private void checkPreconditions(@RequestBody final DsbMannschaftDTO dsbMannschaftDTO) {
        Preconditions.checkNotNull(dsbMannschaftDTO, PRECONDITION_MSG_DSBMANNSCHAFT);
        //Preconditions.checkNotNull(dsbMannschaftDTO.getId(), PRECONDITION_MSG_DSBMANNSCHAFT_ID);
        Preconditions.checkNotNull(dsbMannschaftDTO.getVereinId(), PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID);
        Preconditions.checkNotNull(dsbMannschaftDTO.getNummer(), PRECONDITION_MSG_DSBMANNSCHAFT_NUMMER);
        Preconditions.checkNotNull(dsbMannschaftDTO.getVeranstaltungId(), PRECONDITION_MSG_DSBMANNSCHAFT_VERANSTALTUNG_ID);
        //Preconditions.checkNotNull(dsbMannschaftDTO.getBenutzerId(), PRECONDITION_MSG_DSBMANNSCHAFT_BENUTZER_ID);



        //Preconditions.checkArgument(dsbMannschaftDTO.getId() >= 0,
        //        PRECONDITION_MSG_DSBMANNSCHAFT_ID_NEGATIVE);
        Preconditions.checkArgument(dsbMannschaftDTO.getVereinId() >= 0,
                PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID_NEGATIVE);
        Preconditions.checkArgument(dsbMannschaftDTO.getNummer() >= 0,
                PRECONDITION_MSG_DSBMANNSCHAFT_NUMMER_NEGATIVE);
        //Preconditions.checkArgument(dsbMannschaftDTO.getBenutzerId() >= 0,
        //        PRECONDITION_MSG_DSBMANNSCHAFT_BENUTZER_ID_NEGATIVE);
        Preconditions.checkArgument(dsbMannschaftDTO.getVeranstaltungId() >= 0,
                PRECONDITION_MSG_DSBMANNSCHAFT_VERANSTALTUNG_ID_NEGATIVE);

    }

}
