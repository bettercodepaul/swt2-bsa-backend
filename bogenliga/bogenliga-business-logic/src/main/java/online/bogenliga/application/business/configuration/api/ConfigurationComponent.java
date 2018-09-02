package online.bogenliga.application.business.configuration.api;

import java.util.List;
import online.bogenliga.application.business.configuration.api.types.ConfigurationVO;
import online.bogenliga.application.common.component.ComponentFacade;

/**
 * I´m responsible for the configuration database requests.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface ConfigurationComponent extends ComponentFacade {


    /**
     * I return all configuration entries.
     *
     * @return list of all configuration entries in the database;
     * empty list, if not configuration is found
     */
    List<ConfigurationVO> findAll();


    /**
     * I return a configuration entry with the given key.
     *
     * @param key of the configuration key-value pair
     * @return single configuration entry with the given key;
     * null, if no configuration is found
     */
    ConfigurationVO findByKey(String key);


    /**
     * I persist a new configuration in the database.
     *
     * @param configurationVO new configuration
     * @return persisted version of the configuration
     */
    ConfigurationVO create(ConfigurationVO configurationVO);


    /**
     * I update an existing configuration. The configuration is identified by the key.
     *
     * @param configurationVO to update an existing configuration value
     * @return persisted version of the configuration
     */
    ConfigurationVO update(ConfigurationVO configurationVO);


    /**
     * I delete an existing configuration. The configuration is identified by the key.
     *
     * @param configurationVO to delete an existing configuration key-value-pair
     */
    void delete(ConfigurationVO configurationVO);
}
