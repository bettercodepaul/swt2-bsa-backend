package online.bogenliga.application.common.component.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import online.bogenliga.application.common.database.tx.PostgresqlTransactionManager;
import online.bogenliga.application.common.errorhandling.exception.BusinessException;
import online.bogenliga.application.common.errorhandling.exception.TechnicalException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class BasicDAOTest {
    private static final long ID = 123L;
    private static final String NAME = "foobar";

    private static final String TABLE_NAME = "table";
    private static final String TABLE_COLUMN_ID = "table_id";
    private static final String TABLE_COLUMN_NAME = "table_name";
    private static final String BE_PARAMETER_ID = "id";
    private static final String BE_PARAMETER_NAME = "name";

    private static final String SQL_QUERY_WITH_PARAMETER = "DUMMY SQL QUERY WITH PARAMETER ?;";
    private static final String SQL_QUERY = "DUMMY SQL QUERY;";
    private static final String PARAMETER = "parameter";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private QueryRunner queryRunner;
    @Mock
    private Connection connection;
    @Mock
    private PostgresqlTransactionManager transactionManager;
    @Mock
    private Logger logger;
    @InjectMocks
    private BasicDAO underTest;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;


    private static Map<String, String> getColumnToFieldMapping() {
        final Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put(TABLE_COLUMN_ID, BE_PARAMETER_ID);
        columnMapping.put(TABLE_COLUMN_NAME, BE_PARAMETER_NAME);
        return columnMapping;
    }


    private static BusinessEntityConfiguration<TestBE> createConfig(final Logger logger) {
        return new BusinessEntityConfiguration<>(TestBE.class, TABLE_NAME, getColumnToFieldMapping(), logger);
    }


    @Before
    public void initUnderTest() {
        underTest = new BasicDAO(transactionManager, queryRunner);
    }


    @Test
    public void selectSingleEntity() throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        when(queryRunner.query(
                eq(connection),
                anyString(),
                any(),
                eq(PARAMETER)))
                .thenReturn(expected);

        // call test method
        final TestBE actual = underTest.selectSingleEntity(createConfig(logger), SQL_QUERY_WITH_PARAMETER,
                PARAMETER);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());

        // verify invocations
        verify(queryRunner).query(eq(connection), eq(SQL_QUERY_WITH_PARAMETER), any(BasicBeanHandler.class),
                eq(PARAMETER));
    }


    @Test
    public void selectSingleEntity_withSQLError_shouldThrowException() throws SQLException {
        // prepare test data

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(queryRunner).query(
                eq(connection),
                anyString(),
                any(),
                eq(PARAMETER));

        // call test method
        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.selectSingleEntity(createConfig(logger), SQL_QUERY_WITH_PARAMETER,
                        PARAMETER));
        // assert result

        // verify invocations
        verify(queryRunner).query(eq(connection), eq(SQL_QUERY_WITH_PARAMETER), any(BasicBeanHandler.class),
                eq(PARAMETER));
    }


    @Test
    public void selectEntityList() throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        final List<TestBE> expectedList = Collections.singletonList(expected);

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        when(queryRunner.query(
                eq(connection),
                anyString(),
                any(),
                eq(PARAMETER)))
                .thenReturn(expectedList);

        // call test method
        final List<TestBE> actual = underTest.selectEntityList(createConfig(logger), SQL_QUERY_WITH_PARAMETER,
                PARAMETER);

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final TestBE actualBE = actual.get(0);
        assertThat(actualBE.getId()).isEqualTo(expected.getId());
        assertThat(actualBE.getName()).isEqualTo(expected.getName());

        // verify invocations
        verify(queryRunner).query(eq(connection), eq(SQL_QUERY_WITH_PARAMETER), any(BasicBeanListHandler.class),
                eq(PARAMETER));
    }


    @Test
    public void selectEntityList_withoutResult_shouldReturnEmptyList() throws SQLException {
        // prepare test data
        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        when(queryRunner.query(
                eq(connection),
                anyString(),
                any(),
                eq(PARAMETER)))
                .thenReturn(null);

        // call test method
        final List<TestBE> actual = underTest.selectEntityList(createConfig(logger), SQL_QUERY_WITH_PARAMETER,
                PARAMETER);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isEmpty();

        // verify invocations
        verify(queryRunner).query(eq(connection), eq(SQL_QUERY_WITH_PARAMETER), any(BasicBeanListHandler.class),
                eq(PARAMETER));
    }


    @Test
    public void selectEntityList_withSQLError_shouldThrowException() throws SQLException {
        // prepare test data
        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(queryRunner).query(
                eq(connection),
                anyString(),
                any(),
                eq(PARAMETER));

        // call test method
        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.selectEntityList(createConfig(logger), SQL_QUERY_WITH_PARAMETER,
                        PARAMETER));

        // assert result
        // verify invocations
        verify(queryRunner).query(eq(connection), eq(SQL_QUERY_WITH_PARAMETER), any(BasicBeanListHandler.class),
                eq(PARAMETER));
    }


    @Test
    public void insertEntity() throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        when(queryRunner.insert(
                eq(connection),
                anyString(),
                any(),
                anyString()))
                .thenReturn(expected);

        // call test method
        final TestBE actual = underTest.insertEntity(createConfig(logger), expected);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());

        // verify invocations
        verify(transactionManager).begin();
        verify(transactionManager).commit();
        verify(transactionManager).release();

        verify(queryRunner).insert(eq(connection), stringArgumentCaptor.capture(),
                any(BasicBeanHandler.class),
                eq(NAME));

        final String query = stringArgumentCaptor.getValue();

        assertThat(query)
                .contains("INSERT INTO")
                .contains(TABLE_NAME)
                .contains(TABLE_COLUMN_NAME)
                .contains("VALUES");
    }


    @Test
    public void insertEntity_withSQLError_shouldThrowException() throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(queryRunner).insert(
                eq(connection),
                anyString(),
                any(),
                anyString());

        // call test method
        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.insertEntity(createConfig(logger), expected));

        // assert result
        // verify invocations
        verify(transactionManager).begin();
        verify(transactionManager).release();
        verify(transactionManager).rollback();

        verify(queryRunner).insert(eq(connection), stringArgumentCaptor.capture(),
                any(BasicBeanHandler.class),
                eq(NAME));

        final String query = stringArgumentCaptor.getValue();

        assertThat(query)
                .contains("INSERT INTO")
                .contains(TABLE_NAME)
                .contains(TABLE_COLUMN_NAME)
                .contains("VALUES");
    }


    @Test
    public void updateEntities() throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        final int affectedRows = 1;

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        when(queryRunner.update(
                eq(connection),
                anyString(),
                any(),
                any()))
                .thenReturn(affectedRows);

        // call test method
        final int actual = underTest.updateEntities(createConfig(logger), expected, BE_PARAMETER_ID);

        // assert result
        assertThat(actual).isEqualTo(affectedRows);

        // verify invocations
        verify(transactionManager).begin();
        verify(transactionManager).commit();
        verify(transactionManager).release();

        verify(queryRunner).update(eq(connection), stringArgumentCaptor.capture(),
                eq(NAME), eq(ID));

        final String query = stringArgumentCaptor.getValue();

        assertThat(query)
                .contains("UPDATE")
                .contains(TABLE_NAME)
                .contains(TABLE_NAME)
                .contains(TABLE_COLUMN_ID)
                .contains("WHERE");
    }


    @Test
    public void updateEntities_withSQLError_shouldThrowException() throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(queryRunner).update(
                eq(connection),
                anyString(),
                any(),
                any());

        // call test method
        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.updateEntities(createConfig(logger), expected, BE_PARAMETER_ID));

        // assert result
        // verify invocations
        verify(transactionManager).begin();
        verify(transactionManager).rollback();
        verify(transactionManager).release();

        verify(queryRunner).update(eq(connection), stringArgumentCaptor.capture(),
                eq(NAME), eq(ID));

        final String query = stringArgumentCaptor.getValue();

        assertThat(query)
                .contains("UPDATE")
                .contains(TABLE_NAME)
                .contains(TABLE_NAME)
                .contains(TABLE_COLUMN_ID)
                .contains("WHERE");
    }


    @Test
    public void updateEntity() throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        final int affectedRows = 1;

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        when(queryRunner.update(
                eq(connection),
                anyString(),
                any(),
                any()))
                .thenReturn(affectedRows);
        when(queryRunner.query(
                eq(connection),
                anyString(),
                any(),
                any(Object.class)))
                .thenReturn(expected);


        // call test method
        final TestBE actual = underTest.updateEntity(createConfig(logger), expected, BE_PARAMETER_ID,
                SQL_QUERY_WITH_PARAMETER);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());

        // verify invocations
        verify(transactionManager).begin();
        verify(transactionManager).commit();
        verify(transactionManager).release();

        verify(queryRunner).query(eq(connection), eq(SQL_QUERY_WITH_PARAMETER), any(BasicBeanHandler.class),
                eq(ID));

        verify(queryRunner).update(eq(connection), stringArgumentCaptor.capture(),
                eq(NAME), eq(ID));

        final String query = stringArgumentCaptor.getValue();

        assertThat(query)
                .contains("UPDATE")
                .contains(TABLE_NAME)
                .contains(TABLE_NAME)
                .contains(TABLE_COLUMN_ID)
                .contains("WHERE");
    }


    @Test
    public void updateEntity_withSQLError_shouldThrowException() throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        final int affectedRows = 1;

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        when(queryRunner.update(
                eq(connection),
                anyString(),
                any(),
                any()))
                .thenReturn(affectedRows);
        doThrow(SQLException.class).when(queryRunner).query(
                eq(connection),
                anyString(),
                any(),
                any(Object.class));


        // call test method
        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.updateEntity(createConfig(logger), expected, BE_PARAMETER_ID,
                        SQL_QUERY_WITH_PARAMETER));

        // assert result
        // verify invocations
        verify(transactionManager).begin();
        verify(transactionManager).rollback();
        verify(transactionManager).release();

        verify(queryRunner).query(eq(connection), eq(SQL_QUERY_WITH_PARAMETER), any(BasicBeanHandler.class),
                eq(ID));

        verify(queryRunner).update(eq(connection), stringArgumentCaptor.capture(),
                eq(NAME), eq(ID));

        final String query = stringArgumentCaptor.getValue();

        assertThat(query)
                .contains("UPDATE")
                .contains(TABLE_NAME)
                .contains(TABLE_NAME)
                .contains(TABLE_COLUMN_ID)
                .contains("WHERE");
    }


    @Test
    public void updateEntity_withoutAffectedRows_shouldThrowException() throws SQLException {
        assertAffectedRowsOnUpdate(0, "does not affect any row");
    }


    @Test
    public void updateEntity_withNAffectedRows_shouldThrowException() throws SQLException {
        assertAffectedRowsOnUpdate(5, "affected 5 row");
    }


    private void assertAffectedRowsOnUpdate(final int affectedRows, final String errorMessage) throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        when(queryRunner.update(
                eq(connection),
                anyString(),
                any(),
                any()))
                .thenReturn(affectedRows);


        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updateEntity(createConfig(logger), expected, BE_PARAMETER_ID,
                        SQL_QUERY_WITH_PARAMETER))
                .withMessageContaining(errorMessage);

        // assert result
        // verify invocations
        verify(transactionManager).begin();
        verify(transactionManager).rollback();
        verify(transactionManager).release();

        verify(queryRunner).update(eq(connection), stringArgumentCaptor.capture(),
                eq(NAME), eq(ID));

        final String query = stringArgumentCaptor.getValue();

        assertThat(query)
                .contains("UPDATE")
                .contains(TABLE_NAME)
                .contains(TABLE_NAME)
                .contains(TABLE_COLUMN_ID)
                .contains("WHERE");
    }


    @Test
    public void deleteEntity() throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        final int affectedRows = 1;

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        when(queryRunner.update(
                eq(connection),
                anyString(),
                any()))
                .thenReturn(affectedRows);

        // call test method
        underTest.deleteEntity(createConfig(logger), expected, BE_PARAMETER_ID);

        // assert result

        // verify invocations
        verify(transactionManager).begin();
        verify(transactionManager).commit();
        verify(transactionManager).release();
    }


    @Test
    public void deleteEntity_withSQLError_shouldThrowException() throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(queryRunner).update(
                eq(connection),
                anyString(),
                any());

        // call test method
        assertThatExceptionOfType(TechnicalException.class)
                .isThrownBy(() -> underTest.deleteEntity(createConfig(logger), expected, BE_PARAMETER_ID));

        // assert result

        // verify invocations
        verify(transactionManager).begin();
        verify(transactionManager).rollback();
        verify(transactionManager).release();
    }


    @Test
    public void deleteEntity_withoutAffectedRow_shouldThrowException() throws SQLException {
        assertAffectedRowsOnDelete(0, "does affect 0 rows");
    }


    @Test
    public void deleteEntity_withNAffectedRow_shouldThrowException() throws SQLException {
        assertAffectedRowsOnDelete(6, "does affect 6 rows");
    }


    private void assertAffectedRowsOnDelete(final int affectedRows, final String errorMessage) throws SQLException {
        // prepare test data
        final TestBE expected = new TestBE();
        expected.setId(ID);
        expected.setName(NAME);

        // configure mocks
        when(transactionManager.getConnection()).thenReturn(connection);
        when(queryRunner.update(
                eq(connection),
                anyString(),
                any())).thenReturn(affectedRows);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.deleteEntity(createConfig(logger), expected, BE_PARAMETER_ID))
                .withMessageContaining(errorMessage);

        // assert result

        // verify invocations
        verify(transactionManager).begin();
        verify(transactionManager).rollback();
        verify(transactionManager).release();
    }


    @Test
    public void logSQL() {
        // prepare test data
        // configure mocks
        when(logger.isInfoEnabled()).thenReturn(true);

        // call test method
        final String actual = underTest.logSQL(logger, SQL_QUERY);

        // assert result
        assertThat(actual).isEqualTo(SQL_QUERY);

        // verify invocations
        verify(logger).info(SQL_QUERY);
    }


    @Test
    public void logSQL_withParameter() {
        // prepare test data
        final String expected = SQL_QUERY_WITH_PARAMETER.replace("?", PARAMETER);

        // configure mocks
        when(logger.isInfoEnabled()).thenReturn(true);

        // call test method
        final String actual = underTest.logSQL(logger, SQL_QUERY_WITH_PARAMETER, PARAMETER);

        // assert result
        assertThat(actual).isEqualTo(SQL_QUERY_WITH_PARAMETER);

        // verify invocations
        verify(logger).info(expected);
    }
}