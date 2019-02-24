package de.bogenliga.application.business.role.impl.entity;

import de.bogenliga.application.business.role.impl.entity.RoleBE;
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
public class RoleBETest {

    private static final long ID = 3;
    private static final String ROLENAME = "USER";


    @Test
    public void assertToString() {
        final RoleBE underTest = new RoleBE();
        underTest.setRoleId(ID);
        underTest.setRoleName(ROLENAME);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(ROLENAME);
    }

}