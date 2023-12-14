package de.bogenliga.application.business.altsystem.liga.mapper;

import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * Provides functionality to map the bean of Bogenliga.de to a
 * Business Entity (BE) of the new application
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemLigaMapper implements ValueObjectMapper {
    public static LigaDO toDO(LigaDO ligaDO, AltsystemLigaDO altsystemLigaDO){
        String ligaName = altsystemLigaDO.getName();
        long ligaUebergeordnetId = altsystemLigaDO.getIdNextLiga();
        long disziplinId;


        ligaDO.setName(ligaName);

        // Id muss noch übersetzt werden
        ligaDO.setLigaUebergeordnetId(ligaUebergeordnetId);

        // Checken ob es eine Recurve oder Compound Liga ist
        if(ligaName.toLowerCase().contains("comp")){
            disziplinId = 1;
        }
        else{
            disziplinId = 0;
        }
        ligaDO.setDisziplinId(disziplinId);

        return ligaDO;
    }

    public static LigaDO addDefaultFields(LigaDO ligaDO) {
        // ID vom Admin User herausfinden
        ligaDO.setLigaVerantwortlichId(1L);
        // ID von DSB Region herausfinden
        ligaDO.setRegionId(1000L);
        return ligaDO;
    }

    private AltsystemLigaMapper(){}
}
