package de.bogenliga;

/**

 TODO [AL] class documentation*
 @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    // Verbindungsinformationen
    private static String host = "mysql2f19.netcup.net";
    private static int port = 3306;
    private static String dbName = "k74918_bogenliga";
    private static String user = "k74918_bsapp_ro";
    private static String password = "BsApp@100%";

    // JDBC-URL
    private static final String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

    private static String[] tableNames = {"acl", "banner", "banner-stat", "ergebniss", "liga", "mannschaft", "saison", "schuetze", "sites", "users", "wettkampfdaten"};


    public static void main(String[] args) {
        try {
            // JDBC Treiber laden
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connection herstellen
            try (Connection connection = DriverManager.getConnection(url, user, password)) {

                for (int i = 0; i < tableNames.length; i++) {
                    System.out.println(tableNames[i]);
                    exportTable(connection, tableNames[i]);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    private static void exportTable(Connection connection, String tableName) throws SQLException, IOException {
        // SQL Abfrage mit Backticks um den Tabellennamen
        String sql = "SELECT * FROM `" + tableName + "`";

        // Statement erstellen
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Ergebnisse speichern
            while (resultSet.next()) {
                //Daten in die temporäre Tabelle einfügen
                String insertSql = "INSERT INTO BL_" + tableName + " VALUES (";
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    insertSql += "?, ";
                }
                insertSql = insertSql.substring(0, insertSql.length() - 2) + ");\r\n"; //letztes Komma entfernen
                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {

                        insertStatement.setObject(i, resultSet.getObject(i)); //Wert aus der ResultSet übernehmen
                    }
                    String insertQuery = insertStatement.toString();
                    insertQuery = insertQuery.replace("com.mysql.cj.jdbc.ClientPreparedStatement: ",
                            ""); // Entferne den spezifischen String
                    // FileWriter in try-with-resources verwenden
                    try (FileWriter fw = new FileWriter("temptable.sql", true)) { // true für den Anhängemodus
                        fw.write(insertQuery);
                    } // FileWriter wird hier automatisch geschlossen
                }
            }
        }
    }
}


