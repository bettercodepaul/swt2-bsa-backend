package de.bogenliga.application.services.v1.olddbimport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.Integer.parseInt;


public class OldDbImport {
    private static final Logger LOG = LoggerFactory.getLogger(OldDbImport.class);

    // Verbindungsinformationen
    private static String host = "";
    private static int port = 0;
    private static String name = "";
    private static String user = "";
    private static String password = "";


    private static final String DROPSTATMENT = "DROP TABLE IF EXISTS altsystem_";

    // JDBC-URL
    private static final String URL = "jdbc:mysql://" + host + ":" + port + "/" + name;

    private static String sqlfile ="temptable.sql";

    private static String[] tableNames = {"acl", "ergebniss", "liga", "mannschaft", "saison", "schuetze", "users", "wettkampfdaten"};


    //TEST GET FROM LOGIN CREDENTIALS FROM ENV
    private static String URLT = System.getenv("DB_URL");
    private static String userT = System.getenv("DB_user");
    private static String passwordT = System.getenv("DB_password");

    private static String sqlQueryuser = "SELECT configuration_value FROM configuration WHERE configuration_key = 'OLDDBBenutzer'";
    private static String sqlQueryhost = "SELECT configuration_value FROM configuration WHERE configuration_key = 'OLDDBHost'";
    private static String sqlQuerypw = "SELECT configuration_value FROM configuration WHERE configuration_key = 'OLDDBPasswort'";
    private static String sqlQueryport = "SELECT configuration_value FROM configuration WHERE configuration_key = 'OLDDBPort'";
    private static String sqlQueryname = "SELECT configuration_value FROM configuration WHERE configuration_key = 'OLDDBName'";
    public static void main (String [] args){
        sync();

       /*
        user = executeQuery(sqlQueryuser);
        host = executeQuery(sqlQueryhost);
        password = executeQuery(sqlQuerypw);

        try {
            port = parseInt(executeQuery(sqlQueryport));
        } catch (NumberFormatException e) {
            System.err.println("string is keine zahl");
        }
        name = executeQuery(sqlQueryname);
        */
    }

    private static String executeQuery(String query) {
        String configurationValue = "";
        try (Connection connection = DriverManager.getConnection(URLT, userT, passwordT);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                configurationValue = resultSet.getString("configuration_value");
            }

        } catch (SQLException e) {
            e.printStackTrace();}

