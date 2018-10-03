package de.bogenliga.application.business.user.impl.businessactivity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class PasswordHashingBATest {

    private static final String HASH_VALUE = "2908d2c28dfc047741fc590a026ffade237ab2ba7e1266f010fe49bde548b5987a534a86655a0d17f336588e540cd66f67234b152bbb645b4bb85758a1325d64";
    private static final String PASSWORD = "password";
    private static final String SALT = "salt";
    private static final int EXPECTED_SIZE = 128;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private PasswordHashingBA underTest;


    @Test
    public void calculateHash() {
        final String actual = underTest.calculateHash(PASSWORD, SALT);

        assertThat(actual)
                .hasSize(EXPECTED_SIZE)
                .isEqualTo(HASH_VALUE);
    }


    @Test
    public void calculateHash_withDifferentSalt() {
        final String actual = underTest.calculateHash(PASSWORD, "other");

        assertThat(actual)
                .hasSize(EXPECTED_SIZE)
                .isNotEqualTo(HASH_VALUE);
    }


    @Test
    public void calculateHash_withDifferentPassword() {
        final String actual = underTest.calculateHash("other", SALT);

        assertThat(actual)
                .hasSize(EXPECTED_SIZE)
                .isNotEqualTo(HASH_VALUE);
    }


    @Test
    public void generateSalt() {
        final String actual = underTest.generateSalt();

        assertThat(actual).hasSize(EXPECTED_SIZE);

        final String other = underTest.generateSalt();

        assertThat(other)
                .hasSize(EXPECTED_SIZE)
                .isNotEqualTo(actual);

    }
}