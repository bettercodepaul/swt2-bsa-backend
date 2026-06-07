package de.bogenliga.application.business.anzeigen;

import de.bogenliga.application.business.wettkampf.api.types.AnzeigenDO;
import de.bogenliga.application.business.wettkampf.impl.entity.AnzeigenBE;

import java.time.OffsetDateTime;
import java.util.HashMap;

public class AnzeigenDAOTestHelper {
    private static final long ANZEIGEN_ID = 11;
    private static final String ANZEIGEN_PHYSISCHE_BILDSCHIRM_ID = "abcd";
    private static final String ANZEIGEN_TABLE_TYP = "table";
    private static final long ANZEIGEN_VERANSTALTUNGS_ID = 1;
    private static final int ANZEIGEN_AKTUELLES_MATCH = 1;

    private static final OffsetDateTime offsetDateTime = null;
    private static final long USER = 4;
    private static final long VERSION = 5;
    private final HashMap<String, Object> valuesToMethodMap = new HashMap<>();


    public AnzeigenDAOTestHelper() {
        valuesToMethodMap.put("getId", ANZEIGEN_ID);
        valuesToMethodMap.put("getPhysischeBildschirmId", ANZEIGEN_PHYSISCHE_BILDSCHIRM_ID);
        valuesToMethodMap.put("getTableTyp", ANZEIGEN_TABLE_TYP);
        valuesToMethodMap.put("getVeranstaltungsId", ANZEIGEN_VERANSTALTUNGS_ID);
        valuesToMethodMap.put("getAktuellesMatch", ANZEIGEN_AKTUELLES_MATCH);
    }


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */

    public static AnzeigenBE getAnzeigenBE() {
        final AnzeigenBE expectedAnzeigenBE = new AnzeigenBE();
        expectedAnzeigenBE.setId(ANZEIGEN_ID);
        expectedAnzeigenBE.setPhysischeBildschirmId(ANZEIGEN_PHYSISCHE_BILDSCHIRM_ID);
        expectedAnzeigenBE.setTableTyp(ANZEIGEN_TABLE_TYP);
        expectedAnzeigenBE.setVeranstaltungsId(ANZEIGEN_VERANSTALTUNGS_ID);
        expectedAnzeigenBE.setAktuellesMatch(ANZEIGEN_AKTUELLES_MATCH);

        expectedAnzeigenBE.setLastModifiedByUserId(USER);

        return expectedAnzeigenBE;
    }


    public static AnzeigenDO getAnzeigenDO() {
        return new AnzeigenDO(ANZEIGEN_ID,
                ANZEIGEN_PHYSISCHE_BILDSCHIRM_ID, ANZEIGEN_TABLE_TYP,
                ANZEIGEN_VERANSTALTUNGS_ID, ANZEIGEN_AKTUELLES_MATCH,
                offsetDateTime, USER, offsetDateTime, USER, VERSION);
    }


    public HashMap<String, Object> getValuesToMethodMap() {
        return valuesToMethodMap;
    }

}

