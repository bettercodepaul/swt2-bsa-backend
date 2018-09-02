package online.bogenliga.application.business.configuration.impl.business;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import online.bogenliga.application.business.configuration.api.types.ConfigurationVO;
import online.bogenliga.application.business.configuration.impl.dao.ConfigurationDAO;
import online.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import online.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
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
public class ConfigurationComponentImplTest {

    private static final String KEY = "key";
    private static final String VALUE = "value";
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private ConfigurationDAO configurationDAO;
    @InjectMocks
    private ConfigurationComponentImpl underTest;
    @Captor
    private ArgumentCaptor<ConfigurationBE> configurationBEArgumentCaptor;


    @Test
    public void findAll() {
        // prepare test data
        final ConfigurationBE expectedBE = new ConfigurationBE();
        expectedBE.setConfigurationKey(KEY);
        expectedBE.setConfigurationValue(VALUE);
        final List<ConfigurationBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(configurationDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<ConfigurationVO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getKey())
                .isEqualTo(expectedBE.getConfigurationKey());
        assertThat(actual.get(0).getValue())
                .isEqualTo(expectedBE.getConfigurationValue());

        // verify invocations
        verify(configurationDAO).findAll();
    }


    @Test
    public void findByKey() {
        // prepare test data
        final ConfigurationBE expectedBE = new ConfigurationBE();
        expectedBE.setConfigurationKey(KEY);
        expectedBE.setConfigurationValue(VALUE);

        // configure mocks
        when(configurationDAO.findByKey(anyString())).thenReturn(expectedBE);

        // call test method
        final ConfigurationVO actual = underTest.findByKey(KEY);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getKey())
                .isEqualTo(expectedBE.getConfigurationKey());
        assertThat(actual.getValue())
                .isEqualTo(expectedBE.getConfigurationValue());

        // verify invocations
        verify(configurationDAO).findByKey(KEY);
    }


    @Test
    public void findByKey_withoutKey_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findByKey(null))
                .withMessageContaining("key")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void findByKey_withEmptyKey_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findByKey(""))
                .withMessageContaining("key")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void create() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey(KEY);
        input.setValue(VALUE);

        final ConfigurationBE expectedBE = new ConfigurationBE();
        expectedBE.setConfigurationKey(input.getKey());
        expectedBE.setConfigurationValue(input.getValue());

        // configure mocks
        when(configurationDAO.create(any(ConfigurationBE.class))).thenReturn(expectedBE);

        // call test method
        final ConfigurationVO actual = underTest.create(input);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getKey())
                .isEqualTo(input.getKey());
        assertThat(actual.getValue())
                .isEqualTo(input.getValue());

        // verify invocations
        verify(configurationDAO).create(configurationBEArgumentCaptor.capture());

        final ConfigurationBE persistedBE = configurationBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getConfigurationKey())
                .isEqualTo(input.getKey());
        assertThat(persistedBE.getConfigurationValue())
                .isEqualTo(input.getValue());
    }


    @Test
    public void create_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(null))
                .withMessageContaining("ConfigurationVO")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void create_withoutKey_shouldThrowException() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey(null);
        input.setValue(VALUE);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(input))
                .withMessageContaining("key")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void create_withEmptyKey_shouldThrowException() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey("");
        input.setValue(VALUE);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(input))
                .withMessageContaining("key")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void create_withoutValue_shouldThrowException() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey(KEY);
        input.setValue(null);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(input))
                .withMessageContaining("value")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void update() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey(KEY);
        input.setValue(VALUE);

        final ConfigurationBE expectedBE = new ConfigurationBE();
        expectedBE.setConfigurationKey(input.getKey());
        expectedBE.setConfigurationValue(input.getValue());

        // configure mocks
        when(configurationDAO.update(any(ConfigurationBE.class))).thenReturn(expectedBE);

        // call test method
        final ConfigurationVO actual = underTest.update(input);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getKey())
                .isEqualTo(input.getKey());
        assertThat(actual.getValue())
                .isEqualTo(input.getValue());

        // verify invocations
        verify(configurationDAO).update(configurationBEArgumentCaptor.capture());

        final ConfigurationBE persistedBE = configurationBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getConfigurationKey())
                .isEqualTo(input.getKey());
        assertThat(persistedBE.getConfigurationValue())
                .isEqualTo(input.getValue());
    }


    @Test
    public void update_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(null))
                .withMessageContaining("ConfigurationVO")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void update_withoutKey_shouldThrowException() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey(null);
        input.setValue(VALUE);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(input))
                .withMessageContaining("key")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void update_withEmptyKey_shouldThrowException() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey("");
        input.setValue(VALUE);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(input))
                .withMessageContaining("key")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void update_withoutValue_shouldThrowException() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey(KEY);
        input.setValue(null);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(input))
                .withMessageContaining("value")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void delete() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey(KEY);
        input.setValue(VALUE);

        final ConfigurationBE expectedBE = new ConfigurationBE();
        expectedBE.setConfigurationKey(input.getKey());
        expectedBE.setConfigurationValue(input.getValue());

        // configure mocks

        // call test method
        underTest.delete(input);

        // assert result

        // verify invocations
        verify(configurationDAO).delete(configurationBEArgumentCaptor.capture());

        final ConfigurationBE persistedBE = configurationBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getConfigurationKey())
                .isEqualTo(input.getKey());
    }


    @Test
    public void delete_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(null))
                .withMessageContaining("ConfigurationVO")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void delete_withoutKey_shouldThrowException() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey(null);
        input.setValue(VALUE);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(input))
                .withMessageContaining("key")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }


    @Test
    public void delete_withEmptyKey_shouldThrowException() {
        // prepare test data
        final ConfigurationVO input = new ConfigurationVO();
        input.setKey("");
        input.setValue(VALUE);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(input))
                .withMessageContaining("key")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(configurationDAO);
    }
}