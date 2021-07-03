package de.bogenliga.application.business.kampfrichter.api.types;

import de.bogenliga.application.common.time.DateProvider;
import junit.framework.TestCase;
import java.time.OffsetDateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * This is a test class for the KampfrichterDO class
 * The provided dates CREATEDATUTC and LASTMODIFIEDATUTC can be changed
 * to more fitting data if necessary
 *
 * If the test methods for the setter don't pass, check if the getter methods are correct
 * because the setters rely on the getters to access the correct information.
 *
 * @author Max Weise, FH Reutlingen SS 2021
 */
public class KampfrichterDOTest extends TestCase {
    // Test Data
    public static final Long USERID = 1L;
    public static final Long WETTKAMPFID = 2L;
    public static final boolean LEITEND = true;
    public static final Long CREATEDBYUSERID = 3L;
    public static final Long LASTMODIFIEDBYUSERID = 5L;
    public static final Long VERSION = 999L;
    public static final String KAMPFRICHTERVORNAME = "Max";
    public static final String KAMPFRICHTERNACHNAME = "Mustermann";
    public static final String EMAIL = "max.mustermann@test.de";



    // Dates get set to timestamp at execution
    // This is just a quick way of supplying dates. Feel free to change if needed
    public static final OffsetDateTime CREATEDATUTC = DateProvider.currentDateTimeUtc();
    public static final OffsetDateTime LASTMODIFIEDATUTC = DateProvider.currentDateTimeUtc();

    // Test Data for setters
    public static final Long nUSERID = 2L;
    public static final Long nWETTKAMPFID = 3L;
    public static final boolean nLEITEND = false;
//    public static final Long nCREATEDBYUSERID = 4L;
//    public static final Long nLASTMODIFIEDBYUSERID = 6L;
//    public static final Long nVERSION = 888L;
    public static final String nKAMPFRICHTERVORNAME = "Moritz";
    public static final String nKAMPFRICHTERNACHNAME = "Musterfrau";
    public static final String nEMAIL = "moritz.musterfrau@test.de";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private KampfrichterDO underTest;


    /**
     * This is a utility method to generate an object of KampftichterDO
     * Change access modifier to "public" if it is needed in other classes
     *
     * @return KampfrichterDO dataObject
     * @author Max Weise, FH Reutlingen SS 2021
     */
    private KampfrichterDO getKampfrichterDO() {
        KampfrichterDO dataObject = new KampfrichterDO(USERID, WETTKAMPFID, LEITEND, CREATEDATUTC, CREATEDBYUSERID,
                                                        LASTMODIFIEDATUTC, LASTMODIFIEDBYUSERID, VERSION);
        dataObject.setKampfrichterVorname(KAMPFRICHTERVORNAME);
        dataObject.setKampfrichterNachname(KAMPFRICHTERNACHNAME);
        dataObject.setEmail(EMAIL);

        return dataObject;
    }

    @Before
    public void setUp() {
        underTest = getKampfrichterDO();
    }


    @Test
    public void testGetUserId() {
        Long actual = underTest.getUserId();
        Long expected = USERID;

        assertEquals(expected, actual);
    }


    @Test
    public void testSetUserId() {
        underTest.setUserId(nUSERID);
        Long actual = underTest.getUserId();
        Long expected = nUSERID;

        assertEquals(expected, actual);
    }


    @Test
    public void testGetWettkampfId() {
        Long actual = underTest.getWettkampfId();
        Long expected = WETTKAMPFID;

        assertEquals(expected, actual);
    }

    @Test
    public void testSetWettkampfId() {
        underTest.setWettkampfId(nWETTKAMPFID);
        Long actual = underTest.getWettkampfId();
        Long expected = nWETTKAMPFID;

        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeitend() {
        boolean actual = underTest.isLeitend();
        boolean expected = LEITEND;

        assertEquals(expected, actual);
    }

    @Test
    public void testSetLeitend() {
        underTest.setLeitend(nLEITEND);
        boolean actual = underTest.isLeitend();
        boolean expected = nLEITEND;

        assertEquals(expected, actual);
    }


    @Test
    public void testGetKampfrichterVorname() {
        String actual = underTest.getKampfrichterVorname();
        String expected = KAMPFRICHTERVORNAME;

        assertEquals(expected, actual);
    }


    @Test
    public void testSetKampfrichterVorname() {
        underTest.setKampfrichterVorname(nKAMPFRICHTERVORNAME);
        String actual = underTest.getKampfrichterVorname();
        String expected = nKAMPFRICHTERVORNAME;

        assertEquals(expected, actual);
    }


    @Test
    public void testGetKampfrichterNachname() {
        String actual = underTest.getKampfrichterNachname();
        String expected = KAMPFRICHTERNACHNAME;

        assertEquals(expected, actual);
    }


    @Test
    public void testSetKampfrichterNachname() {
        underTest.setKampfrichterNachname(nKAMPFRICHTERNACHNAME);
        String actual = underTest.getKampfrichterNachname();
        String expected = nKAMPFRICHTERNACHNAME;

        assertEquals(expected, actual);
    }


    @Test
    public void testGetEmail() {
        String actual = underTest.getEmail();
        String expected = EMAIL;

        assertEquals(expected, actual);
    }


    @Test
    public void testSetEmail() {
        underTest.setEmail(nEMAIL);
        String actual = underTest.getEmail();
        String expected = nEMAIL;

        assertEquals(expected, actual);
    }


    /**
     * Test methods for the equals method. Cases:
     *      1. Values are equal     -> true
     *      2. Object is null       -> false
     *      3. values are not equal -> false
     */
    public void testEqualsSameValues() {
        KampfrichterDO testObject = getKampfrichterDO();
        assertEquals(true,underTest.equals(testObject));
    }
    public void testEqualsObjectIsNull() {
        assertEquals(false,underTest.equals(null));
    }
    public void testEqualsDifferentValues() {
        KampfrichterDO testObject = getKampfrichterDO();
        testObject.setKampfrichterVorname("Tom");

        // Objects should be different in Vorname
        assertEquals(false,underTest.equals(testObject));
    }
}