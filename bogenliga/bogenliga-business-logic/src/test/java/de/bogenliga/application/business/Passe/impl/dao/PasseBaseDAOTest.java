package de.bogenliga.application.business.Passe.impl.dao;

import java.time.OffsetDateTime;
import de.bogenliga.application.business.Passe.api.types.PasseDO;
import de.bogenliga.application.business.Passe.impl.entity.PasseBE;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class PasseBaseDAOTest {

    private static final long PASSE_ID = 8;
    private static final long PASSE_MANNSCHAFT_ID = 1;
    private static final long PASSE_WETTKAMPF_ID = 1337;
    private static final long PASSE_LFDR_NR = 2;
    private static final long PASSE_DSB_MITGLIED_ID = 98;
    private static final long PASSE_MATCH_NR = 4;
    private static final long PASSE_MATCH_ID = 224;
    private static final int PASSE_PFEIL_1 = 10;
    private static final int PASSE_PFEIL_2 = 5;
    private static final OffsetDateTime offsetDateTime = null;
    private static final long USER = 1;
    private static final long VERSION = 2;


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

        expectedPasseBE.setLastModifiedByUserId(USER);

        return expectedPasseBE;
    }


    public static PasseDO getPasseDO() {
        final PasseDO expectedPasseDO = new PasseDO(PASSE_ID,
                PASSE_MANNSCHAFT_ID, PASSE_WETTKAMPF_ID, PASSE_MATCH_NR,PASSE_MATCH_ID, PASSE_LFDR_NR,
                PASSE_DSB_MITGLIED_ID, PASSE_PFEIL_1, PASSE_PFEIL_2, 0, 0, 0, 0, offsetDateTime,
                USER, offsetDateTime, USER, VERSION);

        return expectedPasseDO;
    }

}
