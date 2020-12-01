package de.bogenliga.application.business.einstellungen.api;

import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.common.component.ComponentFacade;
import java.util.List;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface EinstellungenComponent extends ComponentFacade {

    /**
     * Return all einstellungen entries
     *
     * @return list of all einstellungen in the database
     * empty list, if no einstellungen is found
     */
    List<EinstellungenDO> findAll();


    /**
     * Update an existing Einstellungen. The Einstellungen is identified by the id.
     *
     * @param einstellungenDO existing EinstellungenDO to update
     * @return persisted version of the Einstellungen
     */
    EinstellungenDO update(EinstellungenDO einstellungenDO, long currentEinstellungenId);

    /**
     * Create a new Einstellungen in the database.
     *
     * @param einstellungenDO new Einstellungen
     * @return persisted version of the einstellungen
     */
    EinstellungenDO create(EinstellungenDO einstellungenDO, long currentEinstellungenId);

    /**
     * Delete an existing einstellungen. The einstellungen is identified by the id.
     *
     * @param einstellungenDO einstellungen to delete
     */
    void delete(EinstellungenDO einstellungenDO, long currentEinstellungenId);

}
