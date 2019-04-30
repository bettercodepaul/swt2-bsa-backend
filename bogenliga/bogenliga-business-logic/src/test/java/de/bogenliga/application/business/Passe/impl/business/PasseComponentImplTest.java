package de.bogenliga.application.business.Passe.impl.business;

import java.time.OffsetDateTime;
import org.junit.Rule;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.Passe.api.types.PasseDO;
import de.bogenliga.application.business.Passe.impl.dao.PasseDAO;
import de.bogenliga.application.business.Passe.impl.entity.PasseBE;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import static org.junit.Assert.*;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class PasseComponentImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static final long PASSE_ID = 8;
    private static final long PASSE_MANNSCHAFT_ID = 1;
    private static final long PASSE_WETTKAMPF_ID = 1337;
    private static final long PASSE_LFDR_NR = 2;
    private static final long PASSE_DSB_MITGLIED_ID = 98;
    private static final long PASSE_MATCH_NR = 4;
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
        expectedPasseBE.setPasseLfdnr(PASSE_LFDR_NR);
        expectedPasseBE.setPfeil1(PASSE_PFEIL_1);
        expectedPasseBE.setPfeil2(PASSE_PFEIL_2);

        expectedPasseBE.setLastModifiedByUserId(USER);

        return expectedPasseBE;
    }


    public static PasseDO getPasseDO() {
        final PasseDO expectedPasseDO = new PasseDO(PASSE_ID,
                PASSE_MANNSCHAFT_ID, PASSE_WETTKAMPF_ID, PASSE_MATCH_NR, PASSE_LFDR_NR,
                PASSE_DSB_MITGLIED_ID, PASSE_PFEIL_1, PASSE_PFEIL_2, 0, 0, 0, 0, offsetDateTime,
                USER, offsetDateTime, USER, VERSION);

        return expectedPasseDO;
    }


}