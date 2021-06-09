package de.bogenliga.application.business.mannschaft.api;

import de.bogenliga.application.business.mannschaft.api.types.MannschaftDO;
import de.bogenliga.application.common.component.ComponentFacade;
import java.util.List;

/**
 * Responsible for the dsbMannschaft database requests.
 */

public interface MannschaftComponent extends ComponentFacade {


    /**
     * Return all mannschaft entries.
     *
     * @return list of all mannschaft mannschaft in the database;
     * empty list, if no mannschaft is found
     */
    List<MannschaftDO> findAll();


    /**
     * Return all mannschaft entries with the given vereinsId.
     *
     * @param id of the verein
     * @return all mannschaft entries with the given vereinsId.
     * null, if no mannschaft is found.
     */

    List<MannschaftDO> findAllByVereinsId(long id);


    /**
     * Return all mannschaft entries with the given Veranstaltungs-Id.
     *
     * @param id of the Veranstaltung
     * @return all mannschaft entries with the given Veranstaltungs-Id.
     * null, if no mannschaft is found.
     */

    List<MannschaftDO> findAllByVeranstaltungsId(long id);


    /**
     * Return a mannschaft entry with the given id.
     *
     * @param id of the mannschaft
     * @return single mannschaft entry with the given id;
     * null, if no mannschaft is found
     */

    MannschaftDO findById(long id);


    /**
     * Create a new mannschaft in the database.
     *
     * @param mannschaftDO new mannschaft
     * @return persisted version of the mannschaft
     */

    MannschaftDO create(MannschaftDO mannschaftDO, long currentDsbMannschaftId);


    /**
     * Update an existing mannschaft. The mannschaft is identified by the id.
     *
     * @param dsbMannschaftdDO existing dsbMannschaftdDO to update
     * @return persisted version of the mannschaft
     */

    MannschaftDO update(MannschaftDO dsbMannschaftdDO, long currentDsbMannschaftId);


    /**
     * Delete an existing mannschaft. The mannschaft is identified by the id.
     *
     * @param mannschaftDO mannschaft to delete
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
