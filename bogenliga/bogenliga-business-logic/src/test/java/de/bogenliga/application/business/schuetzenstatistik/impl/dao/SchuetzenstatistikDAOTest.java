package de.bogenliga.application.business.schuetzenstatistik.impl.dao;

import de.bogenliga.application.business.schuetzenstatistik.impl.entity.SchuetzenstatistikBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static de.bogenliga.application.business.schuetzenstatistik.impl.business.SchuetzenstatistikComponentImplTest.getSchuetzenstatistikBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SchuetzenstatistikDAOTest {

    private static final long USER = 0;

    private static final long ID = 1337;
    private static final String LIGANAME = "Test Liga";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private SchuetzenstatistikDAO underTest;


    @Test
    public void getSchuetzenstatistikVeranstaltung_oktest() {
        final SchuetzenstatistikBE expectedBE = getSchuetzenstatistikBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<SchuetzenstatistikBE> actual = underTest.getSchuetzenstatistikVeranstaltung(expectedBE.getVeranstaltungId(), expectedBE.getVereinId());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId()).isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName()).isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getWettkampfId()).isEqualTo(expectedBE.getWettkampfId());
        assertThat(actual.get(0).getWettkampfTag()).isEqualTo(expectedBE.getWettkampfTag());
        assertThat(actual.get(0).getMannschaftId()).isEqualTo(expectedBE.getMannschaftId());
        assertThat(actual.get(0).getMannschaftNummer()).isEqualTo(expectedBE.getMannschaftNummer());
        assertThat(actual.get(0).getVereinId()).isEqualTo(expectedBE.getVereinId());
        assertThat(actual.get(0).getVereinName()).isEqualTo(expectedBE.getVereinName());
        assertThat(actual.get(0).getMatchId()).isEqualTo(expectedBE.getMatchId());
        assertThat(actual.get(0).getMatchNr()).isEqualTo(expectedBE.getMatchNr());
        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(expectedBE.getDsbMitgliedId());
        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedBE.getDsbMitgliedName());
        assertThat(actual.get(0).getRueckenNummer()).isEqualTo(expectedBE.getRueckenNummer());
        assertThat(actual.get(0).getPfeilpunkteSchnitt()).isEqualTo(expectedBE.getPfeilpunkteSchnitt());
        assertThat(actual.get(0).getschuetzeSatz1()).isEqualTo(expectedBE.getschuetzeSatz1());
        assertThat(actual.get(0).getschuetzeSatz2()).isEqualTo(expectedBE.getschuetzeSatz2());
        assertThat(actual.get(0).getschuetzeSatz3()).isEqualTo(expectedBE.getschuetzeSatz3());
        assertThat(actual.get(0).getschuetzeSatz4()).isEqualTo(expectedBE.getschuetzeSatz4());
        assertThat(actual.get(0).getschuetzeSatz5()).isEqualTo(expectedBE.getschuetzeSatz5());
        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void getSchuetzenstatistikWettkampf_oktest() {
        final SchuetzenstatistikBE expectedBE = getSchuetzenstatistikBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<SchuetzenstatistikBE> actual = underTest.getSchuetzenstatistikWettkampf(expectedBE.getWettkampfId(), expectedBE.getVereinId());

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getVeranstaltungId()).isEqualTo(expectedBE.getVeranstaltungId());
        assertThat(actual.get(0).getVeranstaltungName()).isEqualTo(expectedBE.getVeranstaltungName());
        assertThat(actual.get(0).getWettkampfId()).isEqualTo(expectedBE.getWettkampfId());
        assertThat(actual.get(0).getWettkampfTag()).isEqualTo(expectedBE.getWettkampfTag());
        assertThat(actual.get(0).getMannschaftId()).isEqualTo(expectedBE.getMannschaftId());
        assertThat(actual.get(0).getMannschaftNummer()).isEqualTo(expectedBE.getMannschaftNummer());
        assertThat(actual.get(0).getVereinId()).isEqualTo(expectedBE.getVereinId());
        assertThat(actual.get(0).getVereinName()).isEqualTo(expectedBE.getVereinName());
        assertThat(actual.get(0).getMatchId()).isEqualTo(expectedBE.getMatchId());
        assertThat(actual.get(0).getMatchNr()).isEqualTo(expectedBE.getMatchNr());
        assertThat(actual.get(0).getDsbMitgliedId()).isEqualTo(expectedBE.getDsbMitgliedId());
        assertThat(actual.get(0).getDsbMitgliedName()).isEqualTo(expectedBE.getDsbMitgliedName());
        assertThat(actual.get(0).getRueckenNummer()).isEqualTo(expectedBE.getRueckenNummer());
        assertThat(actual.get(0).getPfeilpunkteSchnitt()).isEqualTo(expectedBE.getPfeilpunkteSchnitt());
        assertThat(actual.get(0).getschuetzeSatz1()).isEqualTo(expectedBE.getschuetzeSatz1());
        assertThat(actual.get(0).getschuetzeSatz2()).isEqualTo(expectedBE.getschuetzeSatz2());
        assertThat(actual.get(0).getschuetzeSatz3()).isEqualTo(expectedBE.getschuetzeSatz3());
        assertThat(actual.get(0).getschuetzeSatz4()).isEqualTo(expectedBE.getschuetzeSatz4());
        assertThat(actual.get(0).getschuetzeSatz5()).isEqualTo(expectedBE.getschuetzeSatz5());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
}
