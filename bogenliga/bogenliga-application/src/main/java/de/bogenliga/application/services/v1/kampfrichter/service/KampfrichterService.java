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
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterDTO;
import de.bogenliga.application.services.v1.kampfrichter.mapper.KampfrichterDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.mapper.WettkampfDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.services.v1.wettkampf.service.WettkampfService;
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
@RequestMapping("v1/kampfrichter")
public class KampfrichterService implements ServiceFacade {
    // TODO: Implement this entire class
    private static final String PRECONDITION_MSG_KAMPFRICHTER = "KampfrichterDO must not be null";
    private static final String PRECONDITION_MSG_KAMPFRICHTER_BENUTZER_ID = "KampfrichterBenutzerID must not be negative and must not be null";
    private static final String PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID = "KampfrichterWettkampfID must not be negative and must not be null";

    private static final Logger LOG = LoggerFactory.getLogger(KampfrichterService.class);

    private final KampfrichterComponent kampfrichterComponent;

    @Autowired
    public KampfrichterService(final KampfrichterComponent kampfrichterComponent) {
        this.kampfrichterComponent = kampfrichterComponent;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<KampfrichterDTO> findAll() {
        final List<KampfrichterDO> kampfrichterDOList = kampfrichterComponent.findAll();
        return kampfrichterDOList.stream().map(KampfrichterDTOMapper.toDTO).collect(Collectors.toList());
    }


    private void checkPreconditions(@RequestBody final KampfrichterDTO kampfrichterDTO) {
        Preconditions.checkNotNull(kampfrichterDTO, PRECONDITION_MSG_KAMPFRICHTER);
        Preconditions.checkArgument(kampfrichterDTO.getUserId() >= 0, PRECONDITION_MSG_KAMPFRICHTER_BENUTZER_ID);
        Preconditions.checkArgument(kampfrichterDTO.getWettkampfId() >= 0, PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID);
    }
}
