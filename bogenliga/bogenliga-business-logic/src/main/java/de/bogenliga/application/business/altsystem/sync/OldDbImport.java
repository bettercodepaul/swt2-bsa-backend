package de.bogenliga.application.business.altsystem.sync;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


import de.bogenliga.application.business.configuration.impl.dao.ConfigurationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OldDbImport {

    // Verbindungsinformationen zu Geros Altsystem DB
    private  String host = "";
    private  String port = "0";
    private  String name = "";
    private  String user = "";
    private  String password = "";


    private static final String DROPSTATMENT = "DROP TABLE IF EXISTS altsystem_";

    private HashMap<Object, Object> TABLE_DEFINITIONS = null;



    // JDBC-URL der My SQL Datenbank
    private  String URL = "jdbc:mysql://" + host + ":" + port + "/" + name;
    private  String[] tableNames = {"acl", "ergebniss", "liga", "mannschaft", "saison", "schuetze", "users", "wettkampfdaten"};

    //SQL Querys for Database Credentials for MY SQL Database from Postgres Database "configuration" Table
    private final String sqlQueryuser = "OLDDBBenutzer";
    private final String sqlQueryhost = "OLDDBHost";
    private final String sqlQuerypw = "OLDDBPassword";
    private final String sqlQueryport = "OLDDBPort";
    private final String sqlQueryname = "OLDDBName";

    public static boolean executeScriptEnabled = true;

    private SyncDAO syncDAO;

    private ConfigurationDAO configurationDAO;

    @Autowired
    public OldDbImport(SyncDAO syncDAO, ConfigurationDAO configurationDAO) {
        this.syncDAO = syncDAO;
        this.configurationDAO = configurationDAO;

        TABLE_DEFINITIONS = new HashMap<>();
        TABLE_DEFINITIONS.put("acl","CREATE TABLE altsystem_acl (ID INT PRIMARY KEY, users_ID INT, liga_ID INT, created_at timestamp, updated_at timestamp)");

        // die Tabellendefinition "ergebniss" im Altsystem umfasst keine ID - wir benötigen aber einen Schlüssel
        // für die weitere die Verarbeitung.
        // Daher definieren wir die ID zusätzlich, und lassen sie beim Insert automatisch erzeugen.
        // in der Verarbeitung wird dann die ID wie ein Attribut des Altsystems genutzt
        TABLE_DEFINITIONS.put("ergebniss","CREATE TABLE altsystem_ergebniss (ID INT GENERATED ALWAYS AS IDENTITY, schuetze_id INT, match INT,  ergebniss INT, created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("liga","CREATE TABLE altsystem_liga (ID INT PRIMARY KEY, subdom VARCHAR(255), name VARCHAR(255), id_nextLiga INT, secret VARCHAR(255), created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("mannschaft","CREATE TABLE altsystem_mannschaft (ID INT PRIMARY KEY, liga_ID INT, manNr VARCHAR(255), name VARCHAR(255), saison_ID INT, created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("saison","CREATE TABLE altsystem_saison (ID INT PRIMARY KEY, name VARCHAR(255), oderNr INT, aktuell INT, created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("schuetze","CREATE TABLE altsystem_schuetze (ID INT PRIMARY KEY, mannschaft_ID INT,rueckNR INT, name VARCHAR(255), created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("users","CREATE TABLE altsystem_users (ID INT PRIMARY KEY,email VARCHAR(50),password CHAR(128),su INT, created_at timestamp, updated_at timestamp)");
        TABLE_DEFINITIONS.put("wettkampfdaten","CREATE TABLE altsystem_wettkampfdaten (ID INT PRIMARY KEY, liga_ID INT, mannschaft INT, gegner INT, match INT, satzPlus INT, satzMinus INT, matchPlus INT, matchMinus INT, satz1 INT, satz2 INT, satz3 INT, satz4 INT, satz5 INT, saison_ID INT, sec INT, created_at timestamp, updated_at timestamp)");
    }

    private  String createTable(String tableName) {
        return DROPSTATMENT + tableName + " CASCADE;\n" + TABLE_DEFINITIONS.get(tableName)+";\r\n";
    }


    public void setURL(String url) {
        URL = url;
    }

    private String getConfig(String key) {
        return configurationDAO.findByKey(key).getConfigurationValue();
    }

    public String sync() {

        StringBuilder generatedSql = new StringBuilder();

        // Holt aus unserer Datenbank die Verbindungsinformationen für Geros DB
        user = getConfig(sqlQueryuser);
        host = getConfig(sqlQueryhost);
        password = getConfig(sqlQuerypw);
        port = getConfig(sqlQueryport);
        name = getConfig(sqlQueryname);

        setURL("jdbc:mysql://" + host + ":" + port + "/" + name);
        String tableSql = "";

        // Verbindung mit Geros Datenbank wird hergestellt
        try (Connection connection = DriverManager.getConnection(URL, user, password)) {
            for (int i = 0; i < tableNames.length; i++) {
                System.out.println(tableNames[i]);

                //Löschen und neu erstellen der Tabellen
                syncDAO.executeScript(createTable(tableNames[i]));

                //Erzeugt SQL Befehle
                tableSql += exportTable(connection, tableNames[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (executeScriptEnabled) {

                //SQL- Query wird auf unserer DB ausgeführt
                syncDAO.executeScript(tableSql);
            }
        }
        return generatedSql.toString();
    }


    private String exportTable(Connection connection, String tableName) throws SQLException {
        String tempInsertQuery;
        String insertSql;
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
                if (tableName.equals("ergebniss")) {
                    insertSql = "INSERT INTO altsystem_" + tableName + " VALUES ( default,";
                }
                else {
                    insertSql = "INSERT INTO altsystem_" + tableName + " VALUES (";
                }
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

                    tempInsertQuery = insertQuery;


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
}
