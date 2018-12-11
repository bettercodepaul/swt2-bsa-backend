package de.bogenliga.application.business.user.impl.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserBETest {

    private static final long ID = 9999;
    private static final String EMAIL = "funktioniert@irgendwie.net";
    private static final long USER = 0;


    @Test
    public void assertToString() {
        final UserBE underTest = new UserBE();
        underTest.setUserId(ID);
        underTest.setUserEmail(EMAIL);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(EMAIL);
    }


    @Test
    public void assertToString_withoutEmail() {
        final UserBE underTest = new UserBE();
        underTest.setUserId(ID);
        underTest.setUserEmail(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains("null");
    }
}