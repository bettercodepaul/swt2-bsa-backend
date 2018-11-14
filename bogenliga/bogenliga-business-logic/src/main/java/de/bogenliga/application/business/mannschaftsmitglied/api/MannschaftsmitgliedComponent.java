package de.bogenliga.application.business.mannschaftsmitglied.api;

import java.util.List;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface MannschaftsmitgliedComponent extends ComponentFacade {

    /**
     * Return all mannschaftsmitglied entries.
     *
     * @return list of all mannschaftsmitglied mannschaftsmitglied in the database;
     * empty list, if no mannschaftsmitglied is found
     */
    List<MannschaftsmitgliedDO> findAll();


    /**
     *
     * todo
     */

    List<MannschaftsmitgliedDO> findAllSchuetze();


    /**
     * Return a mannschaftsmitglied entry with the given id.
     *
     * @param MannschaftsmitgliedMannschaftId of the mannschaftsmitglied,
     * @param MannschaftsmitgliedMitgliedId of the mannschaftsmitglied
     * @return single mannschaftsmitglied entry with the given id;
     * null, if no mannschaftsmitglied is found
     */
    MannschaftsmitgliedDO findByMemberAndTeamId(long MannschaftsmitgliedMannschaftId, long MannschaftsmitgliedMitgliedId);



    /**
     * Create a new mannschaftsmitglied in the database.
     *
     * @param mannschaftsmitgliedDO new mannschaftsmitglied
     * @param currentMannschaftsmitgliedMannschaftId
     * @param currentMannschaftsmitgliedMitgliedId
     * @return persisted version of the mannschaftsmitglied
     */
    MannschaftsmitgliedDO create(MannschaftsmitgliedDO mannschaftsmitgliedDO, long currentMannschaftsmitgliedMannschaftId, long currentMannschaftsmitgliedMitgliedId);



    /**
     * Update an existing mannschaftsmitglied. The mannschaftsmitglied is identified by the id.
     *
     * @param mannschaftsmitgliedDO existing mannschaftsmitgliedDO to update
     * @param currentMannschaftsmitgliedMannschaftId
     * @param currentMannschaftsmitgliedMitgliedId
     * @return persisted version of the mannschaftsmitglied
     */
    MannschaftsmitgliedDO update(MannschaftsmitgliedDO mannschaftsmitgliedDO, long currentMannschaftsmitgliedMannschaftId, long currentMannschaftsmitgliedMitgliedId);


    /**
     * Delete an existing mannschaftsmitglied. The mannschaftsmitglied is identified by the id.
     *
     * @param mannschaftsmitgliedDO mannschaftsmitglied to delete
     * @param currentMannschaftsmitgliedMannschaftId
     * @param currentMannschaftsmitgliedMitgliedId
     */
    void delete(MannschaftsmitgliedDO mannschaftsmitgliedDO, long currentMannschaftsmitgliedMannschaftId, long currentMannschaftsmitgliedMitgliedId);

}
