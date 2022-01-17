package de.bogenliga.application.business.ligamatch.impl.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.ligamatch.impl.BaseLigamatchTest;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class LigamatchDAOTest extends BaseLigamatchTest {

    @Mock
    private BasicDAO basicDAO;

    @InjectMocks
    private LigamatchDAO underTest;

    private LigamatchBE expectedBE;

    //Implements generic way to test business entities methods
    private BasicTest<LigamatchBE, LigamatchBE> basicDAOTest;

    @Before
    public void testSetup() {
        expectedBE = getLigamatchBE();
        basicDAOTest = new BasicTest<>(expectedBE, getValuesToMethodMap());
        //configure mocks
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        when(basicDAO.selectEntityList(any(),any(),any())).thenReturn(Collections.singletonList(expectedBE));
    }

    @Test
    public void testAllFindMethods() throws InvocationTargetException, IllegalAccessException{
        basicDAOTest.testAllFindMethods(underTest);
    }

    @Test
    public void findById(){
        try{
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findById(MATCH_ID));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findLigamatchesByWettkampfId(){
        try{
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findLigamatchesByWettkampfId(WETTKAMPF_ID));
        } catch (InvocationTargetException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }






}
