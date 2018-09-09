package online.bogenliga.application.common.database.tx;

import java.sql.Connection;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class SessionHandlerTest {

    @Test
    public void getConnection_withoutConnection() {
        // prepare test data
        SessionHandler.removeConnection();

        // configure mocks
        // call test method
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(SessionHandler::getConnection);
        // assert result
        // verify invocations
    }


    @Test
    public void getConnection_withConnection() {
        // prepare test data
        SessionHandler.setConnection(mock(Connection.class));

        // configure mocks
        // call test method
        final Connection actual = SessionHandler.getConnection();

        // assert result
        assertThat(actual).isNotNull();

        // verify invocations
    }


    @Test
    public void setConnection_withoutConnection() {
        // prepare test data
        // configure mocks
        // call test method
        SessionHandler.setConnection(mock(Connection.class));

        // assert result
        // verify invocations
    }


    @Test
    public void setConnection_withConnection_shouldReplaceConnection() {
        // prepare test data
        final Connection connection = mock(Connection.class);

        // configure mocks
        // call test method
        SessionHandler.setConnection(connection);

        SessionHandler.setConnection(connection);

        // assert result
        // verify invocations
    }


    @Test
    public void removeConnection() {
        // prepare test data
        // configure mocks
        // call test method
        SessionHandler.removeConnection();

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
    }


    @Test
    public void isActive() {
        // prepare test data
        // configure mocks
        // call test method
        assertThat(SessionHandler.isActive()).isFalse();

        // assert result
        // verify invocations
    }


    @Test
    public void isActive_withIsActiveFlag() {
        // prepare test data
        SessionHandler.setIsActive(false);

        // configure mocks
        // call test method
        assertThat(SessionHandler.isActive()).isFalse();

        // assert result
        // verify invocations
    }


    @Test
    public void setIsActive() {
        // prepare test data

        // configure mocks
        // call test method
        SessionHandler.setIsActive(true);
        SessionHandler.setIsActive(false);

        // assert result
        assertThat(SessionHandler.isActive()).isFalse();

        // verify invocations
    }
}