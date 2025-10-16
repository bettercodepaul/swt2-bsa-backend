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
    private static final long VEREIN_ID = 7L;
    private static final long VERANSTALTUNG_ID = 1L;
    private static final long SPORTJAHR = 2002L;

    private static final String SCHUETZENNAME= "Name Schütze";
    private static final float SPORTJAHR_1 = 8f;
    private static final float SPORTJAHR_2 = 7f;
    private static final float SPORTJAHR_3 = 6.5f;
    private static final float SPORTJAHR_4 = 7.8f;
    private static final float SPORTJAHR_5 = 8.2f;
    private static final float ALLEJAHRE_SCHNITT= 7.5f;

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
        expectedSchuetzenstatistikLetzteJahreBE.setSchuetzenname(SCHUETZENNAME);
        expectedSchuetzenstatistikLetzteJahreBE.setSportjahr1(SPORTJAHR_1);
        expectedSchuetzenstatistikLetzteJahreBE.setSportjahr2(SPORTJAHR_2);
        expectedSchuetzenstatistikLetzteJahreBE.setSportjahr3(SPORTJAHR_3);
        expectedSchuetzenstatistikLetzteJahreBE.setSportjahr4(SPORTJAHR_4);
        expectedSchuetzenstatistikLetzteJahreBE.setSportjahr5(SPORTJAHR_5);
        expectedSchuetzenstatistikLetzteJahreBE.setAllejahre_schnitt(ALLEJAHRE_SCHNITT);

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
        final List<SchuetzenstatistikLetzteJahreDO> actual = underTest.getSchuetzenstatistikLetzteJahre(SPORTJAHR, VERANSTALTUNG_ID, VEREIN_ID);

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
                .getSchuetzenstatistikLetzteJahre(SPORTJAHR, VERANSTALTUNG_ID,VEREIN_ID);
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
