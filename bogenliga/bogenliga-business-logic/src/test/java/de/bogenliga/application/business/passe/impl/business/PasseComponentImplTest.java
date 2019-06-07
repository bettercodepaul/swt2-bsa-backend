package de.bogenliga.application.business.passe.impl.business;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.passe.impl.dao.PasseBaseDAOTest;
import de.bogenliga.application.business.passe.impl.dao.PasseDAO;
import de.bogenliga.application.business.passe.impl.entity.PasseBE;
import de.bogenliga.application.business.baseClass.impl.BasicComponentTest;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer Tests for PasseComponentImpl
 */
public class PasseComponentImplTest extends PasseBaseDAOTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BasicDAO basicDAO;

    @InjectMocks
    private PasseDAO passeDAO;

    private PasseBE expectedBE;

    private PasseComponentImpl underTest;


    private BasicComponentTest<PasseComponentImpl, PasseDO> basicComponentTest;
    private BasicTest<PasseBE, PasseDO> basicTest;


    @Before
    public void testSetup() {
        expectedBE = getPasseBE();
        underTest = new PasseComponentImpl(passeDAO);
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
        PasseDO p = basicComponentTest.testCreateMethod(getPasseDO());
        BasicTest b = new BasicTest<PasseDO, PasseBE>(p, getValuesToMethodMap());
        b.assertEntity(getPasseBE());
    }


    @Test
    public void testUpdateOnCorrectness() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(basicDAO.updateEntity(any(), any(), any())).thenReturn(expectedBE);
        PasseDO p = basicComponentTest.testUpdateMethod(getPasseDO());
        BasicTest b = new BasicTest<PasseDO, PasseBE>(p, getValuesToMethodMap());
        b.assertEntity(getPasseBE());
    }


    @Test
    public void testDelete() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        basicComponentTest.testDeleteMethod(getPasseDO());
    }

}