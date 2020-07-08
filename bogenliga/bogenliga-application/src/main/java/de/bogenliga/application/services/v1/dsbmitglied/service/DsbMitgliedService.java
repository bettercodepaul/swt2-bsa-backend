package de.bogenliga.application.services.v1.dsbmitglied.service;

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
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.dsbmitglied.mapper.DsbMitgliedDTOMapper;
import de.bogenliga.application.services.v1.dsbmitglied.model.DsbMitgliedDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I´m a REST resource and handle dsbMitglied CRUD requests over the HTTP protocol.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">Wikipedia - CRUD</a>
 * @see <a href="https://en.wikipedia.org/wiki/Representational_state_transfer">Wikipedia - REST</a>
 * @see <a href="https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">Wikipedia - HTTP</a>
 * @see <a href="https://en.wikipedia.org/wiki/Design_by_contract">Wikipedia - Design by contract</a>
 * @see <a href="https://spring.io/guides/gs/actuator-service/">
 * Building a RESTful Web Service with Spring Boot Actuator</a>
 * @see <a href="https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-dsbMitglied">
 * Build a REST API with Spring 4 and Java Config</a>
 * @see <a href="https://www.baeldung.com/spring-autowire">Guide to Spring @Autowired</a>
 */
@RestController
@CrossOrigin
@RequestMapping("v1/dsbmitglied")
public class DsbMitgliedService implements ServiceFacade {

    private static final String PRECONDITION_MSG_DSBMITGLIED = "DsbMitgliedDO must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_ID = "DsbMitgliedDO ID must not be negative";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VORNAME = "DsbMitglied vorname must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_NACHNAME = "DsbMitglied nachname must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_GEBURTSDATUM = "DsbMitglied geburtsdatum must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_NATIONALITAET = "DsbMitglied nationalitaet must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_MITGLIEDSNUMMER = "DsbMitglied mitgliedsnummer must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID = "DsbMitglied vereins id must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID_NEGATIVE = "DsbMitglied vereins id must not be negative";


    private static final Logger LOG = LoggerFactory.getLogger(DsbMitgliedService.class);

    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final DsbMitgliedComponent dsbMitgliedComponent;


    /**
     * Constructor with dependency injection
     *
     * @param dsbMitgliedComponent to handle the database CRUD requests
     */
    @Autowired
    public DsbMitgliedService(final DsbMitgliedComponent dsbMitgliedComponent) {
        this.dsbMitgliedComponent = dsbMitgliedComponent;
    }


    /**
     * I return all dsbMitglied entries of the database.

     *
     * Usage:
     * <pre>{@code Request: GET /v1/dsbmitglied}</pre>
     *
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
     * @return list of {@link DsbMitgliedDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DSBMITGLIEDER)
    public List<DsbMitgliedDTO> findAll() {
        final List<DsbMitgliedDO> dsbMitgliedDOList = dsbMitgliedComponent.findAll();
        return dsbMitgliedDOList.stream().map(DsbMitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }

    @RequestMapping(value = "/team/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DSBMITGLIEDER)
    public List<DsbMitgliedDTO> findAllByTeamId(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");
        LOG.debug("Receive 'findAllByTeamid' request with ID '{}'", id );
        final List<DsbMitgliedDO> dsbMitgliedDOList = dsbMitgliedComponent.findAllByTeamId(id);
        return dsbMitgliedDOList.stream().map(DsbMitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * I return the dsbMitglied entry of the database with a specific id.
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
     * @return list of {@link DsbMitgliedDTO} as JSON
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DSBMITGLIEDER)
    public DsbMitgliedDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findByDsbMitgliedId' request with ID '{}'", id);

        final DsbMitgliedDO dsbMitgliedDO = dsbMitgliedComponent.findById(id);
        return DsbMitgliedDTOMapper.toDTO.apply(dsbMitgliedDO);
    }

    /**
     * I return the dsbMitglied entry of the database with a specific id.
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
     * @return list of {@link DsbMitgliedDTO} as JSON
     */
    @RequestMapping(value = "/{id}/{dsbuserid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_STAMMDATEN)
    public DsbMitgliedDTO insertUserId(@PathVariable("id") final long id, @PathVariable("dsbuserid") final long dsbuserid, final Principal principal) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");
        final long userId = UserProvider.getCurrentUserId(principal);

        LOG.debug("Receive 'findByDsbMitgliedId' request with ID '{}'", id);

        final DsbMitgliedDO dsbMitgliedDO = dsbMitgliedComponent.findById(id);
        dsbMitgliedDO.setUserId(dsbuserid);
        final DsbMitgliedDO updatedDsbMitgliedDO = dsbMitgliedComponent.update(dsbMitgliedDO, userId);
        return DsbMitgliedDTOMapper.toDTO.apply(updatedDsbMitgliedDO);
    }


