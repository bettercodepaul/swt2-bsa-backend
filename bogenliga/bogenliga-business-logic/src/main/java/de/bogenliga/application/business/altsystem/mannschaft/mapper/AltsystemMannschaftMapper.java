package de.bogenliga.application.business.altsystem.mannschaft.mapper;

import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemMannschaftMapper implements ValueObjectMapper {

    private final AltsystemUebersetzung altsystemUebersetzung;





    public AltsystemMannschaftMapper(AltsystemUebersetzung altsystemUebersetzung,
                                     AltsystemLigaMapper altsystemLigaMapper,
                                     DsbMannschaftComponent dsbMannschaftComponent) {
        this.altsystemUebersetzung = altsystemUebersetzung;
    }


    public DsbMannschaftDO toDO(AltsystemMannschaftDO altsystemMannschaftDO, DsbMannschaftDO dsbMannschaftDO){
        /**
         * Converts an AltsystemMannschaftDO to a DsbMannschaftDO by setting the team number based on specific conditions.
         *
         * @param altsystemMannschaftDO The source AltsystemMannschaftDO object from which data will be extracted.
         * @param dsbMannschaftDO The target DsbMannschaftDO object where the team number will be set.
         * @return The DsbMannschaftDO with the team number set based on conditions.
         */
        //MannschaftsNamen des Dataobjekts
        String currentName = altsystemMannschaftDO.getName();
        //Länge des Namens
        int nameLong = currentName.length() - 1;
        //Letztes Zeichen im Namen
        char lastChar = altsystemMannschaftDO.getName().charAt(nameLong);
        String lastCharAsString = String.valueOf(lastChar);
        long mannNr;
        String num = "1234567890";


        // wenn keine Manschaftsnummer als Teil des Vereinsnamen auftaucht
        // dann vergeben wir die 0 - die wird von den Anwendern nicht selbst vergeben
        // so sind wir auf der sicheren Seite, weil die 1 mehrfach vergeben wurde.
        if (currentName.contains("fehlender Verein") || currentName.equals("<leer>") || currentName == null) {
            mannNr = 0;
        }
        else {
            //Letztes zeichen Zahl, dann diese Zahl benutzen. Sonst 0
            if (num.contains(lastCharAsString)) {
                mannNr = Integer.parseInt(lastCharAsString);
            } else {
                mannNr = 0;
            }
        }

        dsbMannschaftDO.setNummer(mannNr);

        return dsbMannschaftDO;
    }


    public DsbMannschaftDO addDefaultFields (DsbMannschaftDO dsbMannschaftDO, long currentDSBMitglied, AltsystemMannschaftDO altsystemDataObject, VeranstaltungDO veranstaltungDO){
        /**
         * Adds default fields to the DsbMannschaftDO, such as setting the user ID, Verein ID, and Veranstaltung ID.
         *
         * @param dsbMannschaftDO The DsbMannschaftDO object to which default fields will be added.
         * @param currentDSBMitglied The ID of the current DSB Mitglied performing the operation.
         * @param altsystemDataObject The source AltsystemMannschaftDO object for additional information.
         * @param veranstaltungDO The VeranstaltungDO object associated with the DsbMannschaftDO.
         * @return The DsbMannschaftDO with default fields set.
         * @throws BusinessException If no corresponding entry is found in the translation table for the provided AltsystemMannschaftDO ID.
         */
        dsbMannschaftDO.setBenutzerId(currentDSBMitglied);

        //Verein herausfinden
        AltsystemUebersetzungDO vereinUebersetzung = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemDataObject.getId());

        if(vereinUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", altsystemDataObject.getId()));
        }
        //Verein im DO setzen
        dsbMannschaftDO.setVereinId(vereinUebersetzung.getBogenligaId());
        //Veranstaltung setzen
        dsbMannschaftDO.setVeranstaltungId(veranstaltungDO.getVeranstaltungID());

        return dsbMannschaftDO;
    }

}
