package de.bogenliga.application.business.schuetzenstatistikLetzeJahre.impl.business;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.business.SchuetzenstatistikLetzteJahreComponentImpl;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.entity.SchuetzenstatistikLetzteJahreBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreImplTest {
    private static final long vereinId = (long) 7;
    private static final long veranstaltungId = (long) 1;
    private static final long sportjahr = (long) 2002;

    private static final String schuetzenname = "Name Schütze";
    private static final long sportjahr1 = (long) 8;
    private static final long sportjahr2 = (long) 7;
    private static final long sportjahr3 = (long) 6.5;
    private static final long sportjahr4 = (long) 7.8;
    private static final long sportjahr5 = (long) 8.2;
    private static final long allejahre_schnitt = (long) 7.5;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.dao.SchuetzenstatistikLetzteJahreDAO SchuetzenstatistikLetzteJahreDAO;

    @InjectMocks
    private SchuetzenstatistikLetzteJahreComponentImpl underTest;

    @Captor
    private ArgumentCaptor<SchuetzenstatistikLetzteJahreBE> SchuetzenstatistikLetzteJahreBEArgumentCaptor;

    public static SchuetzenstatistikLetzteJahreBE getSchuetzenstatistikLetzteJahreBE() {
        final SchuetzenstatistikLetzteJahreBE expectedSchuetzenstatistikLetzteJahreBE = new SchuetzenstatistikLetzteJahreBE();
        expectedSchuetzenstatistikLetzteJahreBE.setSchuetzenname(schuetzenname);
        expectedSchuetzenstatistikLetzteJahreBE.setSportjahr1(sportjahr1);
        expectedSchuetzenstatistikLetzteJahreBE.setSportjahr2(sportjahr2);
        expectedSchuetzenstatistikLetzteJahreBE.setSportjahr3(sportjahr3);
        expectedSchuetzenstatistikLetzteJahreBE.setSportjahr4(sportjahr4);
        expectedSchuetzenstatistikLetzteJahreBE.setSportjahr5(sportjahr5);
        expectedSchuetzenstatistikLetzteJahreBE.setAllejahre_schnitt(allejahre_schnitt);

        return expectedSchuetzenstatistikLetzteJahreBE;
    }

    // all parameters are okay
    @Test
    public void getSchuetzenstatistikLetzteJahre_allesok() {
        // prepare test data
        final SchuetzenstatistikLetzteJahreBE expectedSchuetzenstatistikLetzteJahreBE = getSchuetzenstatistikLetzteJahreBE();
        final List<SchuetzenstatistikLetzteJahreBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikLetzteJahreBE);

        // configure mocks
        when(SchuetzenstatistikLetzteJahreDAO.getSchuetzenstatistikLetzteJahre(anyLong(), anyLong(), anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<SchuetzenstatistikLetzteJahreDO> actual = underTest.getSchuetzenstatistikLetzteJahre(sportjahr, veranstaltungId, vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getSchuetzenname()).isEqualTo(expectedSchuetzenstatistikLetzteJahreBE.getSchuetzenname());
        assertThat(actual.get(0).getSportjahr1()).isEqualTo(expectedSchuetzenstatistikLetzteJahreBE.getSportjahr1());
        assertThat(actual.get(0).getSportjahr2()).isEqualTo(expectedSchuetzenstatistikLetzteJahreBE.getSportjahr2());
        assertThat(actual.get(0).getSportjahr3()).isEqualTo(expectedSchuetzenstatistikLetzteJahreBE.getSportjahr3());
        assertThat(actual.get(0).getSportjahr4()).isEqualTo(expectedSchuetzenstatistikLetzteJahreBE.getSportjahr4());
        assertThat(actual.get(0).getSportjahr5()).isEqualTo(expectedSchuetzenstatistikLetzteJahreBE.getSportjahr5());
        assertThat(actual.get(0).getAllejahre_schnitt()).isEqualTo(expectedSchuetzenstatistikLetzteJahreBE.getAllejahre_schnitt());

        // verify invocations
        verify(SchuetzenstatistikLetzteJahreDAO)
                .getSchuetzenstatistikLetzteJahre(sportjahr, veranstaltungId,vereinId);
    }

    //Input ID null -> Exception
    @Test
    public void getSchuetzenstatistikLetzteJahre_IDnull() {
        // prepare test data
        final SchuetzenstatistikLetzteJahreBE expectedSchuetzenstatistikLetzteJahreBE = getSchuetzenstatistikLetzteJahreBE();
        final List<SchuetzenstatistikLetzteJahreBE> expectedBEList = Collections.singletonList(expectedSchuetzenstatistikLetzteJahreBE);
        // configure mocks
        when(SchuetzenstatistikLetzteJahreDAO.getSchuetzenstatistikLetzteJahre(anyLong(), anyLong(), anyLong())).thenReturn(null);
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getSchuetzenstatistikLetzteJahre(anyLong(), anyLong(), anyLong()))
                .withMessageContaining("ENTITY_NOT_FOUND_ERROR: No result found for Sportjahr 0 and Veranstaltungs-ID 0 and Verein-ID 0")
                .withNoCause();
        // assert result

        // verify invocations
        verify(SchuetzenstatistikLetzteJahreDAO).getSchuetzenstatistikLetzteJahre(0,0, 0);
    }
}
