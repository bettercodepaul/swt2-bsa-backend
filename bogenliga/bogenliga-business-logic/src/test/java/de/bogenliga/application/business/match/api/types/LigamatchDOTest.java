package de.bogenliga.application.business.match.api.types;

import junit.framework.TestCase;

/**
 * TODO [AL] class documentation
 *
 * @author Sebastian Schwarz
 */
public class LigamatchDOTest extends TestCase {
    private static Long id = 1L;
    private static Long wettkampfId = 2L;
    private static Long matchNr = 3L;
    private static Long matchScheibennummer = 3L;
    private static Long matchpunkte = 4L;
    private static Long satzpunkte = 5L;
    private static Long mannschaftID = 6L;
    private static String mannschaftName = "Mannschaftsname";
    private static String nameGegner = "GegnerName";
    private static Long scheibennummerGegner = 7L;
    private static Long matchIdGegner = 8L;
    private static Long naechsteMatchId = 9L;

    private static Long naechsteNaechsteMatchNrMatchId = 10L;
    private static Long strafpunkteSatz1 = 11L;
    private static Long strafpunkteSatz2 = 12L;
    private static Long strafpunkteSatz3 = 13L;
    private static Long strafpunkteSatz4 = 14L;
    private static Long strafpunkteSatz5 = 15L;


    //create a final LigamatchDO-Object for the test with values
    public static LigamatchDO getLigamatchDO() {
        final LigamatchDO expectedLigamatchDO = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);

