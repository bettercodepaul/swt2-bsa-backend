package de.bogenliga.application.business.Passe.impl.dao;

import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.Passe.impl.entity.PasseBE;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
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
    private BasicTest<PasseBE> basicDAOTest;


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
        basicDAOTest.testMethod(underTest.findAll());
    }


    @Test
    public void findByMatchId() {
        basicDAOTest.testMethod(underTest.findByMatchId(4));
    }


    @Test
    public void findByMemberId() {
        basicDAOTest.testMethod(underTest.findByMemberId(98));
    }


    @Test
    public void findByTeamId() {
        basicDAOTest.testMethod(underTest.findByTeamId(1));
    }

    @Test
    public void findByMannschaftMatchId() {
        basicDAOTest.testMethod(underTest.findByMannschaftMatchId(1,4));
    }

    @Test
    public void findByMemberMannschaftId() {
        basicDAOTest.testMethod(underTest.findByMemberMannschaftId(98,1));
    }

    @Test
    public void findByWettkampfId() {
        basicDAOTest.testMethod(underTest.findByWettkampfId(1337));
    }
}