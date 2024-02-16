package de.bogenliga.application.services.v1.olddbimport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class OldDbImportTest {

    @InjectMocks
    private OldDbImport oldDbImport;

    private static String originalURL;

    private Connection connection;

    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String TEST_DB_USER = "sa";
    private static final String TEST_DB_PASSWORD = "";

    Map<String, String> tableDefinitions = OldDbImport.getTableDefinitions();

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {

        originalURL = OldDbImport.getURL();

        Class.forName("org.h2.Driver");

        connection = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE CONFIGURATION (configuration_key VARCHAR(255), configuration_value VARCHAR(255))");
            statement.execute(
                    "INSERT INTO CONFIGURATION (configuration_key, configuration_value) VALUES ('Benutzer', 'sa'), ('Host', 'localhost'), ('Passwort', ''), ('Port', '5432'), ('Name', 'test')");
            for (Map.Entry<String, String> entry : tableDefinitions.entrySet()) {
                String tableName = entry.getKey();
                String createTableSql = entry.getValue();
                statement.execute(createTableSql);
                System.out.println("Tabelle " + tableName + " wurde erfolgreich erstellt.");
            }
        }
    }

    @After
    public void tearDown() throws SQLException {

        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP ALL OBJECTS DELETE FILES");
        }
        if (connection != null) {
            connection.close(); // Verbindung schließen
        }
        OldDbImport.setURL(originalURL);
    }


    @Test
    public void testExecuteQuery() throws SQLException {


        OldDbImport.setConnectionInfo(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
        Statement statement = connection.createStatement();

        statement.execute("INSERT INTO configuration (configuration_key, configuration_value) VALUES ('key', 'expectedResult')");

        String query = "SELECT configuration_value FROM configuration WHERE configuration_key = 'key'";

        String result = OldDbImport.executeQueryWrapper(query);
        assertEquals("expectedResult", result);

    }
    @Test
    public void testExportTable() throws Exception {

        File tempTestFile = File.createTempFile("testExportTable", ".sql");
        String testFilePath = tempTestFile.getAbsolutePath();

        Field sqlfileField = OldDbImport.class.getDeclaredField("sqlfile");
        sqlfileField.setAccessible(true);
        sqlfileField.set(null, testFilePath);

        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE test_table (id INT, column_value VARCHAR(255))");
        statement.execute("INSERT INTO test_table (id, column_value) VALUES (1, 'testValue')");
        statement.execute("CREATE TABLE altsystem_test_table (id INT, column_value VARCHAR(255))");

        String generatedSql = OldDbImport.exportTableWrapper(connection, "test_table");

        System.out.println(testFilePath);
        System.out.println(generatedSql);

        assertTrue(generatedSql.contains("INSERT INTO altsystem_test_table VALUES"));

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

        OldDbImport.executeScriptWrapper(tempFilePath, TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
    }
}

