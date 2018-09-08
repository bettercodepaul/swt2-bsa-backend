package online.bogenliga.application.business.configuration.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import online.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import online.bogenliga.application.common.component.dao.BasicDAO;
import online.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import online.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the configuration entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 */
@Repository
public class ConfigurationDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationDAO.class);

    // table name in the database
    private static final String TABLE = "t_configuration";
    // business entity parameter names
    private static final String CONFIGURATION_BE_KEY = "configurationKey";
    private static final String CONFIGURATION_BE_VALUE = "configurationValue";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<ConfigurationBE> CONFIG = new BusinessEntityConfiguration<>(
            ConfigurationBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);
    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM t_configuration";
    private static final String FIND_BY_KEY =
            "SELECT * "
                    + " FROM t_configuration "
                    + " WHERE configuration_key = ?";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao
     */
    @Autowired
    public ConfigurationDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();
        columnsToFieldsMap.put("configuration_key", CONFIGURATION_BE_KEY);
        columnsToFieldsMap.put("configuration_value", CONFIGURATION_BE_VALUE);
        return columnsToFieldsMap;
    }


    /**
     * I return all configuration entries in the database.
     *
     * @return list of {@link ConfigurationBE} or an empty list if no result found
     */
    public List<ConfigurationBE> findAll() {
        return basicDao.selectEntityList(CONFIG, FIND_ALL);
    }


    public ConfigurationBE findByKey(final String key) {
        return basicDao.selectSingleEntity(CONFIG, FIND_BY_KEY, key);
    }


    public ConfigurationBE create(final ConfigurationBE configurationBE) {
        return basicDao.insertEntity(CONFIG, configurationBE);
    }


    public ConfigurationBE update(final ConfigurationBE configurationBE) {
        return basicDao.updateEntity(CONFIG, configurationBE, CONFIGURATION_BE_KEY, FIND_BY_KEY);
    }


    public void delete(final ConfigurationBE configurationBE) {
        basicDao.deleteEntity(CONFIG, configurationBE, CONFIGURATION_BE_KEY);
    }
}



