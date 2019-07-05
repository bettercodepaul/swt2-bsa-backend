package de.bogenliga.application.business.disziplin.impl.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.disziplin.impl.BaseDisziplinTest;
import de.bogenliga.application.business.disziplin.impl.entity.DisziplinBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Nick Kerschagel
 */
public class DisziplinDAOTest extends BaseDisziplinTest {

    @Mock
    private BasicDAO basicDao;

    @InjectMocks
    private DisziplinDAO underTest;

    private DisziplinBE expectedBE;

    // Implements generic way to test business entities methods
    private BasicTest<DisziplinBE, DisziplinBE> basicDAOTest;

    @Before
    public void testSetup() {
        expectedBE = getDisziplinBE();
        basicDAOTest = new BasicTest<>(expectedBE, getValuesToMethodMap());
        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(
                Collections.singletonList(expectedBE));
    }

    @Test
    public void testAllFindMethods() throws InvocationTargetException, IllegalAccessException {
        basicDAOTest.testAllFindMethods(underTest);
    }

    @Test
    public void findById() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findById(DISZIPLIN_ID));
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findAll() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findAll());
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void create() {
        when(basicDao.insertEntity(any(), any())).thenReturn(expectedBE);
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.create(expectedBE, DISZIPLIN_ID));
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // verify invocations
        verify(basicDao).insertEntity(any(), eq(expectedBE));
    }


    @Test
    public void update() {
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(expectedBE);
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.update(expectedBE, DISZIPLIN_ID));
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        verify(basicDao).updateEntity(any(), eq(expectedBE), any());
    }


    @Test
    public void delete() {
        underTest.delete(expectedBE, DISZIPLIN_ID);
        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(expectedBE), any());
    }
}