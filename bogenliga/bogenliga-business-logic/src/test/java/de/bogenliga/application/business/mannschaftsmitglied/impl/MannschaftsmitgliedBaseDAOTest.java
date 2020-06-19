package de.bogenliga.application.business.mannschaftsmitglied.impl;

import java.time.OffsetDateTime;
import java.util.HashMap;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class MannschaftsmitgliedBaseDAOTest {


    protected static Long id = 1L;
    protected static Long mannschaftId = 2L;
    protected static Long dsbMitgliedId = 3L;
    protected static Integer dsbMitgliedEingesetzt = 1;
    protected static String dsbMitgliedVorname = "Max";
    protected static String dsbMitgliedNachname = "Mustermann";
    protected static Long rueckennummer = 0L;

    private static final OffsetDateTime offsetDateTime = null;
    private static final Long USER = 4L;
    private static final Long VERSION = 5L;
    private HashMap<String, Object> valuesToMethodMap = new HashMap<>();


    public MannschaftsmitgliedBaseDAOTest() {
        valuesToMethodMap.put("getId", id);
        valuesToMethodMap.put("getMannschaftId", mannschaftId);
        valuesToMethodMap.put("getDsbMitgliedId", dsbMitgliedId);
        valuesToMethodMap.put("getDsbMitgliedEingesetzt", dsbMitgliedEingesetzt);
        valuesToMethodMap.put("getDsbMitgliedVorname", dsbMitgliedVorname);
        valuesToMethodMap.put("getDsbMitgliedNachname", dsbMitgliedNachname);
        valuesToMethodMap.put("getRueckennummer", rueckennummer);
    }


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */

    public static MannschaftsmitgliedBE getMannschaftsmitgliedBE() {
        final MannschaftsmitgliedBE expectedMannschaftsmitgliedBE = new MannschaftsmitgliedBE();
        expectedMannschaftsmitgliedBE.setId(id);
        expectedMannschaftsmitgliedBE.setMannschaftId(mannschaftId);
        expectedMannschaftsmitgliedBE.setDsbMitgliedId(dsbMitgliedId);
        expectedMannschaftsmitgliedBE.setDsbMitgliedEingesetzt(dsbMitgliedEingesetzt);
        expectedMannschaftsmitgliedBE.setDsbMitgliedVorname(dsbMitgliedVorname);
        expectedMannschaftsmitgliedBE.setDsbMitgliedNachname(dsbMitgliedNachname);
        expectedMannschaftsmitgliedBE.setRueckennummer(rueckennummer);
        expectedMannschaftsmitgliedBE.setLastModifiedByUserId(USER);

        return expectedMannschaftsmitgliedBE;
    }


    public static MannschaftsmitgliedDO getMannschaftsmitgliedDO() {
        final MannschaftsmitgliedDO expectedMannschaftsmitgliedDO = new MannschaftsmitgliedDO(id, mannschaftId,
                dsbMitgliedId, dsbMitgliedEingesetzt, dsbMitgliedVorname, dsbMitgliedNachname, offsetDateTime,
                USER, offsetDateTime, USER, VERSION, rueckennummer);

        return expectedMannschaftsmitgliedDO;
    }


    public HashMap<String, Object> getValuesToMethodMap() {
        return valuesToMethodMap;
    }
}
