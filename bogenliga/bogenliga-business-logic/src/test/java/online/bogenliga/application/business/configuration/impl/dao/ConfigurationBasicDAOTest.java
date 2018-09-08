package online.bogenliga.application.business.configuration.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import online.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import online.bogenliga.application.common.component.dao.BasicDAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
public class ConfigurationBasicDAOTest {

    private static final String KEY = "key";
    private static final String VALUE = "value";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private ConfigurationDAO underTest;


    @Test
    public void findAll() {
        // prepare test data
        final ConfigurationBE expectedBE = new ConfigurationBE();
        expectedBE.setConfigurationKey(KEY);
        expectedBE.setConfigurationValue(VALUE);

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<ConfigurationBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getConfigurationKey())
                .isEqualTo(expectedBE.getConfigurationKey());
        assertThat(actual.get(0).getConfigurationValue())
                .isEqualTo(expectedBE.getConfigurationValue());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }


    @Test
    public void findByKey() {
        // prepare test data
        final ConfigurationBE expectedBE = new ConfigurationBE();
        expectedBE.setConfigurationKey(KEY);
        expectedBE.setConfigurationValue(VALUE);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final ConfigurationBE actual = underTest.findByKey(KEY);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getConfigurationKey())
                .isEqualTo(expectedBE.getConfigurationKey());
        assertThat(actual.getConfigurationValue())
                .isEqualTo(expectedBE.getConfigurationValue());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }


    @Test
    public void create() {
        // prepare test data
        final ConfigurationBE input = new ConfigurationBE();
        input.setConfigurationKey(KEY);
        input.setConfigurationValue(VALUE);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final ConfigurationBE actual = underTest.create(input);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getConfigurationKey())
                .isEqualTo(input.getConfigurationKey());
        assertThat(actual.getConfigurationValue())
                .isEqualTo(input.getConfigurationValue());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }


    @Test
    public void update() {
        // prepare test data
        final ConfigurationBE input = new ConfigurationBE();
        input.setConfigurationKey(KEY);
        input.setConfigurationValue(VALUE);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any(), any())).thenReturn(input);

        // call test method
        final ConfigurationBE actual = underTest.update(input);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getConfigurationKey())
                .isEqualTo(input.getConfigurationKey());
        assertThat(actual.getConfigurationValue())
                .isEqualTo(input.getConfigurationValue());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any(), any());
    }


    @Test
    public void delete() {
        // prepare test data
        final ConfigurationBE input = new ConfigurationBE();
        input.setConfigurationKey(KEY);
        input.setConfigurationValue(VALUE);

        // configure mocks

        // call test method
        underTest.delete(input);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }
}