package de.bogenliga.application.business.kampfrichter.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import static de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest.getDsbMitgliedBE;
import static de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest.getDsbMitgliedDO;
import static de.bogenliga.application.business.kampfrichter.impl.business.KampfrichterComponentImplTest.getKampfrichterBE;
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
        final DsbMitgliedDO dsbMitgliedDO = getDsbMitgliedDO();


        final KampfrichterBE actual = KampfrichterMapper.toKampfrichterBE.apply(dsbMitgliedDO);

        assertThat(actual.getKampfrichterUserId() == USERID);
    }
}