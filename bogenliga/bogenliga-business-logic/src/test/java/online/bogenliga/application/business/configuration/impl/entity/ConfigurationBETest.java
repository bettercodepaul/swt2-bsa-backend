package online.bogenliga.application.business.configuration.impl.entity;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
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