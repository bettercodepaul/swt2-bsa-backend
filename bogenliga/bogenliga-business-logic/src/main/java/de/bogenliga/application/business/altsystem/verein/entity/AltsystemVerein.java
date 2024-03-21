package de.bogenliga.application.business.altsystem.verein.entity;

import de.bogenliga.application.business.altsystem.schuetze.entity.AltsystemSchuetze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.verein.mapper.AltsystemVereinMapper;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemVerein implements AltsystemEntity<AltsystemMannschaftDO> {


    private final AltsystemVereinMapper altsystemVereinMapper;
    private final VereinComponent vereinComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;

    private static final Logger LOGGER = LoggerFactory.getLogger(AltsystemSchuetze.class);


    @Autowired
    public AltsystemVerein (final AltsystemVereinMapper altsystemVereinMapper, final VereinComponent vereinComponent,
                            AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemVereinMapper = altsystemVereinMapper;
        this.vereinComponent = vereinComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }


    @Override
    public void create(AltsystemMannschaftDO altsystemDataObject, long currentUserId) {
        /**
         * Creates a new entry for the AltsystemMannschaftDO in the system.
         *
         * @param altsystemDataObject The AltsystemMannschaftDO object to be created.
         * @param currentUserId The ID of the current user performing the operation.
         */

        // wir mappen mal den Namen und den DSB Identifier aus den Alsystemdaten
        VereinDO vereinDO = new VereinDO();
        vereinDO = altsystemVereinMapper.toDO(vereinDO, altsystemDataObject);
        //parsed den Identifier

        //prüfen ob der Verein ein leerer Verein ist
        if (vereinDO.getName().contains("fehlender Verein")){
            //es ist der leere Verein- wir müssen auf unseren "Standard umlenken...
            vereinDO.setDsbIdentifier("Platzhalter");
        }
        // Schaut, ob Verein bereits vorhanden ist
        VereinDO vorhanden = null;
        try{
            vorhanden = altsystemVereinMapper.getVereinDO(vereinDO.getName(), vereinDO.getDsbIdentifier());
        }catch(Exception e){
            LOGGER.debug(String.valueOf(e));
        }
        if (  vorhanden == null ){
            //Führt mapper aus
            vereinDO = altsystemVereinMapper.addDefaultFields(vereinDO);
            //Create in Verein Tabelle
            vereinDO = vereinComponent.create(vereinDO, currentUserId);
            //Create in Uebersetzungstabele
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemDataObject.getId(), vereinDO.getId(), "");

        }else {
            //Wenn der Verein bereits vorhanden ist, wird nur in die Ueberstzungstabele geschrieben
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemDataObject.getId(), vorhanden.getId(), "");
        }

    }


    @Override
    public void update(AltsystemMannschaftDO altsystemDataObject, long currentUserId) {

        /**
         * Updates the information of the AltsystemMannschaftDO in the system.
         *
         * @param altsystemDataObject The AltsystemMannschaftDO object containing the updated information.
         * @param currentUserId The ID of the current user performing the update operation.
         * @throws BusinessException If no corresponding entry is found in the translation table for the provided AltsystemMannschaftDO ID.
         */

        //Verein in der Uebersetzungstabele suchen
        AltsystemUebersetzungDO vereinUebersetzung = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemDataObject.getId());


        if(vereinUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", altsystemDataObject.getId()));
        }
        //Verein in der Vereintabele suchen
        VereinDO vereinDO = vereinComponent.findById(vereinUebersetzung.getBogenligaId());
        //Mapper ausführen
        altsystemVereinMapper.toDO(vereinDO, altsystemDataObject);
        //Vereinstable updaten
        vereinComponent.update(vereinDO, currentUserId);

    }

}
