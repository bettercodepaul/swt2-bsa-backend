package de.bogenliga.application.business.dsbmannschaft.api;

import de.bogenliga.application.business.dsbmannschaft.api.types.MannschaftDO;
import de.bogenliga.application.common.component.ComponentFacade;
import java.util.List;

/**
 * Responsible for the dsbMannschaft database requests.
 */

public interface MannschaftComponent extends ComponentFacade {


    /**
     * Return all dsbmannschaft entries.
     *
     * @return list of all dsbmannschaft dsbmannschaft in the database;
     * empty list, if no dsbmannschaft is found
     */
    List<MannschaftDO> findAll();


    /**
     * Return all dsbmannschaft entries with the given vereinsId.
     *
     * @param id of the verein
     * @return all dsbmannschaft entries with the given vereinsId.
     * null, if no dsbmannschaft is found.
     */

    List<MannschaftDO> findAllByVereinsId(long id);


    /**
     * Return all dsbmannschaft entries with the given Veranstaltungs-Id.
     *
     * @param id of the Veranstaltung
     * @return all dsbmannschaft entries with the given Veranstaltungs-Id.
     * null, if no dsbmannschaft is found.
     */

    List<MannschaftDO> findAllByVeranstaltungsId(long id);


    /**
     * Return a dsbmannschaft entry with the given id.
     *
     * @param id of the dsbmannschaft
     * @return single dsbmannschaft entry with the given id;
     * null, if no dsbmannschaft is found
     */

    MannschaftDO findById(long id);


    /**
     * Create a new dsbmannschaft in the database.
     *
     * @param mannschaftDO new dsbmannschaft
     * @return persisted version of the dsbmannschaft
     */

    MannschaftDO create(MannschaftDO mannschaftDO, long currentDsbMannschaftId);


    /**
     * Update an existing dsbmannschaft. The dsbmannschaft is identified by the id.
     *
     * @param dsbMannschaftdDO existing dsbMannschaftdDO to update
     * @return persisted version of the dsbmannschaft
     */

    MannschaftDO update(MannschaftDO dsbMannschaftdDO, long currentDsbMannschaftId);


    /**
     * Delete an existing dsbmannschaft. The dsbmannschaft is identified by the id.
     *
     * @param mannschaftDO dsbmannschaft to delete
     */

    void delete(MannschaftDO mannschaftDO, long currentDsbMitgliedId);

    /**
     * Copys the Mannschaften of an old Veranstaltung into a new Veranstaltung
     *
     * @param lastVeranstaltungsId
     * @param currentVeranstaltungsId
     * @param userId
     * @return
     */
    List<MannschaftDO> copyMannschaftFromVeranstaltung(final long lastVeranstaltungsId, final long currentVeranstaltungsId, final long userId);
}
