package de.bogenliga.application.business.configuration.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
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

        final ConfigurationDO actual = ConfigurationMapper.toDO.apply(configurationBE);

        assertThat(actual.getKey()).isEqualTo(KEY);
        assertThat(actual.getValue()).isEqualTo(VALUE);
    }


    @Test
    public void toBE() throws Exception {
        final ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey(KEY);
        configurationDO.setValue(VALUE);

        final ConfigurationBE actual = ConfigurationMapper.toBE.apply(configurationDO);

        assertThat(actual.getConfigurationKey()).isEqualTo(KEY);
        assertThat(actual.getConfigurationValue()).isEqualTo(VALUE);
    }


}