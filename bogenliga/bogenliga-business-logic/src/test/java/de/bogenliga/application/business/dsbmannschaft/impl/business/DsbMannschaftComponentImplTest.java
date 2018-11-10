package de.bogenliga.application.business.dsbmannschaft.impl.business;

import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAO;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class DsbMannschaftComponentImplTest {
    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final long id = 2222L;
    private static final long vereinId=101010;
    private static final long nummer=111;
    private static final long benutzerId=12;
    private static final long veranstaltungId=1;

    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DsbMannschaftDAO dsbMannschaftDAO;
    @InjectMocks
    private DsbMannschaftComponentImpl underTest;
    @Captor
    private ArgumentCaptor<DsbMannschaftBE> dsbMannschaftBEArgumentCaptor;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static DsbMannschaftBE getDsbMannschaftBE() {
        final DsbMannschaftBE expectedBE = new DsbMannschaftBE();

        expectedBE.setId(id);
        expectedBE.setVereinId(vereinId);
        expectedBE.setNummer(nummer);
        expectedBE.setBenutzerId(benutzerId);
        expectedBE.setVeranstaltungId(veranstaltungId);


        return expectedBE;
    }
}
