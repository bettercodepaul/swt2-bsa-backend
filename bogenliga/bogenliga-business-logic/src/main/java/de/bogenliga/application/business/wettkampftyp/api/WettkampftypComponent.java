package de.bogenliga.application.business.wettkampftyp.api;


import java.util.List;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampftypDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the wettkampftyp database requests.
 * @Autor Marvin Holm, Daniel Schott
 */
public interface WettkampftypComponent extends ComponentFacade {

    /**
     * Return all wettkampftypentries.
     *
     * @return list of all wettkampftyp in the database;
     * empty list, if no wettkampftyp is found
     */
    List<WettkampftypDO> findAll();


    /**
     * Return a wettkampftyp entry with the given id.
     *
     * @param id of the wettkampftyp
     * @return single wettkampftyp entry with the given id;
     * null, if no wettkampftyp is found
     */
    WettkampftypDO findById(long id);

    /**
     * Create a new wettkampftyp in the database.
     *
     * @param wettkampftypDO new wettkampftyp
     * @return persisted version of the dsbmitglied
     */
    WettkampftypDO create(WettkampftypDO wettkampftypDO, long currentWettkampftypID);


    /**
     * Update an existing wettkampftyp. The wettkampftyp is identified by the id.
     *
     * @param wettkampftypDO existing wettkampftypDO to update
     * @return persisted version of the wettkampftypDO
     */
    WettkampftypDO update(WettkampftypDO wettkampftypDO, long currentWettkampftypID);


    /**
     * Delete an existing wettkampftyp. The wettkampftyp is identified by the id.
     *
     * @param wettkampftypDO wettkampftyp to delete
     */
    void delete(WettkampftypDO wettkampftypDO, long currentWettkampftypID);

}
