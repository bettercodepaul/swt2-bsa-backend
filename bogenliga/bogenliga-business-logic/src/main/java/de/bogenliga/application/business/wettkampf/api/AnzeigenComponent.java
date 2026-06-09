package de.bogenliga.application.business.wettkampf.api;

import java.util.List;
import de.bogenliga.application.business.wettkampf.api.types.AnzeigenDO;

public interface AnzeigenComponent {

    /**
     * Return all Anzeigen entries.
     *
     * @return list of all anzeigen in the database; empty list, if no anzeige is found
     */
    List<AnzeigenDO> findAll();


    /**
     * Return a single anzeige by unique id
     *
     * @return single anzeigenDO
     */

    AnzeigenDO findById(Long id);

    List<AnzeigenDO> findByVeranstaltungsId(Long veranstaltungsId);

    /**
     * Create a new anzeige in the database.
     *
     * @param anzeigenDO the new anzeigenDO
     * @param currentUserId the id of the creating user
     *
     * @return persisted version of the anzeige
     */
    AnzeigenDO create(AnzeigenDO anzeigenDO, final Long currentUserId);

    /**
     * Update an existing anzeige. The anzeige is identified by the id's set in anzeigenDO.
     *
     * @param anzeigenDO existing anzeigenDO to update
     * @param currentMemberId id of the member currently updating the anzeige
     *
     * @return persisted version of the match
     */
    AnzeigenDO update(AnzeigenDO anzeigenDO, Long currentMemberId);


    /**
     * Delete an existing anzeige. The anzeige is identified by the id's set in anzeigenDO.
     *
     * @param anzeigenDO anzeige to delete
     * @param currentMemberId id of the member currently updating the anzeige
     */
    void delete(AnzeigenDO anzeigenDO, Long currentMemberId);

    /**
     * Generate a randomized, alphanumeric four character id.
     * @return The generated id.
     */
    public String generatePhysischeBildschirmId();
}


