package de.bogenliga.application.business.lizenz.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import static de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest.getDsbMitgliedDO;
import static de.bogenliga.application.business.lizenz.impl.business.LizenzComponentImplTest.getLizenzBE;
import static de.bogenliga.application.business.lizenz.impl.business.LizenzComponentImplTest.getLizenzDO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rahul Pöse & Manuel Baisch
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 */
public class LizenzMapperTest {

    private long LIZENZID = 0;
    private String LIZENZNUMMER = "WT1234567";
    private long LIZENZREGIONID = 1;
    private long LIZENZDSBMITGLIEDID = 1337L;
    private String LIZENZTYP = "Liga";
    private long LIZENZDISZIPLINID = 0;





    @Test
    public void KampfrichterToDO() throws Exception {
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();

        final LizenzBE actual = KampfrichterlizenzMapper.toKampfrichterlizenz.apply(dsbMitgliedDO);
        assertThat(actual.getLizenzDsbMitgliedId() == LIZENZDSBMITGLIEDID);
    }

    @Test
    public void toDO() throws Exception {
        final LizenzBE lizenzBe = getLizenzBE();

        final LizenzDO actual = LizenzMapper.toLizenzDO.apply(lizenzBe);

        assertThat(actual.getLizenzId()).isEqualTo(LIZENZID);
        assertThat(actual.getLizenznummer()).isEqualTo(LIZENZNUMMER);
        assertThat(actual.getLizenzRegionId()).isEqualTo(LIZENZREGIONID);
        assertThat(actual.getLizenzDsbMitgliedId()).isEqualTo(LIZENZDSBMITGLIEDID);
        assertThat(actual.getLizenztyp()).isEqualTo(LIZENZTYP);
        assertThat(actual.getLizenzDisziplinId()).isEqualTo(LIZENZDISZIPLINID);
    }


    @Test
    public void toBE() throws Exception {
        final LizenzDO lizenzDO = getLizenzDO();

        final LizenzBE actual = LizenzMapper.toLizenzBE.apply(lizenzDO);

        assertThat(actual.getLizenzId()).isEqualTo(LIZENZID);
        assertThat(actual.getLizenznummer()).isEqualTo(LIZENZNUMMER);
        assertThat(actual.getLizenzRegionId()).isEqualTo(LIZENZREGIONID);
        assertThat(actual.getLizenzDsbMitgliedId()).isEqualTo(LIZENZDSBMITGLIEDID);
        assertThat(actual.getLizenztyp()).isEqualTo(LIZENZTYP);
        assertThat(actual.getLizenzDisziplinId()).isEqualTo(LIZENZDISZIPLINID);
    }
}