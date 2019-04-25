package de.bogenliga.application.business.dsbmannschaft.api;

import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.common.component.ComponentFacade;
import java.util.List;

/**
 * Responsible for the dsbMannschaft database requests.
 */

public interface DsbMannschaftComponent extends ComponentFacade {


    /**
     * Return all dsbmannschaft entries.
     *
     * @return list of all dsbmannschaft dsbmannschaft in the database;
     * empty list, if no dsbmannschaft is found
     */
    List<DsbMannschaftDO> findAll();


    /**
     * Return all dsbmannschaft entries with the given vereinsId.
     *
     * @param id of the verein
     * @return all dsbmannschaft entries with the given vereinsId.
     * null, if no dsbmannschaft is found.
     */

    List<DsbMannschaftDO> findAllByVereinsId(long id);


    /**
     * Return a dsbmannschaft entry with the given id.
     *
     * @param id of the dsbmannschaft
     * @return single dsbmannschaft entry with the given id;
     * null, if no dsbmannschaft is found
     */

    DsbMannschaftDO findById(long id);


    /**
     * Create a new dsbmannschaft in the database.
     *
     * @param dsbMannschaftDO new dsbmannschaft
     * @return persisted version of the dsbmannschaft
     */

    DsbMannschaftDO create(DsbMannschaftDO dsbMannschaftDO, long currentDsbMannschaftId);


    /**
     * Update an existing dsbmannschaft. The dsbmannschaft is identified by the id.
     *
     * @param dsbMannschaftdDO existing dsbMannschaftdDO to update
     * @return persisted version of the dsbmannschaft
     */

    DsbMannschaftDO update(DsbMannschaftDO dsbMannschaftdDO, long currentDsbMannschaftId);


    /**
     * Delete an existing dsbmannschaft. The dsbmannschaft is identified by the id.
     *
     * @param dsbMannschaftDO dsbmannschaft to delete
     */

    void delete(DsbMannschaftDO dsbMannschaftDO, long currentDsbMitgliedId);



}
