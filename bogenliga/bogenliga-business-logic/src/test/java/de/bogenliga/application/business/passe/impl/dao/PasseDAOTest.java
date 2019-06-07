package de.bogenliga.application.business.passe.impl.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.passe.impl.entity.PasseBE;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class PasseDAOTest extends PasseBaseDAOTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private PasseDAO underTest;

    private PasseBE expectedBE;

    // Implements generic way to test business entities methods
    private BasicTest<PasseBE, PasseBE> basicDAOTest;


    @Before
    public void testSetup() {
        expectedBE = getPasseBE();
        basicDAOTest = new BasicTest<>(expectedBE, getValuesToMethodMap());

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);
    }


    /**
     * Tests all find methods if the DAO implements all method
     * inside the hashmap (getValuesToMethodMap())
     * and returns the correct output
     */
    @Test
    public void findAll() throws InvocationTargetException, IllegalAccessException {
        basicDAOTest.testAllFindMethods(underTest);
    }
}