package de.bogenliga.application.business.competitionclass.api;

import java.util.List;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the Class database requests
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reuntlingen-university.de
 */

public interface CompetitionClassComponent extends ComponentFacade {

    /**
     * Return all class entries.
     * @return list of all competition classes in the database
     */
    List<CompetitionClassDO> findAll();


    //TODO Implement create request
    //TODO Implement delete request
    //TODO Implement update request
    //CompetitionClassDO create(CompetitionClassDO competitionClassDO);
    //CompetitionClassDO update(CompetitionClassDO competitionClassDO, long currentCompetitionClassId);
    //void delete(CompetitionClassDO competitionClassDO, long currentCompetitionClassid);
}
