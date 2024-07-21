package de.bogenliga.application.business.trigger.impl.dao;
import java.util.Collections;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.trigger.impl.entity.TriggerCountBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerCountDAOTest{
    private static final Long count = 40000L;
    private static final Long altCount =50000L;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDAO;
    @Mock
    private TriggerCountBE triggerCountBE;
    @InjectMocks
    private TriggerCountDAO triggerCountDAO;
    public static TriggerCountBE getTriggerCountBE() {
        TriggerCountBE countBE = new TriggerCountBE();
        countBE.setCount(count);
        
        return countBE;
    }



    @Test
    public void testFindAllCount() {
        // prepare test data
        final TriggerCountBE expectedCountBE = getTriggerCountBE();
        expectedCountBE.setCount(count);

        // configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedCountBE);

        // call test method
        final TriggerCountBE actualCount = triggerCountDAO.findAllCount();

        // assert result
        assertThat(actualCount).isNotNull();

    }
    @Test
    public void testFindInProgressCount() {
        // prepare test data
        final TriggerCountBE expectedCountBE = getTriggerCountBE();
        expectedCountBE.setCount(count);

        // configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedCountBE);

        // call test method
        final TriggerCountBE actualCount = triggerCountDAO.findInProgressCount();

        // assert result
        assertThat(actualCount).isNotNull();

    }
    @Test
    public void testFindUnprocessedCount() {
        // prepare test data
        final TriggerCountBE expectedCountBE = getTriggerCountBE();
        expectedCountBE.setCount(count);

        // configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedCountBE);

        // call test method
        final TriggerCountBE actualCount = triggerCountDAO.findUnprocessedCount();

        // assert result
        assertThat(actualCount).isNotNull();
    }
    @Test
    public void testCountAllEntriesByStatusAndDateInterval() {
        // prepare test data
        final TriggerCountBE expectedCountBE = getTriggerCountBE();
        expectedCountBE.setCount(count);

        // configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedCountBE);

        // call test method
        final TriggerCountBE actualCount = triggerCountDAO.countEntriesByStatusAndDateInterval("1 Month","Alle");

        // assert result
        assertThat(actualCount).isNotNull();
    }
    @Test
    public void testCountSuccessedEntriesByStatusAndDateInterval() {
        // prepare test data
        final TriggerCountBE expectedCountBE = getTriggerCountBE();
        expectedCountBE.setCount(count);

        // configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedCountBE);

        // call test method
        final TriggerCountBE actualCount = triggerCountDAO.countEntriesByStatusAndDateInterval("1 Month","Erfolgreich");

        // assert result
        assertThat(actualCount).isNotNull();
    }
    @Test
    public void testCountInProgressEntriesByStatusAndDateInterval() {
        // prepare test data
        final TriggerCountBE expectedCountBE = getTriggerCountBE();
        expectedCountBE.setCount(count);

        // configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedCountBE);

        // call test method
        final TriggerCountBE actualCount = triggerCountDAO.countEntriesByStatusAndDateInterval("1 Month","Laufend");

        // assert result
        assertThat(actualCount).isNotNull();
    }
    @Test
    public void testCountNewEntriesByStatusAndDateInterval() {
        // prepare test data
        final TriggerCountBE expectedCountBE = getTriggerCountBE();
        expectedCountBE.setCount(count);

        // configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedCountBE);

        // call test method
        final TriggerCountBE actualCount = triggerCountDAO.countEntriesByStatusAndDateInterval("1 Month","Neu");

        // assert result
        assertThat(actualCount).isNotNull();
    }
    @Test
    public void testCountFailedEntriesByStatusAndDateInterval() {
        // prepare test data
        final TriggerCountBE expectedCountBE = getTriggerCountBE();
        expectedCountBE.setCount(count);

        // configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedCountBE);

        // call test method
        final TriggerCountBE actualCount = triggerCountDAO.countEntriesByStatusAndDateInterval("1 Month","Fehlgeschlagen");

        // assert result
        assertThat(actualCount).isNotNull();
    }

}

