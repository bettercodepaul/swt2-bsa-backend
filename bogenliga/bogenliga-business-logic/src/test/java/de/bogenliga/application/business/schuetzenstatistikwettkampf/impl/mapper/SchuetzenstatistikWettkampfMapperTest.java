package de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types.SchuetzenstatistikWettkampftageDO;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.entity.SchuetzenstatistikWettkampfBE;
import static de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types.SchuetzenstatistikDOTest.getSchuetzenstatistikWettkampfDO;
import static de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.business.SchuetzenstatistikWettkampfComponentImplTest.getSchuetzenstatistikWettkampfBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Anna Baur
 */
public class SchuetzenstatistikWettkampfMapperTest {
    @Test
    public void toSchuetzenstatistikWettkampfDO() throws Exception {

        final SchuetzenstatistikWettkampfBE schuetzenstatistikWettkampfBE = getSchuetzenstatistikWettkampfBE();

        final SchuetzenstatistikWettkampftageDO actual = SchuetzenstatistikWettkampfMapper.toSchuetzenstatistikWettkampfDO.apply(schuetzenstatistikWettkampfBE);


        assertThat(actual.getDsbMitgliedName()).isEqualTo(schuetzenstatistikWettkampfBE.getDsbMitgliedName());
        assertThat(actual.getWettkampftag1()).isEqualTo(schuetzenstatistikWettkampfBE.getWettkampftag1());
        assertThat(actual.getWettkampftag2()).isEqualTo(schuetzenstatistikWettkampfBE.getWettkampftag2());
        assertThat(actual.getWettkampftag3()).isEqualTo(schuetzenstatistikWettkampfBE.getWettkampftag3());
        assertThat(actual.getWettkampftag4()).isEqualTo(schuetzenstatistikWettkampfBE.getWettkampftag4());
        assertThat(actual.getWettkampftageSchnitt()).isEqualTo(schuetzenstatistikWettkampfBE.getWettkampftageSchnitt());

        SchuetzenstatistikWettkampftageDO schuetzenstatistikWettkampfDO = new SchuetzenstatistikWettkampftageDO(
                schuetzenstatistikWettkampfBE.getDsbMitgliedName(),
                schuetzenstatistikWettkampfBE.getRueckenNummer(),
                schuetzenstatistikWettkampfBE.getWettkampftag1(),
                schuetzenstatistikWettkampfBE.getWettkampftag2(),
                schuetzenstatistikWettkampfBE.getWettkampftag3(),
                schuetzenstatistikWettkampfBE.getWettkampftag4(),
                schuetzenstatistikWettkampfBE.getWettkampftageSchnitt());
    }


    @Test
    public void toBE() throws Exception {
        final SchuetzenstatistikWettkampftageDO schuetzenstatistikWettkampfDO = getSchuetzenstatistikWettkampfDO();

        final SchuetzenstatistikWettkampfBE actual = SchuetzenstatistikWettkampfMapper.toSchuetzenstatistikWettkampfBE.apply(schuetzenstatistikWettkampfDO);


        assertThat(actual.getDsbMitgliedName()).isEqualTo(schuetzenstatistikWettkampfDO.getDsbMitgliedName());
        assertThat(actual.getRueckenNummer()).isEqualTo(schuetzenstatistikWettkampfDO.getRueckenNummer());
        assertThat(actual.getWettkampftag1()).isEqualTo(schuetzenstatistikWettkampfDO.getWettkampftag1());
        assertThat(actual.getWettkampftag2()).isEqualTo(schuetzenstatistikWettkampfDO.getWettkampftag2());
        assertThat(actual.getWettkampftag3()).isEqualTo(schuetzenstatistikWettkampfDO.getWettkampftag3());
        assertThat(actual.getWettkampftag4()).isEqualTo(schuetzenstatistikWettkampfDO.getWettkampftag4());
        assertThat(actual.getWettkampftageSchnitt()).isEqualTo(schuetzenstatistikWettkampfDO.getWettkampftageSchnitt());

    }

}
