package de.bogenliga.application.business.schuetzenstatistikLetzeJahre.api.types;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO;
import static org.junit.Assert.*;

/**
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreDOTest {
    private static final String schuetzenname = "Name Schütze";
    private static final long sportjahr1 = (long) 8;
    private static final long sportjahr2 = (long) 7;
    private static final long sportjahr3 = (long) 6.5;
    private static final long sportjahr4 = (long) 7.8;
    private static final long sportjahr5 = (long) 8.2;
    private static final long allejahre_schnitt = (long) 7.5;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SchuetzenstatistikLetzteJahreDO underTest;


    // create object of SchuetzenstatistikLetzteJahreDO for testing
    public static SchuetzenstatistikLetzteJahreDO getSchuetzenstatistikLetzteJahreDO() {
        final SchuetzenstatistikLetzteJahreDO expectedSchuetzenstatistikLetzteJahreDO = new SchuetzenstatistikLetzteJahreDO();

        expectedSchuetzenstatistikLetzteJahreDO.setSchuetzenname(schuetzenname);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr1(sportjahr1);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr2(sportjahr2);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr3(sportjahr3);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr4(sportjahr4);
        expectedSchuetzenstatistikLetzteJahreDO.setSportjahr5(sportjahr5);
        expectedSchuetzenstatistikLetzteJahreDO.setAllejahre_schnitt(allejahre_schnitt);

        return expectedSchuetzenstatistikLetzteJahreDO;
    }


    // before every test init the object:
    @Before
    public void setUp() {
        underTest = getSchuetzenstatistikLetzteJahreDO();
    }

    // test if the object that should be the same is actually the same
    @Test
    public void testTestEqualsSameObject() {
        assertEquals(underTest, underTest);
    }

    // test if values are the same
    /*@Test
    public void testTestEqualsSameValues() {
        SchuetzenstatistikLetzteJahreDO testObject = getSchuetzenstatistikLetzteJahreDO();
        assertEquals(underTest, testObject);
    }*/

    @Test
    public void testTestEqualsSameValues() {
        SchuetzenstatistikLetzteJahreDO testObject = getSchuetzenstatistikLetzteJahreDO();
        assertEquals(underTest, testObject);
    }

    // test if object is not the same if it is actually null
    @Test
    public void testTestEqualsObjectIsNull() {
        assertNotEquals(null, underTest);
    }

    // test if newly created object has different values from test object
    @Test
    public void testTestEqualsDifferentValues() {
        SchuetzenstatistikLetzteJahreDO testObject = new SchuetzenstatistikLetzteJahreDO();
        assertNotEquals(underTest, testObject);
    }

}
