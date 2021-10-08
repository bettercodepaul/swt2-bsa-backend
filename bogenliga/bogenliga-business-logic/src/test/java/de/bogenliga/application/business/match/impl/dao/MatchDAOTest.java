package de.bogenliga.application.business.match.impl.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.match.impl.BaseMatchTest;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.Mockito.*;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchDAOTest extends BaseMatchTest {

    @Mock
    private BasicDAO basicDao;

    @InjectMocks
    private MatchDAO underTest;

    private MatchBE expectedBE;

    // Implements generic way to test business entities methods
    private BasicTest<MatchBE, MatchBE> basicDAOTest;


    @Before
    public void testSetup() {
        expectedBE = getMatchBE();
        basicDAOTest = new BasicTest<>(expectedBE, getValuesToMethodMap());
        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(
                Collections.singletonList(expectedBE));
    }


    /**
     * Tests all find methods if the DAO implements all method
     * inside the hashmap (getValuesToMethodMap())
     * and returns the correct output
     */
    @Test
    public void testAllFindMethods() throws InvocationTargetException, IllegalAccessException {
        basicDAOTest.testAllFindMethods(underTest);
    }


    @Test
    public void findById() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findById(MATCH_ID));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByPk() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findByPk(
                    MATCH_NR, MATCH_WETTKAMPF_ID,
                    MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                    MATCH_SCHEIBENNUMMER
            ));
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


    @Test
    public void findByWettkampfId() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findByWettkampfId(MATCH_WETTKAMPF_ID));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByMannschaftId() throws InvocationTargetException, IllegalAccessException {
        basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findByMannschaftId(MATCH_MANNSCHAFT_ID));
    }
}