package de.bogenliga.application.services.v1.wettkampftyp.service;

import java.security.Principal;
import java.time.OffsetDateTime;
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
import de.bogenliga.application.business.wettkampftyp.api.WettkampftypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampftypDO;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampftypBE;
import de.bogenliga.application.services.v1.wettkampftyp.model.WettkampftypDTO;
import de.bogenliga.application.services.v1.wettkampftyp.service.WettkampftypService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


/**
 * Test class for Wettkampftyp Service
 * @Author Daniel Schott daniel.schott@student.reutlingen-university.de
 */

public class WettkampftypServiceTest {
    private static final long user_Id=13;

    private static final long wettkampftyp_Id = 1;
    private static final String wettkampftyp_Name = "Liga Satzsystem";

    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private static final long version = 1234;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private WettkampftypComponent wettkampftypComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private WettkampftypService underTest;

    @Captor
    private ArgumentCaptor<WettkampftypDO> wettkampftypDOArgumentCaptor;



    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static WettkampftypBE getWettkampftypBE() {
        final WettkampftypBE expectedBE = new WettkampftypBE();
        expectedBE.setwettkampftypID(wettkampftyp_Id);
        expectedBE.setwettkampftypname(wettkampftyp_Name);

        return expectedBE;
    }

    public static WettkampftypDO getWettkampftypDO() {
        return new WettkampftypDO(
                wettkampftyp_Id,
                wettkampftyp_Name,

                created_At_Utc,
                user_Id,
                version
                );
    }

    private static WettkampftypDTO getWettkampftypDTO() {
       return new WettkampftypDTO (
                wettkampftyp_Id,
               wettkampftyp_Name,

                user_Id,
                created_At_Utc,
                version

        );

    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(user_Id));
    }

    @Test
    public void findAll(){

        // prepare test data
        final WettkampftypDO wettkampftypDO= getWettkampftypDO();
        final List<WettkampftypDO> wettkampftypDOList= Collections.singletonList(wettkampftypDO);

        // configure mocks
        when(wettkampftypComponent.findAll()).thenReturn(wettkampftypDOList);

        // call test method
        final List<WettkampftypDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final WettkampftypDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(wettkampftypDO.getId());

        // verify invocations
        verify(wettkampftypComponent).findAll();

    }

    @Test
    public void findById() {
        // prepare test data
        final WettkampftypDO wettkampftypDO = getWettkampftypDO();

        // configure mocks
        when(wettkampftypComponent.findById(anyLong())).thenReturn(wettkampftypDO);

        // call test method
        final WettkampftypDTO actual = underTest.findById(wettkampftyp_Id);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(wettkampftypDO.getId());

        // verify invocations
        verify(wettkampftypComponent).findById(wettkampftyp_Id);
    }

    @Test
    public void create() {
        // prepare test data
        final WettkampftypDTO input = getWettkampftypDTO();

        final WettkampftypDO expected = getWettkampftypDO();

        // configure mocks
        when(wettkampftypComponent.create(any(), anyLong())).thenReturn(expected);

        // call test method
        final WettkampftypDTO actual = underTest.create(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());

        // verify invocations
        verify(wettkampftypComponent).create(wettkampftypDOArgumentCaptor.capture(), anyLong());

        final WettkampftypDO createdDsbMitglied = wettkampftypDOArgumentCaptor.getValue();

        assertThat(createdDsbMitglied).isNotNull();
        assertThat(createdDsbMitglied.getId()).isEqualTo(input.getId());
    }

    @Test
    public void update() {
        // prepare test data
        final WettkampftypDTO input = getWettkampftypDTO();

        final WettkampftypDO expected = getWettkampftypDO();

        // configure mocks
        when(wettkampftypComponent.update(any(), anyLong())).thenReturn(expected);

        // call test method
        final WettkampftypDTO actual  = underTest.update(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());

        // verify invocations
        verify(wettkampftypComponent).update(wettkampftypDOArgumentCaptor.capture(), anyLong());

        final WettkampftypDO updatedWettkampftyp = wettkampftypDOArgumentCaptor.getValue();

        assertThat(updatedWettkampftyp).isNotNull();
        assertThat(updatedWettkampftyp.getId()).isEqualTo(input.getId());
    }

    @Test
    public void delete() {
        // prepare test data
        final WettkampftypDO expected = getWettkampftypDO();

        // configure mocks

        // call test method
        underTest.delete(wettkampftyp_Id, principal);

        // assert result

        // verify invocations
        verify(wettkampftypComponent).delete(wettkampftypDOArgumentCaptor.capture(), anyLong());

        final WettkampftypDO deletedWettkampftyp= wettkampftypDOArgumentCaptor.getValue();

        assertThat(deletedWettkampftyp).isNotNull();
        assertThat(deletedWettkampftyp.getId()).isEqualTo(expected.getId());
        assertThat(deletedWettkampftyp.getName()).isNullOrEmpty();
    }
}
