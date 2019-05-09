package de.bogenliga.application.business.passe.impl.dao;

import java.time.OffsetDateTime;
import java.util.HashMap;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.passe.impl.entity.PasseBE;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class PasseBaseDAOTest {

    private static final long PASSE_ID = 11;
    private static final long PASSE_MANNSCHAFT_ID = 1;
    private static final long PASSE_WETTKAMPF_ID = 1337;
    private static final long PASSE_LFDR_NR = 2;
    private static final long PASSE_DSB_MITGLIED_ID = 98;
    private static final long PASSE_MATCH_NR = 4;
    private static final long PASSE_MATCH_ID = 224;
    private static final int PASSE_PFEIL_1 = 10;
    private static final int PASSE_PFEIL_2 = 5;
    private static final int PASSE_PFEIL_3 = 0;
    private static final int PASSE_PFEIL_4 = 0;
    private static final int PASSE_PFEIL_5 = 0;
    private static final int PASSE_PFEIL_6 = 6;
    private static final OffsetDateTime offsetDateTime = null;
    private static final long USER = 4;
    private static final long VERSION = 5;
    private HashMap<String, Object> valuesToMethodMap = new HashMap<>();


    public PasseBaseDAOTest() {
        valuesToMethodMap.put("getId", PASSE_ID);
        valuesToMethodMap.put("getPasseMannschaftId", PASSE_MANNSCHAFT_ID);
        valuesToMethodMap.put("getPasseWettkampfId", PASSE_WETTKAMPF_ID);
        valuesToMethodMap.put("getPasseMatchNr", PASSE_MATCH_NR);
        valuesToMethodMap.put("getPasseMatchId", PASSE_MATCH_ID);
        valuesToMethodMap.put("getPasseLfdnr", PASSE_LFDR_NR);
        valuesToMethodMap.put("getPasseDsbMitgliedId", PASSE_DSB_MITGLIED_ID);
        valuesToMethodMap.put("getPfeil1", PASSE_PFEIL_1);
        valuesToMethodMap.put("getPfeil2", PASSE_PFEIL_2);
        valuesToMethodMap.put("getPfeil3", PASSE_PFEIL_3);
        valuesToMethodMap.put("getPfeil4", PASSE_PFEIL_4);
        valuesToMethodMap.put("getPfeil5", PASSE_PFEIL_5);
        valuesToMethodMap.put("getPfeil6", PASSE_PFEIL_6);
    }


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */

    public static PasseBE getPasseBE() {
        final PasseBE expectedPasseBE = new PasseBE();
        expectedPasseBE.setId(PASSE_ID);
        expectedPasseBE.setPasseDsbMitgliedId(PASSE_DSB_MITGLIED_ID);
        expectedPasseBE.setPasseWettkampfId(PASSE_WETTKAMPF_ID);
        expectedPasseBE.setPasseMannschaftId(PASSE_MANNSCHAFT_ID);
        expectedPasseBE.setPasseMatchNr(PASSE_MATCH_NR);
        expectedPasseBE.setPasseMatchId(PASSE_MATCH_ID);
        expectedPasseBE.setPasseLfdnr(PASSE_LFDR_NR);
        expectedPasseBE.setPfeil1(PASSE_PFEIL_1);
        expectedPasseBE.setPfeil2(PASSE_PFEIL_2);
        expectedPasseBE.setPfeil3(PASSE_PFEIL_3);
        expectedPasseBE.setPfeil4(PASSE_PFEIL_4);
        expectedPasseBE.setPfeil5(PASSE_PFEIL_5);
        expectedPasseBE.setPfeil6(PASSE_PFEIL_6);

        expectedPasseBE.setLastModifiedByUserId(USER);

        return expectedPasseBE;
    }


    public static PasseDO getPasseDO() {
        final PasseDO expectedPasseDO = new PasseDO(PASSE_ID,
                PASSE_MANNSCHAFT_ID, PASSE_WETTKAMPF_ID, PASSE_MATCH_NR, PASSE_MATCH_ID, PASSE_LFDR_NR,
                PASSE_DSB_MITGLIED_ID, PASSE_PFEIL_1, PASSE_PFEIL_2, 0, 0, 0, 0, offsetDateTime,
                USER, offsetDateTime, USER, VERSION);

        return expectedPasseDO;
    }


    public HashMap<String, Object> getValuesToMethodMap() {
        return valuesToMethodMap;
    }
}
