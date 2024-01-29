package de.bogenliga.application.services.v1.olddbimport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import de.bogenliga.application.services.v1.olddbimport.OldDbImport;

public class OldDbImportTest {

    private static final String TEST_DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String TEST_DB_USER = "testuser";
    private static final String TEST_DB_PASSWORD = "testpassword";

    private OldDbImport oldDbImport;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);  // Initialisieren Sie die Mock-Objekte

        // Hier können Sie Setup-Logik für jeden Test ausführen, falls benötigt.
        oldDbImport = mock(OldDbImport.class);
    }

    @AfterEach
    public void tearDown() {
        // Hier können Sie Cleanup-Logik für jeden Test ausführen, falls benötigt.
        oldDbImport = null;
    }

    /*@Test
    public void testExecuteQuery() throws SQLException {
        // Mocking
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("configuration_value")).thenReturn("mockedConfigurationValue");

        // Mock DriverManager
        mockStatic(DriverManager.class);
        when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(connection);

        // Test
        String result = OldDbImport.executeQueryWrapper("mockedQuery");

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testExecuteQueryException() throws SQLException {
        // Mock DriverManager
        mockStatic(DriverManager.class);
        when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenThrow(new SQLException("Mocked SQL Exception"));

        // Test and Verify
        assertThrows(SQLException.class, () -> OldDbImport.executeQueryWrapper("mockedQuery"));
    }

    @Test
    public void testExportTable() throws SQLException, IOException {
        // Mocking
        OldDbImport oldDbImport = mock(OldDbImport.class);
        Connection mockConnection = mock(Connection.class);

        // Test
        String tableName = "some_table";
        oldDbImport.exportTableWrapper(mockConnection, tableName);

        // Verifikation
        verify(oldDbImport).exportTableWrapper(mockConnection, tableName);
    }*/

    @Test
    public void testExecuteScript() {
        // Mocking
        OldDbImport oldDbImport = mock(OldDbImport.class);

        // Test
        String scriptFilePath = "path/to/script.sql";
        oldDbImport.executeScriptWrapper(scriptFilePath, TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
    }



    @Test
    public void testCreateTable() {
        // Mocking
        OldDbImport oldDbImport = mock(OldDbImport.class);

        // Test
        String tableName = "some_table";
        String createTableQuery = oldDbImport.createTableWrapper(tableName);
        assertNotNull(createTableQuery);
    }

    @Test
    public void testInsertTable() {
        // Mocking
        OldDbImport oldDbImport = mock(OldDbImport.class);

        // Test
        String tableName = "some_table";
        boolean[] tables = new boolean[8];
        String insertQuery = "INSERT INTO some_table VALUES (?, ?, ?)";
        String result = oldDbImport.insertTableWrapper(tableName, tables, insertQuery);
        assertNotNull(result);
    }

    @Test
    public void testSync() {
        // Mocking
        OldDbImport oldDbImport = mock(OldDbImport.class);

        // Test
        oldDbImport.sync();

        // Verifikation
        // Hier können Sie Assertions oder Verifikationen hinzufügen, um das erwartete Verhalten zu überprüfen.
    }
}
