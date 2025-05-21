package de.bogenliga.application.business.passe.impl.dao;

import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.passe.impl.entity.PasseBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

/**
 * Generic reflection test: every “find…” method of {@link PasseDAO}
 * must return the {@code PasseBE} supplied by the underlying {@link BasicDAO}.
 * @author Kay Scheerer, Marty Lauterbach
 */
public class PasseDAOTest extends PasseBaseDAOTest {

    @Rule public MockitoRule mockito = MockitoJUnit.rule();
    @Mock  private BasicDAO basicDao;

    private TestablePasseDAO dao;
    private BasicTest<PasseBE, PasseBE> checker;
    private PasseBE expectedBE;

    @Before
    public void init() throws Exception {
        expectedBE = getPasseBE();

        Map<String,Object> getters = new HashMap<>();
        for (Method m : PasseBE.class.getMethods())
            if (m.getName().startsWith("get") && m.getParameterCount() == 0)
                getters.put(m.getName(), m.invoke(expectedBE));

        checker = new BasicTest<>(expectedBE, new HashMap<>(getters));

        lenient().when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), anyString()))
                .thenReturn(List.of(expectedBE));
        lenient().when(basicDao.selectEntityList(any(BusinessEntityConfiguration.class), anyString(), (Object[]) any()))
                .thenReturn(List.of(expectedBE));
        lenient().when(basicDao.selectSingleEntity(any(BusinessEntityConfiguration.class), anyString()))
                .thenReturn(expectedBE);
        lenient().when(basicDao.selectSingleEntity(any(BusinessEntityConfiguration.class), anyString(), (Object[]) any()))
                .thenReturn(expectedBE);

        dao = new TestablePasseDAO(basicDao);
    }

    @Test
    public void allFinders_returnExpectedBE() throws InvocationTargetException, IllegalAccessException {
        checker.testAllFindMethods(dao);
    }

    public static class TestablePasseDAO extends PasseDAO {
        public TestablePasseDAO(BasicDAO basicDao) { super(basicDao); }
        @Override public Map<Long,List<PasseBE>> findGroupedBySchuetze(Long m, Long t) { return null; }
    }
}
