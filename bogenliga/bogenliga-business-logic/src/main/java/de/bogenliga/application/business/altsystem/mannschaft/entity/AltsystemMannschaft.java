package de.bogenliga.application.business.altsystem.mannschaft.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemMannschaftMapper;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemVeranstaltungMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.verein.entity.AltsystemVerein;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemMannschaft implements AltsystemEntity<AltsystemMannschaftDO> {


    private final AltsystemMannschaftMapper altsystemMannschaftMapper;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;
    private final AltsystemVeranstaltungMapper altsystemVeranstaltungMapper;
    private final AltsystemVerein altsystemVerein;


    @Autowired
    public AltsystemMannschaft(final AltsystemMannschaftMapper altsystemMannschaftMapper, final DsbMannschaftComponent mannschaftComponent,
                               AltsystemUebersetzung altsystemUebersetzung,
                               AltsystemVeranstaltungMapper altsystemVeranstaltungMapper,
                               AltsystemVerein altsystemVerein){
        this.altsystemMannschaftMapper = altsystemMannschaftMapper;
        this.dsbMannschaftComponent = mannschaftComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
        this.altsystemVeranstaltungMapper = altsystemVeranstaltungMapper;
        this.altsystemVerein = altsystemVerein;
    }

    @Override
    public void create(AltsystemMannschaftDO altsystemDataObject, long currentUserId) {
        /*
         * Creates a new entry for the AltsystemMannschaftDO in the system, including the associated DsbMannschaftDO.
         *
         * @param altsystemDataObject The AltsystemMannschaftDO object to be created.
         * @param currentUserId The ID of the current user performing the operation.
         */

        DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO();
        //Verein-Methode aufrufen, falls der Verein existiert, wird kein neuer angelegt
        altsystemVerein.create(altsystemDataObject, currentUserId);
        //Veranstaltung der zugehörigen Mannschaft abrufen und zurückgeben
        VeranstaltungDO veranstaltungDO = altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemDataObject, currentUserId);

        //Mapper ausführen
        dsbMannschaftDO = altsystemMannschaftMapper.toDO(altsystemDataObject, dsbMannschaftDO);
        dsbMannschaftDO = altsystemMannschaftMapper.addDefaultFields(dsbMannschaftDO, currentUserId, altsystemDataObject, veranstaltungDO);
        dsbMannschaftDO.setBenutzerId(currentUserId);

        //In die Mannschaftstabelle schreiben
        dsbMannschaftDO = dsbMannschaftComponent.create(dsbMannschaftDO, currentUserId);

        //Altsystem ID und Neusystem ID in die Uebersetzungstabelle schreiben.
        altsystemUebersetzung.updateOrInsertUebersetzung(
                AltsystemUebersetzungKategorie.Mannschaft_Mannschaft,
                altsystemDataObject.getId(),
                dsbMannschaftDO.getId(),
                "");
    }


    @Override
    public void update(AltsystemMannschaftDO altsystemDataObject, long currentUserId) {
        /*
         * Updates the information of the AltsystemMannschaftDO in the system, including the associated DsbMannschaftDO.
         *
         * @param altsystemDataObject The AltsystemMannschaftDO object containing the updated information.
         * @param currentUserId The ID of the current user performing the update operation.
         * @throws BusinessException If no corresponding entry is found in the translation table for the provided AltsystemMannschaftDO ID.
         */
        //Verein Methode Aufrufen
        altsystemVerein.update(altsystemDataObject, currentUserId);
        //In der uebersetzungstabele anhand der AltsystemID die NeusystemID herausfinden.
        AltsystemUebersetzungDO mannschaftUebersetzung = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, altsystemDataObject.getId());

        //Wenn die Mannschaft noch nicht in der Uebersetzungstabelle vorhanden ist Exception
        if(mannschaftUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for mannschaftUebersetzung in Update ID'%s'", altsystemDataObject.getId()));
        }
        //Dataobjekt der Mannschaft suchen und zurückgeben
        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(mannschaftUebersetzung.getBogenligaId());

        //Mapper ausführen
        altsystemMannschaftMapper.toDO( altsystemDataObject, dsbMannschaftDO);

        //Mannschaft in der Tabele Updaten
        dsbMannschaftComponent.update(dsbMannschaftDO, currentUserId);

    }
}
