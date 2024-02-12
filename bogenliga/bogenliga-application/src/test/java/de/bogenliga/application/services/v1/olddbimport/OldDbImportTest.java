package de.bogenliga.application.services.v1.olddbimport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Test;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


public class OldDbImportTest {

    @Mock
    private OldDbImport oldDbImport;

    @Test
    public void testCreateTable() {

        String tableName = "some_table";
        String createTableQuery = oldDbImport.createTableWrapper(tableName);
        assertThat(createTableQuery)
                .isNotEmpty()
                .contains("altsystem");

    }

    @Test
    public void testInsertTable() {

        String tableName = "some_table";
        boolean[] tables = new boolean[8];
        String insertQuery = "INSERT INTO some_table VALUES (?, ?, ?)";
        String result = oldDbImport.insertTableWrapper(tableName, tables, insertQuery);
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

    @Test
    public void testExportTable() throws SQLException, IOException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb");
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE test_table (id INT, value VARCHAR(255))");
        statement.execute("CREATE TABLE ALTSYSTEM_test_table (id INT, value VARCHAR(255))");
        statement.execute("INSERT INTO test_table (id, value) VALUES (1, 'testValue')");

        String generatedSql = oldDbImport.exportTableWrapper(connection, "test_table");

        System.out.println("Generated SQL: " + generatedSql);
        assertTrue(generatedSql.contains("INSERT INTO altsystem_test_table VALUES (?, ?);"));
    }

    @Test
    public void testSync() {
        try {
            oldDbImport.sync();
        } catch (Exception e) {
            fail("sync method threw an exception: " + e.getMessage());
        }
    }
}

