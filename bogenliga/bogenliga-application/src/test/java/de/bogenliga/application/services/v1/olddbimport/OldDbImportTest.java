package de.bogenliga.application.services.v1.olddbimport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest(DriverManager.class)
public class OldDbImportTest {

    @InjectMocks
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
    public void testExportTable() throws Exception {
        File tempFile = File.createTempFile("exportTableTempFile", ".sql");

        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb");
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE test_table (id INT, column_value VARCHAR(255))");
        statement.execute("CREATE TABLE ALTSYSTEM_test_table (id INT, column_value VARCHAR(255))");
        statement.execute("INSERT INTO test_table (id, column_value) VALUES (1, 'testValue')");

        FileWriter fileWriterMock = mock(FileWriter.class);

        whenNew(FileWriter.class).withAnyArguments().thenReturn(fileWriterMock);

        String generatedSql = oldDbImport.exportTableWrapper(connection, "test_table");

        String expectedSql = "INSERT INTO altsystem_test_table VALUES (?, ?)";
        System.out.println("Expected SQL: " + expectedSql);
        System.out.println("Generated SQL: " + generatedSql);

        assertTrue(generatedSql.contains(expectedSql));

        try {
            Files.deleteIfExists(tempFile.toPath());
            System.out.println("Temporäre Datei gelöscht: " + tempFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*@Test
    public void testSync() {
        try {
            PowerMockito.mockStatic(Class.class);

            Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");

            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE configuration (configuration_value VARCHAR(255))");
                statement.execute("INSERT INTO configuration (configuration_value) VALUES ('testValue')");
            }
            when(Class.forName("com.mysql.cj.jdbc.Driver")).thenReturn((Class) Driver.class);


            when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(connection);

            oldDbImport.sync();
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM configuration")) {

                assertTrue(resultSet.next());
                assertEquals("testValue", resultSet.getString("configuration_value"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("sync method threw an exception: " + e.getMessage());
        }
    }*/
}

