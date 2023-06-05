package de.bogenliga.application.business.liga.api;

import java.util.List;
import de.bogenliga.application.business.liga.api.types.LigaDO;

public interface LigaComponent {
    /**
     * Return all liga entries.
     *
     * @return list of all liga in the database;
     * empty list, if no liga is found
     */
    List<LigaDO> findAll();

    List<LigaDO> findBySearch(final String searchTerm);

    /**
     * Return a liga entry with the given id.
     *
     * @param id of the liga
     * @return single liga entry with the given id;
     * null, if no liga is found
     */
    LigaDO findById(long id);

    /**
     * Returns a liga entry of the given id
     * or empty liga if no liga entry exists
     *
     * @param id id of the klasse to be returned
     *
     * @return returns a klasse
     */
    LigaDO checkExists(long id);


    /**
     * Create a new liga in the database.
     *
     * @param ligaDO new liga
     * @return persisted version of the liga
     */
    LigaDO create(LigaDO ligaDO, long currentLigaId);


    /**
     * Update an existing liga. The liga is identified by the id.
     *
     * @param ligaDO existing ligaDO to update
     * @return persisted version of the liga
     */
    LigaDO update(LigaDO ligaDO, long currentLigaId);


    /**
     * Delete an existing liga. The liga is identified by the id.
     *
     * @param ligaDO LigaDo to delete
     */
    void delete(LigaDO ligaDO, long currentLigaId);
}


