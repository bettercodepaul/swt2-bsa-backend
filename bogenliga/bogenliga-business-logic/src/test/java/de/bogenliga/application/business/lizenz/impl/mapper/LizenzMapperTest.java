package de.bogenliga.application.business.lizenz.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.business.kampfrichter.impl.mapper.KampfrichterMapper;
import de.bogenliga.application.business.lizenz.entity.LizenzBE;
import de.bogenliga.application.business.lizenz.mapper.KampfrichterlizenzMapper;
import static de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest.getDsbMitgliedDO;
import static de.bogenliga.application.business.kampfrichter.impl.business.KampfrichterComponentImplTest.getKampfrichterBE;
import static de.bogenliga.application.business.kampfrichter.impl.business.KampfrichterComponentImplTest.getKampfrichterDO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rahul Pöse
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
    public void toBE() throws Exception {
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();

        final LizenzBE actual = KampfrichterlizenzMapper.toKampfrichterlizenz.apply(dsbMitgliedDO);
        assertThat(actual.getLizenzDsbMitgliedId() == LIZENZDSBMITGLIEDID);
    }
}