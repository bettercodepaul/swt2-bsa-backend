package de.bogenliga.application.business.altsystem.verein.entity;

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

        VereinDO vereinDO = new VereinDO();
        //parsed den Identifier
        String parsedIdentifier = altsystemVereinMapper.parseIdentifier(altsystemDataObject);

        VereinDO vorhanden = null;
        try{
            vorhanden = altsystemVereinMapper.getVereinDO(parsedIdentifier);
        }catch(Exception e){
            e.printStackTrace();
        }
        // Schaut, ob Verein bereits vorhanden ist
        if (vorhanden == null){
            //Führt mapper aus
            vereinDO = altsystemVereinMapper.toDO(vereinDO, altsystemDataObject);
            vereinDO = altsystemVereinMapper.addDefaultFields(vereinDO);
            //Create in Neue Tabele
            vereinComponent.create(vereinDO, currentUserId);
            //Create in Uebersetzungstabele
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Verein, (int) altsystemDataObject.getId(), vereinDO.getId().longValue(), "");

        }else {
            //Wenn der Verein bereits vorhanden ist, wird nur in die Ueberstzungstabele geschrieben
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Verein, (int) altsystemDataObject.getId(), vorhanden.getId().longValue(), "");
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
