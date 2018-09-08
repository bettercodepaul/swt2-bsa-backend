package online.bogenliga.application.services.v1.configuration.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import online.bogenliga.application.business.configuration.api.ConfigurationComponent;
import online.bogenliga.application.business.configuration.api.types.ConfigurationVO;
import online.bogenliga.application.common.service.ServiceFacade;
import online.bogenliga.application.common.validation.Preconditions;
import online.bogenliga.application.services.v1.configuration.mapper.ConfigurationDTOMapper;
import online.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;

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
@RequestMapping("v1/configuration")
public class ConfigurationService implements ServiceFacade {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

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
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ConfigurationDTO> findAll() {
        logger.debug("Receive 'findAll' request");

        final List<ConfigurationVO> configurationVOList = configurationComponent.findAll();
        return configurationVOList.stream().map(ConfigurationDTOMapper.toDTO).collect(Collectors.toList());
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
    @RequestMapping(value = "{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ConfigurationDTO findByKey(@PathVariable("key") final String key) {
        Preconditions.checkNotNullOrEmpty(key, "Key string must not null or empty");

        logger.debug("Receive 'findByKey' request with key '{}'", key);

        final ConfigurationVO configurationVO = configurationComponent.findByKey(key);
        return ConfigurationDTOMapper.toDTO.apply(configurationVO);
    }


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
     *
     * @return list of {@link ConfigurationDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ConfigurationDTO create(@RequestBody final ConfigurationDTO configurationDTO) {
        Preconditions.checkNotNull(configurationDTO, "ConfigurationDTO must not null");
        Preconditions.checkNotNullOrEmpty(configurationDTO.getKey(), "ConfigurationDTO key must not null or empty");
        Preconditions.checkNotNull(configurationDTO.getValue(), "ConfigurationDTO value must not null");

        logger.debug("Receive 'create' request with key '{}' and value '{}'", configurationDTO.getKey(),
                configurationDTO.getValue());

        final ConfigurationVO newConfigurationVO = ConfigurationDTOMapper.toVO.apply(configurationDTO);
        final ConfigurationVO savedConfigurationVO = configurationComponent.create(newConfigurationVO);
        return ConfigurationDTOMapper.toDTO.apply(savedConfigurationVO);
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
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConfigurationDTO update(@RequestBody final ConfigurationDTO configurationDTO) {
        Preconditions.checkNotNull(configurationDTO, "ConfigurationDTO must not null");
        Preconditions.checkNotNullOrEmpty(configurationDTO.getKey(), "ConfigurationDTO key must not null or empty");
        Preconditions.checkNotNull(configurationDTO.getValue(), "ConfigurationDTO value must not null");

        logger.debug("Receive 'update' request with key '{}' and value '{}'", configurationDTO.getKey(),
                configurationDTO.getValue());

        final ConfigurationVO newConfigurationVO = ConfigurationDTOMapper.toVO.apply(configurationDTO);
        final ConfigurationVO updatedConfigurationVO = configurationComponent.update(newConfigurationVO);
        return ConfigurationDTOMapper.toDTO.apply(updatedConfigurationVO);
    }


    /**
     * I delete an existing configuration entry from the database.
     *
     * Usage:
     * <pre>{@code Request: DELETE /v1/configuration/app.bogenliga.frontend.autorefresh.active}</pre>
     */
    @RequestMapping(value = "{key}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("key") final String key) {
        Preconditions.checkNotNullOrEmpty(key, "Key string must not null or empty");

        logger.debug("Receive 'delete' request with key '{}'", key);

        // allow value == null, the value will be ignored
        final ConfigurationVO configurationVO = new ConfigurationVO(key, null);
        configurationComponent.delete(configurationVO);
    }
}
