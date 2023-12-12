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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;


public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    // Verbindungsinformationen
    //private static String host = System.getenv("DB_HOST");;
    private static int port = 3306;
    private static String host = "mysql2f19.netcup.net";
    private static String dbName = "k74918_bogenliga";
    private static String password = "BsApp@100%";
    //private static String dbName = System.getenv("DB_USERNAME");
    private static String user = "k74918_bsapp_ro";
    //private static String password = System.getenv("DB_PASSWORD");

    // JDBC-URL
    private static final String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

    private static String[] tableNames = {"acl", "ergebniss", "liga", "mannschaft", "saison", "schuetze", "users", "wettkampfdaten"};


    private DatabaseConfiguration_NewDB DatabaseConfiguration_NewDB;

    public Main() {
        this.DatabaseConfiguration_NewDB= DatabaseConfiguration_NewDB;
    }


    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            File file = new File("temptable.sql");

            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    System.out.println("Datei 'temptable.sql' wurde gelöscht.");
                } else {
                    System.out.println("Fehler beim Löschen der Datei 'temptable.sql'.");
                    return;
                }
            }

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

    public static String insertTable(String tableName, boolean[] tables, String insertQuery)
    {
        if ("acl".equalsIgnoreCase(tableName) && tables[0]) {
            insertQuery = "CREATE TEMPORARY TABLE altsystem_acl (ID INT , users_ID INT , liga_ID INT);\r\n" + insertQuery;
            tables[0] = false;
        }
        if ("ergebniss".equalsIgnoreCase(tableName) && tables[1]) {
            insertQuery = "CREATE TEMPORARY TABLE altsystem_ergebniss (match INT , schuetze_ID INT, ergebniss INT);\r\n" + insertQuery;
            tables[1] = false;
        }
        if ("liga".equalsIgnoreCase(tableName) && tables[2]) {
            insertQuery = "CREATE TEMPORARY TABLE altsystem_liga (ID INT, subdom VARCHAR(255), name VARCHAR(255), id_nextLiga INT, secret VARCHAR(255));\r\n" + insertQuery;
            tables[2] = false;
        }
        if ("mannschaft".equalsIgnoreCase(tableName) && tables[3]) {
            insertQuery = "CREATE TEMPORARY TABLE altsystem_mannschaft (ID INT, liga_ID INT, manNr INT, name VARCHAR(255), saison_ID INT);\r\n" + insertQuery;
            tables[3] = false;
        }
        if ("saison".equalsIgnoreCase(tableName) && tables[4]) {
            insertQuery = "CREATE TEMPORARY TABLE altsystem_saison (ID INT, name VARCHAR(255), oderNr INT, aktuell INT);\r\n" + insertQuery;
            tables[4] = false;
        }
        if ("schuetze".equalsIgnoreCase(tableName) && tables[5]) {
            insertQuery = "CREATE TEMPORARY TABLE altsystem_schuetze (ID INT, mannschaft_ID INT,rueckNR INT, name VARCHAR(255));\r\n" + insertQuery;
            tables[5] = false;
        }
        if ("users".equalsIgnoreCase(tableName) && tables[6]) {
            insertQuery = "CREATE TEMPORARY TABLE altsystem_users (ID INT,email VARCHAR(50),password CHAR(128),su INT);\r\n" + insertQuery;
            tables[6] = false;
        }
        if ("wettkampfdaten".equalsIgnoreCase(tableName) && tables[7]) {
            insertQuery = "CREATE TEMPORARY TABLE altsystem_wettkampfdaten (ID INT, liga_ID INT, mannschaft INT, gegner INT, match INT, satzPlus INT, satzMinus INT, matchPlus INT, matchMinus INT, satz1 INT, satz2 INT, satz3 INT, satz4 INT, satz5 INT, saison_ID INT, sec INT);\r\n" + insertQuery;
            tables[7] = false;
        }
        return insertQuery;
    }

    public static void exportTable(Connection connection, String tableName) throws SQLException, IOException {

        // SQL Abfrage mit Backticks um den Tabellennamen
        String sql = "SELECT * FROM `" + tableName + "`";
        boolean[] tables  = new boolean[8];
        for(int i = 0; i < tables.length; i++)
            tables[i] = true;
        // Statement erstellen
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Ergebnisse speichern
            while (resultSet.next()) {
                //Daten in die temporäre Tabelle einfügen
                String insertSql = "INSERT INTO altsystem_" + tableName + " VALUES (";
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    insertSql += "?, ";
                }
                insertSql = insertSql.substring(0, insertSql.length() - 2) + ");\r\n"; //letztes Komma entfernen
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++)
                    insertStatement.setObject(i, resultSet.getObject(i)); //Wert aus der ResultSet übernehmen

                String insertQuery = insertStatement.toString();
                insertQuery = insertQuery.replace("com.mysql.cj.jdbc.ClientPreparedStatement: ",""); // Entferne den spezifischen String

                // Überprüfen, ob die Tabelle "mannschaft" ist
                insertQuery = insertTable(tableName, tables, insertQuery);

                // FileWriter in try-with-resources verwenden
                try (FileWriter fw = new FileWriter("temptable.sql", true)) { // true für den Anhängemodus
                    fw.write(insertQuery);
                } catch (IOException e){
                  System.out.println("Fehler beim Filewriter");
                }
            }
        }
    }
}


