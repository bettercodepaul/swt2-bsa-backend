package de.bogenliga.application.business.schuetzenstatistikmatch.api.types;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.junit.Assert.*;

/**
 * Test class to test the functionality on SchuetzenstatistikMatchDO
 *
 * @author Lennart Raach
 */
public class SchuetzenstatistikMatchDOTest {

    private static final String dsbMitgliedName = "Mitglied_Name";
    private static final int rueckennummer = 5;
    private static final float pfeilpunkteSchnitt = (float) 3.7;
    private static final float match1 = 2f;
    private static final float match2 = 3f;
    private static final float match3 = 4f;
    private static final float match4 = 5f;
    private static final float match5 = 6f;
    private static final float match6 = 7f;
    private static final float match7 = 8f;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private SchuetzenstatistikMatchDO underTest;

    //create a SchuetzenstatistikMatchDO-Object for the test with values
    public static SchuetzenstatistikMatchDO getSchuetzenstatistikMatchDO() {
        final SchuetzenstatistikMatchDO expectedSchuetzenstatistikMatchDO = new SchuetzenstatistikMatchDO();
        expectedSchuetzenstatistikMatchDO.setDsbMitgliedName(dsbMitgliedName);
        expectedSchuetzenstatistikMatchDO.setRueckennummer(rueckennummer);
        expectedSchuetzenstatistikMatchDO.setPfeilpunkteSchnitt(pfeilpunkteSchnitt);
        expectedSchuetzenstatistikMatchDO.setMatch1(match1);
        expectedSchuetzenstatistikMatchDO.setMatch2(match2);
        expectedSchuetzenstatistikMatchDO.setMatch3(match3);
        expectedSchuetzenstatistikMatchDO.setMatch4(match4);
        expectedSchuetzenstatistikMatchDO.setMatch5(match5);
        expectedSchuetzenstatistikMatchDO.setMatch6(match6);
        expectedSchuetzenstatistikMatchDO.setMatch7(match7);
        return expectedSchuetzenstatistikMatchDO;
    }

    //to init the Object before Test
    @Before
    public void setUp()
    {
        underTest= getSchuetzenstatistikMatchDO();
    }

    //test if the Object is the same ->true
    @Test
    public void testTestEqualsSameObject() {

        assertEquals(underTest, underTest);
    }
    //test if the Values are the same ->true
    @Test
    public void testTestEqualsSameValues() {
        SchuetzenstatistikMatchDO testObject = getSchuetzenstatistikMatchDO();
        assertEquals(underTest, testObject);
    }
    //test if the Object is null->false
    @Test
    public void testTestEqualsObjectIsNull() {

        assertNotEquals(null, underTest);
    }
    //test if Object has Different Values ->false
    @Test
    public void testTestEqualsDifferentValues() {
        SchuetzenstatistikMatchDO testObject = new SchuetzenstatistikMatchDO();
        assertNotEquals(underTest, testObject);
    }
}
