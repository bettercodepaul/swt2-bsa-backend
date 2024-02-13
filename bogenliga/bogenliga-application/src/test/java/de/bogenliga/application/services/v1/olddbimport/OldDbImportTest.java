package de.bogenliga.application.services.v1.olddbimport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class OldDbImportTest {
    @InjectMocks
    private OldDbImport oldDbImport;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    private static String originalURL;

    @Before
    public void setUp() {
        originalURL = OldDbImport.getURL();
    }

    @After
    public void tearDown() {
        OldDbImport.setURL(originalURL);
    }
    @Test
    public void testSync() throws Exception {

        String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
        String user = "sa";
        String password = "";
        OldDbImport.executeScriptEnabled = false;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        Field urlField = OldDbImport.class.getDeclaredField("URL");
        urlField.setAccessible(true);

        String testUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        urlField.set(null, testUrl);

        File tempTestFile = File.createTempFile("testExportTable", ".sql");
        String testFilePath = tempTestFile.getAbsolutePath();

        Field tempSQLFile = OldDbImport.class.getDeclaredField("sqlfile");
        tempSQLFile.setAccessible(true);
        tempSQLFile.set(null, testFilePath);

        OldDbImport.setConnectionInfo(url, user, password);

        try (MockedStatic<DriverManager> mocked = mockStatic(DriverManager.class)) {
            mocked.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            OldDbImport.sync();
        }
        assertTrue(tempTestFile.exists());

        tempTestFile.deleteOnExit();

        OldDbImport.executeScriptEnabled = true;
    }

    @Test
    public void testExecuteQuery() throws SQLException {

        String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
        String user = "sa";
        String password = "";

        OldDbImport.setConnectionInfo(url, user, password);
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE configuration (configuration_key VARCHAR(255), configuration_value VARCHAR(255))");
            statement.execute("INSERT INTO configuration (configuration_key, configuration_value) VALUES ('key', 'expectedResult')");

            String query = "SELECT configuration_value FROM configuration WHERE configuration_key = 'key'";

            String result = OldDbImport.executeQueryWrapper(query);
            assertEquals("expectedResult", result);
        }
    }
    @Test
    public void testExportTable() throws Exception {

        File tempTestFile = File.createTempFile("testExportTable", ".sql");
        String testFilePath = tempTestFile.getAbsolutePath();

        Field sqlfileField = OldDbImport.class.getDeclaredField("sqlfile");
        sqlfileField.setAccessible(true);
        sqlfileField.set(null, testFilePath);

        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb");
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE test_table (id INT, column_value VARCHAR(255))");
        statement.execute("INSERT INTO test_table (id, column_value) VALUES (1, 'testValue')");
        statement.execute("CREATE TABLE altsystem_test_table (id INT, column_value VARCHAR(255))");

        String generatedSql = OldDbImport.exportTableWrapper(connection, "test_table");

        System.out.println(testFilePath);
        System.out.println(generatedSql);

        assertTrue(generatedSql.contains("INSERT INTO altsystem_test_table VALUES"));

        connection.close();
        Files.deleteIfExists(Paths.get(testFilePath));
        System.out.println("deleted");
    }

    @Test
    public void testCreateTable() {

        String tableName = "some_table";
        String createTableQuery = OldDbImport.createTableWrapper(tableName);
        assertThat(createTableQuery)
                .isNotEmpty()
                .contains("altsystem");

    }

    @Test
    public void testInsertTable() {

        String tableName = "some_table";
        boolean[] tables = new boolean[8];
        String insertQuery = "INSERT INTO some_table VALUES (?, ?, ?)";
        String result = OldDbImport.insertTableWrapper(tableName, tables, insertQuery);
        assertNotNull(result);
    }

    @Test
    public void executeScriptTest() {

        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp_script", ".sql");
            FileWriter writer = new FileWriter(tempFile);
            writer.write("CREATE TABLE test_table (id INT);");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(tempFile);

        String tempFilePath = tempFile.getAbsolutePath();

        OldDbImport.executeScriptWrapper(tempFilePath, "jdbc:h2:mem:testdb", "testuser", "testpassword");
    }


}

