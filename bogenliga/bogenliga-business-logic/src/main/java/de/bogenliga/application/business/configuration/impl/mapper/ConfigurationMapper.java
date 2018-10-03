package de.bogenliga.application.business.configuration.impl.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * I convert the {@link ConfigurationBE} and the {@link ConfigurationDO}
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public final class ConfigurationMapper implements ValueObjectMapper {

    /**
     * Converts a {@link ConfigurationBE} to a {@link ConfigurationDO}
     */
    public static final Function<ConfigurationBE, ConfigurationDO> toDO = be -> {
        final String key = be.getConfigurationKey();
        final String value = be.getConfigurationValue();

        return new ConfigurationDO(key, value);
    };

    /**
     * Converts a {@link ConfigurationDO} to a {@link ConfigurationBE}
     */
    public static final Function<ConfigurationDO, ConfigurationBE> toBE = vo -> {
        final String key = vo.getKey();
        final String value = vo.getValue();

        ConfigurationBE configurationBE = new ConfigurationBE();
        configurationBE.setConfigurationKey(key);
        configurationBE.setConfigurationValue(value);

        return configurationBE;
    };


    /**
     * Private constructor
     */
    private ConfigurationMapper() {
        // empty private constructor
    }
}
