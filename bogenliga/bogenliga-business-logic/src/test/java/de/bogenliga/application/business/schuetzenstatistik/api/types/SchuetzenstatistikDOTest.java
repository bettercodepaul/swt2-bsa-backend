package de.bogenliga.application.business.schuetzenstatistik.api.types;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;

public class SchuetzenstatistikDOTest {
    private static final Long veranstaltungId = 1L;
    private static final String veranstaltungName = "Name_der_Veranstaltung";
    private static final Long wettkampfId = 2L;
    private static final int wettkampfTag = 3;
    private static final Long mannschaftId = 4L;
    private static final int mannschaftNummer = 9;
    private static final Long vereinId = 7L;
    private static final String vereinName = "Name_Verein";
    private static final Long matchId = 6L;
    private static final int matchNr = 2;
    private static final Long dsbMitgliedId = 2L;
    private static final String dsbMitgliedName = "Mitglied_Name";
    private static final int rueckenNummer = 5;
    private static final float pfeilpunkteSchnitt = (float) 3.7;
    private static final String[] schuetzeSaetze = {"{10,9}", "{4,10}", "{10,6}", "{3,8}", "{8,7}"};


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
        expectedSchuetzenstatistikDO.setMatchNr(matchNr);
        expectedSchuetzenstatistikDO.setDsbMitgliedId(dsbMitgliedId);
        expectedSchuetzenstatistikDO.setDsbMitgliedName(dsbMitgliedName);
        expectedSchuetzenstatistikDO.setRueckenNummer(rueckenNummer);
        expectedSchuetzenstatistikDO.setPfeilpunkteSchnitt(pfeilpunkteSchnitt);
        expectedSchuetzenstatistikDO.setSchuetzeSaetze(schuetzeSaetze);
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