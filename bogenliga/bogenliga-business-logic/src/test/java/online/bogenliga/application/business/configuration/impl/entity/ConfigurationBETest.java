package online.bogenliga.application.business.configuration.impl.entity;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class ConfigurationBETest {

    private static final String KEY = "key";
    private static final String VALUE = "value";


    @Test
    public void assertToString() {
        final ConfigurationBE underTest = new ConfigurationBE();
        underTest.setConfigurationKey(KEY);
        underTest.setConfigurationValue(VALUE);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(KEY)
                .contains(VALUE);
    }


    @Test
    public void assertToString_withoutValue() {
        final ConfigurationBE underTest = new ConfigurationBE();
        underTest.setConfigurationKey(KEY);
        underTest.setConfigurationValue(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(KEY)
                .contains("null");
    }
}