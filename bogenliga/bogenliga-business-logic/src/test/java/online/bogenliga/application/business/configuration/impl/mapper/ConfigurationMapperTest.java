package online.bogenliga.application.business.configuration.impl.mapper;

import org.junit.Test;
import online.bogenliga.application.business.configuration.api.types.ConfigurationVO;
import online.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 */
public class ConfigurationMapperTest {

    private static final String KEY = "key";
    private static final String VALUE = "value";


    @Test
    public void toVO() throws Exception {
        final ConfigurationBE configurationBE = new ConfigurationBE();
        configurationBE.setConfigurationKey(KEY);
        configurationBE.setConfigurationValue(VALUE);

        final ConfigurationVO actual = ConfigurationMapper.toVO.apply(configurationBE);

        assertThat(actual.getKey()).isEqualTo(KEY);
        assertThat(actual.getValue()).isEqualTo(VALUE);
    }


    @Test
    public void toBE() throws Exception {
        final ConfigurationVO configurationVO = new ConfigurationVO();
        configurationVO.setKey(KEY);
        configurationVO.setValue(VALUE);

        final ConfigurationBE actual = ConfigurationMapper.toBE.apply(configurationVO);

        assertThat(actual.getConfigurationKey()).isEqualTo(KEY);
        assertThat(actual.getConfigurationValue()).isEqualTo(VALUE);
    }


}