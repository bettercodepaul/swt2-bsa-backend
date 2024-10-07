package de.bogenliga.application.business.schuetzenstatistikmatch.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.types.SchuetzenstatistikMatchDO;
import de.bogenliga.application.business.schuetzenstatistikmatch.impl.entity.SchuetzenstatistikMatchBE;
import static de.bogenliga.application.business.schuetzenstatistikmatch.api.types.SchuetzenstatistikMatchDOTest.getSchuetzenstatistikMatchDO;
import static de.bogenliga.application.business.schuetzenstatistikmatch.impl.business.SchuetzenstatistikMatchComponentImplTest.getSchuetzenstatistikMatchBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class to test the functionality on SchuetzenstatistikMatchMapper
 *
 * @author Lennart Raach
 */
public class SchuetzenstatistikMatchMapperTest {
    @Test
    public void toSchuetzenstatistikMatchDO() {

        // prepare test data
        final SchuetzenstatistikMatchBE schuetzenstatistikMatchBE = getSchuetzenstatistikMatchBE();

        final SchuetzenstatistikMatchDO actual = SchuetzenstatistikMatchMapper.toSchuetzenstatistikMatchDO.apply(schuetzenstatistikMatchBE);

        // assert result
        assertThat(actual.getDsbMitgliedName()).isEqualTo(schuetzenstatistikMatchBE.getDsbMitgliedName());
        assertThat(actual.getPfeilpunkteSchnitt()).isEqualTo(schuetzenstatistikMatchBE.getPfeilpunkteSchnitt());
        assertThat(actual.getMatch1()).isEqualTo(schuetzenstatistikMatchBE.getMatch1());
        assertThat(actual.getMatch2()).isEqualTo(schuetzenstatistikMatchBE.getMatch2());
        assertThat(actual.getMatch3()).isEqualTo(schuetzenstatistikMatchBE.getMatch3());
        assertThat(actual.getMatch4()).isEqualTo(schuetzenstatistikMatchBE.getMatch4());
        assertThat(actual.getMatch5()).isEqualTo(schuetzenstatistikMatchBE.getMatch5());
        assertThat(actual.getMatch6()).isEqualTo(schuetzenstatistikMatchBE.getMatch6());
        assertThat(actual.getMatch7()).isEqualTo(schuetzenstatistikMatchBE.getMatch7());
        assertThat(actual.getRueckennummer()).isEqualTo(schuetzenstatistikMatchBE.getRueckennummer());

    }


    @Test
    public void toSchuetzenstatistikMatchBE() {

        // prepare test data
        final SchuetzenstatistikMatchDO schuetzenstatistikMatchDO = getSchuetzenstatistikMatchDO();

        final SchuetzenstatistikMatchBE actual = SchuetzenstatistikMatchMapper.toSchuetzenstatistikMatchBE.apply(schuetzenstatistikMatchDO);

        // assert result
        assertThat(actual.getDsbMitgliedName()).isEqualTo(schuetzenstatistikMatchDO.getDsbMitgliedName());
        assertThat(actual.getPfeilpunkteSchnitt()).isEqualTo(schuetzenstatistikMatchDO.getPfeilpunkteSchnitt());
        assertThat(actual.getMatch1()).isEqualTo(schuetzenstatistikMatchDO.getMatch1());
        assertThat(actual.getMatch2()).isEqualTo(schuetzenstatistikMatchDO.getMatch2());
        assertThat(actual.getMatch3()).isEqualTo(schuetzenstatistikMatchDO.getMatch3());
        assertThat(actual.getMatch4()).isEqualTo(schuetzenstatistikMatchDO.getMatch4());
        assertThat(actual.getMatch5()).isEqualTo(schuetzenstatistikMatchDO.getMatch5());
        assertThat(actual.getMatch6()).isEqualTo(schuetzenstatistikMatchDO.getMatch6());
        assertThat(actual.getMatch7()).isEqualTo(schuetzenstatistikMatchDO.getMatch7());
        assertThat(actual.getRueckennummer()).isEqualTo(schuetzenstatistikMatchDO.getRueckennummer());

    }
}
