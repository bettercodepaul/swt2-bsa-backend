package de.bogenliga.application.business.passe.impl.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import org.junit.After;
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
    }


    @After
    public void tearDown() {
        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }


    @Test
    public void testfindAll() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(underTest.findAll());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByMatchId() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(
                    underTest.findByMatchId((Long) getValuesToMethodMap().get("findByMatchId")));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByMemberId() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(
                    underTest.findByMemberId((Long) getValuesToMethodMap().get("getPasseDsbMitgliedId")));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByTeamId() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(
                    underTest.findByTeamId((Long) getValuesToMethodMap().get("getPasseMannschaftId")));

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByMannschaftMatchId() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(
                    underTest.findByMannschaftMatchId((Long) getValuesToMethodMap().get("getPasseMannschaftId"),
                            (Long) getValuesToMethodMap().get("getPasseMatchId")));

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByMemberMannschaftId() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(
                    underTest.findByMemberMannschaftId((Long) getValuesToMethodMap().get("getPasseDsbMitgliedId"),
                            (Long) getValuesToMethodMap().get("getPasseMatchId")));

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void findByWettkampfId() {
        try {
            basicDAOTest.testAllFieldsOnEqualToExpectedEntity(
                    underTest.findByWettkampfId((Long) getValuesToMethodMap().get("getPasseWettkampfId")));

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}