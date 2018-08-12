package app.bogenliga.application.business.configuration.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import app.bogenliga.application.business.configuration.api.ConfigurationComponent;
import app.bogenliga.application.business.configuration.api.types.ConfigurationVO;
import app.bogenliga.application.business.configuration.impl.dao.ConfigurationDAO;
import app.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import app.bogenliga.application.business.configuration.impl.mapper.ConfigurationMapper;
import app.bogenliga.application.common.validation.Preconditions;

/**
 * I´m the implementation of {@link ConfigurationComponent}.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class ConfigurationComponentImpl implements ConfigurationComponent {

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
        Preconditions.checkNotNullOrEmpty(key, "Configuration key must not be null or empty");

        return ConfigurationMapper.toVO.apply(configurationDAO.findByKey(key));
    }


    @Override
    public ConfigurationVO create(final ConfigurationVO configurationVO) {
        Preconditions.checkNotNull(configurationVO, "ConfigurationVO must not be null");
        Preconditions.checkNotNullOrEmpty(configurationVO.getKey(), "ConfigurationVO key must not be null or empty");
        Preconditions.checkNotNull(configurationVO.getValue(),
                "ConfigurationVO value must not be null");

        final ConfigurationBE configurationBE = ConfigurationMapper.toBE.apply(configurationVO);
        return ConfigurationMapper.toVO.apply(configurationDAO.create(configurationBE));
    }


    @Override
    public void update(final ConfigurationVO configurationVO) {
        Preconditions.checkNotNull(configurationVO, "ConfigurationVO must not be null");
        Preconditions.checkNotNullOrEmpty(configurationVO.getKey(), "ConfigurationVO key must not be null or empty");
        Preconditions.checkNotNull(configurationVO.getValue(),
                "ConfigurationVO value must not be null");

        final ConfigurationBE configurationBE = ConfigurationMapper.toBE.apply(configurationVO);
        configurationDAO.update(configurationBE);
    }


    @Override
    public void delete(final ConfigurationVO configurationVO) {
        Preconditions.checkNotNull(configurationVO, "ConfigurationVO must not be null");
        Preconditions.checkNotNullOrEmpty(configurationVO.getKey(), "ConfigurationVO key must not be null or empty");

        final ConfigurationBE configurationBE = ConfigurationMapper.toBE.apply(configurationVO);
        configurationDAO.delete(configurationBE);
    }
}
