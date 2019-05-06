package de.bogenliga.application.business.Passe.impl.business;

import java.lang.reflect.InvocationTargetException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.Passe.impl.dao.PasseDAO;
import de.bogenliga.application.business.Passe.impl.entity.PasseBE;
import de.bogenliga.application.business.baseClass.impl.BasicTest;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class PasseComponentImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PasseDAO passeDAO;

    @Mock
    private DsbMannschaftDAO dsbMannschaftDAO;

    @Mock
    private DsbMitgliedDAO dsbMitglieDao;

    @InjectMocks
    private PasseComponentImpl underTest;


    private BasicTest<PasseComponentImpl> basicTest;


    @Before
    public void testSetup() {
        basicTest = new BasicTest<>();
        basicTest.setEntity(underTest);

    }


    @Test(expected = Test.None.class /* no exception expected */)
    public void test() {
        try {
            basicTest.assertException();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}