        return expectedLigamatchDO;
    }

    public void testGetId() {
        Long expected = id;
        Long actual = getLigamatchDO().getId();
        assertEquals(expected, actual);
    }

    public void testGetWettkampfId() {
        Long expected = wettkampfId;
        Long actual = getLigamatchDO().getWettkampfId();
        assertEquals(expected, actual);
    }

    public void testGetMatchNr() {
        Long expected = matchNr;
        Long actual = getLigamatchDO().getMatchNr();
        assertEquals(expected, actual);
    }

    public void testGetMatchScheibennummer() {
        Long expected = matchScheibennummer;
        Long actual = getLigamatchDO().getMatchScheibennummer();
        assertEquals(expected, actual);
    }

    public void testGetMatchpunkte() {
        Long expected = matchpunkte;
        Long actual = getLigamatchDO().getMatchpunkte();
        assertEquals(expected, actual);
    }
    public void testGetSatzpunkte() {
        Long expected = satzpunkte;
        Long actual = getLigamatchDO().getSatzpunkte();
        assertEquals(expected, actual);
    }
    public void testGetMannschaftID() {
        Long expected = mannschaftID;
        Long actual = getLigamatchDO().getMannschaftId();
        assertEquals(expected, actual);
    }
    public void testGetMannschaftName() {
        String expected = mannschaftName;
        String actual = getLigamatchDO().getMannschaftName();
        assertEquals(expected, actual);
    }
    public void testGetNameGegner() {
        String expected = nameGegner;
        String actual = getLigamatchDO().getNameGegner();
        assertEquals(expected, actual);
    }
    public void testGetScheibennummerGegner() {
        Long expected = scheibennummerGegner;
        Long actual = getLigamatchDO().getScheibennummerGegner();
        assertEquals(expected, actual);
    }
    public void testGetMatchIdGegner() {
        Long expected = matchIdGegner;
        Long actual = getLigamatchDO().getMatchIdGegner();
        assertEquals(expected, actual);
    }
    public void testGetNaechsteMatchId() {
        Long expected = naechsteMatchId;
        Long actual = getLigamatchDO().getNaechsteMatchId();
        assertEquals(expected, actual);
    }
    public void testGetNaechsteNaechsteMatchNrMatchId() {
        Long expected = naechsteNaechsteMatchNrMatchId;
        Long actual = getLigamatchDO().getNaechsteNaechsteMatchNrMatchId();
        assertEquals(expected, actual);
    }
    public void testGetStrafpunkteSatz1() {
        Long expected = strafpunkteSatz1;
        Long actual = getLigamatchDO().getStrafpunkteSatz1();
        assertEquals(expected, actual);
    }
    public void testGetStrafpunkteSatz2() {
        Long expected = strafpunkteSatz2;
        Long actual = getLigamatchDO().getStrafpunkteSatz2();
        assertEquals(expected, actual);
    }
    public void testGetStrafpunkteSatz3() {
        Long expected = strafpunkteSatz3;
        Long actual = getLigamatchDO().getStrafpunkteSatz3();
        assertEquals(expected, actual);
    }
    public void testGetStrafpunkteSatz4() {
        Long expected = strafpunkteSatz4;
        Long actual = getLigamatchDO().getStrafpunkteSatz4();
        assertEquals(expected, actual);
    }
    public void testGetStrafpunkteSatz5() {
        Long expected = strafpunkteSatz5;
        Long actual = getLigamatchDO().getStrafpunkteSatz5();
        assertEquals(expected, actual);
    }

    public void testSetId() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 20L;
        testObj.setId(expected);
        long actual = testObj.getId();
        assertEquals(expected, actual);
    }

    public void testSetWettkampfId() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 21L;
        testObj.setWettkampfId(expected);
        long actual = testObj.getWettkampfId();
        assertEquals(expected, actual);
    }

    public void testSetMatchNr() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 22L;
        testObj.setMatchNr(expected);
        long actual = testObj.getMatchNr();
        assertEquals(expected, actual);
    }

    public void testSetMatchScheibennummer() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 23L;
        testObj.setMatchScheibennummer(expected);
        long actual = testObj.getMatchScheibennummer();
        assertEquals(expected, actual);
    }

    public void testSetMatchpunkte() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 24L;
        testObj.setMatchpunkte(expected);
        long actual = testObj.getMatchpunkte();
        assertEquals(expected, actual);
    }

    public void testSetSatzpunkte() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 25L;
        testObj.setSatzpunkte(expected);
        long actual = testObj.getSatzpunkte();
        assertEquals(expected, actual);
    }

    public void testSetMannschaftID() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 26L;
        testObj.setMannschaftId(expected);
        long actual = testObj.getMannschaftId();
        assertEquals(expected, actual);
    }

    public void testSetMannschaftName() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        String expected = "TestMannschaft";
        testObj.setMannschaftName(expected);
        String actual = testObj.getMannschaftName();
        assertEquals(expected, actual);
    }

    public void testSetNameGegner() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        String expected = "TestGegner";
        testObj.setNameGegner(expected);
        String actual = testObj.getNameGegner();
        assertEquals(expected, actual);
    }

    public void testSetScheibennummerGegner() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 27L;
        testObj.setScheibennummerGegner(expected);
        long actual = testObj.getScheibennummerGegner();
        assertEquals(expected, actual);
    }

    public void testSetMatchIdGegner() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 28L;
        testObj.setMatchIdGegner(expected);
        long actual = testObj.getMatchIdGegner();
        assertEquals(expected, actual);
    }

    public void testNaechsteMatchId() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 29L;
        testObj.setNaechsteMatchId(expected);
        long actual = testObj.getNaechsteMatchId();
        assertEquals(expected, actual);
    }

    public void testNaechsteNaechsteMatchNrMatchId() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 30L;
        testObj.setNaechsteNaechsteMatchNrMatchId(expected);
        long actual = testObj.getNaechsteNaechsteMatchNrMatchId();
        assertEquals(expected, actual);
    }

    public void testStrafpunkteSatz1() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 31L;
        testObj.setStrafpunkteSatz1(expected);
        long actual = testObj.getStrafpunkteSatz1();
        assertEquals(expected, actual);
    }

    public void testStrafpunkteSatz2() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 32L;
        testObj.setStrafpunkteSatz2(expected);
        long actual = testObj.getStrafpunkteSatz2();
        assertEquals(expected, actual);
    }


    public void testStrafpunkteSatz3() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 33L;
        testObj.setStrafpunkteSatz3(expected);
        long actual = testObj.getStrafpunkteSatz3();
        assertEquals(expected, actual);
    }

    public void testStrafpunkteSatz4() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 34L;
        testObj.setStrafpunkteSatz4(expected);
        long actual = testObj.getStrafpunkteSatz4();
        assertEquals(expected, actual);
    }

    public void testStrafpunkteSatz5() {
        LigamatchDO testObj = new LigamatchDO(id,wettkampfId,matchNr,matchScheibennummer,matchpunkte,satzpunkte,mannschaftID,mannschaftName,nameGegner,scheibennummerGegner,matchIdGegner,naechsteMatchId,naechsteNaechsteMatchNrMatchId,strafpunkteSatz1,strafpunkteSatz2,strafpunkteSatz3,strafpunkteSatz4,strafpunkteSatz5);
        long expected = 35L;
        testObj.setStrafpunkteSatz5(expected);
        long actual = testObj.getStrafpunkteSatz5();
        assertEquals(expected, actual);
    }





}

