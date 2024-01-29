package de.bogenliga.application.services.v1.olddbimport;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


public class OldDbImportTest {

    private static final String TEST_DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String TEST_DB_USER = "testuser";
    private static final String TEST_DB_PASSWORD = "testpassword";

    private OldDbImport oldDbImport;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);  // Initialisieren Sie die Mock-Objekte

        oldDbImport = mock(OldDbImport.class);
    }

    @AfterEach
    public void tearDown() {
        oldDbImport = null;
    }

    @Test
    public void testExecuteScript() {
        OldDbImport oldDbImport = mock(OldDbImport.class);

        String scriptFilePath = "path/to/script.sql";
        oldDbImport.executeScriptWrapper(scriptFilePath, TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
    }

    @Test
    public void testCreateTable() {
        OldDbImport oldDbImport = mock(OldDbImport.class);

        String tableName = "some_table";
        String createTableQuery = oldDbImport.createTableWrapper(tableName);
        assertNotNull(createTableQuery);
    }

    @Test
    public void testInsertTable() {
        OldDbImport oldDbImport = mock(OldDbImport.class);

        String tableName = "some_table";
        boolean[] tables = new boolean[8];
        String insertQuery = "INSERT INTO some_table VALUES (?, ?, ?)";
        String result = oldDbImport.insertTableWrapper(tableName, tables, insertQuery);
        assertNotNull(result);
    }

    @Test
    public void testSync() {
        OldDbImport oldDbImport = mock(OldDbImport.class);

        oldDbImport.sync();
    }
}
