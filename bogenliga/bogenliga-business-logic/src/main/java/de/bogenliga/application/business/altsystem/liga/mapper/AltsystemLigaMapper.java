package de.bogenliga.application.business.altsystem.liga.mapper;

import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * Provides functionality to map the bean of Bogenliga.de to a
 * Business Entity (BE) of the new application
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemLigaMapper implements ValueObjectMapper {
    public static LigaBE toBE(AltsystemLigaDO altsystemLigaDO){
        LigaBE ligaBE = new LigaBE();
        ligaBE.setLigaId(altsystemLigaDO.getId());
        ligaBE.setLigaName(altsystemLigaDO.getName());
        ligaBE.setLigaUebergeordnetId(200L);
        return ligaBE;
    }

    public static LigaBE addDefaultFields(LigaBE ligaBE){
        ligaBE.setLigaVerantwortlichId(1L);
        ligaBE.setLigaRegionId(1000L);
        ligaBE.setLigaDisziplinId(0L);
        return ligaBE;
    }

    private AltsystemLigaMapper(){}
}
