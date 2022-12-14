package de.bogenliga.application.services.v1.disziplin.service;

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
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.services.v1.disziplin.model.DisziplinDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/***
 * @author nico keilig
 */
public class DisziplinServiceTest {

    private static final long disziplinId = 1;
    private static final String disziplinName = "Recurve";

    private static final long USER = 0;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private DisziplinComponent disziplinComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private DisziplinService underTest;

    @Captor
    private ArgumentCaptor<DisziplinDO> disziplinVOArgumentCaptor;


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }

    @Test
    public void findAll() {
        // prepare test data
        final DisziplinDO disziplinDO = new DisziplinDO();
        disziplinDO.setDisziplinID(disziplinId);
        disziplinDO.setDisziplinName(disziplinName);

        final List<DisziplinDO> disziplinDOList = Collections.singletonList(disziplinDO);

        // configure mocks
        when(disziplinComponent.findAll()).thenReturn(disziplinDOList);

        // call test method
        final List<DisziplinDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final DisziplinDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getDisziplinId()).isEqualTo(disziplinDO.getDisziplinID());
        assertThat(actualDTO.getDisziplinName()).isEqualTo(disziplinDO.getDisziplinName());


        // verify invocations
        verify(disziplinComponent).findAll();
    }

    @Test
    public void findByKey() {
        // prepare test data
        final DisziplinDO disziplinDO = new DisziplinDO();
        disziplinDO.setDisziplinID(disziplinId);
        disziplinDO.setDisziplinName(disziplinName);

        // configure mocks
        when(disziplinComponent.findById(anyLong())).thenReturn(disziplinDO);

        // call test method
        final DisziplinDTO actual = underTest.findById(disziplinId);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getDisziplinId()).isEqualTo(disziplinDO.getDisziplinID());
        assertThat(actual.getDisziplinName()).isEqualTo(disziplinDO.getDisziplinName());


        // verify invocations
        verify(disziplinComponent).findById(disziplinId);
    }


    @Test
    public void create() {
        // prepare test data
        final DisziplinDTO input = new DisziplinDTO();
        input.setDisziplinId(disziplinId);
        input.setDisziplinName(disziplinName);

        final DisziplinDO expected = new DisziplinDO();
        expected.setDisziplinID(input.getDisziplinId());
        expected.setDisziplinName(input.getDisziplinName());

        // configure mocks
        when(disziplinComponent.create(any(), anyLong())).thenReturn(expected);

        // call test method
        final DisziplinDTO actual = underTest.create(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getDisziplinId()).isEqualTo(input.getDisziplinId());
        assertThat(actual.getDisziplinName()).isEqualTo(input.getDisziplinName());

        // verify invocations
        verify(disziplinComponent).create(disziplinVOArgumentCaptor.capture(), anyLong());

        final DisziplinDO createdConfiguration = disziplinVOArgumentCaptor.getValue();

        assertThat(createdConfiguration).isNotNull();
        assertThat(createdConfiguration.getDisziplinID()).isEqualTo(input.getDisziplinId());
        assertThat(createdConfiguration.getDisziplinName()).isEqualTo(input.getDisziplinName());

    }

    @Test
    public void update() {
        // prepare test data
        final DisziplinDTO input = new DisziplinDTO();
        input.setDisziplinId(disziplinId);
        input.setDisziplinName(disziplinName);


        final DisziplinDO toBeReturned = new DisziplinDO();
        toBeReturned.setDisziplinID(disziplinId);
        toBeReturned.setDisziplinName(disziplinName);


        final DisziplinDO expected = new DisziplinDO();
        expected.setDisziplinID(input.getDisziplinId());
        expected.setDisziplinName(input.getDisziplinName());

        // configure mocks
        when(disziplinComponent.update(any(), anyLong())).thenReturn(expected);
        when(disziplinComponent.findById(anyLong())).thenReturn(toBeReturned);

        // call test method
        final DisziplinDTO actual = underTest.update(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getDisziplinId()).isEqualTo(input.getDisziplinId());
        assertThat(actual.getDisziplinName()).isEqualTo(input.getDisziplinName());


        // verify invocations
        verify(disziplinComponent).update(disziplinVOArgumentCaptor.capture(), anyLong());

        final DisziplinDO updatedDisziplin = disziplinVOArgumentCaptor.getValue();

        assertThat(updatedDisziplin).isNotNull();
        assertThat(updatedDisziplin.getDisziplinID()).isEqualTo(input.getDisziplinId());
        assertThat(updatedDisziplin.getDisziplinName()).isEqualTo(input.getDisziplinName());
    }


    @Test
    public void delete() {
        // prepare test data
        final DisziplinDO expected = new DisziplinDO();
        expected.setDisziplinID(disziplinId);


        // call test method
        underTest.delete(disziplinId, principal);


        // verify invocations
        verify(disziplinComponent).delete(disziplinVOArgumentCaptor.capture(), anyLong());

        final DisziplinDO deletedDisziplin =disziplinVOArgumentCaptor.getValue();

        assertThat(deletedDisziplin).isNotNull();
        assertThat(deletedDisziplin.getDisziplinID()).isEqualTo(expected.getDisziplinID());
        assertThat(deletedDisziplin.getDisziplinName()).isEqualTo(expected.getDisziplinName());
    }

}
