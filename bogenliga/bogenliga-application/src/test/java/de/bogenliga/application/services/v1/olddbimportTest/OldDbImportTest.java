package de.bogenliga.application.services.v1.olddbimportTest;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.bogenliga.application.services.v1.olddbimport.OldDbImport;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OldDbImportTest {

    private OldDbImport oldDbImport;
    File tempFile = new File("tempFile.sql");


    @Before
    public void setUp() {
        oldDbImport = new OldDbImport();
    }

    @Test
    public void testInsertTable() {
        // Test the InsertTable function for a table
        boolean[] tables = new boolean[8];
        String insertQuery = "INSERT INTO test_table VALUES (?, ?, ?)";
        String result = oldDbImport.insertTable("test_table", tables, insertQuery);

        // Check if the expected INSERT statement was generated
        assertTrue(result.contains("VALUES (?, ?, ?)"));
    }
    @Test
    public void testExportTable() throws SQLException, IOException {
        // Testet die ExportTable-Funktion für eine Tabelle
        Connection connection = mock(Connection.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenReturn(mock(java.sql.ResultSetMetaData.class));
        when(connection.prepareStatement(anyString())).thenReturn(mock(java.sql.PreparedStatement.class));
        when(connection.prepareStatement(anyString()).executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        // Erstelle temporäre Datei, um die Funktion zu testen
        File tempFile = new File("temptable.sql");


        oldDbImport.exportTable(connection, "test_table");

        // Überprüfe, ob die Datei 'temptable.sql' erstellt wurde
        assertThat(tempFile.exists()).isTrue();

        tempFile.delete();
    }
    @Test
    public void testExportTableSQLException() throws SQLException, IOException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // Simuliere eine SQLException, wenn executeQuery aufgerufen wird
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Simulated SQL Exception"));

        File tempFile = new File("tempFile.sql");

        try {
            oldDbImport.exportTable(connection, "test_table");

            // Überprüfe, ob die Datei 'temptable.sql' nicht erstellt wurde
            assertFalse(tempFile.exists());
        } catch (SQLException e) {
            // Hier können zusätzliche Überprüfungen für die SQLException durchgeführt werden
        } finally {
            // Lösche die temporäre Datei, wenn sie existiert
            if (tempFile.exists() && !tempFile.delete()) {
                System.out.println("Failed to delete the 'tempFile.sql' file");
            }
        }
    }
    @Test
    public void testExportTableTableNotExists() throws SQLException, IOException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        // Simuliere, dass die Tabelle nicht existiert (ResultSet ist leer)
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        File tempFile = new File("tempFile.sql");

        try {
            oldDbImport.exportTable(connection, "non_existent_table");

            // Überprüfe, ob die Datei 'temptable.sql' nicht erstellt wurde
            assertFalse(tempFile.exists());
        } finally {
            // Lösche die temporäre Datei, wenn sie existiert
            if (tempFile.exists() && !tempFile.delete()) {
                System.out.println("Failed to delete the 'tempFile.sql' file");
            }
        }
    }
}
