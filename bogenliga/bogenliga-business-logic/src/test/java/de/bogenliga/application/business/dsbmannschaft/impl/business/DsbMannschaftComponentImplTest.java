package de.bogenliga.application.business.dsbmannschaft.impl.business;

import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAO;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class DsbMannschaftComponentImplTest {
    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final long id = 2222L;
    private static final long vereinId=101010L;
    private static final long nummer=111L;
    private static final long benutzerId=12L;
    private static final long veranstaltungId=1L;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DsbMannschaftDAO dsbMannschaftDAO ;
    @InjectMocks
    private DsbMannschaftComponentImpl underTest;
    @Captor
    private ArgumentCaptor<DsbMannschaftBE> dsbMannschaftBEArgumentCaptor;



    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static DsbMannschaftBE getDsbMannschaftBE() {
        final DsbMannschaftBE expectedBE = new DsbMannschaftBE();

        expectedBE.setId(id);
        expectedBE.setVereinId(vereinId);
        expectedBE.setNummer(nummer);
        expectedBE.setBenutzerId(benutzerId);
        expectedBE.setVeranstaltungId(veranstaltungId);



        return expectedBE;
    }

    public static DsbMannschaftDO getDsbMannschaftDO() {
        return new DsbMannschaftDO(
                id,
                vereinId,
                nummer,
                benutzerId,
                veranstaltungId);
    }

    @Test
    public void findAll() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMannschaftDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getBenutzerId())
                .isEqualTo(expectedBE.getBenutzerId());
        assertThat(actual.get(0).getNummer())
                .isEqualTo(expectedBE.getNummer());
        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVereinId())
                .isEqualTo(expectedBE.getVereinId());


        // verify invocations
        verify(dsbMannschaftDAO).findAll();
    }

    @Test
    public void findById() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks
        when(dsbMannschaftDAO.findById(id)).thenReturn(expectedBE);

        // call test method
        final DsbMannschaftDO actual = underTest.findById(id);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getId());

        // verify invocations
        verify(dsbMannschaftDAO).findById(id);
    }

    @Test
    public void create() {
        // prepare test data
        final DsbMannschaftDO input = getDsbMannschaftDO();

        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks
        when(dsbMannschaftDAO.create(any(DsbMannschaftBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMannschaftDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(dsbMannschaftDAO).create(dsbMannschaftBEArgumentCaptor.capture(), anyLong());

        final DsbMannschaftBE persistedBE = dsbMannschaftBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
    }

    @Test
    public void create_with_mandatory_parameters() {
        // prepare test data
        final OffsetDateTime dateTime = OffsetDateTime.now();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final DsbMannschaftDO input = new DsbMannschaftDO(
                id,
                benutzerId,
                vereinId,
                nummer,
                veranstaltungId,
                dateTime,
                USER,
                VERSION);

        final DsbMannschaftBE expectedBE = new DsbMannschaftBE();
        expectedBE.setId(id);
        expectedBE.setBenutzerId(benutzerId);
        expectedBE.setNummer(nummer);
        expectedBE.setVereinId(vereinId);
        expectedBE.setVeranstaltungId(veranstaltungId);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setVersion(VERSION);


        // configure mocks
        when(dsbMannschaftDAO.create(any(DsbMannschaftBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMannschaftDO actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(dsbMannschaftDAO).create(dsbMannschaftBEArgumentCaptor.capture(), anyLong());

        final DsbMannschaftBE persistedBE = dsbMannschaftBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
    }

    @Test
    public void create_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
    }

   /* @Test
    public void create_withoutNummer_shouldThrowException() {
        // prepare test data
        final DsbMannschaftDO input = getDsbMannschaftDO();
        final Long l = null;
        input.setId(id);
        input.setNummer(l);

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(input, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
    }*/

    @Test
    public void update() {
        // prepare test data
        final DsbMannschaftDO input = getDsbMannschaftDO();

        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks
        when(dsbMannschaftDAO.update(any(DsbMannschaftBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final DsbMannschaftDO actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());
        assertThat(actual.getNummer())
                .isEqualTo(input.getNummer());

        // verify invocations
        verify(dsbMannschaftDAO).update(dsbMannschaftBEArgumentCaptor.capture(), anyLong());

        final DsbMannschaftBE persistedBE = dsbMannschaftBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
        assertThat(persistedBE.getVeranstaltungId())
                .isEqualTo(input.getVeranstaltungId());
    }

    @Test
    public void update_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
    }

    @Test
    public void delete() {
        // prepare test data
        final DsbMannschaftDO input = getDsbMannschaftDO();

        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(dsbMannschaftDAO).delete(dsbMannschaftBEArgumentCaptor.capture(), anyLong());

        final DsbMannschaftBE persistedBE = dsbMannschaftBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
    }


    @Test
    public void delete_withoutInput_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.delete(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verifyZeroInteractions(dsbMannschaftDAO);
    }
}

