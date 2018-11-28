package de.bogenliga.application.business.kampfrichter.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
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
public class KampfrichterMapperTest {

    private static final long USER = 0;

    private static final long USERID = 1337;
    private static final long WETTKAMPFID = 9999;
    private static final boolean LEITEND = false;


    @Test
    public void toVO() throws Exception {
        final KampfrichterBE kampfrichterBE = getKampfrichterBE();

        final KampfrichterDO actual = KampfrichterMapper.toKampfrichterDO.apply(kampfrichterBE);

        assertThat(actual.getUserId() == USERID);
    }


    @Test
    public void toBE() throws Exception {
        final KampfrichterDO kampfrichterDO = getKampfrichterDO();

        final KampfrichterBE actual = KampfrichterMapper.toKampfrichterBE.apply(kampfrichterDO);

        assertThat(actual.getKampfrichterUserId() == USERID);
    }
}