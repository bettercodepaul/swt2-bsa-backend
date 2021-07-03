package de.bogenliga.application.business.kampfrichter.impl.entity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.kampfrichter.impl.dao.KampfrichterDAO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import junit.framework.TestCase;
import static org.junit.Assert.*;

/**
 * Test class for KampfrichterExtendedBE
 *
 * @author Max Weise, FH Reutlingen SS 2021
 */
public class KampfrichterExtendedBETest extends TestCase {

    // Test data
    private static final Long USERID = 1L;
    private static final Long SERIALVERSIONUID = -76389969048178948L;
    private static final Long WETTKAMPFID = 999L;
    private static final Boolean LEITEND = false;
    private static final String VORNAME = "Max";
    private static final String NACHNAME = "Mustermann";
    private static final String EMAIL = "max.mustermann@test.de";

    // Test data for setters
    private static final Long nUSERID = 2L;
    private static final Long nWETTKAMPFID = 888L;
    private static final Boolean nLEITEND = true;
    private static final String nVORNAME = "Kaede";
    private static final String nNACHNAME = "Kayano";
    private static final String nEMAIL = "kaede.kayano@test.de";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @InjectMocks
    private KampfrichterExtendedBE underTest;

    private KampfrichterExtendedBE getKampfrichterExtendedBE() {
        KampfrichterExtendedBE businessEntity = new KampfrichterExtendedBE(USERID, WETTKAMPFID, LEITEND, VORNAME,
                                                                            NACHNAME, EMAIL);
        return businessEntity;
    }

    @Before
    public void setUp() {
        underTest = getKampfrichterExtendedBE();
    }


    @Test
    public void testGetSerialVersionUID() {
        Long expected = SERIALVERSIONUID;
        Long actual = underTest.getSerialVersionUID();

        assertEquals(expected, actual);
    }


    public void testGetKampfrichterExtendedUserID() {
        Long expected = USERID;
        Long actual = underTest.getKampfrichterExtendedUserID();

        assertEquals(expected, actual);
    }

    public void testSetKampfrichterExtendedUserID() {
        Long expected = nUSERID;
        underTest.setKampfrichterExtendedUserID(nUSERID);
        Long actual = underTest.getKampfrichterExtendedUserID();

        assertEquals(expected, actual);
    }


    public void testGetKampfrichterExtendedWettkampfID() {
        Long expected = WETTKAMPFID;
        Long actual = underTest.getKampfrichterExtendedWettkampfID();

        assertEquals(expected, actual);
    }

    public void testSetKampfrichterExtendedWettkampfID() {
        Long expected = nWETTKAMPFID;
        underTest.setKampfrichterExtendedWettkampfID(nWETTKAMPFID);
        Long actual = underTest.getKampfrichterExtendedWettkampfID();

        assertEquals(expected, actual);
    }


    public void testGetKampfrichterExtendedLeitend() {
        boolean expected = LEITEND;
        boolean actual = underTest.getKampfrichterExtendedLeitend();

        assertEquals(expected, actual);
    }

    public void testSetKampfrichterExtendedLeitend() {
        boolean expected = nLEITEND;
        underTest.setKampfrichterExtendedLeitend(nLEITEND);
        boolean actual = underTest.getKampfrichterExtendedLeitend();

        assertEquals(expected, actual);
    }


    public void testGetKampfrichterExtendedVorname() {
        String expected = VORNAME;
        String actual = underTest.getKampfrichterExtendedVorname();

        assertEquals(expected, actual);
    }

    public void testSetKampfrichterExtendedVorname() {
        String expected = nVORNAME;
        underTest.setKampfrichterExtendedVorname(nVORNAME);
        String actual = underTest.getKampfrichterExtendedVorname();

        assertEquals(expected, actual);
    }


    public void testGetKampfrichterExtendedNachname() {
        String expected = NACHNAME;
        String actual = underTest.getKampfrichterExtendedNachname();

        assertEquals(expected, actual);
    }

    public void testSetKampfrichterExtendedNachname() {
        String expected = nNACHNAME;
        underTest.setKampfrichterExtendedNachname(nNACHNAME);
        String actual = underTest.getKampfrichterExtendedNachname();

        assertEquals(expected, actual);
    }


    public void testGetKampfrichterExtendedEmail() {
        String expected = EMAIL;
        String actual = underTest.getKampfrichterExtendedEmail();

        assertEquals(expected, actual);
    }

    public void testSetKampfrichterExtendedEmail() {
        String expected = nEMAIL;
        underTest.setKampfrichterExtendedEmail(nEMAIL);
        String actual = underTest.getKampfrichterExtendedEmail();

        assertEquals(expected, actual);
    }


    @Test
    public void testToString() {
        String s = underTest.toString();
        assertTrue(s.length() > 0);
        assertTrue(s.contains(underTest.getKampfrichterExtendedUserID().toString()));
        assertTrue(s.contains(underTest.getKampfrichterExtendedWettkampfID().toString()));
        assertTrue(s.contains(String.valueOf(underTest.getKampfrichterExtendedLeitend())));
        assertTrue(s.contains(underTest.getKampfrichterExtendedVorname()));
        assertTrue(s.contains(underTest.getKampfrichterExtendedNachname()));
        assertTrue(s.contains(underTest.getKampfrichterExtendedEmail()));
    }
}