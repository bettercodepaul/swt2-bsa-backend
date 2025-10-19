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

    private static final String DSB_MITGLIED_NAME = "Mitglied_Name";
    private static final int RUECKENNUMMER = 5;
    private static final float PFEILPUNKTE = 3.7f;
    private static final float MATCH_1 = 2f;
    private static final float MATCH_2 = 3f;
    private static final float MATCH_3 = 4f;
    private static final float MATCH_4 = 5f;
    private static final float MATCH_5 = 6f;
    private static final float MATCH_6 = 7f;
    private static final float MATCH_7 = 8f;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private SchuetzenstatistikMatchDO underTest;

    //create a SchuetzenstatistikMatchDO-Object for the test with values
    public static SchuetzenstatistikMatchDO getSchuetzenstatistikMatchDO() {
        final SchuetzenstatistikMatchDO expectedSchuetzenstatistikMatchDO = new SchuetzenstatistikMatchDO();
        expectedSchuetzenstatistikMatchDO.setDsbMitgliedName(DSB_MITGLIED_NAME);
        expectedSchuetzenstatistikMatchDO.setRueckennummer(RUECKENNUMMER);
        expectedSchuetzenstatistikMatchDO.setPfeilpunkteSchnitt(PFEILPUNKTE);
        expectedSchuetzenstatistikMatchDO.setMatch1(MATCH_1);
        expectedSchuetzenstatistikMatchDO.setMatch2(MATCH_2);
        expectedSchuetzenstatistikMatchDO.setMatch3(MATCH_3);
        expectedSchuetzenstatistikMatchDO.setMatch4(MATCH_4);
        expectedSchuetzenstatistikMatchDO.setMatch5(MATCH_5);
        expectedSchuetzenstatistikMatchDO.setMatch6(MATCH_6);
        expectedSchuetzenstatistikMatchDO.setMatch7(MATCH_7);
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
