package de.bogenliga.application.business.vereine.impl.dao;

import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.business.vereine.impl.entity.VereinBEext;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class VereinDAOextTest {

    private static final long USER=0;

    private static long VEREIN_ID= 3;
    private static String VEREIN_NAME="TEST";
    private static String VEREIN_DSB_IDENTIFIER="2";
    private static long VEREIN_REGION_ID= 1;
    private static String REGION_NAME="QUALITYLAND";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private VereinDAOext underTest;

    public static VereinBEext getVereinBEext() {
        final VereinBEext expectedBE = new VereinBEext();


        expectedBE.setVereinDsbIdentifier(VEREIN_DSB_IDENTIFIER);
        expectedBE.setVereinName(VEREIN_NAME);
        expectedBE.setVereinId(VEREIN_ID);
        expectedBE.setVereinRegionId(VEREIN_REGION_ID);
        expectedBE.setVereinRegionName(REGION_NAME);
        return expectedBE;
    }

    @Test
    public void findAll() {
        // prepare test data
        final VereinBEext expectedBE = getVereinBEext();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<VereinBEext> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVereinId())
                .isEqualTo(expectedBE.getVereinId());
        assertThat(actual.get(0).getVereinName())
                .isEqualTo(expectedBE.getVereinName());
        assertThat(actual.get(0).getVereinRegionName())
                .isEqualTo(expectedBE.getVereinRegionName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findBySearch() {
        // prepare test data
        final VereinBEext expectedBE = getVereinBEext();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<VereinBEext> actual = underTest.findBySearch(expectedBE.getVereinName());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVereinId())
                .isEqualTo(expectedBE.getVereinId());
        assertThat(actual.get(0).getVereinName())
                .isEqualTo(expectedBE.getVereinName());
        assertThat(actual.get(0).getVereinRegionName())
                .isEqualTo(expectedBE.getVereinRegionName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findById() {
        // prepare test data
        final VereinBEext expectedBE = getVereinBEext();
        expectedBE.setVereinId(VEREIN_ID);
        expectedBE.setVereinName(VEREIN_NAME);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final VereinBEext actual = underTest.findById(VEREIN_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getVereinId())
                .isEqualTo(expectedBE.getVereinId());
        assertThat(actual.getVereinName())
                .isEqualTo(expectedBE.getVereinName());
        assertThat(actual.getVereinRegionName())
                .isEqualTo(expectedBE.getVereinRegionName());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }

   }