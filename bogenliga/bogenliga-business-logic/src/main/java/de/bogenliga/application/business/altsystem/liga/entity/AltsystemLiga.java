package de.bogenliga.application.business.altsystem.liga.entity;

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

    @Autowired
    public AltsystemLiga(final AltsystemLigaMapper altsystemLigaMapper, final LigaComponent ligaComponent) {
        this.altsystemLigaMapper = altsystemLigaMapper;
        this.ligaComponent = ligaComponent;
    }

    @Override
    public void create(AltsystemLigaDO altsystemDataObject, long currentUserId){
        // Map data to new object, add default fields
        LigaDO ligaDO = new LigaDO();
        ligaDO = altsystemLigaMapper.toDO(ligaDO, altsystemDataObject);
        ligaDO = altsystemLigaMapper.addDefaultFields(ligaDO, currentUserId);

        // Add data to table
        ligaComponent.create(ligaDO, currentUserId);

        // Add to translation table
        AltsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Liga_Liga, (int) altsystemDataObject.getId(), ligaDO.getId().intValue(), "");
    }

    @Override
    public void update(AltsystemLigaDO altsystemDataObject, long currentUserId){
        // Get primary key from translation table
        AltsystemUebersetzungDO ligaUebersetzung = AltsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Liga_Liga, altsystemDataObject.getId());

        // Check if the translation data has been found
        if(ligaUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", altsystemDataObject.getId()));
        }

        // Find data in table with corresponding id
        LigaDO ligaDO = ligaComponent.findById(ligaUebersetzung.getBogenliga_id());

        // Map data to new object, don't add default fields
        altsystemLigaMapper.toDO(ligaDO, altsystemDataObject);

        // Update data in table with given primary key
        ligaComponent.update(ligaDO, currentUserId);
    }

}
