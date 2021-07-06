package de.bogenliga.application.business.configuration.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the configuration entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class ConfigurationDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationDAO.class);

    // table name in the database
    private static final String TABLE = "configuration";

    // business entity parameter names
    private static final String CONFIGURATION_BE_ID = "configurationId";
    private static final String CONFIGURATION_BE_KEY = "configurationKey";
    private static final String CONFIGURATION_BE_VALUE = "configurationValue";
    private static final String CONFIGURATION_BE_REGEX = "configurationRegex";

    private static final String CONFIGURATION_TABLE_ID = "configuration_id";
    private static final String CONFIGURATION_TABLE_KEY = "configuration_key";
    private static final String CONFIGURATION_TABLE_VALUE = "configuration_value";
    private static final String CONFIGURATION_TABLE_REGEX = "configuration_regex";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<ConfigurationBE> CONFIG = new BusinessEntityConfiguration<>(
            ConfigurationBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);
    /*
     * SQL queries
     */
    private static final String SELECT = "SELECT * ";
    private static final String FIND_ALL =
            SELECT
                    + " FROM configuration";
    private static final String FIND_BY_KEY =
            SELECT
                    + " FROM configuration "
                    + " WHERE configuration_key = ?";
    private static final String FIND_BY_ID =
            SELECT
                    + " FROM configuration"
                    + " WHERE configuration_id = ?";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public ConfigurationDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();
        columnsToFieldsMap.put(CONFIGURATION_TABLE_ID, CONFIGURATION_BE_ID);
        columnsToFieldsMap.put(CONFIGURATION_TABLE_KEY, CONFIGURATION_BE_KEY);
        columnsToFieldsMap.put(CONFIGURATION_TABLE_VALUE, CONFIGURATION_BE_VALUE);
        columnsToFieldsMap.put(CONFIGURATION_TABLE_REGEX, CONFIGURATION_BE_REGEX);

        //fill null fields
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

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


    public ConfigurationBE create(final ConfigurationBE configurationBE, final long currentUserId) {
        return basicDao.insertEntity(CONFIG, configurationBE);
    }

    public ConfigurationBE findById(final long id) {
        return basicDao.selectSingleEntity(CONFIG, FIND_BY_ID, id);
    }

    public ConfigurationBE update(final ConfigurationBE configurationBE, final long currentUserId) {
        return basicDao.updateEntity(CONFIG, configurationBE, CONFIGURATION_BE_KEY);
    }

    public void delete(final ConfigurationBE configurationBE, final long currentUserId) {
        basicDao.deleteEntity(CONFIG, configurationBE, CONFIGURATION_BE_KEY);
    }
}



