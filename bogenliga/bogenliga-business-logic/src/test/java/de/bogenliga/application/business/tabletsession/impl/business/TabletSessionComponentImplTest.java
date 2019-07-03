package de.bogenliga.application.business.tabletsession.impl.business;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.baseClass.impl.BasicComponentTest;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.tabletsession.api.types.TabletSessionDO;
import de.bogenliga.application.business.tabletsession.impl.BaseTabletSessionTest;
import de.bogenliga.application.business.tabletsession.impl.dao.TabletSessionDAO;
import de.bogenliga.application.business.tabletsession.impl.entity.TabletSessionBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *
 * @author Kay Scheerer Tests for TabletSessionComponentImpl
 */
public class TabletSessionComponentImplTest extends BaseTabletSessionTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BasicDAO basicDAO;

    @InjectMocks
    private TabletSessionDAO passeDAO;

    private TabletSessionBE expectedBE;

    private TabletSessionComponentImpl underTest;


    private BasicComponentTest<TabletSessionComponentImpl, TabletSessionDO> basicComponentTest;
    private BasicTest<TabletSessionBE, TabletSessionDO> basicTest;


    @Before
    public void testSetup() {
        expectedBE = getTabletSessionBE();
        underTest = new TabletSessionComponentImpl(passeDAO);
        basicComponentTest = new BasicComponentTest<>(underTest);
        basicTest = new BasicTest<>(expectedBE, getValuesToMethodMap());
    }


    @Test
    public void testAllMethodsOnPreconditions() throws IllegalAccessException {
        basicComponentTest.assertException();
    }


    @Test
    public void testAllMethodsOnCorrectness() throws InvocationTargetException, IllegalAccessException {
        when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);
        basicTest.testAllFindMethods(underTest);
    }


    @Test
    public void testCreateOnCorrectness() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(basicDAO.insertEntity(any(), any())).thenReturn(expectedBE);
        TabletSessionDO p = basicComponentTest.testCreateMethod(getTabletSessionDO());
        BasicTest b = new BasicTest<TabletSessionDO, TabletSessionBE>(p, getValuesToMethodMap());
        b.assertEntity(getTabletSessionBE());
    }


    @Test
    public void testUpdateOnCorrectness() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(basicDAO.updateEntity(any(), any(), any())).thenReturn(expectedBE);
        TabletSessionDO p = basicComponentTest.testUpdateMethod(getTabletSessionDO());
        BasicTest b = new BasicTest<TabletSessionDO, TabletSessionBE>(p, getValuesToMethodMap());
        b.assertEntity(getTabletSessionBE());
    }


    @Test
    public void testDelete() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        basicComponentTest.testDeleteMethod(getTabletSessionDO());
    }

}