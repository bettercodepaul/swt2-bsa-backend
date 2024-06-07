package de.bogenliga.application.business.configuration.impl.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.configuration.api.ConfigurationComponent;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.configuration.impl.dao.ConfigurationDAO;
import de.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import de.bogenliga.application.business.configuration.impl.mapper.ConfigurationMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * I´m the implementation of {@link ConfigurationComponent}.
 * The global application configuration consists of a key-value pairs.
 * The configuration is stored in the database.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@Component
public class ConfigurationComponentImpl implements ConfigurationComponent {

    private static final String PRECONDITION_MSG_CONFIGURATION = "ConfigurationDO must not be null";
    private static final String PRECONDITION_MSG_CONFIGURATION_KEY = "ConfigurationDO key must not be null or empty";
    private static final String PRECONDITION_MSG_CONFIGURATION_VALUE = "ConfigurationDO value must not be null";

    private final ConfigurationDAO configurationDAO;


    /**
     * Constructor
     * dependency injection with {@link Autowired}
     *
     * @param configurationDAO to access the database and return configuration representations
     */
    @Autowired
    public ConfigurationComponentImpl(final ConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }


    @Override
    public List<ConfigurationDO> findAll() {
        final List<ConfigurationBE> configurationBEList = configurationDAO.findAll();
        return configurationBEList.stream().map(ConfigurationMapper.toDO).toList();
    }


    @Override
    public ConfigurationDO findById(long id) {
        final ConfigurationBE result = configurationDAO.findById(id);
        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "no match found for id ");
        }
        return ConfigurationMapper.toDO.apply(result);
    }


    @Override
    public ConfigurationDO findByKey(final String key) {
        Preconditions.checkNotNullOrEmpty(key, PRECONDITION_MSG_CONFIGURATION_KEY);

        final ConfigurationBE result = configurationDAO.findByKey(key);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for key '%s'", key));
        }

        return ConfigurationMapper.toDO.apply(result);
    }


    @Override
    public ConfigurationDO create(final ConfigurationDO configurationDO, final long currentUser) {
        Preconditions.checkNotNull(configurationDO, PRECONDITION_MSG_CONFIGURATION);
        Preconditions.checkNotNullOrEmpty(configurationDO.getKey(), PRECONDITION_MSG_CONFIGURATION_KEY);
        Preconditions.checkNotNull(configurationDO.getValue(),
                PRECONDITION_MSG_CONFIGURATION_VALUE);

        final ConfigurationBE configurationBE = ConfigurationMapper.toBE.apply(configurationDO);
        configurationBE.setCreatedByUserId(currentUser);
        final ConfigurationBE persistedConfigurationBE = configurationDAO.create(configurationBE);
        return ConfigurationMapper.toDO.apply(persistedConfigurationBE);
    }


    @Override
    public ConfigurationDO update(final ConfigurationDO configurationDO, final long currentUser) {
        Preconditions.checkNotNull(configurationDO, PRECONDITION_MSG_CONFIGURATION);
        Preconditions.checkNotNullOrEmpty(configurationDO.getKey(), PRECONDITION_MSG_CONFIGURATION_KEY);
        Preconditions.checkNotNull(configurationDO.getValue(),
                PRECONDITION_MSG_CONFIGURATION_VALUE);

        final ConfigurationBE configurationBE = ConfigurationMapper.toBE.apply(configurationDO);
        configurationBE.setLastModifiedByUserId(currentUser);
        final ConfigurationBE persistedConfigurationBE = configurationDAO.update(configurationBE);
        return ConfigurationMapper.toDO.apply(persistedConfigurationBE);
    }


    @Override
    public void delete(final ConfigurationDO configurationDO, final long currentUser) {
        Preconditions.checkNotNull(configurationDO, PRECONDITION_MSG_CONFIGURATION);
        Preconditions.checkNotNullOrEmpty(configurationDO.getKey(), PRECONDITION_MSG_CONFIGURATION_KEY);

        final ConfigurationBE configurationBE = ConfigurationMapper.toBE.apply(configurationDO);
        configurationDAO.delete(configurationBE);
    }
}
