package de.bogenliga.application.business.einstellungen.api;

import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.common.component.ComponentFacade;
import java.util.List;

/**
 * I contain the signatures for the functions necessary to manage configurations
 *
 * @author Fabio Care, fabio_silas.care@student.reutlingen-university.de
 */
public interface EinstellungenComponent extends ComponentFacade {

    /**
     * Return all einstellungen entries
     *
     * @return list of all einstellungen in the database empty list, if no einstellungen is found
     */
    List<EinstellungenDO> findAll();



    /**
     * Return a single einstellungen entry found by its id
     *
     * @param id        id of the entry
     * @return a single entry or empty list, if no einstellungen is found
     */
    EinstellungenDO findById(final long id);



    /**
     * Update an existing Einstellungen. The Einstellungen is identified by the id.
     *
     * @param einstellungenDO existing EinstellungenDO to update
     * @param userId          current user
     *
     * @return persisted version of the Einstellungen
     */
    EinstellungenDO update(EinstellungenDO einstellungenDO, long userId);

    /**
     * Create a new Einstellungen in the database.
     *
     * @param einstellungenDO new Einstellungen
     * @param userId          current user
     *
     * @return persisted version of the einstellungen
     */
    EinstellungenDO create(EinstellungenDO einstellungenDO, long userId);

    /**
     * Delete an existing einstellungen. The einstellungen is identified by the id.
     *
     * @param einstellungenDO einstellungen to delete
     * @param userId          current user
     */
    void delete(EinstellungenDO einstellungenDO, long userId);

}
