package de.bogenliga.application.services.v1;

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
public class DOTransfer {

    public ResultSet getTeamDO() throws SQLException {
        // Verein , Schütze & Mannschaften

        //database objects auslesen
        //---------------------------------------------
        Connection connection = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/swt2", "swt2", "swt2");

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mannschaft_test");
            results = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            connection.close();
        }

        return results;
    }

    public ResultSet getSchuetzeDO() throws SQLException {
        // Verein , Schütze & Mannschaften

        //database objects auslesen
        //---------------------------------------------
        Connection connection = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/swt2", "swt2", "swt2");

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM schuetze_test");
            results = statement.executeQuery();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection.close();


        return results;
    }

    //fehlende informationen ergänzen

    //ausgelesene objekte in neue datenbank schreiben


    //AUFBAU MANNSCHAFTEN
    /*
    CREATE TABLE mannschaft (
    ID int NOT NULL PRIMARY KEY DEFAULT nextval('sq_mannschaft'),
    liga_ID int NOT NULL,
    manNr varchar(45)  NOT NULL,
    name varchar(45)  NOT NULL,
    saison_ID int NOT NULL,
    CONSTRAINT fk_mannschaft_liga_id FOREIGN KEY (liga_ID) REFERENCES liga (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_mannschaft_saison_id FOREIGN KEY (saison_ID) REFERENCES saison (ID) ON DELETE CASCADE ON UPDATE CASCADE
    ) ;
     */
    //(9,3,'32WT111230','NBAV Neuenstadt',1)


}
