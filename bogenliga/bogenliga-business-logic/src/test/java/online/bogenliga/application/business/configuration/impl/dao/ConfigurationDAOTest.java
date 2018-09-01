package online.bogenliga.application.business.configuration.impl.dao;

import java.util.List;
import org.junit.Test;
import online.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 */
public class ConfigurationDAOTest {

    @Test
    public void findAll() throws Exception {

        final ConfigurationDAO underTest = new ConfigurationDAO();


        final List<ConfigurationBE> actual = underTest.findAll();


        assertThat(actual).isNotEmpty();

        actual.forEach(confg -> {
            System.out.println(confg.getConfigurationKey() + ":" + confg.getConfigurationValue());
        });
    }


    @Test
    public void findByKey() throws Exception {

        final ConfigurationDAO underTest = new ConfigurationDAO();

        final ConfigurationBE actual = underTest.findByKey("app.bogenliga.frontend.autorefresh.active");

        assertThat(actual).isNotNull();

        System.out.println(actual.getConfigurationKey() + ":" + actual.getConfigurationValue());

    }


    @Test
    public void create() throws Exception {
        final ConfigurationBE configurationBE = new ConfigurationBE();
        configurationBE.setConfigurationKey("key");
        configurationBE.setConfigurationValue("value");

        final ConfigurationDAO underTest = new ConfigurationDAO();

        final ConfigurationBE actual = underTest.create(configurationBE);

        assertThat(actual).isNotNull();
        assertThat(actual.getConfigurationKey()).isEqualTo(configurationBE.getConfigurationKey());
        assertThat(actual.getConfigurationValue()).isEqualTo(configurationBE.getConfigurationValue());

        actual.setConfigurationValue("updated");

        System.out.println(
                ("UPDATE " + actual.getConfigurationKey() + ":" + actual.getConfigurationValue()));


        underTest.update(actual);

        System.out.println(
                ("DELETE " + actual.getConfigurationKey() + ":" + actual.getConfigurationValue()));

        underTest.delete(actual);
    }

}