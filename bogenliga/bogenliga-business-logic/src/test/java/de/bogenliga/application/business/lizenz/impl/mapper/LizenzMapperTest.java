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

    private static final long USER = 0;

    private static final long USERID = 1337;
    private static final long WETTKAMPFID = 9999;
    private static final boolean LEITEND = false;





    @Test
    public void toBE() throws Exception {
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();

        final LizenzBE actual = KampfrichterlizenzMapper.toKampfrichterlizenz.apply(dsbMitgliedDO);

        assertThat(actual.getLizenzId() == USERID);
    }
}