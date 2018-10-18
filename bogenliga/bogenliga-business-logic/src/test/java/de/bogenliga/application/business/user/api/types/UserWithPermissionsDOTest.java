package de.bogenliga.application.business.user.api.types;

import java.time.OffsetDateTime;
import java.util.Arrays;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserWithPermissionsDOTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void assertEquals() {

        final UserWithPermissionsDO underTest = new UserWithPermissionsDO(123L, "email", OffsetDateTime.now(),
                null, OffsetDateTime.now(),
                null, null,
                Arrays.asList("permission1", "permission2"));


        assertThat(underTest.equals(underTest)).isTrue();
    }
}