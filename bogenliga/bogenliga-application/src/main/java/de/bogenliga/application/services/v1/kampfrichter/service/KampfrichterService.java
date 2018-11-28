package de.bogenliga.application.services.v1.kampfrichter.service;

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
import de.bogenliga.application.business.kampfrichter.api.KampfrichterComponent;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterDTO;
import de.bogenliga.application.services.v1.kampfrichter.mapper.KampfrichterDTOMapper;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I´m a REST resource and handle Kampfrichter CRUD requests over the HTTP protocol.
 *
 * @author
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
@RequestMapping("v1/")
public class KampfrichterService implements ServiceFacade {

    private static final String PRECONDITION_MSG_KAMPFRICHTER = "KampfrichterDO must not be null";
    private static final String PRECONDITION_MSG_KAMPFRICHTER_USERID = "KampfrichterDO USERID must not be negative";
    private static final String PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID = "KampfrichterDO Wettkampf_ID must not be negative";


    private static final Logger LOG = LoggerFactory.getLogger(KampfrichterService.class);

    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final KampfrichterComponent kampfrichterComponent;


    /**
     * Constructor with dependency injection
     *
     * @param kampfrichterComponent to handle the database CRUD requests
     */
    @Autowired
    public KampfrichterService(final KampfrichterComponent kampfrichterComponent) {
        this.kampfrichterComponent = kampfrichterComponent;
    }






    /**
     * I persist a new dsbMitglied and return this dsbMitglied entry.
     *
     * Usage:
     * <pre>{@code Request: POST /v1/kampfrichter
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
     * @param kampfrichterDTO of the request body
     * @param principal authenticated user
     * @return list of {@link KampfrichterDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public KampfrichterDTO create(@RequestBody final KampfrichterDTO kampfrichterDTO, final Principal principal) {

        checkPreconditions(kampfrichterDTO);

        LOG.debug("Receive 'create' request with user_id '{}',",
                kampfrichterDTO.getUserId());

               // dsbMitgliedDTO.isKampfrichter();
        final KampfrichterDO newKampfrichterDO = KampfrichterDTOMapper.toDO.apply(kampfrichterDTO);

        final long userId = UserProvider.getCurrentUserId(principal);

        final KampfrichterDO savedKampfrichterDO = kampfrichterComponent.create(newKampfrichterDO, userId);
        return KampfrichterDTOMapper.toDTO.apply(savedKampfrichterDO);
    }


    /**
     * I persist a newer version of the kampfrichter in the database.
     *
     * Usage:
     * <pre>{@code Request: PUT /v1/kampfrichter
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
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public KampfrichterDTO update(@RequestBody final KampfrichterDTO kampfrichterDTO, final Principal principal) {
        checkPreconditions(kampfrichterDTO);
        Preconditions.checkArgument(kampfrichterDTO.getUserId() >= 0, PRECONDITION_MSG_KAMPFRICHTER_USERID);

        LOG.debug("Receive 'create' request with user_id '{}'",
                kampfrichterDTO.getUserId());

                //dsbMitgliedDTO.isKampfrichter();


        final KampfrichterDO newKampfrichterDO = KampfrichterDTOMapper.toDO.apply(kampfrichterDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final KampfrichterDO updatedKampfrichterDO = kampfrichterComponent.update(newKampfrichterDO, userId);
        return KampfrichterDTOMapper.toDTO.apply(updatedKampfrichterDO);
    }


    /**
     * I delete an existing kampfrichter entry from the database.
     *
     * Usage:
     * <pre>{@code Request: DELETE /v1/kampfrichter/app.bogenliga.frontend.autorefresh.active}</pre>
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final KampfrichterDO kampfrichterDO = new KampfrichterDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        kampfrichterComponent.delete(kampfrichterDO, userId);
    }


    private void checkPreconditions(@RequestBody final KampfrichterDTO kampfrichterDTO) {
        Preconditions.checkNotNull(kampfrichterDTO, PRECONDITION_MSG_KAMPFRICHTER);

    }
}
