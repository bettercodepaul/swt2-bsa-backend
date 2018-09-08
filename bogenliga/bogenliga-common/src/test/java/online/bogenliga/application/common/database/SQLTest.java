package online.bogenliga.application.common.database;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class SQLTest {

    private static final long ID = 123L;
    private static final String NAME = "value";
    private static final boolean ACTIVE = true;
    private static final Boolean READY = true;
    private static final int NUMBER = 5;
    private static final TestEnum STATE = TestEnum.TEST;

    private static final String TABLE_NAME = "test_table";
    private static final String TABLE_FIELD_ID = "pk";
    private static final String TABLE_FIELD_NAME = "name";
    private static final String TABLE_FIELD_ACTIVE = "is_active";
    private static final String TABLE_FIELD_READY = "ready";
    private static final String TABLE_FIELD_NUMBER = "quantity";
    private static final String TABLE_FIELD_STATE = "entity_state";

    private static final TestBE INPUT = createBE();

    private static final Map<String, String> FIELD_MAPPING = createFieldMapping();


    private static TestBE createBE() {
        final TestBE input = new TestBE();
        input.setId(ID);
        input.setName(NAME);
        input.setActive(ACTIVE);
        input.setReady(READY);
        input.setNumber(NUMBER);
        input.setState(STATE);
        return input;
    }


    private static Map<String, String> createFieldMapping() {
        final Map<String, String> fieldMapping = new HashMap<>();
        fieldMapping.put(TABLE_FIELD_ID, "id");
        fieldMapping.put(TABLE_FIELD_NAME, "name");
        fieldMapping.put(TABLE_FIELD_ACTIVE, "active");
        fieldMapping.put(TABLE_FIELD_READY, "ready");
        fieldMapping.put(TABLE_FIELD_NUMBER, "number");
        fieldMapping.put(TABLE_FIELD_STATE, "state");
        return fieldMapping;
    }


    @Test
    public void insertSQL_withIdParameter_shouldIgnorePrimaryKey() {
        // prepare test data
        // configure mocks
        // call test method
        final SQL.SQLWithParameter actual = SQL.insertSQL(INPUT);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getSql())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("INSERT INTO TestBE (name, active, ready, number, state) VALUES (?, ?, ?, ?, ?);");
        assertThat(actual.getParameter())
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .contains(NAME, ACTIVE, READY, NUMBER, STATE.name(), READY);

        // verify invocations
    }


    @Test
    public void insertSQL_withCustomFieldMapping() {
        // prepare test data
        // configure mocks
        // call test method
        final SQL.SQLWithParameter actual = SQL.insertSQL(INPUT, TABLE_NAME, FIELD_MAPPING);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getSql())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(
                        String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?);",
                                TABLE_NAME, TABLE_FIELD_NAME, TABLE_FIELD_ACTIVE, TABLE_FIELD_READY, TABLE_FIELD_NUMBER,
                                TABLE_FIELD_STATE));
        assertThat(actual.getParameter())
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .contains(NAME, ACTIVE, READY, NUMBER, STATE.name());

        // verify invocations
    }


    @Test
    public void updateSQL() {
        // prepare test data
        // configure mocks
        // call test method
        final SQL.SQLWithParameter actual = SQL.updateSQL(INPUT);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getSql())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("UPDATE TestBE SET name=?, active=?, ready=?, number=?, state=? WHERE id = ?;");
        assertThat(actual.getParameter())
                .isNotNull()
                .isNotEmpty()
                .hasSize(6)
                .contains(NAME, ACTIVE, READY, NUMBER, STATE.name(), ID);

        // verify invocations
    }


    @Test
    public void updateSQL_withCustomFieldMapping() {
        // prepare test data
        // configure mocks
        // call test method
        // define custom WHERE selector TABLE_FIELD_STATE
        final SQL.SQLWithParameter actual = SQL.updateSQL(INPUT, TABLE_NAME, TABLE_FIELD_STATE, FIELD_MAPPING);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getSql())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(
                        String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s = ?;",
                                TABLE_NAME, TABLE_FIELD_NAME, TABLE_FIELD_ACTIVE, TABLE_FIELD_READY, TABLE_FIELD_NUMBER,
                                TABLE_FIELD_STATE, TABLE_FIELD_STATE));
        assertThat(actual.getParameter())
                .isNotNull()
                .isNotEmpty()
                .hasSize(6)
                .contains(NAME, ACTIVE, READY, NUMBER, STATE.name(), STATE.name());

        // verify invocations
    }


    @Test
    public void updateSQL_withCustomFieldMapping_withIdFieldSelector() {
        // prepare test data
        // configure mocks
        // call test method
        // define custom WHERE selector TABLE_FIELD_STATE
        final SQL.SQLWithParameter actual = SQL.updateSQL(INPUT, TABLE_NAME, "id", FIELD_MAPPING);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getSql())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(
                        String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s = ?;",
                                TABLE_NAME, TABLE_FIELD_NAME, TABLE_FIELD_ACTIVE, TABLE_FIELD_READY, TABLE_FIELD_NUMBER,
                                TABLE_FIELD_STATE, TABLE_FIELD_ID));
        assertThat(actual.getParameter())
                .isNotNull()
                .isNotEmpty()
                .hasSize(6)
                .contains(NAME, ACTIVE, READY, NUMBER, STATE.name(), ID);

        // verify invocations
    }


    @Test
    public void updateSQL_withCustomFieldMapping_withOtherFieldSelector() {
        // prepare test data
        // configure mocks
        // call test method
        // define custom WHERE selector TABLE_FIELD_STATE
        final SQL.SQLWithParameter actual = SQL.updateSQL(INPUT, TABLE_NAME, "name", FIELD_MAPPING);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getSql())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(
                        String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s = ?;",
                                TABLE_NAME, TABLE_FIELD_ACTIVE, TABLE_FIELD_READY, TABLE_FIELD_NUMBER,
                                TABLE_FIELD_STATE, TABLE_FIELD_NAME));
        assertThat(actual.getParameter())
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .contains(ACTIVE, READY, NUMBER, STATE.name(), NAME);

        // verify invocations
    }


    @Test
    public void deleteSQL() {
        // prepare test data
        // configure mocks
        // call test method
        final SQL.SQLWithParameter actual = SQL.deleteSQL(INPUT);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getSql())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("DELETE FROM TestBE WHERE id = ?;");
        assertThat(actual.getParameter())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(ID);

        // verify invocations
    }


    @Test
    public void deleteSQL_withCustomFieldMapping() {
        // prepare test data
        // configure mocks
        // call test method
        // define custom WHERE selector TABLE_FIELD_STATE
        final SQL.SQLWithParameter actual = SQL.deleteSQL(INPUT, TABLE_NAME, "state", FIELD_MAPPING);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getSql())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(
                        String.format("DELETE FROM %s WHERE %s = ?;",
                                TABLE_NAME, TABLE_FIELD_STATE));
        assertThat(actual.getParameter())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(STATE.name());

        // verify invocations
    }


    @Test
    public void deleteSQL_withCustomFieldMapping_withInteger() {
        // prepare test data
        // configure mocks
        // call test method
        // define custom WHERE selector TABLE_FIELD_STATE
        final SQL.SQLWithParameter actual = SQL.deleteSQL(INPUT, TABLE_NAME, "number", FIELD_MAPPING);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getSql())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(
                        String.format("DELETE FROM %s WHERE %s = ?;",
                                TABLE_NAME, TABLE_FIELD_NUMBER));
        assertThat(actual.getParameter())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(NUMBER);

        // verify invocations
    }


    @Test
    public void deleteSQL_withCustomFieldMapping_withIdFieldSelector() {
        // prepare test data
        // configure mocks
        // call test method
        // define custom WHERE selector "id"
        final SQL.SQLWithParameter actual = SQL.deleteSQL(INPUT, TABLE_NAME, "id", FIELD_MAPPING);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getSql())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(
                        String.format("DELETE FROM %s WHERE %s = ?;",
                                TABLE_NAME, TABLE_FIELD_ID));
        assertThat(actual.getParameter())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(ID);

        // verify invocations
    }
}