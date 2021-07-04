package de.bogenliga.application.business.configuration.api.types;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick762
 */
public class ConfigurationDOTest {

    long id = 12345;
    String key = "Key";
    String value = "Value";
    String regex = null;


    ConfigurationDO getConfigurationDO() {
        return new ConfigurationDO(id, key, value, null);
    }


    @Test
    public void constructor() {
        ConfigurationDO underTest = getConfigurationDO();

        assertThat(underTest.getId()).isEqualTo(id);
        assertThat(underTest.getKey()).isEqualTo(key);
        assertThat(underTest.getValue()).isEqualTo(value);

        underTest = new ConfigurationDO(key, value);

        assertThat(underTest.getKey()).isEqualTo(key);
        assertThat(underTest.getValue()).isEqualTo(value);
    }


    @Test
    public void setterAndGetter() {
        ConfigurationDO underTest = getConfigurationDO();

        underTest.setId(id);

        assertThat(underTest.getId()).isEqualTo(id);

        underTest.setKey(key);

        assertThat(underTest.getKey()).isEqualTo(key);

        underTest.setValue(value);

        assertThat(underTest.getValue()).isEqualTo(value);
    }


    @Test
    public void assertEquals() {
        ConfigurationDO underTest = getConfigurationDO();
        ConfigurationDO underTest2 = getConfigurationDO();
        String notInstanceOfRoleDO = "Some String";

        assertThat(underTest)
                .isEqualTo(underTest)
                .isNotEqualTo(notInstanceOfRoleDO)
                .isEqualTo(underTest2);
    }
}
