package de.bogenliga.application.business.configuration.api.types;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick762
 */
public class ConfigurationDOTest {

    @Test
    public void constructor(){
        long id = 12345;
        String key = "Key";
        String value = "Value";

        ConfigurationDO underTest = new ConfigurationDO(id, key, value);

        assertThat(underTest.getId()).isEqualTo(id);
        assertThat(underTest.getKey()).isEqualTo(key);
        assertThat(underTest.getValue()).isEqualTo(value);
    }

    @Test
    public void setterAndGetter(){
        long id = 12345;
        String key = "Key";
        String value = "Value";

        ConfigurationDO underTest = new ConfigurationDO((long)54321, null, null);

        underTest.setId(id);

        assertThat(underTest.getId()).isEqualTo(id);

        underTest.setKey(key);

        assertThat(underTest.getKey()).isEqualTo(key);

        underTest.setValue(value);

        assertThat(underTest.getValue()).isEqualTo(value);
    }

    @Test
    public void assertEquals(){
        long id = 12345;
        String key = "Key";
        String value = "Value";

        ConfigurationDO underTest = new ConfigurationDO(id, key, value);
        ConfigurationDO underTestClone = underTest;
        ConfigurationDO underTest2 = new ConfigurationDO(id, key, value);
        String notInstanceOfRoleDO = "Some String";


        assertThat(underTest.equals(underTestClone)).isTrue();
        assertThat(underTest.equals(notInstanceOfRoleDO)).isFalse();
        assertThat(underTest.equals(underTest2)).isTrue();

    }
}
