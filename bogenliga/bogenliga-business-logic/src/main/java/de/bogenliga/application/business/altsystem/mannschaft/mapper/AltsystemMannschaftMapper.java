package de.bogenliga.application.business.altsystem.mannschaft.mapper;

import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemMannschaftMapper implements ValueObjectMapper {

    public static DsbMannschaftDO toDO(DsbMannschaftDO dsbMannschaftDO, AltsystemMannschaftDO altsystemMannschaftDO){


        return dsbMannschaftDO;
    }






}