        System.out.println("Configuration Value: " + configurationValue);
        return configurationValue;
    }

    // Hilfsmethode zum Abrufen des Konfigurationswerts anhand des Schlüssels


    private static void executeScript(String scriptFilePath, String jdbcUrl, String username, String password) {
        try{
            String script=new String(Files.readAllBytes(Paths.get(scriptFilePath)));
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            try(Statement statement = connection.createStatement()){
                statement.execute(script);
                System.out.println("Script erfolgreich ausgeführt");
            }
            finally{
                if(connection != null && !connection.isClosed()){
                    connection.close();}}
        }
        catch (IOException | SQLException e){
            e.printStackTrace();
        }
    }


    public static void sync(){

        user = executeQuery(sqlQueryuser);
        host = executeQuery(sqlQueryhost);
        password = executeQuery(sqlQuerypw);

        try {
            port = parseInt(executeQuery(sqlQueryport));
        } catch (NumberFormatException e) {
            System.err.println("string is keine zahl");}
        name = executeQuery(sqlQueryname);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            File file = new File(sqlfile);

            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    System.out.println("Datei 'temptable.sql' wurde gelöscht.");
                } else {
                    System.out.println("Fehler beim Löschen der Datei 'temptable.sql'.");
                    return;
                }
            }

            // Connection herstellen
            try (Connection connection = DriverManager.getConnection(URL, user, password)) {

                for (int i = 0; i < tableNames.length; i++) {
                    System.out.println(tableNames[i]);
                    exportTable(connection, tableNames[i]);
                }
            }
            catch (IOException  | SQLException e) {
                e.printStackTrace();
            }
            finally{
                executeScript(sqlfile, URLT, userT, passwordT);
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();}

    }


    private static final Map<String, String> TABLE_DEFINITIONS;

    // Initialize the map with table definitions
    static {
        TABLE_DEFINITIONS = new HashMap<>();
        TABLE_DEFINITIONS.put("acl", "CREATE TABLE altsystem_acl (ID INT PRIMARY KEY, users_ID INT, liga_ID INT, created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("ergebniss", "CREATE TABLE altsystem_ergebniss (schuetze_ID INT, match INT,  ergebniss INT, created_at timestamp, updated_at timestamp, PRIMARY KEY(schuetze_ID ,match))");
        TABLE_DEFINITIONS.put("liga", "CREATE TABLE altsystem_liga (ID INT PRIMARY KEY, subdom VARCHAR(255), name VARCHAR(255), id_nextLiga INT, secret VARCHAR(255), created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("mannschaft", "CREATE TABLE altsystem_mannschaft (ID INT PRIMARY KEY, liga_ID INT, manNr VARCHAR(255), name VARCHAR(255), saison_ID INT, created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("saison", "CREATE TABLE altsystem_saison (ID INT PRIMARY KEY, name VARCHAR(255), oderNr INT, aktuell INT, created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("schuetze", "CREATE TABLE altsystem_schuetze (ID INT PRIMARY KEY, mannschaft_ID INT,rueckNR INT, name VARCHAR(255), created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("users", "CREATE TABLE altsystem_users (ID INT PRIMARY KEY,email VARCHAR(50),password CHAR(128),su INT, created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("wettkampfdaten", "CREATE TABLE altsystem_wettkampfdaten (ID INT PRIMARY KEY, liga_ID INT, mannschaft INT, gegner INT, match INT, satzPlus INT, satzMinus INT, matchPlus INT, matchMinus INT, satz1 INT, satz2 INT, satz3 INT, satz4 INT, satz5 INT, saison_ID INT, sec INT, created_at timestamp, updated_at timestamp)");
    }

    private static String createTable(String tableName) {
        return DROPSTATMENT + tableName + ";\n" + TABLE_DEFINITIONS.get(tableName) + ";\r\n";
    }

    private static String insertTable(String tableName, boolean[] tables, String insertQuery) {
        for (int i = 0; i < TABLE_DEFINITIONS.size(); i++) {
            if (tableName.equalsIgnoreCase(TABLE_DEFINITIONS.keySet().toArray()[i].toString()) && tables[i]) {
                tables[i] = false;
                return createTable(tableName) + insertQuery;
            }
        }

        return insertQuery;
    }

    private static String exportTable(Connection connection, String tableName) throws SQLException, IOException {
        String tempInsertQuery;
        String sql = "SELECT * FROM " + tableName + "";
        boolean[] tables = new boolean[8];
        for (int i = 0; i < tables.length; i++) {
            tables[i] = true;
        }

        StringBuilder generatedSql;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            generatedSql = new StringBuilder();
            while (resultSet.next()) {
                String insertSql = "INSERT INTO altsystem_" + tableName + " VALUES (";
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    insertSql += "?, ";
                }
                insertSql = insertSql.substring(0, insertSql.length() - 2) + ");\r\n";
                PreparedStatement insertStatement = null;

                try {
                    insertStatement = connection.prepareStatement(insertSql);
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        insertStatement.setObject(i, resultSet.getObject(i));
                    }

                    String insertQuery = insertStatement.toString();
                    tempInsertQuery = insertQuery;
                    insertQuery = insertQuery.replace("com.mysql.cj.jdbc.ClientPreparedStatement: ", "");

                    insertQuery = insertTable(tableName, tables, insertQuery);

                    FileWriter fw = null;
                    try {
                        fw = new FileWriter(sqlfile, true);
                        fw.write(insertQuery);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Fehler beim Filewriter");
                    } finally {
                        if (fw != null) {
                            try {
                                fw.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("Fehler beim Schließen des Filewriters");
                            }
                        }
                    }

                } finally {
                    if (insertStatement != null) {
                        insertStatement.close();
                    }
                }
                generatedSql.append(tempInsertQuery);
            }
        }
        return generatedSql.toString();
    }

    public static String executeQueryWrapper(String query) {
        return OldDbImport.executeQuery(query);
    }

    public static void executeScriptWrapper(String scriptFilePath, String jdbcUrl, String username, String password) {
        OldDbImport.executeScript(scriptFilePath, jdbcUrl, username, password);
    }

    public static String exportTableWrapper(Connection connection, String tableName) throws SQLException, IOException {
        return OldDbImport.exportTable(connection, tableName);
    }

    public static String createTableWrapper(String tableName) {
        return OldDbImport.createTable(tableName);
    }

    public static String insertTableWrapper(String tableName, boolean[] tables, String insertQuery) {
        return OldDbImport.insertTable(tableName, tables, insertQuery);
    }
}