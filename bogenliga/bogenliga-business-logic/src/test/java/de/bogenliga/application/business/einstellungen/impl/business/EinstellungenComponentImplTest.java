package de.bogenliga.application.business.einstellungen.impl.business;

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
import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.business.einstellungen.impl.dao.EinstellungenDAO;
import de.bogenliga.application.business.einstellungen.impl.entity.EinstellungenBE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class EinstellungenComponentImplTest {
    private static final Long USER = 1L;

    private static final Long ID = 1L;
    private static final String KEY = "testKey";
    private static final String VALUE = "testValue";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private EinstellungenDAO EinstellungenDAO ;
    @InjectMocks
    private EinstellungenComponentImpl underTest;
    @Captor
    private ArgumentCaptor<EinstellungenBE> EinstellungenBEArgumentCaptor;



    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static EinstellungenBE getEinstellungenBE() {
        final EinstellungenBE expectedBE = new EinstellungenBE();

        expectedBE.setEinstellungenId((ID));
        expectedBE.setEinstellungenKey(KEY);
        expectedBE.setEinstellungenValue(VALUE);

        return expectedBE;
    }

    public static EinstellungenDO getEinstellungenDO() {
        return new EinstellungenDO(
                ID,
                KEY,
                VALUE);
    }


    @Test
    public void findAll() {
        // prepare test data
        final EinstellungenBE expectedBE = getEinstellungenBE();
        final List<EinstellungenBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(EinstellungenDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<EinstellungenDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getKey())
                .isEqualTo(expectedBE.getEinstellungenKey());
        assertThat(actual.get(0).getValue())
                .isEqualTo(expectedBE.getEinstellungenValue());


        // verify invocations
        verify(EinstellungenDAO).findAll();
    }


    @Test
    public void create() {
        // prepare test data
        final EinstellungenDO input = getEinstellungenDO();

        final EinstellungenBE expectedBE = getEinstellungenBE();

        // configure mocks
        when(EinstellungenDAO.create(any(EinstellungenBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final EinstellungenDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getKey())
                .isEqualTo(input.getKey());

        // verify invocations
        verify(EinstellungenDAO).create(EinstellungenBEArgumentCaptor.capture(), anyLong());

        final EinstellungenBE persistedBE = EinstellungenBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getEinstellungenKey())
                .isEqualTo(input.getKey());
    }

    @Test
    public void update() {
        // prepare test data
        final EinstellungenDO input = getEinstellungenDO();

        final EinstellungenDO expectedDO = getEinstellungenDO();
        expectedDO.setId(input.getId());
        expectedDO.setKey(input.getKey());
        expectedDO.setValue("other");

        final EinstellungenBE expectedBE = getEinstellungenBE();
        // configure mocks
        when(EinstellungenDAO.create(any(EinstellungenBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
         underTest.create(input, USER);


        when(EinstellungenDAO.update(any(EinstellungenBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final EinstellungenDO actual = underTest.update(expectedDO, USER);


        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getKey())
                .isEqualTo(input.getKey());
        assertThat(actual.getValue())
                .isEqualTo(input.getValue());

    }

    @Test
    public void delete() {
        // prepare test data
        final EinstellungenDO input = getEinstellungenDO();

        final EinstellungenBE expectedBE = getEinstellungenBE();

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(EinstellungenDAO).delete(EinstellungenBEArgumentCaptor.capture(), anyLong());

        final EinstellungenBE persistedBE = EinstellungenBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getEinstellungenKey())
                .isEqualTo(input.getKey());
    }

}

