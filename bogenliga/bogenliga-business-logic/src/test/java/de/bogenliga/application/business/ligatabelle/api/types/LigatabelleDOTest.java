package de.bogenliga.application.business.ligatabelle.api.types;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import junit.framework.TestCase;

/**
 * Test the equal-Methode of LigatabelleDO
 *
 * @author Lisa Tochtermann
 */
public class LigatabelleDOTest extends TestCase {
    private static Long veranstaltungId = 1L;
    private static String veranstaltungName = "Name_der_Veranstaltung";
    private static Long wettkampfId = 2L;
    private static int wettkampfTag = 3;
    private static Long mannschaftId = 4L;
    private static int mannschaftNummer = 9;
    private static Long vereinId = 7L;
    private static String vereinName = "Name_Verein";
    private static int matchpkt = 6;
    private static int matchpktGegen = 2;
    private static int satzpkt = 18;
    private static int satzpktGegen = 3;
    private static int satzpktDifferenz = 15;
    private static int sortierung = 0;
    private static int tabellenplatz = 8;

    private static final int matchCount = 0;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private LigatabelleDO underTest;

    //create a LigatabelleDO-Object for the test with values
    public static LigatabelleDO getLigatabelleDO() {
        final LigatabelleDO expectedLigatabelleDO = new LigatabelleDO();
        expectedLigatabelleDO.setveranstaltungId(veranstaltungId);
        expectedLigatabelleDO.setveranstaltungName(veranstaltungName);
        expectedLigatabelleDO.setwettkampfId(wettkampfId);
        expectedLigatabelleDO.setwettkampfTag(wettkampfTag);
        expectedLigatabelleDO.setmannschaftId(mannschaftId);
        expectedLigatabelleDO.setmannschaftNummer(mannschaftNummer);
        expectedLigatabelleDO.setvereinId(vereinId);
        expectedLigatabelleDO.setvereinName(vereinName);
        expectedLigatabelleDO.setmatchpkt(matchpkt);
        expectedLigatabelleDO.setMatchpktGegen(matchpktGegen);
        expectedLigatabelleDO.setsatzpkt(satzpkt);
        expectedLigatabelleDO.setSatzpktGegen(satzpktGegen);
        expectedLigatabelleDO.setSatzpktDifferenz(satzpktDifferenz);
        expectedLigatabelleDO.setsortierung(sortierung);
        expectedLigatabelleDO.settabellenplatz(tabellenplatz);
        expectedLigatabelleDO.setMatchCount(matchCount);


        return expectedLigatabelleDO;
    }

    //to init the Object befor Test
    @Before
    public void setUp()
    {
        underTest= getLigatabelleDO();
    }

    //test if the Object is the same ->true
    public void testTestEqualsSameObject() {

        assertEquals(true,underTest.equals(underTest));
    }
    //test if the Values are the same ->true
    public void testTestEqualsSameValues() {
        LigatabelleDO testObject = getLigatabelleDO();
        assertEquals(true,underTest.equals(testObject));
    }
    //test if the Object is null->false
    public void testTestEqualsObjectIsNull() {

        assertEquals(false,underTest.equals(null));
    }
    //test if Object has Different Values ->false
    public void testTestEqualsDifferentValues() {
        LigatabelleDO testObject = new LigatabelleDO();
        assertEquals(false,underTest.equals(testObject));
    }
}