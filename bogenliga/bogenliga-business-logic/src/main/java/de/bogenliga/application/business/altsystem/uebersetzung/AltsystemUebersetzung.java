package de.bogenliga.application.business.altsystem.uebersetzung;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

@Component
public class AltsystemUebersetzung {

    public void updateOrInsertUebersetzung(AltsystemUebersetzungKategorie kategorie, long altsystemID, long bogenligaID, String wert) {
        String kategorieLabel = kategorie.label;
        Connection connection = null;
        try {
            // Datenbankverbindung herstellen
            connection = DSBConnection.connect();

            // Überprüfen, ob der Name bereits in der Tabelle vorhanden ist
            String checkQuery = "SELECT COUNT(*) FROM altsystem_uebersetzung WHERE kategorie = ? AND altsystem_id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, kategorieLabel);
                checkStatement.setLong(2, altsystemID);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();

                int count = resultSet.getInt(1);

                if (count > 0) {
                    // Der Name ist bereits vorhanden, also aktualisieren (UPDATE)
                    String updateQuery = "UPDATE altsystem_uebersetzung SET bogenliga_id = ? AND wert = ? WHERE kategorie = ? AND altsystem_id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setLong(1, bogenligaID);
                        updateStatement.setString(2, wert);
                        updateStatement.executeUpdate();
                    }
                } else {
                    // Der Name ist noch nicht vorhanden, also einfügen (INSERT)
                    String insertQuery = "INSERT INTO altsystem_uebersetzung (kategorie, altsystem_id, bogenliga_id, wert ) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, kategorieLabel);
                        insertStatement.setLong(2, altsystemID);
                        insertStatement.setLong(3, bogenligaID);
                        insertStatement.setString(4, wert);
                        insertStatement.executeUpdate();
                    }
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DSBConnection.close(connection);
        }
    }


    public AltsystemUebersetzungDO findByAltsystemID(AltsystemUebersetzungKategorie kategorie, Long altsystemID) {
        String kategorieLabel = kategorie.label;
        Connection connection = null;
        try {
            // Datenbankverbindung herstellen
            connection = DSBConnection.connect();

            // Überprüfen, ob der Datensatz bereits in der Tabelle vorhanden ist
            String checkQuery = "SELECT * FROM altsystem_uebersetzung WHERE kategorie = ? AND altsystem_id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, kategorie.label);
                checkStatement.setLong(2, altsystemID);

                ResultSet resultSet = checkStatement.executeQuery();

                // Prüfen, ob ein Datensatz gefunden wurde
                if (resultSet.next()) {

                    AltsystemUebersetzungDO dataObject = new AltsystemUebersetzungDO();

                    dataObject.setUebersetzung_id(resultSet.getInt("uebersetzungs_id"));
                    dataObject.setKategorie(resultSet.getString("kategorie"));
                    dataObject.setAltsystem_id(resultSet.getLong("altsystem_id"));
                    dataObject.setBogenliga_id(resultSet.getLong("bogenliga_id"));
                    dataObject.setValue(resultSet.getString("wert"));

                    // Gib das Datenobjekt zurück
                    return dataObject;
                } else {

                    return null;
                }


            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DSBConnection.close(connection);
        }
    }
}