package de.bogenliga.application.business.disziplin.api;

import java.util.List;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;

/**
 * @author Marcel Neumann
 * @author Robin Mueller
 */
public interface DisziplinComponent {

    /**
     * Return all matches entries.
     *
     * @return list of all match in the database; empty list, if no match is found
     */
    List<DisziplinDO> findAll();


    /**
     * Return a single match by unique id
     *
     * @return single matchDO
     */

    DisziplinDO findById(Long id);

    /**
     * Create a new match in the database.
     *
     * @param matchDO       the new matchDO
     * @param currentUserId the id of the creating user
     *
     * @return persisted version of the match
     */
    public DisziplinDO create(DisziplinDO matchDO, final Long currentUserId);

    /**
     * Update an existing match. The match is identified by the id's set in matchDO.
     *
     * @param matchDO         existing matchDO to update
     * @param currentMemberId id of the member currently updating the match
     *
     * @return persisted version of the match
     */
    DisziplinDO update(DisziplinDO matchDO, Long currentMemberId);


    /**
     * Delete an existing match. The match is identified by the id's set in matchDO.
     *
     * @param matchDO         match to delete
     * @param currentMemberId id of the member currently updating the match
     */
    void delete(DisziplinDO matchDO, Long currentMemberId);
}
