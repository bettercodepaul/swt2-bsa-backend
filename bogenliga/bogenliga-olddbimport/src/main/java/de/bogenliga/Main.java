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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;




public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    // Verbindungsinformationen



    private static String host = "mysql2f19.netcup.net";
    private static int port = 3306;
    private static String dbName = "k74918_bogenliga";
    private static String user = "k74918_bsapp_ro";
    private static String password = "BsApp@100%";


    private static final String DROPSTATMENT = "DROP TABLE IF EXISTS altsystem_";

    // JDBC-URL
    private static final String URL = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

    private static String sqlfile ="temptable.sql";

    private static String[] tableNames = {"acl", "ergebniss", "liga", "mannschaft", "saison", "schuetze", "users", "wettkampfdaten"};
    public static void main (String [] args){
        sync();

    }


    public static void executeScript(String scriptFilePath, String jdbcUrl, String username, String password) {
        try{
            String script=new String(Files.readAllBytes(Paths.get(scriptFilePath)));
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            try(Statement statement = connection.createStatement()){
                statement.execute(script);
                System.out.println("Script erfolgreich ausgeführt");
            }
            finally{
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            }
        }
        catch (IOException | SQLException e){
            e.printStackTrace();
        }

    }


    public static void sync(){
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

                executeScript("temptable.sql", "jdbc:postgresql://localhost:5432/swt2", "swt2","swt2" );
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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

    private static void exportTable(Connection connection, String tableName) throws SQLException, IOException {
        String sql = "SELECT * FROM " + tableName + "";
        boolean[] tables = new boolean[8];
        for (int i = 0; i < tables.length; i++) {
            tables[i] = true;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

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
                    insertQuery = insertQuery.replace("com.mysql.cj.jdbc.ClientPreparedStatement: ", "");

                    insertQuery = insertTable(tableName, tables, insertQuery);

                    try (FileWriter fw = new FileWriter(sqlfile, true)) {
                        fw.write(insertQuery);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Fehler beim Filewriter");
                    }

                } finally {
                    if (insertStatement != null) {
                        insertStatement.close();
                    }
                }
            }
        }
    }

}