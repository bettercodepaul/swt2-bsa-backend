package de.bogenliga.application.services.v1.olddbimport;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import de.bogenliga.application.business.altsystem.sync.OldDbImport;
import org.junit.After;
import org.mockito.InjectMocks;


public class OldDbImportTest {

    @InjectMocks
    private OldDbImport oldDbImport;

    private static String originalURL;

    private Connection connection;

    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String TEST_DB_USER = "sa";
    private static final String TEST_DB_PASSWORD = "";

    @After
    public void tearDown() throws SQLException {

        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP ALL OBJECTS DELETE FILES");
        }
        if (connection != null) {
            connection.close(); // Verbindung schließen
        }
        oldDbImport.setURL(originalURL);
    }
}

