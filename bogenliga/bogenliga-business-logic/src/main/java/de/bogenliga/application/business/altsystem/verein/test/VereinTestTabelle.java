package de.bogenliga.application.business.altsystem.verein.test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import de.bogenliga.application.business.altsystem.BL_DBOMapper;
import de.bogenliga.application.business.altsystem.verein.dataobject.DSBVereinDO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class VereinTestTabelle {

    public static void updateOrInsertVerein(String verein) {

        try {
            // Datenbankverbindung herstellen
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/swt2", "swt2", "swt2");

            // Überprüfen, ob der Name bereits in der Tabelle vorhanden ist
            String checkQuery = "SELECT COUNT(*) FROM verein_testtabelle WHERE verein_name_test = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, verein);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();

                int count = resultSet.getInt(1);

                if (count > 0) {
                    // Der Name ist bereits vorhanden, also aktualisieren (UPDATE)
                    String updateQuery = "UPDATE verein_testtabelle SET verein_dsb_identifier_test = ? WHERE verein_name_test = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, verein);
                        updateStatement.setString(2, null);
                        updateStatement.executeUpdate();
                    }
                } else {
                    // Der Name ist noch nicht vorhanden, also einfügen (INSERT)
                    String insertQuery = "INSERT INTO verein_testtabelle (verein_name_test, verein_dsb_identifier_test) VALUES (?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, verein);
                        insertStatement.setString(2, null);
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

    public static void main(String[] args) throws SQLException {

        BL_DBOMapper dataAccessObj = new BL_DBOMapper();
        List<DSBVereinDO> mappedVerein = null;
        mappedVerein = dataAccessObj.mapVerein();
        String vereinSolo = null;
        for (DSBVereinDO verein : mappedVerein) {
            System.out.println("Verein Name: " + verein.getVerein_name());
            vereinSolo = verein.getVerein_name();
            updateOrInsertVerein(vereinSolo);
        }


    }





}