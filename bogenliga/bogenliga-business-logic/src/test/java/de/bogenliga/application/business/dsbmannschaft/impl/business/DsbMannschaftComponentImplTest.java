package de.bogenliga.application.business.dsbmannschaft.impl.business;

import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAO;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    public static DsbMannschaftDO getDsbMannschaftDO() {
        return new DsbMannschaftDO(
                id,
                vereinId,
                nummer,
                benutzerId,
                veranstaltungId);
    }

    @Test
    public void findAll() {
        // prepare test data
        final DsbMannschaftBE expectedBE = getDsbMannschaftBE();
        final List<DsbMannschaftBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(dsbMannschaftDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<DsbMannschaftDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getBenutzerId())
                .isEqualTo(expectedBE.getBenutzerId());
        assertThat(actual.get(0).getNummer())
                .isEqualTo(expectedBE.getNummer());
        assertThat(actual.get(0).getVeranstaltungId())
                .isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVereinId())
                .isEqualTo(expectedBE.getVereinId());


        // verify invocations
        verify(dsbMannschaftDAO).findAll();
    }
}
