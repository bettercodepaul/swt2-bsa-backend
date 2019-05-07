package de.bogenliga.application.business.mannschaftsmitglied.impl.dao;

import java.lang.reflect.InvocationTargetException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.mannschaftsmitglied.impl.MannschaftsmitgliedBaseDAOTest;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class MannschaftsmitgliedBasicDAOTestAll extends MannschaftsmitgliedBaseDAOTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private MannschaftsmitgliedDAO underTest;

    private static final long USER = 0;
    private MannschaftsmitgliedBE expectedBE;

    // Implements generic way to test business entities methods
    private BasicTest<MannschaftsmitgliedBE,MannschaftsmitgliedBE> basicDAOTest;


    @Before
    public void testSetup() {
        expectedBE = getMannschaftsmitgliedBE();
        basicDAOTest = new BasicTest<>(expectedBE,getValuesToMethodMap());
    }


    @Test
    public void create() {
        when(basicDao.insertEntity(any(), any())).thenReturn(expectedBE);
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.create(expectedBE, USER));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // verify invocations
        verify(basicDao).insertEntity(any(), eq(expectedBE));
    }


    @Test
    public void update() {
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(expectedBE);
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.update(expectedBE, USER));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        verify(basicDao).updateEntity(any(), eq(expectedBE), any());
    }


    @Test
    public void delete() {
        underTest.delete(expectedBE, USER);
        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(expectedBE), any());
    }

}
