package de.bogenliga.application.business.altsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import de.bogenliga.application.business.altsystem.verein.test.DSBMannschaftDO;
import de.bogenliga.application.business.altsystem.verein.test.DSBMitgliedDO;
import de.bogenliga.application.business.altsystem.verein.test.DSBVereinDO;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class BL_DBOMapper {
    private static int schuetzeID;

    //methode zum auslesen der "bl_"-tabellen schuetze, mannschaft, ...
    private ResultSet getData(TableType type) throws SQLException {
        String databaseURL = "jdbc:postgresql://localhost:5432/swt2";
        String query = null;
        ResultSet result = null;
        Connection connection = null;

        //durch tabletype pruefen welche daten abgerufen werden sollen, sql-abfrage entsprechend anpassen
        switch (type) {
            case SCHUETZE:
                query = "SELECT * FROM bl_schuetze";
                break;
            case MANNSCHAFT:
                query = "SELECT * FROM bl_mannschaft";
                break;
            case VEREIN:
                query = "SELECT * FROM verein";
                break;
            default:
                break;
        }

        try {
            connection = DriverManager.getConnection(databaseURL, "swt2", "swt2");
            Statement statement = connection.createStatement();
            result = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }


        return result;
    }



    public List<DSBMitgliedDO> mapSchuetze() throws SQLException {
        ResultSet schuetze_oldData = null;
        List<DSBMitgliedDO> schuetzeList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();

        try {
            schuetze_oldData = getData(TableType.SCHUETZE);
        } catch (SQLException e){
            e.printStackTrace();
        }



        while (schuetze_oldData.next()) {
            DSBMitgliedDO schuetze_new = new DSBMitgliedDO(); // Ein neues Objekt in jeder Iteration erstellen

            //aus schützen die keys auslesen und trennen name (nachname, vorname)

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

            //schütze 1: m. gheb. schütze: m. gheb.
            if(!nameList.contains(name[0] + " " + name[1])) {
                nameList.add(name[0] + " " + name[1]);

                schuetze_new.setDsb_mitglied_vorname(name[1]);
                schuetze_new.setDsb_mitglied_nachname(name[0]);
                schuetze_new.setDsb_mitglied_geburtsdatum(null);
                schuetze_new.setDsb_mitglied_nationalitaet(null);
                schuetze_new.setDsb_mitglied_mitgliedsnummer(null);
                //schuetze_new.setDsb_mitglied_benutzer_id(); -> Daten aus Verein auslesen
                schuetze_new.setCreated_at_utc(null);
                schuetze_new.setCreated_by(0);
                schuetze_new.setLast_modified_at_utc(null);
                schuetze_new.setLast_modified_by(0);
                schuetze_new.setVersion(0);

                schuetzeList.add(schuetze_new);

                int mannschaft_id = Integer.parseInt(schuetze_oldData.getString(2));
            } else {
                continue;
            }
        }

        //

        return schuetzeList;
    }


    public List<DSBMannschaftDO> mapMannschaft() throws SQLException {
        List<DSBMannschaftDO> mannschaftList = new ArrayList<>();
        ResultSet mannschaft_oldData = getData(TableType.MANNSCHAFT);
        ResultSet verein = getData(TableType.VEREIN);

        int sort = 0;

        while (mannschaft_oldData.next()) {
            char[] nameOld = mannschaft_oldData.getString(4).toCharArray();
            String nameNew = "";
            String nameNewWithoutNumber = "";
            String sonderzeichen = "/-.";
            String num = "1234567890";
            int data = 0;

            String currentName = new String(nameOld);
            if (currentName.equals("fehlender Verein") || currentName.equals("<leer>") || currentName.equals(null)) {
                continue; // Überspringe den Eintrag, wenn der Name "fehlender Verein" oder "<leer>" ist
            }

            // Anpassen des Strings, sodass dieser genauso aussieht wie bei mapVerein, aber die Zahl am Ende beibehalten wird
            for (int i = 0; i < nameOld.length; i++) {
                // Speichere das aktuelle Zeichen am Index, um getString().charAt()-Aufrufe zu minimieren (für bessere Speichereffizienz)
                if (i + 1 < mannschaft_oldData.getString(4).length()) {
                    // Wenn ein Punkt ohne nachfolgendem Leerzeichen kommt, füge ein Leerzeichen hinzu
                    if (mannschaft_oldData.getString(4).charAt(i) == '.' && mannschaft_oldData.getString(4).charAt(
                            i + 1) != ' ') {
                        nameNew += ". ";
                    } else if (sonderzeichen.contains(mannschaft_oldData.getString(4).charAt(i) + "") &&
                            !num.contains(mannschaft_oldData.getString(4).charAt(i - 1) + "")) {
                        // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge ein Leerzeichen hinzu
                        nameNew += " ";
                    } else if (num.contains(mannschaft_oldData.getString(4).charAt(i) + "") && i > 8) {
                        // Wenn das Zeichen eine Zahl ist und der Index größer als 7 ist, füge nichts hinzu (überspringe Zahl in der Mitte des Namens)
                        nameNew += "";
                    } else {
                        // Andernfalls füge das Zeichen zum neuen Namen hinzu
                        nameNew += mannschaft_oldData.getString(4).charAt(i);
                    }
                } else {
                    // Für das letzte Zeichen
                    if (mannschaft_oldData.getString(4).charAt(i) == '.') {
                        nameNew += ".";
                    } else if (sonderzeichen.contains(mannschaft_oldData.getString(4).charAt(i) + "") &&
                            !num.contains(mannschaft_oldData.getString(4).charAt(i - 1) + "")) {
                        // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge nichts hinzu
                        nameNew += "";
                    } else {
                        // Andernfalls füge das Zeichen zum neuen Namen hinzu
                        nameNew += mannschaft_oldData.getString(4).charAt(i);
                    }
                }
            }

            // Überprüfen, ob die letzte Zeichenfolge eine Zahl ist
            if (Character.isDigit(nameNew.charAt(nameNew.length() - 1))) {
                // Wenn ja, setze data auf die gefundene Nummer
                StringBuilder numberStringBuilder = new StringBuilder();
                for (int j = nameNew.length() - 1; j >= 0 && Character.isDigit(nameNew.charAt(j)); j--) {
                    numberStringBuilder.insert(0, nameOld[j]);
                }
                data = Integer.parseInt(numberStringBuilder.toString());

                // Setze nameNewWithoutNumber auf den Namen ohne die Zahl am Ende
                nameNewWithoutNumber = nameNew.substring(0, nameNew.length() - numberStringBuilder.length());
            } else {
                // Wenn nicht, setze data auf 1 und nameNewWithoutNumber auf den gesamten Namen
                data = 1;
                nameNewWithoutNumber = nameNew;
            }
            DSBMannschaftDO mannschaftDBO = new DSBMannschaftDO();
            if (verein != null) {
                try {
                    while (verein.next()) {
                        String name = verein.getString("verein_name");
                        if (nameNewWithoutNumber.equals(name)) {
                            int Id = verein.getInt("verein_id");
                            mannschaftDBO.setMannschaftVereinId(Id);
                            break;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    verein.close();
                }


                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                mannschaftDBO.setMannschaftId(sort);
                mannschaftDBO.setMannschaftNummer(data);
                mannschaftDBO.setMannschaftBenutzerId(1000);
                //@TODO
                //mannschaftDBO.setMannschaftVeranstaltungId(1);
                mannschaftDBO.setCreatedAtUtc(timestamp);
                mannschaftDBO.setCreatedBy(1);
                mannschaftDBO.setLastModifiedAtUtc(timestamp);
                mannschaftDBO.setLastModifiedBy(0);
                mannschaftDBO.setVersion(1);
                mannschaftDBO.setMannschaftSortierung(sort);

                mannschaftList.add(mannschaftDBO);

                // Für Sortierung mannschaftDBO;
                sort += 1;
            }

        }
        return mannschaftList;
    }


    public List<DSBVereinDO> mapVerein() throws SQLException {
        //BL_MannschaftDBO mannschaft_old = null;
        List<DSBVereinDO> vereinList = new ArrayList<>();

        ResultSet mannschaft_oldData = null;


        try {
            mannschaft_oldData = getData(TableType.MANNSCHAFT);
        } catch (SQLException e){
            e.printStackTrace();
        }



        while (mannschaft_oldData.next()) {
            char[] nameOld = mannschaft_oldData.getString(4).toCharArray();
            DSBVereinDO verein_new = new DSBVereinDO();
            String nameNew = "";
            int sort = 0;
            String sonderzeichen = "/-";
            String num = "1234567890";

            String currentName = new String(nameOld);
            if (currentName.equals("fehlender Verein") || currentName.equals("<leer>") || currentName.equals(null)) {
                continue; // Überspringe den Eintrag, wenn der Name "fehlender Verein" oder "<leer>" ist
            }

            // Iteriere über den alten Namen und überprüfe jedes Zeichen auf inkorrekte Formatierung, ersetze entsprechend
            for (int i = 0; i < nameOld.length; i++) {

                // Speichere das aktuelle Zeichen am Index, um getString().charAt()-Aufrufe zu minimieren (für bessere Speichereffizienz)
                if (i + 1 < mannschaft_oldData.getString(4).length()) {
                    // Wenn ein Punkt ohne nachfolgendem Leerzeichen kommt, füge ein Leerzeichen hinzu
                    if (mannschaft_oldData.getString(4).charAt(i) == '.' && mannschaft_oldData.getString(4).charAt(
                            i + 1) != ' ') {
                        nameNew += ". ";
                    } else if (sonderzeichen.contains(mannschaft_oldData.getString(4).charAt(i) + "")) {
                        // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge ein Leerzeichen hinzu
                        nameNew += " ";
                    } else if (mannschaft_oldData.getString(4).charAt(i) == '.' && !num.contains(
                            mannschaft_oldData.getString(4).charAt(i - 1) + "")) {
                        // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge ein Leerzeichen hinzu
                        nameNew += "";
                    } else if (num.contains(mannschaft_oldData.getString(4).charAt(i) + "") && i > 8) {
                        // Wenn das Zeichen eine Zahl ist und der Index größer als 7 ist, füge nichts hinzu (überspringe Zahl in der Mitte des Namens)
                        nameNew += "";
                    }else {
                        // Andernfalls füge das Zeichen zum neuen Namen hinzu
                        nameNew += mannschaft_oldData.getString(4).charAt(i);
                    }
                } else {
                    // Für das letzte Zeichen
                    if (sonderzeichen.contains(mannschaft_oldData.getString(4).charAt(i) + "") && !num.contains(
                            mannschaft_oldData.getString(4).charAt(i - 1) + "")) {
                        // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge ein Leerzeichen hinzu
                        nameNew += "";
                    } else if (mannschaft_oldData.getString(4).charAt(i) == '.' && !num.contains(
                            mannschaft_oldData.getString(4).charAt(i - 1) + "")) {
                        // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge ein Leerzeichen hinzu
                        nameNew += "";
                    } else if (num.contains(mannschaft_oldData.getString(4).charAt(i) + "") && i >= 8) {
                        // Wenn das Zeichen eine Zahl ist und der Index größer oder gleich 7 ist, füge nichts hinzu (überspringe Zahl in der Mitte des Namens)
                        nameNew += "";
                    }else {
                        // Andernfalls füge das Zeichen zum neuen Namen hinzu
                        nameNew += mannschaft_oldData.getString(4).charAt(i);
                    }
                }
            }

            int nameLong = nameNew.length() - 1;

            if (nameNew.charAt(nameLong) == ' ') {
                nameNew = nameNew.substring(0, nameNew.length() - 1);
            }


            // Spezielle Überprüfung für bestimmte Namen und Ausgabe des normalisierten Namens
            if (nameNew.equals("SVgg Endersb.  Strümpf") || nameNew.equals("SVng Endersbach Strümpf")) {
                nameNew = "SVng Endersbach Strümpfelbach";
            } else if (nameNew.equals("SV Ensheim  e. V")){
                nameNew = "SV Ensheim  e.V.";
            } else if (nameNew.equals("BSG Odenheim e. V")){
                nameNew = "BSG Odenheim e.V.";
            } else if (nameNew.equals("BWT Kichentellinsfurt")){
                nameNew = "BWT Kirchentellinsfurt";
            } else if (nameNew.equals("BSC Schoemberg")){
                nameNew = "BSC Schömberg";
            } else if (nameNew.equals("ZV Sontheim")){
                nameNew = "ZV Sontheim Brenz";
            } else if (nameNew.equals("SV Tell Weilheim Teck") || nameNew.equals("SV Weilheim Teck")){
                nameNew = "SV Tell Weilheim";
            } else if (nameNew.equals("BSV Hausen i K") || nameNew.equals("BSV Hausen i. K") ){
                nameNew = "BSV Hausen i.K.";
            }


            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            verein_new.setVerein_id(sort);
            verein_new.setVerein_name(nameNew);
            verein_new.setVerein_region_id(1);
            verein_new.setVerein_dsb_identifier(null);
            verein_new.setCreated_at_utc(timestamp);
            verein_new.setCreated_by(null);
            verein_new.setLast_modified_at_utc(null);
            //verein_new.setLast_modified_by(null);
            //verein_new.setVersion(null);
            verein_new.setVerein_website(null);
            verein_new.setVerein_describtion(null);
            verein_new.setVerein_icon(null);


            vereinList.add(verein_new);
            sort += 1;
        }

        // Gibt das aktualisierte Vereinsobjekt zurück
        return vereinList;
    }
}

