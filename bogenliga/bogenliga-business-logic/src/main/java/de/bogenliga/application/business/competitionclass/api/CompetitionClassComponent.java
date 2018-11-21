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
     *
     * @return list of all competition classes in the database
     */
    List<CompetitionClassDO> findAll();

    /**
     * Returns a klasse entry of the given id
     *
     * @param id of the klasse entry
     *
     * @return single database entry of a klasse
     */
    CompetitionClassDO findById(final long id);


    /**
     * Create a new CommpetitionClass.
     *
     * @param competitionClassDO new competitionClass
     * @param currentDsbMitglied id of currently logged in user
     *
     * @return a new CompetitionClass
     */
    CompetitionClassDO create(CompetitionClassDO competitionClassDO, long currentDsbMitglied);


    /**
     * Update an existing competition Class. A class is identified by its ClassId
     *
     * @param competitionClassDO existing competitionClassDO to update
     * @param currentDsbMitblied id of the currently logged in user
     *
     * @return an updated version of the edited competition Class
     */
    CompetitionClassDO update(CompetitionClassDO competitionClassDO, long currentDsbMitblied);
}