package de.bogenliga.application.business.dsbMember.api;

import java.util.List;
import de.bogenliga.application.business.dsbMember.api.types.DsbMemberDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the dsbMember database requests.
 */
public interface DsbMemberComponent extends ComponentFacade {

    /**
     * Return all dsbMember entries.
     *
     * @return list of all dsbMember dsbMember in the database;
     * empty list, if no dsbMember is found
     */
    List<DsbMemberDO> findAll();


    /**
     * Return a dsbMember entry with the given id.
     *
     * @param id of the dsbMember
     * @return single dsbMember entry with the given id;
     * null, if no dsbMember is found
     */
    DsbMemberDO findById(int id);

    /**
     * Create a new dsbMember in the database.
     *
     * @param dsbMemberDO new dsbMember
     * @return persisted version of the dsbMember
     */
    DsbMemberDO create(DsbMemberDO dsbMemberDO, long currentDsbMemberId);


    /**
     * Update an existing dsbMember. The dsbMember is identified by the id.
     *
     * @param dsbMemberDO existing dsbMemberDO to update
     * @return persisted version of the dsbMember
     */
    DsbMemberDO update(DsbMemberDO dsbMemberDO, long currentDsbMemberId);


    /**
     * Delete an existing dsbMember. The dsbMember is identified by the id.
     *
     * @param dsbMemberDO dsbMember to delete
     */
    void delete(DsbMemberDO dsbMemberDO, long currentDsbMemberId);
}
