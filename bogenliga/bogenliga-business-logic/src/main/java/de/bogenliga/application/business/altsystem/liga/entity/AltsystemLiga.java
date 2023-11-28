package de.bogenliga.application.business.altsystem.liga.entity;

import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.common.altsystem.AltsystemEntity;

/**
 * Component to handle the import of a "Liga" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemLiga implements AltsystemEntity<AltsystemLigaDO> {

    public void create(AltsystemLigaDO altsystemDataObject){
        // Check Preconditions

        // Map data to new object, add default fields
        LigaBE ligaBE = AltsystemLigaMapper.toBE(altsystemDataObject);
        AltsystemLigaMapper.addDefaultFields(ligaBE);

        // Add data to table

        // Add to translation table

    }

    public void update(AltsystemLigaDO altsystemDataObject){
        // Check Preconditions

        // Map data to new object, don't add default fields
        AltsystemLigaMapper.toBE(altsystemDataObject);

        // Get primary key from translation table

        // Update data in table with given primary key

    }

}
