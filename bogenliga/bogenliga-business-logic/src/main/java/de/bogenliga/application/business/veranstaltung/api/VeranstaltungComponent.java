package de.bogenliga.application.business.veranstaltung.api;

import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;

import java.util.List;

/**
 * Responsible for the veranstaltung database requests.
 */
public interface VeranstaltungComponent {

    /**
     * Return all veranstaltung entries.
     *
     * @return list of all veranstaltung in the database;
     * empty list, if no veranstaltung is found
     */
    List<VeranstaltungDO> findAll();
    /**
     * Return a veranstaltung entry with the given id.
     *
     * @param id of the veranstaltung
     * @return single veranstaltung entry with the given id;
     * null, if no veranstaltung is found
     */
    VeranstaltungDO findById(long id);

    /**
     * Create a new veranstaltung in the database.
     *
     * @param veranstaltungDO new veranstaltung
     * @return persisted version of the veranstaltung
     */
    VeranstaltungDO create(VeranstaltungDO veranstaltungDO, long currentVeranstaltungId);


    /**
     * Update an existing veranstaltung. The veranstaltung is identified by the id.
     *
     * @param veranstaltungDO existing veranstaltungDO to update
     * @return persisted version of the veranstaltung
     */
    VeranstaltungDO update(VeranstaltungDO veranstaltungDO, long currentVeranstaltungId);


    /**
     * Delete an existing veranstaltung. The veranstaltung is identified by the id.
     *
     * @param veranstaltungDO veranstaltung to delete
     */
    void delete(VeranstaltungDO veranstaltungDO, long currentVeranstaltungId);
}



