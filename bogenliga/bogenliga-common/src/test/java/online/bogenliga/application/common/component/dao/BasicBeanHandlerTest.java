package online.bogenliga.application.common.component.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class BasicBeanHandlerTest {

    @Test
    public void handle() throws SQLException {
        // prepare test data
        final Map<String, String> columnMapping = new HashMap<>();
        final ResultSet resultSet = mock(ResultSet.class);

        // configure mocks
        when(resultSet.next()).thenReturn(false);

        // call test method
        final BasicBeanHandler<TestBE> underTest = new BasicBeanHandler<>(TestBE.class, columnMapping);

        // assert result
        final TestBE actual = underTest.handle(resultSet);

        assertThat(actual).isNull();

        // verify invocations
    }
}