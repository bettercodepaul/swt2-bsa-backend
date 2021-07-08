package de.bogenliga.application.business.schuetzenstatistik.api.types;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;

public class SchuetzenstatistikDOTest {
    private static Long veranstaltungId = 1L;
    private static String veranstaltungName = "Name_der_Veranstaltung";
    private static Long wettkampfId = 2L;
    private static int wettkampfTag = 3;
    private static Long mannschaftId = 4L;
    private static int mannschaftNummer = 9;
    private static Long vereinId = 7L;
    private static String vereinName = "Name_Verein";
    private static Long matchId = 6L;
    private static Long dsbMitgliedId = 2L;
    private static String dsbMitgliedName = "Name_dsbMitglied";
    private static float pfeilpunkteSchnitt = (float) 3.78;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private SchuetzenstatistikDO underTest;

    //create a SchuetzenstatistikDO-Object for the test with values
    public static SchuetzenstatistikDO getSchuetzenstatistikDO() {
        final SchuetzenstatistikDO expectedSchuetzenstatistikDO = new SchuetzenstatistikDO();
        expectedSchuetzenstatistikDO.setveranstaltungId(veranstaltungId);
        expectedSchuetzenstatistikDO.setveranstaltungName(veranstaltungName);
        expectedSchuetzenstatistikDO.setwettkampfId(wettkampfId);
        expectedSchuetzenstatistikDO.setwettkampfTag(wettkampfTag);
        expectedSchuetzenstatistikDO.setmannschaftId(mannschaftId);
        expectedSchuetzenstatistikDO.setmannschaftNummer(mannschaftNummer);
        expectedSchuetzenstatistikDO.setvereinId(vereinId);
        expectedSchuetzenstatistikDO.setvereinName(vereinName);
        expectedSchuetzenstatistikDO.setMatchId(matchId);
        expectedSchuetzenstatistikDO.setDsbMitgliedId(dsbMitgliedId);
        expectedSchuetzenstatistikDO.setDsbMitgliedName(dsbMitgliedName);
        expectedSchuetzenstatistikDO.setPfeilpunkteSchnitt(pfeilpunkteSchnitt);

        return expectedSchuetzenstatistikDO;
    }

    //to init the Object before Test
    @Before
    public void setUp()
    {
        underTest= getSchuetzenstatistikDO();
    }

    //test if the Object is the same ->true
    @Test
    public void testTestEqualsSameObject() {

        assertEquals(underTest, underTest);
    }
    //test if the Values are the same ->true
    @Test
    public void testTestEqualsSameValues() {
        SchuetzenstatistikDO testObject = getSchuetzenstatistikDO();
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
        SchuetzenstatistikDO testObject = new SchuetzenstatistikDO();
        assertNotEquals(underTest, testObject);
    }
}