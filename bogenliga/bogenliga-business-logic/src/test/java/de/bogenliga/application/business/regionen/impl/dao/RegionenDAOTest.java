package de.bogenliga.application.business.regionen.impl.dao;

import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static de.bogenliga.application.business.regionen.impl.business.RegionenComponentImplTest.getRegionenBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegionenDAOTest {

    private static final long REGION_ID = 10;
    private static final String REGION_TYP = "TEST";
    private static final long REGION_UEBERGEORDNET = 3;
    private static final String REGION_NAME = "Test";
    private static final String REGION_KUERZEL = "T";
    private static final long USER = 1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private RegionenDAO underTest;


    @Test
    public void findAll() {
        // prepare test data
        final RegionenBE expectedBE = getRegionenBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<RegionenBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getRegionId())
                .isEqualTo(expectedBE.getRegionId());
        assertThat(actual.get(0).getRegionName())
                .isEqualTo(expectedBE.getRegionName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());

    }


    @Test
    public void findAllByType() {
        // prepare test data
        final RegionenBE expectedBE = getRegionenBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<RegionenBE> actual = underTest.findAllByType(REGION_TYP);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getRegionId())
                .isEqualTo(expectedBE.getRegionId());
        assertThat(actual.get(0).getRegionName())
                .isEqualTo(expectedBE.getRegionName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());

    }


    @Test
    public void findById() {
        // prepare test data
        final RegionenBE expectedBE = getRegionenBE();

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final RegionenBE actual = underTest.findById(REGION_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getRegionId()).isEqualTo(expectedBE.getRegionId());
        assertThat(actual.getRegionName()).isEqualTo(expectedBE.getRegionName());
        assertThat(actual.getRegionKuerzel()).isEqualTo(expectedBE.getRegionKuerzel());
        assertThat(actual.getRegionTyp()).isEqualTo(expectedBE.getRegionTyp());
        assertThat(actual.getRegionUebergeordnet()).isEqualTo(expectedBE.getRegionUebergeordnet());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }


    @Test
    public void create() {
        // prepare test data
        final RegionenBE input = getRegionenBE();

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final RegionenBE actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getRegionId()).isEqualTo(input.getRegionId());
        assertThat(actual.getRegionName()).isEqualTo(input.getRegionName());
        assertThat(actual.getRegionKuerzel()).isEqualTo(input.getRegionKuerzel());
        assertThat(actual.getRegionTyp()).isEqualTo(input.getRegionTyp());
        assertThat(actual.getRegionUebergeordnet()).isEqualTo(input .getRegionUebergeordnet());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }


    @Test
    public void update() {
        // prepare test data
        final RegionenBE input = getRegionenBE();

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final RegionenBE actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getRegionId()).isEqualTo(input.getRegionId());
        assertThat(actual.getRegionName()).isEqualTo(input.getRegionName());
        assertThat(actual.getRegionKuerzel()).isEqualTo(input.getRegionKuerzel());
        assertThat(actual.getRegionTyp()).isEqualTo(input.getRegionTyp());
        assertThat(actual.getRegionUebergeordnet()).isEqualTo(input .getRegionUebergeordnet());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any());
    }


    @Test
    public void delete() {
        // prepare test data
        final RegionenBE input = getRegionenBE();

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }


}