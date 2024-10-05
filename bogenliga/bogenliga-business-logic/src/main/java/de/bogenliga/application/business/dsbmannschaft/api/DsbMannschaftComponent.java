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
     * Return all dsbmannschaft entries with the given Veranstaltungs-Id.
     *
     * @param id of the Veranstaltung
     * @return all dsbmannschaft entries with the given Veranstaltungs-Id.
     * null, if no dsbmannschaft is found.
     */

    List<DsbMannschaftDO> findAllByVeranstaltungsId(long id);


    /**
     * Return all dsbmannschaft entries with the given Wettkampf-Id.
     *
     * @param id of th eWettkampf
     * @return all dsbmannschaft entries with the given Wettkampf-Id.
     * null, if no dsbmannschaft is found.
     */

    List<DsbMannschaftDO> findAllByWettkampfId(long id);


    /**
     * Return a dsbmannschaft entry with the given id.
     *
     * @param id of the dsbmannschaft
     * @return single dsbmannschaft entry with the given id;
     * null, if no dsbmannschaft is found
     */

    DsbMannschaftDO findById(long id);
    List<DsbMannschaftDO> findVeranstaltungAndWettkampfByID(long id);

    /**
     * Create a new dsbmannschaft in the database.
     *
     * @param dsbMannschaftDO new dsbmannschaft
     * @return persisted version of the dsbmannschaft
     */

    DsbMannschaftDO create(DsbMannschaftDO dsbMannschaftDO, long userId);


    /**
     * Update an existing dsbmannschaft. The dsbmannschaft is identified by the id.
     *
     * @param dsbMannschaftdDO existing dsbMannschaftdDO to update
     * @return persisted version of the dsbmannschaft
     */

    DsbMannschaftDO update(DsbMannschaftDO dsbMannschaftdDO, long userId);


    /**
     * Delete an existing dsbmannschaft. The dsbmannschaft is identified by the id.
     *
     * @param dsbMannschaftDO dsbmannschaft to delete
     */

    void delete(DsbMannschaftDO dsbMannschaftDO, Long userId);

    /**
     * Copys the Mannschaften of an old Veranstaltung into a new Veranstaltung
     *
     * @param lastVeranstaltungsId
     * @param currentVeranstaltungsId
     * @param userId
     * @return
     */

    List<DsbMannschaftDO> copyMannschaftFromVeranstaltung(final long lastVeranstaltungsId, final long currentVeranstaltungsId, final long userId);

    /**
     * Return all dsbmannschaft entries that are currently in the waiting queue.
     *
     * @return all dsbmannschaft entries in the waiting queue.
     * null, if no dsbmannschaft is found.
     */

    List<DsbMannschaftDO> findAllByWarteschlange();

    /**
     * Return all dsbmannschaft entries with the given name.
     *
     * @return all dsbmannschaft entries with the given name
     * null, if no dsbmannschaft is found.
     */

    List<DsbMannschaftDO> findAllByName(String name);
    /**
     * Copys the Mitglieder of an old Mannschaft into a new Mannschaft
     *
     * @param oldMannschaftsID
     * @param newMannschaftsID
     * @param userId
     */
    void copyMitgliederFromMannschaft(long oldMannschaftsID, long newMannschaftsID, long userId);
}
