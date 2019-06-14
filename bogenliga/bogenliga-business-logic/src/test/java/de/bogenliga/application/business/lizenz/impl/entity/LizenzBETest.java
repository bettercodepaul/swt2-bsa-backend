package de.bogenliga.application.business.lizenz.impl.entity;

import org.junit.Test;
import de.bogenliga.application.business.dsbmitglied.impl.business.DsbMitgliedComponentImplTest;
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
public class LizenzBETest {

    private long LIZENZID = 0;
    private String LIZENZNUMMER = "WT1234567";
    private long LIZENZREGIONID = 1;
    private long LIZENZDSBMITGLIEDID = 71;
    private String LIZENZTYP = "Liga";
    private long LIZENZDISZIPLINID = 0;
    private DsbMitgliedComponentImplTest dsbMitgliedComponentImplTest = new DsbMitgliedComponentImplTest();

    @Test
    public void assertToLong() {
        final LizenzBE underTest = dsbMitgliedComponentImplTest.getLizenzBE();
        underTest.setLizenzId((long)0);
        underTest.setLizenznummer("WT1234567");
        underTest.setLizenzRegionId((long)1);
        underTest.setLizenzDsbMitgliedId(LIZENZDSBMITGLIEDID);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(LIZENZID))
                .contains(LIZENZNUMMER)
                .contains(Long.toString(LIZENZREGIONID));
    }


    @Test
    public void assertToString_withLizenzDisziplinId_0() {
        final LizenzBE underTest = new LizenzBE();
        underTest.setLizenzDisziplinId((long)0);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains("0");
    }
}