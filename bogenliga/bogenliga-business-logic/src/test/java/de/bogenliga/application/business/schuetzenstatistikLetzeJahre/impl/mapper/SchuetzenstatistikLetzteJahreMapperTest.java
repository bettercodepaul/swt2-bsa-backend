package de.bogenliga.application.business.schuetzenstatistikLetzeJahre.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.entity.SchuetzenstatistikLetzteJahreBE;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.mapper.SchuetzenstatistikLetzteJahreMapper;
import static de.bogenliga.application.business.schuetzenstatistikLetzeJahre.api.types.SchuetzenstatistikLetzteJahreDOTest.getSchuetzenstatistikLetzteJahreDO;
import static de.bogenliga.application.business.schuetzenstatistikLetzeJahre.impl.business.SchuetzenstatistikLetzteJahreImplTest.getSchuetzenstatistikLetzteJahreBE;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreMapperTest {
    @Test
    public void toSchuetzenstatistikLetzteJahreDO() throws Exception {

        final SchuetzenstatistikLetzteJahreBE schuetzenstatistikLetzteJahreBE = getSchuetzenstatistikLetzteJahreBE();

        final SchuetzenstatistikLetzteJahreDO actual = SchuetzenstatistikLetzteJahreMapper.toSchuetzenstatistikLetzteJahreDO.apply(schuetzenstatistikLetzteJahreBE);


        assertThat(actual.getSchuetzenname()).isEqualTo(schuetzenstatistikLetzteJahreBE.getSchuetzenname());
        assertThat(actual.getSportjahr1()).isEqualTo(schuetzenstatistikLetzteJahreBE.getSportjahr1());
        assertThat(actual.getSportjahr2()).isEqualTo(schuetzenstatistikLetzteJahreBE.getSportjahr2());
        assertThat(actual.getSportjahr3()).isEqualTo(schuetzenstatistikLetzteJahreBE.getSportjahr3());
        assertThat(actual.getSportjahr4()).isEqualTo(schuetzenstatistikLetzteJahreBE.getSportjahr4());
        assertThat(actual.getSportjahr5()).isEqualTo(schuetzenstatistikLetzteJahreBE.getSportjahr5());
        assertThat(actual.getAllejahre_schnitt()).isEqualTo(schuetzenstatistikLetzteJahreBE.getAllejahre_schnitt());


        SchuetzenstatistikLetzteJahreDO schuetzenstatistikLetzteJahreDO = new SchuetzenstatistikLetzteJahreDO(
                schuetzenstatistikLetzteJahreBE.getSchuetzenname(),
                schuetzenstatistikLetzteJahreBE.getSportjahr1(),
                schuetzenstatistikLetzteJahreBE.getSportjahr2(),
                schuetzenstatistikLetzteJahreBE.getSportjahr3(),
                schuetzenstatistikLetzteJahreBE.getSportjahr4(),
                schuetzenstatistikLetzteJahreBE.getSportjahr5(),
                schuetzenstatistikLetzteJahreBE.getAllejahre_schnitt());
    }

    @Test
    public void toBE() throws Exception {
        final SchuetzenstatistikLetzteJahreDO schuetzenstatistikLetzteJahreDO = getSchuetzenstatistikLetzteJahreDO();

        final SchuetzenstatistikLetzteJahreBE actual = SchuetzenstatistikLetzteJahreMapper.toSchuetzenstatistikLetzteJahreBE.apply(schuetzenstatistikLetzteJahreDO);

        assertThat(actual.getSchuetzenname()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSchuetzenname());
        assertThat(actual.getSportjahr1()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSportjahr1());
        assertThat(actual.getSportjahr2()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSportjahr2());
        assertThat(actual.getSportjahr3()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSportjahr3());
        assertThat(actual.getSportjahr4()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSportjahr4());
        assertThat(actual.getSportjahr5()).isEqualTo(schuetzenstatistikLetzteJahreDO.getSportjahr5());
        assertThat(actual.getAllejahre_schnitt()).isEqualTo(schuetzenstatistikLetzteJahreDO.getAllejahre_schnitt());
    }
}
