package de.bogenliga.application.business.ligapasse.impl.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import static org.mockito.Mockito.*;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.ligapasse.impl.entity.LigapasseBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.business.ligapasse.impl.BaseLigapasseTest;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class LigapasseDAOTest extends BaseLigapasseTest{

    @Mock
    private BasicDAO basicDAO;

    @InjectMocks
    private LigapasseDAO underTest;

    private LigapasseBE expectedBE;

    //Implements generic way to test business entities methods
    private BasicTest<LigapasseBE, LigapasseBE> basicDAOTest;

    @Before
    public void testSetup(){
        expectedBE = getLigapasseBE();
        basicDAOTest = new BasicTest<>(expectedBE, getValuesToMethodMap());
        //configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
    }


    @Test
    public void testAllFindMethods() throws InvocationTargetException, IllegalAccessException{
        basicDAOTest.testAllFindMethods(underTest);
    }

    @Test
    public void findLigapassenByLigamatchId(){
        try{
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findLigapasseByLigamatchId(LIGAPASSE_MATCH_ID));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findLigapasseByLigamatchId(){
        try{
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findLigapasseByLigamatchId(LIGAPASSE_MATCH_ID));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
