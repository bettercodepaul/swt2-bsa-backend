package de.bogenliga.application.business.match.impl.business;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.impl.dao.WettkampfDAO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.baseClass.impl.BasicComponentTest;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.BaseMatchTest;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Kay Scheerer
 */
public class MatchComponentImplTestAll extends BaseMatchTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BasicDAO basicDAO;
    @Mock
    private DsbMannschaftComponent dsbMannschaftComponent;
    @Mock
    private VereinComponent vereinComponent;

    @Mock
    private WettkampfComponent wettkampfComponent;

    @InjectMocks
    private MatchDAO matchDAO;

    private MatchBE expectedBE;

    private MatchComponentImpl underTest;


    private BasicComponentTest<MatchComponentImpl, MatchDO> basicComponentTest;
    private BasicTest<MatchBE, MatchDO> basicTest;


    @Before
    public void testSetup() {
        expectedBE = getMatchBE();
        underTest = new MatchComponentImpl(matchDAO,dsbMannschaftComponent, vereinComponent);
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
        MatchDO p = basicComponentTest.testCreateMethod(getMatchDO());
        BasicTest b = new BasicTest<MatchDO, MatchBE>(p, getValuesToMethodMap());
        b.assertEntity(getMatchBE());
    }


    @Test
    public void testUpdateOnCorrectness() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(basicDAO.updateEntity(any(), any(), any())).thenReturn(expectedBE);
        MatchDO p = basicComponentTest.testUpdateMethod(getMatchDO());
        BasicTest b = new BasicTest<MatchDO, MatchBE>(p, getValuesToMethodMap());
        b.assertEntity(getMatchBE());
    }


    @Test
    public void testDelete() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        basicComponentTest.testDeleteMethod(getMatchDO());
    }
}
