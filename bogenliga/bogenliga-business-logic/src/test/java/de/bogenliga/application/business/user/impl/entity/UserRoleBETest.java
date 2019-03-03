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
public class UserRoleBETest {

    private static final long ID = 9999;
    private static final long ROLE_ID = 777;


    @Test
    public void assertToString() {
        final UserRoleBE underTest = new UserRoleBE();
        underTest.setUserId(ID);
        underTest.setRoleId(ROLE_ID);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(Long.toString(ROLE_ID));
    }

}