package de.bogenliga.application.business.dsbmitglied.api;

import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

/**
 * Responsible for the dsbmitglied database requests.
 */
public interface DsbMitgliedComponent extends ComponentFacade {

    /**
     * Return all dsbmitglied entries.
     *
     * @return list of all dsbmitglied dsbmitglied in the database;
     * empty list, if no dsbmitglied is found
     */
    List<DsbMitgliedDO> findAll();


    /**
     * Return a dsbmitglied entry with the given id.
     *
     * @param id of the dsbmitglied
     * @return single dsbmitglied entry with the given id;
     * null, if no dsbmitglied is found
     */
    DsbMitgliedDO findById(long id);

    /**
     * Create a new dsbmitglied in the database.
     *
     * @param dsbMitgliedDO new dsbmitglied
     * @return persisted version of the dsbmitglied
     */
    DsbMitgliedDO create(DsbMitgliedDO dsbMitgliedDO, long currentDsbMitgliedId);


    /**
     * Update an existing dsbmitglied. The dsbmitglied is identified by the id.
     *
     * @param dsbMitgliedDO existing dsbMitgliedDO to update
     * @return persisted version of the dsbmitglied
     */
    DsbMitgliedDO update(DsbMitgliedDO dsbMitgliedDO, long currentDsbMitgliedId);


    /**
     * Delete an existing dsbmitglied. The dsbmitglied is identified by the id.
     *
     * @param dsbMitgliedDO dsbmitglied to delete
     */
    void delete(DsbMitgliedDO dsbMitgliedDO, long currentDsbMitgliedId);
}
