package de.bogenliga.application.services.v1.olddbimport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OldDbImportTest {

    private static final String TEST_DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String TEST_DB_USER = "testuser";
    private static final String TEST_DB_PASSWORD = "testpassword";

    private OldDbImport oldDbImport;

    @BeforeEach
    public void setUp() {
        oldDbImport = new OldDbImport();
    }

    @AfterEach
    public void tearDown() {
        oldDbImport = null;
    }

    @Test
    public void testExecuteScript() {
        String scriptFilePath = "path/to/script.sql";
        oldDbImport.executeScriptWrapper(scriptFilePath, TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
    }

    @Test
    public void testCreateTable() {
        String tableName = "some_table";
        String createTableQuery = oldDbImport.createTableWrapper(tableName);
        assertNotNull(createTableQuery);
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
    public void testSync() {
        oldDbImport.sync();
    }
}
