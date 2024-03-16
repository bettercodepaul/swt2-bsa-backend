package de.bogenliga.application.services.v1.role.service;

import de.bogenliga.application.business.role.api.RoleComponent;
import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.services.v1.role.mapper.RoleDTOMapper;
import de.bogenliga.application.services.v1.role.model.*;
import de.bogenliga.application.springconfiguration.security.WebSecurityConfiguration;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * I´m a REST resource and handle configuration CRUD requests over the HTTP protocol.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 * @see <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">Wikipedia - CRUD</a>
 * @see <a href="https://en.wikipedia.org/wiki/Representational_state_transfer">Wikipedia - REST</a>
 * @see <a href="https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">Wikipedia - HTTP</a>
 * @see <a href="https://en.wikipedia.org/wiki/Design_by_contract">Wikipedia - Design by contract</a>
 * @see <a href="https://spring.io/guides/gs/actuator-service/">
 * Building a RESTful Web Service with Spring Boot Actuator</a>
 * @see <a href="https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-configuration">
 * Build a REST API with Spring 4 and Java Config</a>
 * @see <a href="https://www.baeldung.com/spring-autowire">Guide to Spring @Autowired</a>
 */
@RestController
@CrossOrigin
@RequestMapping("v1/role")
public class RoleService implements ServiceFacade {




    private final RoleComponent roleComponent;

    private final JwtTokenProvider jwtTokenProvider;

    private final WebSecurityConfiguration webSecurityConfiguration;



    @Autowired
    public RoleService(final JwtTokenProvider jwtTokenProvider,
                       final WebSecurityConfiguration webSecurityConfiguration,
                       final RoleComponent roleComponent) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.webSecurityConfiguration = webSecurityConfiguration;
        this.roleComponent = roleComponent;
    }



    /**
     * I return all role entries of the database.

     *
     * Usage:
     * <pre>{@code Request: GET /v1/role}</pre>
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
     * @return list of {@link RoleDTO} as JSON
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<RoleDTO> findAll() {
        final List<RoleDO> roleDOList = roleComponent.findAll();
        return roleDOList.stream().map(RoleDTOMapper.toDTO).toList();
    }




}
