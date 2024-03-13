package de.bogenliga.application.business.altsystem.verein.mapper;


import java.util.List;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemVereinMapper implements ValueObjectMapper {

    private final VereinComponent vereinComponent;
    private final AltsystemLigaMapper altsystemLigaMapper;



    public AltsystemVereinMapper(VereinComponent vereinComponent,  AltsystemLigaMapper altsystemLigaMapper) {
        this.vereinComponent = vereinComponent;
        this.altsystemLigaMapper = altsystemLigaMapper;
    }


    public VereinDO toDO(VereinDO vereinDO, AltsystemMannschaftDO altsystemDataObject) {
        /**
         * Converts an AltsystemMannschaftDO to a VereinDO by setting the name and identifier.
         *
         * @param vereinDO The target VereinDO object where the data will be set.
         * @param altsystemDataObject The source AltsystemMannschaftDO object from which data will be extracted.
         * @return The VereinDO with data set from the AltsystemMannschaftDO.
         */

        //ParseName aufrufen und Name setzten
        vereinDO.setName(parseVereinName(altsystemDataObject));
        //ParseIdentifire aufrufen und neuer Identifire setzten
        vereinDO.setDsbIdentifier(parseIdentifier(altsystemDataObject));

        // wenn der Name des Vereisn leer ist oder der Name "leer" oder "fehlender Verein" ist
        // dann kopieren wir den Dsb Identifier in den Namen...
        if (vereinDO.getName().isEmpty() || vereinDO.getName().equals("leer") || vereinDO.getName().equals("fehlender Verein") ){
            vereinDO.setName( vereinDO.getName()+"Verein"+vereinDO.getDsbIdentifier());
        }

        return vereinDO;
    }

    public VereinDO addDefaultFields(VereinDO vereinDO){

        /**
         * Adds default fields to the VereinDO, such as setting the region based on mapping.
         *
         * @param vereinDO The VereinDO object to which default fields will be added.
         * @return The VereinDO with default fields set.
         */

        //region herausfinden und setzten
        RegionenDO dsbDO = altsystemLigaMapper.getDsbDO();
        vereinDO.setRegionId(dsbDO.getId());


        return vereinDO;
    }


    public String parseVereinName(AltsystemMannschaftDO altsystemMannschaftDO) {
        /**
         * Parses and normalizes the name from the AltsystemMannschaftDO to handle specific formatting.
         *
         * @param altsystemMannschaftDO The AltsystemMannschaftDO object from which the name will be parsed.
         * @return The parsed and normalized name with corrected formatting.
         */

        char[] nameOld = altsystemMannschaftDO.getName().toCharArray();
        String nameNew = "";
        String sonderzeichen = "/-";
        String num = "1234567890";

        String currentName = new String(nameOld);

        // Iteriere über den alten Namen und überprüfe jedes Zeichen auf inkorrekte Formatierung, ersetze entsprechend
        for (int i = 0; i < nameOld.length; i++) {

            // Speichere das aktuelle Zeichen am Index, um getString().charAt()-Aufrufe zu minimieren (für bessere Speichereffizienz)
            if (i + 1 < altsystemMannschaftDO.getName().length()) {
                // Wenn ein Punkt ohne nachfolgenden Leerzeichen kommt, füge ein Leerzeichen hinzu
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

        // Gibt das Vereinsobjekt zurück
        return nameNew;

    }

    public String parseIdentifier(AltsystemMannschaftDO altsystemDataObject){
        /**
         * Parses the identifier from the AltsystemMannschaftDO to extract specific positions.
         *
         * @param altsystemDataObject The AltsystemMannschaftDO object from which the identifier will be parsed.
         * @return The parsed identifier based on specific positions.
         */

        //Mannschaftsnummer in Array
        char[] identifierOld = altsystemDataObject.getMannr().toCharArray();
        //Die Stellen des Identifirers die gebraucht werden.
        int[] need = {2, 3, 6, 7, 8, 9};
        //String an den Stellen zusammenbauen
        StringBuilder builder = new StringBuilder();
        for (int index : need) {
            if (index >= 0 && index < identifierOld.length) {
                builder.append(identifierOld[index]);
            }
        }
        String vereinIdentifier = builder.toString();

        return vereinIdentifier;
    }

    public VereinDO getVereinDO(String vereinName, String vereinIdentifier){

        /**
         * Retrieves a VereinDO based on the provided identifier.
         *
         * @param vereinIdentifier The identifier used to search for the VereinDO.
         * @return The found VereinDO if it exists, or a VereinDO with a null ID if not found.
         */

        VereinDO identifiedDO = null;

        //Alle vorhandenen Vereine in eine Liste
        //wir suchen erst die Vereine mit identischem DSB Identifier
        List<VereinDO> vereinDOS = vereinComponent.findBySearch(vereinIdentifier);
        if (vereinDOS.isEmpty()){
            //wenn wir keinen Verein mit identischem Identifier finden, dann suchen wir nach gleichen Namen
            // der Name ist in der VereinTabelle ein unique Attribut - daher können wir da nichts Doppeltes anlegen
            vereinDOS = vereinComponent.findBySearch(vereinName);
        }
        //Durchsucht Liste nach angegebenen Verein
        if (vereinDOS.isEmpty()){
                throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                        String.format("No result found for Name '%s' or DSB-ID '%s'",vereinName ,vereinIdentifier));
        }
        // den ersten Treffer geben wir zurück
        //ggf. könnten wir hier noch prüfen, ob wir mehr als einen Treffer haben - aber wir gehen primär mal nicht davon aus.
        identifiedDO = vereinDOS.get(0);
        return identifiedDO;
    }

}
