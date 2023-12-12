package de.bogenliga.test;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.bogenliga.Main;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {

    private Main main;


    @Before
    public void setUp() {
        main = new Main();
    }


    @Test
    public void testInsertTable() {
        // Testet die InsertTable-Funktion für eine Tabelle
        boolean[] tables = new boolean[8];
        String insertQuery = "INSERT INTO test_table VALUES (?, ?, ?)";
        String result = main.insertTable("test_table", tables, insertQuery);

        // Überprüfe, ob das erwartete INSERT-Statement generiert wurde
        //assertEquals(true, result.contains("CREATE TEMPORARY TABLE test_table"));
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

        // Erstelle temporäre Datei, um die Funktion zu testen
        File tempFile = new File("temptable.sql");
        if (tempFile.exists()) {
            tempFile.delete();
        }

        main.exportTable(connection, "test_table");

        // Überprüfe, ob die Datei 'temptable.sql' erstellt wurde
        assertThat(tempFile.exists()).isTrue();

        // Lösche die temporäre Datei nach dem Test
        tempFile.delete();
    }


    @Test
    public void testMainMethod() throws SQLException, IOException, ClassNotFoundException {
        // Testet die main-Methode
        Class<?> driverManagerClass = Class.forName("com.mysql.cj.jdbc.Driver");
        File tempFile = new File("temptable.sql");
        if (tempFile.exists()) {
            tempFile.delete();
        }
        // Erstelle temporäre Datei, um die Funktion zu testen
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://mysql2f19.netcup.net:3306/k74918_bogenliga", "k74918_bsapp_ro", "BsApp@100%")) {
            Main.exportTable(connection, "acl");

            // Überprüfe, ob die Datei 'temptable.sql' erstellt wurde
            assertTrue(tempFile.exists());

            // Lösche die temporäre Datei nach dem Test
            tempFile.delete();
        }
    }
}
