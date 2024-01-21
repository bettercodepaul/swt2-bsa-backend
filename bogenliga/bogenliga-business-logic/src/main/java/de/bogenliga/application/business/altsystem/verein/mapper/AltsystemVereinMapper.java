package de.bogenliga.application.business.altsystem.verein.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.verein.test.DSBVereinDO;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemVereinMapper implements ValueObjectMapper {


    public VereinDO toDO(VereinDO verein, AltsystemMannschaftDO altsystemMannschaftDO) throws SQLException {

        char[] nameOld = altsystemMannschaftDO.getName().toCharArray();
        String nameNew = "";
        long sort = 0;
        String sonderzeichen = "/-";
        String num = "1234567890";

        String currentName = new String(nameOld);
        if (currentName.equals("fehlender Verein") || currentName.equals("<leer>") || currentName.equals(null)) {
            return null;
        }

        // Iteriere über den alten Namen und überprüfe jedes Zeichen auf inkorrekte Formatierung, ersetze entsprechend
        for (int i = 0; i < nameOld.length; i++) {

            // Speichere das aktuelle Zeichen am Index, um getString().charAt()-Aufrufe zu minimieren (für bessere Speichereffizienz)
            if (i + 1 < altsystemMannschaftDO.getName().length()) {
                    // Wenn ein Punkt ohne nachfolgendem Leerzeichen kommt, füge ein Leerzeichen hinzu
                if (altsystemMannschaftDO.getName().charAt(i) == '.' && altsystemMannschaftDO.getName().charAt(
                        i + 1) != ' ') {
                    nameNew += ". ";
                } else if (sonderzeichen.contains(altsystemMannschaftDO.getName().charAt(i) + "")) {
                    // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge ein Leerzeichen hinzu
                    nameNew += " ";
                } else if (altsystemMannschaftDO.getName().charAt(i) == '.' && !num.contains(
                        altsystemMannschaftDO.getName().charAt(i - 1) + "")) {
                    // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge ein Leerzeichen hinzu
                    nameNew += "";
                } else if (num.contains(altsystemMannschaftDO.getName().charAt(i) + "") && i > 8) {
                    // Wenn das Zeichen eine Zahl ist und der Index größer als 7 ist, füge nichts hinzu (überspringe Zahl in der Mitte des Namens)
                    nameNew += "";
                } else {
                    // Andernfalls füge das Zeichen zum neuen Namen hinzu
                    nameNew += altsystemMannschaftDO.getName().charAt(i);
                }
            } else {
                // Für das letzte Zeichen
                if (sonderzeichen.contains(altsystemMannschaftDO.getName().charAt(i) + "") && !num.contains(
                        altsystemMannschaftDO.getName().charAt(i - 1) + "")) {
                    // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge ein Leerzeichen hinzu
                    nameNew += "";
                } else if (altsystemMannschaftDO.getName().charAt(i) == '.' && !num.contains(
                        altsystemMannschaftDO.getName().charAt(i - 1) + "")) {
                    // Wenn das Zeichen ein Sonderzeichen ist und nicht von einer Zahl gefolgt wird, füge ein Leerzeichen hinzu
                    nameNew += "";
                } else if (num.contains(altsystemMannschaftDO.getName().charAt(i) + "") && i >= 8) {
                    // Wenn das Zeichen eine Zahl ist und der Index größer oder gleich 7 ist, füge nichts hinzu (überspringe Zahl in der Mitte des Namens)
                    nameNew += "";
                } else {
                    // Andernfalls füge das Zeichen zum neuen Namen hinzu
                    nameNew += altsystemMannschaftDO.getName().charAt(i);
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
            verein.setId(sort);
            verein.setName(nameNew);
            verein.setRegionId(null);
            verein.setDsbIdentifier(null);
            //verein.setCreated_at_utc(timestamp);
            //verein.setCreated_by(null);
            //verein.setLast_modified_at_utc(null);
            //verein_new.setLast_modified_by(null);
            //verein_new.setVersion(null);
            verein.setWebsite(null);
            verein.setDescription(null);
            verein.setIcon(null);



        // Gibt das aktualisierte Vereinsobjekt zurück
        return verein;
    }

}
