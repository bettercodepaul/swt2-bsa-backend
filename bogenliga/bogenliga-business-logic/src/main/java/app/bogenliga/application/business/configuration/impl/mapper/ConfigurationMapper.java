package app.bogenliga.application.business.configuration.impl.mapper;

import java.util.function.Function;
import app.bogenliga.application.business.configuration.api.types.ConfigurationVO;
import app.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import app.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public interface ConfigurationMapper extends ValueObjectMapper {

    public static Function<ConfigurationBE, ConfigurationVO> toVO = (be) -> {
        String key = be.getConfigurationKey();
        String value = be.getConfigurationValue();

        return new ConfigurationVO(key, value);
    };

    public static Function<ConfigurationVO, ConfigurationBE> toBE = (vo) -> {
        String key = vo.getKey();
        String value = vo.getValue();

        ConfigurationBE configurationBE = new ConfigurationBE();
        configurationBE.setConfigurationKey(key);
        configurationBE.setConfigurationValue(value);

        return configurationBE;
    };
}
