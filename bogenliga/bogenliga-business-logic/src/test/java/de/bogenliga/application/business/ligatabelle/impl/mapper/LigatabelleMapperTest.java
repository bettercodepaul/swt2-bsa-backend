package de.bogenliga.application.business.ligatabelle.impl.mapper;

import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.business.ligatabelle.impl.entity.LigatabelleBE;
import static de.bogenliga.application.business.ligatabelle.impl.business.LigatabelleComponentImplTest.getLigatabelleBE;
import static de.bogenliga.application.business.ligatabelle.impl.business.LigatabelleComponentImplTest.getLigatabelleDO;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LigatabelleMapperTest {

    @Test
    public void toLigatabelleDO() throws Exception {

        final LigatabelleBE ligatabelleBE = getLigatabelleBE();

        final LigatabelleDO actual = LigatabelleMapper.toLigatabelleDO.apply(ligatabelleBE);

        assertThat(actual.getveranstaltungId()).isEqualTo(ligatabelleBE.getVeranstaltungId());
        assertThat(actual.getveranstaltungName()).isEqualTo(ligatabelleBE.getVeranstaltungName());
        assertThat(actual.getwettkampfId()).isEqualTo(ligatabelleBE.getWettkampfId());
        assertThat(actual.getwettkampfTag()).isEqualTo(ligatabelleBE.getWettkampfTag());
        assertThat(actual.getmannschaftId()).isEqualTo(ligatabelleBE.getMannschaftId());
        assertThat(actual.getmannschaftNummer()).isEqualTo(ligatabelleBE.getMannschaftNummer());
        assertThat(actual.getvereinId()).isEqualTo(ligatabelleBE.getVereinId());
        assertThat(actual.getvereinName()).isEqualTo(ligatabelleBE.getVereinName());
        assertThat(actual.getmatchpkt()).isEqualTo(ligatabelleBE.getMatchpkt());
        assertThat(actual.getMatchpktGegen()).isEqualTo(ligatabelleBE.getMatchpktGegen());
        assertThat(actual.getsatzpkt()).isEqualTo(ligatabelleBE.getSatzpkt());
        assertThat(actual.getSatzpktGegen()).isEqualTo(ligatabelleBE.getSatzpktGegen());
        assertThat(actual.getSatzpktDifferenz()).isEqualTo(ligatabelleBE.getSatzpktDifferenz());
        assertThat(actual.getsortierung()).isEqualTo(ligatabelleBE.getSortierung());
        assertThat(actual.gettabellenplatz()).isEqualTo(ligatabelleBE.getTabellenplatz());
        assertThat(actual.getMatchCount()).isEqualTo(ligatabelleBE.getMatchCount());

        LigatabelleDO ligatabelleDO = new LigatabelleDO(
                ligatabelleBE.getVeranstaltungId(),
                ligatabelleBE.getVeranstaltungName(),
                ligatabelleBE.getWettkampfId(),
                ligatabelleBE.getWettkampfTag(),
                ligatabelleBE.getMannschaftId(),
                ligatabelleBE.getMannschaftNummer(),
                ligatabelleBE.getVereinId(),
                ligatabelleBE.getVereinName(),
                ligatabelleBE.getMatchpkt(),
                ligatabelleBE.getMatchpktGegen(),
                ligatabelleBE.getSatzpkt(),
                ligatabelleBE.getSatzpktGegen(),
                ligatabelleBE.getSatzpktDifferenz(),
                ligatabelleBE.getSortierung(),
                ligatabelleBE.getTabellenplatz(),
                ligatabelleBE.getMatchCount());

        assertThat(actual.hashCode()).isEqualTo(ligatabelleDO.hashCode());
    }


    @Test
    public void toBE() throws Exception {
        final LigatabelleDO ligatabelleDO = getLigatabelleDO();

        final LigatabelleBE actual = LigatabelleMapper.toLigatabelleBE.apply(ligatabelleDO);

        assertThat(actual.getVeranstaltungId()).isEqualTo(ligatabelleDO.getveranstaltungId());
        assertThat(actual.getVeranstaltungName()).isEqualTo(ligatabelleDO.getveranstaltungName());
        assertThat(actual.getWettkampfId()).isEqualTo(ligatabelleDO.getwettkampfId());
        assertThat(actual.getWettkampfTag()).isEqualTo(ligatabelleDO.getwettkampfTag());
        assertThat(actual.getMannschaftId()).isEqualTo(ligatabelleDO.getmannschaftId());
        assertThat(actual.getMannschaftNummer()).isEqualTo(ligatabelleDO.getmannschaftNummer());
        assertThat(actual.getVereinId()).isEqualTo(ligatabelleDO.getvereinId());
        assertThat(actual.getVereinName()).isEqualTo(ligatabelleDO.getvereinName());
        assertThat(actual.getMatchpkt()).isEqualTo(ligatabelleDO.getmatchpkt());
        assertThat(actual.getMatchpktGegen()).isEqualTo(ligatabelleDO.getMatchpktGegen());
        assertThat(actual.getSatzpkt()).isEqualTo(ligatabelleDO.getsatzpkt());
        assertThat(actual.getSatzpktGegen()).isEqualTo(ligatabelleDO.getSatzpktGegen());
        assertThat(actual.getSatzpktDifferenz()).isEqualTo(ligatabelleDO.getSatzpktDifferenz());
        assertThat(actual.getSortierung()).isEqualTo(ligatabelleDO.getsortierung());
        assertThat(actual.getTabellenplatz()).isEqualTo(ligatabelleDO.gettabellenplatz());
        assertThat(actual.getMatchCount()).isEqualTo(ligatabelleDO.getMatchCount());
    }

}