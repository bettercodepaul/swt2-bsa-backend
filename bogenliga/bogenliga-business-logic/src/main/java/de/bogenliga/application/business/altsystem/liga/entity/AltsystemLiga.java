package de.bogenliga.application.business.altsystem.liga.entity;

import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;

/**
 * Component to handle the import of a "Liga" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemLiga implements AltsystemEntity<AltsystemLigaDO> {

    private final LigaComponent ligaComponent;

    public AltsystemLiga(final LigaComponent ligaComponent) {
        this.ligaComponent = ligaComponent;
    }

    @Override
    public void create(AltsystemLigaDO altsystemDataObject){
        // Map data to new object, add default fields
        LigaDO ligaDO = new LigaDO();
        ligaDO = AltsystemLigaMapper.toDO(ligaDO, altsystemDataObject);
        ligaDO = AltsystemLigaMapper.addDefaultFields(ligaDO);

        // Add data to table
        // ligaComponent.create(ligaDO, <UserID>);

        // Add to translation table

    }

    @Override
    public void update(AltsystemLigaDO altsystemDataObject){
        // Get primary key from translation table

        // Find data in table with corresponding id
        LigaDO ligaDO = ligaComponent.findById(1);

        // Map data to new object, don't add default fields
        AltsystemLigaMapper.toDO(ligaDO, altsystemDataObject);

        // Update data in table with given primary key
        // ligaComponent.update(ligaDO, <UserID>);
    }

}
