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

    List<MannschaftsmitgliedDO> findAllSchuetzeInTeam(long MannschaftsmitgliederMannschaftsId);



    /**
     * Return all mannschaftsmitglied entries from one team.
     *
     * @return list of all mannschaftsmitglied mannschaftsmitglied in the database;
     * empty list, if no mannschaftsmitglied is found
     */

    List<MannschaftsmitgliedDO> findByTeamId(long MannschaftsmitgliederMannschaftsId);



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
     * @param currentMemberId
     * @return persisted version of the mannschaftsmitglied
     */
    MannschaftsmitgliedDO create(MannschaftsmitgliedDO mannschaftsmitgliedDO, long currentMemberId);



    /**
     * Update an existing mannschaftsmitglied. The mannschaftsmitglied is identified by the id.
     *
     * @param mannschaftsmitgliedDO existing mannschaftsmitgliedDO to update
     * @param currentMemberId
     * @return persisted version of the mannschaftsmitglied
     */
    MannschaftsmitgliedDO update(MannschaftsmitgliedDO mannschaftsmitgliedDO, long currentMemberId);


    /**
     * Delete an existing mannschaftsmitglied. The mannschaftsmitglied is identified by the id.
     *
     * @param mannschaftsmitgliedDO mannschaftsmitglied to delete
     * @param currentMemberId
     */
    void delete(MannschaftsmitgliedDO mannschaftsmitgliedDO, long currentMemberId);

    boolean checkExistingSchuetze(long teamId, long memberId);

}
