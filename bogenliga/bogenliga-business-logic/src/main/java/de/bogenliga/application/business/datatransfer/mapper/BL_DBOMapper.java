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
    private List<SchuetzeDBO> schuetzeList = new ArrayList<>();

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
            result.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (result != null) {
                result.close();
            }
            connection.close();
        }


        return result;
    }


    public SchuetzeDBO mapSchuetze() throws SQLException {
        SchuetzeDBO schuetze_new = new SchuetzeDBO();
        ResultSet schuetze_oldData = null;

        try {
            schuetze_oldData = getData(TableType.SCHUETZE);
        } catch (SQLException e){
            e.printStackTrace();
        }



        while (schuetze_oldData.next()) {
            SchuetzeDBO schuetze_new = new SchuetzeDBO(); // Ein neues Objekt in jeder Iteration erstellen

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
        }
        // DBO dass die daten speichert in attribute
        // diese DBO an die neue DBO anschließen und fehlende generieren

        //bl_schuetze -> dsb_mitglied

        return schuetze_new;
    }


    public List<MannschaftDBO> mapMannschaft() throws SQLException {
        List<MannschaftDBO> mannschaftList = new ArrayList<>();
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
                    } else if (num.contains(mannschaft_oldData.getString(4).charAt(i) + "") && i > 7) {
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
            MannschaftDBO mannschaftDBO = new MannschaftDBO();
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


    public VereinDBO mapVerein() throws SQLException {
        //BL_MannschaftDBO mannschaft_old = null;
        VereinDBO verein_new = new VereinDBO();
        ResultSet mannschaft_oldData = null;

        mannschaft_oldData = getData(TableType.MANNSCHAFT);


        while (mannschaft_oldData.next()) {
            char[] nameOld;
            String nameNew = "";
            String sonderzeichen = "/-.";
            String num = "1234567890";


            nameOld = mannschaft_oldData.getString(4).toCharArray();

            // Iteriere über den alten Namen und überprüfe jedes Zeichen auf inkorrekte Formatierung, ersetze entsprechend
            for (int i = 0; i < nameOld.length; i++) {

                // Speichere das aktuelle Zeichen am Index, um getString().charAt()-Aufrufe zu minimieren (für bessere Speichereffizienz)
                if (i + 1 < mannschaft_oldData.getString(4).length()) {
                    // Wenn ein Punkt ohne nachfolgendem Leerzeichen kommt, füge ein Leerzeichen hinzu
                    if (mannschaft_oldData.getString(4).charAt(i) == '.' && mannschaft_oldData.getString(4).charAt(
                            i + 1) != ' ') {
                        nameNew += ". ";
                    } else if (sonderzeichen.contains(mannschaft_oldData.getString(4).charAt(i) + "") && !num.contains(
                            mannschaft_oldData.getString(4).charAt(i - 1) + "")) {
                        // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge ein Leerzeichen hinzu
                        nameNew += " ";
                    } else if (num.contains(mannschaft_oldData.getString(4).charAt(i) + "") && i > 7) {
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
                    } else if (sonderzeichen.contains(mannschaft_oldData.getString(4).charAt(i) + "") && !num.contains(
                            mannschaft_oldData.getString(4).charAt(i - 1) + "")) {
                        // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge nichts hinzu
                        nameNew += "";
                    } else if (num.contains(mannschaft_oldData.getString(4).charAt(i) + "") && i >= 7) {
                        // Wenn das Zeichen eine Zahl ist und der Index größer oder gleich 7 ist, füge nichts hinzu (überspringe Zahl in der Mitte des Namens)
                        nameNew += "";
                    } else {
                        // Andernfalls füge das Zeichen zum neuen Namen hinzu
                        nameNew += mannschaft_oldData.getString(4).charAt(i);
                    }
                }
            }

            // Spezielle Überprüfung für bestimmte Namen und Ausgabe des normalisierten Namens
            if (nameNew.equals("SVgg Endersb.  Strümpf.") || nameNew.equals("SVng Endersbach Strümpf.")) {
                nameNew = "SVng Endersbach Strümpfelbach";
            }

            System.out.println(nameNew);
            verein_new.setVerein_name(nameNew);
        }

        // Gibt das aktualisierte Vereinsobjekt zurück
        return verein_new;
    }
}

