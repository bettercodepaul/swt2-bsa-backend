package app.bogenliga.application.business.configuration.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import app.bogenliga.application.business.common.dao.DAO;
import app.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;

/**
 * DataAccessObject for the configuration entity in the database.
 */
public class ConfigurationDAO extends DAO {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationDAO.class);

    // table name in the database
    private static final String TABLE = "t_configuration";

    /*
     * SQL queries
     */
    // @formatter:off  
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM t_configuration";
    private static final String FIND_BY_KEY =
            "SELECT * "
                    + " FROM t_configuration "
                    + " WHERE configuration_key = ?";


    /**
     * Initialize the transaction manager to provide a database connection
     */
    public ConfigurationDAO() {
        super(LOGGER, getColumnsToFieldsMap());
    }

    // @formatter:on


    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();
        columnsToFieldsMap.put("configuration_key", "configurationKey");
        columnsToFieldsMap.put("configuration_value", "configurationValue");
        return columnsToFieldsMap;
    }


    /**
     * I return all configuration entries in the database.
     *
     * @return list of {@link ConfigurationBE} or an empty list if no result found
     */
    public List<ConfigurationBE> findAll() {
        return selectEntityList(ConfigurationBE.class, FIND_ALL);
    }


    public ConfigurationBE findByKey(final String key) {
        return selectSingleEntity(ConfigurationBE.class, FIND_BY_KEY, key);
    }


    public ConfigurationBE create(final ConfigurationBE configurationBE) {
        return insertEntity(configurationBE, TABLE);
    }


    public void update(final ConfigurationBE configurationBE) {

        LOGGER.info("UPDATE " + configurationBE.getConfigurationKey() + ":" + configurationBE.getConfigurationValue());

        updateEntity(configurationBE, TABLE, "configurationKey");
    }


    public void delete(final ConfigurationBE configurationBE) {

        LOGGER.info("DELETE " + configurationBE.getConfigurationKey() + ":" + configurationBE.getConfigurationValue());

        deleteEntity(configurationBE, TABLE, "configurationKey");
    }
}



