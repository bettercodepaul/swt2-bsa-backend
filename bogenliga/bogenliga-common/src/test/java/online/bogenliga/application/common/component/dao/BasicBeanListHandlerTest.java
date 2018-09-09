package online.bogenliga.application.common.component.dao;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class BasicBeanListHandlerTest {

    @Test
    public void test() {
        // prepare test data
        final Map<String, String> columnMapping = new HashMap<>();

        // configure mocks

        // call test method
        final BasicBeanListHandler<TestBE> underTest = new BasicBeanListHandler<>(TestBE.class, columnMapping);

        // assert result
        // verify invocations
    }
}