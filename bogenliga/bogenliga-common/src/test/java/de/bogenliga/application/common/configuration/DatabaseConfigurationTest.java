package de.bogenliga.application.common.configuration;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class DatabaseConfigurationTest {


    private static final String HOST = "host";
    private static final int PORT = 42;
    private static final String DATABASE_NAME = "db";
    private static final String USER = "user";
    private static final String PASSWORD = "password";


    @Test
    public void asert() {
        final DatabaseConfiguration underTest = new DatabaseConfiguration();
        assertThat(underTest.getHost()).isNull();
        assertThat(underTest.getPort()).isZero();
        assertThat(underTest.getDatabaseName()).isNull();
        assertThat(underTest.getUser()).isNull();
        assertThat(underTest.getPassword()).isNull();

        underTest.setHost(HOST);
        underTest.setPort(PORT);
        underTest.setDatabaseName(DATABASE_NAME);
        underTest.setUser(USER);
        underTest.setPassword(PASSWORD);

        assertThat(underTest.getHost()).isEqualTo(HOST);
        assertThat(underTest.getPort()).isEqualTo(PORT);
        assertThat(underTest.getDatabaseName()).isEqualTo(DATABASE_NAME);
        assertThat(underTest.getUser()).isEqualTo(USER);
        assertThat(underTest.getPassword()).isEqualTo(PASSWORD);
    }

}