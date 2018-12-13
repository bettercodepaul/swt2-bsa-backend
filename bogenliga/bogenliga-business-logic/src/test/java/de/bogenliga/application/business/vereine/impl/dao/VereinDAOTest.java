package de.bogenliga.application.business.vereine.impl.dao;

import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;



public class VereinDAOTest {

    private static final long USER=0;

    private static long VEREIN_ID= 3;
    private static String VEREIN_NAME="TEST";
    private static String VEREIN_DSB_IDENTIFIER="2";
    private static long VEREIN_REGION_ID= 1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private VereinDAO underTest;

    public static VereinBE getVereinBE() {
        final VereinBE expectedBE = new VereinBE();


        expectedBE.setVereinDsbIdentifier(VEREIN_DSB_IDENTIFIER);
        expectedBE.setVereinName(VEREIN_NAME);
        expectedBE.setVereinId(VEREIN_ID);
        expectedBE.setVereinRegionId(VEREIN_REGION_ID);

        return expectedBE;
    }

    @Test
    public void findAll() {
        // prepare test data
        final VereinBE expectedBE = getVereinBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<VereinBE> actual = underTest.findAll();

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

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
    @Test
    public void findById() {
        // prepare test data
        final VereinBE expectedBE = getVereinBE();
        expectedBE.setVereinId(VEREIN_ID);
        expectedBE.setVereinName(VEREIN_NAME);

        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final VereinBE actual = underTest.findById(VEREIN_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getVereinId())
                .isEqualTo(expectedBE.getVereinId());
        assertThat(actual.getVereinName())
                .isEqualTo(expectedBE.getVereinName());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }

    @Test
    public void create() {
        // prepare test data
        final VereinBE input = new VereinBE();
        input.setVereinId(VEREIN_ID);
        input.setVereinName(VEREIN_NAME);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final VereinBE actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getVereinId())
                .isEqualTo(input.getVereinId());
        assertThat(actual.getVereinName())
                .isEqualTo(input.getVereinName());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }


    @Test
    public void update() {
        // prepare test data
        final VereinBE input = new VereinBE();
        input.setVereinId(VEREIN_ID);
        input.setVereinName(VEREIN_NAME);

        // configure mocks
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(input);

        // call test method
        final VereinBE actual = underTest.update(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getVereinId())
                .isEqualTo(input.getVereinId());
        assertThat(actual.getVereinName())
                .isEqualTo(input.getVereinName());

        // verify invocations
        verify(basicDao).updateEntity(any(), eq(input), any());
    }

    @Test
    public void delete() {
        // prepare test data
        final VereinBE input = new VereinBE();
        input.setVereinId(VEREIN_ID);
        input.setVereinName(VEREIN_NAME);

        // configure mocks

        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any());
    }
}