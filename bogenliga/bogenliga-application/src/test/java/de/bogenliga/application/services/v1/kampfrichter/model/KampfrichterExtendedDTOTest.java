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
    private static final long N_USERID = 2442;
    private static final long N_WETTKAMPFID = 8888;
    private static final String N_VORNAME = "Moritz";
    private static final String N_NACHNAME = "Musterfrau";
    private static final String N_EMAIL = "moritz.musterfrau@test.de";
    private static final boolean N_LEITEND = true;

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
        actual.setUserId(N_USERID);
        long actualUserId = actual.getUserID();

        assertEquals(N_USERID, actualUserId);
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
        actual.setWettkampfId(N_WETTKAMPFID);
        long actualWettkampfId = actual.getWettkampfID();

        assertEquals(N_WETTKAMPFID, actualWettkampfId);

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
        actual.setLeitend(N_LEITEND);
        boolean actualLeitend = actual.getLeitend();

        assertEquals(N_LEITEND, actualLeitend);
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
        actual.setKampfrichterVorname(N_VORNAME);
        String actualVorname = actual.getKampfrichterVorname();

        assertEquals(N_VORNAME, actualVorname);
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
        actual.setKampfrichterNachname(N_NACHNAME);
        String actualNachname = actual.getKampfrichterNachname();

        assertEquals(N_NACHNAME, actualNachname);
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
        actual.setEmail(N_EMAIL);
        String actualEmail = actual.getEmail();

        assertEquals(N_EMAIL, actualEmail);
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