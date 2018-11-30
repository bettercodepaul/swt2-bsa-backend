package de.bogenliga.application.business.kampfrichter.impl.entity;

import org.junit.Test;
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
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class KampfrichterBETest {

    private static final long USER = 0;

    private static final long USERID = 1337;
    private static final long WETTKAMPFID = 9999;
    private static final boolean LEITEND = false;


    @Test
    public void assertToLong() {
        final KampfrichterBE underTest = getKampfrichterBE();
        underTest.setKampfrichterUserId(USERID);
        underTest.setKampfrichterWettkampfId(WETTKAMPFID);
        underTest.setKampfrichterLeitend(LEITEND);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(USERID))
                .contains(WETTKAMPFID+"");
    }


    @Test
    public void assertToString_withWettkampfId_0() {
        final KampfrichterBE underTest = new KampfrichterBE();
        underTest.setKampfrichterUserId(USERID);
        underTest.setKampfrichterWettkampfId((long) 0);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(USERID))
                .contains("0");
    }
}