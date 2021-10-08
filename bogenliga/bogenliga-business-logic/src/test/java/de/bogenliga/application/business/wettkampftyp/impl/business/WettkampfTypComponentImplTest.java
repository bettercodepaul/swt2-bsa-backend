package de.bogenliga.application.business.wettkampftyp.impl.business;


import java.time.OffsetDateTime;
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
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.business.wettkampftyp.impl.dao.WettkampfTypDAO;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampfTypBE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Daniel Schott daniel.schott@student.reutlingen-university.de
 */
public class WettkampfTypComponentImplTest {

    private static final long user_Id=13;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private static final long version = 1234;

    private static final long wettkampftyp_Id = 1;
    private static final String wettkampftyp_Name = "Liga Satzsystem";



    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private WettkampfTypDAO wettkampftypDAO;
    @InjectMocks
    private WettkampfTypComponentImpl underTest;
    @Captor
    private ArgumentCaptor<WettkampfTypBE> wettkampftypBEArgumentCaptor;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static WettkampfTypBE getWettkampfTypBE() {
        final WettkampfTypBE expectedBE = new WettkampfTypBE();
        expectedBE.setwettkampftypID(wettkampftyp_Id);
        expectedBE.setwettkampftypname(wettkampftyp_Name);

        return expectedBE;
    }


    public static WettkampfTypDO getWettkampfTypDO() {
        return new WettkampfTypDO( wettkampftyp_Id,
                wettkampftyp_Name,

                created_At_Utc,
                user_Id,
                version
                );
    }

    @Test
    public void findAll() {
        // prepare test data
        final WettkampfTypBE expectedBE = getWettkampfTypBE();
        final List<WettkampfTypBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(wettkampftypDAO.findAll()).thenReturn(expectedBEList);


        // call test method
        final List<WettkampfTypDO> actual = underTest.findAll();


        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getwettkampftypID());
        assertThat(actual.get(0).getName())
                .isEqualTo(expectedBE.getwettkampftypname());

        // verify invocations
        verify(wettkampftypDAO).findAll();
    }

    @Test
    public void findById() {
        // prepare test data
        final WettkampfTypBE expectedBE = getWettkampfTypBE();

        // configure mocks
        when(wettkampftypDAO.findById(wettkampftyp_Id)).thenReturn(expectedBE);

        // call test method
        final WettkampfTypDO actual = underTest.findById(wettkampftyp_Id);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getwettkampftypID());

        // verify invocations
        verify(wettkampftypDAO).findById(wettkampftyp_Id);
    }

    @Test
    public void create() {
        // prepare test data
        final WettkampfTypDO input = getWettkampfTypDO();

        final WettkampfTypBE expectedBE = getWettkampfTypBE();

        // configure mocks
        when(wettkampftypDAO.create(any(WettkampfTypBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final WettkampfTypDO actual = underTest.create(input, user_Id);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(wettkampftypDAO).create(wettkampftypBEArgumentCaptor.capture(), anyLong());

        final WettkampfTypBE persistedBE = wettkampftypBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getwettkampftypID())
                .isEqualTo(input.getId());
    }

    @Test
    public void delete() {
        // prepare test data
        final WettkampfTypDO input = getWettkampfTypDO();

        final WettkampfTypBE expectedBE = getWettkampfTypBE();

        // configure mocks

        // call test method
        underTest.delete(input, user_Id);

        // assert result

        // verify invocations
        verify(wettkampftypDAO).delete(wettkampftypBEArgumentCaptor.capture(), anyLong());

        final WettkampfTypBE persistedBE = wettkampftypBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getwettkampftypID())
                .isEqualTo(input.getId());
    }
}
