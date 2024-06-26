package de.bogenliga.application.business.schuetzenstatistikLetzeJahre.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.dao.SchuetzenstatistikLetzteJahreDAO;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.entity.SchuetzenstatistikLetzteJahreBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.schuetzenstatistikLetzeJahre.impl.business.SchuetzenstatistikLetzteJahreImplTest.getSchuetzenstatistikLetzteJahreBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreDAOTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private SchuetzenstatistikLetzteJahreDAO underTest;
    private static final long vereinId = (long) 7;
    private static final long veranstaltungId = (long) 1;
    private static final long sportjahr = (long) 2002;

    @Test
    public void getSchuetzenstatistikLetzteJahre_oktest() {
        final SchuetzenstatistikLetzteJahreBE expectedBE = getSchuetzenstatistikLetzteJahreBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<SchuetzenstatistikLetzteJahreBE> actual = underTest.getSchuetzenstatistikLetzteJahre(sportjahr, veranstaltungId, vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getSchuetzenname()).isEqualTo(expectedBE.getSchuetzenname());
        assertThat(actual.get(0).getSportjahr1()).isEqualTo(expectedBE.getSportjahr1());
        assertThat(actual.get(0).getSportjahr2()).isEqualTo(expectedBE.getSportjahr2());
        assertThat(actual.get(0).getSportjahr3()).isEqualTo(expectedBE.getSportjahr3());
        assertThat(actual.get(0).getSportjahr4()).isEqualTo(expectedBE.getSportjahr4());
        assertThat(actual.get(0).getSportjahr5()).isEqualTo(expectedBE.getSportjahr5());
        assertThat(actual.get(0).getAllejahre_schnitt()).isEqualTo(expectedBE.getAllejahre_schnitt());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
}
