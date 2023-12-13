package de.bogenliga.application.business.datatransfer.mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import de.bogenliga.application.business.datatransfer.TableType;
import de.bogenliga.application.business.datatransfer.model.MannschaftDBO;
import de.bogenliga.application.business.datatransfer.model.SchuetzeDBO;
import de.bogenliga.application.business.datatransfer.model.VereinDBO;

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

        //bl_schuetze -> dsb_mitglied

        return schuetze_new;
    }

    public MannschaftDBO mapMannschaft() {
        MannschaftDBO mannschaftDBO = new MannschaftDBO();
        SchuetzeDBO schuetze_new = new SchuetzeDBO();

        return mannschaftDBO;
    }


    public VereinDBO mapVerein() throws SQLException{
        //BL_MannschaftDBO mannschaft_old = null;
        VereinDBO verein_new = new VereinDBO();
        ResultSet mannschaft_oldData = null;

        mannschaft_oldData = getData(TableType.MANNSCHAFT);


        while(mannschaft_oldData.next()) {
            char[] nameOld;
            String nameNew = "";
            String sonderzeichen = "/-.";
            String num = "1234567890";

            //@Setup Alles Einheitlich machen
            nameOld = mannschaft_oldData.getString(4).toCharArray();

            //iterate over old name -> check every character for mismatching formatting -> replace accordingly
            for(int i = 0; i < nameOld.length; i++){

                //store current char at index to reduce getString().charAt()-calls for memory efficiency
                if (i + 1 < mannschaft_oldData.getString(4).length()) {
                    if (mannschaft_oldData.getString(4).charAt(i) == '.' && mannschaft_oldData.getString(4).charAt(i + 1) != ' ') {
                        nameNew += ". ";
                    } else if (sonderzeichen.contains(mannschaft_oldData.getString(4).charAt(i) + "") && !num.contains(
                            mannschaft_oldData.getString(4).charAt(i - 1) + "")) {
                        nameNew += " ";
                    } else if (num.contains(mannschaft_oldData.getString(4).charAt(i) + "") && i > 7) {
                        nameNew += "";
                    } else {
                        nameNew += mannschaft_oldData.getString(4).charAt(i);
                    }
                }else {
                    if (mannschaft_oldData.getString(4).charAt(i) == '.'){
                        nameNew += ".";
                    } else if (sonderzeichen.contains(mannschaft_oldData.getString(4).charAt(i) + "") && !num.contains(
                            mannschaft_oldData.getString(4).charAt(i - 1) + "")) {
                        nameNew += "";
                    } else if (num.contains(mannschaft_oldData.getString(4).charAt(i) + "") && i >= 7) {
                        nameNew += "";
                    } else {
                        nameNew += mannschaft_oldData.getString(4).charAt(i);
                    }

                }
            }
            if (nameNew.equals("SVgg Endersb.  Strümpf.") || nameNew.equals("SVng Endersbach Strümpf.") ) {
                nameNew = "SVng Endersbach Strümpfelbach";
            }

            System.out.println(nameNew);
            verein_new.setVerein_name(nameNew);
        }

       return verein_new;
    }
}
