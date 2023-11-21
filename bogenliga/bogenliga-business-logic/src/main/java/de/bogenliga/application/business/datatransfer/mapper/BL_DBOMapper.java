package de.bogenliga.application.business.datatransfer.mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.transform.Result;
import de.bogenliga.application.business.datatransfer.TableType;
import de.bogenliga.application.business.datatransfer.model.MannschaftDBO;
import de.bogenliga.application.business.datatransfer.bl_model.BL_MannschaftDBO;
import de.bogenliga.application.business.datatransfer.model.SchuetzeDBO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class BL_DBOMapper {

    //methode zum auslesen der "bl_"-tabellen schuetze, mannschaft, ...
    private ResultSet getData(TableType type) throws SQLException {
        String databaseURL = "jdbc:postgresql://localhost:5432/swt2";
        String query = null;
        ResultSet result = null;
        Connection connection = null;

        //durch tabletype pruefen welche daten abgerufen werden sollen, sql-abfrage entsprechend anpassen
        switch (type){
            case SCHUETZE:
                query = "SELECT * FROM bl_schuetze";
                break;
            case MANNSCHAFT:
                query = "SELECT * FROM bl_mannschaft";
                break;
            default:
                break;
        }

        try {
            connection = DriverManager.getConnection(databaseURL, "swt2", "swt2");
            Statement statement = connection.createStatement();
            result = statement.executeQuery(query);
            result.next();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            connection.close();
        }


        return result;
    }

    public SchuetzeDBO mapSchuetze() throws SQLException {
        SchuetzeDBO schuetze_new = new SchuetzeDBO();
        ResultSet schuetze_oldData = null;

        // bl schuzte auslesen
        schuetze_oldData = getData(TableType.SCHUETZE);

        while(schuetze_oldData.next()) {
            // aus schützen die keys auslesen und trennen name (nachname, vorname)
            schuetze_new.setDsb_mitglied_id(schuetze_oldData.getInt(1));

            String[] name = null;

            // (Allmendinger , Michael)
            //argument 0 = nachname, argument 1 = vorname
            if (schuetze_oldData.getString(4).contains(",")) {
                name = schuetze_oldData.getString(4).split(",");
                name[0] = name[0].replaceAll("\\s+", "");
                name[1] = name[1].replaceAll("\\s+", "");

            } else {
                name = schuetze_oldData.getString(4).split(" ");
                name[0] = name[0].replaceAll("\\s+", "");
                name[1] = name[1].replaceAll("\\s+", "");
            }

            schuetze_new.setDsb_mitglied_vorname(name[1]);
            schuetze_new.setDsb_mitglied_nachname(name[0]);


            System.out.println(name[1] + " " + name[0]);
        }
        // DBO dass die daten speichert in attribute
        // diese DBO an die neue DBO anschließen und fehlende generieren

        //
        //bl_schuetze -> dsb_mitglied


        return schuetze_new;
    }

    public MannschaftDBO mapMannschaft() {
        MannschaftDBO mannschaftDBO = new MannschaftDBO();
        SchuetzeDBO schuetze_new = new SchuetzeDBO();


        
        return mannschaftDBO;
    }


    private MannschaftDBO mapVerein(){
        //BL_MannschaftDBO mannschaft_old = null;
        MannschaftDBO mannschaft_new = null;
        ResultSet mannschaft_oldData = null;

        try {
            mannschaft_oldData = getData(TableType.MANNSCHAFT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /*

        mannschaft_oldData = [id][liga_id][mannr][saison_id]

        mannschaft_new.setVereinID([id]);

         */

        /*
        *
        *
        *
        TODO: tatsächliches mapping
        *
        *
        *
         */

        return mannschaft_new;
    }
}
