package de.bogenliga.application.business.user.api.types;

import java.time.OffsetDateTime;
import java.util.Arrays;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserWithPermissionsDOTest {


    public static final OffsetDateTime NOW = OffsetDateTime.now();


    @Test
    public void assertEquals() {

        final UserWithPermissionsDO underTest = new UserWithPermissionsDO(123L, "email", NOW,
                null, NOW,
                null, null,
                Arrays.asList("permission1", "permission2"));


        assertThat(underTest.equals(underTest)).isTrue();

        final UserWithPermissionsDO underTest2 = new UserWithPermissionsDO(123L, "email", NOW,
                null, NOW,
                null, null,
                Arrays.asList("permission1", "permission2"));


        assertThat(underTest.equals(underTest2)).isTrue();
    }
}