package de.bogenliga.application.business.disziplin.api;

import java.util.List;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;

/**
 * @author Marcel Neumann
 * @author Robin Mueller
 */
public interface DisziplinComponent {

    /**
     * Return all disziplin entries.
     *
     * @return list of all disziplins in the database; empty list, if no disziplin is found
     */
    List<DisziplinDO> findAll();


    /**
     * Return a single disziplin by unique id
     *
     * @return single disziplinDO
     */

    DisziplinDO findById(Long id);

    /**
     * Create a new disziplin in the database.
     *
     * @param disziplinDO       the new disziplinDO
     * @param currentUserId the id of the creating user
     *
     * @return persisted version of the disziplin
     */
    DisziplinDO create(DisziplinDO disziplinDO, final Long currentUserId);

    /**
     * Update an existing disziplin. The disziplin is identified by the id's set in disziplinDO.
     *
     * @param disziplinDO         existing disziplinDO to update
     * @param currentMemberId id of the member currently updating the disziplin
     *
     * @return persisted version of the match
     */
    DisziplinDO update(DisziplinDO disziplinDO, Long currentMemberId);


    /**
     * Delete an existing disziplin. The disziplin is identified by the id's set in disziplinDO.
     *
     * @param disziplinDO         disziplin to delete
     * @param currentMemberId id of the member currently updating the disziplin
     */
    void delete(DisziplinDO disziplinDO, Long currentMemberId);
}
