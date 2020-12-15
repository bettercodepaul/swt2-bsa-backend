package de.bogenliga.application.business.einstellungen.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.einstellungen.impl.entity.EinstellungenBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.einstellungen.impl.business.EinstellungenComponentImplTest.getEinstellungenBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class EinstellungenBasicDAOTest {

    private static final Long USER = 0L;

    private static final long ID = 1L;
    private static final String KEY = "testkey";
    private static final String VALUE = "testValue";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private EinstellungenDAO underTest;

    @Test
    public void findAll() {
        // prepare test data
        final EinstellungenBE expectedBE = getEinstellungenBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<EinstellungenBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getEinstellungenId())
                .isEqualTo(expectedBE.getEinstellungenId());
        assertThat(actual.get(0).getEinstellungenValue())
                .isEqualTo(expectedBE.getEinstellungenValue());
        assertThat(actual.get(0).getEinstellungenKey())
                .isEqualTo(expectedBE.getEinstellungenKey());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }

    @Test
    public void findById() {
        // prepare test data
        final EinstellungenBE expectedBE = new EinstellungenBE();
        expectedBE.setEinstellungenId(ID);
        expectedBE.setEinstellungenKey(KEY);
        expectedBE.setEinstellungenValue(VALUE);


        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final EinstellungenBE actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getEinstellungenId())
                .isEqualTo(expectedBE.getEinstellungenId());
        assertThat(actual.getEinstellungenKey())
                .isEqualTo(expectedBE.getEinstellungenKey());
        assertThat(actual.getEinstellungenValue())
                .isEqualTo(expectedBE.getEinstellungenValue());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }


    @Test
    public void create() {
        // prepare test data
        final EinstellungenBE input = new EinstellungenBE();
        input.setEinstellungenId(ID);
        input.setEinstellungenKey(KEY);
        input.setEinstellungenValue(VALUE);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final EinstellungenBE actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();


        assertThat(actual.getEinstellungenId())
                .isEqualTo(input.getEinstellungenId());
        assertThat(actual.getEinstellungenKey())
                .isEqualTo(input.getEinstellungenKey());
        assertThat(actual.getEinstellungenValue())
                .isEqualTo(input.getEinstellungenValue());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }


    @Test
    public void update() {
        // prepare test data
        final EinstellungenBE input = new EinstellungenBE();
        input.setEinstellungenId(ID);
        input.setEinstellungenKey(KEY);
        input.setEinstellungenValue(VALUE);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final EinstellungenBE actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getEinstellungenId())
                .isEqualTo(input.getEinstellungenId());
        assertThat(actual.getEinstellungenKey())
                .isEqualTo(input.getEinstellungenKey());
        assertThat(actual.getEinstellungenValue())
                .isEqualTo(input.getEinstellungenValue());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any());
    }


    @Test
    public void delete() {
        // prepare test data
        final EinstellungenBE input = new EinstellungenBE();
        input.setEinstellungenId(ID);
        input.setEinstellungenKey(KEY);
        input.setEinstellungenValue(VALUE);

        // call test method
        underTest.delete(input, USER);

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }
}
