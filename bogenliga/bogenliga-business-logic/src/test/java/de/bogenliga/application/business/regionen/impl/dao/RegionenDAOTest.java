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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegionenDAOTest {

    private static final String REGION_TYP = "TEST";

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



}