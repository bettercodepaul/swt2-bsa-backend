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
     * @return list of all dsbmitglied in the database;
     * empty list, if no dsbmitglied is found
     */
    List<DsbMitgliedDO> findAll();


    /**
     * Retrun all dsbmitglied entries with the given teamId.
     *
     * @return list of all dsbmitlgied entries with the given id.
     */
    List<DsbMitgliedDO> findAllByTeamId(long id);


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
     * and create an lizens if needed
     *
     * @param dsbMitgliedDO new dsbmitglied
     * @return persisted version of the dsbmitglied
     */
    DsbMitgliedDO create(DsbMitgliedDO dsbMitgliedDO, long currentDsbMitgliedId);


    /**
     * Update an existing dsbmitglied. The dsbmitglied is identified by the id.
     * and check if a lizenz is given or nor
     *
     * @param dsbMitgliedDO existing dsbMitgliedDO to update
     * @return persisted version of the dsbmitglied
     */
    DsbMitgliedDO update(DsbMitgliedDO dsbMitgliedDO, long currentDsbMitgliedId);


    /**
     * Delete an existing dsbmitglied. The dsbmitglied is identified by the id.
     * and the lizenz if one exists
     *
     * @param dsbMitgliedDO dsbmitglied to delete
     */
    void delete(DsbMitgliedDO dsbMitgliedDO, long currentDsbMitgliedId);
}
