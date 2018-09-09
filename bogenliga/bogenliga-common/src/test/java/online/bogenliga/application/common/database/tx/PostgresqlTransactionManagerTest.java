package online.bogenliga.application.common.database.tx;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import online.bogenliga.application.common.errorhandling.exception.TechnicalException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class PostgresqlTransactionManagerTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;

    private PostgresqlTransactionManager underTest;


    @Before
    public void initUnderTest() {
        underTest = new PostgresqlTransactionManager(dataSource);
    }


    @Test
    public void isActive() {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks

        // call test method
        final boolean actual = underTest.isActive();

        // assert result
        assertThat(actual).isFalse();

        // verify invocations
    }


    @Test
    public void begin() throws SQLException {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);

        // call test method
        underTest.begin();

        // assert result
        assertThat(SessionHandler.isActive()).isTrue();

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }


    @Test
    public void begin_withError_shouldThrowException() throws SQLException {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(connection).setAutoCommit(false);

        // call test method
        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.begin());

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection, never()).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }


    @Test
    public void rollback_withSession() throws SQLException {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);

        // call test method
        underTest.begin();

        assertThat(SessionHandler.isActive()).isTrue();

        underTest.rollback();

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        verify(connection).rollback();
    }


    @Test
    public void rollback_withSession_withError_shouldThrowException() throws SQLException {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(connection).rollback();

        // call test method
        underTest.begin();

        assertThat(SessionHandler.isActive()).isTrue();

        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.rollback());

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        verify(connection).rollback();
    }


    @Test
    public void rollback_withoutSession() {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks

        // call test method
        underTest.rollback();

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
    }


    @Test
    public void commit_withSession() throws SQLException {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);

        // call test method
        underTest.begin();

        assertThat(SessionHandler.isActive()).isTrue();

        underTest.commit();

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        verify(connection).commit();
    }


    @Test
    public void commit_withSession_withError_shouldThrowException() throws SQLException {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(connection).commit();

        // call test method
        underTest.begin();

        assertThat(SessionHandler.isActive()).isTrue();

        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.commit());

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        verify(connection).commit();
    }


    @Test
    public void commit_withoutSession() {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks

        // call test method
        underTest.commit();

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
    }


    @Test
    public void release_withSession() throws SQLException {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);

        // call test method
        underTest.begin();

        assertThat(SessionHandler.isActive()).isTrue();

        underTest.release();

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        verify(connection).rollback();
        verify(connection).close();
    }


    @Test
    public void release_withSession_withError_shouldThrowException() throws SQLException {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(connection).rollback();

        // call test method
        underTest.begin();

        assertThat(SessionHandler.isActive()).isTrue();

        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.release());

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }


    @Test
    public void release_withSession_withError_shouldThrowException2() throws SQLException {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(connection).close();

        // call test method
        underTest.begin();

        assertThat(SessionHandler.isActive()).isTrue();

        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.release());

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        verify(connection).close();
    }


    @Test
    public void release_withoutSession_shouldThrowException() {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks

        // call test method
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> underTest.release())
                .withMessageContaining("empty ThreadLocal");

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
    }


    @Test
    public void getConnection() throws SQLException {
        // prepare test data

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);

        // call test method
        final Connection actual = underTest.getConnection();

        assertThat(actual).isNotNull();

        // assert result

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }


    @Test
    public void getConnection_withoutSession_shouldNotCreateNewTransaction() throws SQLException {
        // prepare test data
        SessionHandler.setIsActive(false);

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);

        // call test method
        final Connection actual = underTest.getConnection();

        assertThat(actual).isNotNull();

        // assert result

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }


    @Test
    public void getConnection_withActiveSession_shouldCreateNewTransaction() throws SQLException {
        // prepare test data
        SessionHandler.setIsActive(true);

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);

        // call test method
        final Connection actual = underTest.getConnection();

        assertThat(actual).isNotNull();

        // assert result

        // verify invocations
        verify(connection).setAutoCommit(false);
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }


    @Test
    public void testConnection() throws SQLException {
        // prepare test data
        final DatabaseMetaData databaseMetaData = mock(DatabaseMetaData.class);
        final String database = "myDB";
        final String version = "1.2.3";

        // configure mocks
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(databaseMetaData);
        when(databaseMetaData.getDatabaseProductName()).thenReturn(database);
        when(databaseMetaData.getDatabaseProductVersion()).thenReturn(version);

        // call test method
        underTest.testConnection();

        // assert result

        // verify invocations
        verify(connection).getMetaData();
    }


    @Test
    public void testConnection_withConnectionProblem_shouldThrowException() throws SQLException {
        // prepare test data
        // configure mocks
        doThrow(SQLException.class).when(dataSource).getConnection();

        // call test method

        assertThatExceptionOfType(SQLException.class)
                .isThrownBy(() -> underTest.testConnection());

        // assert result
        // verify invocations
    }
}