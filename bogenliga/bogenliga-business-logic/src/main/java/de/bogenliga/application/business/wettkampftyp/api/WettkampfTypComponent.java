package de.bogenliga.application.business.wettkampftyp.api;


import java.util.List;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the wettkampftyp database requests.
 * @Autor Marvin Holm, Daniel Schott
 */
public interface WettkampfTypComponent extends ComponentFacade {

    /**
     * Return all wettkampftypentries.
     *
     * @return list of all wettkampftyp in the database;
     * empty list, if no wettkampftyp is found
     */
    List<WettkampfTypDO> findAll();


    /**
     * Return a wettkampftyp entry with the given id.
     *
     * @param id of the wettkampftyp
     * @return single wettkampftyp entry with the given id;
     * null, if no wettkampftyp is found
     */
    WettkampfTypDO findById(long id);

    /**
     * Create a new wettkampftyp in the database.
     *
     * @param wettkampftypDO new wettkampftyp
     * @return persisted version of the dsbmitglied
     */
    WettkampfTypDO create(WettkampfTypDO wettkampftypDO, long currentWettkampftypID);


    /**
     * Update an existing wettkampftyp. The wettkampftyp is identified by the id.
     *
     * @param wettkampftypDO existing wettkampftypDO to update
     * @return persisted version of the wettkampftypDO
     */
    WettkampfTypDO update(WettkampfTypDO wettkampftypDO, long currentWettkampftypID);


    /**
     * Delete an existing wettkampftyp. The wettkampftyp is identified by the id.
     *
     * @param wettkampftypDO wettkampftyp to delete
     */
    void delete(WettkampfTypDO wettkampftypDO, long currentWettkampftypID);

}
