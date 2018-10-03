package de.bogenliga.application.common.configuration;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class SecurityJsonWebTokenConfigurationTest {

    @Test
    public void getSecret() {
        final SecurityJsonWebTokenConfiguration underTest = new SecurityJsonWebTokenConfiguration();
        assertThat(underTest.getSecret()).isEqualTo(SecurityJsonWebTokenConfiguration.DEFAULT_SECRET);

        final String secret = "test";
        underTest.setSecret(secret);

        assertThat(underTest.getSecret()).isEqualTo(secret);
    }


    @Test
    public void getExpiration() {
        final SecurityJsonWebTokenConfiguration underTest = new SecurityJsonWebTokenConfiguration();
        assertThat(underTest.getExpiration()).isEqualTo(SecurityJsonWebTokenConfiguration.DEFAULT_EXPIRATION_TIME);

        final long expiration = 123;
        underTest.setExpiration(expiration);

        assertThat(underTest.getExpiration()).isEqualTo(expiration);
    }


    @Test
    public void getRefresh() {
        final SecurityJsonWebTokenConfiguration underTest = new SecurityJsonWebTokenConfiguration();
        assertThat(underTest.getRefresh()).isEqualTo(SecurityJsonWebTokenConfiguration.DEFAULT_REFRESH_COUNT);

        final int refresh = 321;
        underTest.setRefresh(refresh);

        assertThat(underTest.getRefresh()).isEqualTo(refresh);
    }
}