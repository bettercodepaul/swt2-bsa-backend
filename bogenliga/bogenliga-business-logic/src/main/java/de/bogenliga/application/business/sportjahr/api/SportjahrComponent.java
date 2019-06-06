package de.bogenliga.application.business.sportjahr.api;


import java.util.List;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the sportjahr database requests.
 *
 * @author Marcel Schneider
 */
public interface SportjahrComponent extends ComponentFacade {

    /**
     * Return all sportjahrentriesw.
     *
     * @return list of all sportjahr in the database; empty list, if no sportjahr is found
     */
    List<SportjahrDO> findAll();


    /**
     * Return a sportjahr entry with the given id.
     *
     * @param id of the sportjahr
     *
     * @return single sportjahr entry with the given id; null, if no sportjahr is found
     */
    SportjahrDO findById(long id);

    /**
     * Create a new sportjahr in the database.
     *
     * @param sportjahrDO new sportjahr
     *
     * @return persisted version of the dsbmitglied
     */
    SportjahrDO create(SportjahrDO sportjahrDO, long currentSportjahrID);


    /**
     * Update an existing sportjahr. The sportjahr is identified by the id.
     *
     * @param sportjahrDO existing sportjahrDO to update
     *
     * @return persisted version of the sportjahrDO
     */
    SportjahrDO update(SportjahrDO sportjahrDO, long currentSportjahrID);


    /**
     * Delete an existing sportjahr. The sportjahr is identified by the id.
     *
     * @param sportjahrDO sportjahr to delete
     */
    void delete(SportjahrDO sportjahrDO, long currentSportjahrID);

}
