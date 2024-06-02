package de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.dao;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.entity.SchuetzenstatistikWettkampfBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.business.SchuetzenstatistikWettkampfComponentImplTest.getSchuetzenstatistikWettkampfBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Anna Baur
 */
public class SchuetzenstatistikWettkampfDAOTest {


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private SchuetzenstatistikWettkampfDAO underTest;
    private static final Long wettkampfId = 2L;
    private static final Long vereinId = 7L;
    private static final Long veranstaltungId = 1L;


    @Test
    public void getSchuetzenstatistikWettkampfVeranstaltung_oktest() {
        final SchuetzenstatistikWettkampfBE expectedBE = getSchuetzenstatistikWettkampfBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<SchuetzenstatistikWettkampfBE> actual = underTest.getSchuetzenstatistikWettkampfVeranstaltung(veranstaltungId,vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedBE.getDsbMitgliedName());
        assertThat(actual.get(0).getRueckenNummer()).isEqualTo(expectedBE.getRueckenNummer());
        assertThat(actual.get(0).getWettkampftag1()).isEqualTo(expectedBE.getWettkampftag1());
        assertThat(actual.get(0).getWettkampftag2()).isEqualTo(expectedBE.getWettkampftag2());
        assertThat(actual.get(0).getWettkampftag3()).isEqualTo(expectedBE.getWettkampftag3());
        assertThat(actual.get(0).getWettkampftag4()).isEqualTo(expectedBE.getWettkampftag4());
        assertThat(actual.get(0).getWettkampftageSchnitt()).isEqualTo(expectedBE.getWettkampftageSchnitt());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void getSchuetzenstatistikWettkampf_oktest() {
        final SchuetzenstatistikWettkampfBE expectedBE = getSchuetzenstatistikWettkampfBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<SchuetzenstatistikWettkampfBE> actual = underTest.getSchuetzenstatistikWettkampf(wettkampfId, vereinId);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();


        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedBE.getDsbMitgliedName());
        assertThat(actual.get(0).getRueckenNummer()).isEqualTo(expectedBE.getRueckenNummer());
        assertThat(actual.get(0).getWettkampftag1()).isEqualTo(expectedBE.getWettkampftag1());
        assertThat(actual.get(0).getWettkampftag2()).isEqualTo(expectedBE.getWettkampftag2());
        assertThat(actual.get(0).getWettkampftag3()).isEqualTo(expectedBE.getWettkampftag3());
        assertThat(actual.get(0).getWettkampftag4()).isEqualTo(expectedBE.getWettkampftag4());
        assertThat(actual.get(0).getWettkampftageSchnitt()).isEqualTo(expectedBE.getWettkampftageSchnitt());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
}
