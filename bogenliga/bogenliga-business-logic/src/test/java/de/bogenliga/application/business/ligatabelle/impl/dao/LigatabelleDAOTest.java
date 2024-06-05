package de.bogenliga.application.business.ligatabelle.impl.dao;

import de.bogenliga.application.business.ligatabelle.impl.entity.LigatabelleBE;
import org.junit.Test;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.ligatabelle.impl.business.LigatabelleComponentImplTest.getLigatabelleBE;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LigatabelleDAOTest {
    private static final long USER = 0;

    private static final long ID = 1337;
    private static final String LIGANAME = "Test Liga";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private LigatabelleDAO underTest;


    @Test
    public void getLigatabelleVeranstaltung_oktest() {
        final LigatabelleBE expectedBE = getLigatabelleBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<LigatabelleBE> actual = underTest.getLigatabelleVeranstaltung(expectedBE.getVeranstaltungId());

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
        assertThat(actual.get(0).getMatchpkt()).isEqualTo(expectedBE.getMatchpkt());
        assertThat(actual.get(0).getMatchpktGegen()).isEqualTo(expectedBE.getMatchpktGegen());
        assertThat(actual.get(0).getSatzpkt()).isEqualTo(expectedBE.getSatzpkt());
        assertThat(actual.get(0).getSatzpktGegen()).isEqualTo(expectedBE.getSatzpktGegen());
        assertThat(actual.get(0).getSatzpktDifferenz()).isEqualTo(expectedBE.getSatzpktDifferenz());
        assertThat(actual.get(0).getSortierung()).isEqualTo(expectedBE.getSortierung());
        assertThat(actual.get(0).getTabellenplatz()).isEqualTo(expectedBE.getTabellenplatz());
        assertThat(actual.get(0).getMatchCount()).isEqualTo(expectedBE.getMatchCount());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void getLigatabelleWettkampf_oktest() {
        final LigatabelleBE expectedBE = getLigatabelleBE();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<LigatabelleBE> actual = underTest.getLigatabelleWettkampf(expectedBE.getWettkampfId());

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
        assertThat(actual.get(0).getMatchpkt()).isEqualTo(expectedBE.getMatchpkt());
        assertThat(actual.get(0).getMatchpktGegen()).isEqualTo(expectedBE.getMatchpktGegen());
        assertThat(actual.get(0).getSatzpkt()).isEqualTo(expectedBE.getSatzpkt());
        assertThat(actual.get(0).getSatzpktGegen()).isEqualTo(expectedBE.getSatzpktGegen());
        assertThat(actual.get(0).getSatzpktDifferenz()).isEqualTo(expectedBE.getSatzpktDifferenz());
        assertThat(actual.get(0).getSortierung()).isEqualTo(expectedBE.getSortierung());
        assertThat(actual.get(0).getTabellenplatz()).isEqualTo(expectedBE.getTabellenplatz());
        assertThat(actual.get(0).getMatchCount()).isEqualTo(expectedBE.getMatchCount());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
    }
}