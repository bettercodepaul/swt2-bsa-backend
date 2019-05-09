package de.bogenliga.application.business.mannschaftsmitglied.impl.business;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.MannschaftsmitgliedBaseDAOTest;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.baseClass.impl.BasicComponentTest;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer Tests for MannschaftsmitgliedComponentImpl
 */
public class MannschaftsmitgliedComponentImplTestAll extends MannschaftsmitgliedBaseDAOTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BasicDAO basicDAO;

    @InjectMocks
    private MannschaftsmitgliedDAO mannschaftsmitgliedDAO;

    private MannschaftsmitgliedBE expectedBE;

    private MannschaftsmitgliedComponentImpl underTest;


    private BasicComponentTest<MannschaftsmitgliedComponentImpl, MannschaftsmitgliedDO> basicComponentTest;
    private BasicTest<MannschaftsmitgliedBE, MannschaftsmitgliedDO> basicTest;


    @Before
    public void testSetup() {
        expectedBE = getMannschaftsmitgliedBE();
        underTest = new MannschaftsmitgliedComponentImpl(mannschaftsmitgliedDAO);
        basicComponentTest = new BasicComponentTest<>(underTest);
        basicTest = new BasicTest<>(expectedBE, getValuesToMethodMap());
    }


    @Test
    public void testAllMethodsOnPreconditions() {
        try {
            basicComponentTest.assertException();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
        MannschaftsmitgliedDO p = basicComponentTest.testCreateMethod(getMannschaftsmitgliedDO());
        BasicTest b = new BasicTest<MannschaftsmitgliedDO, MannschaftsmitgliedBE>(p, getValuesToMethodMap());
        b.assertEntity(getMannschaftsmitgliedBE());
    }


    @Test
    public void testUpdateOnCorrectness() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(basicDAO.updateEntity(any(), any(), any())).thenReturn(expectedBE);
        MannschaftsmitgliedDO p = basicComponentTest.testUpdateMethod(getMannschaftsmitgliedDO());
        BasicTest b = new BasicTest<MannschaftsmitgliedDO, MannschaftsmitgliedBE>(p, getValuesToMethodMap());
        b.assertEntity(getMannschaftsmitgliedBE());
    }


    @Test
    public void testDelete() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        basicComponentTest.testDeleteMethod(getMannschaftsmitgliedDO());
    }

}