package de.bogenliga.application.services.v1.configuration.service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.configuration.api.ConfigurationComponent;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;
import static org.assertj.core.api.Assertions.assertThat;
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
public class ConfigurationServiceTest {

    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final long USER = 0;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ConfigurationComponent configurationComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private ConfigurationService underTest;

    @Captor
    private ArgumentCaptor<ConfigurationDO> configurationVOArgumentCaptor;


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }

    @Test
    public void findAll() {
        // prepare test data
        final ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey(KEY);
        configurationDO.setValue(VALUE);

        final List<ConfigurationDO> configurationDOList = Collections.singletonList(configurationDO);

        // configure mocks
        when(configurationComponent.findAll()).thenReturn(configurationDOList);

        // call test method
        final List<ConfigurationDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final ConfigurationDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getKey()).isEqualTo(configurationDO.getKey());
        assertThat(actualDTO.getValue()).isEqualTo(configurationDO.getValue());

        // verify invocations
        verify(configurationComponent).findAll();
    }


    @Test
    public void findByKey() {
        // prepare test data
        final ConfigurationDO configurationDO = new ConfigurationDO();
        configurationDO.setKey(KEY);
        configurationDO.setValue(VALUE);

        // configure mocks
        when(configurationComponent.findByKey(any())).thenReturn(configurationDO);

        // call test method
        final ConfigurationDTO actual = underTest.findByKey(KEY);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getKey()).isEqualTo(configurationDO.getKey());
        assertThat(actual.getValue()).isEqualTo(configurationDO.getValue());

        // verify invocations
        verify(configurationComponent).findByKey(KEY);
    }


    @Test
    public void create() {
        // prepare test data
        final ConfigurationDTO input = new ConfigurationDTO();
        input.setKey(KEY);
        input.setValue(VALUE);

        final ConfigurationDO expected = new ConfigurationDO();
        expected.setKey(input.getKey());
        expected.setValue(input.getValue());

        // configure mocks
        when(configurationComponent.create(any(), anyLong())).thenReturn(expected);

        // call test method
        final ConfigurationDTO actual = underTest.create(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getKey()).isEqualTo(input.getKey());
        assertThat(actual.getValue()).isEqualTo(input.getValue());

        // verify invocations
        verify(configurationComponent).create(configurationVOArgumentCaptor.capture(), anyLong());

        final ConfigurationDO createdConfiguration = configurationVOArgumentCaptor.getValue();

        assertThat(createdConfiguration).isNotNull();
        assertThat(createdConfiguration.getKey()).isEqualTo(input.getKey());
        assertThat(createdConfiguration.getValue()).isEqualTo(input.getValue());
    }


    @Test
    public void update() {
        // prepare test data
        final ConfigurationDTO input = new ConfigurationDTO();
        input.setKey(KEY);
        input.setValue(VALUE);

        final ConfigurationDO expected = new ConfigurationDO();
        expected.setKey(input.getKey());
        expected.setValue(input.getValue());

        // configure mocks
        when(configurationComponent.update(any(), anyLong())).thenReturn(expected);

        // call test method
        final ConfigurationDTO actual = underTest.update(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getKey()).isEqualTo(input.getKey());
        assertThat(actual.getValue()).isEqualTo(input.getValue());

        // verify invocations
        verify(configurationComponent).update(configurationVOArgumentCaptor.capture(), anyLong());

        final ConfigurationDO updatedConfiguration = configurationVOArgumentCaptor.getValue();

        assertThat(updatedConfiguration).isNotNull();
        assertThat(updatedConfiguration.getKey()).isEqualTo(input.getKey());
        assertThat(updatedConfiguration.getValue()).isEqualTo(input.getValue());
    }


    @Test
    public void delete() {
        // prepare test data
        final ConfigurationDO expected = new ConfigurationDO();
        expected.setKey(KEY);

        // configure mocks

        // call test method
        underTest.delete(KEY, principal);

        // assert result

        // verify invocations
        verify(configurationComponent).delete(configurationVOArgumentCaptor.capture(), anyLong());

        final ConfigurationDO deletedConfiguration = configurationVOArgumentCaptor.getValue();

        assertThat(deletedConfiguration).isNotNull();
        assertThat(deletedConfiguration.getKey()).isEqualTo(expected.getKey());
        assertThat(deletedConfiguration.getValue()).isNullOrEmpty();
    }
}