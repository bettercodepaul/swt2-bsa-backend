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
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedExtDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.baseClass.impl.BasicComponentTest;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedExtendedBE;
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
    private MannschaftsmitgliedExtDAO mannschaftsmitgliedExtDAO;

    private MannschaftsmitgliedBE expectedBE;
    private MannschaftsmitgliedExtendedBE expectedExtendedBE;

    private MannschaftsmitgliedComponentImpl underTest;


    private BasicComponentTest<MannschaftsmitgliedComponentImpl, MannschaftsmitgliedDO> basicComponentTest;
    private BasicTest<MannschaftsmitgliedBE, MannschaftsmitgliedDO> basicTest;
    private BasicTest<MannschaftsmitgliedExtendedBE, MannschaftsmitgliedDO> basicExtendedTest;


    @Before
    public void testSetup() {
        expectedBE = getMannschaftsmitgliedExtendedBE();
        expectedExtendedBE = getMannschaftsmitgliedExtendedBE();
        underTest = new MannschaftsmitgliedComponentImpl(mannschaftsmitgliedDAO, mannschaftsmitgliedExtDAO);
        basicComponentTest = new BasicComponentTest<>(underTest);
        basicTest = new BasicTest<>(expectedBE, getValuesToMethodMap());
        basicExtendedTest = new BasicTest<>(expectedExtendedBE, getValuesToMethodMap());

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
        when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedExtendedBE));
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedExtendedBE);
        basicExtendedTest.testAllFindMethods(underTest);
    }


    @Test
    public void testCreateOnCorrectness() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(basicDAO.insertEntity(any(), any())).thenReturn(expectedBE);
        when(basicDAO.selectSingleEntity(any(),any(),any(),any())).thenReturn(expectedBE);
        MannschaftsmitgliedDO p = basicComponentTest.testCreateMethod(getMannschaftsmitgliedDO());
        BasicTest b = new BasicTest<MannschaftsmitgliedDO, MannschaftsmitgliedBE>(p, getValuesToMethodMap());
        b.assertEntity(getMannschaftsmitgliedExtendedBE());
    }


    @Test
    public void testUpdateOnCorrectness() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(basicDAO.updateEntity(any(), any(), any())).thenReturn(expectedBE);
        when(basicDAO.selectSingleEntity(any(),any(),any(),any())).thenReturn(expectedBE);
        MannschaftsmitgliedDO p = basicComponentTest.testUpdateMethod(getMannschaftsmitgliedDO());
        BasicTest b = new BasicTest<MannschaftsmitgliedDO, MannschaftsmitgliedBE>(p, getValuesToMethodMap());
        b.assertEntity(getMannschaftsmitgliedExtendedBE());
    }


    @Test
    public void testDelete() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        basicComponentTest.testDeleteMethod(getMannschaftsmitgliedDO());
    }

}