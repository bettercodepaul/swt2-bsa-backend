package de.bogenliga.application.services.v1.configuration.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.configuration.api.ConfigurationComponent;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.configuration.mapper.ConfigurationDTOMapper;
import de.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I´m a REST resource and handle configuration CRUD requests over the HTTP protocol.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
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
@RequestMapping("v1/configuration")
public class ConfigurationService implements ServiceFacade {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationService.class);

    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final ConfigurationComponent configurationComponent;


    /**
     * Constructor with dependency injection
     *
     * @param configurationComponent to handle the database CRUD requests
     */
    @Autowired
    public ConfigurationService(final ConfigurationComponent configurationComponent) {
        this.configurationComponent = configurationComponent;
    }


    /**
     * I return all configuration entries of the database.
     *
     * Usage:
     * <pre>{@code Request: GET /v1/configuration}</pre>
     * <pre>{@code Response:
     * [
     *  {
     *    "key": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  },
     *  {
     *    "key": "app.bogenliga.frontend.autorefresh.interval",
     *    "value": "10"
     *  }
     * ]
     * }
     * </pre>
     *
     * @return list of {@link ConfigurationDTO} as JSON
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public List<ConfigurationDTO> findAll() {
        final List<ConfigurationDO> configurationDOList = configurationComponent.findAll();
        LOG.debug("Received Configuration request");
        return configurationDOList.stream().map(ConfigurationDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * I return the configuration entry of the database with a specific key.
     *
     * Usage:
     * <pre>{@code Request: GET /v1/configuration/app.bogenliga.frontend.autorefresh.active}</pre>
     * <pre>{@code Response:
     *  {
     *    "key": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  }
     * }
     * </pre>
     *
     * @return list of {@link ConfigurationDTO} as JSON
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public ConfigurationDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with id '{}'", id);

        final ConfigurationDO configurationDO = configurationComponent.findById(id);
        return ConfigurationDTOMapper.toDTO.apply(configurationDO);
    }


    //Endpoint not in use
    /**
     * I persist a new configuration and return this configuration entry.
     *
     * Usage:
     * <pre>{@code Request: POST /v1/configuration
     * Body:
     * {
     *    "key": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     * }
     * }</pre>
     * <pre>{@code Response:
     *  {
     *    "key": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  }
     * }</pre>
     * @param configurationDTO of the request body
     * @param principal authenticated user
     * @return list of {@link ConfigurationDTO} as JSON
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public ConfigurationDTO create(@RequestBody final ConfigurationDTO configurationDTO, final Principal principal) {
        Preconditions.checkNotNull(configurationDTO, "ConfigurationDTO must not null");
        Preconditions.checkArgument(configurationDTO.getId() >= 0, "ConfigurationDTO id must not be negative");
        Preconditions.checkNotNullOrEmpty(configurationDTO.getKey(), "ConfigurationDTO key must not null or empty");
        Preconditions.checkNotNull(configurationDTO.getValue(), "ConfigurationDTO value must not null");

        final ConfigurationDO newConfigurationDO = ConfigurationDTOMapper.toDO.apply(configurationDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final ConfigurationDO savedConfigurationDO = configurationComponent.create(newConfigurationDO, userId);
        return ConfigurationDTOMapper.toDTO.apply(savedConfigurationDO);
    }


    /**
     * I persist a newer version of the configuration in the database.
     *
     * Usage:
     * <pre>{@code Request: PUT /v1/configuration
     * Body:
     * {
     *    "key": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     * }
     * }</pre>
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public ConfigurationDTO update(@RequestBody final ConfigurationDTO configurationDTO, final Principal principal) {
        Preconditions.checkNotNull(configurationDTO, "ConfigurationDTO must not null");
        Preconditions.checkArgument(configurationDTO.getId() >= 0, "ConfigurationDTO id must not be negative");
        Preconditions.checkNotNullOrEmpty(configurationDTO.getKey(), "ConfigurationDTO key must not null or empty");
        Preconditions.checkNotNull(configurationDTO.getValue(), "ConfigurationDTO value must not null");

        final ConfigurationDO newConfigurationDO = ConfigurationDTOMapper.toDO.apply(configurationDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final ConfigurationDO updatedConfigurationDO = configurationComponent.update(newConfigurationDO, userId);
        return ConfigurationDTOMapper.toDTO.apply(updatedConfigurationDO);
    }


    /**
     * I delete an existing configuration entry from the database.
     *
     * Usage:
     * <pre>{@code Request: DELETE /v1/configuration/app.bogenliga.frontend.autorefresh.active}</pre>
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_SYSTEMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setId(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        configurationComponent.delete(configurationDO, userId);
    }
}
