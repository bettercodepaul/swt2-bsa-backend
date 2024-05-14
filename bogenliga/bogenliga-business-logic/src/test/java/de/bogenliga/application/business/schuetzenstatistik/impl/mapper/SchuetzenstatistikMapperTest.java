package de.bogenliga.application.business.schuetzenstatistik.impl.mapper;

import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.business.schuetzenstatistik.impl.entity.SchuetzenstatistikBE;
import org.junit.Test;

import static de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDOTest.getSchuetzenstatistikDO;
import static de.bogenliga.application.business.schuetzenstatistik.impl.business.SchuetzenstatistikComponentImplTest.getSchuetzenstatistikBE;
import static org.assertj.core.api.Assertions.assertThat;

public class SchuetzenstatistikMapperTest {


    @Test
    public void toSchuetzenstatistikDO() throws Exception {

        final SchuetzenstatistikBE schuetzenstatistikBE = getSchuetzenstatistikBE();

        final SchuetzenstatistikDO actual = SchuetzenstatistikMapper.toSchuetzenstatistikDO.apply(schuetzenstatistikBE);

        assertThat(actual.getveranstaltungId()).isEqualTo(schuetzenstatistikBE.getVeranstaltungId());
        assertThat(actual.getveranstaltungName()).isEqualTo(schuetzenstatistikBE.getVeranstaltungName());
        assertThat(actual.getwettkampfId()).isEqualTo(schuetzenstatistikBE.getWettkampfId());
        assertThat(actual.getwettkampfTag()).isEqualTo(schuetzenstatistikBE.getWettkampfTag());
        assertThat(actual.getmannschaftId()).isEqualTo(schuetzenstatistikBE.getMannschaftId());
        assertThat(actual.getmannschaftNummer()).isEqualTo(schuetzenstatistikBE.getMannschaftNummer());
        assertThat(actual.getvereinId()).isEqualTo(schuetzenstatistikBE.getVereinId());
        assertThat(actual.getvereinName()).isEqualTo(schuetzenstatistikBE.getVereinName());
        assertThat(actual.getMatchId()).isEqualTo(schuetzenstatistikBE.getMatchId());
        assertThat(actual.getDsbMitgliedId()).isEqualTo(schuetzenstatistikBE.getDsbMitgliedId());
        assertThat(actual.getDsbMitgliedName()).isEqualTo(schuetzenstatistikBE.getDsbMitgliedName());
        assertThat(actual.getPfeilpunkteSchnitt()).isEqualTo(schuetzenstatistikBE.getPfeilpunkteSchnitt());
        assertThat(actual.getSchuetzeSaetze()).isEqualTo(schuetzenstatistikBE.getSchuetzeSaetze());

        SchuetzenstatistikDO schuetzenstatistikDO = new SchuetzenstatistikDO(
                schuetzenstatistikBE.getVeranstaltungId(),
                schuetzenstatistikBE.getVeranstaltungName(),
                schuetzenstatistikBE.getWettkampfId(),
                schuetzenstatistikBE.getWettkampfTag(),
                schuetzenstatistikBE.getMannschaftId(),
                schuetzenstatistikBE.getMannschaftNummer(),
                schuetzenstatistikBE.getVereinId(),
                schuetzenstatistikBE.getVereinName(),
                schuetzenstatistikBE.getMatchId(),
                schuetzenstatistikBE.getMatchNr(),
                schuetzenstatistikBE.getDsbMitgliedId(),
                schuetzenstatistikBE.getDsbMitgliedName(),
                schuetzenstatistikBE.getRueckenNummer(),
                schuetzenstatistikBE.getPfeilpunkteSchnitt(),
                schuetzenstatistikBE.getSchuetzeSaetze());
    }


    @Test
    public void toBE() throws Exception {
        final SchuetzenstatistikDO schuetzenstatistikDO = getSchuetzenstatistikDO();

        final SchuetzenstatistikBE actual = SchuetzenstatistikMapper.toSchuetzenstatistikBE.apply(schuetzenstatistikDO);

        assertThat(actual.getVeranstaltungId()).isEqualTo(schuetzenstatistikDO.getveranstaltungId());
        assertThat(actual.getVeranstaltungName()).isEqualTo(schuetzenstatistikDO.getveranstaltungName());
        assertThat(actual.getWettkampfId()).isEqualTo(schuetzenstatistikDO.getwettkampfId());
        assertThat(actual.getWettkampfTag()).isEqualTo(schuetzenstatistikDO.getwettkampfTag());
        assertThat(actual.getMannschaftId()).isEqualTo(schuetzenstatistikDO.getmannschaftId());
        assertThat(actual.getMannschaftNummer()).isEqualTo(schuetzenstatistikDO.getmannschaftNummer());
        assertThat(actual.getVereinId()).isEqualTo(schuetzenstatistikDO.getvereinId());
        assertThat(actual.getVereinName()).isEqualTo(schuetzenstatistikDO.getvereinName());
        assertThat(actual.getMatchId()).isEqualTo(schuetzenstatistikDO.getMatchId());
        assertThat(actual.getMatchNr()).isEqualTo(schuetzenstatistikDO.getMatchNr());
        assertThat(actual.getDsbMitgliedId()).isEqualTo(schuetzenstatistikDO.getDsbMitgliedId());
        assertThat(actual.getDsbMitgliedName()).isEqualTo(schuetzenstatistikDO.getDsbMitgliedName());
        assertThat(actual.getRueckenNummer()).isEqualTo(schuetzenstatistikDO.getRueckenNummer());
        assertThat(actual.getPfeilpunkteSchnitt()).isEqualTo(schuetzenstatistikDO.getPfeilpunkteSchnitt());
        assertThat(actual.getSchuetzeSaetze()).isEqualTo(schuetzenstatistikDO.getSchuetzeSaetze());
    }

}
