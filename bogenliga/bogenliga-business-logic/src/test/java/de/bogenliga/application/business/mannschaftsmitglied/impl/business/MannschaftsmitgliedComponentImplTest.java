package de.bogenliga.application.business.mannschaftsmitglied.impl.business;

import java.sql.Date;
import org.junit.Rule;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImpl;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class MannschaftsmitgliedComponentImplTest {


    private static final Long USER = 0L;
    private static final Long VERSION = 0L;



    private static final Long MANNSCHAFTSID = 1111L;
    private static final Long DSB_MITGLIED_ID = 2222L;
    private static final Boolean DSB_MITGLIED_EINGESTZT = false;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private MannschaftsmitgliedDAO mannschaftsmitgliedDAO;
    @InjectMocks
    private MannschaftsmitgliedComponentImpl underTest;
    @Captor
    private ArgumentCaptor<MannschaftsmitgliedBE> mannschaftsmitgliedBEArgumentCaptor;



    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static  MannschaftsmitgliedBE getMannschatfsmitgliedBE(){
        final MannschaftsmitgliedBE expectedBE = new MannschaftsmitgliedBE();
        expectedBE.setMannschaftId(MANNSCHAFTSID);
        expectedBE.setDsbMitgliedId(DSB_MITGLIED_ID);
        expectedBE.setDsbMitgliedEingesetzt(DSB_MITGLIED_EINGESTZT);

        return expectedBE;

    }

    public static MannschaftsmitgliedDO getMannschaftsmitgliedDO(){
        return new MannschaftsmitgliedDO(
                    MANNSCHAFTSID,
                    DSB_MITGLIED_ID,
                    DSB_MITGLIED_EINGESTZT);
    }


}
