package de.bogenliga.application.business.configuration.api;

import java.util.List;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.common.component.ComponentFacade;

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
     * empty list, if no configuration is found
     */
    List<ConfigurationDO> findAll();


    /**
     * Return a single configuration entry found by its id
     *
     * @param id        id of the entry
     * @return a single entry or empty list, if no configuration is found
     */
    ConfigurationDO findById(final long id);


    /**
     * I return a configuration entry with the given key.
     *
     * @param key of the configuration key-value pair
     * @return single configuration entry with the given key;
     * null, if no configuration is found
     */
    ConfigurationDO findByKey(String key);


    /**
     * I persist a new configuration in the database.
     *
     * @param configurationDO new configuration
     * @param userId current user
     * @return persisted version of the configuration
     */
    ConfigurationDO create(ConfigurationDO configurationDO, long userId);


    /**
     * I update an existing configuration. The configuration is identified by the key.
     *
     * @param configurationDO to update an existing configuration value
     * @param userId current user
     * @return persisted version of the configuration
     */
    ConfigurationDO update(ConfigurationDO configurationDO, long userId);


    /**
     * I delete an existing configuration. The configuration is identified by the key.
     *
     * @param configurationDO to delete an existing configuration key-value-pair
     * @param userId current user
     */
    void delete(ConfigurationDO configurationDO, long userId);
}
