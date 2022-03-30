package de.bogenliga.application.services.v1.kampfrichter.model;

import org.junit.Test;
import junit.framework.TestCase;

/**
 * Testcase for the KampfrichterExtendetDTO class
 *
 * To test the setter methods, the getter methods need to be correct, because
 * they are needed to get access to the attributes of the test object
 *
 * @author Max Weise, FH Reutlingen SS 2021
 */
public class KampfrichterExtendedDTOTest extends TestCase {

    // Test Data
    private static final long USERID = 1337;
    private static final long WETTKAMPFID = 9999;
    private static final String VORNAME = "Max";
    private static final String NACHNAME = "Mustermann";
    private static final String EMAIL = "max.mustermann@test.de";
    private static final boolean LEITEND = false;

    // Test Data for the setter methods
    private static final long nUSERID = 2442;
    private static final long nWETTKAMPFID = 8888;
    private static final String nVORNAME = "Moritz";
    private static final String nNACHNAME = "Musterfrau";
    private static final String nEMAIL = "moritz.musterfrau@test.de";
    private static final boolean nLEITEND = true;

    /**
     * Utility method to generate a test object
     *
     * @return KampfrichterExtendedDTO expectedDTO
     *
     * @author Max Weise, FH Reutlingen
     */
    private KampfrichterExtendedDTO getExpectedDTO() {
        return new KampfrichterExtendedDTO(USERID, VORNAME, NACHNAME, EMAIL, WETTKAMPFID, LEITEND);
    }


    @Test
    public void testGetUserID() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        long actualUserId = actual.getUserID();

        assertEquals(USERID, actualUserId);
    }

    @Test
    public void testSetUserId() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        actual.setUserId(nUSERID);
        long actualUserId = actual.getUserID();

        assertEquals(nUSERID, actualUserId);
    }


    @Test
    public void testGetWettkampfID() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        long actualWettkampfId = actual.getWettkampfID();

        assertEquals(WETTKAMPFID, actualWettkampfId);
    }

    @Test
    public void testSetWettkampfId() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        actual.setWettkampfId(nWETTKAMPFID);
        long actualWettkampfId = actual.getWettkampfID();

        assertEquals(nWETTKAMPFID, actualWettkampfId);

    }


    @Test
    public void testGetLeitend() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        boolean actualLeitend = actual.getLeitend();

        assertEquals(LEITEND, actualLeitend);
    }

    @Test
    public void testSetLeitend() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        actual.setLeitend(nLEITEND);
        boolean actualLeitend = actual.getLeitend();

        assertEquals(nLEITEND, actualLeitend);
    }


    @Test
    public void testGetKampfrichterVorname() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        String actualVorname = actual.getKampfrichterVorname();

        assertEquals(VORNAME, actualVorname);
    }

    @Test
    public void testSetKampfrichterVorname() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        actual.setKampfrichterVorname(nVORNAME);
        String actualVorname = actual.getKampfrichterVorname();

        assertEquals(nVORNAME, actualVorname);
    }


    @Test
    public void testGetKampfrichterNachname() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        String actualNachname = actual.getKampfrichterNachname();

        assertEquals(NACHNAME, actualNachname);
    }

    @Test
    public void testSetKampfrichterNachname() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        actual.setKampfrichterNachname(nNACHNAME);
        String actualNachname = actual.getKampfrichterNachname();

        assertEquals(nNACHNAME, actualNachname);
    }


    @Test
    public void testGetEmail() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        String actualEMail = actual.getEmail();

        assertEquals(EMAIL, actualEMail);
    }

    @Test
    public void testSetEmail() {
        KampfrichterExtendedDTO actual = getExpectedDTO();
        actual.setEmail(nEMAIL);
        String actualEmail = actual.getEmail();

        assertEquals(nEMAIL, actualEmail);
    }


    @Test
    public void testToString() {
        KampfrichterExtendedDTO actual = getExpectedDTO();

        String s = actual.toString();
        assertTrue(s.length() > 0);
        assertTrue(s.contains(actual.getUserID().toString()));
        assertTrue(s.contains(actual.getWettkampfID().toString()));
        assertTrue(s.contains(actual.getKampfrichterVorname()));
        assertTrue(s.contains(actual.getKampfrichterNachname()));
        assertTrue(s.contains(actual.getEmail()));
        assertTrue(s.contains(String.valueOf(actual.getLeitend())));
    }
}