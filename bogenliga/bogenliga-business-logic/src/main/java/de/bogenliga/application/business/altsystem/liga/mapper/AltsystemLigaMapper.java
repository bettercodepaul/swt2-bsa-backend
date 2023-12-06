package de.bogenliga.application.business.altsystem.liga.mapper;

import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * Provides functionality to map the bean of Bogenliga.de to a
 * Business Entity (BE) of the new application
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemLigaMapper implements ValueObjectMapper {
    public static LigaDO toDO(LigaDO ligaDO, AltsystemLigaDO altsystemLigaDO){
        ligaDO.setName(altsystemLigaDO.getName());
        ligaDO.setLigaUebergeordnetId(200L);
        return ligaDO;
    }

    public static LigaDO addDefaultFields(LigaDO ligaDO) {
        ligaDO.setLigaVerantwortlichId(1L);
        ligaDO.setRegionId(1000L);
        ligaDO.setDisziplinId(0L);
        return ligaDO;
    }

    private AltsystemLigaMapper(){}
}
