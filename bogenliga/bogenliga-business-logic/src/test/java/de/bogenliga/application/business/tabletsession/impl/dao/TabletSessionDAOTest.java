package de.bogenliga.application.business.tabletsession.impl.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.tabletsession.impl.BaseTabletSessionTest;
import de.bogenliga.application.business.tabletsession.impl.entity.TabletSessionBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.Mockito.*;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class TabletSessionDAOTest extends BaseTabletSessionTest {

    @Mock
    private BasicDAO basicDao;

    @InjectMocks
    private TabletSessionDAO underTest;

    private TabletSessionBE expectedBE;

    // Implements generic way to test business entities methods
    private BasicTest<TabletSessionBE, TabletSessionBE> basicDAOTest;


    @Before
    public void testSetup() {
        expectedBE = getTabletSessionBE();
        basicDAOTest = new BasicTest<>(expectedBE, getValuesToMethodMap());
        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(
                Collections.singletonList(expectedBE));
    }


    /**
     * Tests all find methods if the DAO implements all method inside the hashmap (getValuesToMethodMap()) and returns
     * the correct output
     */
    @Test
    public void testAllFindMethods() throws InvocationTargetException, IllegalAccessException {
        basicDAOTest.testAllFindMethods(underTest);
    }


    @Test
    public void findById() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(
                    underTest.findByWettkampfId(WETTKAMPF_ID));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByPk() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(
                    underTest.findByIdScheinebnummer(WETTKAMPF_ID, SCHEIBENNUMMER));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findAll() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findAll());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}