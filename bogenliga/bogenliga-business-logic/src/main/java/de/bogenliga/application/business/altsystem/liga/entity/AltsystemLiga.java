package de.bogenliga.application.business.altsystem.liga.entity;

import de.bogenliga.application.business.altsystem.schuetze.entity.AltsystemSchuetze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;


/**
 * Component to handle the import of a "Liga" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemLiga implements AltsystemEntity<AltsystemLigaDO> {

    private final AltsystemLigaMapper altsystemLigaMapper;
    private final LigaComponent ligaComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;

    private static final Logger LOGGER = LoggerFactory.getLogger(AltsystemSchuetze.class);

    @Autowired
    public AltsystemLiga(final AltsystemLigaMapper altsystemLigaMapper, final LigaComponent ligaComponent,
                         AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemLigaMapper = altsystemLigaMapper;
        this.ligaComponent = ligaComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    /**
     Create a new liga with given data of the legacy system.*
     @param altsystemLigaDO data of the legacy system
     @param currentUserId id of the user starting the synchronization
     */
    @Override
    public void create(AltsystemLigaDO altsystemLigaDO, long currentUserId){
        // Map data to new object, add default fields
        LigaDO ligaDO = new LigaDO();
        ligaDO = altsystemLigaMapper.toDO(ligaDO, altsystemLigaDO);
        ligaDO = altsystemLigaMapper.addDefaultFields(ligaDO, currentUserId);


        // Schaut, ob Verein bereits vorhanden ist
        LigaDO vorhanden = null;
        try{
            vorhanden = ligaComponent.checkExistsLigaName(ligaDO.getName());
        }catch(Exception e){
            LOGGER.debug(String.valueOf(e));
        }
        if (  vorhanden == null || vorhanden.getId() == null ){
            //neue Liga anlegen
            ligaDO = ligaComponent.create(ligaDO, currentUserId);
            // Add to translation table
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Liga_Liga, altsystemLigaDO.getId(), ligaDO.getId(), "");

        }else {
            //Wenn der Verein bereits vorhanden ist, wird nur in die Ueberstzungstabele geschrieben
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Liga_Liga, altsystemLigaDO.getId(), vorhanden.getId(), "");
        }

        // Add data to table
    }

    /**
     Update existing Liga with given data of the legacy system.*
     @param altsystemLigaDO data of the legacy system
     @param currentUserId id of the user starting the synchronization
     */
    @Override
    public void update(AltsystemLigaDO altsystemLigaDO, long currentUserId){

        // Get primary key from translation table
        AltsystemUebersetzungDO ligaUebersetzung = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Liga_Liga, altsystemLigaDO.getId());

        // Check if the translation data has been found
        if(ligaUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ligaUebersetzung in update altsystem-ID '%s'", altsystemLigaDO.getId()));
        }

        // Find data in table with corresponding id
        LigaDO ligaDO = ligaComponent.findById(ligaUebersetzung.getBogenligaId());

        // Map data to new object, don't add default fields
        ligaDO = altsystemLigaMapper.toDO(ligaDO, altsystemLigaDO);

        // Update data in table with given primary key
        ligaComponent.update(ligaDO, currentUserId);
    }

}
