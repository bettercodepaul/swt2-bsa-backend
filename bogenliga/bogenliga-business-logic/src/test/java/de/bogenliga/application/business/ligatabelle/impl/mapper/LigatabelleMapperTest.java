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
        assertThat(actual.getmatchpktGegen()).isEqualTo(ligatabelleBE.getMatchpkt_gegen());
        assertThat(actual.getsatzpkt()).isEqualTo(ligatabelleBE.getSatzpkt());
        assertThat(actual.getsatzpktGegen()).isEqualTo(ligatabelleBE.getSatzpkt_gegen());
        assertThat(actual.getsatzpktDifferenz()).isEqualTo(ligatabelleBE.getSatzpkt_differenz());
        assertThat(actual.getsortierung()).isEqualTo(ligatabelleBE.getSortierung());
        assertThat(actual.gettabellenplatz()).isEqualTo(ligatabelleBE.getTabellenplatz());

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
                ligatabelleBE.getMatchpkt_gegen(),
                ligatabelleBE.getSatzpkt(),
                ligatabelleBE.getSatzpkt_gegen(),
                ligatabelleBE.getSatzpkt_differenz(),
                ligatabelleBE.getSortierung(),
                ligatabelleBE.getTabellenplatz());

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
        assertThat(actual.getMatchpkt_gegen()).isEqualTo(ligatabelleDO.getmatchpktGegen());
        assertThat(actual.getSatzpkt()).isEqualTo(ligatabelleDO.getsatzpkt());
        assertThat(actual.getSatzpkt_gegen()).isEqualTo(ligatabelleDO.getsatzpktGegen());
        assertThat(actual.getSatzpkt_differenz()).isEqualTo(ligatabelleDO.getsatzpktDifferenz());
        assertThat(actual.getSortierung()).isEqualTo(ligatabelleDO.getsortierung());
        assertThat(actual.getTabellenplatz()).isEqualTo(ligatabelleDO.gettabellenplatz());
    }

}