package de.bogenliga.application.business.Passe.impl.business;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.baseClass.impl.BasicComponentTest;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 * Tests for PasseComponentImpl
 */
public class PasseComponentImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private PasseComponentImpl underTest;


    private BasicComponentTest<PasseComponentImpl> basicTest;


    @Before
    public void testSetup() {
        basicTest = new BasicComponentTest<>(underTest);

    }


    @Test
    public void testAllMethods() {
        try {
            basicTest.assertException();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}