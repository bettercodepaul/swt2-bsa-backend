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
     *
     * @return list containing all Lowest Liga in each Region
     */
    List<LigaDO> findByLowest();
    /**
     * Return a liga entry with the given id.
     *
     * @param id of the liga
     * @return single liga entry with the given id;
     * null, if no liga is found
     */
    LigaDO findById(long id);


    /**
     Return if liga exist with the given id.*
     @param id of the liga
     @return LigaDO,
     if found -> return LigaDO;
     if not found -> empty LigaDO
     */
    LigaDO checkExist(long id);




    /**
     Return if liga exist with the given liga name.*
     @param ligaName of the liga
     @return LigaDO,
     if found -> return LigaDO;
     if not found -> empty LigaDO
     */
    LigaDO checkExistsLigaName(String ligaName);



    /**
     * Create a new liga in the database.
     *
     * @param ligaDO new liga
     * @return persisted version of the liga
     */
    LigaDO create(LigaDO ligaDO, long currentDSBMitgliedId);


    /**
     * Update an existing liga. The liga is identified by the id.
     *
     * @param ligaDO existing ligaDO to update
     * @return persisted version of the liga
     */
    LigaDO update(LigaDO ligaDO, long currentDSBMitgliedId);


    /**
     * Delete an existing liga. The liga is identified by the id.
     *
     * @param ligaDO LigaDo to delete
     */
    void delete(LigaDO ligaDO, long currentDSBMitgliedId);
}


