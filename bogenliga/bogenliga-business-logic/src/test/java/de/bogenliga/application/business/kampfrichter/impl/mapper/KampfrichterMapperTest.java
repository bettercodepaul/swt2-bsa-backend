package de.bogenliga.application.business.kampfrichter.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterExtendedBE;
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

    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final String EMAIL = "Sorscha.Kratikoff@test.de";

    private KampfrichterDO getKampfrichterDO(){
        KampfrichterDO kampfrichterDO = new KampfrichterDO(USERID,VORNAME,NACHNAME,EMAIL,WETTKAMPFID,LEITEND);
        return kampfrichterDO;
    }
    private KampfrichterExtendedBE getKampfrichterExtendedBE() {
        KampfrichterExtendedBE kampfrichterExtendedBE = new KampfrichterExtendedBE(USERID, WETTKAMPFID, LEITEND,
                VORNAME, NACHNAME, EMAIL);
        return kampfrichterExtendedBE;
    }

    @Test
    public void toKampfrichterDOExtendedTest(){
        KampfrichterExtendedBE kampfrichterExtendedBE= getKampfrichterExtendedBE();
        final KampfrichterDO actual = KampfrichterMapper.toKampfrichterDOExtended.apply(kampfrichterExtendedBE);
        assertThat(actual.getUserId()).isEqualTo(USERID);
        assertThat(actual.getKampfrichterEmail()).isEqualTo(EMAIL);
        assertThat(actual.getVorname()).isEqualTo(VORNAME);
        assertThat(actual.getNachname()).isEqualTo(NACHNAME);
        assertThat(actual.getWettkampfId()).isEqualTo(WETTKAMPFID);
        assertThat(actual.isLeitend()).isEqualTo(LEITEND);
    }

    @Test
    public void toKampfrichterExtendedBETest(){
        KampfrichterDO kampfrichterDO = getKampfrichterDO();
        final KampfrichterExtendedBE actual = KampfrichterMapper.toKampfrichterExtendedBE.apply(kampfrichterDO);
        assertThat(actual.getKampfrichterExtendedUserID()).isEqualTo(USERID);
        assertThat(actual.getKampfrichterExtendedEmail()).isEqualTo(EMAIL);
        assertThat(actual.getKampfrichterExtendedVorname()).isEqualTo(VORNAME);
        assertThat(actual.getKampfrichterExtendedNachname()).isEqualTo(NACHNAME);
        assertThat(actual.getKampfrichterExtendedWettkampfID()).isEqualTo(WETTKAMPFID);
        assertThat(actual.getKampfrichterExtendedLeitend()).isEqualTo(LEITEND);
    }

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