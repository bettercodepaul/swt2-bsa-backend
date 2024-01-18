package de.bogenliga.application.business.altsystem.Uebersetzung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class Uebersetzung {

    public static void updateOrInsertUebersetzung(String kategorie, int altsystemID, int bogenligaID, String value) {

        try {
            // Datenbankverbindung herstellen
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/swt2", "swt2",
                    "swt2");

            // Überprüfen, ob der Name bereits in der Tabelle vorhanden ist
            String checkQuery = "SELECT COUNT(*) FROM altsystem_uebersetzung WHERE kategorie = ? AND altsystem_id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, kategorie);
                checkStatement.setInt(2, altsystemID);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();

                int count = resultSet.getInt(1);

                if (count > 0) {
                    // Der Name ist bereits vorhanden, also aktualisieren (UPDATE)
                    String updateQuery = "UPDATE altsystem_uebersetzung SET bogenliga_id = ? AND value = ? WHERE kategorie = ? AND altsystem_id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setInt(2, bogenligaID);
                        updateStatement.setString(3, value);
                        updateStatement.executeUpdate();
                    }
                } else {
                    // Der Name ist noch nicht vorhanden, also einfügen (INSERT)
                    String insertQuery = "INSERT INTO altsystem_uebersetzung (kategorie, altsystem_id, bogenliga_id, value ) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, kategorie);
                        insertStatement.setInt(2, altsystemID);
                        insertStatement.setInt(3, bogenligaID);
                        insertStatement.setString(4, value);
                        insertStatement.executeUpdate();
                    }
                }
            }

            // Datenbankverbindung schließen
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void findByAltsystemID(String kategorie, int altsystemID) {

        try {
            // Datenbankverbindung herstellen
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/swt2", "swt2",
                    "swt2");

            // Überprüfen, ob der Name bereits in der Tabelle vorhanden ist
            String checkQuery = "SELECT COUNT(*) FROM altsystem_uebersetzung WHERE kategorie = ? AND altsystem_id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, kategorie);
                checkStatement.setInt(2, altsystemID);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}