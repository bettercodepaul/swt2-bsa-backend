package online.bogenliga.application.business.configuration.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import online.bogenliga.application.business.configuration.api.ConfigurationComponent;
import online.bogenliga.application.business.configuration.api.types.ConfigurationVO;
import online.bogenliga.application.business.configuration.impl.dao.ConfigurationDAO;
import online.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import online.bogenliga.application.business.configuration.impl.mapper.ConfigurationMapper;
import online.bogenliga.application.common.validation.Preconditions;

/**
 * I´m the implementation of {@link ConfigurationComponent}.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class ConfigurationComponentImpl implements ConfigurationComponent {

    private static final String PRECONDITION_MSG_CONFIGURATION = "ConfigurationVO must not be null";
    private static final String PRECONDITION_MSG_CONFIGURATION_KEY = "ConfigurationVO key must not be null or empty";
    private static final String PRECONDITION_MSG_CONFIGURATION_VALUE = "ConfigurationVO value must not be null";

    private final ConfigurationDAO configurationDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     *
     * @param configurationDAO to access the database and return configuration representations
     */
    @Autowired
    public ConfigurationComponentImpl(final ConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }


    @Override
    public List<ConfigurationVO> findAll() {
        final List<ConfigurationBE> configurationBEList = configurationDAO.findAll();
        return configurationBEList.stream().map(ConfigurationMapper.toVO).collect(Collectors.toList());
    }


    @Override
    public ConfigurationVO findByKey(final String key) {
        Preconditions.checkNotNullOrEmpty(key, PRECONDITION_MSG_CONFIGURATION_KEY);

        return ConfigurationMapper.toVO.apply(configurationDAO.findByKey(key));
    }


    @Override
    public ConfigurationVO create(final ConfigurationVO configurationVO) {
        Preconditions.checkNotNull(configurationVO, PRECONDITION_MSG_CONFIGURATION);
        Preconditions.checkNotNullOrEmpty(configurationVO.getKey(), PRECONDITION_MSG_CONFIGURATION_KEY);
        Preconditions.checkNotNull(configurationVO.getValue(),
                PRECONDITION_MSG_CONFIGURATION_VALUE);

        final ConfigurationBE configurationBE = ConfigurationMapper.toBE.apply(configurationVO);
        return ConfigurationMapper.toVO.apply(configurationDAO.create(configurationBE));
    }


    @Override
    public ConfigurationVO update(final ConfigurationVO configurationVO) {
        Preconditions.checkNotNull(configurationVO, PRECONDITION_MSG_CONFIGURATION);
        Preconditions.checkNotNullOrEmpty(configurationVO.getKey(), PRECONDITION_MSG_CONFIGURATION_KEY);
        Preconditions.checkNotNull(configurationVO.getValue(),
                PRECONDITION_MSG_CONFIGURATION_VALUE);

        final ConfigurationBE configurationBE = ConfigurationMapper.toBE.apply(configurationVO);
        return ConfigurationMapper.toVO.apply(configurationDAO.update(configurationBE));
    }


    @Override
    public void delete(final ConfigurationVO configurationVO) {
        Preconditions.checkNotNull(configurationVO, PRECONDITION_MSG_CONFIGURATION);
        Preconditions.checkNotNullOrEmpty(configurationVO.getKey(), PRECONDITION_MSG_CONFIGURATION_KEY);

        final ConfigurationBE configurationBE = ConfigurationMapper.toBE.apply(configurationVO);
        configurationDAO.delete(configurationBE);
    }
}
