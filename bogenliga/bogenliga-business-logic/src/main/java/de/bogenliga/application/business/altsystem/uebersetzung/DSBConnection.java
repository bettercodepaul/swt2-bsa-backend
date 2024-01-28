package de.bogenliga.application.business.altsystem.uebersetzung;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class DSBConnection {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/swt2";
    private static final String USERNAME = "swt2";
    private static final String PASSWORD = "swt2";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Herstellen der Datenbankverbindung.", e);
        }
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Fehler beim Schließen der Datenbankverbindung.", e);
            }
        }
    }

}
