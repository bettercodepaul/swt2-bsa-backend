package de.bogenliga.application.business.altsystem.wettkampfdaten.mapper;

import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject.AltsystemWettkampfdatenDO;
import de.bogenliga.application.business.match.api.types.MatchDO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemWettkampfdatenMapper {

    public MatchDO toDO(MatchDO matchDO, AltsystemWettkampfdatenDO altsystemDataObject) {
        return new MatchDO();
    }


    public MatchDO addDefaultFields(MatchDO matchDO) {
        return new MatchDO();
    }
}