    /**
     * I persist a new dsbMitglied and return this dsbMitglied entry.
     *
     * Usage:
     * <pre>{@code Request: POST /v1/dsbmitglied
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
     * @param dsbMitgliedDTO of the request body
     * @param principal authenticated user
     * @return list of {@link DsbMitgliedDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_CREATE_DSBMITGLIEDER)
    public DsbMitgliedDTO create(@RequestBody final DsbMitgliedDTO dsbMitgliedDTO, final Principal principal) {

        checkPreconditions(dsbMitgliedDTO);

        LOG.debug("Receive 'create' request with id '{}', vorname '{}', nachname '{}', geburtsdatum '{}', nationalitaet '{}'," +
                " mitgliedsnummer '{}', vereinsid '{}'",
                dsbMitgliedDTO.getId(),
                dsbMitgliedDTO.getVorname(),
                dsbMitgliedDTO.getNachname(),
                dsbMitgliedDTO.getGeburtsdatum(),
                dsbMitgliedDTO.getNationalitaet(),
                dsbMitgliedDTO.getMitgliedsnummer(),
                dsbMitgliedDTO.getVereinsId());

        final DsbMitgliedDO newDsbMitgliedDO = DsbMitgliedDTOMapper.toDO.apply(dsbMitgliedDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final DsbMitgliedDO savedDsbMitgliedDO = dsbMitgliedComponent.create(newDsbMitgliedDO, userId);

        return DsbMitgliedDTOMapper.toDTO.apply(savedDsbMitgliedDO);
    }


    /**
     * I persist a newer version of the dsbMitglied in the database.
     *
     * Usage:
     * <pre>{@code Request: PUT /v1/dsbmitglied
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
    @RequiresPermission(UserPermission.CAN_MODIFY_DSBMITGLIEDER)
    public DsbMitgliedDTO update(@RequestBody final DsbMitgliedDTO dsbMitgliedDTO, final Principal principal) {

        checkPreconditions(dsbMitgliedDTO);
        Preconditions.checkArgument(dsbMitgliedDTO.getId() >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);

        LOG.debug("Receive 'create' request with id '{}', vorname '{}', nachname '{}', geburtsdatum '{}', nationalitaet '{}'," +
                        " mitgliedsnummer '{}', vereinsid '{}'",
                dsbMitgliedDTO.getId(),
                dsbMitgliedDTO.getVorname(),
                dsbMitgliedDTO.getNachname(),
                dsbMitgliedDTO.getGeburtsdatum(),
                dsbMitgliedDTO.getNationalitaet(),
                dsbMitgliedDTO.getMitgliedsnummer(),
                dsbMitgliedDTO.getVereinsId()
                );

        final DsbMitgliedDO newDsbMitgliedDO = DsbMitgliedDTOMapper.toDO.apply(dsbMitgliedDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final DsbMitgliedDO updatedDsbMitgliedDO = dsbMitgliedComponent.update(newDsbMitgliedDO, userId);
        return DsbMitgliedDTOMapper.toDTO.apply(updatedDsbMitgliedDO);
    }


    /**
     * I delete an existing dsbMitglied entry from the database.
     *
     * Usage:
     * <pre>{@code Request: DELETE /v1/dsbmitglied/app.bogenliga.frontend.autorefresh.active}</pre>
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_DELETE_DSBMITGLIEDER)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        dsbMitgliedComponent.delete(dsbMitgliedDO, userId);
    }


    private void checkPreconditions(@RequestBody final DsbMitgliedDTO dsbMitgliedDTO) {
        Preconditions.checkNotNull(dsbMitgliedDTO, PRECONDITION_MSG_DSBMITGLIED);
        Preconditions.checkNotNull(dsbMitgliedDTO.getVorname(), PRECONDITION_MSG_DSBMITGLIED_VORNAME);
        Preconditions.checkNotNull(dsbMitgliedDTO.getNachname(), PRECONDITION_MSG_DSBMITGLIED_NACHNAME);
        Preconditions.checkNotNull(dsbMitgliedDTO.getGeburtsdatum(), PRECONDITION_MSG_DSBMITGLIED_GEBURTSDATUM);
        Preconditions.checkNotNull(dsbMitgliedDTO.getNationalitaet(), PRECONDITION_MSG_DSBMITGLIED_NATIONALITAET);
        Preconditions.checkNotNull(dsbMitgliedDTO.getMitgliedsnummer(), PRECONDITION_MSG_DSBMITGLIED_MITGLIEDSNUMMER);
        Preconditions.checkNotNull(dsbMitgliedDTO.getVereinsId(), PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID);
        Preconditions.checkArgument(dsbMitgliedDTO.getVereinsId() >= 0,
                PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID_NEGATIVE);
    }
}
